package solutions.held.pncp.api;

import java.util.stream.Stream;
import solutions.held.pncp.http.HttpTransport;
import solutions.held.pncp.http.QueryParams;
import solutions.held.pncp.model.InstrumentoCobranca;
import solutions.held.pncp.pagina.Pagina;
import solutions.held.pncp.pagina.Paginador;
import solutions.held.pncp.request.ConsultaInstrumentoCobrancaRequest;

/**
 * Consultas de instrumentos de cobrança de contratos e empenhos do PNCP, por data
 * de inclusão.
 *
 * <p>Há uma função que devolve uma única página e uma função {@code stream...} que
 * percorre todas as páginas de forma lazy.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class InstrumentosCobrancaApi {

    private static final String VERSAO_API = "/v1";

    private static final String CAMINHO_INCLUSAO = VERSAO_API + "/instrumentoscobranca/inclusao";

    private final HttpTransport transporte;

    public InstrumentosCobrancaApi(HttpTransport transporte) {
        this.transporte = transporte;
    }

    /**
     * Consulta, em uma página, os instrumentos de cobrança por data de inclusão.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de instrumentos de cobrança
     * @since 23/11/2026
     */
    public Pagina<InstrumentoCobranca> consultarPorInclusao(
            ConsultaInstrumentoCobrancaRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_INCLUSAO,
                new QueryParams()
                        .adicionarData("dataInicial", requisicao.dataInicial())
                        .adicionarData("dataFinal", requisicao.dataFinal())
                        .adicionar("tipoInstrumentoCobranca", requisicao.tipoInstrumentoCobranca())
                        .adicionar("cnpjOrgao", requisicao.cnpjOrgao())
                        .adicionar("pagina", pagina)
                        .adicionar("tamanhoPagina", requisicao.tamanhoPagina()),
                InstrumentoCobranca.class);
    }

    /**
     * Percorre todos os instrumentos de cobrança por data de inclusão de forma
     * lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todos os instrumentos de cobrança do período
     * @since 23/11/2026
     */
    public Stream<InstrumentoCobranca> streamPorInclusao(ConsultaInstrumentoCobrancaRequest requisicao) {
        return Paginador.streamDe(pagina -> consultarPorInclusao(requisicao, pagina));
    }
}
