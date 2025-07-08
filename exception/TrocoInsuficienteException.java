
/**
 * Exceção lançada quando não há troco suficiente no caixa para finalizar uma venda.
 * 
 *
 * @author Grupo Artur, João e Miguel
 * @version 1.0

 */
package exception;

public class TrocoInsuficienteException extends Exception
{
    /**
     * Constrói uma nova exceção com uma mensagem padrão informativa.
     * 
     */
    public TrocoInsuficienteException() {
        super("A venda não pode ser finalizada, não temos troco suficiente para lhe dar");
    }
}
