package controller;

import model.Produto;
import service.ProdutoService;
import exception.ProdutoNaoExisteException;
import java.util.List;

public class ProdutoController {

    public static List<Produto> listarProdutos() {
        return ProdutoService.getProdutos();
    }

    public static Produto buscarProdutoPorId(String id) throws ProdutoNaoExisteException {
        return ProdutoService.getProdutoId(id);
    }
}
