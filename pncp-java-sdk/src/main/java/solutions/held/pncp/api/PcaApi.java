package solutions.held.pncp.api;

import java.util.stream.Stream;
import solutions.held.pncp.http.HttpTransport;
import solutions.held.pncp.http.QueryParams;
import solutions.held.pncp.model.PlanoContratacao;
import solutions.held.pncp.pagina.Pagina;
import solutions.held.pncp.pagina.Paginador;
import solutions.held.pncp.request.ConsultaPcaAtualizacaoRequest;
import solutions.held.pncp.request.ConsultaPcaRequest;
import solutions.held.pncp.request.ConsultaPcaUsuarioRequest;

/**
 * Consultas de planos de contratações anuais (PCA) do PNCP: por ano e classificação superior, por usuário e por data de
 * atualização global.
 *
 * <p>Para cada consulta há uma função que devolve uma única página e uma função {@code stream...} que percorre todas as
 * páginas de forma lazy.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class PcaApi {

    private static final String VERSAO_API = "/v1";

    private static final String CAMINHO_POR_ANO = VERSAO_API + "/pca/";

    private static final String CAMINHO_POR_USUARIO = CAMINHO_POR_ANO + "/usuario";

    private static final String CAMINHO_ATUALIZACAO = CAMINHO_POR_ANO + "/atualizacao";

    private final HttpTransport transporte;

    public PcaApi(HttpTransport transporte) {
        this.transporte = transporte;
    }

    /**
     * Consulta, em uma página, os itens de PCA por ano e classificação superior.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de planos de contratações
     * @since 23/11/2026
     */
    public Pagina<PlanoContratacao> consultarPorAno(ConsultaPcaRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_POR_ANO,
                new QueryParams()
                        .adicionar("anoPca", requisicao.anoPca())
                        .adicionar("codigoClassificacaoSuperior", requisicao.codigoClassificacaoSuperior())
                        .adicionar("pagina", pagina)
                        .adicionar("tamanhoPagina", requisicao.tamanhoPagina()),
                PlanoContratacao.class);
    }

    /**
     * Percorre todos os itens de PCA por ano e classificação superior de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todos os planos de contratações
     * @since 23/11/2026
     */
    public Stream<PlanoContratacao> streamPorAno(ConsultaPcaRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarPorAno(requisicao, pagina));
    }

    /**
     * Consulta, em uma página, os itens de PCA por ano, usuário e, opcionalmente, classificação superior.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de planos de contratações
     * @since 23/11/2026
     */
    public Pagina<PlanoContratacao> consultarPorUsuario(ConsultaPcaUsuarioRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_POR_USUARIO,
                new QueryParams()
                        .adicionar("anoPca", requisicao.anoPca())
                        .adicionar("idUsuario", requisicao.idUsuario())
                        .adicionar("codigoClassificacaoSuperior", requisicao.codigoClassificacaoSuperior())
                        .adicionar("cnpj", requisicao.cnpj())
                        .adicionar("pagina", pagina)
                        .adicionar("tamanhoPagina", requisicao.tamanhoPagina()),
                PlanoContratacao.class);
    }

    /**
     * Percorre todos os itens de PCA por ano e usuário de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todos os planos de contratações
     * @since 23/11/2026
     */
    public Stream<PlanoContratacao> streamPorUsuario(ConsultaPcaUsuarioRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarPorUsuario(requisicao, pagina));
    }

    /**
     * Consulta, em uma página, os planos de contratações por data de atualização global.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de planos de contratações
     * @since 23/11/2026
     */
    public Pagina<PlanoContratacao> consultarPorAtualizacao(ConsultaPcaAtualizacaoRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_ATUALIZACAO,
                new QueryParams()
                        .adicionarData("dataInicio", requisicao.dataInicio())
                        .adicionarData("dataFim", requisicao.dataFim())
                        .adicionar("cnpj", requisicao.cnpj())
                        .adicionar("codigoUnidade", requisicao.codigoUnidade())
                        .adicionar("pagina", pagina)
                        .adicionar("tamanhoPagina", requisicao.tamanhoPagina()),
                PlanoContratacao.class);
    }

    /**
     * Percorre todos os planos de contratações por data de atualização global de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todos os planos de contratações
     * @since 23/11/2026
     */
    public Stream<PlanoContratacao> streamPorAtualizacao(ConsultaPcaAtualizacaoRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarPorAtualizacao(requisicao, pagina));
    }
}
