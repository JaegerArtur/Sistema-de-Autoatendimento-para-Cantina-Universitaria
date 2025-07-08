
/**
 * Exceção lançada quando o saldo do usuário é insuficiente para realizar uma operação.
 * 
 *
 * @author Grupo Artur, João e Miguel
 * @version 1.0

 */
package exception;

public class SaldoInsuficienteException extends Exception
{
    /**
     * Constrói uma nova exceção com uma mensagem padrão informativa.
     * 
     */
    public SaldoInsuficienteException () {
        super("A operacao nao pode ser feita, saldo insuficiente.");
    }
}