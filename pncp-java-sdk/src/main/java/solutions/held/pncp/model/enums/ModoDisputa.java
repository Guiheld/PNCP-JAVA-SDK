package solutions.held.pncp.model.enums;

import java.util.Optional;

/**
 * Modo de disputa de uma contratação, conforme a tabela de domínio publicada nos manuais do PNCP.
 *
 * <p>
 *     Pode ser informado como parâmetro ({@code codigoModoDisputa}) em algumas consultas e também aparece nas respostas.
 *     Ao desserializar um código ainda não mapeado por esta biblioteca, prefira {@link #fromCodigo(Integer)}, que devolve
 *     vazio em vez de falhar.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public enum ModoDisputa {
    ABERTO(1, "Aberto"),

    FECHADO(2, "Fechado"),

    ABERTO_FECHADO(3, "Aberto-Fechado"),

    DISPENSA_COM_DISPUTA(4, "Dispensa Com Disputa"),

    NAO_SE_APLICA(5, "Não se aplica"),

    FECHADO_ABERTO(6, "Fechado-Aberto");

    private final Integer codigo;

    private final String descricao;

    ModoDisputa(Integer codigo, String descricao) {
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
     * Resolve o modo de disputa a partir de seu código numérico.
     *
     * @param codigo código do modo de disputa conforme a tabela de domínio do PNCP
     * @return o modo de disputa correspondente, ou vazio quando o código não é conhecido
     * @since 23/11/2026
     */
    public static Optional<ModoDisputa> fromCodigo(Integer codigo) {
        for (ModoDisputa modo : values()) {
            if (modo.codigo != null && modo.codigo.equals(codigo)) return Optional.of(modo);
        }
        return Optional.empty();
    }
}
