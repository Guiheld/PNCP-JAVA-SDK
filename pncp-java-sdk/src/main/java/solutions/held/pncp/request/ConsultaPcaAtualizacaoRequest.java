package solutions.held.pncp.request;

import java.time.LocalDate;

/**
 * Parâmetros da consulta de planos de contratações anuais (PCA) por data de atualização global.
 *
 * <p>
 *     Construa com {@link #builder(LocalDate, LocalDate)}.
 * </p>
 *
 * @param dataInicio data inicial do período de atualização (obrigatória)
 * @param dataFim data final do período de atualização (obrigatória)
 * @param cnpj CNPJ do órgão, ou nulo
 * @param codigoUnidade código da unidade, ou nulo
 * @param tamanhoPagina tamanho da página (de 10 a 500), ou nulo para o padrão da API
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ConsultaPcaAtualizacaoRequest(
        LocalDate dataInicio, LocalDate dataFim, String cnpj, String codigoUnidade, Integer tamanhoPagina) {

    public ConsultaPcaAtualizacaoRequest {
        java.util.Objects.requireNonNull(dataInicio, "dataInicio é obrigatória.");
        java.util.Objects.requireNonNull(dataFim, "dataFim é obrigatória.");
    }

    /**
     * Inicia a construção informando os parâmetros obrigatórios.
     *
     * @param dataInicio data inicial do período de atualização
     * @param dataFim data final do período de atualização
     * @return um novo construtor
     * @since 23/11/2026
     */
    public static Builder builder(LocalDate dataInicio, LocalDate dataFim) {
        return new Builder(dataInicio, dataFim);
    }

    /**
     * Construtor fluente de {@link ConsultaPcaAtualizacaoRequest}.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private final LocalDate dataInicio;
        private final LocalDate dataFim;
        private String cnpj;
        private String codigoUnidade;
        private Integer tamanhoPagina;

        Builder(LocalDate dataInicio, LocalDate dataFim) {
            this.dataInicio = dataInicio;
            this.dataFim = dataFim;
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
         * Define o código da unidade.
         *
         * @param codigoUnidade código da unidade, ou nulo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder codigoUnidade(String codigoUnidade) {
            this.codigoUnidade = codigoUnidade;
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
        public ConsultaPcaAtualizacaoRequest build() {
            return new ConsultaPcaAtualizacaoRequest(
                    this.dataInicio, this.dataFim, this.cnpj, this.codigoUnidade, this.tamanhoPagina);
        }
    }
}
