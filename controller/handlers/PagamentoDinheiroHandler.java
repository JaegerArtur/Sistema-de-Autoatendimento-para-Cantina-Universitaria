package controller.handlers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Usuario;
import model.Membro;
import model.Venda;
import model.enums.FormaPagamento;
import model.enums.Dinheiro;
import controller.PagamentoController;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            // 1. Pergunta ao usuário quais notas/moedas está entregando
            Map<Dinheiro, Integer> entregue = new HashMap<>();
            double valorEntregue = 0.0;
            // Primeiro, moedas
            JOptionPane.showMessageDialog(parent, "Informe as quantidades de cada moeda entregue:", "Pagamento em Dinheiro", JOptionPane.INFORMATION_MESSAGE);
            for (Dinheiro tipo : Dinheiro.values()) {
                if (tipo.name().startsWith("MOEDA_")) {
                    String label = String.format("Moeda de R$ %.2f", tipo.getValor()).replace(".", ",");
                    String qtdStr = JOptionPane.showInputDialog(parent, label + " - Quantidade:", "Pagamento em Dinheiro", JOptionPane.PLAIN_MESSAGE);
                    if (qtdStr == null) return; // Cancelado
                    int qtd = 0;
                    try { qtd = Integer.parseInt(qtdStr); } catch (Exception ex) { qtd = 0; }
                    if (qtd > 0) {
                        entregue.put(tipo, qtd);
                        valorEntregue += tipo.getValor() * qtd;
                    }
                }
            }
            // Depois, cédulas
            JOptionPane.showMessageDialog(parent, "Informe as quantidades de cada cédula entregue:", "Pagamento em Dinheiro", JOptionPane.INFORMATION_MESSAGE);
            for (Dinheiro tipo : Dinheiro.values()) {
                if (tipo.name().startsWith("CEDULA_")) {
                    String label = String.format("Cédula de R$ %.2f", tipo.getValor()).replace(".", ",");
                    String qtdStr = JOptionPane.showInputDialog(parent, label + " - Quantidade:", "Pagamento em Dinheiro", JOptionPane.PLAIN_MESSAGE);
                    if (qtdStr == null) return; // Cancelado
                    int qtd = 0;
                    try { qtd = Integer.parseInt(qtdStr); } catch (Exception ex) { qtd = 0; }
                    if (qtd > 0) {
                        entregue.put(tipo, qtd);
                        valorEntregue += tipo.getValor() * qtd;
                    }
                }
            }
            if (valorEntregue < total) {
                JOptionPane.showMessageDialog(parent, "Valor insuficiente!", "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            double troco = valorEntregue - total;
            // 2. Verifica se o caixa pode dar o troco (considerando o caixa + o que foi entregue)
            Map<Dinheiro, Integer> caixaSimulado = new HashMap<>(PagamentoController.getNotasMoedas());
            for (Map.Entry<Dinheiro, Integer> entry : entregue.entrySet()) {
                caixaSimulado.put(entry.getKey(), caixaSimulado.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
            if (PagamentoController.calcularTrocoDetalhado(troco, caixaSimulado) == null) {
                JOptionPane.showMessageDialog(parent, "Não é possível fornecer o troco exato com as notas/moedas disponíveis no caixa (após receber o pagamento)!", "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            // 3. Calcula detalhamento do troco usando o caixa simulado (após receber o pagamento)
            Map<Dinheiro, Integer> trocoDetalhado = PagamentoController.calcularTrocoDetalhado(troco, caixaSimulado);
            if (trocoDetalhado == null) {
                JOptionPane.showMessageDialog(parent, "Erro ao calcular o troco detalhado!", "Erro", JOptionPane.ERROR_MESSAGE);
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
                PagamentoController.realizarPagamentoDinheiro(venda, valorEntregue);
                // 6. Exibe detalhamento do troco
                StringBuilder sb = new StringBuilder();
                sb.append("Pagamento realizado com sucesso!\nTroco: R$ ").append(String.format("%.2f", troco)).append("\n\nDetalhamento do troco:\n");
                for (Map.Entry<Dinheiro, Integer> entry : trocoDetalhado.entrySet()) {
                    if (entry.getValue() > 0) {
                        sb.append(entry.getValue()).append(" x ")
                          .append(entry.getKey().name().replace("MOEDA_", "Moeda R$ ").replace("CEDULA_", "Cédula R$ "))
                          .append(String.format("%.2f", entry.getKey().getValor())).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(parent, sb.toString(), "Pagamento", JOptionPane.INFORMATION_MESSAGE);
                carrinho.clear();
                parent.dispose();
                new view.TelaLogin();
                pago = true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Erro ao processar pagamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    // ...
    }

