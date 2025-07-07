package exception;

public class ProdutoIndisponivelException extends Exception
{
    public ProdutoIndisponivelException (String nomeProduto, int qtdEstoque) {
        super("A operacao nao pode ser feita: " + nomeProduto + "  sem estoque suficiente para a venda: " + qtdEstoque + " em estoque.");
    }
}