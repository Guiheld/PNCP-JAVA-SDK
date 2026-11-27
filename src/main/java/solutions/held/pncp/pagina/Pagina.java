package solutions.held.pncp.pagina;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Página de resultados devolvida pelas consultas paginadas da API do PNCP.
 *
 * <p>
 *     Espelha o envelope padrão do portal, com os itens da página e os metadados de paginação.
 *     O campo {@code data} da API é exposto como {@link #itens()} para evitar a ambiguidade de "data" em português.
 * </p>
 *
 * @param <T> tipo dos itens contidos na página
 * @param itens itens desta página (campo {@code data} na API); a lista é imutável e nunca {@code null} (vazia quando a API não devolveu itens)
 * @param totalRegistros total de registros que satisfazem a consulta
 * @param totalPaginas total de páginas disponíveis
 * @param numeroPagina número desta página, iniciando em 1
 * @param paginasRestantes quantidade de páginas ainda não percorridas após esta
 * @param vazia indicador de página vazia (campo {@code empty} na API)
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record Pagina<T>(
        @JsonProperty("data") List<T> itens,
        Long totalRegistros,
        Long totalPaginas,
        Long numeroPagina,
        Long paginasRestantes,
        @JsonProperty("empty") Boolean vazia) {

    /**
     * Normaliza os itens para uma lista imutável, tratando {@code null} como lista vazia.
     *
     * @since 27/11/2026
     */
    public Pagina {
        itens = itens == null ? List.of() : List.copyOf(itens);
    }

    /**
     * Informa se há páginas subsequentes a esta.
     *
     * @return {@code true} quando {@code paginasRestantes} é maior que zero
     * @since 23/11/2026
     */
    public boolean temProximaPagina() {
        return this.paginasRestantes != null && this.paginasRestantes > 0L;
    }

    /**
     * Retorna os itens da página, nunca {@code null}.
     *
     * @return a lista de itens, ou uma lista vazia quando a API não devolveu itens
     * @since 23/11/2026
     */
    public List<T> itensSeguros() {
        return this.itens == null ? List.of() : this.itens;
    }
}
