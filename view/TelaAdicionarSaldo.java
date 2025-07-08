package view;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import model.Membro;
import model.enums.Dinheiro;
import java.util.HashMap;
import java.util.Map;

/**
 * Tela gráfica para membros adicionarem saldo à conta, integrando popup prático de notas/moedas.
 * Exibe o saldo atual, permite adicionar valores em dinheiro físico (simulação) e atualiza o saldo do membro.
 * Utiliza interface amigável para touchscreen, com botões grandes e feedback visual.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.1
 */
public class TelaAdicionarSaldo extends JFrame {
    private Membro membro;
    private JLabel saldoLabel;

    /**
     * Cria a tela de adição de saldo para membros.
     * Mostra o saldo atual e permite adicionar saldo via popup de notas/moedas.
     *
     * @param membro Membro autenticado que irá adicionar saldo.
     */
    public TelaAdicionarSaldo(Membro membro) {
        this.membro = membro;
        setTitle("Adicionar Saldo");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JLabel titulo = new JLabel("Adicionar Saldo à Conta", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        add(titulo, BorderLayout.NORTH);

        saldoLabel = new JLabel("Saldo atual: R$ " + String.format("%.2f", membro.getSaldo()), SwingConstants.CENTER);
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(saldoLabel, BorderLayout.CENTER);

        JButton btnAdicionar = new JButton("Adicionar Saldo");
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 28));
        btnAdicionar.setBackground(new Color(40, 167, 69));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.addActionListener(e -> abrirMenuNotasMoedas());

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 22));
        btnVoltar.setBackground(new Color(220, 53, 69));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> {
            dispose();
            new TelaAtendimento(new LinkedHashMap<>(), membro);
        });

        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        painelSul.add(btnAdicionar);
        painelSul.add(btnVoltar);
        add(painelSul, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Abre o popup de seleção de notas/moedas, soma o valor informado e adiciona ao saldo do membro.
     * Atualiza o saldo na interface e exibe mensagens de sucesso ou erro.
     */
    private void abrirMenuNotasMoedas() {
        double valorAdicionado = 0.0;
        Map<Double, Integer> entregue = view.components.MenuNotasMoedas.mostrar(this, 0.01); // Permite qualquer valor
        if (entregue != null) {
            for (Map.Entry<Double, Integer> entry : entregue.entrySet()) {
                valorAdicionado += entry.getKey() * entry.getValue();
            }
            if (valorAdicionado > 0) {
                try {
                    controller.UsuarioController.alterarSaldo(membro.getCpf(), valorAdicionado);
                    double novoSaldo = controller.UsuarioController.consultarSaldo(membro.getCpf());
                    saldoLabel.setText("Saldo atual: R$ " + String.format("%.2f", membro.getSaldo()));
                    JOptionPane.showMessageDialog(this, "Saldo adicionado com sucesso!\nNovo saldo: R$ " + String.format("%.2f", membro.getSaldo()), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao adicionar saldo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum valor informado.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
