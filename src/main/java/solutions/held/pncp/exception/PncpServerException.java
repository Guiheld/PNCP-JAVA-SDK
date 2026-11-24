package solutions.held.pncp.exception;

import solutions.held.pncp.model.RespostaErroValidacao;

/**
 * Erro de servidor (status HTTP 5xx) devolvido pela API de consulta do PNCP, indicando uma falha no lado do portal.
 *
 * <p>Costuma ser transitório; é a classe de erro que a política de novas tentativas da SDK considera recuperável.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public class PncpServerException extends PncpApiException {

    private static final long serialVersionUID = 1L;

    public PncpServerException(
            String mensagem, Integer codigoStatus, String corpoResposta, RespostaErroValidacao erroValidacao) {
        super(mensagem, codigoStatus, corpoResposta, erroValidacao);
    }
}
