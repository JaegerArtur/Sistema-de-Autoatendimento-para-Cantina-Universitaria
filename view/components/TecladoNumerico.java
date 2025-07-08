package view.components;

import javax.swing.*;
import java.awt.*;

public class TecladoNumerico extends JDialog {
    private String valorDigitado = null;

    public TecladoNumerico(JFrame parent, String mensagem, String valorInicial) {
        super(parent, mensagem, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(parent);

        JTextField campo = new JTextField(valorInicial);
        campo.setFont(new Font("Arial", Font.BOLD, 40));
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setEditable(false);
        add(campo, BorderLayout.NORTH);

        JPanel teclado = new JPanel(new GridLayout(4, 3, 10, 10));
        String[] teclas = {"7","8","9","4","5","6","1","2","3","0",",","C"};
        for (String t : teclas) {
            JButton btn = new JButton(t);
            btn.setFont(new Font("Arial", Font.BOLD, 36));
            btn.setPreferredSize(new Dimension(100, 80));
            btn.addActionListener(e -> {
                if (t.equals("C")) {
                    campo.setText("");
                } else if (t.equals(",")) {
                    if (!campo.getText().contains(",")) campo.setText(campo.getText() + ",");
                } else {
                    campo.setText(campo.getText() + t);
                }
            });
            teclado.add(btn);
        }
        add(teclado, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new GridLayout(1, 2, 20, 10));
        JButton ok = new JButton("OK");
        ok.setFont(new Font("Arial", Font.BOLD, 32));
        ok.addActionListener(e -> {
            valorDigitado = campo.getText().trim();
            dispose();
        });
        JButton cancelar = new JButton("Cancelar");
        cancelar.setFont(new Font("Arial", Font.BOLD, 32));
        cancelar.addActionListener(e -> {
            valorDigitado = null;
            dispose();
        });
        botoes.add(ok);
        botoes.add(cancelar);
        add(botoes, BorderLayout.SOUTH);
    }

    public static String mostrar(JFrame parent, String mensagem, String valorInicial) {
        TecladoNumerico dialog = new TecladoNumerico(parent, mensagem, valorInicial);
        dialog.setVisible(true);
        String valor = dialog.valorDigitado;
        return (valor == null || valor.isEmpty()) ? null : valor;
    }
}
