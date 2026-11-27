package solutions.held.pncp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.held.pncp.exception.PncpClientException;
import solutions.held.pncp.exception.PncpRateLimitException;
import solutions.held.pncp.exception.PncpServerException;
import solutions.held.pncp.model.ContratacaoResumo;
import solutions.held.pncp.pagina.Pagina;
import solutions.held.pncp.request.ConsultaContratacaoRequest;

/**
 * Testes de ponta a ponta do transporte HTTP contra um servidor local (JDK
 * HttpServer): resposta de sucesso, 204 sem conteúdo, 429 com corpo em HTML, erro
 * de validação, erro de servidor com novas tentativas e paginação por fluxo.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
class ConsultaHttpTest {

    private HttpServer servidor;
    private int porta;
    private volatile HttpHandler manipulador;

    /**
     * Inicia um servidor HTTP local em porta efêmera antes de cada teste.
     *
     * @throws IOException se o servidor não puder ser iniciado
     */
    @BeforeEach
    void iniciarServidor() throws IOException {
        this.servidor = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        this.servidor.createContext("/", troca -> this.manipulador.handle(troca));
        this.servidor.start();
        this.porta = this.servidor.getAddress().getPort();
    }

    /**
     * Encerra o servidor HTTP local após cada teste.
     */
    @AfterEach
    void pararServidor() {
        this.servidor.stop(0);
    }

    /**
     * Cria um cliente apontado ao servidor local, sem limitação de vazão e com o
     * número de tentativas informado e recuo mínimo.
     *
     * @param maxTentativas número máximo de tentativas por requisição
     * @return um cliente configurado para o servidor local
     */
    private PncpConsultaClient clienteComTentativas(int maxTentativas) {
        PncpClientConfig config = PncpClientConfig.builder()
                .urlBase("http://localhost:" + this.porta)
                .vazaoPorSegundo((double) 0)
                .maxTentativas(maxTentativas)
                .backoffInicial(Duration.ofMillis(1))
                .backoffMaximo(Duration.ofMillis(5))
                .build();
        return PncpConsultaClient.comConfig(config);
    }

    /**
     * Cria uma requisição mínima de contratações para uso nos testes.
     *
     * @return uma requisição válida de contratações por publicação
     */
    private static ConsultaContratacaoRequest requisicaoContratacao() {
        return ConsultaContratacaoRequest.builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2), 6L)
                .build();
    }

    /**
     * Escreve a resposta no exchange com o status, corpo e tipo de conteúdo dados.
     *
     * @param troca exchange HTTP a responder
     * @param status código de status HTTP
     * @param corpo corpo da resposta
     * @param tipoConteudo valor do cabeçalho Content-Type
     * @throws IOException se a escrita falhar
     */
    private static void responder(HttpExchange troca, int status, String corpo, String tipoConteudo)
            throws IOException {
        byte[] bytes = corpo.getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().add("Content-Type", tipoConteudo);
        troca.sendResponseHeaders(status, bytes.length);
        try (OutputStream saida = troca.getResponseBody()) {
            saida.write(bytes);
        }
    }

    /**
     * Uma resposta 200 com uma página válida deve ser desserializada.
     */
    @Test
    void consultarPorPublicacao_respostaOk_retornaPagina() {
        this.manipulador = troca -> responder(
                troca,
                200,
                "{\"data\":[{\"numeroControlePNCP\":\"A\"}],\"totalRegistros\":1,\"totalPaginas\":1,"
                        + "\"numeroPagina\":1,\"paginasRestantes\":0,\"empty\":false}",
                "application/json");
        Pagina<ContratacaoResumo> pagina =
                clienteComTentativas(1).contratacoes().consultarPorPublicacao(requisicaoContratacao(), 1);
        assertEquals(1, pagina.itens().size());
        assertEquals("A", pagina.itens().get(0).numeroControlePNCP());
        assertEquals(1L, pagina.totalRegistros());
    }

    /**
     * Uma resposta 204 deve resultar em página vazia, sem erro.
     */
    @Test
    void consultarPorPublicacao_semConteudo_retornaPaginaVazia() {
        this.manipulador = troca -> {
            troca.sendResponseHeaders(204, -1);
            troca.close();
        };
        Pagina<ContratacaoResumo> pagina =
                clienteComTentativas(1).contratacoes().consultarPorPublicacao(requisicaoContratacao(), 1);
        assertTrue(pagina.itensSeguros().isEmpty());
        assertEquals(Boolean.TRUE, pagina.vazia());
    }

    /**
     * Um 429 com corpo em HTML deve virar {@link PncpRateLimitException}, com o
     * corpo preservado e sem tentativa de interpretação como JSON.
     */
    @Test
    void consultar_rateLimitComHtml_lancaRateLimit() {
        this.manipulador = troca -> responder(troca, 429, "<html><body>Too Many Requests</body></html>", "text/html");
        PncpRateLimitException excecao = assertThrows(
                PncpRateLimitException.class,
                () -> clienteComTentativas(1).contratacoes().consultarPorPublicacao(requisicaoContratacao(), 1));
        assertEquals(429, excecao.codigoStatus());
        assertTrue(excecao.corpoResposta().contains("Too Many Requests"));
    }

    /**
     * Um 400 com JSON de validação deve virar {@link PncpClientException} com o
     * erro estruturado disponível.
     */
    @Test
    void consultar_erro400ComJson_lancaClientException() {
        this.manipulador = troca -> responder(
                troca,
                400,
                "{\"message\":\"parametro invalido\",\"path\":\"/v1/contratacoes/publicacao\","
                        + "\"timestamp\":\"2024-01-01\",\"status\":\"400\",\"error\":\"Bad Request\"}",
                "application/json");
        PncpClientException excecao = assertThrows(
                PncpClientException.class,
                () -> clienteComTentativas(1).contratacoes().consultarPorPublicacao(requisicaoContratacao(), 1));
        assertEquals(400, excecao.codigoStatus());
        assertTrue(excecao.erroValidacao().isPresent());
        assertEquals("parametro invalido", excecao.erroValidacao().get().message());
    }

    /**
     * Um 500 deve ser repetido até o limite de tentativas e então lançar {@link
     * PncpServerException}.
     */
    @Test
    void consultar_erro500_repeteELanca() {
        AtomicInteger chamadas = new AtomicInteger();
        this.manipulador = troca -> {
            chamadas.incrementAndGet();
            responder(troca, 500, "erro interno", "text/plain");
        };
        assertThrows(
                PncpServerException.class,
                () -> clienteComTentativas(2).contratacoes().consultarPorPublicacao(requisicaoContratacao(), 1));
        assertEquals(2, chamadas.get());
    }

    /**
     * O fluxo de paginação deve percorrer todas as páginas automaticamente.
     */
    @Test
    void streamPorPublicacao_percorreVariasPaginas() {
        AtomicInteger chamadas = new AtomicInteger();
        this.manipulador = troca -> {
            chamadas.incrementAndGet();
            String query = troca.getRequestURI().getQuery();
            boolean segundaPagina = query != null && query.contains("pagina=2");
            String corpo = segundaPagina
                    ? "{\"data\":[{\"numeroControlePNCP\":\"B\"}],\"totalRegistros\":2,\"totalPaginas\":2,"
                            + "\"numeroPagina\":2,\"paginasRestantes\":0,\"empty\":false}"
                    : "{\"data\":[{\"numeroControlePNCP\":\"A\"}],\"totalRegistros\":2,\"totalPaginas\":2,"
                            + "\"numeroPagina\":1,\"paginasRestantes\":1,\"empty\":false}";
            responder(troca, 200, corpo, "application/json");
        };
        List<String> numeros = clienteComTentativas(1)
                .contratacoes()
                .streamPorPublicacao(requisicaoContratacao())
                .map(ContratacaoResumo::numeroControlePNCP)
                .collect(Collectors.toList());
        assertEquals(List.of("A", "B"), numeros);
        assertEquals(2, chamadas.get());
    }

    /**
     * Um {@link HttpClient} próprio informado na configuração deve ser de fato usado pelo transporte: um cliente que
     * não segue redirecionamentos faz o 302 aflorar como erro de cliente, enquanto o cliente padrão da SDK (que segue
     * redirecionamentos) o seguiria.
     */
    /**
     * Instala um manipulador que responde 302 (com {@code Location} para {@code /destino}) em qualquer caminho, exceto
     * {@code /destino}, que responde 200 com uma página vazia. Devolve o contador de acessos ao servidor.
     *
     * @return o contador de acessos ao servidor
     */
    private AtomicInteger instalarRedirecionamento() {
        AtomicInteger chamadas = new AtomicInteger();
        this.manipulador = troca -> {
            chamadas.incrementAndGet();
            if (troca.getRequestURI().getPath().endsWith("/destino")) {
                responder(troca, 200, "{\"data\":[],\"empty\":true}", "application/json");
            } else {
                troca.getResponseHeaders().add("Location", "http://localhost:" + this.porta + "/destino");
                troca.sendResponseHeaders(302, -1);
                troca.close();
            }
        };
        return chamadas;
    }

    /**
     * Um {@link HttpClient} próprio informado na configuração deve ser de fato usado pelo transporte: um cliente que
     * não segue redirecionamentos faz o 302 aflorar como erro de cliente e acessa o servidor uma única vez —
     * comportamento que o cliente padrão da SDK (que segue redirecionamentos) não produziria.
     */
    @Test
    void httpClientPersonalizado_eUsadoPeloTransporte() {
        AtomicInteger chamadas = instalarRedirecionamento();
        HttpClient semRedirecionamento = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
        PncpClientConfig config = PncpClientConfig.builder()
                .urlBase("http://localhost:" + this.porta)
                .vazaoPorSegundo((double) 0)
                .maxTentativas(1)
                .httpClient(semRedirecionamento)
                .build();
        PncpConsultaClient cliente = PncpConsultaClient.comConfig(config);

        PncpClientException excecao = assertThrows(
                PncpClientException.class,
                () -> cliente.contratacoes().consultarPorPublicacao(requisicaoContratacao(), 1));
        assertEquals(302, excecao.codigoStatus());
        assertEquals(1, chamadas.get());
    }

    /**
     * Sem {@link HttpClient} informado, o cliente padrão da SDK segue o redirecionamento 302 até o destino 200 — o
     * oposto do teste com cliente próprio, provando que a escolha do cliente altera o resultado observável.
     */
    @Test
    void semHttpClientInformado_clientePadraoSegueRedirecionamento() {
        AtomicInteger chamadas = instalarRedirecionamento();

        Pagina<ContratacaoResumo> pagina =
                clienteComTentativas(1).contratacoes().consultarPorPublicacao(requisicaoContratacao(), 1);

        assertTrue(pagina.itensSeguros().isEmpty());
        assertEquals(2, chamadas.get());
    }
}
