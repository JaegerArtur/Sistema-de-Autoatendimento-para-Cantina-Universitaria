package controller;

import model.Venda;
import service.*;
import service.CaixaService;
import exception.SaldoInsuficienteException;
import java.util.*;
import model.enums.FormaPagamento;

/**
 * Classe responsável por orquestrar o processo de venda. <br>
 * Valida a venda, atualiza o estoque, desconta saldo do usuário, atualiza o caixa e registra a venda.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public class VendaController
{
    /**
     * Orquestra o processo completo de venda.<p>
     * Valida, desconta estoque, saldo, atualiza caixa e registra a venda.<br>
     * Lança exceções específicas em caso de erro.
     * @param venda A venda a ser processada
     * @param valorDinheiro Valor em dinheiro entregue pelo cliente (se aplicável)
     * @return true se a venda foi realizada com sucesso, false caso contrário
     */
    public static boolean processarVenda(Venda venda, double valorDinheiro) {
        Map<String, Integer> estoqueAnterior = salvarEstoqueAnterior(venda);
        double saldoAnterior = 0;
        boolean usouSaldo = false;
        boolean usouDinheiro = false;
        try {
            if (venda.getFormaPagamento() == FormaPagamento.SALDO) {
                saldoAnterior = UsuarioService.consultarSaldoMembro(venda.getCpfUsuario());
                usouSaldo = true;
            }

            atualizarEstoqueParaVenda(venda);

            if (venda.getFormaPagamento().name().contains("SALDO")) {
                UsuarioService.alterarSaldoMembro(venda.getCpfUsuario(), -venda.getValorTotal());
            }

            switch (venda.getFormaPagamento()) {
                case DINHEIRO:
                    usouDinheiro = true;
                    double trocoDinheiro = PagamentoService.pagarComDinheiro(venda, valorDinheiro);
                    CaixaService.adicionarCaixa(venda.getValorTotal() - trocoDinheiro);
                    break;
                case SALDO:
                    PagamentoService.pagarComSaldo(venda);
                    break;
                case DINHEIRO_E_SALDO:
                    usouDinheiro = true;
                    double trocoSaldoDinheiro = PagamentoService.pagarComSaldoEDinheiro(venda, valorDinheiro);
                    CaixaService.adicionarCaixa(venda.getValorTotal() - trocoSaldoDinheiro);
                    break;
            }

            VendaService.adicionarVenda(venda);
            return true;

        } catch (SaldoInsuficienteException e) {
            System.err.println("Erro de negócio: " + e.getMessage());
            rollbackVenda(venda, estoqueAnterior, saldoAnterior, usouSaldo, usouDinheiro);
            return false;
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            rollbackVenda(venda, estoqueAnterior, saldoAnterior, usouSaldo, usouDinheiro);
            return false;
        }
    }

    private static Map<String, Integer> salvarEstoqueAnterior(Venda venda) {
        Map<String, Integer> estoqueAnterior = new HashMap<>();
        venda.getItens().forEach((idProduto, quantidade) -> {
            estoqueAnterior.put(idProduto, EstoqueService.getQuantidade(idProduto));
        });
        return estoqueAnterior;
    }

    private static void atualizarEstoqueParaVenda(Venda venda) {
        venda.getItens().forEach((idProduto, quantidade) -> {
            EstoqueService.atualizarQuantidade(idProduto, EstoqueService.getQuantidade(idProduto) - quantidade);
        });
    }

    public static List<Venda> obterVendas() {
        return VendaService.obterVendas();
    }

    /**
     * Retorna o mapa de produtos (id -> quantidade) de uma venda.
     * @param venda Venda a ser consultada
     * @return Mapa de produtos e quantidades
     */
    public static Map<String, Integer> getProdutos(Venda venda) {
        if (venda == null) return Collections.emptyMap();
        return venda.getItens();
    }
    
    private static void rollbackVenda(Venda venda, Map<String, Integer> estoqueAnterior, double saldoAnterior, boolean usouSaldo, boolean usouDinheiro) {
        estoqueAnterior.forEach((idProduto, quantidadeAnterior) -> {
            EstoqueService.atualizarQuantidade(idProduto, quantidadeAnterior);
        });
        if (usouSaldo) {
            try {
                double saldoAtual = UsuarioService.consultarSaldoMembro(venda.getCpfUsuario());
                double diff = saldoAnterior - saldoAtual;
                if (diff > 0) {
                    UsuarioService.alterarSaldoMembro(venda.getCpfUsuario(), diff);
                }
            } catch (Exception ex) {
                System.err.println("Erro ao restaurar saldo: " + ex.getMessage());
            }
        }

        if (usouDinheiro) {
            System.out.println("Devolva o dinheiro ao cliente.");
        }
    }
}
