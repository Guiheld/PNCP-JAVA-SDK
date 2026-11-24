package solutions.held.pncp.model;

/**
 * Item de uma nota fiscal eletrônica.
 *
 * <p>
 *     Todos os campos são textuais, como exatamente entregues pela API, inclusive quantidades e valores.
 * </p>
 *
 * @param numeroItem número do item
 * @param descricaoProdutoServico descrição do produto ou serviço
 * @param codigoNCM código NCM do produto
 * @param descricaoNCM descrição do código NCM
 * @param cfop código fiscal de operações e prestações (CFOP)
 * @param quantidade quantidade do item, como texto
 * @param unidade unidade de medida
 * @param valorUnitario valor unitário, como texto
 * @param valorTotal valor total do item, como texto
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ItemNotaFiscal(
        String numeroItem,
        String descricaoProdutoServico,
        String codigoNCM,
        String descricaoNCM,
        String cfop,
        String quantidade,
        String unidade,
        String valorUnitario,
        String valorTotal) {}
