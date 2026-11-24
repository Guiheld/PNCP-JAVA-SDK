# PNCP-JAVA-SDK

SDK Java para a **API de Consulta do PNCP** (Portal Nacional de Contratações Públicas) — uma
biblioteca idiomática, enxuta e sem dependências de rede externas para consultar contratações,
contratos, atas de registro de preço, planos de contratações anuais (PCA) e instrumentos de
cobrança publicados no PNCP.

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](#requisitos)
[![License](https://img.shields.io/badge/license-Apache--2.0-green)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/solutions.held/pncp-java-sdk)](https://central.sonatype.com/artifact/solutions.held/pncp-java-sdk)

> API oficial: <https://pncp.gov.br/api/consulta/swagger-ui/index.html>

---

## Sumário

- [Por que esta SDK](#por-que-esta-sdk)
- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Começo rápido](#começo-rápido)
- [Consultas disponíveis](#consultas-disponíveis)
- [Paginação automática](#paginação-automática)
- [Tratamento de erros](#tratamento-de-erros)
- [Limites de requisição e resiliência](#limites-de-requisição-e-resiliência)
- [Configuração](#configuração)
- [Comportamentos conhecidos da API](#comportamentos-conhecidos-da-api)
- [Documentação](#documentação)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Por que esta SDK

A API de Consulta do PNCP é pública e somente leitura, mas consumi-la "na mão" obriga cada
projeto a repetir a montagem de URLs, a paginação, a formatação de datas `yyyyMMdd`, o mapeamento
de dezenas de DTOs e o tratamento de erros e limites não documentados do portal. Esta SDK resolve
tudo isso com:

- **Modelos tipados e imutáveis** (records Java) para todos os recursos.
- **Paginação automática** como `Stream`, percorrendo todas as páginas de forma lazy.
- **Enums tipados** para modalidade de contratação, modo de disputa, situação e tipo de pessoa.
- **Exceções claras** por categoria de erro (cliente, servidor, limite de requisições).
- **Resiliência** embutida: limitação de vazão, novas tentativas com recuo exponencial e
  tratamento correto de respostas 429 em HTML.
- **Dependência única** em tempo de execução: apenas o Jackson. HTTP e logs usam o próprio JDK.

## Requisitos

- **Java 17** ou superior.
- Nenhuma credencial: a API de Consulta do PNCP é pública.

## Instalação

> Publicado no **Maven Central** e no **GitHub Packages** — use as coordenadas abaixo.

**Maven**

```xml
<dependency>
    <groupId>solutions.held</groupId>
    <artifactId>pncp-java-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Gradle (Kotlin DSL)**

```kotlin
implementation("solutions.held:pncp-java-sdk:0.1.0")
```

## Começo rápido

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
        .limit(100)
        .forEach(contratacao -> System.out.println(contratacao.objetoCompra()));
```

## Consultas disponíveis

Cada grupo é acessado a partir do `PncpConsultaClient`. As consultas paginadas oferecem um método
`consultar...(requisicao, pagina)` (uma página) e um método `stream...(requisicao)` (todas as
páginas).

| Grupo | Acesso | Consultas |
| --- | --- | --- |
| Contratações | `cliente.contratacoes()` | por publicação, por atualização, com proposta aberta, por id (órgão/ano/sequencial) |
| Contratos/Empenhos | `cliente.contratos()` | por publicação, por atualização |
| Atas de registro de preço | `cliente.atas()` | por vigência, por atualização |
| Planos de contratações (PCA) | `cliente.pca()` | por ano, por usuário, por atualização |
| Instrumentos de cobrança | `cliente.instrumentosCobranca()` | por data de inclusão |

Exemplo de consulta de um único recurso:

```java
cliente.contratacoes()
        .consultarPorId("00394460000141", 2024, 1)
        .ifPresent(contratacao -> System.out.println(contratacao.valorTotalHomologado()));
```

## Paginação automática

```java
// Uma página específica, com metadados:
Pagina<Contrato> pagina = cliente.contratos().consultarPorPublicacao(consulta, 1);
System.out.println(pagina.totalRegistros() + " registros em " + pagina.totalPaginas() + " páginas");

// Ou percorra tudo de forma lazy — a próxima página só é buscada quando necessária:
cliente.contratos().streamPorPublicacao(consulta)
        .forEach(contrato -> processar(contrato));
```

Quando a API responde `204 No Content` (sem resultados), a SDK devolve uma página vazia — nunca
um erro.

## Tratamento de erros

Todas as falhas são exceções não verificadas que derivam de `PncpException`:

| Exceção | Quando ocorre |
| --- | --- |
| `PncpClientException` | erro de cliente HTTP 4xx (parâmetros inválidos, recurso inexistente) |
| `PncpRateLimitException` | HTTP 429 — limite de requisições excedido |
| `PncpServerException` | erro de servidor HTTP 5xx no portal |
| `PncpSerializationException` | falha ao interpretar a resposta |
| `PncpException` | falha de comunicação ou erro genérico |

```java
try {
    cliente.atas().consultarPorVigencia(consulta, 1);
} catch (PncpRateLimitException e) {
    e.tempoEsperaSugerido().ifPresent(espera -> aguardar(espera));
} catch (PncpClientException e) {
    e.erroValidacao().ifPresent(erro -> System.err.println(erro.message()));
}
```

## Limites de requisição e resiliência

O PNCP **não documenta formalmente** seus limites de requisições e, sob rajada, costuma responder
`429` com corpo em **HTML** (não JSON). A SDK já lida com isso:

- **Limitação de vazão** client-side ligada por padrão (3 req/s), ajustável ou desligável.
- **Novas tentativas** automáticas com recuo exponencial e variação aleatória para `429`, `5xx` e
  falhas transitórias de rede, respeitando o cabeçalho `Retry-After` quando presente.
- Respostas `429` em HTML são tratadas como `PncpRateLimitException` sem tentar interpretá-las
  como JSON.

## Configuração

```java
PncpClientConfig config = PncpClientConfig.builder()
        .vazaoPorSegundo(2.0)                 // limite client-side (0 desliga)
        .maxTentativas(4)                     // tentativas por requisição
        .timeoutResposta(Duration.ofSeconds(20))
        .build();

PncpConsultaClient cliente = PncpConsultaClient.comConfig(config);
```

Também é possível apontar para outro endereço base (por exemplo, um ambiente local) com
`urlBase(...)` e substituir o `ObjectMapper` do Jackson com `objectMapper(...)`.

## Comportamentos conhecidos da API

- **Filtros ignorados em silêncio:** há relatos de que a API ignora, sem aviso, alguns parâmetros
  de filtro. A SDK envia apenas os parâmetros previstos na especificação; ainda assim, confira os
  resultados quando usar filtros opcionais.
- **Datas:** os parâmetros de data usam o formato `yyyyMMdd`; a SDK recebe `LocalDate` e cuida da
  conversão. Campos de data e hora nas respostas chegam sem fuso (horário de Brasília).
- **Códigos de domínio:** valores ainda não mapeados pelos enums não quebram a desserialização —
  o nome textual correspondente continua disponível no próprio modelo.

## Documentação

- **Wiki do projeto:** <https://github.com/Guiheld/PNCP-JAVA-SDK/wiki> — começando, consultas por
  domínio, paginação, tratamento de erros, limites e FAQ.
- **Javadoc** completo da API pública (em português).
- Exemplos executáveis em [`examples/`](examples/).

## Contribuição

Contribuições são bem-vindas. Veja [CONTRIBUTING.md](CONTRIBUTING.md) e o
[Código de Conduta](CODE_OF_CONDUCT.md).

## Licença

Distribuído sob a licença [Apache 2.0](LICENSE).

---

> Este projeto não é afiliado ao PNCP, ao Serpro nem ao Governo Federal; é uma biblioteca
> independente que consome a API pública de consulta.
