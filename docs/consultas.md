# Consultas

Cada grupo de consultas é acessado a partir do `PncpConsultaClient`. As consultas paginadas têm um
método `consultar...(requisicao, pagina)` (uma página) e um método `stream...(requisicao)` (todas as páginas).

## Contratações — `cliente.contratacoes()`

```java
ConsultaContratacaoRequest req = ConsultaContratacaoRequest
        .builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31), ModalidadeContratacao.PREGAO_ELETRONICO)
        .uf("SP")
        .modoDisputa(ModoDisputa.ABERTO)
        .build();

// Por data de publicação
cliente.contratacoes().streamPorPublicacao(req).forEach(...);

// Por data de atualização global
cliente.contratacoes().streamPorAtualizacao(req).forEach(...);

// Com recebimento de propostas em aberto
ConsultaContratacaoPropostaRequest abertas = ConsultaContratacaoPropostaRequest
        .builder(LocalDate.of(2024, 12, 31))
        .uf("SP")
        .build();
cliente.contratacoes().streamComPropostaAberta(abertas).forEach(...);

// Uma contratação específica (órgão, ano, sequencial)
cliente.contratacoes().consultarPorId("00394460000141", 2024, 1)
        .ifPresent(c -> System.out.println(c.valorTotalHomologado()));
```

## Contratos e empenhos — `cliente.contratos()`

```java
ConsultaContratoRequest req = ConsultaContratoRequest
        .builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31))
        .cnpjOrgao("00394460000141")
        .build();

cliente.contratos().streamPorPublicacao(req).forEach(...);
cliente.contratos().streamPorAtualizacao(req).forEach(...);
```

## Atas de registro de preço — `cliente.atas()`

```java
ConsultaAtaRequest req = ConsultaAtaRequest
        .builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))
        .build();

cliente.atas().streamPorVigencia(req).forEach(...);
cliente.atas().streamPorAtualizacao(req).forEach(...);
```

## Planos de contratações anuais (PCA) — `cliente.pca()`

```java
// Por ano e classificação superior
cliente.pca().streamPorAno(ConsultaPcaRequest.builder(2024, "979").build()).forEach(...);

// Por usuário
cliente.pca().streamPorUsuario(ConsultaPcaUsuarioRequest.builder(2024, 12345L).build()).forEach(...);

// Por data de atualização global
ConsultaPcaAtualizacaoRequest req = ConsultaPcaAtualizacaoRequest
        .builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31))
        .build();
cliente.pca().streamPorAtualizacao(req).forEach(...);
```

## Instrumentos de cobrança — `cliente.instrumentosCobranca()`

```java
ConsultaInstrumentoCobrancaRequest req = ConsultaInstrumentoCobrancaRequest
        .builder(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31))
        .cnpjOrgao("00394460000141")
        .build();

cliente.instrumentosCobranca().streamPorInclusao(req).forEach(...);
```
