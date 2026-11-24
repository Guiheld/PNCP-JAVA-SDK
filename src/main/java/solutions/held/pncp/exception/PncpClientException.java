package solutions.held.pncp.exception;

import solutions.held.pncp.model.RespostaErroValidacao;

/**
 * Erro de cliente (status HTTP 4xx) devolvido pela API de consulta do PNCP:
 * parâmetros inválidos, recurso inexistente ou requisição malformada.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public class PncpClientException extends PncpApiException {

    private static final long serialVersionUID = 1L;

    public PncpClientException(
            String mensagem, Integer codigoStatus, String corpoResposta, RespostaErroValidacao erroValidacao) {
        super(mensagem, codigoStatus, corpoResposta, erroValidacao);
    }
}
