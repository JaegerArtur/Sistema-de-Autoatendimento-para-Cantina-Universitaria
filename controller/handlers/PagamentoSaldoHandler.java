package controller.handlers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import model.Usuario;
import model.Membro;
import model.Venda;
import model.enums.FormaPagamento;
import controller.PagamentoController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PagamentoSaldoHandler implements ActionListener {
    private JFrame parent;
    private double total;
    private LinkedHashMap<String, Integer> carrinho;
    private Usuario usuario;
    private JLabel saldoLabel;

    public PagamentoSaldoHandler(JFrame parent, double total, LinkedHashMap<String, Integer> carrinho, Usuario usuario, JLabel saldoLabel) {
        this.parent = parent;
        this.total = total;
        this.carrinho = carrinho;
        this.usuario = usuario;
        this.saldoLabel = saldoLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (usuario instanceof Membro) {
            try {
                Membro membro = (Membro) usuario;
                if (membro.getSaldo() >= total) {
                    String cpf = membro.getCpf();
                    String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    Venda venda = new Venda(cpf, dataHora, new LinkedHashMap<>(carrinho), FormaPagamento.SALDO);
                    boolean sucesso = controller.VendaController.processarVenda(venda, 0);
                    saldoLabel.setText("Saldo atual: R$ " + String.format("%.2f", membro.getSaldo()));
                    if (sucesso) {
                        JOptionPane.showMessageDialog(parent, "Pagamento com saldo realizado com sucesso!", "Pagamento", JOptionPane.INFORMATION_MESSAGE);
                        carrinho.clear();
                        parent.dispose();
                        new view.TelaLogin();
                    } else {
                        JOptionPane.showMessageDialog(parent, "Erro ao processar pagamento.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(parent, "Saldo insuficiente!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Erro ao processar pagamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
