/**
 * Handler responsável por processar a ação de adicionar produtos ao carrinho de compras.
 * <p>
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Produto;
import javax.swing.JTextArea;

public class AdicionarProdutoHandler implements ActionListener {
    /** Produto que será adicionado ao carrinho quando o handler for acionado. */
    private Produto produto;
    
    /** Carrinho de compras compartilhado, onde os produtos são armazenados. */
    private LinkedHashMap<String, Integer> carrinho;
    
    /** Mapa com todos os produtos disponíveis, usado para buscar informações dos produtos. */
    private Map<String, Produto> produtoMap;
    
    /** Área de texto onde o conteúdo do carrinho é exibido ao usuário. */
    private JTextArea itensCarrinho;

    /**
     * Constrói um novo handler para adicionar produto ao carrinho.
     * 
     * @param produto o produto que será adicionado quando o handler for acionado
     * @param carrinho o carrinho de compras onde o produto será adicionado
     * @param produtoMap mapa com todos os produtos disponíveis para consulta
     * @param itensCarrinho área de texto onde o carrinho será exibido
     */
    public AdicionarProdutoHandler(Produto produto, LinkedHashMap<String, Integer> carrinho, Map<String, Produto> produtoMap, JTextArea itensCarrinho) {
        this.produto = produto;
        this.carrinho = carrinho;
        this.produtoMap = produtoMap;
        this.itensCarrinho = itensCarrinho;
    }

    /**
     * Processa o evento de adição de produto ao carrinho.
     * <p>
     * Adiciona uma unidade do produto ao carrinho (ou incrementa a quantidade
     * se já existir) e atualiza a exibição do carrinho com todos os itens,
     * quantidades e o valor total da compra.
     * 
     * @param e o evento de ação que disparou este handler
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        carrinho.put(produto.getId(), carrinho.getOrDefault(produto.getId(), 0) + 1);
        if (carrinho.isEmpty()) {
            itensCarrinho.setText("Seu carrinho está vazio");
        } else {
            StringBuilder sb = new StringBuilder();
            double total = 0.0;
            for (Map.Entry<String, Integer> entry : carrinho.entrySet()) {
                Produto p = produtoMap.get(entry.getKey());
                if (p != null) {
                    double subtotal = p.getPreco() * entry.getValue();
                    sb.append(p.getNome())
                      .append(" x")
                      .append(entry.getValue())
                      .append(" - R$ ")
                      .append(String.format("%.2f", subtotal))
                      .append("\n\n");
                    total += subtotal;
                }
            }
            sb.append("\nTotal: R$ ").append(String.format("%.2f", total));
            itensCarrinho.setText(sb.toString());
        }
    }
}
