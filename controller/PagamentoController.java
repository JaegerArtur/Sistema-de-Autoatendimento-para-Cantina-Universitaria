package controller;

import service.PagamentoService;
import service.CaixaService;
import model.enums.Dinheiro;
import model.Venda;
import exception.SaldoInsuficienteException;
import exception.UsuarioNaoExisteException;
import java.util.Map;

/**
 * Controller responsável por orquestrar operações de pagamento e caixa.
 * Atua como fachada entre a interface e os serviços.
 *
 * Boas práticas aplicadas:
 * - JavaDocs em métodos públicos
 * - Nomes claros e padronizados
 * - Delegação de responsabilidades
 */
public class PagamentoController {

    /**
     * Realiza pagamento apenas com dinheiro.
     * @param venda Venda a ser paga
     * @param valorDinheiro Valor entregue pelo cliente
     * @return Troco a ser devolvido
     * @throws SaldoInsuficienteException se valor for insuficiente
     */
    public static double realizarPagamentoDinheiro(Venda venda, double valorDinheiro) throws SaldoInsuficienteException {
        return PagamentoService.pagarComDinheiro(venda, valorDinheiro);
    }

    /**
     * Realiza pagamento apenas com saldo.
     * @param venda Venda a ser paga
     * @throws SaldoInsuficienteException se saldo for insuficiente
     * @throws UsuarioNaoExisteException se usuário não existir
     */
    public static void realizarPagamentoSaldo(Venda venda) throws SaldoInsuficienteException, UsuarioNaoExisteException {
        PagamentoService.pagarComSaldo(venda);
    }

    /**
     * Realiza pagamento com saldo e dinheiro.
     * @param venda Venda a ser paga
     * @param valorDinheiro Valor entregue pelo cliente
     * @return Troco a ser devolvido
     * @throws SaldoInsuficienteException se saldo+dinheiro for insuficiente
     * @throws UsuarioNaoExisteException se usuário não existir
     */
    public static double realizarPagamentoSaldoMaisDinheiro(Venda venda, double valorDinheiro) throws SaldoInsuficienteException, UsuarioNaoExisteException {
        return PagamentoService.pagarComSaldoEDinheiro(venda, valorDinheiro);
    }

    /**
     * Verifica se é possível dar o troco solicitado.
     * @param valorTroco Valor do troco
     * @return true se for possível, false caso contrário
     */
    public static boolean podeDarTroco(double valorTroco) {
        return CaixaService.podeDarTroco(valorTroco);
    }

    /**
     * Calcula o detalhamento do troco a ser devolvido.
     * @param troco Valor do troco
     * @param caixaDisponivel Mapa de notas/moedas disponíveis
     * @return Mapa com a quantidade de cada nota/moeda a ser devolvida
     */
    public static Map<Dinheiro, Integer> calcularTrocoDetalhado(double troco) {
        return CaixaService.calcularTrocoDetalhado(troco);
    }

    /**
     * Retorna o mapa de notas/moedas disponíveis no caixa.
     */
    public static Map<Dinheiro, Integer> getNotasMoedas() {
        return CaixaService.getNotasMoedas();
    }

    /**
     * Adiciona uma quantidade de uma nota/moeda ao caixa.
     */
    public static void adicionarNotaMoeda(Dinheiro tipo, int quantidade) {
        CaixaService.adicionarNotaMoeda(tipo, quantidade);
    }

    /**
     * Remove uma quantidade de uma nota/moeda do caixa.
     */
    public static void removerNotaMoeda(Dinheiro tipo, int quantidade) {
        CaixaService.removerNotaMoeda(tipo, quantidade);
    }

    /**
     * Salva o estado atual do caixa.
     */
    public static void salvarCaixa() {
        CaixaService.salvarCaixa();
    }
}

