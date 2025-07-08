package service;

import model.Produto;
import exception.ProdutoNaoExisteException;
import util.JsonManager;
import model.enums.CategoriaProduto;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de serviço responsável por operações relacionadas a produtos.<br>
 * Permite buscar produtos por ID.
 * <br>
 * Utiliza o {@link util.JsonManager} para persistência e leitura dos dados dos produtos.
 *
 * @author Grupo Artur, João, Miguel
 * @version 1.0
 */
public class ProdutoService {
    // Caminho do arquivo JSON onde os produtos estão armazenados
    private static final String CAMINHO_PRODUTOS = "data/produtos.json";

    // Cache estático dos produtos
    private static List<Produto> produtos;

    /**
     * Busca um produto pelo seu id.
     *
     * @param id Identificador do produto.
     * @return O produto correspondente ao ID.
     * @throws ProdutoNaoExisteException se não encontrado.
     */
    public static Produto getProdutoId(String id) throws ProdutoNaoExisteException {
        if (produtos != null) {
            for (Produto produto : produtos) {
                if (produto.getId().equals(id)) {
                    return produto;
                }
            }
        }
        throw new ProdutoNaoExisteException();
    }

    /**
     * Retorna a lista de produtos carregada em memória.
     * Garante que a lista está carregada apenas uma vez por execução.
     */
    public static List<Produto> getProdutos() {
        if (produtos == null) {
            carregarProdutos();
        }
        return produtos;
    }

    /**
     * Carrega a lista de produtos do arquivo JSON.
     *
     * @return Lista de produtos.
     */
    public static void carregarProdutos() {
        produtos = JsonManager.carregarProdutos(CAMINHO_PRODUTOS);
    }

    /**
     * Busca produtos por nome (contém, case-insensitive).
     */
    public static List<Produto> buscarPorNome(String nome) {
        return produtos.stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca produtos por categoria.
     */
    public static List<Produto> buscarPorCategoria(CategoriaProduto categoria) {
        return produtos.stream()
                .filter(p -> p.getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza a data de validade de um produto específico.
     * @param idProduto ID do produto a ser atualizado
     * @param novaDataValidade Nova data de validade no formato "yyyy-MM-dd"
     * @throws ProdutoNaoExisteException se o produto não existir
     */
    public static void atualizarDataValidade(String idProduto, String novaDataValidade) throws ProdutoNaoExisteException {
        Produto produto = getProdutoId(idProduto);
        produto.setDataValidade(novaDataValidade);
        salvarProdutos();
    }

    /**
     * Salva a lista atual de produtos no arquivo JSON.
     */
    public static void salvarProdutos() {
        if (produtos != null) {
            JsonManager.salvarProdutos(produtos, CAMINHO_PRODUTOS);
        }
    }
}
