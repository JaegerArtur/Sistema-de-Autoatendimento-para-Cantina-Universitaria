package controller.handlers;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import controller.LoginController;
import model.Usuario;
import model.Visitante;
import exception.UsuarioNaoExisteException;
import view.TelaLogin;
import view.TelaAtendimento;
import java.util.LinkedHashMap;

public class TipoUsuarioHandler implements ActionListener {
    private TelaLogin telaLogin;
    private JButton btnMembro, btnVisitante, btnAdmin;
    private LoginController loginController;
    private JPanel painelFormulario;

    public TipoUsuarioHandler(TelaLogin telaLogin, JButton btnMembro, JButton btnVisitante, JButton btnAdmin, LoginController loginController, JPanel painelFormulario) {
        this.telaLogin = telaLogin;
        this.btnMembro = btnMembro;
        this.btnVisitante = btnVisitante;
        this.btnAdmin = btnAdmin;
        this.loginController = loginController;
        this.painelFormulario = painelFormulario;
    }

    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();
        painelFormulario.removeAll();
        painelFormulario.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        if(origem == btnMembro) {
            JLabel lblCpf = new JLabel("CPF:");
            JTextField txtCpf = new JTextField(15);
            JLabel lblSenha = new JLabel("Senha:");
            JPasswordField txtSenha = new JPasswordField(15);
            JButton btnLogin = new JButton("Login");

            lblCpf.setFont(new Font("Arial", Font.PLAIN, 20));
            lblSenha.setFont(new Font("Arial", Font.PLAIN, 20));
            btnLogin.setFont(new Font("Arial", Font.BOLD, 20));

            painelFormulario.add(lblCpf, gbc);
            gbc.gridx = 1;
            painelFormulario.add(txtCpf, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            painelFormulario.add(lblSenha, gbc);
            gbc.gridx = 1;
            painelFormulario.add(txtSenha, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2;
            painelFormulario.add(btnLogin, gbc);

            // Integração com teclado virtual
            telaLogin.setCampoFocado(txtCpf);
            txtCpf.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    telaLogin.setCampoFocado(txtCpf);
                }
            });
            txtSenha.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    telaLogin.setCampoFocado(txtSenha);
                }
            });

            btnLogin.addActionListener(ev -> {
                String cpf = txtCpf.getText();
                String senha = new String(txtSenha.getPassword());
                try {
                    Usuario usuario = loginController.loginMembro(cpf, senha);
                    JOptionPane.showMessageDialog(telaLogin, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    telaLogin.dispose();
                    new TelaAtendimento(new LinkedHashMap<String, Integer>(), usuario);
                } catch (UsuarioNaoExisteException ex) {
                    JOptionPane.showMessageDialog(telaLogin, "CPF não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(telaLogin, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (origem == btnAdmin) {
            JLabel lblLogin = new JLabel("Login:");
            JTextField txtLogin = new JTextField(15);
            JLabel lblSenha = new JLabel("Senha:");
            JPasswordField txtSenha = new JPasswordField(15);
            JButton btnLogin = new JButton("Login");

            lblLogin.setFont(new Font("Arial", Font.PLAIN, 20));
            lblSenha.setFont(new Font("Arial", Font.PLAIN, 20));
            btnLogin.setFont(new Font("Arial", Font.BOLD, 20));

            painelFormulario.add(lblLogin, gbc);
            gbc.gridx = 1;
            painelFormulario.add(txtLogin, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            painelFormulario.add(lblSenha, gbc);
            gbc.gridx = 1;
            painelFormulario.add(txtSenha, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2;
            painelFormulario.add(btnLogin, gbc);

            // Integração com teclado virtual
            telaLogin.setCampoFocado(txtLogin);
            txtLogin.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    telaLogin.setCampoFocado(txtLogin);
                }
            });
            txtSenha.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    telaLogin.setCampoFocado(txtSenha);
                }
            });

            btnLogin.addActionListener(ev -> {
                String login = txtLogin.getText();
                String senha = new String(txtSenha.getPassword());
                try {
                    Usuario usuario = loginController.loginAdmin(login, senha);
                    JOptionPane.showMessageDialog(telaLogin, "Login de admin realizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    telaLogin.dispose();
                    new view.TelaAdmin();
                } catch (UsuarioNaoExisteException ex) {
                    JOptionPane.showMessageDialog(telaLogin, "Login de admin não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(telaLogin, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (origem == btnVisitante) {
            JLabel lblNome = new JLabel("Nome:");
            JTextField txtNome = new JTextField(15);
            JButton btnEntrar = new JButton("Entrar");

            lblNome.setFont(new Font("Arial", Font.PLAIN, 20));
            btnEntrar.setFont(new Font("Arial", Font.BOLD, 20));

            painelFormulario.add(lblNome, gbc);
            gbc.gridx = 1;
            painelFormulario.add(txtNome, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 2;
            painelFormulario.add(btnEntrar, gbc);

            // Integração com teclado virtual
            telaLogin.setCampoFocado(txtNome);
            txtNome.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    telaLogin.setCampoFocado(txtNome);
                }
            });

            btnEntrar.addActionListener(ev -> {
                String nome = txtNome.getText();
                if (!nome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(telaLogin, "Bem-vindo, " + nome + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    telaLogin.dispose();
                    new TelaAtendimento(new LinkedHashMap<String, Integer>(), new Visitante(nome));
                } else {
                    JOptionPane.showMessageDialog(telaLogin, "Digite um nome.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        painelFormulario.revalidate();
        painelFormulario.repaint();
    }
}
