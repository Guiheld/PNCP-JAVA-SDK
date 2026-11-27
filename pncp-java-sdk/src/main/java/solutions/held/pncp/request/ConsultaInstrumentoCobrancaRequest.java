package solutions.held.pncp.request;

import java.time.LocalDate;

/**
 * Parâmetros da consulta de instrumentos de cobrança por data de inclusão.
 *
 * <p>Construa com {@link #builder(LocalDate, LocalDate)}.
 *
 * @param dataInicial data inicial de inclusão (obrigatória)
 * @param dataFinal data final de inclusão (obrigatória)
 * @param tipoInstrumentoCobranca código do tipo de instrumento de cobrança, ou nulo
 * @param cnpjOrgao CNPJ do órgão, ou nulo
 * @param tamanhoPagina tamanho da página (de 10 a 100), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaInstrumentoCobrancaRequest(
        LocalDate dataInicial,
        LocalDate dataFinal,
        Long tipoInstrumentoCobranca,
        String cnpjOrgao,
        Integer tamanhoPagina) {

    public ConsultaInstrumentoCobrancaRequest {
        java.util.Objects.requireNonNull(dataInicial, "dataInicial é obrigatória.");
        java.util.Objects.requireNonNull(dataFinal, "dataFinal é obrigatória.");
    }

    /**
     * Inicia a construção informando os parâmetros obrigatórios.
     *
     * @param dataInicial data inicial de inclusão
     * @param dataFinal data final de inclusão
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(LocalDate dataInicial, LocalDate dataFinal) {
        return new Builder(dataInicial, dataFinal);
    }

    /**
     * Construtor fluente de {@link ConsultaInstrumentoCobrancaRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final LocalDate dataInicial;
        private final LocalDate dataFinal;
        private Long tipoInstrumentoCobranca;
        private String cnpjOrgao;
        private Integer tamanhoPagina;

        Builder(LocalDate dataInicial, LocalDate dataFinal) {
            this.dataInicial = dataInicial;
            this.dataFinal = dataFinal;
        }

        /**
         * Define o código do tipo de instrumento de cobrança.
         *
         * @param tipoInstrumentoCobranca código do tipo de instrumento de cobrança, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder tipoInstrumentoCobranca(Long tipoInstrumentoCobranca) {
            this.tipoInstrumentoCobranca = tipoInstrumentoCobranca;
            return this;
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
         * Define o tamanho da página.
         *
         * @param tamanhoPagina tamanho da página (de 10 a 100), ou nulo para o padrão da API
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
        public ConsultaInstrumentoCobrancaRequest build() {
            return new ConsultaInstrumentoCobrancaRequest(
                    this.dataInicial, this.dataFinal, this.tipoInstrumentoCobranca, this.cnpjOrgao, this.tamanhoPagina);
        }
    }
}
