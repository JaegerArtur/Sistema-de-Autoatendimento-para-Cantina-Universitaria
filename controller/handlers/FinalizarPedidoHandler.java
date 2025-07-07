package controller.handlers;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import model.Produto;
import view.TelaPagamento;

public class FinalizarPedidoHandler implements ActionListener {
    private LinkedHashMap<String, Integer> carrinho;
    private Map<String, Produto> produtoMap;
    private model.Usuario usuario;
    private JFrame parent;

    public FinalizarPedidoHandler(LinkedHashMap<String, Integer> carrinho, Map<String, Produto> produtoMap, model.Usuario usuario, JFrame parent) {
        this.carrinho = carrinho;
        this.produtoMap = produtoMap;
        this.usuario = usuario;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Adicione produtos ao carrinho antes de finalizar.", "Carrinho vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Monta resumo da venda
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
        // Abre TelaPagamento passando o resumo, valor total, carrinho e produtoMap
        parent.dispose();
        new view.TelaPagamento(resumo.toString(), total, carrinho, produtoMap, usuario);
    }
}
