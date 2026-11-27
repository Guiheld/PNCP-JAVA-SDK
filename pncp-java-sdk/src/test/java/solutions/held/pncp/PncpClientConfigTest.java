package solutions.held.pncp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.http.HttpClient;
import org.junit.jupiter.api.Test;

/**
 * Testes de {@link PncpClientConfig}: valores padrão e validações do construtor.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
class PncpClientConfigTest {

    /**
     * A configuração padrão deve trazer o endereço público e os limites padrão.
     */
    @Test
    void padrao_trazValoresPadrao() {
        PncpClientConfig config = PncpClientConfig.padrao();
        assertEquals(PncpClientConfig.URL_BASE_PADRAO, config.urlBase());
        assertEquals(3, config.maxTentativas());
        assertEquals(PncpClientConfig.VAZAO_PADRAO_POR_SEGUNDO, config.vazaoPorSegundo());
        assertNotNull(config.objectMapper());
    }

    /**
     * O cabeçalho {@code User-Agent} padrão traz a versão real da SDK (filtrada do POM), não o rótulo
     * de desenvolvimento.
     */
    @Test
    void padrao_userAgentTrazVersaoReal() {
        String userAgent = PncpClientConfig.padrao().userAgent();
        assertTrue(userAgent.startsWith("PNCP-JAVA-SDK/"));
        assertNotEquals("PNCP-JAVA-SDK/dev", userAgent);
    }

    /**
     * O número máximo de tentativas deve ser pelo menos 1.
     */
    @Test
    void build_maxTentativasInvalido_lancaExcecao() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PncpClientConfig.builder().maxTentativas(0).build());
    }

    /**
     * O endereço base não pode ser vazio.
     */
    @Test
    void build_urlBaseVazia_lancaExcecao() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PncpClientConfig.builder().urlBase("  ").build());
    }

    /**
     * O multiplicador do recuo exponencial deve ser pelo menos 1,0.
     */
    @Test
    void build_multiplicadorInvalido_lancaExcecao() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PncpClientConfig.builder().multiplicadorBackoff(0.5).build());
    }

    /**
     * O {@link HttpClient} é opcional: ausente por padrão e preservado quando informado.
     */
    @Test
    void httpClient_opcional_ausentePorPadraoEPreservadoQuandoInformado() {
        assertTrue(PncpClientConfig.padrao().httpClient().isEmpty());

        HttpClient cliente = HttpClient.newHttpClient();
        PncpClientConfig config = PncpClientConfig.builder().httpClient(cliente).build();
        assertTrue(config.httpClient().isPresent());
        assertSame(cliente, config.httpClient().get());
    }
}
