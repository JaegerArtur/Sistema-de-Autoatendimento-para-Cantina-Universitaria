package model.enums;

/**
 * Representa os métodos disponíveis para pagamento no sistema.
 * 
 * Os tipos incluem DINHEIRO e SALDO.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public enum FormaPagamento
{
    /**
     * Método de pagamento com dinheiro com cédulas ou moedas, disponível para todos os usuários.
     */
    DINHEIRO,
    
    /**
     * Método de pagamento com saldo do sistema, disponível apenas para membros com saldo suficiente.
     */
    SALDO,
    
    /**
     * Método de pagamento que combina dinheiro e saldo, disponível para membros com saldo suficiente.
     */
    DINHEIRO_E_SALDO;
}
