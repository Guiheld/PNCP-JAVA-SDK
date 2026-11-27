package solutions.held.pncp.model;

/**
 * Corpo de erro de validação devolvido pela API de consulta do PNCP em respostas com status de erro.
 *
 * <p>
 *     Todos os campos são textuais, exatamente como a API os entrega; o {@code timestamp} e o {@code status} chegam como
 *     texto e não são convertidos, para preservar fielmente o que foi recebido.
 * </p>
 *
 * @param message mensagem descritiva do erro
 * @param path caminho do recurso que originou o erro
 * @param timestamp instante do erro, como texto, no formato devolvido pela API
 * @param status código de status HTTP, como texto
 * @param error descrição curta do status HTTP
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record RespostaErroValidacao(String message, String path, String timestamp, String status, String error) {}
