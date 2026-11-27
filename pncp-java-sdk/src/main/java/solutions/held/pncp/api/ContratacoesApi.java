package solutions.held.pncp.api;

import java.util.Optional;
import java.util.stream.Stream;
import solutions.held.pncp.http.HttpTransport;
import solutions.held.pncp.http.QueryParams;
import solutions.held.pncp.model.Contratacao;
import solutions.held.pncp.model.ContratacaoResumo;
import solutions.held.pncp.pagina.Pagina;
import solutions.held.pncp.pagina.Paginador;
import solutions.held.pncp.request.ConsultaContratacaoPropostaRequest;
import solutions.held.pncp.request.ConsultaContratacaoRequest;

/**
 * Consultas de contratações (compras) do PNCP:
 * por data de publicação, por data de atualização global,
 * por recebimento de propostas aberto e a consulta detalhada de uma contratação específica.
 *
 * <p>Para cada consulta paginada há uma função que devolve uma única página e uma
 * função {@code stream...} que percorre todas as páginas de forma lazy.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class ContratacoesApi {

    private static final String VERSAO_API = "/v1";

    private static final String CAMINHO_CONTRATACOES = "/contratacoes";

    private static final String CAMINHO_PUBLICACAO = VERSAO_API + CAMINHO_CONTRATACOES + "/publicacao";

    private static final String CAMINHO_ATUALIZACAO = VERSAO_API + CAMINHO_CONTRATACOES + "/atualizacao";

    private static final String CAMINHO_PROPOSTA = VERSAO_API + CAMINHO_CONTRATACOES + "/proposta";

    private static final String CAMINHO_ORGAOS = VERSAO_API + "/orgaos";

    private static final String CAMINHO_COMPRAS = "/compras";

    private final HttpTransport transporte;

    public ContratacoesApi(HttpTransport transporte) {
        this.transporte = transporte;
    }

    /**
     * Consulta uma contratação específica pelo CNPJ do órgão, ano e sequencial.
     *
     * @param cnpj CNPJ do órgão
     * @param ano ano da compra
     * @param sequencial sequencial da compra no órgão
     * @return a contratação, ou vazio quando não encontrada (HTTP 204)
     * @since 23/11/2026
     */
    public Optional<Contratacao> consultarPorId(String cnpj, Integer ano, Integer sequencial) {
        return Optional.ofNullable(this.transporte.consultarObjeto(
                CAMINHO_ORGAOS + "/" + cnpj + CAMINHO_COMPRAS + "/" + ano + "/" + sequencial, null, Contratacao.class));
    }

    /**
     * Consulta, em uma página, as contratações por data de publicação.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de contratações
     * @since 23/11/2026
     */
    public Pagina<ContratacaoResumo> consultarPorPublicacao(ConsultaContratacaoRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_PUBLICACAO, this.montarParametros(requisicao, pagina), ContratacaoResumo.class);
    }

    /**
     * Percorre todas as contratações por data de publicação de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todas as contratações do período
     * @since 23/11/2026
     */
    public Stream<ContratacaoResumo> streamPorPublicacao(ConsultaContratacaoRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarPorPublicacao(requisicao, pagina));
    }

    /**
     * Consulta, em uma página, as contratações por data de atualização global.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de contratações
     * @since 23/11/2026
     */
    public Pagina<ContratacaoResumo> consultarPorAtualizacao(ConsultaContratacaoRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_ATUALIZACAO, this.montarParametros(requisicao, pagina), ContratacaoResumo.class);
    }

    /**
     * Percorre todas as contratações por data de atualização global de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todas as contratações do período
     * @since 23/11/2026
     */
    public Stream<ContratacaoResumo> streamPorAtualizacao(ConsultaContratacaoRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarPorAtualizacao(requisicao, pagina));
    }

    /**
     * Consulta, em uma página, as contratações com recebimento de propostas em aberto.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de contratações
     * @since 23/11/2026
     */
    public Pagina<ContratacaoResumo> consultarComPropostaAberta(
            ConsultaContratacaoPropostaRequest requisicao, Integer pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_PROPOSTA,
                new QueryParams()
                        .adicionarData("dataFinal", requisicao.dataFinal())
                        .adicionar("codigoModalidadeContratacao", requisicao.codigoModalidadeContratacao())
                        .adicionar("uf", requisicao.uf())
                        .adicionar("codigoMunicipioIbge", requisicao.codigoMunicipioIbge())
                        .adicionar("cnpj", requisicao.cnpj())
                        .adicionar("codigoUnidadeAdministrativa", requisicao.codigoUnidadeAdministrativa())
                        .adicionar("idUsuario", requisicao.idUsuario())
                        .adicionar("pagina", pagina)
                        .adicionar("tamanhoPagina", requisicao.tamanhoPagina()),
                ContratacaoResumo.class);
    }

    /**
     * Percorre todas as contratações com recebimento de propostas em aberto de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todas as contratações elegíveis
     * @since 23/11/2026
     */
    public Stream<ContratacaoResumo> streamComPropostaAberta(ConsultaContratacaoPropostaRequest requisicao) {
        return Paginador.streamDe(pagina -> this.consultarComPropostaAberta(requisicao, pagina));
    }

    /**
     * Monta os parâmetros comuns às consultas por publicação e por atualização.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return os parâmetros de consulta montados
     * @since 23/11/2026
     */
    private QueryParams montarParametros(ConsultaContratacaoRequest requisicao, Integer pagina) {
        return new QueryParams()
                .adicionarData("dataInicial", requisicao.dataInicial())
                .adicionarData("dataFinal", requisicao.dataFinal())
                .adicionar("codigoModalidadeContratacao", requisicao.codigoModalidadeContratacao())
                .adicionar("codigoModoDisputa", requisicao.codigoModoDisputa())
                .adicionar("uf", requisicao.uf())
                .adicionar("codigoMunicipioIbge", requisicao.codigoMunicipioIbge())
                .adicionar("cnpj", requisicao.cnpj())
                .adicionar("codigoUnidadeAdministrativa", requisicao.codigoUnidadeAdministrativa())
                .adicionar("idUsuario", requisicao.idUsuario())
                .adicionar("pagina", pagina)
                .adicionar("tamanhoPagina", requisicao.tamanhoPagina());
    }
}
