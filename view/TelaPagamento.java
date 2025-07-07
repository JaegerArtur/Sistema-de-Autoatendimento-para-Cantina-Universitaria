package view;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import model.Produto;
import model.Usuario;
import model.Membro;
import model.Visitante;

public class TelaPagamento extends JFrame {
    public TelaPagamento(String resumoVenda, double total, LinkedHashMap<String, Integer> carrinho, Map<String, Produto> produtoMap, Usuario usuario) {
        setTitle("Pagamento");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior com título e resumo lado a lado
        JPanel painelTopo = new JPanel(new BorderLayout(20, 0));
        JLabel titulo = new JLabel("Pagamento", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        painelTopo.add(titulo, BorderLayout.WEST);

        JTextArea resumo = new JTextArea(resumoVenda);
        resumo.setFont(new Font("Arial", Font.PLAIN, 22));
        resumo.setEditable(false);
        resumo.setBackground(new Color(245,245,245));
        resumo.setBorder(BorderFactory.createTitledBorder("Resumo do Pedido"));
        painelTopo.add(new JScrollPane(resumo), BorderLayout.CENTER);
        add(painelTopo, BorderLayout.NORTH);

        // Opções de pagamento
        JPanel opcoes = new JPanel(new GridLayout(1, 3, 40, 40));
        opcoes.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton btnDinheiro = new JButton("Dinheiro");
        JButton btnSaldo = new JButton("Saldo");
        JButton btnSaldoDinheiro = new JButton("<html>Saldo +<br>Dinheiro</html>");

        // Lógica para visitante/membro
        boolean isVisitante = (usuario instanceof Visitante);
        double saldo = 0.0;
        if (usuario instanceof Membro) {
            saldo = ((Membro)usuario).getSaldo();
        }

        btnDinheiro.setFont(new Font("Arial", Font.BOLD, 28));
        btnDinheiro.setBackground(new Color(255, 230, 0));
        btnDinheiro.setFocusPainted(false);

        btnSaldo.setFont(new Font("Arial", Font.BOLD, 28));
        btnSaldoDinheiro.setFont(new Font("Arial", Font.BOLD, 28));
        btnSaldoDinheiro.setBackground(new Color(255, 230, 0));
        btnSaldoDinheiro.setFocusPainted(false);

        if (isVisitante) {
            btnSaldo.setEnabled(false);
            btnSaldo.setBackground(new Color(180, 180, 180));
            btnSaldoDinheiro.setEnabled(false);
            btnSaldoDinheiro.setBackground(new Color(180, 180, 180));
        } else {
            btnSaldo.setBackground(new Color(255, 230, 0));
            btnSaldo.setFocusPainted(false);
            btnSaldoDinheiro.setEnabled(true);
        }

        opcoes.add(btnDinheiro);
        opcoes.add(btnSaldo);
        opcoes.add(btnSaldoDinheiro);
        add(opcoes, BorderLayout.CENTER);

        // Exibe saldo do membro
        JLabel saldoLabel = new JLabel();
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        saldoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (!isVisitante) {
            saldoLabel.setText("Saldo atual: R$ " + String.format("%.2f", saldo));
        } else {
            saldoLabel.setText("Visitante não possui saldo");
        }

        // Handlers externos para métodos de pagamento
        btnDinheiro.addActionListener(new controller.handlers.PagamentoDinheiroHandler(this, total, carrinho, usuario));
        btnSaldo.addActionListener(new controller.handlers.PagamentoSaldoHandler(this, total, carrinho, usuario, saldoLabel));
        btnSaldoDinheiro.addActionListener(new controller.handlers.PagamentoSaldoMaisDinheiroHandler(this, total, carrinho, usuario, saldoLabel));

        // Botão Voltar
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 22));
        btnVoltar.setBackground(new Color(220, 53, 69));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnVoltar.addActionListener(e -> {
            dispose();
            new TelaAtendimento(carrinho, usuario);
        });

        JPanel painelSul = new JPanel(new BorderLayout());
        painelSul.add(btnVoltar, BorderLayout.WEST);
        painelSul.add(saldoLabel, BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);

        setVisible(true);
    }
}
