package solutions.held.pncp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contratação (compra) do PNCP em sua forma detalhada, devolvida pela consulta de uma contratação específica por órgão,
 * ano e sequencial.
 *
 * <p>
 *     O campo {@code indicadorOrcamentoSigiloso} do contrato de dados é de escrita ({@code writeOnly}) e nunca retorna
 *     nas consultas; por isso não é modelado aqui.
 *     Os códigos de modalidade, modo de disputa e situação são mantidos em sua forma bruta ({@code Long}/{@code String});
 *     para resolvê-los em constantes tipadas use os enums do pacote {@code solutions.held.pncp.model.enums}.
 * </p>
 *
 * @param valorTotalEstimado valor total estimado da contratação
 * @param valorTotalHomologado valor total homologado da contratação
 * @param orcamentoSigilosoCodigo código do orçamento sigiloso
 * @param orcamentoSigilosoDescricao descrição do orçamento sigiloso
 * @param numeroControlePNCP número de controle da contratação no PNCP
 * @param linkSistemaOrigem link para o sistema de origem
 * @param linkProcessoEletronico link para o processo eletrônico
 * @param anoCompra ano da compra
 * @param sequencialCompra sequencial da compra no órgão
 * @param numeroCompra número da compra
 * @param processo número do processo
 * @param orgaoEntidade órgão ou entidade responsável
 * @param unidadeOrgao unidade administrativa responsável
 * @param orgaoSubRogado órgão ou entidade sub-rogado, quando houver
 * @param unidadeSubRogada unidade administrativa sub-rogada, quando houver
 * @param modalidadeId código da modalidade de contratação
 * @param modalidadeNome nome da modalidade de contratação
 * @param justificativaPresencial justificativa para realização presencial
 * @param modoDisputaId código do modo de disputa
 * @param modoDisputaNome nome do modo de disputa
 * @param tipoInstrumentoConvocatorioCodigo código do tipo de instrumento convocatório
 * @param tipoInstrumentoConvocatorioNome nome do tipo de instrumento convocatório
 * @param amparoLegal amparo legal da contratação
 * @param objetoCompra objeto da compra
 * @param informacaoComplementar informação complementar
 * @param srp indica se é sistema de registro de preços
 * @param fontesOrcamentarias fontes orçamentárias da contratação
 * @param emendaParlamentar indica se há emenda parlamentar
 * @param dataPublicacaoPncp data e hora de publicação no PNCP
 * @param dataAberturaProposta data e hora de abertura das propostas
 * @param dataEncerramentoProposta data e hora de encerramento das propostas
 * @param situacaoCompraId código da situação da contratação
 * @param situacaoCompraNome nome da situação da contratação
 * @param existeResultado indica se há resultado para a contratação
 * @param dataInclusao data e hora de inclusão no PNCP
 * @param dataAtualizacao data e hora da última atualização no PNCP
 * @param dataAtualizacaoGlobal data e hora da última atualização global no PNCP
 * @param usuarioNome nome do usuário responsável pelo registro
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record Contratacao(
        BigDecimal valorTotalEstimado,
        BigDecimal valorTotalHomologado,
        Integer orcamentoSigilosoCodigo,
        String orcamentoSigilosoDescricao,
        String numeroControlePNCP,
        String linkSistemaOrigem,
        String linkProcessoEletronico,
        Integer anoCompra,
        Integer sequencialCompra,
        String numeroCompra,
        String processo,
        OrgaoEntidade orgaoEntidade,
        UnidadeOrgao unidadeOrgao,
        OrgaoEntidade orgaoSubRogado,
        UnidadeOrgao unidadeSubRogada,
        Long modalidadeId,
        String modalidadeNome,
        String justificativaPresencial,
        Long modoDisputaId,
        String modoDisputaNome,
        Long tipoInstrumentoConvocatorioCodigo,
        String tipoInstrumentoConvocatorioNome,
        AmparoLegal amparoLegal,
        String objetoCompra,
        String informacaoComplementar,
        Boolean srp,
        List<FonteOrcamentaria> fontesOrcamentarias,
        Boolean emendaParlamentar,
        LocalDateTime dataPublicacaoPncp,
        LocalDateTime dataAberturaProposta,
        LocalDateTime dataEncerramentoProposta,
        Long situacaoCompraId,
        String situacaoCompraNome,
        Boolean existeResultado,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao,
        LocalDateTime dataAtualizacaoGlobal,
        String usuarioNome) {}
