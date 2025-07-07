package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import controller.handlers.*;
import controller.EstoqueController;
import controller.ProdutoController;
import model.Usuario;
import model.Produto;

public class TelaAtendimento extends JFrame {
    private LinkedHashMap<String, Integer> carrinho;
    private Map<String, Produto> produtoMap;
    private Usuario usuario;

    public TelaAtendimento(LinkedHashMap<String, Integer> carrinho, Usuario usuario) {
        setTitle("Autoatendimento - Cantina Universitária");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1170, 660);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        if (carrinho == null) this.carrinho = new LinkedHashMap<>();
        else this.carrinho = carrinho;
        this.usuario = usuario;

        // Sempre reconstrói o produtoMap a partir do controller
        this.produtoMap = new HashMap<>();
        java.util.List<model.Produto> produtos = ProdutoController.listarProdutos();
        for (Produto p : produtos) {
            this.produtoMap.put(p.getId(), p);
        }

        // Topo: Logo, saudação e botão de sair
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(255, 230, 0));
        JLabel logo = new JLabel("CANTINA");
        logo.setFont(new Font("Arial", Font.BOLD, 48));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        topo.add(logo, BorderLayout.CENTER);

        // Botão de sair
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 24));
        btnSair.setBackground(new Color(220, 53, 69));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnSair.addActionListener(new SairHandler(this));
        topo.add(btnSair, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        // Centro: Produtos reais do estoque
        java.util.Map<String, Integer> estoque = EstoqueController.getMapaProdutos();
        int qtdProdutos = 0;
        for (String id : estoque.keySet()) {
            if (estoque.get(id) > 0) qtdProdutos++;
        }
        int linhas = (int) Math.ceil(qtdProdutos / 3.0);
        JPanel produtosPanel = new JPanel(new GridLayout(linhas, 3, 15, 15));
        produtosPanel.setBackground(new Color(245, 245, 245));

        JPanel carrinhoPanel = new JPanel(new BorderLayout());
        // Referência ao JTextArea do carrinho para atualizar sempre que adicionar produto
        JTextArea itensCarrinho = new JTextArea();
        itensCarrinho.setFont(new Font("Arial", Font.PLAIN, 24));
        itensCarrinho.setEditable(false);
        itensCarrinho.setMargin(new Insets(10, 10, 10, 10));

        // Label do total
        JLabel totalLabel = new JLabel("Total: R$ 0,00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 26));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton removerItem = new JButton("Remover Último Item");
        removerItem.setFont(new Font("Arial", Font.BOLD, 20));
        removerItem.setBackground(new Color(220, 53, 69));
        removerItem.setForeground(Color.WHITE);
        removerItem.setFocusPainted(false);
        removerItem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        removerItem.addActionListener(new RemoverItemHandler(this.carrinho, produtoMap, itensCarrinho));
        for (String id : estoque.keySet()) {
            if (estoque.get(id) > 0) {
                Produto prod = produtoMap.get(id);
                if (prod != null) {
                    JButton btn = new JButton();
                    btn.setLayout(new BorderLayout());
                    // Imagem
                    String imgPath = "view/img/produtos/" + prod.getImg();
                    java.io.File imgFile = new java.io.File(imgPath);
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imgPath);
                        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        JLabel imgLabel = new JLabel(new ImageIcon(img));
                        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        btn.add(imgLabel, BorderLayout.CENTER);
                    }

                    String nome = prod.getNome();
                    String preco = String.format("R$ %.2f", prod.getPreco());
                    JLabel nomeLabel = new JLabel("<html>" + nome + "<br><span style='font-size:16px;color:#009688;font-weight:normal;'>" + preco + "</span></html>", SwingConstants.CENTER);
                    nomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
                    btn.add(nomeLabel, BorderLayout.SOUTH);
                    btn.setBackground(new Color(255, 255, 255));
                    btn.setFocusPainted(false);
                    btn.addActionListener(new AdicionarProdutoHandler(prod, carrinho, produtoMap, itensCarrinho));
                    produtosPanel.add(btn);
                }
            }
        }
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centro.add(produtosPanel, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // Lateral direita: Carrinho
        carrinhoPanel.setPreferredSize(new Dimension(400, 0));
        carrinhoPanel.setBackground(new Color(255, 255, 255));
        JLabel carrinhoLabel = new JLabel("Carrinho");
        carrinhoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        carrinhoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        carrinhoPanel.add(carrinhoLabel, BorderLayout.NORTH);
        JPanel carrinhoConteudo = new JPanel(new BorderLayout());
        carrinhoConteudo.setOpaque(false);
        carrinhoConteudo.add(itensCarrinho, BorderLayout.CENTER);
        carrinhoConteudo.add(totalLabel, BorderLayout.NORTH);
        carrinhoConteudo.add(removerItem, BorderLayout.SOUTH);
        carrinhoPanel.add(carrinhoConteudo, BorderLayout.CENTER);
        JButton finalizar = new JButton("Finalizar Pedido");
        finalizar.setFont(new Font("Arial", Font.BOLD, 32));
        finalizar.setBackground(new Color(0, 200, 83));
        finalizar.setForeground(Color.WHITE);
        carrinhoPanel.add(finalizar, BorderLayout.SOUTH);
        add(carrinhoPanel, BorderLayout.EAST);

        // Ação do botão finalizar: abrir TelaPagamento com resumo do carrinho
        finalizar.addActionListener(new FinalizarPedidoHandler(this.carrinho, produtoMap, this.usuario, this));

        // Atualiza o carrinho ao abrir a tela
        atualizarItensCarrinho(itensCarrinho, totalLabel, this.carrinho, this.produtoMap);

        // Touch-friendly
        UIManager.put("Button.focus", new Color(0,0,0,0));
        setVisible(true);
    }
    private void atualizarItensCarrinho(JTextArea itensCarrinho, JLabel totalLabel, LinkedHashMap<String, Integer> carrinho, Map<String, Produto> produtoMap) {
        if (carrinho == null || carrinho.isEmpty()) {
            itensCarrinho.setText("Seu carrinho está vazio");
            totalLabel.setText("Total: R$ 0,00");
            return;
        }
        StringBuilder sb = new StringBuilder();
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : carrinho.entrySet()) {
            Produto prod = produtoMap.get(entry.getKey());
            if (prod != null) {
                double subtotal = prod.getPreco() * entry.getValue();
                total += subtotal;
                sb.append(prod.getNome())
                  .append(" x")
                  .append(entry.getValue())
                  .append(" - R$ ")
                  .append(String.format("%.2f", subtotal))
                  .append("\n");
            }
        }
        itensCarrinho.setText(sb.toString());
        totalLabel.setText("Total: R$ " + String.format("%.2f", total));
    }
}
