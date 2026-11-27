package solutions.held.pncp.http;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import solutions.held.pncp.PncpClientConfig;
import solutions.held.pncp.exception.PncpClientException;
import solutions.held.pncp.exception.PncpException;
import solutions.held.pncp.exception.PncpRateLimitException;
import solutions.held.pncp.exception.PncpSerializationException;
import solutions.held.pncp.exception.PncpServerException;
import solutions.held.pncp.model.RespostaErroValidacao;
import solutions.held.pncp.pagina.Pagina;

/**
 * Transporte HTTP da SDK:
 * monta as requisições GET à API do PNCP, aplica limitação de vazão e novas tentativas, traduz os códigos de status em
 * exceções tipadas e desserializa as respostas.
 *
 * <p>
 *     Usa o {@link HttpClient} do JDK: por padrão, um cliente próprio construído a partir da configuração; ou, quando
 *     informado em {@link PncpClientConfig#httpClient()}, o cliente fornecido. Não há dependências externas de rede.
 *     É de uso interno da SDK e seguro para uso concorrente.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class HttpTransport {

    private static final System.Logger LOGGER = System.getLogger(HttpTransport.class.getName());

    private final PncpClientConfig config;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final Throttler throttler;

    public HttpTransport(PncpClientConfig config) {
        this.config = config;
        this.objectMapper = config.objectMapper();
        this.throttler = new Throttler(config.vazaoPorSegundo());
        this.httpClient = config.httpClient()
                .orElseGet(() -> HttpClient.newBuilder()
                        .connectTimeout(config.timeoutConexao())
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .build());
    }

    /**
     * Consulta um recurso que devolve um único objeto JSON.
     *
     * @param <T> tipo do objeto esperado
     * @param caminho caminho do recurso, relativo ao endereço base
     * @param params parâmetros de consulta
     * @param tipo classe do objeto a desserializar
     * @return o objeto desserializado, ou {@code null} quando a API responde 204 No Content
     * @since 23/11/2026
     */
    public <T> T consultarObjeto(String caminho, QueryParams params, Class<T> tipo) {
        String corpo = this.executar(this.montarRequisicao(caminho, params));
        if (corpo == null) return null;

        return this.desserializar(corpo, this.objectMapper.getTypeFactory().constructType(tipo));
    }

    /**
     * Consulta um recurso paginado e devolve uma única página de resultados.
     *
     * @param <T> tipo dos itens da página
     * @param caminho caminho do recurso, relativo ao endereço base
     * @param params parâmetros de consulta, incluindo a paginação
     * @param tipoItem classe dos itens contidos na página
     * @return a página de resultados, ou uma página vazia quando a API responde 204 No Content
     * @since 23/11/2026
     */
    public <T> Pagina<T> consultarPagina(String caminho, QueryParams params, Class<T> tipoItem) {
        String corpo = this.executar(this.montarRequisicao(caminho, params));
        if (corpo == null) return new Pagina<T>(List.of(), 0L, 0L, 0L, 0L, Boolean.TRUE);

        return this.desserializar(
                corpo, this.objectMapper.getTypeFactory().constructParametricType(Pagina.class, tipoItem));
    }

    /**
     * Monta a requisição GET com a URL completa, o tempo limite de resposta e os cabeçalhos de identificação.
     *
     * @param caminho caminho do recurso, relativo ao endereço base
     * @param params parâmetros de consulta
     * @return a requisição pronta para envio
     * @since 23/11/2026
     */
    private HttpRequest montarRequisicao(String caminho, QueryParams params) {
        String url = this.config.urlBase() + caminho;
        String queryString = params == null ? "" : params.paraQueryString();
        if (!queryString.isEmpty()) url = url + "?" + queryString;

        return HttpRequest.newBuilder(URI.create(url))
                .timeout(this.config.timeoutResposta())
                .header("Accept", "application/json")
                .header("User-Agent", this.config.userAgent())
                .GET()
                .build();
    }

    /**
     * Envia a requisição aplicando limitação de vazão e novas tentativas, e traduz o status da resposta em sucesso
     * (corpo) ou em exceção tipada.
     *
     * @param requisicao requisição a enviar
     * @return o corpo da resposta de sucesso, ou {@code null} quando o status é 204 No Content
     * @throws PncpRateLimitException quando a API responde 429 após esgotar as tentativas
     * @throws PncpServerException quando a API responde 5xx após esgotar as tentativas
     * @throws PncpClientException quando a API responde 4xx (exceto 429)
     * @throws PncpException quando a comunicação falha após esgotar as tentativas
     * @since 23/11/2026
     */
    private String executar(HttpRequest requisicao) {
        Integer maxTentativas = this.config.maxTentativas();
        PncpException ultimoErro = null;

        for (Integer tentativa = 1; tentativa <= maxTentativas; tentativa++) {
            this.throttler.adquirir();
            HttpResponse<String> resposta;

            try {
                resposta = this.httpClient.send(requisicao, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            } catch (IOException e) {
                ultimoErro = new PncpException("Falha de comunicação com a API do PNCP.", e);
                if (tentativa < maxTentativas) {
                    this.aguardarBackoff(tentativa, null);
                    continue;
                }
                throw ultimoErro;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PncpException("Consulta à API do PNCP interrompida.", e);
            }

            int status = resposta.statusCode();
            if (status == 204) return null;
            if (status >= 200 && status < 300) return resposta.body();
            if (status == 429) {
                Duration tempoEspera = this.lerRetryAfter(resposta).orElse(null);
                if (tentativa < maxTentativas) {
                    aguardarBackoff(tentativa, tempoEspera);
                    continue;
                }
                throw new PncpRateLimitException(
                        "Limite de requisições do PNCP excedido (HTTP 429).", status, resposta.body(), tempoEspera);
            }
            if (status >= 500) {
                if (tentativa < maxTentativas) {
                    this.aguardarBackoff(tentativa, null);
                    continue;
                }
                throw new PncpServerException(
                        "Erro no servidor do PNCP (HTTP " + status + ").",
                        status,
                        resposta.body(),
                        this.interpretarErro(resposta.body()));
            }
            throw new PncpClientException(
                    "Requisição rejeitada pelo PNCP (HTTP " + status + ").",
                    status,
                    resposta.body(),
                    this.interpretarErro(resposta.body()));
        }
        throw ultimoErro != null
                ? ultimoErro
                : new PncpException("Falha ao consultar o PNCP após " + maxTentativas + " tentativa(s).");
    }

    /**
     * Desserializa o corpo da resposta no tipo informado.
     *
     * @param <T> tipo esperado
     * @param corpo corpo da resposta em JSON
     * @param tipo tipo alvo da desserialização
     * @return o objeto desserializado
     * @throws PncpSerializationException quando o corpo não pode ser interpretado
     * @since 23/11/2026
     */
    private <T> T desserializar(String corpo, JavaType tipo) {
        try {
            return this.objectMapper.readValue(corpo, tipo);
        } catch (IOException e) {
            throw new PncpSerializationException("Não foi possível interpretar a resposta do PNCP.", e);
        }
    }

    /**
     * Tenta interpretar o corpo de uma resposta de erro como JSON de validação, sem lançar exceção quando o corpo não é JSON (por exemplo, HTML).
     *
     * @param corpo corpo bruto da resposta de erro
     * @return o erro de validação interpretado, ou {@code null} quando não aplicável
     * @since 23/11/2026
     */
    private RespostaErroValidacao interpretarErro(String corpo) {
        if (corpo == null || corpo.isBlank()) return null;
        try {
            return this.objectMapper.readValue(corpo, RespostaErroValidacao.class);
        } catch (IOException e) {
            LOGGER.log(
                    System.Logger.Level.DEBUG,
                    "Corpo de erro do PNCP não é JSON de validação; preservado como texto bruto.");
            return null;
        }
    }

    /**
     * Lê o cabeçalho {@code Retry-After} da resposta, quando presente e numérico.
     *
     * @param resposta resposta da API
     * @return a duração sugerida, ou vazio quando o cabeçalho é ausente ou inválido
     * @since 23/11/2026
     */
    private Optional<Duration> lerRetryAfter(HttpResponse<String> resposta) {
        return resposta.headers().firstValue("Retry-After").flatMap(HttpTransport::interpretarRetryAfter);
    }

    /**
     * Interpreta o valor do cabeçalho {@code Retry-After} expresso em segundos.
     *
     * @param valor texto do cabeçalho
     * @return a duração correspondente, ou vazio quando o valor não é um número de segundos
     * @since 23/11/2026
     */
    private static Optional<Duration> interpretarRetryAfter(String valor) {
        try {
            return Optional.of(Duration.ofSeconds(Long.parseLong(valor.trim())));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Aguarda antes de uma nova tentativa, respeitando o tempo sugerido pelo servidor quando informado, ou aplicando recuo
     * exponencial com variação aleatória (jitter).
     *
     * @param tentativa número da tentativa já realizada
     * @param tempoSugerido tempo sugerido pelo servidor, ou {@code null} para usar o recuo exponencial
     * @since 23/11/2026
     */
    private void aguardarBackoff(Integer tentativa, Duration tempoSugerido) {
        Duration espera;
        if (tempoSugerido != null) {
            espera = tempoSugerido;
        } else {
            long millis = Math.min(
                    (long) (this.config.backoffInicial().toMillis()
                            * Math.pow(this.config.multiplicadorBackoff(), tentativa - 1)),
                    this.config.backoffMaximo().toMillis());
            espera = Duration.ofMillis(millis + ThreadLocalRandom.current().nextLong(0, Math.max(1L, millis / 4 + 1)));
        }
        LOGGER.log(
                System.Logger.Level.DEBUG,
                "Aguardando {0} ms antes de nova tentativa ao PNCP (tentativa {1}).",
                espera.toMillis(),
                tentativa);
        try {
            Thread.sleep(espera.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PncpException("Espera entre tentativas ao PNCP interrompida.", e);
        }
    }
}
