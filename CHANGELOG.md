# Changelog

Todos os as mudanças relevantes deste projeto serão documentadas neste arquivo.

O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e o projeto adota
o [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [0.2.0] — Não lançado

### Alterado
- Dependências atualizadas: Jackson `2.23.0`, JUnit `5.14.4`, Spotless `3.12.0`,
  palantir-java-format `2.103.0` e maven-compiler-plugin `3.16.0`.
- A verificação de integração contínua passa a executar `mvn verify` (compila, roda os testes e
  valida a formatação com o Spotless) na matriz JDK 17/21/25.

## [0.1.0] — 2026-11-24

Primeira versão pública.

### Adicionado
- Cliente de consulta (`PncpConsultaClient`) cobrindo os 12 endpoints da API de Consulta do PNCP:
  contratações (por publicação, por atualização, com proposta aberta e por id), contratos e
  empenhos (por publicação e por atualização), atas de registro de preço (por vigência e por
  atualização), planos de contratações anuais (por ano, por usuário e por atualização) e
  instrumentos de cobrança (por data de inclusão).
- Modelos imutáveis (records) para todos os recursos e enums tipados para modalidade de
  contratação, modo de disputa, situação da compra e tipo de pessoa.
- Paginação automática como `Stream`, com busca lazy de páginas.
- Resiliência: limitação de vazão client-side, novas tentativas com recuo exponencial e
  tratamento de respostas `429` em HTML.
- Hierarquia de exceções tipadas (`PncpClientException`, `PncpServerException`,
  `PncpRateLimitException`, `PncpSerializationException`).
- Suíte de testes com JUnit 5 e servidor HTTP local do JDK.
