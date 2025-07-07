package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TecladoVirtual extends JPanel {
    private JTextField campoFocado;

    public TecladoVirtual() {
        setLayout(new GridLayout(4, 10, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));
        setBackground(new Color(0xEFF1ED));
        String letras = "QWERTYUIOPASDFGHJKLZÇXCVBNM";
        String numeros = "1234567890";
        // Primeira linha: números
        for (char c : numeros.toCharArray()) {
            JButton btn = new JButton(String.valueOf(c));
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(e -> inserirNoCampoFocado(String.valueOf(c)));
            add(btn);
        }
        // Segunda a quarta linha: letras
        for (char c : letras.toCharArray()) {
            JButton btn = new JButton(String.valueOf(c));
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(e -> inserirNoCampoFocado(String.valueOf(c)));
            add(btn);
        }
        // Backspace
        JButton backspace = new JButton("←");
        backspace.setFont(new Font("Arial", Font.BOLD, 20));
        backspace.addActionListener(e -> apagarNoCampoFocado());
        add(backspace);
        // Espaço
        JButton espaco = new JButton("Espaço");
        espaco.setFont(new Font("Arial", Font.BOLD, 18));
        espaco.addActionListener(e -> inserirNoCampoFocado(" "));
        add(espaco);
        // Enter
        JButton enter = new JButton("Enter");
        enter.setFont(new Font("Arial", Font.BOLD, 18));
        add(enter);
    }

    public void setCampoFocado(JTextField campo) {
        this.campoFocado = campo;
    }

    private void inserirNoCampoFocado(String texto) {
        if (campoFocado != null) {
            int pos = campoFocado.getCaretPosition();
            StringBuilder sb = new StringBuilder(campoFocado.getText());
            sb.insert(pos, texto);
            campoFocado.setText(sb.toString());
            campoFocado.requestFocus();
            campoFocado.setCaretPosition(pos + texto.length());
        }
    }

    private void apagarNoCampoFocado() {
        if (campoFocado != null) {
            int pos = campoFocado.getCaretPosition();
            if (pos > 0) {
                StringBuilder sb = new StringBuilder(campoFocado.getText());
                sb.deleteCharAt(pos - 1);
                campoFocado.setText(sb.toString());
                campoFocado.requestFocus();
                campoFocado.setCaretPosition(pos - 1);
            }
        }
    }
}
