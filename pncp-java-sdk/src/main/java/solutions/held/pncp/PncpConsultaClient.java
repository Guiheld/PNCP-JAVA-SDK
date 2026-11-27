package solutions.held.pncp;

import solutions.held.pncp.api.AtasApi;
import solutions.held.pncp.api.ContratacoesApi;
import solutions.held.pncp.api.ContratosApi;
import solutions.held.pncp.api.InstrumentosCobrancaApi;
import solutions.held.pncp.api.PcaApi;
import solutions.held.pncp.http.HttpTransport;

/**
 * Ponto de entrada da SDK de consulta ao PNCP (Portal Nacional de Contratações Públicas).
 *
 * <p>
 *     Agrupa as consultas por domínio — contratações, contratos, atas, planos de contratações e instrumentos de cobrança
 *     — compartilhando uma única configuração e um único transporte HTTP. A instância é imutável e segura para uso
 *     concorrente.
 * </p>
 *
 * <p>
 *     Exemplo de uso:
 * </p>
 *
 * <pre>{@code
 * PncpConsultaClient cliente = PncpConsultaClient.criar();
 *
 * ConsultaContratacaoRequest consulta = ConsultaContratacaoRequest
 *         .builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31), ModalidadeContratacao.PREGAO_ELETRONICO)
 *         .uf("SP")
 *         .build();
 *
 * cliente.contratacoes().streamPorPublicacao(consulta)
 *         .limit(100)
 *         .forEach(contratacao -> System.out.println(contratacao.objetoCompra()));
 * }</pre>
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class PncpConsultaClient {

    private final PncpClientConfig config;
    private final ContratacoesApi contratacoes;
    private final ContratosApi contratos;
    private final AtasApi atas;
    private final PcaApi pca;
    private final InstrumentosCobrancaApi instrumentosCobranca;

    private PncpConsultaClient(PncpClientConfig config) {
        this.config = config;
        HttpTransport transporte = new HttpTransport(config);
        this.contratacoes = new ContratacoesApi(transporte);
        this.contratos = new ContratosApi(transporte);
        this.atas = new AtasApi(transporte);
        this.pca = new PcaApi(transporte);
        this.instrumentosCobranca = new InstrumentosCobrancaApi(transporte);
    }

    /**
     * Cria um cliente com a configuração padrão, adequada ao uso do portal público.
     *
     * @return um novo cliente com a configuração padrão
     * @since 23/11/2026
     */
    public static PncpConsultaClient criar() {
        return new PncpConsultaClient(PncpClientConfig.padrao());
    }

    /**
     * Cria um cliente com uma configuração personalizada.
     *
     * @param config configuração do cliente
     * @return um novo cliente com a configuração informada
     * @since 23/11/2026
     */
    public static PncpConsultaClient comConfig(PncpClientConfig config) {
        return new PncpConsultaClient(config);
    }

    public ContratacoesApi contratacoes() {
        return this.contratacoes;
    }

    public ContratosApi contratos() {
        return this.contratos;
    }

    public AtasApi atas() {
        return this.atas;
    }

    public PcaApi pca() {
        return this.pca;
    }

    public InstrumentosCobrancaApi instrumentosCobranca() {
        return this.instrumentosCobranca;
    }

    public PncpClientConfig config() {
        return this.config;
    }
}
