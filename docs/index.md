# PNCP Consulta SDK

SDK Java para a **API de Consulta do PNCP** (Portal Nacional de Contratações Públicas): uma
biblioteca idiomática, enxuta e sem dependências de rede externas para consultar contratações,
contratos, atas de registro de preço, planos de contratações anuais (PCA) e instrumentos de
cobrança.

## Destaques

- Modelos tipados e imutáveis (records Java) para todos os recursos.
- Paginação automática como `Stream`, com busca lazy de páginas.
- Enums tipados para modalidade, modo de disputa, situação e tipo de pessoa.
- Exceções claras por categoria de erro.
- Resiliência embutida: limitação de vazão, novas tentativas e tratamento de `429` em HTML.
- Uma única dependência em tempo de execução: o Jackson.

## Requisitos

- Java 17 ou superior.
- Nenhuma credencial — a API de Consulta do PNCP é pública.

## Primeiros passos

Veja [Começando](comecando.md) para instalar e fazer a primeira consulta, ou vá direto para as
[Consultas disponíveis](consultas.md).

!!! note "Escopo"
    Esta SDK cobre a **API de Consulta** do PNCP. A API de Integração (autenticada, para publicar
    dados) está fora do escopo desta biblioteca ... lib para isso está em desenvolvimento e será publicada em breve :^)
