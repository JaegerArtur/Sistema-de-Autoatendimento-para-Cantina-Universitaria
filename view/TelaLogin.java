package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.LoginController;
import model.Usuario;
import exception.UsuarioNaoExisteException;
import controller.handlers.TipoUsuarioHandler;
import view.components.TecladoVirtual;

public class TelaLogin extends JFrame {
    private JButton btnMembro, btnVisitante, btnAdmin;
    private LoginController loginController = new LoginController();
    private JPanel painelCentral;
    private JPanel painelFormulario;
    private view.components.TecladoVirtual tecladoVirtual;
    private JTextField campoFocado;
    public view.components.TecladoVirtual getTecladoVirtual() {
        return tecladoVirtual;
    }
    public void setCampoFocado(JTextField campo) {
        this.campoFocado = campo;
        if (tecladoVirtual != null) tecladoVirtual.setCampoFocado(campo);
    }

    public TelaLogin() {
        super("Lanchonete Universitária");
        LoginController.carregarUsuarios();
       
        Container container = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1170, 660);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelImagem = new JPanel();
        painelImagem.setBackground(new Color(0x15478E));
        painelImagem.setPreferredSize(new Dimension(1170, 170));
        ImageIcon iconOriginal = new ImageIcon("view/img/logo/logo-transparent.png");
        Image imgRedimensionada = iconOriginal.getImage().getScaledInstance(209, 156, Image.SCALE_SMOOTH);
        ImageIcon iconReduzido = new ImageIcon(imgRedimensionada);
        JLabel labelImagem = new JLabel(iconReduzido);
        painelImagem.add(labelImagem);
        add(painelImagem, BorderLayout.NORTH);

        painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(new Color(0xEFF1ED));

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(0xEFF1ED));
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        btnMembro = new JButton("Membro");
        btnVisitante = new JButton("Visitante");
        btnAdmin = new JButton("Admin");

        painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridBagLayout());
        painelFormulario.setBackground(new Color(0xEFF1ED));

        // Teclado virtual (componente externo)
        tecladoVirtual = new TecladoVirtual();

        TipoUsuarioHandler handler = new TipoUsuarioHandler(this, btnMembro, btnVisitante, btnAdmin, loginController, painelFormulario);
        btnMembro.addActionListener(handler);
        btnVisitante.addActionListener(handler);
        btnAdmin.addActionListener(handler);

        Dimension tamanhoBotao = new Dimension(120, 60);
        Font fonteBotao = new Font("Arial", Font.BOLD, 20);
        btnMembro.setPreferredSize(tamanhoBotao);
        btnVisitante.setPreferredSize(tamanhoBotao);
        btnAdmin.setPreferredSize(tamanhoBotao);
        btnMembro.setFont(fonteBotao);
        btnVisitante.setFont(fonteBotao);
        btnAdmin.setFont(fonteBotao);

        painelBotoes.add(btnMembro);
        painelBotoes.add(btnVisitante);
        painelBotoes.add(btnAdmin);

        painelCentral.add(painelBotoes, BorderLayout.NORTH);
        painelCentral.add(painelFormulario, BorderLayout.CENTER);
        painelCentral.add(tecladoVirtual, BorderLayout.SOUTH);

        add(painelCentral, BorderLayout.CENTER);
        setVisible(true);
    }
}