# Contribuindo

Obrigado pelo interesse em contribuir com o PNCP Consulta SDK! Este documento resume como propor
mudanças.

## Como contribuir

1. Abra uma _issue_ descrevendo o problema ou a proposta antes de um _pull request_ maior.
2. Faça um _fork_ e crie um _branch_ a partir de `main`.
3. Implemente a mudança com testes.
4. Garanta que o build está verde (veja abaixo).
5. Abra um _pull request_ descrevendo o quê e o porquê.

## Build e testes

Requer **Java 17+**. O projeto usa Maven (há um `pom.xml` na raiz).

```bash
mvn verify
```

Isso compila, roda os testes (JUnit 5) e verifica a formatação com o Spotless.

Para corrigir a formatação automaticamente:

```bash
mvn spotless:apply
```

## Convenções de código

- **Formatação:** Spotless com `palantir-java-format` — rode `mvn spotless:apply` antes de commitar.
- **Javadoc:** em português, em bloco, em toda a API pública.
- **Imutabilidade:** modelos são records imutáveis; o cliente é imutável e seguro para concorrência.
- **Dependências:** o artefato publicado depende apenas do Jackson. Não adicione dependências de
  tempo de execução sem necessidade clara; dependências de teste ficam em `scope=test`.
- **Testes:** cubra a lógica com JUnit 5. Para cenários HTTP, use o `HttpServer` do JDK em vez de
  bibliotecas de mock.

## Escopo

Este projeto cobre a **API de Consulta** do PNCP. A API de Integração (autenticada) está fora do
escopo desta biblioteca.
