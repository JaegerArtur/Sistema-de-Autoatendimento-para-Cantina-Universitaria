package controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Produto;
import javax.swing.JTextArea;

public class AdicionarProdutoHandler implements ActionListener {
    private Produto produto;
    private LinkedHashMap<String, Integer> carrinho;
    private Map<String, Produto> produtoMap;
    private JTextArea itensCarrinho;

    public AdicionarProdutoHandler(Produto produto, LinkedHashMap<String, Integer> carrinho, Map<String, Produto> produtoMap, JTextArea itensCarrinho) {
        this.produto = produto;
        this.carrinho = carrinho;
        this.produtoMap = produtoMap;
        this.itensCarrinho = itensCarrinho;
    }

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
