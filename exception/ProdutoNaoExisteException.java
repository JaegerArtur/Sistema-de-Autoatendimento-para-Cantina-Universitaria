
/**
 * Exceção lançada quando um produto não é encontrado no sistema.
 * 
 *
 * @author Grupo Artur, João e Miguel
 * @version 1.0

 */
package exception;

public class ProdutoNaoExisteException extends Exception
{
    /**
     * Constrói uma nova exceção com uma mensagem padrão.
     */
    public ProdutoNaoExisteException () {
        super("A operacao nao pode ser feita: produto não encontrado");
    }
}
