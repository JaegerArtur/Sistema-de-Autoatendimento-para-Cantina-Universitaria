package service;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Venda;
import util.JsonManager;
import exception.*;
import service.ProdutoService;
import service.EstoqueService;
import service.UsuarioService;
import model.enums.FormaPagamento;

public class VendaService
{
    private static final String CAMINHO_VENDAS = "data/vendas.json";

    public static List<Venda> vendas = carregarVendas();

    /**
     * Carrega as vendas do arquivo JSON.
     * 
     */
    public static List<Venda> carregarVendas() {
        return JsonManager.carregarVendas(CAMINHO_VENDAS);
    }

    /**
     * Salva as vendas no arquivo JSON.
     */
    public static void salvarVendas() {
        JsonManager.salvarVendas(vendas, CAMINHO_VENDAS);
    }

    /**
     * Realiza uma venda no sistema.
     * 
     * @param venda A venda a ser realizada.
     * @return true se a venda foi realizada com sucesso, false caso contrário.
     */
    public static boolean realizarVenda(Venda venda) {
        try {
            adicionarVenda(venda);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao realizar a venda: " + e.getMessage());
            return false;
        }
    }

    public static void validarVenda(Venda venda) throws exception.ProdutoNaoExisteException, SaldoInsuficienteException, UsuarioNaoExisteException, ProdutoIndisponivelException {
        for (Map.Entry<String, Integer> item : venda.getItens().entrySet()) {
            String idProduto = item.getKey();
            int quantidade = item.getValue();
            if (!EstoqueService.verificarEstoqueSuficiente(idProduto, quantidade)) {
                throw new ProdutoIndisponivelException((ProdutoService.getProdutoId(idProduto)).getNome(),EstoqueService.getQuantidade(idProduto));
            }
        }
        if (venda.getFormaPagamento() == FormaPagamento.SALDO) {
            double saldo = UsuarioService.consultarSaldoMembro(venda.getCpfUsuario());
            if (saldo < venda.getValorTotal()) {
                throw new SaldoInsuficienteException();
            }
        }
    }

    /**
     * Adiciona uma venda ao sistema.
     * 
     * @param venda A venda a ser adicionada.
     */
    public static void adicionarVenda(Venda venda) {
        vendas.add(venda);
        salvarVendas();
    }

    /**
     * Obtém todas as vendas registradas no sistema.
     * 
     * @return Lista de vendas registradas.
     */
    public static List<Venda> obterVendas() {
        return new ArrayList<>(vendas);
    }

    /**
     * Busca uma venda pelo ID.
     * @param idVenda ID da venda
     * @return Venda encontrada ou null
     */
    public static Venda buscarVendaPorId(String idVenda) {
        return vendas.stream()
            .filter(v -> v.getId().equals(idVenda))
            .findFirst()
            .orElse(null);
    }

    /**
     * Busca todas as vendas de um usuário pelo CPF.
     * @param cpfUsuario CPF do usuário
     * @return Lista de vendas do usuário
     */
    public static List<Venda> buscarVendasPorUsuario(String cpfUsuario) {
        return vendas.stream()
            .filter(v -> v.getCpfUsuario() != null && v.getCpfUsuario().equals(cpfUsuario))
            .collect(Collectors.toList());
    }

    /**
     * Busca vendas realizadas em um período (datas no formato dd/MM/yyyy HH:mm:ss).
     * @param dataInicio Data/hora inicial
     * @param dataFim Data/hora final
     * @return Lista de vendas no período
     */
    public static List<Venda> buscarVendasPorPeriodo(String dataInicio, String dataFim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime inicio = LocalDateTime.parse(dataInicio, formatter);
        LocalDateTime fim = LocalDateTime.parse(dataFim, formatter);
        return vendas.stream()
            .filter(v -> {
                try {
                    LocalDateTime dataVenda = LocalDateTime.parse(v.getDataHoraVenda(), formatter);
                    return (dataVenda.isEqual(inicio) || dataVenda.isAfter(inicio)) &&
                           (dataVenda.isEqual(fim) || dataVenda.isBefore(fim));
                } catch (Exception e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
    }
}
