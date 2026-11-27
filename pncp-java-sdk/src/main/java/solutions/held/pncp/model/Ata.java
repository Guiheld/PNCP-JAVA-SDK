package solutions.held.pncp.model;

import java.time.LocalDateTime;

/**
 * Ata de registro de preço publicada no PNCP.
 *
 * @param numeroControlePNCPAta número de controle da ata no PNCP
 * @param numeroAtaRegistroPreco número da ata de registro de preço
 * @param anoAta ano da ata
 * @param numeroControlePNCPCompra número de controle da compra no PNCP
 * @param cancelado indica se a ata foi cancelada
 * @param dataCancelamento data e hora do cancelamento, quando houver
 * @param dataAssinatura data e hora de assinatura da ata
 * @param vigenciaInicio data e hora de início de vigência
 * @param vigenciaFim data e hora de fim de vigência
 * @param dataPublicacaoPncp data e hora de publicação no PNCP
 * @param dataInclusao data e hora de inclusão no PNCP
 * @param dataAtualizacao data e hora da última atualização no PNCP
 * @param dataAtualizacaoGlobal data e hora da última atualização global no PNCP
 * @param usuario nome do usuário responsável pelo registro
 * @param objetoContratacao objeto da contratação
 * @param cnpjOrgao CNPJ do órgão
 * @param nomeOrgao nome do órgão
 * @param cnpjOrgaoSubrogado CNPJ do órgão sub-rogado, quando houver
 * @param nomeOrgaoSubrogado nome do órgão sub-rogado, quando houver
 * @param codigoUnidadeOrgao código da unidade do órgão
 * @param nomeUnidadeOrgao nome da unidade do órgão
 * @param codigoUnidadeOrgaoSubrogado código da unidade do órgão sub-rogado, quando houver
 * @param nomeUnidadeOrgaoSubrogado nome da unidade do órgão sub-rogado, quando houver
 * @param possibilidadeAdesao indica se há possibilidade de adesão à ata
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record Ata(
        String numeroControlePNCPAta,
        String numeroAtaRegistroPreco,
        Integer anoAta,
        String numeroControlePNCPCompra,
        Boolean cancelado,
        LocalDateTime dataCancelamento,
        LocalDateTime dataAssinatura,
        LocalDateTime vigenciaInicio,
        LocalDateTime vigenciaFim,
        LocalDateTime dataPublicacaoPncp,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao,
        LocalDateTime dataAtualizacaoGlobal,
        String usuario,
        String objetoContratacao,
        String cnpjOrgao,
        String nomeOrgao,
        String cnpjOrgaoSubrogado,
        String nomeOrgaoSubrogado,
        String codigoUnidadeOrgao,
        String nomeUnidadeOrgao,
        String codigoUnidadeOrgaoSubrogado,
        String nomeUnidadeOrgaoSubrogado,
        Boolean possibilidadeAdesao) {}
