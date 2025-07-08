
/**
 * Tela administrativa do sistema, permitindo acesso a relatórios, estoque, produtos,
 * usuários e caixa da cantina universitária.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package view;

import javax.swing.*;
import java.awt.*;

import controller.*;
import java.util.Map;
import java.util.List;
import model.enums.Dinheiro;
import view.components.TecladoNumerico;

public class TelaAdmin extends JFrame {
    /**
     * Cria a tela administrativa, exibindo opções de gerenciamento e relatórios.
     */
    public TelaAdmin() {
        setTitle("Painel Administrativo - Cantina");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Painel do Administrador", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(3, 3, 40, 40));
        String[] opcoes = {"Relatórios", "Produtos", "Usuários", "Estoque", "Caixa", "Histórico de Vendas", "", "", "Sair"};
        for (String opcao : opcoes) {
            JButton btn = new JButton(opcao);
            if (opcao.isEmpty()) {
                btn.setVisible(false); // Oculta botões vazios
            } else {
                btn.setFont(new Font("Arial", Font.BOLD, 38)); // fonte maior
                btn.setBackground(new Color(255, 230, 0));
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(260, 140)); // aumenta tamanho do botão
                btn.setMargin(new Insets(30, 30, 30, 30)); // padding maior
                btn.addActionListener(e -> handleOpcao(opcao));
            }
            botoes.add(btn);
        }
        botoes.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(botoes, BorderLayout.CENTER);

        setVisible(true);
    }

    private void handleOpcao(String opcao) {
        switch (opcao) {
            case "Relatórios":
                mostrarRelatorios();
                break;
            case "Produtos":
                mostrarProdutos();
                break;
            case "Usuários":
                mostrarUsuarios();
                break;
            case "Estoque":
                mostrarEstoque();
                break;
            case "Caixa":
                mostrarCaixa();
                break;
            case "Histórico de Vendas":
                mostrarHistoricoVendas();
                break;
            case "Sair":
                dispose();
                new TelaLogin();
                break;
        }
    }

    private void mostrarRelatorios() {
        // Garante que produtos e estoque estejam carregados antes dos relatórios
        controller.ProdutoController.carregarProdutos();
        controller.EstoqueController.carregarEstoque();

        StringBuilder sb = new StringBuilder();
        sb.append(controller.AdminController.relatorioResumoGeral()).append("\n");
        sb.append("\n--- Caixa Detalhado ---\n");
        Map<Dinheiro, Integer> caixa = controller.AdminController.relatorioCaixaDetalhado();
        for (Map.Entry<Dinheiro, Integer> entry : caixa.entrySet()) {
            if (entry.getValue() > 0) {
                String tipo = entry.getKey().name().startsWith("MOEDA_") ? "Moeda R$ " : "Cédula R$ ";
                sb.append(tipo)
                  .append(String.format("%.2f", entry.getKey().getValor()).replace('.', ','))
                  .append(": ")
                  .append(entry.getValue())
                  .append("\n");
            }
        }

        // Relatório de horários de maior movimento
        sb.append("\n--- Horários de Maior Movimento ---\n");
        sb.append(controller.AdminController.relatorioHorariosMovimento()).append("\n");

        // Relatório de produtos vencidos/próximos do vencimento
        sb.append("\n--- Produtos Vencidos ou Próximos do Vencimento ---\n");
        sb.append(controller.AdminController.relatorioProdutosVencidos()).append("\n");

        setPopupFontSize(14);
        JOptionPane.showMessageDialog(this, sb.toString(), "Relatórios", JOptionPane.INFORMATION_MESSAGE);
        resetPopupFontSize();
    }

    private void mostrarProdutos() {
        List<model.Produto> produtos = controller.AdminController.listarProdutos();
        StringBuilder sb = new StringBuilder("Produtos cadastrados:\n");
        for (model.Produto p : produtos) {
            sb.append("- ").append(p.getNome()).append(" | Preço: R$ ")
              .append(String.format("%.2f", p.getPreco()).replace('.', ','))
              .append("\n");
        }
        setPopupFontSize(28);
        JOptionPane.showMessageDialog(this, sb.toString(), "Produtos", JOptionPane.INFORMATION_MESSAGE);
        resetPopupFontSize();
    }

    private void mostrarUsuarios() {
        List<model.Usuario> usuarios = controller.AdminController.listarUsuarios();
        StringBuilder sb = new StringBuilder("Usuários cadastrados:\n");
        for (model.Usuario u : usuarios) {
            sb.append("- ").append(u.getNome()).append(" | Tipo: ")
              .append(u.getClass().getSimpleName()).append("\n");
        }
        setPopupFontSize(28);
        JOptionPane.showMessageDialog(this, sb.toString(), "Usuários", JOptionPane.INFORMATION_MESSAGE);
        resetPopupFontSize();
    }

    private void mostrarEstoque() {
        new view.TelaVisualizarEstoque(this);
    }

    private void mostrarCaixa() {
        double valor = controller.AdminController.getValorCaixa();
        setPopupFontSize(28);
        int opt = JOptionPane.showOptionDialog(this, "Valor total em caixa: R$ " + String.format("%.2f", valor).replace('.', ',') + "\nDeseja retirar algum valor?", "Caixa", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Retirar do Caixa", "Fechar"}, "Fechar");
        if (opt == 0) {
            String valorStr = mostrarTecladoNumerico("Informe o valor a ser retirado:", "0,00");
            if (valorStr != null) {
                try {
                    double retirar = Double.parseDouble(valorStr.replace(",", "."));
                    if (retirar > 0 && retirar <= valor) {
                        java.util.Map<model.enums.Dinheiro, Integer> detalhamento = controller.CaixaController.retirarDoCaixaDetalhado(retirar);
                        StringBuilder sb = new StringBuilder("Valor retirado com sucesso!\n\nDetalhamento da retirada:\n");
                        for (java.util.Map.Entry<model.enums.Dinheiro, Integer> entry : detalhamento.entrySet()) {
                            if (entry.getValue() > 0) {
                                String tipo = entry.getKey().name().startsWith("MOEDA_") ? "Moeda R$ " : "Cédula R$ ";
                                sb.append(tipo)
                                  .append(String.format("%.2f", entry.getKey().getValor()).replace('.', ','))
                                  .append(": ")
                                  .append(entry.getValue())
                                  .append("\n");
                            }
                        }
                        JOptionPane.showMessageDialog(this, sb.toString(), "Caixa", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Valor inválido ou maior que o disponível.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Valor inválido ou não há notas/moedas suficientes.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        resetPopupFontSize();
    }

    private void mostrarHistoricoVendas() {
        new view.TelaHistoricoVendas(this);
    }

    // Métodos utilitários para aumentar o tamanho dos popups
    private void setPopupFontSize(int size) {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, size));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, size));
        UIManager.put("OptionPane.font", new Font("Arial", Font.BOLD, size));
        UIManager.put("TextField.font", new Font("Arial", Font.BOLD, size));
        UIManager.put("ComboBox.font", new Font("Arial", Font.BOLD, size));
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, size));
        UIManager.put("Panel.font", new Font("Arial", Font.BOLD, size));
        UIManager.put("OptionPane.minimumSize", new Dimension(1200, 600));
    }

    private void resetPopupFontSize() {
        UIManager.put("OptionPane.messageFont", UIManager.getDefaults().getFont("OptionPane.messageFont"));
        UIManager.put("OptionPane.buttonFont", UIManager.getDefaults().getFont("OptionPane.buttonFont"));
        UIManager.put("OptionPane.font", UIManager.getDefaults().getFont("OptionPane.font"));
        UIManager.put("TextField.font", UIManager.getDefaults().getFont("TextField.font"));
        UIManager.put("ComboBox.font", UIManager.getDefaults().getFont("ComboBox.font"));
        UIManager.put("Label.font", UIManager.getDefaults().getFont("Label.font"));
        UIManager.put("Panel.font", UIManager.getDefaults().getFont("Panel.font"));
        UIManager.put("OptionPane.minimumSize", null);
    }

    // Teclado numérico customizado para touchscreen (agora em components)
    private String mostrarTecladoNumerico(String mensagem, String valorInicial) {
        return TecladoNumerico.mostrar(this, mensagem, valorInicial);
    }
}

