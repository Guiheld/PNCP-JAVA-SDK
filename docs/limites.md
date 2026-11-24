# Limites e resiliência

## Limites de requisição

O PNCP **não documenta formalmente** seus limites de requisições. Há relatos da comunidade de que,
sob rajada, o portal passa a responder `429 Too Many Requests` — muitas vezes com corpo em
**HTML**, não em JSON.

A SDK já trata isso:

- **Limitação de vazão** client-side, ligada por padrão em **3 requisições por segundo**. Ajuste
  com `vazaoPorSegundo(...)` ou desligue com `0`.
- **Novas tentativas** automáticas com recuo exponencial e variação aleatória para `429`, `5xx` e
  falhas transitórias de rede, respeitando o cabeçalho `Retry-After` quando presente.
- Respostas `429` em HTML viram `PncpRateLimitException` sem qualquer tentativa de interpretação
  como JSON.

```java
PncpClientConfig config = PncpClientConfig.builder()
        .vazaoPorSegundo(1.5)                       // mais conservador
        .maxTentativas(5)                           // tenta mais vezes
        .backoffInicial(Duration.ofMillis(800))
        .backoffMaximo(Duration.ofSeconds(20))
        .multiplicadorBackoff(2.0)
        .build();
```

!!! warning "Seja um bom cidadão"
    A API é um bem público. Prefira janelas de data menores, reutilize o mesmo cliente e evite
    paralelismo agressivo - se for fazer scrapping, use cache local e respeite o `Retry-After`. 
A SDK já ajuda nisso, mas você também pode implementar **circuit breaker** e **rate limiter** no seu código.

## Datas

Os parâmetros de data usam o formato `yyyyMMdd`; você passa `LocalDate` e a SDK faz a conversão.
Nos modelos, campos de data e hora chegam **sem fuso** (horário de Brasília) e são mapeados para
`LocalDateTime`; campos só de data, para `LocalDate`.

## Filtros ignorados em silêncio

Há relatos de que a API ignora, sem aviso, alguns parâmetros de filtro em certas consultas. A SDK
envia apenas os parâmetros previstos na especificação, mas **confira os resultados** ao usar
filtros opcionais — o conjunto retornado pode ser mais amplo do que o filtro sugere.

## Códigos de domínio desconhecidos

Valores de enum ainda não mapeados pela biblioteca **não quebram** a desserialização: o campo
tipado fica nulo, mas o nome textual correspondente continua disponível no modelo (por exemplo,
`modalidadeNome` ao lado de `modalidadeId`).
