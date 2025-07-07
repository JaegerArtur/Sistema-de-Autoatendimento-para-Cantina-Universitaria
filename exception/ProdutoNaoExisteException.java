package exception;

public class ProdutoNaoExisteException extends Exception
{
    public ProdutoNaoExisteException () {
        super("A operacao nao pode ser feita: produto não encontrado");
    }
}
