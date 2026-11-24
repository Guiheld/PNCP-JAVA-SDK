package solutions.held.pncp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import solutions.held.pncp.model.enums.TipoPessoa;

/**
 * Contrato ou empenho publicado no PNCP.
 *
 * @param numeroControlePncpAta número de controle da ata no PNCP, quando oriundo de ata
 * @param numeroControlePncpCompra número de controle da compra no PNCP
 * @param numeroControlePNCP número de controle do contrato no PNCP
 * @param codigoPaisFornecedor código do país do fornecedor
 * @param dataAssinatura data de assinatura do contrato
 * @param dataVigenciaInicio data de início de vigência
 * @param dataVigenciaFim data de fim de vigência
 * @param dataPublicacaoPncp data e hora de publicação no PNCP
 * @param dataAtualizacao data e hora da última atualização no PNCP
 * @param dataAtualizacaoGlobal data e hora da última atualização global no PNCP
 * @param emendaParlamentar indica se há emenda parlamentar
 * @param receita indica se o contrato é de receita
 * @param temRemanejamento indica se houve remanejamento
 * @param frutoAdesao indica se o contrato é fruto de adesão
 * @param processo número do processo
 * @param informacaoComplementar informação complementar
 * @param orgaoEntidade órgão ou entidade responsável
 * @param unidadeOrgao unidade administrativa responsável
 * @param orgaoSubRogado órgão ou entidade sub-rogado, quando houver
 * @param unidadeSubRogada unidade administrativa sub-rogada, quando houver
 * @param tipoPessoa tipo de pessoa do fornecedor
 * @param niFornecedor número de identificação do fornecedor
 * @param nomeRazaoSocialFornecedor nome ou razão social do fornecedor
 * @param tipoPessoaSubContratada tipo de pessoa do fornecedor subcontratado
 * @param niFornecedorSubContratado número de identificação do fornecedor subcontratado
 * @param nomeFornecedorSubContratado nome do fornecedor subcontratado
 * @param tipoContrato tipo do contrato
 * @param categoriaProcesso categoria do processo
 * @param numeroContratoEmpenho número do contrato ou empenho
 * @param numeroParcelas número de parcelas
 * @param numeroRetificacao número da retificação
 * @param objetoContrato objeto do contrato
 * @param valorInicial valor inicial do contrato
 * @param valorParcela valor da parcela
 * @param valorGlobal valor global do contrato
 * @param valorAcumulado valor acumulado do contrato
 * @param identificadorCipi identificador no CIPI
 * @param urlCipi URL do CIPI
 * @param anoContrato ano do contrato
 * @param sequencialContrato sequencial do contrato no órgão
 * @param usuarioNome nome do usuário responsável pelo registro
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record Contrato(
        String numeroControlePncpAta,
        String numeroControlePncpCompra,
        String numeroControlePNCP,
        String codigoPaisFornecedor,
        LocalDate dataAssinatura,
        LocalDate dataVigenciaInicio,
        LocalDate dataVigenciaFim,
        LocalDateTime dataPublicacaoPncp,
        LocalDateTime dataAtualizacao,
        LocalDateTime dataAtualizacaoGlobal,
        Boolean emendaParlamentar,
        Boolean receita,
        Boolean temRemanejamento,
        Boolean frutoAdesao,
        String processo,
        String informacaoComplementar,
        OrgaoEntidade orgaoEntidade,
        UnidadeOrgao unidadeOrgao,
        OrgaoEntidade orgaoSubRogado,
        UnidadeOrgao unidadeSubRogada,
        TipoPessoa tipoPessoa,
        String niFornecedor,
        String nomeRazaoSocialFornecedor,
        TipoPessoa tipoPessoaSubContratada,
        String niFornecedorSubContratado,
        String nomeFornecedorSubContratado,
        TipoContrato tipoContrato,
        Categoria categoriaProcesso,
        String numeroContratoEmpenho,
        Integer numeroParcelas,
        Integer numeroRetificacao,
        String objetoContrato,
        BigDecimal valorInicial,
        BigDecimal valorParcela,
        BigDecimal valorGlobal,
        BigDecimal valorAcumulado,
        String identificadorCipi,
        String urlCipi,
        Integer anoContrato,
        Integer sequencialContrato,
        String usuarioNome) {}
