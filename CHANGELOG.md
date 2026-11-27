# Changelog

Todos os as mudanças relevantes deste projeto serão documentadas neste arquivo.

O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e o projeto adota
o [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [0.2.0] — Não lançado

### Adicionado
- Novo módulo `pncp-java-sdk-spring-boot-starter`: auto-configuração Spring Boot 3.x que expõe um
  `PncpConsultaClient` pronto para injeção, configurável por `pncp.*` no `application.yml`.
- `PncpClientConfig.Builder#httpClient(HttpClient)` para fornecer um `java.net.http.HttpClient`
  próprio (proxy, SSL/TLS, autenticação, versão de protocolo ou executor dedicado). Quando ausente,
  a SDK constrói o cliente padrão a partir da configuração, como antes.

### Alterado
- O projeto passou a ser um reator Maven multi-módulo: o novo `pncp-dados-parent` agrega o core
  `pncp-java-sdk` (coordenadas **inalteradas**) e o `pncp-java-sdk-spring-boot-starter`.
- O cabeçalho `User-Agent` passou a reportar a versão real da SDK (derivada do `project.version`) em
  vez de um valor fixo.
- As listas expostas pelos modelos (`Pagina#itens`, `Contratacao`/`ContratacaoResumo#fontesOrcamentarias`,
  `NotaFiscalEletronica#itens`/`#eventos` e `PlanoContratacao#itens`) agora são imutáveis e nunca nulas —
  campos ausentes na resposta viram listas vazias.
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
