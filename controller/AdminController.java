package controller;

import service.*;
import model.Usuario;
import model.Produto;
import model.Estoque;
import java.util.List;
public class AdminController {
    public List<Usuario> listarUsuarios() {
        return UsuarioService.usuarios;
    }

    public List<Produto> listarProdutos() {
        return ProdutoService.carregarProdutos();
    }

    public Estoque getEstoque() {
        return EstoqueService.carregarEstoque();
    }

    public double getValorCaixa() {
        return CaixaService.getValorTotalCaixa();
    }

    public int getNumeroVendas() {
        return VendaService.obterVendas().size();
    }

    /**
     * Relatório de vendas em um período (por data inicial e final).
     */
    /**
     * Relatório de vendas em um período (por data inicial e final).
     * Usa datas no formato yyyy-MM-dd'T'HH:mm:ss
     */
    public List<model.Venda> relatorioVendasPorPeriodo(String dataInicio, String dataFim) {
        return VendaService.buscarVendasPorPeriodo(dataInicio, dataFim);
    }

    /**
     * Relatório de produtos mais vendidos (top N).
     */
    // Método removido pois não existe implementação correspondente no service

    /**
     * Relatório de estoque baixo (produtos abaixo do mínimo).
     */
    // Método removido pois não existe implementação correspondente no service


    /**
     * Relatório de caixa detalhado (quantidade de cada denominação).
     */
    public java.util.Map<model.enums.Dinheiro, Integer> relatorioCaixaDetalhado() {
        return CaixaService.getCaixaDetalhado();
    }

    /**
     * Relatório geral resumido.
     */
    public String relatorioResumoGeral() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resumo Geral:\n");
        sb.append("Total de vendas: ").append(getNumeroVendas()).append("\n");
        sb.append("Valor total em caixa: R$ ").append(String.format("%.2f", getValorCaixa())).append("\n");
        sb.append("Produtos cadastrados: ").append(listarProdutos().size()).append("\n");
        sb.append("Usuários cadastrados: ").append(listarUsuarios().size()).append("\n");
        sb.append("Produtos com estoque baixo: ").append(relatorioEstoqueBaixo().size()).append("\n");
        return sb.toString();
    }
}
