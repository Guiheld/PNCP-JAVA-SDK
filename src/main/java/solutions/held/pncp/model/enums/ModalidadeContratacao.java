package solutions.held.pncp.model.enums;

import java.util.Optional;

/**
 * Modalidade de contratação prevista na Lei n.º 14.133/2021, conforme a tabela de domínio publicada nos manuais do PNCP.
 *
 * <p>
 *     É aceita como parâmetro em várias consultas (o {@code codigoModalidadeContratacao}) e também aparece nas respostas.
 *     Ao desserializar um código ainda não mapeado por esta biblioteca, prefira {@link #fromCodigo(Integer)}, que devolve vazio em vez de falhar.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public enum ModalidadeContratacao {
    LEILAO_ELETRONICO(1, "Leilão - Eletrônico"),

    DIALOGO_COMPETITIVO(2, "Diálogo Competitivo"),

    CONCURSO(3, "Concurso"),

    CONCORRENCIA_ELETRONICA(4, "Concorrência - Eletrônica"),

    CONCORRENCIA_PRESENCIAL(5, "Concorrência - Presencial"),

    PREGAO_ELETRONICO(6, "Pregão - Eletrônico"),

    PREGAO_PRESENCIAL(7, "Pregão - Presencial"),

    DISPENSA_DE_LICITACAO(8, "Dispensa de Licitação"),

    INEXIGIBILIDADE(9, "Inexigibilidade"),

    MANIFESTACAO_DE_INTERESSE(10, "Manifestação de Interesse"),

    PRE_QUALIFICACAO(11, "Pré-qualificação"),

    CREDENCIAMENTO(12, "Credenciamento"),

    LEILAO_PRESENCIAL(13, "Leilão - Presencial");

    private final Integer codigo;

    private final String descricao;

    ModalidadeContratacao(Integer codigo, String descricao) {
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
     * Resolve a modalidade a partir de seu código numérico.
     *
     * @param codigo código da modalidade conforme a tabela de domínio do PNCP
     * @return a modalidade correspondente, ou vazio quando o código não é conhecido
     * @since 23/11/2026
     */
    public static Optional<ModalidadeContratacao> fromCodigo(Integer codigo) {
        for (ModalidadeContratacao modalidade : values()) {
            if (modalidade.codigo != null && modalidade.codigo.equals(codigo)) return Optional.of(modalidade);
        }
        return Optional.empty();
    }
}
