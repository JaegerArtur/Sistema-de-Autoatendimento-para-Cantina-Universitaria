package controller.handlers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.*;
import model.enums.*;
import controller.PagamentoController;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Font;

public class PagamentoDinheiroHandler implements ActionListener {
    private JFrame parent;
    private double total;
    private LinkedHashMap<String, Integer> carrinho;
    private Usuario usuario;

    public PagamentoDinheiroHandler(JFrame parent, double total, LinkedHashMap<String, Integer> carrinho, Usuario usuario) {
        this.parent = parent;
        this.total = total;
        this.carrinho = carrinho;
        this.usuario = usuario;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean pago = false;
        while (!pago) {
            // Novo menu prático para informar notas/moedas
            Map<Double, Integer> entregueMenu = view.components.MenuNotasMoedas.mostrar(parent, total);
            if (entregueMenu == null) {
                // Restaura fontes
                SwingUtilities.invokeLater(() -> resetPopupFontSize());
                return; // Cancelado
            }
            Map<Dinheiro, Integer> entregue = new HashMap<>();
            double valorEntregue = 0.0;
            for (Map.Entry<Double, Integer> entry : entregueMenu.entrySet()) {
                double valor = entry.getKey();
                int qtd = entry.getValue();
                if (qtd > 0) {
                    for (Dinheiro tipo : Dinheiro.values()) {
                        if (Math.abs(tipo.getValor() - valor) < 0.001) {
                            entregue.put(tipo, qtd);
                            valorEntregue += valor * qtd;
                        }
                    }
                }
            }
            if (valorEntregue < total) {
                JOptionPane.showMessageDialog(parent, "Valor entregue insuficiente!", "Atenção", JOptionPane.WARNING_MESSAGE);
                SwingUtilities.invokeLater(() -> resetPopupFontSize());
                continue;
            }
            double troco = valorEntregue - total;
            // 2. Verifica se o caixa pode dar o troco (considerando o caixa + o que foi entregue)
            // Garante que o caixa simulado parte do caixa real carregado do arquivo
            Map<Dinheiro, Integer> caixaReal = PagamentoController.getNotasMoedas();

            if (PagamentoController.calcularTrocoDetalhado(troco) == null) {
                JOptionPane.showMessageDialog(parent, "Não é possível fornecer o troco exato com as notas/moedas disponíveis no caixa (após receber o pagamento)!", "Erro", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.invokeLater(() -> resetPopupFontSize());
                continue;
            }
            // 3. Calcula detalhamento do troco usando o caixa simulado (após receber o pagamento)
            Map<Dinheiro, Integer> trocoDetalhado = PagamentoController.calcularTrocoDetalhado(troco);
            if (trocoDetalhado == null) {
                JOptionPane.showMessageDialog(parent, "Erro ao calcular o troco detalhado!", "Erro", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.invokeLater(() -> resetPopupFontSize());
                continue;
            }
            // 4. Atualiza o caixa: adiciona o que foi entregue
            for (Map.Entry<Dinheiro, Integer> entry : entregue.entrySet()) {
                PagamentoController.adicionarNotaMoeda(entry.getKey(), entry.getValue());
            }
            // 5. Remove do caixa as notas/moedas usadas para o troco
            for (Map.Entry<Dinheiro, Integer> entry : trocoDetalhado.entrySet()) {
                PagamentoController.removerNotaMoeda(entry.getKey(), entry.getValue());
            }
            PagamentoController.salvarCaixa();
            try {
                String cpf = (usuario instanceof Membro) ? ((Membro)usuario).getCpf() : usuario.getNome();
                String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                Venda venda = new Venda(cpf, dataHora, new LinkedHashMap<>(carrinho), FormaPagamento.DINHEIRO);
                boolean sucesso = controller.VendaController.processarVenda(venda, valorEntregue);
                if (sucesso) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Pagamento realizado com sucesso!\nTroco: R$ ").append(String.format("%.2f", troco));
                    if(troco > 0) sb.append("\n\nDetalhamento do troco:\n");
                    for (Map.Entry<Dinheiro, Integer> entry : trocoDetalhado.entrySet()) {
                        if (entry.getValue() > 0) {
                            sb.append(entry.getValue()).append(" x ");
                            if (entry.getKey().name().startsWith("MOEDA_")) {
                                sb.append("Moeda R$ ");
                            } else {
                                sb.append("Cédula R$ ");
                            }
                            sb.append(String.format("%.2f", entry.getKey().getValor()).replace(".", ",")).append("\n");
                        }
                    }
                    JOptionPane.showMessageDialog(parent, sb.toString(), "Pagamento", JOptionPane.INFORMATION_MESSAGE);
                    carrinho.clear();
                    parent.dispose();
                    new view.TelaLogin();
                    pago = true;
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                } else {
                    JOptionPane.showMessageDialog(parent, "Erro ao processar pagamento.", "Erro", JOptionPane.ERROR_MESSAGE);
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Erro ao processar pagamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.invokeLater(() -> resetPopupFontSize());
                return;
            }
        }
    }

    // Restaura fontes dos popups
    private void resetPopupFontSize() {
        UIManager.put("OptionPane.messageFont", UIManager.getDefaults().getFont("OptionPane.messageFont"));
        UIManager.put("OptionPane.buttonFont", UIManager.getDefaults().getFont("OptionPane.buttonFont"));
        UIManager.put("OptionPane.font", UIManager.getDefaults().getFont("OptionPane.font"));
        UIManager.put("TextField.font", UIManager.getDefaults().getFont("TextField.font"));
        UIManager.put("ComboBox.font", UIManager.getDefaults().getFont("ComboBox.font"));
        UIManager.put("Label.font", UIManager.getDefaults().getFont("Label.font"));
        UIManager.put("Panel.font", UIManager.getDefaults().getFont("Panel.font"));
    }
}

