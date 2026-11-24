/**
 * Consultas da API do PNCP agrupadas por domínio: contratações, contratos e empenhos, atas de registro de preço, planos
 * de contratações anuais e instrumentos de cobrança.
 *
 * <p>
 *     Cada classe é obtida a partir do {@link solutions.held.pncp.PncpConsultaClient} e oferece, para cada consulta
 *     paginada, uma função que devolve uma página e uma função {@code stream...} que percorre todas as páginas.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
package solutions.held.pncp.api;
