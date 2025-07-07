package service;

import model.Venda;
import model.enums.FormaPagamento;
import exception.SaldoInsuficienteException;
import exception.UsuarioNaoExisteException;
import service.UsuarioService;

/**
 * Classe de serviço responsável por operações relacionadas a pagamentos de vendas.
 * Permite simular o pagamento de uma venda, aceitando dinheiro e saldo para membros.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */

public class PagamentoService
{
    /**
     * Simula o pagamento apenas com dinheiro.
     * @param venda A venda a ser paga.
     * @param valorDinheiro Valor em dinheiro fornecido pelo cliente.
     * @return Troco a ser devolvido (0 se não houver troco).
     * @throws SaldoInsuficienteException se o valor for insuficiente.
     */
    public static double pagarComDinheiro(Venda venda, double valorDinheiro) throws SaldoInsuficienteException {
        double valorTotal = venda.getValorTotal();
        if (valorDinheiro < valorTotal) {
            throw new SaldoInsuficienteException();
        }
        return valorDinheiro - valorTotal;
    }

    /**
     * Simula o pagamento apenas com saldo.
     * @param venda A venda a ser paga.
     * @throws SaldoInsuficienteException se o saldo for insuficiente.
     */
    public static void pagarComSaldo(Venda venda) throws SaldoInsuficienteException,UsuarioNaoExisteException {
        double valorTotal = venda.getValorTotal();
        double saldo = UsuarioService.getSaldoUsuario(venda.getCpfUsuario());
        if (saldo < valorTotal) {
            throw new SaldoInsuficienteException();
        }
        UsuarioService.subtraiSaldoUsuario(venda.getCpfUsuario(), valorTotal);
    }

    /**
     * Simula o pagamento usando saldo e dinheiro juntos (saldo primeiro).
     * @param venda A venda a ser paga.
     * @param valorDinheiro Valor em dinheiro fornecido pelo cliente.
     * @return Troco a ser devolvido (0 se não houver troco).
     * @throws SaldoInsuficienteException se o saldo+dinheiro for insuficiente.
     */
    public static double pagarComSaldoEDinheiro(Venda venda, double valorDinheiro) throws SaldoInsuficienteException,UsuarioNaoExisteException {
        double valorTotal = venda.getValorTotal();
        double saldo = UsuarioService.getSaldoUsuario(venda.getCpfUsuario());
        if (saldo + valorDinheiro < valorTotal) {
            throw new SaldoInsuficienteException();
        }
        double restante = valorTotal - saldo;
        if (restante <= 0) {
            return valorDinheiro;
        } else {
            return valorDinheiro - restante;
        }
    }

    /**
     * Verifica se é possível pagar apenas com saldo.
     */
    public static boolean podePagarComSaldo(Venda venda) throws UsuarioNaoExisteException {
        double valorTotal = venda.getValorTotal();
        double saldo = UsuarioService.getSaldoUsuario(venda.getCpfUsuario());
        return saldo >= valorTotal;
    }

    /**
     * Verifica se é possível pagar apenas com dinheiro.
     */
    public static boolean podePagarComDinheiro(Venda venda, double valorDinheiro) {
        return valorDinheiro >= venda.getValorTotal();
    }

    /**
     * Verifica se é possível pagar com saldo e dinheiro juntos.
     */
    public static boolean podePagarComSaldoEDinheiro(Venda venda, double valorDinheiro) throws UsuarioNaoExisteException {
        double valorTotal = venda.getValorTotal();
        double saldo = UsuarioService.getSaldoUsuario(venda.getCpfUsuario());
        return saldo + valorDinheiro >= valorTotal;
    }
}
