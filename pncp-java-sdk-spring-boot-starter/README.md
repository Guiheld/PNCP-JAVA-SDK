# pncp-java-sdk-spring-boot-starter

Auto-configuração **Spring Boot** para o [PNCP-JAVA-SDK](../README.md). Adicione a dependência e
injete um `PncpConsultaClient` pronto, configurado por `pncp.*` no `application.yml` — sem nenhum
`@Bean` manual.

> Requer **Spring Boot 3.x** (baseline Java 17). Disponível a partir do release **0.2.0**.

## Sumário

- [Instalação](#instalação)
- [Começo rápido](#começo-rápido)
- [Configuração](#configuração)
- [Sobrescrevendo o cliente](#sobrescrevendo-o-cliente)
- [Ajuste programático](#ajuste-programático)
- [Cache com Spring](#cache-com-spring)
- [Testes](#testes)
- [Como funciona](#como-funciona)

## Instalação

```xml
<dependency>
    <groupId>solutions.held</groupId>
    <artifactId>pncp-java-sdk-spring-boot-starter</artifactId>
    <version>0.2.0</version>
</dependency>
```

O starter traz o core (`pncp-java-sdk`) **transitivamente** — você declara só esta dependência.

## Começo rápido

```java
@Service
public class ContratacoesService {

    private final PncpConsultaClient pncp;

    public ContratacoesService(PncpConsultaClient pncp) { // injetado e configurado pelo starter
        this.pncp = pncp;
    }

    public Optional<Contratacao> porId(String cnpj, int ano, int sequencial) {
        return pncp.contratacoes().consultarPorId(cnpj, ano, sequencial);
    }
}
```

## Configuração

Todas as propriedades são **opcionais**: o que você não definir mantém o padrão da SDK. O prefixo é
`pncp` e vale o _relaxed binding_ do Spring (kebab-case, em `.properties` ou `.yml`).

| Propriedade (`pncp.*`) | Tipo | Padrão | Descrição |
|---|---|---|---|
| `url-base` | String | endpoint oficial | Endereço base da API de consulta. |
| `timeout-conexao` | Duration | `10s` | Tempo limite de conexão. |
| `timeout-resposta` | Duration | `30s` | Tempo limite de resposta por requisição. |
| `max-tentativas` | int | `3` | Máximo de tentativas por requisição (a 1ª já conta). |
| `backoff-inicial` | Duration | `500ms` | Atraso inicial do backoff. |
| `backoff-maximo` | Duration | `10s` | Atraso máximo do backoff. |
| `multiplicador-backoff` | double | `2.0` | Multiplicador do backoff exponencial. |
| `vazao-por-segundo` | double | `3.0` | Vazão máxima client-side (≤ 0 desliga o limitador). |
| `user-agent` | String | `PNCP-JAVA-SDK/<versão>` | Identificação no cabeçalho `User-Agent`. |

`application.yml`:

```yaml
pncp:
  max-tentativas: 5
  timeout-resposta: 45s
  vazao-por-segundo: 2.0
```

Como é `@ConfigurationProperties`, você ganha **autocompletar e documentação** dessas chaves na IDE
(via `spring-boot-configuration-processor`), e pode sobrepor por _profile_, variável de ambiente
(`PNCP_MAX_TENTATIVAS`) ou argumento de linha de comando — como qualquer config do Spring Boot.

## Sobrescrevendo o cliente

A auto-configuração usa `@ConditionalOnMissingBean`: se você declarar o seu próprio
`PncpConsultaClient` (ou `PncpClientConfig`), o starter **recua** e respeita o seu.

```java
@Bean
PncpConsultaClient pncpConsultaClient() {
    return PncpConsultaClient.comConfig(PncpClientConfig.builder().maxTentativas(1).build());
}
```

## Ajuste programático

Para o que não cabe em propriedades (um `HttpClient` próprio, um `ObjectMapper` custom), declare um
`PncpClientConfigCustomizer`. Ele roda **depois** das propriedades `pncp.*`, então tem a palavra final:

```java
@Bean
PncpClientConfigCustomizer pncpCustomizer() {
    return builder -> builder
            .httpClient(meuHttpClient)     // proxy, autenticação, executor, cliente instrumentado...
            .objectMapper(meuObjectMapper);
}
```

> O HTTP é feito pelo `HttpClient` do JDK — é o ponto natural de instrumentação (tracing via agente do
> OpenTelemetry, proxy corporativo, executor dedicado), fornecido aqui via `builder.httpClient(...)`.

## Cache com Spring

O Spring Cache (`@EnableCaching` + `@Cacheable`) combina bem com a SDK, porque **uma contratação já
publicada é imutável** — consultá-la por `(cnpj, ano, sequencial)` sempre devolve o mesmo resultado.
Cacheie essas consultas e elimine chamadas repetidas à API.

```java
@Service
public class ContratacaoCacheada {

    private final PncpConsultaClient pncp;

    public ContratacaoCacheada(PncpConsultaClient pncp) {
        this.pncp = pncp;
    }

    // A chave DEVE conter todos os parâmetros que variam.
    @Cacheable(value = "pncp-contratacao", key = "#cnpj + ':' + #ano + ':' + #seq")
    public Optional<Contratacao> porId(String cnpj, int ano, int seq) {
        return pncp.contratacoes().consultarPorId(cnpj, ano, seq);
    }
}
```

O que cachear — e por quanto tempo:

| Dado | Muda? | Estratégia de TTL |
|---|---|---|
| Contratação/ata/contrato por id, já publicados | Não (imutável) | Longo (horas, dias) |
| Buscas por data de publicação em períodos passados | Não | Longo |
| Buscas por atualização ou incluindo o dia corrente | Sim | Curto ou **sem cache** |

> Nunca cacheie com chave que ignore um parâmetro da consulta.

## Testes

Substitua o `PncpConsultaClient` auto-configurado por um dublê nos seus testes:

```java
@SpringBootTest
class MinhaAppTest {

    @MockitoBean // org.springframework.test.context.bean.override.mockito.MockitoBean
    PncpConsultaClient pncp; // substitui o bean auto-configurado
}
```

Para testar a própria auto-configuração, use `ApplicationContextRunner` (ver
`PncpAutoConfiguracaoTest` neste módulo).

## Como funciona

- `PncpProperties` (record `@ConfigurationProperties("pncp")`) liga as chaves `pncp.*`.
- `PncpAutoConfiguration` (`@AutoConfiguration`) monta um `PncpClientConfig` a partir das propriedades
  preenchidas, aplica os `PncpClientConfigCustomizer` e expõe `PncpClientConfig` e `PncpConsultaClient` —
  ambos com `@ConditionalOnMissingBean`.
- Registrada em `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
  (mecanismo do Spring Boot 3).
