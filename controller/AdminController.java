
/**
 * {@code AdminController} centraliza as operações administrativas do sistema de autoatendimento,
 * fornecendo métodos estáticos para geração de relatórios, consulta e listagem de usuários,
 * produtos, estoque e caixa. Permite ao administrador obter informações gerenciais essenciais,
 * como vendas por período, produtos mais vendidos, horários de maior movimento, estoque baixo,
 * caixa detalhado, resumo geral e produtos vencidos ou próximos do vencimento.
 * <p>
 * Todos os métodos são utilitários e estáticos, facilitando o acesso a dados consolidados para
 * exibição em telas administrativas e geração de relatórios.
 * <p>
 *
 * <ul>
 *   <li>Listagem de usuários e produtos</li>
 *   <li>Consulta de estoque e valor em caixa</li>
 *   <li>Relatórios de vendas por período, produtos mais vendidos, horários de maior movimento</li>
 *   <li>Relatórios de estoque baixo, caixa detalhado, produtos vencidos/próximos do vencimento</li>
 *   <li>Resumo geral do sistema</li>
 * </ul>
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 *
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

    /**
     * Lista todos os usuários cadastrados no sistema.
     * @return Lista de usuários.
     */
    public static List<Usuario> listarUsuarios() {
        return UsuarioService.usuarios;
    }

    /**
     * Lista todos os produtos cadastrados no sistema.
     * @return Lista de produtos.
     */
    public static List<Produto> listarProdutos() {
        return ProdutoController.listarProdutos();
    }

    /**
     * Retorna o mapa de produtos e quantidades do estoque.
     * @return Mapa de id do produto para quantidade disponível.
     */
    public static Map<String, Integer> getEstoque() {
        return EstoqueController.getMapaProdutos();
    }

    /**
     * Retorna o valor total disponível no caixa.
     * @return Valor total em reais.
     */
    public static double getValorCaixa() {
        return CaixaService.getValorTotalCaixa();
    }

    /**
     * Retorna o número total de vendas realizadas.
     * @return Quantidade de vendas.
     */
    public static int getNumeroVendas() {
        VendaController.carregarVendas();
        return VendaService.obterVendas().size();
    }

    /**
     * Gera relatório de vendas realizadas em um período.
     * @param dataInicio Data/hora inicial (formato yyyy-MM-dd'T'HH:mm:ss)
     * @param dataFim Data/hora final (formato yyyy-MM-dd'T'HH:mm:ss)
     * @return Lista de vendas no período.
     */
    public static List<model.Venda> relatorioVendasPorPeriodo(String dataInicio, String dataFim) {
        VendaController.carregarVendas();
        return VendaService.buscarVendasPorPeriodo(dataInicio, dataFim);
    }

    /**
     * Gera relatório dos produtos mais vendidos (top N).
     * @param topN Quantidade de produtos a retornar.
     * @return Lista dos produtos mais vendidos.
     */
    public static List<Produto> relatorioProdutosMaisVendidos(int topN) {
        VendaController.carregarVendas();
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
        for (int i = 0; i < Math.min(topN, lista.size()); i++) {
            String id = lista.get(i).getKey();
            try {
                Produto p = ProdutoController.buscarProdutoPorId(id);
                resultado.add(p);
            } catch (exception.ProdutoNaoExisteException e) {
                // ignora produtos inexistentes
            }
        }
        return resultado;
    }

    /**
     * Gera relatório dos horários de maior movimento (top 3 faixas horárias com mais vendas).
     * @return String com as faixas horárias e quantidade de vendas.
     */
    public static String relatorioHorariosMovimento() {
        // Garante que as vendas estejam atualizadas antes de gerar o relatório
        VendaController.carregarVendas();
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
     * Gera relatório de produtos com estoque baixo (quantidade menor ou igual a 5).
     * @return Lista de produtos com estoque baixo.
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
     * Gera relatório detalhado do caixa (quantidade de cada denominação).
     * @return Mapa de denominação para quantidade.
     */
    public static java.util.Map<model.enums.Dinheiro, Integer> relatorioCaixaDetalhado() {
        return CaixaService.getCaixaDetalhado();
    }

    /**
     * Gera relatório geral resumido do sistema.
     * @return String com resumo das principais informações.
     */
    public static String relatorioResumoGeral() {
        VendaController.carregarVendas();
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
     * Gera relatório de produtos vencidos e próximos do vencimento (até 7 dias).
     * @return String com produtos vencidos ou próximos do vencimento.
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
