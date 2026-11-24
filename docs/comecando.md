# Começando

## Instalação

=== "Maven"

    ```xml
    <dependency>
        <groupId>solutions.held</groupId>
        <artifactId>pncp-consulta</artifactId>
        <version>0.1.0</version>
    </dependency>
    ```

=== "Gradle (Kotlin DSL)"

    ```kotlin
    implementation("solutions.held:pncp-consulta:0.1.0")
    ```

## Primeira consulta

```java
import solutions.held.pncp.PncpConsultaClient;
import solutions.held.pncp.model.enums.ModalidadeContratacao;
import solutions.held.pncp.request.ConsultaContratacaoRequest;
import java.time.LocalDate;

PncpConsultaClient cliente = PncpConsultaClient.criar();

ConsultaContratacaoRequest consulta = ConsultaContratacaoRequest
        .builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31), ModalidadeContratacao.PREGAO_ELETRONICO)
        .uf("SP")
        .build();

cliente.contratacoes()
        .streamPorPublicacao(consulta)
        .limit(50)
        .forEach(c -> System.out.println(c.objetoCompra()));
```

## Configurando o cliente

O cliente padrão já vem pronto para o portal público. Para ajustar tempos limite, novas tentativas ou a limitação de vazão, use um `PncpClientConfig`:

```java
import solutions.held.pncp.PncpClientConfig;
import java.time.Duration;

PncpClientConfig config = PncpClientConfig.builder()
        .vazaoPorSegundo(2.0)
        .maxTentativas(4)
        .timeoutResposta(Duration.ofSeconds(20))
        .build();

PncpConsultaClient cliente = PncpConsultaClient.comConfig(config);
```

O cliente é imutável e seguro para uso concorrente — crie um e reutilize-o.
