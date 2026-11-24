import java.time.LocalDate;
import solutions.held.pncp.PncpConsultaClient;
import solutions.held.pncp.exception.PncpException;
import solutions.held.pncp.exception.PncpRateLimitException;
import solutions.held.pncp.model.enums.ModalidadeContratacao;
import solutions.held.pncp.request.ConsultaContratacaoRequest;

/**
 * Exemplo executável: consulta contratações do tipo Pregão Eletrônico publicadas
 * em janeiro de 2024 em São Paulo e imprime as 20 primeiras, além de buscar uma
 * contratação específica.
 *
 * <p>Instruções de compilação e execução em {@code examples/README.md}.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class ExemploContratacoes {

    private ExemploContratacoes() {}

    /**
     * Executa o exemplo.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        PncpConsultaClient cliente = PncpConsultaClient.criar();

        ConsultaContratacaoRequest consulta = ConsultaContratacaoRequest.builder(
                        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31), ModalidadeContratacao.PREGAO_ELETRONICO)
                .uf("SP")
                .build();

        try {
            System.out.println("== Contratações (Pregão Eletrônico, SP, jan/2024) ==");
            cliente.contratacoes().streamPorPublicacao(consulta).limit(20).forEach(contratacao -> System.out.printf(
                    "%s | %s | estimado: %s%n",
                    contratacao.numeroControlePNCP(),
                    contratacao.objetoCompra(),
                    contratacao.valorTotalEstimado()));

            System.out.println();
            System.out.println("== Contratação específica ==");
            cliente.contratacoes()
                    .consultarPorId("00394460000141", 2024, 1)
                    .ifPresentOrElse(
                            c -> System.out.println("Objeto: " + c.objetoCompra()),
                            () -> System.out.println("Contratação não encontrada."));
        } catch (PncpRateLimitException e) {
            System.err.println("Limite de requisições excedido. Tente novamente mais tarde.");
        } catch (PncpException e) {
            System.err.println("Falha ao consultar o PNCP: " + e.getMessage());
        }
    }
}
