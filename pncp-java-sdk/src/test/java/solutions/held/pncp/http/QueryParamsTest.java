package solutions.held.pncp.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Testes de {@link QueryParams}: formatação de datas, descarte de nulos, ordem de inserção e codificação para URL.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
class QueryParamsTest {

    /**
     * Datas devem ser serializadas no formato yyyyMMdd exigido pelo PNCP.
     */
    @Test
    void adicionarData_formatoYyyyMMdd() {
        String queryString = new QueryParams()
                .adicionarData("dataInicial", LocalDate.of(2024, 1, 5))
                .paraQueryString();
        assertEquals("dataInicial=20240105", queryString);
    }

    /**
     * Valores nulos não devem gerar parâmetros na query string.
     */
    @Test
    void adicionar_ignoraValoresNulos() {
        String queryString =
                new QueryParams().adicionar("a", null).adicionar("b", 2).paraQueryString();
        assertEquals("b=2", queryString);
    }

    /**
     * A ordem de inserção é preservada e os valores são codificados para URL.
     */
    @Test
    void adicionar_preservaOrdemEEncodaValores() {
        String queryString = new QueryParams()
                .adicionar("uf", "SP")
                .adicionar("objeto", "água potável")
                .paraQueryString();
        assertEquals("uf=SP&objeto=%C3%A1gua+pot%C3%A1vel", queryString);
    }

    /**
     * Sem parâmetros, a query string é vazia.
     */
    @Test
    void paraQueryString_semParametros_retornaVazio() {
        assertEquals("", new QueryParams().paraQueryString());
    }
}
