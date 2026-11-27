package solutions.held.pncp.model.enums;

import java.util.Optional;

/**
 * Situação de uma contratação no PNCP, conforme a tabela de domínio publicada nos manuais do portal.
 *
 * <p>
 *     Nas respostas, a API entrega tanto o código ({@code situacaoCompraId}) quanto o nome ({@code situacaoCompraNome});
 *     este enum permite resolver o código para uma constante tipada. Ao encontrar um código ainda não mapeado, prefira
 *     {@link #fromCodigo(Integer)}, que devolve vazio em vez de falhar.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public enum SituacaoCompra {
    DIVULGADA_NO_PNCP(1, "Divulgada no PNCP"),

    REVOGADA(2, "Revogada"),

    ANULADA(3, "Anulada"),

    SUSPENSA(4, "Suspensa");

    private final Integer codigo;

    private final String descricao;

    SituacaoCompra(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer codigo() {
        return this.codigo;
    }

    public String descricao() {
        return this.descricao;
    }

    /**
     * Resolve a situação da contratação a partir de seu código numérico.
     *
     * @param codigo código da situação conforme a tabela de domínio do PNCP
     * @return a situação correspondente, ou vazio quando o código não é conhecido
     * @since 23/11/2026
     */
    public static Optional<SituacaoCompra> fromCodigo(Integer codigo) {
        for (SituacaoCompra situacao : values()) {
            if (situacao.codigo != null && situacao.codigo.equals(codigo)) return Optional.of(situacao);
        }
        return Optional.empty();
    }
}
