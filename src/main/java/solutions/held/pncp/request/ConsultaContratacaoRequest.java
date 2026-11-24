package solutions.held.pncp.request;

import java.time.LocalDate;
import solutions.held.pncp.model.enums.ModalidadeContratacao;
import solutions.held.pncp.model.enums.ModoDisputa;

/**
 * Parâmetros das consultas de contratações por data de publicação e por data de atualização global, que compartilham o
 * mesmo conjunto de filtros.
 *
 * <p>
 *     Construa com {@link #builder(LocalDate, LocalDate, ModalidadeContratacao)} ou
 *     {@link #builder(LocalDate, LocalDate, Long)} para informar os parâmetros obrigatórios e, em seguida, os filtros opcionais.
 * </p>
 *
 * @param dataInicial data inicial do período (obrigatória)
 * @param dataFinal data final do período (obrigatória)
 * @param codigoModalidadeContratacao código da modalidade de contratação (obrigatório)
 * @param codigoModoDisputa código do modo de disputa, ou nulo
 * @param uf sigla da unidade federativa, ou nula
 * @param codigoMunicipioIbge código IBGE do município, ou nulo
 * @param cnpj CNPJ do órgão, ou nulo
 * @param codigoUnidadeAdministrativa código da unidade administrativa, ou nulo
 * @param idUsuario identificador do usuário, ou nulo
 * @param tamanhoPagina tamanho da página (de 10 a 50), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaContratacaoRequest(
        LocalDate dataInicial,
        LocalDate dataFinal,
        Long codigoModalidadeContratacao,
        Integer codigoModoDisputa,
        String uf,
        String codigoMunicipioIbge,
        String cnpj,
        String codigoUnidadeAdministrativa,
        Long idUsuario,
        Integer tamanhoPagina) {

    public ConsultaContratacaoRequest {
        java.util.Objects.requireNonNull(dataInicial, "dataInicial é obrigatória.");
        java.util.Objects.requireNonNull(dataFinal, "dataFinal é obrigatória.");
    }

    /**
     * Inicia a construção informando os parâmetros obrigatórios, com a modalidade tipada.
     *
     * @param dataInicial data inicial do período
     * @param dataFinal data final do período
     * @param modalidade modalidade de contratação
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(LocalDate dataInicial, LocalDate dataFinal, ModalidadeContratacao modalidade) {
        java.util.Objects.requireNonNull(modalidade, "modalidade é obrigatória.");
        return new Builder(dataInicial, dataFinal, Long.valueOf(modalidade.codigo()));
    }

    /**
     * Inicia a construção informando os parâmetros obrigatórios, com o código da
     * modalidade.
     *
     * @param dataInicial data inicial do período
     * @param dataFinal data final do período
     * @param codigoModalidadeContratacao código da modalidade de contratação
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(LocalDate dataInicial, LocalDate dataFinal, Long codigoModalidadeContratacao) {
        return new Builder(dataInicial, dataFinal, codigoModalidadeContratacao);
    }

    /**
     * Construtor fluente de {@link ConsultaContratacaoRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final LocalDate dataInicial;
        private final LocalDate dataFinal;
        private final Long codigoModalidadeContratacao;
        private Integer codigoModoDisputa;
        private String uf;
        private String codigoMunicipioIbge;
        private String cnpj;
        private String codigoUnidadeAdministrativa;
        private Long idUsuario;
        private Integer tamanhoPagina;

        Builder(LocalDate dataInicial, LocalDate dataFinal, Long codigoModalidadeContratacao) {
            this.dataInicial = dataInicial;
            this.dataFinal = dataFinal;
            this.codigoModalidadeContratacao = codigoModalidadeContratacao;
        }

        /**
         * Define o modo de disputa a partir da constante tipada.
         *
         * @param modoDisputa modo de disputa, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        @SuppressWarnings("unused")
        public Builder modoDisputa(ModoDisputa modoDisputa) {
            this.codigoModoDisputa = modoDisputa == null ? null : modoDisputa.codigo();
            return this;
        }

        /**
         * Define o código do modo de disputa.
         *
         * @param codigoModoDisputa código do modo de disputa, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        @SuppressWarnings("unused")
        public Builder codigoModoDisputa(Integer codigoModoDisputa) {
            this.codigoModoDisputa = codigoModoDisputa;
            return this;
        }

        /**
         * Define a sigla da unidade federativa.
         *
         * @param uf sigla da UF, ou nula
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder uf(String uf) {
            this.uf = uf;
            return this;
        }

        /**
         * Define o código IBGE do município.
         *
         * @param codigoMunicipioIbge código IBGE do município, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        @SuppressWarnings("unused")
        public Builder codigoMunicipioIbge(String codigoMunicipioIbge) {
            this.codigoMunicipioIbge = codigoMunicipioIbge;
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
        @SuppressWarnings("unused")
        public Builder codigoUnidadeAdministrativa(String codigoUnidadeAdministrativa) {
            this.codigoUnidadeAdministrativa = codigoUnidadeAdministrativa;
            return this;
        }

        /**
         * Define o identificador do usuário.
         *
         * @param idUsuario identificador do usuário, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        @SuppressWarnings("unused")
        public Builder idUsuario(Long idUsuario) {
            this.idUsuario = idUsuario;
            return this;
        }

        /**
         * Define o tamanho da página.
         *
         * @param tamanhoPagina tamanho da página (de 10 a 50), ou nulo para o padrão da API
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
        public ConsultaContratacaoRequest build() {
            return new ConsultaContratacaoRequest(
                    this.dataInicial,
                    this.dataFinal,
                    this.codigoModalidadeContratacao,
                    this.codigoModoDisputa,
                    this.uf,
                    this.codigoMunicipioIbge,
                    this.cnpj,
                    this.codigoUnidadeAdministrativa,
                    this.idUsuario,
                    this.tamanhoPagina);
        }
    }
}
