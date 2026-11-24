package solutions.held.pncp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
