package solutions.held.pncp.http;

import solutions.held.pncp.exception.PncpException;

/**
 * Limitador de vazão client-side que espaça as requisições à API do PNCP, respeitando os limites não documentados do portal.
 *
 * <p>
 *     Implementa um esquema simples de intervalo mínimo entre requisições, derivado de uma vazão em requisições por segundo.
 *     É seguro para uso concorrente: as chamadas a {@link #adquirir()} são serializadas.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class Throttler {

    private final long intervaloNanos;

    private long proximoInstantePermitidoNanos;

    public Throttler(double vazaoPorSegundo) {
        this.intervaloNanos = vazaoPorSegundo <= 0 ? 0L : (long) (1_000_000_000.0 / vazaoPorSegundo);
        this.proximoInstantePermitidoNanos = System.nanoTime();
    }

    /**
     * Bloqueia o chamador, se necessário, até que uma nova requisição seja permitida conforme a vazão configurada.
     *
     * <p>
     *     Quando a vazão configurada é zero ou negativa, nenhuma limitação é aplicada e a função retorna imediatamente.
     * </p>
     *
     * @since 23/11/2026
     */
    public synchronized void adquirir() {
        if (this.intervaloNanos == 0L) return;
        long agora = System.nanoTime();
        long esperaNanos = this.proximoInstantePermitidoNanos - agora;
        if (esperaNanos > 0) {
            try {
                Thread.sleep(esperaNanos / 1_000_000L, (int) (esperaNanos % 1_000_000L));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PncpException("Espera por vazão de requisições ao PNCP interrompida.", e);
            }
        }
        this.proximoInstantePermitidoNanos = Math.max(agora, this.proximoInstantePermitidoNanos) + this.intervaloNanos;
    }
}
