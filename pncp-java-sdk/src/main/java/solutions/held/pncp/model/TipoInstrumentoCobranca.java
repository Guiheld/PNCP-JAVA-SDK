package solutions.held.pncp.model;

import java.time.LocalDateTime;

/**
 * Tipo de um instrumento de cobrança (por exemplo, nota fiscal ou fatura).
 *
 * @param id identificador do tipo de instrumento de cobrança
 * @param nome nome do tipo de instrumento de cobrança
 * @param descricao descrição do tipo de instrumento de cobrança
 * @param dataInclusao data e hora de inclusão do tipo no PNCP
 * @param dataAtualizacao data e hora da última atualização do tipo no PNCP
 * @param statusAtivo indica se o tipo está ativo
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record TipoInstrumentoCobranca(
        Long id,
        String nome,
        String descricao,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao,
        Boolean statusAtivo) {}
