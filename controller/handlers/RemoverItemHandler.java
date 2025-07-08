/**
 * Handler responsável por processar a ação de remover itens do carrinho de compras.
 * Quando acionado, remove o ultimo item do carrinho
 * Se a quantidade do produto chegar a zero, o produto é completamente removido
 * do carrinho.
 * 
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0

 */
package controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Produto;
import javax.swing.JTextArea;

public class RemoverItemHandler implements ActionListener {
    /** Carrinho de compras compartilhado, onde os produtos são armazenados. */
    private LinkedHashMap<String, Integer> carrinho;
    
    /** Mapa com todos os produtos disponíveis, usado para buscar informações dos produtos. */
    private Map<String, Produto> produtoMap;
    
    /** Área de texto onde o conteúdo do carrinho é exibido ao usuário. */
    private JTextArea itensCarrinho;

    /**
     * Constrói um novo handler para remover produtos do carrinho.
     * 
     * @param carrinho o carrinho de compras de onde o produto será removido
     * @param produtoMap mapa com todos os produtos disponíveis para consulta
     * @param itensCarrinho área de texto onde o carrinho será exibido
     */
    public RemoverItemHandler(LinkedHashMap<String, Integer> carrinho, Map<String, Produto> produtoMap, JTextArea itensCarrinho) {
        this.carrinho = carrinho;
        this.produtoMap = produtoMap;
        this.itensCarrinho = itensCarrinho;
    }

    /**
     * Processa o evento de remoção de produto do carrinho.
     * <p>
     * @param e o evento de ação que disparou este handler
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!carrinho.isEmpty()) {
            String ultimoId = null;
            Iterator<String> it = carrinho.keySet().iterator();
            while (it.hasNext()) {
                ultimoId = it.next();
            }
            if (ultimoId != null) {
                int qtd = carrinho.get(ultimoId);
                if (qtd > 1) {
                    carrinho.put(ultimoId, qtd - 1);
                } else {
                    carrinho.remove(ultimoId);
                }
            }
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
}
