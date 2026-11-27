# Exemplos

Exemplos executáveis da SDK. Eles ficam **fora** de `src/`, portanto não entram no artefato
publicado nem são compilados pelo build principal.

## Pré-requisitos

- Java 17+
- A SDK e suas dependências disponíveis (instale localmente com `mvn install` na raiz do projeto).

## Como executar

A forma mais simples é montar o classpath com o Maven e usar o executor de arquivo único do Java
(`java Arquivo.java`), que compila e executa em um passo.

No diretório raiz do projeto:

```bash
# 1. Instale a SDK no repositório local (gera os jars e resolve as dependências)
mvn -q install

# 2. Monte o classpath do core (SDK + Jackson)
mvn -q -pl pncp-java-sdk dependency:build-classpath -Dmdep.outputFile=cp.txt
CP="pncp-java-sdk/target/classes:$(cat pncp-java-sdk/cp.txt)"   # Windows (PowerShell): $CP = "pncp-java-sdk/target/classes;$(Get-Content pncp-java-sdk/cp.txt)"

# 3. Execute o exemplo
java -cp "$CP" examples/ExemploContratacoes.java
```

> Dica: no Windows, o separador de classpath é `;` (ponto e vírgula), não `:`.

## Exemplos disponíveis

- `ExemploContratacoes.java` — consulta contratações por publicação (com fluxo e `limit`),
  busca uma contratação específica e trata erros.
