package solutions.held.pncp.model;

/**
 * Amparo legal que fundamenta uma contratação.
 *
 * @param codigo código do amparo legal
 * @param nome nome do amparo legal
 * @param descricao descrição do amparo legal
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record AmparoLegal(Long codigo, String nome, String descricao) {}
