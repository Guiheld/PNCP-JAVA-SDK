package solutions.held.pncp.exception;

/**
 * Lançada quando a SDK não consegue serializar os parâmetros de uma consulta ou desserializar o corpo de uma resposta
 * da API do PNCP.
 *
 * <p>Normalmente indica uma divergência entre o modelo da SDK e o formato efetivamente devolvido pela API — por exemplo,
 * uma mudança de contrato não acompanhada por uma atualização da biblioteca.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public class PncpSerializationException extends PncpException {

    private static final long serialVersionUID = 1L;

    public PncpSerializationException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
