package solutions.held.pncp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contratação (compra) do PNCP em sua forma resumida, tal como aparece nas listas das consultas por data de publicação,
 * por atualização e por recebimento de propostas aberto.
 *
 * <p>
 *     Em relação à {@link Contratacao} detalhada, não traz os campos de orçamento sigiloso nem o indicador de existência
 *     de resultado.
 *     Os códigos de modalidade, modo de disputa e situação são mantidos em sua forma bruta; para resolvê-los em constantes
 *     tipadas use os enums do pacote {@code solutions.held.pncp.model.enums}.
 * </p>
 *
 * @param sequencialCompra sequencial da compra no órgão
 * @param anoCompra ano da compra
 * @param dataPublicacaoPncp data e hora de publicação no PNCP
 * @param dataAtualizacaoGlobal data e hora da última atualização global no PNCP
 * @param srp indica se é sistema de registro de preços
 * @param emendaParlamentar indica se há emenda parlamentar
 * @param numeroCompra número da compra
 * @param processo número do processo
 * @param objetoCompra objeto da compra
 * @param informacaoComplementar informação complementar
 * @param dataAberturaProposta data e hora de abertura das propostas
 * @param dataEncerramentoProposta data e hora de encerramento das propostas
 * @param unidadeOrgao unidade administrativa responsável
 * @param orgaoSubRogado órgão ou entidade sub-rogado, quando houver
 * @param unidadeSubRogada unidade administrativa sub-rogada, quando houver
 * @param amparoLegal amparo legal da contratação
 * @param linkSistemaOrigem link para o sistema de origem
 * @param linkProcessoEletronico link para o processo eletrônico
 * @param justificativaPresencial justificativa para realização presencial
 * @param numeroControlePNCP número de controle da contratação no PNCP
 * @param orgaoEntidade órgão ou entidade responsável
 * @param valorTotalHomologado valor total homologado da contratação
 * @param dataInclusao data e hora de inclusão no PNCP
 * @param dataAtualizacao data e hora da última atualização no PNCP
 * @param valorTotalEstimado valor total estimado da contratação
 * @param modalidadeId código da modalidade de contratação
 * @param modalidadeNome nome da modalidade de contratação
 * @param modoDisputaId código do modo de disputa
 * @param modoDisputaNome nome do modo de disputa
 * @param tipoInstrumentoConvocatorioCodigo código do tipo de instrumento convocatório
 * @param tipoInstrumentoConvocatorioNome nome do tipo de instrumento convocatório
 * @param fontesOrcamentarias fontes orçamentárias da contratação
 * @param situacaoCompraId código da situação da contratação
 * @param situacaoCompraNome nome da situação da contratação
 * @param usuarioNome nome do usuário responsável pelo registro
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ContratacaoResumo(
        Integer sequencialCompra,
        Integer anoCompra,
        LocalDateTime dataPublicacaoPncp,
        LocalDateTime dataAtualizacaoGlobal,
        Boolean srp,
        Boolean emendaParlamentar,
        String numeroCompra,
        String processo,
        String objetoCompra,
        String informacaoComplementar,
        LocalDateTime dataAberturaProposta,
        LocalDateTime dataEncerramentoProposta,
        UnidadeOrgao unidadeOrgao,
        OrgaoEntidade orgaoSubRogado,
        UnidadeOrgao unidadeSubRogada,
        AmparoLegal amparoLegal,
        String linkSistemaOrigem,
        String linkProcessoEletronico,
        String justificativaPresencial,
        String numeroControlePNCP,
        OrgaoEntidade orgaoEntidade,
        BigDecimal valorTotalHomologado,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao,
        BigDecimal valorTotalEstimado,
        Long modalidadeId,
        String modalidadeNome,
        Long modoDisputaId,
        String modoDisputaNome,
        Long tipoInstrumentoConvocatorioCodigo,
        String tipoInstrumentoConvocatorioNome,
        List<FonteOrcamentaria> fontesOrcamentarias,
        Long situacaoCompraId,
        String situacaoCompraNome,
        String usuarioNome) {}
