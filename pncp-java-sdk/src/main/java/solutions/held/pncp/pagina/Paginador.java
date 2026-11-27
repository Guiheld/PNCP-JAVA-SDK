package solutions.held.pncp.pagina;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Converte uma sequência de páginas da API do PNCP em um {@link Stream} que percorre todos os itens de forma lazy,
 * buscando a página seguinte apenas quando a anterior se esgota.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class Paginador {

    private Paginador() {}

    /**
     * Cria um fluxo preguiçoso sobre todas as páginas, a partir de uma função que busca a página de um dado número (iniciando em 1).
     *
     * <p>
     *     A busca é sequencial e sob demanda: a próxima página só é solicitada quando o fluxo consome o último item da
     *     página corrente e {@link solutions.held.pncp.pagina.Pagina#temProximaPagina()} indica que há continuação.
     * </p>
     *
     * @param <T> tipo dos itens percorridos
     * @param buscadorDePagina função que recebe o número da página e devolve a página correspondente
     * @return um fluxo preguiçoso com os itens de todas as páginas
     * @since 23/11/2026
     */
    public static <T> Stream<T> streamDe(IntFunction<Pagina<T>> buscadorDePagina) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new IteradorDePaginas<>(buscadorDePagina), Spliterator.ORDERED | Spliterator.NONNULL),
                Boolean.FALSE);
    }

    /**
     * Iterador que avança pelas páginas sob demanda, mantendo o número da próxima página a buscar e o iterador dos itens
     * da página corrente.
     *
     * @param <T> tipo dos itens percorridos
     * @since 23/11/2026
     */
    private static final class IteradorDePaginas<T> implements Iterator<T> {

        private final IntFunction<Pagina<T>> buscadorDePagina;

        private Integer proximaPagina = 1;

        private Iterator<T> itensDaPaginaAtual = Collections.emptyIterator();

        private Boolean haMaisPaginas = Boolean.TRUE;

        IteradorDePaginas(IntFunction<Pagina<T>> buscadorDePagina) {
            this.buscadorDePagina = buscadorDePagina;
        }

        /**
         * Indica se há um próximo item, buscando páginas subsequentes enquanto a página corrente estiver esgotada e
         * houver continuação.
         *
         * @return {@code true} se ainda há itens a percorrer
         * @since 23/11/2026
         */
        @Override
        public boolean hasNext() {
            while (!this.itensDaPaginaAtual.hasNext() && Boolean.TRUE.equals(this.haMaisPaginas)) {
                Pagina<T> pagina = this.buscadorDePagina.apply(this.proximaPagina);
                this.itensDaPaginaAtual = pagina.itensSeguros().iterator();
                this.haMaisPaginas = pagina.temProximaPagina();
                this.proximaPagina++;
            }
            return this.itensDaPaginaAtual.hasNext();
        }

        /**
         * Retorna o próximo item da sequência.
         *
         * @return o próximo item
         * @throws NoSuchElementException se não houver mais itens
         * @since 23/11/2026
         */
        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return this.itensDaPaginaAtual.next();
        }
    }
}
