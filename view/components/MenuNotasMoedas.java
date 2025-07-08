package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuNotasMoedas extends JDialog {
    private final Map<Double, Integer> resultado = new LinkedHashMap<>();
    private double totalEntregue = 0.0;
    private final JLabel totalLabel;
    private boolean confirmado = false;

    private static final double[] DENOMINACOES = {100, 50, 20, 10, 5, 2, 1, 0.5, 0.25, 0.10, 0.05};

    public MenuNotasMoedas(JFrame parent, double totalAPagar) {
        super(parent, "Informe as notas/moedas entregues", true);
        setLayout(new BorderLayout(10, 5));
        setSize(1170, 660);
        setLocationRelativeTo(parent);

        JPanel painelDenominacoes = new JPanel(new GridLayout(DENOMINACOES.length, 4, 15, 5));
        Map<Double, JTextField> campos = new LinkedHashMap<>();

        Font fonteLabel = new Font("Arial", Font.BOLD, 20);
        Font fonteBotao = new Font("Arial", Font.BOLD, 24);
        Font fonteCampo = new Font("Arial", Font.BOLD, 24);

        for (double valor : DENOMINACOES) {
            JLabel lbl = new JLabel((valor >= 1 ? "R$ " : "Moeda R$ ") + String.format("%.2f", valor).replace('.', ','));
            lbl.setFont(fonteLabel);
            JButton menos = new JButton("-");
            menos.setFont(fonteBotao);
            JButton mais = new JButton("+");
            mais.setFont(fonteBotao);
            JTextField campo = new JTextField("0", 2);
            campo.setFont(fonteCampo);
            campo.setHorizontalAlignment(JTextField.CENTER);
            campos.put(valor, campo);
            menos.addActionListener(e -> {
                int v = Integer.parseInt(campo.getText());
                if (v > 0) campo.setText(String.valueOf(v - 1));
                atualizarTotal(campos);
            });
            mais.addActionListener(e -> {
                int v = Integer.parseInt(campo.getText());
                campo.setText(String.valueOf(v + 1));
                atualizarTotal(campos);
            });
            campo.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    atualizarTotal(campos);
                }
            });
            painelDenominacoes.add(lbl);
            painelDenominacoes.add(menos);
            painelDenominacoes.add(campo);
            painelDenominacoes.add(mais);
        }

        JScrollPane scroll = new JScrollPane(painelDenominacoes);
        scroll.getVerticalScrollBar().setUnitIncrement(32);
        add(scroll, BorderLayout.CENTER);

        JPanel painelTotais = new JPanel(new GridLayout(2, 1));
        JLabel totalCompraLabel = new JLabel("Total da compra: R$ " + String.format("%.2f", totalAPagar).replace('.', ','));
        totalCompraLabel.setFont(new Font("Arial", Font.BOLD, 26));
        totalCompraLabel.setHorizontalAlignment(SwingConstants.CENTER);
        painelTotais.add(totalCompraLabel);

        totalLabel = new JLabel("Total entregue: R$ 0,00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 26));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        painelTotais.add(totalLabel);
        add(painelTotais, BorderLayout.NORTH);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setFont(fonteBotao);
        btnConfirmar.setBackground(new Color(40, 167, 69));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setPreferredSize(new Dimension(220, 60));
        btnConfirmar.addActionListener(e -> {
            if (totalEntregue < totalAPagar) {
                JOptionPane.showMessageDialog(this, "Valor entregue insuficiente!", "Atenção", JOptionPane.WARNING_MESSAGE);
            } else {
                for (double valor : DENOMINACOES) {
                    resultado.put(valor, Integer.parseInt(campos.get(valor).getText()));
                }
                confirmado = true;
                dispose();
            }
        });
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(fonteBotao);
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setPreferredSize(new Dimension(220, 60));
        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void atualizarTotal(Map<Double, JTextField> campos) {
        totalEntregue = 0.0;
        for (Map.Entry<Double, JTextField> entry : campos.entrySet()) {
            try {
                int qtd = Integer.parseInt(entry.getValue().getText());
                totalEntregue += entry.getKey() * qtd;
            } catch (Exception ignored) {}
        }
        totalLabel.setText("Total entregue: R$ " + String.format("%.2f", totalEntregue).replace('.', ','));
    }

    public static Map<Double, Integer> mostrar(JFrame parent, double totalAPagar) {
        MenuNotasMoedas dialog = new MenuNotasMoedas(parent, totalAPagar);
        dialog.setVisible(true);
        return dialog.confirmado ? dialog.resultado : null;
    }
}
