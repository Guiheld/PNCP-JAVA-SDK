package solutions.held.pncp.http;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Acumula e serializa os parâmetros de consulta (query string) de uma requisição à API do PNCP, ignorando valores nulos
 * e preservando a ordem de inserção.
 *
 * <p>
 *     É de uso interno da SDK, exposto apenas porque as classes de API residem em outro pacote.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public final class QueryParams {

    /**
     * Formato de data exigido pela API do PNCP em seus parâmetros (yyyyMMdd).
     */
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final Map<String, String> valores = new LinkedHashMap<>();

    /**
     * Adiciona um parâmetro, convertido para texto, quando o valor não é nulo.
     *
     * @param nome nome do parâmetro na query string
     * @param valor valor do parâmetro; quando {@code null}, o parâmetro é ignorado
     * @return esta instância, para encadeamento fluente
     * @since 23/11/2026
     */
    public QueryParams adicionar(String nome, Object valor) {
        if (valor != null) this.valores.put(nome, String.valueOf(valor));
        return this;
    }

    /**
     * Adiciona um parâmetro de data no formato exigido pelo PNCP (yyyyMMdd), quando a data não é nula.
     *
     * @param nome nome do parâmetro na query string
     * @param data data a formatar; quando {@code null}, o parâmetro é ignorado
     * @return esta instância, para encadeamento fluente
     * @since 23/11/2026
     */
    public QueryParams adicionarData(String nome, LocalDate data) {
        if (data != null) this.valores.put(nome, data.format(FORMATO_DATA));
        return this;
    }

    /**
     * Serializa os parâmetros acumulados como uma query string, com chave e valor codificados para URL.
     *
     * @return a query string sem o {@code ?} inicial, ou texto vazio quando não há parâmetros
     * @since 23/11/2026
     */
    public String paraQueryString() {
        StringBuilder construtor = new StringBuilder();
        for (Map.Entry<String, String> entrada : this.valores.entrySet()) {
            if (construtor.length() > 0) construtor.append('&');
            construtor
                    .append(URLEncoder.encode(entrada.getKey(), StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(entrada.getValue(), StandardCharsets.UTF_8));
        }
        return construtor.toString();
    }
}
