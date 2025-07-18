
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

    /**
     * Atualiza a data de validade de um produto.
     * @param idProduto ID do produto a ser atualizado
     * @param novaDataValidade Nova data de validade no formato "yyyy-MM-dd"
     * @throws ProdutoNaoExisteException se o produto não existir
     */
    public static void atualizarDataValidade(String idProduto, String novaDataValidade) throws ProdutoNaoExisteException {
        ProdutoService.atualizarDataValidade(idProduto, novaDataValidade);
    }

    /**
     * Atualiza o preço de um produto.
     * @param idProduto ID do produto a ser atualizado
     * @param novoPreco Novo preço do produto
     * @throws ProdutoNaoExisteException se o produto não existir
     */
    public static void atualizarPreco(String idProduto, double novoPreco) throws ProdutoNaoExisteException {
        ProdutoService.atualizarPreco(idProduto, novoPreco);
    }
}
