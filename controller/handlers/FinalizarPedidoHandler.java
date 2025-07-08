/**
 * Handler responsável por processar a ação de finalizar pedido/compra.
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package controller.handlers;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import model.Produto;
import view.TelaPagamento;

public class FinalizarPedidoHandler implements ActionListener {
    /** Carrinho de compras com os produtos selecionados pelo usuário. */
    private LinkedHashMap<String, Integer> carrinho;
    
    /** Mapa com todos os produtos disponíveis, usado para buscar informações dos produtos. */
    private Map<String, Produto> produtoMap;
    
    /** Usuário autenticado que está realizando a compra. */
    private model.Usuario usuario;
    
    /** Janela pai que será fechada após finalizar o pedido. */
    private JFrame parent;

    /**
     * Constrói um novo handler para finalizar pedido.
     * 
     * @param carrinho o carrinho de compras com os produtos selecionados
     * @param produtoMap mapa com todos os produtos disponíveis para consulta
     * @param usuario o usuário autenticado que está realizando a compra
     * @param parent a janela pai que será fechada após finalizar
     */
    public FinalizarPedidoHandler(LinkedHashMap<String, Integer> carrinho, Map<String, Produto> produtoMap, model.Usuario usuario, JFrame parent) {
        this.carrinho = carrinho;
        this.produtoMap = produtoMap;
        this.usuario = usuario;
        this.parent = parent;
    }

    /**
     * Processa o evento de finalização do pedido.
     * <p>
     * Verifica se o carrinho possui itens. Se estiver vazio, exibe mensagem
     * de aviso. Caso contrário, calcula o resumo da venda com todos os produtos,
     * quantidades e valor total, fecha a tela atual e abre a tela de pagamento
     * passando todas as informações necessárias.
     * 
     * @param e o evento de ação que disparou este handler
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Adicione produtos ao carrinho antes de finalizar.", "Carrinho vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        StringBuilder resumo = new StringBuilder();
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : carrinho.entrySet()) {
            Produto p = produtoMap.get(entry.getKey());
            if (p != null) {
                double subtotal = p.getPreco() * entry.getValue();
                resumo.append(p.getNome())
                      .append(" x")
                      .append(entry.getValue())
                      .append(" - R$ ")
                      .append(String.format("%.2f", subtotal))
                      .append("\n");
                total += subtotal;
            }
        }
        resumo.append("\nTotal: R$ ").append(String.format("%.2f", total));
        parent.dispose();
        new view.TelaPagamento(resumo.toString(), total, carrinho, produtoMap, usuario);
    }
}
