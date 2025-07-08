
/**
 * Exceção lançada quando um produto não é encontrado no sistema.
 *
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
package exception;

public class ProdutoNaoExisteException extends Exception
{
    public ProdutoNaoExisteException () {
        super("A operacao nao pode ser feita: produto não encontrado");
    }
}
