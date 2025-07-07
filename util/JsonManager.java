package util;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import model.*;
import model.enums.Dinheiro;

/**
 * Classe utilitária responsável pela manipulação de arquivos JSON para persistência de dados.<br>
 * Pode ser usada para carregar e salvar listas de objetos como produtos, usuários, etc.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public class JsonManager {
    /**
     * Carrega uma lista de produtos a partir de um arquivo JSON.
     * 
     * @param caminho Caminho do arquivo JSON.
     * @return Lista de produtos carregados.
     */
    public static List<Produto> carregarProdutos(String caminho) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(caminho);
            List<Produto> produtos = gson.fromJson(reader, new TypeToken<List<Produto>>(){}.getType());
            reader.close();
            return produtos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Salva uma lista de produtos em um arquivo JSON.
     * 
     * @param produtos Lista de produtos a serem salvos.
     * @param caminho Caminho do arquivo onde os dados serão gravados.
     */
    public static void salvarProdutos(List<Produto> produtos, String caminho) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(caminho);
            gson.toJson(produtos, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega uma lista de vendas a partir de um arquivo JSON.
     * 
     * @param caminho Caminho do arquivo JSON.
     * @return Lista de vendas carregadas.
     */
    public static List<Venda> carregarVendas(String caminho) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(caminho);
            List<Venda> vendas = gson.fromJson(reader, new TypeToken<List<Venda>>(){}.getType());
            reader.close();
            return vendas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Salva uma lista de vendas em um arquivo JSON.
     * 
     * @param produtos Lista de vendas a serem salvas.
     * @param caminho Caminho do arquivo onde os dados serão gravados.
     */
    public static void salvarVendas(List<Venda> vendas, String caminho) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(caminho);
            gson.toJson(vendas, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega uma lista de usuários a partir de um arquivo JSON.
     * 
     * @param caminho Caminho do arquivo JSON.
     * @return Lista de usuários carregados.
     */
    public static List<Usuario> carregarUsuarios(String caminho) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Usuario.class, new util.PolimorphicUsuarioAdapter());
            Gson gson = builder.create();
            FileReader reader = new FileReader(caminho);
            com.google.gson.JsonArray array = com.google.gson.JsonParser.parseReader(reader).getAsJsonArray();
            java.util.List<Usuario> usuarios = new java.util.ArrayList<>();
            for (com.google.gson.JsonElement elem : array) {
                usuarios.add(gson.fromJson(elem, Usuario.class));
            }
            reader.close();
            return usuarios;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Salva uma lista de usuários em um arquivo JSON.
     * 
     * @param produtos Lista de usuários a serem salvos.
     * @param caminho Caminho do arquivo onde os dados serão gravados.
     */
    public static void salvarUsuarios(List<Usuario> usuarios, String caminho) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Usuario.class, new util.PolimorphicUsuarioAdapter());
            Gson gson = builder.create();
            FileWriter writer = new FileWriter(caminho);
            gson.toJson(usuarios, new TypeToken<List<Usuario>>(){}.getType(), writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega o estoque (mapa de idProduto para quantidade) de um arquivo JSON.
     * @param caminho Caminho do arquivo JSON.
     * @return Mapa de idProduto para quantidade, ou null se erro.
     */
    public static Map<String, Integer> carregarEstoque(String caminho) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(caminho);
            Map<String, Integer> mapa = gson.fromJson(reader, new TypeToken<Map<String, Integer>>(){}.getType());
            reader.close();
            return mapa;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Salva o estoque (mapa de idProduto para quantidade) em um arquivo JSON.
     * @param estoque Mapa de idProduto para quantidade.
     * @param caminho Caminho do arquivo JSON.
     */
    public static void salvarEstoque(Map<String, Integer> estoque, String caminho) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(caminho);
            gson.toJson(estoque, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega o caixa (mapa de Dinheiro para quantidade) de um arquivo JSON.
     * @param caminho Caminho do arquivo JSON.
     * @return Mapa de Dinheiro para quantidade, ou null se erro.
     */
    public static Map<Dinheiro, Integer> carregarCaixa(String caminho) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(caminho);
            Map<Dinheiro, Integer> mapa = gson.fromJson(reader, new TypeToken<Map<Dinheiro, Integer>>(){}.getType());
            reader.close();
            return mapa;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Salva o caixa (mapa de Dinheiro para quantidade) em um arquivo JSON.
     * @param caixa Mapa de Dinheiro para quantidade.
     * @param caminho Caminho do arquivo JSON.
     */
    public static void salvarCaixa(Map<Dinheiro, Integer> caixa, String caminho) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(caminho);
            gson.toJson(caixa, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
