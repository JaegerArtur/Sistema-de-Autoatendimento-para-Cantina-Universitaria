package model;

import java.util.UUID;
import java.util.*;
import model.enums.FormaPagamento;
import service.ProdutoService;

/**
 * Representa uma venda realizada no sistema da cantina.
 * Cada venda contém informações sobre o usuário que a realizou, data e hora,
 * os itens comprados, o valor total e a forma de pagamento utilizada.
 * 
 * O valor total é calculado automaticamente com base nos produtos e quantidades compradas.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public class Venda {
    
    /** Identificador único da venda (UUID). */
    private String id;
    
    /** CPF do usuário que realizou a compra. */
    private String cpfUsuario;
    
    /** Data e hora em que a venda foi realizada. */
    private String dataHoraVenda;
    
    /** Mapa com os IDs dos produtos e suas respectivas quantidades. */
    private Map<String, Integer> itens;
    
    /** Valor total da venda, calculado com base nos itens. */
    private double valorTotal;
    
    /** Forma de pagamento utilizada na venda. */
    private FormaPagamento formaPagamento;

    /**
     * Constrói um novo objeto Venda com os dados fornecidos.
     * 
     * @param cpfUsuario     CPF do usuário que realizou a compra.
     * @param dataHoraVenda   Data e hora da venda (formato texto).
     * @param itens           Mapa de produtos vendidos (ID do produto → quantidade).
     * @param formaPagamento  Forma de pagamento utilizada.
     */
    public Venda(String cpfUsuario, String dataHoraVenda, Map<String, Integer> itens, FormaPagamento formaPagamento) throws exception.ProdutoNaoExisteException {
        this.id = UUID.randomUUID().toString();
        this.cpfUsuario = cpfUsuario;
        this.dataHoraVenda = dataHoraVenda;
        this.itens = itens;
        this.valorTotal = calcularTotal(itens);
        this.formaPagamento = formaPagamento;
    }

    /**
     * Calcula o valor total da venda com base nos itens e quantidades.
     * Busca o preço dos produtos por meio do ProdutoService.
     * 
     * @param itens Mapa de produtos (ID do produto → quantidade).
     * @return Valor total da venda.
     */
    private double calcularTotal(Map<String, Integer> itens) throws exception.ProdutoNaoExisteException {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : itens.entrySet()) {
            String idProduto = entry.getKey();
            int quantidade = entry.getValue();

            Produto produto = ProdutoService.getProdutoId(idProduto);

            if (produto != null) {
                total += produto.getPreco() * quantidade;
            }
        }
        return total;
    }

    public String getId() {
        return id;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public String getDataHoraVenda() {
        return dataHoraVenda;
    }

    public Map<String, Integer> getItens() {
        return itens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }
}
