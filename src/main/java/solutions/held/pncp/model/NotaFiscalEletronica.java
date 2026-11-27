package solutions.held.pncp.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Nota fiscal eletrônica vinculada a um instrumento de cobrança.
 *
 * <p>Alguns campos de data e de valor chegam como texto livre da API e, por isso,
 * são mantidos como {@link String}; apenas {@code dataInclusao} e {@code
 * dataAtualizacao} vêm em formato de data e hora.
 *
 * @param instrumentoCobrancaId identificador do instrumento de cobrança
 * @param chave chave de acesso da nota fiscal
 * @param nfTransparenciaID identificador da nota no Portal da Transparência
 * @param numero número da nota fiscal
 * @param serie série da nota fiscal
 * @param dataEmissao data de emissão, como texto
 * @param niEmitente número de identificação do emitente
 * @param nomeEmitente nome do emitente
 * @param nomeMunicipioEmitente nome do município do emitente
 * @param codigoOrgaoDestinatario código do órgão destinatário
 * @param nomeOrgaoDestinatario nome do órgão destinatário
 * @param codigoOrgaoSuperiorDestinatario código do órgão superior destinatário
 * @param nomeOrgaoSuperiorDestinatario nome do órgão superior destinatário
 * @param valorNotaFiscal valor da nota fiscal, como texto
 * @param tipoEventoMaisRecente tipo do evento mais recente
 * @param dataTipoEventoMaisRecente data do evento mais recente, como texto
 * @param dataInclusao data e hora de inclusão no PNCP
 * @param dataAtualizacao data e hora da última atualização no PNCP
 * @param itens itens da nota fiscal; a lista é imutável e nunca {@code null}
 * @param eventos eventos registrados na nota fiscal; a lista é imutável e nunca {@code null}
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record NotaFiscalEletronica(
        Long instrumentoCobrancaId,
        String chave,
        Long nfTransparenciaID,
        Long numero,
        Integer serie,
        String dataEmissao,
        String niEmitente,
        String nomeEmitente,
        String nomeMunicipioEmitente,
        String codigoOrgaoDestinatario,
        String nomeOrgaoDestinatario,
        String codigoOrgaoSuperiorDestinatario,
        String nomeOrgaoSuperiorDestinatario,
        String valorNotaFiscal,
        String tipoEventoMaisRecente,
        String dataTipoEventoMaisRecente,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao,
        List<ItemNotaFiscal> itens,
        List<EventoNotaFiscal> eventos) {

    /**
     * Normaliza os itens e os eventos para listas imutáveis, tratando {@code null} como lista vazia.
     *
     * @since 27/11/2026
     */
    public NotaFiscalEletronica {
        itens = itens == null ? List.of() : List.copyOf(itens);
        eventos = eventos == null ? List.of() : List.copyOf(eventos);
    }
}
