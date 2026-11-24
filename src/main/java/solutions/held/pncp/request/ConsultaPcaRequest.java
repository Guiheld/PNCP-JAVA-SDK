package solutions.held.pncp.request;

/**
 * Parâmetros da consulta de itens de plano de contratações anual (PCA) por ano e
 * código de classificação superior.
 *
 * <p>Construa com {@link #builder(Integer, String)}.
 *
 * @param anoPca ano do plano de contratações (obrigatório)
 * @param codigoClassificacaoSuperior código de classificação superior (obrigatório)
 * @param tamanhoPagina tamanho da página (de 10 a 500), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaPcaRequest(Integer anoPca, String codigoClassificacaoSuperior, Integer tamanhoPagina) {

    public ConsultaPcaRequest {
        java.util.Objects.requireNonNull(codigoClassificacaoSuperior, "codigoClassificacaoSuperior é obrigatório.");
    }

    /**
     * Inicia a construção informando os parâmetros obrigatórios.
     *
     * @param anoPca ano do plano de contratações
     * @param codigoClassificacaoSuperior código de classificação superior
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(Integer anoPca, String codigoClassificacaoSuperior) {
        return new Builder(anoPca, codigoClassificacaoSuperior);
    }

    /**
     * Construtor fluente de {@link ConsultaPcaRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final Integer anoPca;
        private final String codigoClassificacaoSuperior;
        private Integer tamanhoPagina;

        Builder(Integer anoPca, String codigoClassificacaoSuperior) {
            this.anoPca = anoPca;
            this.codigoClassificacaoSuperior = codigoClassificacaoSuperior;
        }

        /**
         * Define o tamanho da página.
         *
         * @param tamanhoPagina tamanho da página (de 10 a 500), ou nulo para o padrão da API
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder tamanhoPagina(Integer tamanhoPagina) {
            this.tamanhoPagina = tamanhoPagina;
            return this;
        }

        /**
         * Constrói os parâmetros imutáveis.
         *
         * @return os parâmetros construídos
         * @since 23/11/2026
         */
        public ConsultaPcaRequest build() {
            return new ConsultaPcaRequest(this.anoPca, this.codigoClassificacaoSuperior, this.tamanhoPagina);
        }
    }
}
