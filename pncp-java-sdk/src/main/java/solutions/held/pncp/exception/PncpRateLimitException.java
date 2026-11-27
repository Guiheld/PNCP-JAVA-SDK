package solutions.held.pncp.exception;

import java.time.Duration;
import java.util.Optional;

/**
 * Sinaliza que a API do PNCP respondeu com HTTP 429 (Too Many Requests), indicando que o limite de requisições foi excedido.
 *
 * <p>O portal não documenta formalmente seus limites e, sob rajada, costuma responder 429 com corpo em HTML em vez de
 * JSON; por isso o corpo é preservado apenas como texto bruto e nenhuma interpretação estruturada é tentada.
 * Quando a resposta traz o cabeçalho {@code Retry-After}, seu valor é exposto em {@link #tempoEsperaSugerido()}.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public class PncpRateLimitException extends PncpClientException {

    private static final long serialVersionUID = 1L;

    /**
     * Tempo de espera sugerido pelo cabeçalho {@code Retry-After}, ou {@code null} quando não informado.
     */
    private final transient Duration tempoEsperaSugerido;

    public PncpRateLimitException(
            String mensagem, Integer codigoStatus, String corpoResposta, Duration tempoEsperaSugerido) {
        super(mensagem, codigoStatus, corpoResposta, null);
        this.tempoEsperaSugerido = tempoEsperaSugerido;
    }

    /**
     * Retorna o tempo de espera sugerido pelo servidor antes de uma nova tentativa, quando informado pelo cabeçalho
     * {@code Retry-After}.
     *
     * @return a duração sugerida, ou vazio quando o servidor não a informou
     * @since 23/11/2026
     */
    public Optional<Duration> tempoEsperaSugerido() {
        return Optional.ofNullable(this.tempoEsperaSugerido);
    }
}
