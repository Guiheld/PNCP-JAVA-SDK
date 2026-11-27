package solutions.held.pncp.spring;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades de configuração da SDK de consulta do PNCP, ligadas ao prefixo {@code pncp} no
 * {@code application.properties}/{@code application.yml}.
 *
 * <p>
 *     Todos os campos são opcionais: quando ausentes, prevalecem os padrões da própria SDK (definidos
 *     em {@code PncpClientConfig.Builder}). Preencha apenas o que quiser sobrescrever. Durações aceitam
 *     o formato do Spring (ex.: {@code 30s}, {@code 500ms}).
 * </p>
 *
 * @param urlBase endereço base da API de consulta
 * @param timeoutConexao tempo limite de conexão
 * @param timeoutResposta tempo limite de resposta por requisição
 * @param maxTentativas número máximo de tentativas por requisição
 * @param backoffInicial atraso inicial do backoff entre tentativas
 * @param backoffMaximo atraso máximo do backoff entre tentativas
 * @param multiplicadorBackoff multiplicador do backoff exponencial
 * @param vazaoPorSegundo vazão máxima de requisições por segundo (0 ou negativo desliga o limitador)
 * @param userAgent identificação enviada no cabeçalho {@code User-Agent}
 * @author Guilherme A. Held
 * @since 27/11/2026
 */
@ConfigurationProperties(prefix = "pncp")
public record PncpProperties(
        String urlBase,
        Duration timeoutConexao,
        Duration timeoutResposta,
        Integer maxTentativas,
        Duration backoffInicial,
        Duration backoffMaximo,
        Double multiplicadorBackoff,
        Double vazaoPorSegundo,
        String userAgent) {}
