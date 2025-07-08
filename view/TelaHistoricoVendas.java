package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import controller.VendaController;
import controller.ProdutoController;
import model.Venda;
import model.Produto;
import model.enums.FormaPagamento;

/**
 * Tela para exibir o histórico completo de vendas do sistema.
 * Permite visualizar todas as vendas realizadas com detalhes dos produtos,
 * valores, formas de pagamento e datas.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
public class TelaHistoricoVendas extends JDialog {
    private JFrame parent;
    private JTable tabelaVendas;
    private DefaultTableModel modeloTabela;

    /**
     * Constrói a tela de histórico de vendas.
     * @param parent Janela pai (TelaAdmin)
     */
    public TelaHistoricoVendas(JFrame parent) {
        super(parent, "Histórico de Vendas", true);
        this.parent = parent;
        
        initializeComponents();
        carregarDados();
        
        setSize(1400, 800);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Histórico de Vendas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Tabela de vendas
        String[] colunas = {"ID Venda", "Data/Hora", "Cliente/CPF", "Produtos", "Valor Total", "Forma Pagamento"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };
        
        tabelaVendas = new JTable(modeloTabela);
        tabelaVendas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaVendas.setRowHeight(60);
        tabelaVendas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabelaVendas.getTableHeader().setBackground(new Color(255, 230, 0));
        
        // Ajusta largura das colunas
        tabelaVendas.getColumnModel().getColumn(0).setPreferredWidth(120); // ID
        tabelaVendas.getColumnModel().getColumn(1).setPreferredWidth(150); // Data/Hora
        tabelaVendas.getColumnModel().getColumn(2).setPreferredWidth(120); // Cliente
        tabelaVendas.getColumnModel().getColumn(3).setPreferredWidth(400); // Produtos
        tabelaVendas.getColumnModel().getColumn(4).setPreferredWidth(100); // Valor
        tabelaVendas.getColumnModel().getColumn(5).setPreferredWidth(130); // Pagamento

        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Arial", Font.BOLD, 18));
        btnFechar.setBackground(new Color(255, 200, 200));
        btnFechar.setPreferredSize(new Dimension(120, 40));
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnFechar);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarDados() {
        // Limpa dados existentes
        modeloTabela.setRowCount(0);
        
        // Carrega produtos e vendas do controller
        ProdutoController.carregarProdutos();
        VendaController.carregarVendas();
        List<Venda> vendas = VendaController.obterVendas();
        
        if (vendas.isEmpty()) {
            // Adiciona linha indicando que não há vendas
            modeloTabela.addRow(new Object[]{
                "N/A", "N/A", "N/A", "Nenhuma venda registrada", "R$ 0,00", "N/A"
            });
            return;
        }

        // Ordena vendas por data (mais recentes primeiro)
        vendas.sort((v1, v2) -> v2.getDataHoraVenda().compareTo(v1.getDataHoraVenda()));

        // Preenche tabela com dados das vendas
        for (Venda venda : vendas) {
            try {
                String idVenda = venda.getId().substring(0, 8) + "..."; // Mostra só parte do UUID
                String dataHora = formatarDataHora(venda.getDataHoraVenda());
                String cliente = venda.getCpfUsuario() != null ? venda.getCpfUsuario() : "Visitante";
                String produtos = formatarProdutos(venda.getItens());
                String valorTotal = "R$ " + String.format("%.2f", venda.getValorTotal()).replace('.', ',');
                String formaPagamento = formatarFormaPagamento(venda.getFormaPagamento());

                modeloTabela.addRow(new Object[]{
                    idVenda, dataHora, cliente, produtos, valorTotal, formaPagamento
                });
            } catch (Exception e) {
                System.err.println("Erro ao processar venda: " + e.getMessage());
            }
        }
    }

    private String formatarDataHora(String dataHora) {
        try {
            // Converte de "dd/MM/yyyy HH:mm:ss" para formato mais legível
            if (dataHora.contains("/")) {
                return dataHora; // Já está no formato correto
            }
            // Se estiver em outro formato, tenta converter
            return dataHora.replace("T", " ");
        } catch (Exception e) {
            return dataHora;
        }
    }

    private String formatarProdutos(Map<String, Integer> itens) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        
        // Carrega produtos para garantir que estão disponíveis
        ProdutoController.carregarProdutos();
        
        for (Map.Entry<String, Integer> entry : itens.entrySet()) {
            if (count > 0) sb.append(", ");
            
            try {
                Produto produto = ProdutoController.buscarProdutoPorId(entry.getKey());
                sb.append(entry.getValue()).append("x ").append(produto.getNome());
            } catch (Exception e) {
                // Fallback: tenta buscar nome do produto ou usa "Produto Desconhecido"
                sb.append(entry.getValue()).append("x Produto Desconhecido");
            }
            count++;
            
            // Limita a 3 produtos por linha para não ficar muito longo
            if (count >= 3 && itens.size() > 3) {
                sb.append("...");
                break;
            }
        }
        return sb.toString();
    }

    private String formatarFormaPagamento(FormaPagamento forma) {
        switch (forma) {
            case DINHEIRO:
                return "Dinheiro";
            case SALDO:
                return "Saldo";
            case DINHEIRO_E_SALDO:
                return "Saldo + Dinheiro";
            default:
                return forma.toString();
        }
    }
} 