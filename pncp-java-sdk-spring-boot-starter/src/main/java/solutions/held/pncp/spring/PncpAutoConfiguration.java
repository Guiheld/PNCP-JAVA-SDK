package solutions.held.pncp.spring;

import java.util.function.Consumer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import solutions.held.pncp.PncpClientConfig;
import solutions.held.pncp.PncpConsultaClient;

/**
 * Autoconfiguração Spring Boot da SDK de consulta do PNCP.
 *
 * <p>
 *     Com a SDK no classpath, expõe um {@link PncpClientConfig} montado a partir das propriedades
 *     {@code pncp.*} (ver {@link PncpProperties}) e um {@link PncpConsultaClient} pronto para injeção. Ambos
 *     os beans recuam se a aplicação já declarar o seu próprio equivalente ({@link ConditionalOnMissingBean}).
 *     Ajustes programáticos (por exemplo, um {@code HttpClient} instrumentado ou um {@code ObjectMapper}
 *     próprio) são feitos via {@link PncpClientConfigCustomizer}.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 27/11/2026
 */
@AutoConfiguration
@ConditionalOnClass(PncpConsultaClient.class)
@EnableConfigurationProperties(PncpProperties.class)
public class PncpAutoConfiguration {

    /**
     * Monta a configuração da SDK a partir das propriedades e dos customizadores.
     *
     * @param propriedades propriedades {@code pncp.*} (apenas os campos preenchidos sobrescrevem os padrões)
     * @param customizadores ajustes programáticos opcionais, aplicados por último
     * @return a configuração imutável da SDK
     * @since 27/11/2026
     */
    @Bean
    @ConditionalOnMissingBean
    public PncpClientConfig pncpClientConfig(
            PncpProperties propriedades, ObjectProvider<PncpClientConfigCustomizer> customizadores) {
        PncpClientConfig.Builder construtor = PncpClientConfig.builder();
        aplicar(propriedades.urlBase(), construtor::urlBase);
        aplicar(propriedades.timeoutConexao(), construtor::timeoutConexao);
        aplicar(propriedades.timeoutResposta(), construtor::timeoutResposta);
        aplicar(propriedades.maxTentativas(), construtor::maxTentativas);
        aplicar(propriedades.backoffInicial(), construtor::backoffInicial);
        aplicar(propriedades.backoffMaximo(), construtor::backoffMaximo);
        aplicar(propriedades.multiplicadorBackoff(), construtor::multiplicadorBackoff);
        aplicar(propriedades.vazaoPorSegundo(), construtor::vazaoPorSegundo);
        aplicar(propriedades.userAgent(), construtor::userAgent);
        customizadores.orderedStream().forEach(customizador -> customizador.customize(construtor));
        return construtor.build();
    }

    /**
     * Cria o {@link PncpConsultaClient} a partir da configuração resolvida.
     *
     * @param config a configuração da SDK
     * @return o cliente pronto para uso
     * @since 27/11/2026
     */
    @Bean
    @ConditionalOnMissingBean
    public PncpConsultaClient pncpConsultaClient(PncpClientConfig config) {
        return PncpConsultaClient.comConfig(config);
    }

    private static <T> void aplicar(T valor, Consumer<T> ajuste) {
        if (valor != null) {
            ajuste.accept(valor);
        }
    }
}
