package solutions.held.pncp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Item de um plano de contratações anual (PCA).
 *
 * @param numeroItem número sequencial do item no plano
 * @param classificacaoCatalogoId identificador da classificação no catálogo
 * @param nomeClassificacaoCatalogo nome da classificação no catálogo
 * @param categoriaItemPcaNome nome da categoria do item no PCA
 * @param descricaoItem descrição do item
 * @param classificacaoSuperiorCodigo código da classificação superior
 * @param classificacaoSuperiorNome nome da classificação superior
 * @param pdmCodigo código do padrão descritivo de material (PDM)
 * @param pdmDescricao descrição do padrão descritivo de material (PDM)
 * @param codigoItem código do item
 * @param unidadeFornecimento unidade de fornecimento
 * @param unidadeRequisitante unidade requisitante
 * @param quantidadeEstimada quantidade estimada
 * @param valorUnitario valor unitário estimado
 * @param valorTotal valor total estimado do item
 * @param valorOrcamentoExercicio valor do orçamento do exercício
 * @param dataDesejada data desejada para a contratação
 * @param grupoContratacaoCodigo código do grupo de contratação
 * @param grupoContratacaoNome nome do grupo de contratação
 * @param dataInclusao data e hora de inclusão do item no PNCP
 * @param dataAtualizacao data e hora da última atualização do item no PNCP
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record ItemPlanoContratacao(
        Integer numeroItem,
        Long classificacaoCatalogoId,
        String nomeClassificacaoCatalogo,
        String categoriaItemPcaNome,
        String descricaoItem,
        String classificacaoSuperiorCodigo,
        String classificacaoSuperiorNome,
        String pdmCodigo,
        String pdmDescricao,
        String codigoItem,
        String unidadeFornecimento,
        String unidadeRequisitante,
        BigDecimal quantidadeEstimada,
        BigDecimal valorUnitario,
        BigDecimal valorTotal,
        BigDecimal valorOrcamentoExercicio,
        LocalDate dataDesejada,
        String grupoContratacaoCodigo,
        String grupoContratacaoNome,
        LocalDateTime dataInclusao,
        LocalDateTime dataAtualizacao) {}
