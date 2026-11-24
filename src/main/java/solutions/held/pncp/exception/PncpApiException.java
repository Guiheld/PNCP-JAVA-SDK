package solutions.held.pncp.exception;

import java.util.Optional;
import solutions.held.pncp.model.RespostaErroValidacao;

/**
 * Representa uma resposta de erro HTTP devolvida pela API de consulta do PNCP (qualquer código de status fora da faixa de sucesso).
 *
 * <p>Carrega o código de status recebido, o corpo bruto da resposta e, quando o corpo pôde ser interpretado como JSON
 * de validação, o {@link RespostaErroValidacao} correspondente.
 *
 * As subclasses distinguem erros de cliente (4xx), de servidor (5xx) e de excesso de requisições (429).
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public class PncpApiException extends PncpException {

    private static final long serialVersionUID = 1L;

    /**
     * Código de status HTTP da resposta de erro.
     */
    private final Integer codigoStatus;

    /**
     * Corpo bruto da resposta, preservado como texto para fins de diagnóstico.
     */
    private final transient String corpoResposta;

    /**
     * Erro de validação estruturado, ou {@code null} quando o corpo não pôde ser interpretado como tal.
     */
    private final transient RespostaErroValidacao erroValidacao;

    public PncpApiException(
            String mensagem, Integer codigoStatus, String corpoResposta, RespostaErroValidacao erroValidacao) {
        super(mensagem);
        this.codigoStatus = codigoStatus;
        this.corpoResposta = corpoResposta;
        this.erroValidacao = erroValidacao;
    }

    public int codigoStatus() {
        return this.codigoStatus;
    }

    public String corpoResposta() {
        return this.corpoResposta;
    }

    /**
     * Retorna o erro de validação estruturado, quando disponível.
     *
     * @return o {@link RespostaErroValidacao} interpretado do corpo, ou vazio quando a resposta não trazia JSON de validação
     * @since 23/11/2026
     */
    public Optional<RespostaErroValidacao> erroValidacao() {
        return Optional.ofNullable(this.erroValidacao);
    }
}
