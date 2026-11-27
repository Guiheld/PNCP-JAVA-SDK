package solutions.held.pncp.model;

/**
 * Evento registrado em uma nota fiscal eletrônica.
 *
 * <p>
 *     Todos os campos são textuais, como exatamente entregues pela API.
 * </p>
 *
 * @param dataEvento data do evento, como texto
 * @param tipoEvento tipo do evento
 * @param evento descrição do evento
 * @param motivoEvento motivo do evento
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record EventoNotaFiscal(String dataEvento, String tipoEvento, String evento, String motivoEvento) {}
