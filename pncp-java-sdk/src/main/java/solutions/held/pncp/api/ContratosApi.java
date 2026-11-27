package solutions.held.pncp.api;

import java.util.stream.Stream;
import solutions.held.pncp.http.HttpTransport;
import solutions.held.pncp.http.QueryParams;
import solutions.held.pncp.model.Contrato;
import solutions.held.pncp.pagina.Pagina;
import solutions.held.pncp.pagina.Paginador;
import solutions.held.pncp.request.ConsultaContratoRequest;

/**
 * Consultas de contratos e empenhos do PNCP: por data de publicação e por data de atualização global.
 *
 * <p>Para cada consulta há uma função que devolve uma única página e uma função {@code stream...} que percorre todas as páginas de forma lazy.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class ContratosApi {

    private static final String VERSAO_API = "/v1";

    private static final String CAMINHO_PUBLICACAO = VERSAO_API + "/contratos";

    private static final String CAMINHO_ATUALIZACAO = VERSAO_API + "/contratos/atualizacao";

    private final HttpTransport transporte;

    public ContratosApi(HttpTransport transporte) {
        this.transporte = transporte;
    }

    /**
     * Consulta, em uma página, os contratos por data de publicação.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de contratos
     * @since 23/11/2026
     */
    public Pagina<Contrato> consultarPorPublicacao(ConsultaContratoRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_PUBLICACAO, this.montarParametros(requisicao, pagina), Contrato.class);
    }

    /**
     * Percorre todos os contratos por data de publicação de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todos os contratos do período
     * @since 23/11/2026
     */
    public Stream<Contrato> streamPorPublicacao(ConsultaContratoRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarPorPublicacao(requisicao, pagina));
    }

    /**
     * Consulta, em uma página, os contratos por data de atualização global.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de contratos
     * @since 23/11/2026
     */
    public Pagina<Contrato> consultarPorAtualizacao(ConsultaContratoRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_ATUALIZACAO, this.montarParametros(requisicao, pagina), Contrato.class);
    }

    /**
     * Percorre todos os contratos por data de atualização global de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todos os contratos do período
     * @since 23/11/2026
     */
    public Stream<Contrato> streamPorAtualizacao(ConsultaContratoRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarPorAtualizacao(requisicao, pagina));
    }

    /**
     * Monta os parâmetros comuns às consultas por publicação e por atualização.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return os parâmetros de consulta montados
     * @since 23/11/2026
     */
    private QueryParams montarParametros(ConsultaContratoRequest requisicao, Integer pagina) {
        return new QueryParams()
                .adicionarData("dataInicial", requisicao.dataInicial())
                .adicionarData("dataFinal", requisicao.dataFinal())
                .adicionar("cnpjOrgao", requisicao.cnpjOrgao())
                .adicionar("codigoUnidadeAdministrativa", requisicao.codigoUnidadeAdministrativa())
                .adicionar("usuarioId", requisicao.usuarioId())
                .adicionar("pagina", pagina)
                .adicionar("tamanhoPagina", requisicao.tamanhoPagina());
    }
}
