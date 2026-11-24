package solutions.held.pncp.model;

/**
 * Órgão ou entidade pública vinculado a uma contratação, contrato ou ata.
 *
 * @param cnpj CNPJ do órgão ou entidade
 * @param razaoSocial razão social do órgão ou entidade
 * @param poderId identificador do poder (por exemplo, Executivo, Legislativo ou Judiciário)
 * @param esferaId identificador da esfera (por exemplo, Federal, Estadual ou Municipal)
 * @author Guilherme A. Held
 * @since 23/11/2026
 */
public record OrgaoEntidade(String cnpj, String razaoSocial, String poderId, String esferaId) {}
