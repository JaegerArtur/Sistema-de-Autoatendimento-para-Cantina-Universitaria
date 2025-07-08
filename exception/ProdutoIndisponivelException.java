
/**
 * Exceção lançada quando não há estoque suficiente para um produto solicitado.
 * 
 *
 * @author Grupo Artur, João e Miguel
 * @version 1.0

 */
package exception;

public class ProdutoIndisponivelException extends Exception
{
    /**
     * Constrói uma nova exceção com informações específicas sobre o produto e estoque.
     * 
     * @param nomeProduto o nome do produto que está com estoque insuficiente
     * @param qtdEstoque a quantidade atual disponível em estoque
     */
    public ProdutoIndisponivelException (String nomeProduto, int qtdEstoque) {
        super("A operacao nao pode ser feita: " + nomeProduto + "  sem estoque suficiente para a venda: " + qtdEstoque + " em estoque.");
    }
}