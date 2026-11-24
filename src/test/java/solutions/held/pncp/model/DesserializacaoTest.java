package solutions.held.pncp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import solutions.held.pncp.PncpClientConfig;
import solutions.held.pncp.model.enums.TipoPessoa;
import solutions.held.pncp.pagina.Pagina;

/**
 * Testes de desserialização dos modelos com o {@link ObjectMapper} padrão da SDK:
 * datas, valores monetários, enums com tolerância a valores desconhecidos,
 * envelope de paginação e tolerância a campos novos.
 *
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
class DesserializacaoTest {

    private final ObjectMapper mapper = PncpClientConfig.mapperPadrao();

    /**
     * Datas e horas, valores monetários e objetos aninhados devem ser mapeados.
     *
     * @throws Exception se a desserialização falhar
     */
    @Test
    void contratacaoResumo_mapeiaDatasEValores() throws Exception {
        String json =
                """
                {
                  "numeroControlePNCP": "00394460000141-1-000001/2024",
                  "anoCompra": 2024,
                  "valorTotalEstimado": 1234.56,
                  "dataPublicacaoPncp": "2024-01-02T08:00:00",
                  "amparoLegal": { "codigo": 1, "nome": "Lei 14.133/2021", "descricao": "d" },
                  "modalidadeId": 6
                }
                """;
        ContratacaoResumo resumo = mapper.readValue(json, ContratacaoResumo.class);
        assertEquals(2024, resumo.anoCompra());
        assertEquals(new BigDecimal("1234.56"), resumo.valorTotalEstimado());
        assertEquals(LocalDateTime.of(2024, 1, 2, 8, 0, 0), resumo.dataPublicacaoPncp());
        assertEquals(1L, resumo.amparoLegal().codigo());
        assertEquals(6L, resumo.modalidadeId());
    }

    /**
     * Um valor de enum conhecido deve ser mapeado para a constante correspondente.
     *
     * @throws Exception se a desserialização falhar
     */
    @Test
    void contrato_tipoPessoaConhecido_mapeiaEnum() throws Exception {
        String json =
                """
                { "tipoPessoa": "PJ", "valorGlobal": 1000.00, "dataVigenciaInicio": "2024-03-01" }
                """;
        Contrato contrato = mapper.readValue(json, Contrato.class);
        assertEquals(TipoPessoa.PJ, contrato.tipoPessoa());
        assertEquals(new BigDecimal("1000.00"), contrato.valorGlobal());
        assertEquals(LocalDate.of(2024, 3, 1), contrato.dataVigenciaInicio());
    }

    /**
     * Um valor de enum desconhecido deve resultar em nulo, não em falha.
     *
     * @throws Exception se a desserialização falhar
     */
    @Test
    void contrato_tipoPessoaDesconhecido_viraNulo() throws Exception {
        Contrato contrato = mapper.readValue("{ \"tipoPessoa\": \"ZZ\" }", Contrato.class);
        assertNull(contrato.tipoPessoa());
    }

    /**
     * Campos desconhecidos na resposta não devem causar falha de desserialização.
     *
     * @throws Exception se a desserialização falhar
     */
    @Test
    void contrato_campoDesconhecido_ignorado() throws Exception {
        Contrato contrato = mapper.readValue("{ \"campoNovoDaApi\": \"x\", \"anoContrato\": 2024 }", Contrato.class);
        assertEquals(2024, contrato.anoContrato());
    }

    /**
     * O envelope de paginação genérico deve ser mapeado com seus metadados.
     *
     * @throws Exception se a desserialização falhar
     */
    @Test
    void pagina_envelopeGenerico_mapeiaMetadados() throws Exception {
        String json =
                """
                {
                  "data": [ { "numeroControlePNCP": "A" }, { "numeroControlePNCP": "B" } ],
                  "totalRegistros": 2,
                  "totalPaginas": 1,
                  "numeroPagina": 1,
                  "paginasRestantes": 0,
                  "empty": false
                }
                """;
        JavaType tipo = mapper.getTypeFactory().constructParametricType(Pagina.class, ContratacaoResumo.class);
        Pagina<ContratacaoResumo> pagina = mapper.readValue(json, tipo);
        assertEquals(2, pagina.itens().size());
        assertEquals(2L, pagina.totalRegistros());
        assertEquals("A", pagina.itens().get(0).numeroControlePNCP());
    }

    /**
     * O corpo de erro de validação deve ser mapeado em {@link RespostaErroValidacao}.
     *
     * @throws Exception se a desserialização falhar
     */
    @Test
    void respostaErro_mapeiaCampos() throws Exception {
        String json =
                """
                { "message": "erro", "path": "/v1/x", "timestamp": "2024-01-01", "status": "400", "error": "Bad Request" }
                """;
        RespostaErroValidacao erro = mapper.readValue(json, RespostaErroValidacao.class);
        assertEquals("erro", erro.message());
        assertEquals("400", erro.status());
    }
}
