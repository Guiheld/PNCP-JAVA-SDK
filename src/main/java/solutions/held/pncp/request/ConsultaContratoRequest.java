package solutions.held.pncp.request;

import java.time.LocalDate;

/**
 * Parâmetros das consultas de contratos e empenhos por data de publicação e por
 * data de atualização global, que compartilham o mesmo conjunto de filtros.
 *
 * <p>Construa com {@link #builder(LocalDate, LocalDate)}.
 *
 * @param dataInicial data inicial do período (obrigatória)
 * @param dataFinal data final do período (obrigatória)
 * @param cnpjOrgao CNPJ do órgão, ou nulo
 * @param codigoUnidadeAdministrativa código da unidade administrativa, ou nulo
 * @param usuarioId identificador do usuário, ou nulo
 * @param tamanhoPagina tamanho da página (de 10 a 500), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaContratoRequest(
        LocalDate dataInicial,
        LocalDate dataFinal,
        String cnpjOrgao,
        String codigoUnidadeAdministrativa,
        Long usuarioId,
        Integer tamanhoPagina) {

    public ConsultaContratoRequest {
        java.util.Objects.requireNonNull(dataInicial, "dataInicial é obrigatória.");
        java.util.Objects.requireNonNull(dataFinal, "dataFinal é obrigatória.");
    }

    /**
     * Inicia a construção informando os parâmetros obrigatórios.
     *
     * @param dataInicial data inicial do período
     * @param dataFinal data final do período
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(LocalDate dataInicial, LocalDate dataFinal) {
        return new Builder(dataInicial, dataFinal);
    }

    /**
     * Construtor fluente de {@link ConsultaContratoRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final LocalDate dataInicial;
        private final LocalDate dataFinal;
        private String cnpjOrgao;
        private String codigoUnidadeAdministrativa;
        private Long usuarioId;
        private Integer tamanhoPagina;

        Builder(LocalDate dataInicial, LocalDate dataFinal) {
            this.dataInicial = dataInicial;
            this.dataFinal = dataFinal;
        }

        /**
         * Define o CNPJ do órgão.
         *
         * @param cnpjOrgao CNPJ do órgão, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder cnpjOrgao(String cnpjOrgao) {
            this.cnpjOrgao = cnpjOrgao;
            return this;
        }

        /**
         * Define o código da unidade administrativa.
         *
         * @param codigoUnidadeAdministrativa código da unidade administrativa, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder codigoUnidadeAdministrativa(String codigoUnidadeAdministrativa) {
            this.codigoUnidadeAdministrativa = codigoUnidadeAdministrativa;
            return this;
        }

        /**
         * Define o identificador do usuário.
         *
         * @param usuarioId identificador do usuário, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder usuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
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
        public ConsultaContratoRequest build() {
            return new ConsultaContratoRequest(
                    this.dataInicial,
                    this.dataFinal,
                    this.cnpjOrgao,
                    this.codigoUnidadeAdministrativa,
                    this.usuarioId,
                    this.tamanhoPagina);
        }
    }
}
