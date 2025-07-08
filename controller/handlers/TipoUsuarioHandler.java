
/**
 * Handler responsável por gerenciar a escolha e autenticação do tipo de usuário
 * (Membro, Visitante ou Admin) na tela de login do sistema de autoatendimento.
 * Exibe e processa os formulários de login correspondentes, integrando com o teclado virtual
 * e otimizando popups para uso em touchscreen.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
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

    /**
     * Cria um handler para os botões de seleção de tipo de usuário.
     * @param telaLogin Referência à tela de login principal.
     * @param btnMembro Botão de login para membros.
     * @param btnVisitante Botão de login para visitantes.
     * @param btnAdmin Botão de login para administradores.
     * @param loginController Controller responsável pela autenticação.
     * @param painelFormulario Painel onde os formulários de login são exibidos.
     */
    public TipoUsuarioHandler(TelaLogin telaLogin, JButton btnMembro, JButton btnVisitante, JButton btnAdmin, LoginController loginController, JPanel painelFormulario) {
        this.telaLogin = telaLogin;
        this.btnMembro = btnMembro;
        this.btnVisitante = btnVisitante;
        this.btnAdmin = btnAdmin;
        this.loginController = loginController;
        this.painelFormulario = painelFormulario;
    }

    /**
     * Trata o evento de clique nos botões de seleção de tipo de usuário,
     * exibindo o formulário e processando o login conforme o tipo escolhido.
     */
    /**
     * {@inheritDoc}
     */
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
            txtCpf.setFont(new Font("Arial", Font.PLAIN, 28));
            JLabel lblSenha = new JLabel("Senha:");
            JPasswordField txtSenha = new JPasswordField(15);
            txtSenha.setFont(new Font("Arial", Font.PLAIN, 28));
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
                    if (cpf.trim().isEmpty() || senha.trim().isEmpty()) {
                        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                        JOptionPane.showMessageDialog(telaLogin, "Preencha CPF e senha.", "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(() -> resetPopupFontSize());
                        return;
                    }
                    Usuario usuario = loginController.loginMembro(cpf, senha);
                    UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                    UIManager.put("OptionPane.font", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("ComboBox.font", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("Panel.font", new Font("Arial", Font.PLAIN, 28));
                    JOptionPane.showMessageDialog(telaLogin, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    telaLogin.dispose();
                    if (usuario instanceof model.Membro) {
                        int opcao = JOptionPane.showOptionDialog(
                            null,
                            "Deseja adicionar saldo à sua conta?",
                            "Opção de Saldo",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Adicionar Saldo", "Ir para Autoatendimento"},
                            "Adicionar Saldo"
                        );
                        // Restaura fontes após popup
                        SwingUtilities.invokeLater(() -> resetPopupFontSize());
                        if (opcao == 0) {
                            new view.TelaAdicionarSaldo((model.Membro) usuario);
                        } else {
                            new TelaAtendimento(new LinkedHashMap<String, Integer>(), usuario);
                        }
                    } else {
                        SwingUtilities.invokeLater(() -> resetPopupFontSize());
                        new TelaAtendimento(new LinkedHashMap<String, Integer>(), usuario);
                    }
                } catch (UsuarioNaoExisteException ex) {
                    UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                    JOptionPane.showMessageDialog(telaLogin, "CPF não encontrado. Verifique seus dados.", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                } catch (IllegalArgumentException ex) {
                    UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                    JOptionPane.showMessageDialog(telaLogin, ex.getMessage(), "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                }
            });
            // Fim do bloco btnMembro
        }
        else if (origem == btnAdmin) {
            JLabel lblLogin = new JLabel("Login:");
            JTextField txtLogin = new JTextField(15);
            txtLogin.setFont(new Font("Arial", Font.PLAIN, 28));
            JLabel lblSenha = new JLabel("Senha:");
            JPasswordField txtSenha = new JPasswordField(15);
            txtSenha.setFont(new Font("Arial", Font.PLAIN, 28));
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
                    if (login.trim().isEmpty() || senha.trim().isEmpty()) {
                        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                        JOptionPane.showMessageDialog(telaLogin, "Preencha login e senha.", "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(() -> resetPopupFontSize());
                        return;
                    }
                    Usuario usuario = loginController.loginAdmin(login, senha);
                    UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                    JOptionPane.showMessageDialog(telaLogin, "Login de admin realizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    telaLogin.dispose();
                    new view.TelaAdmin();
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                } catch (UsuarioNaoExisteException ex) {
                    UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                    JOptionPane.showMessageDialog(telaLogin, "Login de admin não encontrado. Verifique seus dados.", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                } catch (IllegalArgumentException ex) {
                    UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                    UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                    JOptionPane.showMessageDialog(telaLogin, ex.getMessage(), "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                }
            });
        }
        else if (origem == btnVisitante) {
            JLabel lblNome = new JLabel("Nome:");
            JTextField txtNome = new JTextField(15);
            txtNome.setFont(new Font("Arial", Font.PLAIN, 28));
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
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 28));
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 28));
                if (!nome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(telaLogin, "Bem-vindo, " + nome + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    telaLogin.dispose();
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                    new TelaAtendimento(new LinkedHashMap<String, Integer>(), new Visitante(nome));
                } else {
                    JOptionPane.showMessageDialog(telaLogin, "Digite um nome para continuar.", "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
                    SwingUtilities.invokeLater(() -> resetPopupFontSize());
                }
            });
        }
        painelFormulario.revalidate();
        painelFormulario.repaint();
    }
     // Restaura fontes dos popups
    /**
     * Restaura o tamanho padrão das fontes dos popups após customização para touchscreen.
     */
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
