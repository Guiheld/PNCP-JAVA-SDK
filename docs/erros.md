# Tratamento de erros

Todas as falhas são exceções **não verificadas** que derivam de `PncpException`.

| Exceção | Quando ocorre |
| --- | --- |
| `PncpClientException` | erro de cliente HTTP 4xx (parâmetros inválidos, recurso inexistente) |
| `PncpRateLimitException` | HTTP 429 — limite de requisições excedido |
| `PncpServerException` | erro de servidor HTTP 5xx no portal |
| `PncpSerializationException` | falha ao interpretar a resposta |
| `PncpException` | falha de comunicação ou erro genérico |

`PncpClientException`, `PncpServerException` e `PncpRateLimitException` derivam de
`PncpApiException` e expõem o código de status, o corpo bruto da resposta e, quando o corpo era um
JSON de validação, o `RespostaErroValidacao`.

```java
try {
    cliente.atas().consultarPorVigencia(req, 1);
} catch (PncpRateLimitException e) {
    // 429: respeite o tempo sugerido, se houver
    e.tempoEsperaSugerido().ifPresent(espera -> dormir(espera));
} catch (PncpClientException e) {
    // 4xx: muitas vezes traz um erro de validação estruturado
    System.err.println("HTTP " + e.codigoStatus());
    e.erroValidacao().ifPresent(erro -> System.err.println(erro.message()));
} catch (PncpServerException e) {
    // 5xx: normalmente transitório (a SDK já tenta novamente antes de desistir)
    System.err.println("Falha no servidor do PNCP: " + e.codigoStatus());
}
```

!!! tip "Captura ampla"
    Para tratar qualquer falha da SDK de uma vez, capture `PncpException` — a raiz da hierarquia.
