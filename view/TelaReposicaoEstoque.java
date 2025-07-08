package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import controller.EstoqueController;
import controller.ProdutoController;
import service.EstoqueService;
import model.Produto;

/**
 * Tela para reposição de estoque com seleção de data de validade.
 * Permite ao administrador adicionar quantidade e atualizar a validade dos produtos.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
public class TelaReposicaoEstoque extends JDialog {
    private JFrame parent;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JTextField campoQuantidade;
    private JTextField campoDataValidade;
    private String produtoSelecionadoId;

    /**
     * Constrói a tela de reposição de estoque.
     * @param parent Janela pai (TelaAdmin)
     */
    public TelaReposicaoEstoque(JFrame parent) {
        super(parent, "Reposição de Estoque", true);
        this.parent = parent;
        
        initializeComponents();
        carregarDados();
        
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Reposição de Estoque", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Painel central dividido
        JPanel painelCentral = new JPanel(new BorderLayout());

        // Tabela de produtos
        String[] colunas = {"ID", "Nome do Produto", "Quantidade Atual", "Preço", "Validade Atual"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaProdutos.setRowHeight(40);
        tabelaProdutos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabelaProdutos.getTableHeader().setBackground(new Color(255, 230, 0));
        
        // Seleção única
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Listener para seleção
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linhaSelecionada = tabelaProdutos.getSelectedRow();
                if (linhaSelecionada != -1) {
                    produtoSelecionadoId = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
                    // Preenche com a data de validade atual do produto em formato ISO
                    try {
                        Produto produto = ProdutoController.buscarProdutoPorId(produtoSelecionadoId);
                        campoDataValidade.setText(produto.getDataValidade());
                    } catch (Exception ex) {
                        campoDataValidade.setText(LocalDate.now().plusMonths(1).toString());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        painelCentral.add(scrollPane, BorderLayout.CENTER);

        // Painel de reposição
        JPanel painelReposicao = criarPainelReposicao();
        painelCentral.add(painelReposicao, BorderLayout.SOUTH);

        add(painelCentral, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 16));
        btnAtualizar.setBackground(new Color(0, 150, 255));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setPreferredSize(new Dimension(150, 40));
        btnAtualizar.addActionListener(e -> {
            carregarDados();
            JOptionPane.showMessageDialog(this, "Lista atualizada!", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Arial", Font.BOLD, 16));
        btnFechar.setBackground(new Color(255, 100, 100));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setPreferredSize(new Dimension(120, 40));
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnFechar);
        
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelReposicao() {
        JPanel painel = new JPanel();
        painel.setBorder(BorderFactory.createTitledBorder("Reposição de Estoque"));
        painel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Instrução
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel instrucao = new JLabel("Selecione um produto na tabela acima para repor estoque:");
        instrucao.setFont(new Font("Arial", Font.BOLD, 14));
        painel.add(instrucao, gbc);

        // Quantidade a adicionar
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblQuantidade = new JLabel("Quantidade a adicionar:");
        lblQuantidade.setFont(new Font("Arial", Font.PLAIN, 14));
        painel.add(lblQuantidade, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        campoQuantidade = new JTextField(10);
        campoQuantidade.setFont(new Font("Arial", Font.PLAIN, 14));
        painel.add(campoQuantidade, gbc);

        // Data de validade
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDataValidade = new JLabel("Nova data de validade (yyyy-MM-dd):");
        lblDataValidade.setFont(new Font("Arial", Font.PLAIN, 14));
        painel.add(lblDataValidade, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        campoDataValidade = new JTextField(LocalDate.now().plusMonths(1).toString(), 10);
        campoDataValidade.setFont(new Font("Arial", Font.PLAIN, 14));
        painel.add(campoDataValidade, gbc);

        // Botão de repor
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton btnRepor = new JButton("Repor Estoque");
        btnRepor.setFont(new Font("Arial", Font.BOLD, 16));
        btnRepor.setBackground(new Color(40, 167, 69));
        btnRepor.setForeground(Color.WHITE);
        btnRepor.setPreferredSize(new Dimension(200, 50));
        btnRepor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reporEstoque();
            }
        });
        painel.add(btnRepor, gbc);

        return painel;
    }

    private void carregarDados() {
        // Limpa dados existentes
        modeloTabela.setRowCount(0);
        
        // Carrega produtos e estoque
        ProdutoController.carregarProdutos();
        EstoqueController.carregarEstoque();
        Map<String, Integer> estoque = EstoqueController.getMapaProdutos();
        
        // Preenche tabela com todos os produtos
        for (Produto produto : ProdutoController.listarProdutos()) {
            try {
                String idProduto = produto.getId();
                int quantidade = estoque.getOrDefault(idProduto, 0);
                
                String idExibido = idProduto.substring(0, 8) + "...";
                String nome = produto.getNome();
                String quantidadeStr = String.valueOf(quantidade);
                String preco = "R$ " + String.format("%.2f", produto.getPreco()).replace('.', ',');
                String validade = formatarData(produto.getDataValidade());

                modeloTabela.addRow(new Object[]{
                    idProduto, nome, quantidadeStr, preco, validade
                });
            } catch (Exception e) {
                System.err.println("Erro ao processar produto: " + e.getMessage());
            }
        }
    }

    private String formatarData(String dataISO) {
        try {
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataISO, formatoEntrada);
            return data.format(formatoSaida);
        } catch (Exception e) {
            return dataISO;
        }
    }

    private void reporEstoque() {
        if (produtoSelecionadoId == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto na tabela!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Valida quantidade
            String quantidadeTexto = campoQuantidade.getText().trim();
            if (quantidadeTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Informe a quantidade a ser adicionada!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantidade = Integer.parseInt(quantidadeTexto);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "A quantidade deve ser maior que zero!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Valida data de validade
            String novaDataValidade = campoDataValidade.getText().trim();
            if (novaDataValidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Informe a nova data de validade!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Valida formato da data
            try {
                LocalDate.parse(novaDataValidade, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Data inválida! Use o formato yyyy-MM-dd", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Repõe o estoque
            EstoqueService.reporEstoque(produtoSelecionadoId, quantidade);
            
            // Atualiza a data de validade do produto
            ProdutoController.atualizarDataValidade(produtoSelecionadoId, novaDataValidade);

            // Mostra confirmação
            Produto produto = ProdutoController.buscarProdutoPorId(produtoSelecionadoId);
            JOptionPane.showMessageDialog(this, 
                String.format("Estoque atualizado com sucesso!\n\nProduto: %s\nQuantidade adicionada: %d\nNova validade: %s", 
                    produto.getNome(), quantidade, formatarData(novaDataValidade)), 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);

            // Limpa os campos
            campoQuantidade.setText("");
            campoDataValidade.setText(LocalDate.now().plusMonths(1).toString());
            
            // Atualiza a tabela
            carregarDados();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Quantidade inválida! Digite apenas números.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao repor estoque: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 