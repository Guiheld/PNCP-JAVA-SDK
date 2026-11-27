package solutions.held.pncp.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Plano de contratações anual (PCA) de uma unidade, com seus itens.
 *
 * @param itens itens que compõem o plano de contratações; a lista é imutável e nunca {@code null}
 * @param anoPca ano do plano de contratações
 * @param codigoUnidade código da unidade responsável pelo plano
 * @param nomeUnidade nome da unidade responsável pelo plano
 * @param orgaoEntidadeRazaoSocial razão social do órgão ou entidade
 * @param orgaoEntidadeCnpj CNPJ do órgão ou entidade
 * @param idPcaPncp identificador do plano no PNCP
 * @param dataPublicacaoPNCP data e hora de publicação do plano no PNCP
 * @param dataAtualizacaoGlobalPCA data e hora da última atualização global do plano
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record PlanoContratacao(
        List<ItemPlanoContratacao> itens,
        Integer anoPca,
        String codigoUnidade,
        String nomeUnidade,
        String orgaoEntidadeRazaoSocial,
        String orgaoEntidadeCnpj,
        String idPcaPncp,
        LocalDateTime dataPublicacaoPNCP,
        LocalDateTime dataAtualizacaoGlobalPCA) {

    /**
     * Normaliza os itens para uma lista imutável, tratando {@code null} como lista vazia.
     *
     * @since 27/11/2026
     */
    public PlanoContratacao {
        itens = itens == null ? List.of() : List.copyOf(itens);
    }
}
