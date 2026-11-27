package solutions.held.pncp.exception;

/**
 * Exceção base de todas as falhas lançadas pela SDK de consulta do PNCP.
 *
 * <p>É uma exceção não verificada (unchecked): o chamador não é obrigado a tratá-la, mas pode capturá-la para lidar de
 * forma uniforme com qualquer erro originado pela biblioteca — seja de comunicação, de serialização ou de resposta da API.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public class PncpException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PncpException(String mensagem) {
        super(mensagem);
    }

    public PncpException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
