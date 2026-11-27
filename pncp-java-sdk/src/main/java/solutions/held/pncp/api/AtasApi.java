package solutions.held.pncp.api;

import java.util.stream.Stream;
import solutions.held.pncp.http.HttpTransport;
import solutions.held.pncp.http.QueryParams;
import solutions.held.pncp.model.Ata;
import solutions.held.pncp.pagina.Pagina;
import solutions.held.pncp.pagina.Paginador;
import solutions.held.pncp.request.ConsultaAtaRequest;

/**
 * Consultas de atas de registro de preço do PNCP:
 * por período de vigência e por data de atualização global.
 *
 * <p>Para cada consulta há uma função que devolve uma única página e uma função
 * {@code stream...} que percorre todas as páginas de forma lazy.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class AtasApi {

    private static final String VERSAO_API = "/v1";

    private static final String CAMINHO_VIGENCIA = VERSAO_API + "/atas";

    private static final String CAMINHO_ATUALIZACAO = CAMINHO_VIGENCIA + "/atualizacao";

    private final HttpTransport transporte;

    public AtasApi(HttpTransport transporte) {
        this.transporte = transporte;
    }

    /**
     * Consulta, em uma página, as atas por período de vigência.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de atas
     * @since 23/11/2026
     */
    public Pagina<Ata> consultarPorVigencia(ConsultaAtaRequest requisicao, int pagina) {
        return this.transporte.consultarPagina(CAMINHO_VIGENCIA, this.montarParametros(requisicao, pagina), Ata.class);
    }

    /**
     * Percorre todas as atas por período de vigência de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todas as atas do período
     * @since 23/11/2026
     */
    public Stream<Ata> streamPorVigencia(ConsultaAtaRequest requisicao) {
        return Paginador.streamDe(pagina -> consultarPorVigencia(requisicao, pagina));
    }

    /**
     * Consulta, em uma página, as atas por data de atualização global.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return a página de atas
     * @since 23/11/2026
     */
    public Pagina<Ata> consultarPorAtualizacao(ConsultaAtaRequest requisicao, int pagina) {
        return this.transporte.consultarPagina(
                CAMINHO_ATUALIZACAO, this.montarParametros(requisicao, pagina), Ata.class);
    }

    /**
     * Percorre todas as atas por data de atualização global de forma lazy.
     *
     * @param requisicao parâmetros da consulta
     * @return um fluxo com todas as atas do período
     * @since 23/11/2026
     */
    public Stream<Ata> streamPorAtualizacao(ConsultaAtaRequest requisicao) {
        return Paginador.streamDe(pagina -> consultarPorAtualizacao(requisicao, pagina));
    }

    /**
     * Monta os parâmetros comuns às consultas de atas.
     *
     * @param requisicao parâmetros da consulta
     * @param pagina número da página, iniciando em 1
     * @return os parâmetros de consulta montados
     * @since 23/11/2026
     */
    private QueryParams montarParametros(ConsultaAtaRequest requisicao, int pagina) {
        return new QueryParams()
                .adicionarData("dataInicial", requisicao.dataInicial())
                .adicionarData("dataFinal", requisicao.dataFinal())
                .adicionar("idUsuario", requisicao.idUsuario())
                .adicionar("cnpj", requisicao.cnpj())
                .adicionar("codigoUnidadeAdministrativa", requisicao.codigoUnidadeAdministrativa())
                .adicionar("pagina", pagina)
                .adicionar("tamanhoPagina", requisicao.tamanhoPagina());
    }
}
