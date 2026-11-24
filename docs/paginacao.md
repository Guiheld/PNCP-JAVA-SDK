# Paginação

A API do PNCP devolve resultados paginados em um envelope com os itens e os metadados de
paginação. A SDK expõe isso de duas formas.

## Página a página

```java
Pagina<Contrato> pagina = cliente.contratos().consultarPorPublicacao(req, 1);

pagina.itens();             // itens desta página
pagina.totalRegistros();    // total de registros da consulta
pagina.totalPaginas();      // total de páginas
pagina.numeroPagina();      // número desta página (começa em 1)
pagina.paginasRestantes();  // páginas ainda não percorridas
pagina.temProximaPagina();  // atalho: paginasRestantes > 0
```

!!! note "O campo `data` vira `itens()`"
    O campo `data` da API é exposto como `itens()` para evitar a ambiguidade de "data" em
    português. Use `itensSeguros()` quando quiser uma lista nunca nula.

## Todas as páginas, de forma lazy

O método `stream...` percorre todas as páginas automaticamente, buscando a próxima apenas quando a
anterior se esgota:

```java
long total = cliente.contratos().streamPorPublicacao(req).count();

cliente.contratos().streamPorPublicacao(req)
        .filter(c -> c.valorGlobal() != null)
        .limit(500)
        .forEach(this::processar);
```

Como o fluxo é preguiçoso, `limit(...)` evita buscar páginas desnecessárias.

## Sem resultados

Quando a API responde `204 No Content`, a SDK devolve uma **página vazia** (lista vazia,
`temProximaPagina()` falso) — nunca um erro. No fluxo, isso simplesmente encerra a iteração.
