package service;

import model.Estoque;

import util.JsonManager;
import java.util.*;
import com.google.gson.reflect.TypeToken;

/**
 * Classe de serviço responsável por operações relacionadas ao estoque de produtos.
 * Toda a lógica de manipulação de quantidades e persistência do estoque fica aqui.
 *
 * @author Grupo Artur, João, Miguel
 * @version 1.0
 */
public class EstoqueService {
    private static final String CAMINHO_ESTOQUE = "data/estoque.json";

    private static Estoque estoque;

    /**
     * Carrega o estoque do arquivo JSON.
     */
    public static void carregarEstoque() {
        Map<String, Integer> mapa = JsonManager.carregarEstoque(CAMINHO_ESTOQUE);
        Estoque novoEstoque = new Estoque();
        if (mapa != null) {
            for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
                novoEstoque.atualizarQuantidade(entry.getKey(), entry.getValue());
            }
        }
        estoque = novoEstoque;
    }

    /**
     * Salva o estoque no arquivo JSON.
     */
    public static void salvarEstoque(Estoque estoque) {
        JsonManager.salvarEstoque(estoque.getMapaProdutos(), CAMINHO_ESTOQUE);
    }


    /**
     * Atualiza a quantidade de um produto no estoque.
     */
    public static void atualizarQuantidade(String idProduto, int novaQuantidade) {
        estoque.atualizarQuantidade(idProduto, novaQuantidade);
        salvarEstoque(estoque);
    }

    /**
     * Verifica se há quantidade suficiente de um produto no estoque.
     */
    public static boolean verificarEstoqueSuficiente(String idProduto, int quantidade) {
        return estoque.getQuantidade(idProduto) >= quantidade;
    }

    /**
     * Retorna a quantidade de um produto no estoque.
     */
    public static int getQuantidade(String idProduto) {
        return estoque.getQuantidade(idProduto);
    }

    /**
     * Retorna todos os IDs de produtos presentes no estoque.
     */
    public static Set<String> getTodosProdutos() {
        return estoque.getTodosProdutos();
    }

    /**
     * Retorna o mapa completo de produtos e quantidades.
     */
    public static Map<String, Integer> getMapaProdutos() {
        return estoque.getMapaProdutos();
    }

    /**
     * Retorna produtos com estoque abaixo do mínimo informado.
     */
    public static List<String> consultarEstoqueBaixo(int minimo) {
        List<String> idsBaixo = new ArrayList<>();
        for (String id : estoque.getMapaProdutos().keySet()) {
            if (estoque.getQuantidade(id) < minimo) {
                idsBaixo.add(id);
            }
        }
        return idsBaixo;
    }

    /**
     * Repor quantidade de um produto no estoque.
     */
    public static void reporEstoque(String idProduto, int quantidade) {
        int atual = estoque.getQuantidade(idProduto);
        estoque.atualizarQuantidade(idProduto, atual + quantidade);
        salvarEstoque(estoque);
    }

    /**
     * Retorna o estoque atual de todos os produtos.
     */
    public static Map<String, Integer> listarEstoqueAtual() {
        return new HashMap<>(estoque.getMapaProdutos());
    }
}
