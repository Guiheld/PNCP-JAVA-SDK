package solutions.held.pncp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Instrumento de cobrança (por exemplo, nota fiscal) vinculado a um contrato ou empenho.
 *
 * @param cnpj CNPJ do órgão do contrato
 * @param ano ano do contrato
 * @param sequencialContrato sequencial do contrato
 * @param sequencialInstrumentoCobranca sequencial do instrumento de cobrança
 * @param tipoInstrumentoCobranca tipo do instrumento de cobrança
 * @param numeroInstrumentoCobranca número do instrumento de cobrança
 * @param dataEmissaoDocumento data de emissão do documento
 * @param observacao observação do instrumento de cobrança
 * @param chaveNFe chave de acesso da nota fiscal eletrônica
 * @param fonteNFe fonte da nota fiscal eletrônica
 * @param dataConsultaNFe data e hora da consulta à nota fiscal eletrônica
 * @param statusResponseNFe status da resposta da consulta à nota fiscal
 * @param jsonResponseNFe conteúdo bruto da resposta da consulta à nota fiscal
 * @param notaFiscalEletronica dados estruturados da nota fiscal eletrônica
 * @param dataInclusao data e hora de inclusão no PNCP
 * @param dataAtualizacao data e hora da última atualização no PNCP
 * @param contrato contrato ao qual o instrumento de cobrança está vinculado
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record InstrumentoCobranca(
        String cnpj,
        Integer ano,
        Integer sequencialContrato,
        Integer sequencialInstrumentoCobranca,
        TipoInstrumentoCobranca tipoInstrumentoCobranca,
        String numeroInstrumentoCobranca,
        LocalDate dataEmissaoDocumento,
        String observacao,
        String chaveNFe,
        Long fonteNFe,
        LocalDateTime dataConsultaNFe,
        String statusResponseNFe,
        String jsonResponseNFe,
        NotaFiscalEletronica notaFiscalEletronica,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao,
        @JsonProperty("recuperarContratoDTO") Contrato contrato) {}
