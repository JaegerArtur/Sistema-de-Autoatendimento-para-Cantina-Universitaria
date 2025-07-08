
/**
 * Controller responsável por operações relacionadas a produtos,
 * como listagem, busca e carregamento de produtos do sistema.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package controller;

import model.Produto;
import service.ProdutoService;
import exception.ProdutoNaoExisteException;
import java.util.List;

public class ProdutoController {

    public static List<Produto> listarProdutos() {
        return ProdutoService.getProdutos();
    }

    /**
     * Carrega os produtos do arquivo JSON.
     * Deve ser chamado antes de qualquer operação que dependa dos produtos.
     */
    public static void carregarProdutos() {
        ProdutoService.carregarProdutos();
    }

    public static Produto buscarProdutoPorId(String id) throws ProdutoNaoExisteException {
        return ProdutoService.getProdutoId(id);
    }
}
