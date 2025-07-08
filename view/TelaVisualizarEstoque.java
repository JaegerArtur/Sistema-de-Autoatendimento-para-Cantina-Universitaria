package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import controller.EstoqueController;
import controller.ProdutoController;
import model.Produto;

/**
 * Tela para visualizar o estoque completo do sistema em formato de tabela.
 * Permite visualizar todos os produtos em estoque com suas quantidades,
 * e ao clicar em um produto, exibe detalhes individuais incluindo data de validade.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
public class TelaVisualizarEstoque extends JDialog {
    private JFrame parent;
    private JTable tabelaEstoque;
    private DefaultTableModel modeloTabela;

    /**
     * Constrói a tela de visualização do estoque.
     * @param parent Janela pai (TelaAdmin)
     */
    public TelaVisualizarEstoque(JFrame parent) {
        super(parent, "Visualizar Estoque", true);
        this.parent = parent;
        
        initializeComponents();
        carregarDados();
        
        setSize(1200, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Estoque de Produtos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Tabela de estoque
        String[] colunas = {"ID Produto", "Nome do Produto", "Quantidade", "Preço Unitário", "Categoria", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };
        
        tabelaEstoque = new JTable(modeloTabela);
        tabelaEstoque.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaEstoque.setRowHeight(50);
        tabelaEstoque.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabelaEstoque.getTableHeader().setBackground(new Color(255, 230, 0));
        
        // Ajusta largura das colunas
        tabelaEstoque.getColumnModel().getColumn(0).setPreferredWidth(120); // ID
        tabelaEstoque.getColumnModel().getColumn(1).setPreferredWidth(250); // Nome
        tabelaEstoque.getColumnModel().getColumn(2).setPreferredWidth(100); // Quantidade
        tabelaEstoque.getColumnModel().getColumn(3).setPreferredWidth(120); // Preço
        tabelaEstoque.getColumnModel().getColumn(4).setPreferredWidth(100); // Categoria
        tabelaEstoque.getColumnModel().getColumn(5).setPreferredWidth(100); // Status

        // Adiciona listener para clique duplo na tabela
        tabelaEstoque.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int linhaSelecionada = tabelaEstoque.getSelectedRow();
                    if (linhaSelecionada != -1) {
                        String idProduto = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
                        abrirDetalheProduto(idProduto);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões e informações
        JPanel painelInferior = new JPanel(new BorderLayout());
        
        // Instrução para o usuário
        JLabel instrucao = new JLabel("Duplo clique em um produto para ver detalhes e data de validade", SwingConstants.CENTER);
        instrucao.setFont(new Font("Arial", Font.ITALIC, 14));
        instrucao.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painelInferior.add(instrucao, BorderLayout.NORTH);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAtualizar.setBackground(new Color(0, 150, 255));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setPreferredSize(new Dimension(120, 40));
        btnAtualizar.addActionListener(e -> {
            carregarDados();
            JOptionPane.showMessageDialog(this, "Dados atualizados!", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Arial", Font.BOLD, 18));
        btnFechar.setBackground(new Color(255, 100, 100));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setPreferredSize(new Dimension(120, 40));
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnFechar);
        painelInferior.add(painelBotoes, BorderLayout.CENTER);
        
        add(painelInferior, BorderLayout.SOUTH);
    }

    private void carregarDados() {
        // Limpa dados existentes
        modeloTabela.setRowCount(0);
        
        // Carrega produtos e estoque
        ProdutoController.carregarProdutos();
        EstoqueController.carregarEstoque();
        Map<String, Integer> estoque = EstoqueController.getMapaProdutos();
        
        if (estoque.isEmpty()) {
            // Adiciona linha indicando que não há itens no estoque
            modeloTabela.addRow(new Object[]{
                "N/A", "Nenhum produto no estoque", "0", "R$ 0,00", "N/A", "N/A"
            });
            return;
        }

        // Preenche tabela com dados do estoque
        for (Map.Entry<String, Integer> entry : estoque.entrySet()) {
            try {
                String idProduto = entry.getKey();
                int quantidade = entry.getValue();
                
                Produto produto = ProdutoController.buscarProdutoPorId(idProduto);
                
                String idExibido = idProduto.substring(0, 8) + "..."; // Mostra só parte do UUID
                String nome = produto.getNome();
                String quantidadeStr = String.valueOf(quantidade);
                String preco = "R$ " + String.format("%.2f", produto.getPreco()).replace('.', ',');
                String categoria = produto.getCategoria().toString();
                String status = determinarStatus(quantidade, produto);

                modeloTabela.addRow(new Object[]{
                    idProduto, nome, quantidadeStr, preco, categoria, status
                });
            } catch (Exception e) {
                System.err.println("Erro ao processar produto: " + e.getMessage());
            }
        }
    }

    private String determinarStatus(int quantidade, Produto produto) {
        if (quantidade == 0) {
            return "Sem Estoque";
        } else if (quantidade <= 5) {
            return "Estoque Baixo";
        } else if (produto.isVencido()) {
            return "Vencido";
        } else {
            return "Normal";
        }
    }

    private void abrirDetalheProduto(String idProduto) {
        try {
            new TelaDetalhesProduto(this, idProduto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao abrir detalhes do produto: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 