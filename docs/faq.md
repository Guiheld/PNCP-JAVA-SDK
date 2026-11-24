# Perguntas frequentes

## Preciso de alguma credencial?

Não. A API de Consulta do PNCP é pública e somente leitura.

## A SDK cobre a publicação de dados no PNCP?

Não. Esta biblioteca cobre apenas a **API de Consulta**. A API de Integração (autenticada, para
órgãos publicarem contratações, atas e contratos) está fora do escopo.

## Qual versão do Java é necessária?

Java 17 ou superior.

## Quais dependências a biblioteca traz?

Apenas o **Jackson** em tempo de execução. O cliente HTTP e os logs usam o próprio JDK
(`java.net.http.HttpClient` e `System.Logger`), ou seja, é super levinho mesmo.

Utilizável em qualquer projeto Java 17+ sem precisar de frameworks externos, pode ser spring ou quarkus ... mesmo que
quarkus seja melhor rsrs.

## Como percorro todos os resultados sem me preocupar com páginas?

Use os métodos `stream...`, que percorrem todas as páginas de forma lazy. Veja
[Paginação](paginacao.md).

## Recebi um `PncpRateLimitException`. E agora?

Você atingiu o limite de requisições do portal. Reduza a `vazaoPorSegundo`, aumente
`maxTentativas` e consulte [Limites e resiliência](limites.md).

## Um filtro opcional parece não ter efeito. É bug da SDK?

Provavelmente não. A API do PNCP é conhecida por ignorar silenciosamente alguns filtros. A SDK
envia os parâmetros corretamente; confira o comportamento diretamente na API quando necessário.

## Posso usar um `ObjectMapper` personalizado?

Sim. Passe-o em `PncpClientConfig.builder().objectMapper(...)`. O padrão já está configurado para
as respostas do PNCP.
