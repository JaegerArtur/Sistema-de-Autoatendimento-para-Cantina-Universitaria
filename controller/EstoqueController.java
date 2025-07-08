
/**
 * Controller responsável por operações de estoque, como carregamento,
 * consulta e atualização de quantidades de produtos.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package controller;

import java.util.Map;
import service.EstoqueService;

public class EstoqueController {

    public static void carregarEstoque() {
        EstoqueService.carregarEstoque();
    }
    
    /**
     * Retorna o mapa de produtos e quantidades do estoque (cópia defensiva).
     */
    public static Map<String, Integer> getMapaProdutos() {
        return EstoqueService.getMapaProdutos();
    }

    public static void atualizarQuantidade(String idProduto, int quantidade) {
        EstoqueService.atualizarQuantidade(idProduto, quantidade);
    }
}
