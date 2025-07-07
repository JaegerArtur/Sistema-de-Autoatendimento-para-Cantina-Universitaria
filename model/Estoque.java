package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Representa o estoque de produtos da cantina.<p>
 * Mantém um mapeamento entre o ID do produto e sua quantidade disponível.<br>
 * Permite consultar, adicionar, remover e atualizar quantidades.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public class Estoque {

    private Map<String, Integer> produtos;

    /**
     * Cria um novo estoque vazio.
     */
    public Estoque() {
        this.produtos = new HashMap<>();
    }

    /**
     * Define a quantidade exata de um produto no estoque.
     *
     * @param idProduto O ID do produto.
     * @param quantidade A nova quantidade.
     */
    public void atualizarQuantidade(String idProduto, int quantidade) {
        this.produtos.put(idProduto, quantidade);
    }

    /**
     * Obtém a quantidade disponível de um produto.
     *
     * @param idProduto O ID do produto.
     * @return A quantidade disponível, ou 0 se não estiver no estoque.
     */
    public int getQuantidade(String idProduto) {
        return this.produtos.getOrDefault(idProduto, 0);
    }

    /**
     * Retorna todos os IDs de produtos presentes no estoque.
     *
     * @return Um conjunto com todos os IDs de produtos.
     */
    public Set<String> getTodosProdutos() {
        return this.produtos.keySet();
    }

    /**
     * Retorna o mapa completo de produtos e quantidades.
     *
     * @return Um mapa do ID do produto para a quantidade.
     */
    public Map<String, Integer> getMapaProdutos() {
        return this.produtos;
    }

}
