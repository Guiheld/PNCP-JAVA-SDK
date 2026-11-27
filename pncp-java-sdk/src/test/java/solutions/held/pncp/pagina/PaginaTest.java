package solutions.held.pncp.pagina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Testes de {@link Pagina}: os itens são sempre imutáveis e nunca nulos.
 *
 * @author Guilherme A. Held
 * @since 27/11/2026
 */
class PaginaTest {

    /**
     * Itens nulos viram uma lista vazia, tanto em {@link Pagina#itens()} quanto em {@link Pagina#itensSeguros()}.
     */
    @Test
    void itensNulos_viramListaVazia() {
        Pagina<String> pagina = new Pagina<>(null, 0L, 0L, 1L, 0L, Boolean.TRUE);
        assertTrue(pagina.itens().isEmpty());
        assertTrue(pagina.itensSeguros().isEmpty());
    }

    /**
     * Os itens são uma cópia defensiva imutável: alterar a lista de origem não afeta a página, e a lista exposta
     * rejeita modificação.
     */
    @Test
    void itens_copiaDefensivaImutavel() {
        List<String> origem = new ArrayList<>(List.of("a", "b"));
        Pagina<String> pagina = new Pagina<>(origem, 2L, 1L, 1L, 0L, Boolean.FALSE);

        origem.add("c");

        assertEquals(2, pagina.itens().size());
        assertThrows(UnsupportedOperationException.class, () -> pagina.itens().add("x"));
    }
}
