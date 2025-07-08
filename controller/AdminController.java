
/**
 * Controller responsável pelas operações administrativas do sistema,
 * como relatórios, listagem de usuários/produtos, consulta de estoque e caixa.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package controller;

import service.*;
import model.Usuario;
import model.Produto;
import model.Estoque;
import java.util.List;
import java.util.*;

import controller.*;
public class AdminController {
    public static List<Usuario> listarUsuarios() {
        return UsuarioService.usuarios;
    }

    public static List<Produto> listarProdutos() {
        return ProdutoController.listarProdutos();
    }

    public static Map<String, Integer> getEstoque() {
        return EstoqueController.getMapaProdutos();
    }

    public static double getValorCaixa() {
        return CaixaService.getValorTotalCaixa();
    }

    public static int getNumeroVendas() {
        return VendaService.obterVendas().size();
    }

    /**
     * Relatório de vendas em um período (por data inicial e final).
     * Usa datas no formato yyyy-MM-dd'T'HH:mm:ss
     */
    public static List<model.Venda> relatorioVendasPorPeriodo(String dataInicio, String dataFim) {
        return VendaService.buscarVendasPorPeriodo(dataInicio, dataFim);
    }

    /**
     * Relatório de produtos mais vendidos (top N).
     */
    public static List<Produto> relatorioProdutosMaisVendidos(int topN) {
        List<model.Venda> vendas = VendaController.obterVendas();
        Map<String, Integer> contagem = new java.util.HashMap<>();
        for (model.Venda v : vendas) {
            for (Map.Entry<String, Integer> entry : VendaController.getProdutos(v).entrySet()) {
                contagem.put(entry.getKey(), contagem.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        List<Map.Entry<String, Integer>> lista = new java.util.ArrayList<>(contagem.entrySet());
        lista.sort((a, b) -> b.getValue() - a.getValue());
        List<Produto> resultado = new java.util.ArrayList<>();
        List<Produto> todos = listarProdutos();
        for (int i = 0; i < Math.min(topN, lista.size()); i++) {
            String nome = lista.get(i).getKey();
            for (Produto p : todos) {
                if (p.getNome().equals(nome)) {
                    resultado.add(p);
                    break;
                }
            }
        }
        return resultado;
    }

    /**
     * Relatório dos horários de maior movimento (top 3 faixas horárias com mais vendas).
     * Agrupa por hora (ex: 10:00-10:59).
     */
    public static String relatorioHorariosMovimento() {
        List<model.Venda> vendas = VendaController.obterVendas();
        java.util.Map<Integer, Integer> contagemPorHora = new java.util.HashMap<>();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        for (model.Venda v : vendas) {
            try {
                String dataHora = v.getDataHoraVenda();
                java.time.LocalDateTime ldt = java.time.LocalDateTime.parse(dataHora, formatter);
                int hora = ldt.getHour();
                contagemPorHora.put(hora, contagemPorHora.getOrDefault(hora, 0) + 1);
            } catch (Exception e) {
                // ignora vendas com data inválida
            }
        }
        if (contagemPorHora.isEmpty()) return "Nenhuma venda registrada.";
        java.util.List<java.util.Map.Entry<Integer, Integer>> lista = new java.util.ArrayList<>(contagemPorHora.entrySet());
        lista.sort((a, b) -> b.getValue() - a.getValue());
        StringBuilder sb = new StringBuilder();
        int top = Math.min(3, lista.size());
        for (int i = 0; i < top; i++) {
            int hora = lista.get(i).getKey();
            int qtd = lista.get(i).getValue();
            sb.append(String.format("%02d:00-%02d:59", hora, hora)).append(" | Vendas: ").append(qtd).append("\n");
        }
        return sb.toString();
    }

    /**
     * Relatório de estoque baixo (produtos abaixo do mínimo).
     * Considera quantidade <= 5 como baixo.
     */
    public static List<Produto> relatorioEstoqueBaixo() {
        Map<String, Integer> mapaEstoque = EstoqueController.getMapaProdutos();
        List<Produto> baixo = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mapaEstoque.entrySet()) {
            if (entry.getValue() <= 5) {
                try {
                    Produto p = ProdutoController.buscarProdutoPorId(entry.getKey());
                    baixo.add(p);
                } catch (exception.ProdutoNaoExisteException e) {
                    // ignora produtos inexistentes
                }
            }
        }
        return baixo;
    }

    /**
     * Relatório de caixa detalhado (quantidade de cada denominação).
     */
    public static java.util.Map<model.enums.Dinheiro, Integer> relatorioCaixaDetalhado() {
        return CaixaService.getCaixaDetalhado();
    }

    /**
     * Relatório geral resumido.
     */
    public static String relatorioResumoGeral() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resumo Geral:\n");
        sb.append("Total de vendas: ").append(getNumeroVendas()).append("\n");
        sb.append("Valor total em caixa: R$ ").append(String.format("%.2f", getValorCaixa())).append("\n");
        sb.append("Produtos cadastrados: ").append(listarProdutos().size()).append("\n");
        sb.append("Usuários cadastrados: ").append(listarUsuarios().size()).append("\n");
        sb.append("Produtos com estoque baixo: ").append(relatorioEstoqueBaixo().size()).append("\n");
        sb.append("Top 3 produtos mais vendidos: ");
        List<Produto> top = relatorioProdutosMaisVendidos(3);
        for (Produto p : top) {
            sb.append(p.getNome()).append(", ");
        }
        if (!top.isEmpty()) sb.setLength(sb.length() - 2); // remove última vírgula
        sb.append("\n");
        return sb.toString();
    }
    /**
     * Relatório de produtos vencidos e próximos do vencimento (até 7 dias).
     */
    public static String relatorioProdutosVencidos() {
        List<Produto> produtos = listarProdutos();
        StringBuilder sb = new StringBuilder();
        java.time.LocalDate hoje = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean algum = false;
        for (Produto p : produtos) {
            try {
                java.time.LocalDate validade = java.time.LocalDate.parse(p.getDataValidade(), formatter);
                if (validade.isBefore(hoje)) {
                    sb.append("- ").append(p.getNome()).append(" | VENCIDO em: ").append(p.getDataValidade()).append("\n");
                    algum = true;
                } else if (!validade.isAfter(hoje.plusDays(7))) {
                    sb.append("- ").append(p.getNome()).append(" | Vence em: ").append(p.getDataValidade()).append("\n");
                    algum = true;
                }
            } catch (Exception e) {
                // ignora produtos com data inválida
            }
        }
        if (!algum) sb.append("Nenhum produto vencido ou próximo do vencimento.");
        return sb.toString();
    }
}
