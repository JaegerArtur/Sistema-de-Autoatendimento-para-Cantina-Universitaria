package view;

import javax.swing.*;
import java.awt.*;

public class TelaAdmin extends JFrame {
    public TelaAdmin() {
        setTitle("Painel Administrativo - Cantina");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Painel do Administrador", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(2, 3, 40, 40));
        String[] opcoes = {"Relatórios", "Produtos", "Usuários", "Estoque", "Caixa", "Sair"};
        for (String opcao : opcoes) {
            JButton btn = new JButton(opcao);
            btn.setFont(new Font("Arial", Font.BOLD, 28));
            btn.setBackground(new Color(255, 230, 0));
            btn.setFocusPainted(false);
            botoes.add(btn);
        }
        botoes.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(botoes, BorderLayout.CENTER);

        setVisible(true);
    }
}
