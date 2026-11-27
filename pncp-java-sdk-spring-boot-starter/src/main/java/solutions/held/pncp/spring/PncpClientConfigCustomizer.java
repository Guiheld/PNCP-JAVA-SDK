package solutions.held.pncp.spring;

import solutions.held.pncp.PncpClientConfig;

/**
 * Ponto de extensão para ajustar programaticamente a configuração da SDK antes de o
 * {@link solutions.held.pncp.PncpConsultaClient} ser criado.
 *
 * <p>
 *     Declare um ou mais beans deste tipo no contexto Spring para, por exemplo, fornecer um
 *     {@link java.net.http.HttpClient} instrumentado (observabilidade, proxy), um
 *     {@link com.fasterxml.jackson.databind.ObjectMapper} próprio ou qualquer outro parâmetro não
 *     coberto por {@link PncpProperties}. Os customizadores são aplicados depois das propriedades
 *     {@code pncp.*}, de modo que têm a palavra final.
 * </p>
 *
 * @author Guilherme A. Held
 * @since 27/11/2026
 */
@FunctionalInterface
public interface PncpClientConfigCustomizer {

    /**
     * Ajusta o construtor da configuração da SDK.
     *
     * @param builder o construtor a ser ajustado
     * @since 27/11/2026
     */
    void customize(PncpClientConfig.Builder builder);
}
