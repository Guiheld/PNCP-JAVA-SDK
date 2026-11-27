package solutions.held.pncp.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import solutions.held.pncp.PncpClientConfig;
import solutions.held.pncp.PncpConsultaClient;

/**
 * Testes da auto-configuração Spring Boot ({@link PncpAutoConfiguration}): criação dos beans padrão,
 * aplicação das propriedades, precedência do customizador e respeito a um cliente próprio.
 *
 * @author Guilherme A. Held
 * @since 27/11/2026
 */
class PncpAutoConfiguracaoTest {

    private final ApplicationContextRunner runner =
            new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(PncpAutoConfiguration.class));

    @Test
    void criaClienteEConfigPorPadrao() {
        runner.run(contexto -> {
            assertThat(contexto).hasSingleBean(PncpConsultaClient.class);
            assertThat(contexto).hasSingleBean(PncpClientConfig.class);
        });
    }

    @Test
    void aplicaPropriedadesDeclaradas() {
        runner.withPropertyValues("pncp.max-tentativas=7", "pncp.user-agent=meu-agente/1.0")
                .run(contexto -> {
                    PncpClientConfig config = contexto.getBean(PncpClientConfig.class);
                    assertThat(config.maxTentativas()).isEqualTo(7);
                    assertThat(config.userAgent()).isEqualTo("meu-agente/1.0");
                });
    }

    @Test
    void customizadorTemAPalavraFinal() {
        PncpClientConfigCustomizer customizador = construtor -> construtor.maxTentativas(9);
        runner.withPropertyValues("pncp.max-tentativas=7")
                .withBean(PncpClientConfigCustomizer.class, () -> customizador)
                .run(contexto -> assertThat(
                                contexto.getBean(PncpClientConfig.class).maxTentativas())
                        .isEqualTo(9));
    }

    @Test
    void respeitaClienteDoUsuario() {
        PncpConsultaClient proprio = PncpConsultaClient.criar();
        runner.withBean("pncpClientProprio", PncpConsultaClient.class, () -> proprio)
                .run(contexto ->
                        assertThat(contexto.getBean(PncpConsultaClient.class)).isSameAs(proprio));
    }
}
