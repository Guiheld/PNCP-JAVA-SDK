package solutions.held.pncp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;

/**
 * Configuração imutável do cliente de consulta do PNCP: endereço base, tempos limite, política de novas tentativas,
 * limitação de vazão, cabeçalho de identificação e o {@link ObjectMapper} usado na desserialização.
 *
 * <p>
 *     Instâncias são criadas por meio de {@link #builder()} e trazem valores padrão adequados ao uso do portal público.
 *     O limitador de vazão vem ligado em um ritmo conservador porque o PNCP não documenta formalmente seus limites de
 *     requisições.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class PncpClientConfig {

    /**
     * Endereço base padrão da API de consulta do PNCP.
     */
    public static final String URL_BASE_PADRAO = "https://pncp.gov.br/api/consulta";

    /**
     * Valor padrão do cabeçalho {@code User-Agent} enviado em cada requisição.
     */
    public static final String USER_AGENT_PADRAO = "pncp-consulta-java/0.1.0";

    /**
     * Vazão padrão, em requisições por segundo, do limitador client-side.
     */
    public static final double VAZAO_PADRAO_POR_SEGUNDO = 3.0;

    private final String urlBase;
    private final Duration timeoutConexao;
    private final Duration timeoutResposta;
    private final Integer maxTentativas;
    private final Duration backoffInicial;
    private final Duration backoffMaximo;
    private final Double multiplicadorBackoff;
    private final Double vazaoPorSegundo;
    private final String userAgent;
    private final ObjectMapper objectMapper;

    private PncpClientConfig(Builder builder) {
        this.urlBase = builder.urlBase;
        this.timeoutConexao = builder.timeoutConexao;
        this.timeoutResposta = builder.timeoutResposta;
        this.maxTentativas = builder.maxTentativas;
        this.backoffInicial = builder.backoffInicial;
        this.backoffMaximo = builder.backoffMaximo;
        this.multiplicadorBackoff = builder.multiplicadorBackoff;
        this.vazaoPorSegundo = builder.vazaoPorSegundo;
        this.userAgent = builder.userAgent;
        this.objectMapper = builder.objectMapper;
    }

    /**
     * Cria um construtor de configuração já preenchido com os valores padrão.
     *
     * @return um novo {@link Builder}
     * @since 23/11/2026
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Cria a configuração padrão, adequada ao uso do portal público.
     *
     * @return uma configuração com todos os valores padrão
     * @since 23/11/2026
     */
    public static PncpClientConfig padrao() {
        return builder().build();
    }

    /**
     * Cria o {@link ObjectMapper} padrão da SDK, com suporte a {@code java.time}, tolerante a campos desconhecidos e a
     * valores de enum ainda não mapeados.
     *
     * @return um {@link ObjectMapper} configurado para as respostas do PNCP
     * @since 23/11/2026
     */
    public static ObjectMapper mapperPadrao() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                .build();
    }

    public String urlBase() {
        return this.urlBase;
    }

    public Duration timeoutConexao() {
        return this.timeoutConexao;
    }

    public Duration timeoutResposta() {
        return this.timeoutResposta;
    }

    public int maxTentativas() {
        return this.maxTentativas;
    }

    public Duration backoffInicial() {
        return this.backoffInicial;
    }

    public Duration backoffMaximo() {
        return this.backoffMaximo;
    }

    public double multiplicadorBackoff() {
        return this.multiplicadorBackoff;
    }

    public double vazaoPorSegundo() {
        return this.vazaoPorSegundo;
    }

    public String userAgent() {
        return this.userAgent;
    }

    public ObjectMapper objectMapper() {
        return this.objectMapper;
    }

    /**
     * Construtor fluente de {@link PncpClientConfig}, com valores padrão já aplicados; cada função substitui um único parâmetro.
     *
     * @since 23/11/2026
     */
    public static final class Builder {

        private String urlBase = URL_BASE_PADRAO;
        private Duration timeoutConexao = Duration.ofSeconds(10);
        private Duration timeoutResposta = Duration.ofSeconds(30);
        private Integer maxTentativas = 3;
        private Duration backoffInicial = Duration.ofMillis(500);
        private Duration backoffMaximo = Duration.ofSeconds(10);
        private Double multiplicadorBackoff = 2.0;
        private Double vazaoPorSegundo = VAZAO_PADRAO_POR_SEGUNDO;
        private String userAgent = USER_AGENT_PADRAO;
        private ObjectMapper objectMapper;

        /**
         * Define o endereço base da API, útil para apontar a um ambiente local ou
         * a um proxy.
         *
         * @param urlBase endereço base, sem barra final
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder urlBase(String urlBase) {
            this.urlBase = urlBase;
            return this;
        }

        /**
         * Define o tempo limite para estabelecer a conexão.
         *
         * @param timeoutConexao duração máxima para conectar
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder timeoutConexao(Duration timeoutConexao) {
            this.timeoutConexao = timeoutConexao;
            return this;
        }

        /**
         * Define o tempo limite para receber a resposta de cada requisição.
         *
         * @param timeoutResposta duração máxima para a resposta
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder timeoutResposta(Duration timeoutResposta) {
            this.timeoutResposta = timeoutResposta;
            return this;
        }

        /**
         * Define o número máximo de tentativas por requisição, contando a
         * primeira. Deve ser pelo menos 1.
         *
         * @param maxTentativas número máximo de tentativas
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder maxTentativas(Integer maxTentativas) {
            this.maxTentativas = maxTentativas;
            return this;
        }

        /**
         * Define o atraso inicial do recuo exponencial entre tentativas.
         *
         * @param backoffInicial atraso da primeira nova tentativa
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder backoffInicial(Duration backoffInicial) {
            this.backoffInicial = backoffInicial;
            return this;
        }

        /**
         * Define o teto do atraso do recuo exponencial entre tentativas.
         *
         * @param backoffMaximo atraso máximo entre tentativas
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder backoffMaximo(Duration backoffMaximo) {
            this.backoffMaximo = backoffMaximo;
            return this;
        }

        /**
         * Define o multiplicador do recuo exponencial aplicado a cada tentativa.
         *
         * @param multiplicadorBackoff multiplicador do atraso
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder multiplicadorBackoff(Double multiplicadorBackoff) {
            this.multiplicadorBackoff = multiplicadorBackoff;
            return this;
        }

        /**
         * Define a vazão do limitador client-side, em requisições por segundo. Um
         * valor igual ou menor que zero desliga a limitação.
         *
         * @param vazaoPorSegundo vazão em requisições por segundo
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder vazaoPorSegundo(Double vazaoPorSegundo) {
            this.vazaoPorSegundo = vazaoPorSegundo;
            return this;
        }

        /**
         * Define o cabeçalho {@code User-Agent} enviado nas requisições.
         *
         * @param userAgent valor do cabeçalho de identificação
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        /**
         * Substitui o {@link ObjectMapper} usado na desserialização das respostas.
         *
         * @param objectMapper mapeador a utilizar; quando nulo, usa-se {@link #mapperPadrao()}
         * @return este construtor, para encadeamento fluente
         * @since 23/11/2026
         */
        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        /**
         * Valida os parâmetros e constrói a configuração imutável.
         *
         * @return a configuração construída
         * @throws IllegalArgumentException se algum parâmetro for inválido
         * @since 23/11/2026
         */
        public PncpClientConfig build() {
            if (this.urlBase == null || this.urlBase.isBlank())
                throw new IllegalArgumentException("urlBase não pode ser nula ou vazia.");
            if (this.maxTentativas < 1) throw new IllegalArgumentException("maxTentativas deve ser pelo menos 1.");
            if (this.multiplicadorBackoff < 1.0)
                throw new IllegalArgumentException("multiplicadorBackoff deve ser pelo menos 1,0.");
            java.util.Objects.requireNonNull(this.timeoutConexao, "timeoutConexao não pode ser nulo.");
            java.util.Objects.requireNonNull(this.timeoutResposta, "timeoutResposta não pode ser nulo.");
            java.util.Objects.requireNonNull(this.backoffInicial, "backoffInicial não pode ser nulo.");
            java.util.Objects.requireNonNull(this.backoffMaximo, "backoffMaximo não pode ser nulo.");
            if (this.userAgent == null || this.userAgent.isBlank())
                throw new IllegalArgumentException("userAgent não pode ser nulo ou vazio.");
            if (this.objectMapper == null) this.objectMapper = mapperPadrao();
            return new PncpClientConfig(this);
        }
    }
}
