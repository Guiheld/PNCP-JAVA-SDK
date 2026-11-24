package solutions.held.pncp.pagina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Testes de {@link Paginador} e de {@link Pagina}: percurso preguiçoso por várias
 * páginas, parada na última página e utilitários da página.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
class PaginadorTest {

    /**
     * O fluxo deve concatenar os itens de todas as páginas na ordem correta.
     */
    @Test
    void streamDe_percorreTodasAsPaginas() {
        List<Pagina<String>> paginas = List.of(
                new Pagina<>(List.of("a", "b"), 3L, 2L, 1L, 1L, false),
                new Pagina<>(List.of("c"), 3L, 2L, 2L, 0L, false));
        List<String> resultado =
                Paginador.streamDe(numero -> paginas.get(numero - 1)).collect(Collectors.toList());
        assertEquals(List.of("a", "b", "c"), resultado);
    }

    /**
     * O paginador não deve buscar páginas além da última (paginasRestantes zero).
     */
    @Test
    void streamDe_paraNaUltimaPagina() {
        AtomicInteger chamadas = new AtomicInteger();
        IntFunction<Pagina<String>> buscador = numero -> {
            chamadas.incrementAndGet();
            return new Pagina<>(List.of("x"), 1L, 1L, (long) numero, 0L, false);
        };
        Paginador.streamDe(buscador).forEach(item -> {});
        assertEquals(1, chamadas.get());
    }

    /**
     * {@link Pagina#temProximaPagina()} reflete o campo paginasRestantes.
     */
    @Test
    void temProximaPagina_refleteRestantes() {
        assertTrue(new Pagina<>(List.of(), 0L, 0L, 1L, 2L, false).temProximaPagina());
        assertFalse(new Pagina<>(List.of(), 0L, 0L, 1L, 0L, false).temProximaPagina());
    }

    /**
     * {@link Pagina#itensSeguros()} devolve lista vazia quando os itens são nulos.
     */
    @Test
    void itensSeguros_quandoNulo_retornaVazio() {
        assertTrue(new Pagina<String>(null, 0L, 0L, 1L, 0L, true).itensSeguros().isEmpty());
    }
}
