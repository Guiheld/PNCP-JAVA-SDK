package solutions.held.pncp.request;

import java.time.LocalDate;

/**
 * Parâmetros das consultas de atas de registro de preço por período de vigência e por data de atualização global, que
 * compartilham o mesmo conjunto de filtros.
 *
 * <p>
 *     Construa com {@link #builder(LocalDate, LocalDate)}.
 * </p>
 *
 * @param dataInicial data inicial do período (obrigatória)
 * @param dataFinal data final do período (obrigatória)
 * @param idUsuario identificador do usuário, ou nulo
 * @param cnpj CNPJ do órgão, ou nulo
 * @param codigoUnidadeAdministrativa código da unidade administrativa, ou nulo
 * @param tamanhoPagina tamanho da página (de 10 a 500), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaAtaRequest(
        LocalDate dataInicial,
        LocalDate dataFinal,
        Long idUsuario,
        String cnpj,
        String codigoUnidadeAdministrativa,
        Integer tamanhoPagina) {

    public ConsultaAtaRequest {
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
     * Construtor fluente de {@link ConsultaAtaRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final LocalDate dataInicial;
        private final LocalDate dataFinal;
        private Long idUsuario;
        private String cnpj;
        private String codigoUnidadeAdministrativa;
        private Integer tamanhoPagina;

        Builder(LocalDate dataInicial, LocalDate dataFinal) {
            this.dataInicial = dataInicial;
            this.dataFinal = dataFinal;
        }

        /**
         * Define o identificador do usuário.
         *
         * @param idUsuario identificador do usuário, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder idUsuario(Long idUsuario) {
            this.idUsuario = idUsuario;
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
        public ConsultaAtaRequest build() {
            return new ConsultaAtaRequest(
                    this.dataInicial,
                    this.dataFinal,
                    this.idUsuario,
                    this.cnpj,
                    this.codigoUnidadeAdministrativa,
                    this.tamanhoPagina);
        }
    }
}
