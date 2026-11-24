package solutions.held.pncp.request;

/**
 * Parâmetros da consulta de itens de plano de contratações anual (PCA) por ano,
 * identificador de usuário e, opcionalmente, código de classificação superior.
 *
 * <p>Construa com {@link #builder(Integer, Long)}.
 *
 * @param anoPca ano do plano de contratações (obrigatório)
 * @param idUsuario identificador do usuário (obrigatório)
 * @param codigoClassificacaoSuperior código de classificação superior, ou nulo
 * @param cnpj CNPJ do órgão, ou nulo
 * @param tamanhoPagina tamanho da página (de 10 a 500), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaPcaUsuarioRequest(
        Integer anoPca, Long idUsuario, String codigoClassificacaoSuperior, String cnpj, Integer tamanhoPagina) {

    /**
     * Inicia a construção informando os parâmetros obrigatórios.
     *
     * @param anoPca ano do plano de contratações
     * @param idUsuario identificador do usuário
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(Integer anoPca, Long idUsuario) {
        return new Builder(anoPca, idUsuario);
    }

    /**
     * Construtor fluente de {@link ConsultaPcaUsuarioRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final Integer anoPca;
        private final Long idUsuario;
        private String codigoClassificacaoSuperior;
        private String cnpj;
        private Integer tamanhoPagina;

        Builder(Integer anoPca, Long idUsuario) {
            this.anoPca = anoPca;
            this.idUsuario = idUsuario;
        }

        /**
         * Define o código de classificação superior.
         *
         * @param codigoClassificacaoSuperior código de classificação superior, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder codigoClassificacaoSuperior(String codigoClassificacaoSuperior) {
            this.codigoClassificacaoSuperior = codigoClassificacaoSuperior;
            return this;
        }

        /**
         * Define o CNPJ do órgão.
         *
         * @param cnpj CNPJ do órgão, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder cnpj(String cnpj) {
            this.cnpj = cnpj;
            return this;
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
        public ConsultaPcaUsuarioRequest build() {
            return new ConsultaPcaUsuarioRequest(
                    this.anoPca, this.idUsuario, this.codigoClassificacaoSuperior, this.cnpj, this.tamanhoPagina);
        }
    }
}
