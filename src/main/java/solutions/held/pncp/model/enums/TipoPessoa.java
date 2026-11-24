package solutions.held.pncp.model.enums;

/**
 * Tipo de pessoa de um fornecedor em um contrato.
 *
 * <p>
 *     Os nomes das constantes correspondem exatamente aos valores transmitidos pela API, de modo que a desserialização
 *     é feita diretamente pelo nome.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public enum TipoPessoa {
    PJ("Pessoa Jurídica"),

    PF("Pessoa Física"),

    PE("Pessoa Estrangeira");

    private final String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String descricao() {
        return this.descricao;
    }
}
