package solutions.held.pncp.model;

/**
 * Unidade administrativa de um órgão ou entidade, com sua localização.
 *
 * @param ufNome nome da unidade federativa
 * @param ufSigla sigla da unidade federativa
 * @param municipioNome nome do município
 * @param codigoIbge código IBGE do município
 * @param codigoUnidade código da unidade administrativa
 * @param nomeUnidade nome da unidade administrativa
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record UnidadeOrgao(
        String ufNome,
        String ufSigla,
        String municipioNome,
        String codigoIbge,
        String codigoUnidade,
        String nomeUnidade) {}
