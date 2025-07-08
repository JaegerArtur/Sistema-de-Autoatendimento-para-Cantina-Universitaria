package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import controller.EstoqueController;
import controller.ProdutoController;
import model.Produto;
import exception.ProdutoNaoExisteException;
import view.components.TecladoNumerico;

/**
 * Tela para exibir detalhes individuais de um produto específico.
 * Mostra informações completas incluindo data de validade, quantidade em estoque,
 * status de vencimento e outras características do produto.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
public class TelaDetalhesProduto extends JDialog {
    private JDialog parent;
    private String idProduto;
    private Produto produto;
    private int quantidadeEstoque;

    /**
     * Constrói a tela de detalhes do produto.
     * @param parent Janela pai (TelaVisualizarEstoque)
     * @param idProduto ID do produto a ser exibido
     */
    public TelaDetalhesProduto(JDialog parent, String idProduto) throws Exception {
        super(parent, "Detalhes do Produto", true);
        this.parent = parent;
        this.idProduto = idProduto;
        
        carregarDadosProduto();
        initializeComponents();
        
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void carregarDadosProduto() throws Exception {
        produto = ProdutoController.buscarProdutoPorId(idProduto);
        quantidadeEstoque = EstoqueController.getMapaProdutos().getOrDefault(idProduto, 0);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Painel principal com informações
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Informações do Produto", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(titulo);

        // Imagem do produto (se existir)
        adicionarImagemProduto(painelPrincipal);

        // Informações básicas
        adicionarInformacaoBasica(painelPrincipal);

        // Informações de estoque
        adicionarInformacaoEstoque(painelPrincipal);

        // Informações de validade
        adicionarInformacaoValidade(painelPrincipal);

        JScrollPane scrollPane = new JScrollPane(painelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        JButton btnEditarPreco = new JButton("Editar Preço");
        btnEditarPreco.setFont(new Font("Arial", Font.BOLD, 16));
        btnEditarPreco.setBackground(new Color(70, 130, 180));
        btnEditarPreco.setForeground(Color.WHITE);
        btnEditarPreco.setPreferredSize(new Dimension(140, 40));
        btnEditarPreco.addActionListener(e -> abrirDialogEditarPreco());
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Arial", Font.BOLD, 16));
        btnFechar.setBackground(new Color(255, 100, 100));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setPreferredSize(new Dimension(120, 40));
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnEditarPreco);
        painelBotoes.add(btnFechar);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void adicionarImagemProduto(JPanel painel) {
        try {
            String caminhoImagem = "view/img/produtos/" + produto.getImg();
            java.io.File arquivoImagem = new java.io.File(caminhoImagem);
            
            if (arquivoImagem.exists()) {
                ImageIcon icone = new ImageIcon(caminhoImagem);
                Image img = icone.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                JLabel labelImagem = new JLabel(new ImageIcon(img));
                labelImagem.setAlignmentX(Component.CENTER_ALIGNMENT);
                labelImagem.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
                painel.add(labelImagem);
            }
        } catch (Exception e) {
            // Se não conseguir carregar a imagem, continua sem ela
        }
    }

    private void adicionarInformacaoBasica(JPanel painel) {
        // Painel para informações básicas
        JPanel painelBasico = criarPainelInformacao("Informações Básicas");

        adicionarCampoInformacao(painelBasico, "ID:", idProduto);
        adicionarCampoInformacao(painelBasico, "Nome:", produto.getNome());
        adicionarCampoInformacao(painelBasico, "Preço:", "R$ " + String.format("%.2f", produto.getPreco()).replace('.', ','));
        adicionarCampoInformacao(painelBasico, "Categoria:", produto.getCategoria().toString());

        painel.add(painelBasico);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void adicionarInformacaoEstoque(JPanel painel) {
        // Painel para informações de estoque
        JPanel painelEstoque = criarPainelInformacao("Estoque");

        adicionarCampoInformacao(painelEstoque, "Quantidade:", String.valueOf(quantidadeEstoque));
        
        String statusEstoque = determinarStatusEstoque();
        Color corStatus = determinarCorStatus(statusEstoque);
        adicionarCampoInformacao(painelEstoque, "Status:", statusEstoque, corStatus);

        painel.add(painelEstoque);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void adicionarInformacaoValidade(JPanel painel) {
        // Painel para informações de validade
        JPanel painelValidade = criarPainelInformacao("Validade");

        String dataValidade = produto.getDataValidade();
        adicionarCampoInformacao(painelValidade, "Data de Validade:", formatarData(dataValidade));

        // Calcula dias até vencimento
        String diasRestantes = calcularDiasRestantes(dataValidade);
        Color corDias = determinarCorDiasRestantes(diasRestantes);
        adicionarCampoInformacao(painelValidade, "Situação:", diasRestantes, corDias);

        painel.add(painelValidade);
    }

    private JPanel criarPainelInformacao(String titulo) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            titulo, 
            0, 
            0, 
            new Font("Arial", Font.BOLD, 16)
        ));
        painel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return painel;
    }

    private void adicionarCampoInformacao(JPanel painel, String label, String valor) {
        adicionarCampoInformacao(painel, label, valor, Color.BLACK);
    }

    private void adicionarCampoInformacao(JPanel painel, String label, String valor, Color cor) {
        JPanel linhaPainel = new JPanel(new BorderLayout());
        linhaPainel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel labelTexto = new JLabel(label);
        labelTexto.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel valorTexto = new JLabel(valor);
        valorTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        valorTexto.setForeground(cor);

        linhaPainel.add(labelTexto, BorderLayout.WEST);
        linhaPainel.add(valorTexto, BorderLayout.EAST);
        
        painel.add(linhaPainel);
    }

    private String determinarStatusEstoque() {
        if (quantidadeEstoque == 0) {
            return "SEM ESTOQUE";
        } else if (quantidadeEstoque <= 5) {
            return "ESTOQUE BAIXO";
        } else {
            return "ESTOQUE NORMAL";
        }
    }

    private Color determinarCorStatus(String status) {
        switch (status) {
            case "SEM ESTOQUE":
                return Color.RED;
            case "ESTOQUE BAIXO":
                return Color.ORANGE;
            case "ESTOQUE NORMAL":
                return new Color(0, 150, 0);
            default:
                return Color.BLACK;
        }
    }

    private String formatarData(String dataISO) {
        try {
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataISO, formatoEntrada);
            return data.format(formatoSaida);
        } catch (Exception e) {
            return dataISO; // Retorna formato original se houver erro
        }
    }

    private String calcularDiasRestantes(String dataValidade) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate validade = LocalDate.parse(dataValidade, formatter);
            LocalDate hoje = LocalDate.now();
            
            long diasRestantes = ChronoUnit.DAYS.between(hoje, validade);
            
            if (diasRestantes < 0) {
                return "PRODUTO VENCIDO (" + Math.abs(diasRestantes) + " dias atrás)";
            } else if (diasRestantes == 0) {
                return "VENCE HOJE";
            } else if (diasRestantes <= 7) {
                return "VENCE EM " + diasRestantes + " DIA(S)";
            } else {
                return "VÁLIDO (" + diasRestantes + " dias restantes)";
            }
        } catch (Exception e) {
            return "Data inválida";
        }
    }

    private Color determinarCorDiasRestantes(String situacao) {
        if (situacao.contains("VENCIDO")) {
            return Color.RED;
        } else if (situacao.contains("VENCE HOJE") || situacao.contains("VENCE EM")) {
            return Color.ORANGE;
        } else if (situacao.contains("VÁLIDO")) {
            return new Color(0, 150, 0);
        } else {
            return Color.BLACK;
        }
    }

    /**
     * Abre um dialog para editar o preço do produto usando o teclado numérico.
     */
    private void abrirDialogEditarPreco() {
        // Mostra informação do preço atual
        String precoAtual = String.format("%.2f", produto.getPreco()).replace('.', ',');
        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Produto: " + produto.getNome() + "\n" +
            "Preço atual: R$ " + precoAtual + "\n\n" +
            "Deseja alterar o preço?",
            "Editar Preço do Produto",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Obtém o JFrame parent (necessário para o TecladoNumerico)
        JFrame frameParent = getFrameParent();
        
        // Abre o teclado numérico
        String novoPrecoTexto = TecladoNumerico.mostrar(
            frameParent, 
            "Digite o novo preço para: " + produto.getNome(), 
            precoAtual
        );
        
        // Se o usuário cancelou ou não digitou nada
        if (novoPrecoTexto == null || novoPrecoTexto.trim().isEmpty()) {
            return;
        }
        
        try {
            // Converte o texto para double (substituindo vírgula por ponto)
            String textoPreco = novoPrecoTexto.trim().replace(',', '.');
            double novoPreco = Double.parseDouble(textoPreco);
            
            if (novoPreco < 0) {
                JOptionPane.showMessageDialog(this, 
                    "O preço não pode ser negativo!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (novoPreco == produto.getPreco()) {
                JOptionPane.showMessageDialog(this, 
                    "O preço informado é igual ao preço atual!", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Atualiza o preço do produto
            ProdutoController.atualizarPreco(idProduto, novoPreco);
            
            // Recarrega os dados do produto
            carregarDadosProduto();
            
            // Atualiza a tela
            atualizarInterface();
            
            JOptionPane.showMessageDialog(this, 
                "Preço atualizado com sucesso!\n" +
                "Preço anterior: R$ " + precoAtual + "\n" +
                "Novo preço: R$ " + String.format("%.2f", novoPreco).replace('.', ','), 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira um valor numérico válido!\n" +
                "Valor informado: " + novoPrecoTexto, 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (ProdutoNaoExisteException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro: Produto não encontrado!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar o preço: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Obtém o JFrame parent necessário para o TecladoNumerico.
     */
    private JFrame getFrameParent() {
        Window owner = getOwner();
        while (owner != null && !(owner instanceof JFrame)) {
            if (owner instanceof Dialog) {
                owner = ((Dialog) owner).getOwner();
            } else {
                break;
            }
        }
        return (JFrame) owner;
    }

    /**
     * Atualiza a interface após mudanças nos dados.
     */
    private void atualizarInterface() {
        // Remove todos os componentes
        getContentPane().removeAll();
        
        // Recria a interface
        initializeComponents();
        
        // Atualiza a tela
        revalidate();
        repaint();
    }
} 