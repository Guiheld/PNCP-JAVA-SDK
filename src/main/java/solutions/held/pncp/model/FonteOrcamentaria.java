package solutions.held.pncp.model;

import java.time.LocalDateTime;

/**
 * Fonte orçamentária associada a uma contratação.
 *
 * @param codigo código da fonte orçamentária
 * @param nome nome da fonte orçamentária
 * @param descricao descrição da fonte orçamentária
 * @param dataInclusao data e hora de inclusão da fonte no PNCP
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record FonteOrcamentaria(Long codigo, String nome, String descricao, LocalDateTime dataInclusao) {}
