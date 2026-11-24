package solutions.held.pncp.request;

import java.time.LocalDate;
import solutions.held.pncp.model.enums.ModalidadeContratacao;

/**
 * Parâmetros da consulta de contratações com recebimento de propostas em aberto.
 *
 * <p>
 *     Diferentemente das demais consultas de contratações, exige apenas a data final e a modalidade é opcional.
 *     Construa com {@link #builder(LocalDate)}.
 * </p>
 *
 * @param dataFinal data final do período de recebimento de propostas (obrigatória)
 * @param codigoModalidadeContratacao código da modalidade de contratação, ou nulo
 * @param uf sigla da unidade federativa, ou nula
 * @param codigoMunicipioIbge código IBGE do município, ou nulo
 * @param cnpj CNPJ do órgão, ou nulo
 * @param codigoUnidadeAdministrativa código da unidade administrativa, ou nulo
 * @param idUsuario identificador do usuário, ou nulo
 * @param tamanhoPagina tamanho da página (de 10 a 50), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaContratacaoPropostaRequest(
        LocalDate dataFinal,
        Long codigoModalidadeContratacao,
        String uf,
        String codigoMunicipioIbge,
        String cnpj,
        String codigoUnidadeAdministrativa,
        Long idUsuario,
        Integer tamanhoPagina) {

    public ConsultaContratacaoPropostaRequest {
        java.util.Objects.requireNonNull(dataFinal, "dataFinal é obrigatória.");
    }

    /**
     * Inicia a construção informando a data final obrigatória.
     *
     * @param dataFinal data final do período de recebimento de propostas
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(LocalDate dataFinal) {
        return new Builder(dataFinal);
    }

    /**
     * Construtor fluente de {@link ConsultaContratacaoPropostaRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final LocalDate dataFinal;
        private Long codigoModalidadeContratacao;
        private String uf;
        private String codigoMunicipioIbge;
        private String cnpj;
        private String codigoUnidadeAdministrativa;
        private Long idUsuario;
        private Integer tamanhoPagina;

        Builder(LocalDate dataFinal) {
            this.dataFinal = dataFinal;
        }

        /**
         * Define a modalidade de contratação a partir da constante tipada.
         *
         * @param modalidade modalidade de contratação, ou nula
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder modalidade(ModalidadeContratacao modalidade) {
            this.codigoModalidadeContratacao = modalidade == null ? null : (long) modalidade.codigo();
            return this;
        }

        /**
         * Define o código da modalidade de contratação.
         *
         * @param codigoModalidadeContratacao código da modalidade, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder codigoModalidadeContratacao(Long codigoModalidadeContratacao) {
            this.codigoModalidadeContratacao = codigoModalidadeContratacao;
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
        public ConsultaContratacaoPropostaRequest build() {
            return new ConsultaContratacaoPropostaRequest(
                    this.dataFinal,
                    this.codigoModalidadeContratacao,
                    this.uf,
                    this.codigoMunicipioIbge,
                    this.cnpj,
                    this.codigoUnidadeAdministrativa,
                    this.idUsuario,
                    this.tamanhoPagina);
        }
    }
}
