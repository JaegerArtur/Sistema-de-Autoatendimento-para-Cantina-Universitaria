
/**
 * Exceção lançada quando um usuário não é encontrado no sistema.
 *
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
package exception;

public class UsuarioNaoExisteException extends Exception
{
    public UsuarioNaoExisteException(String cpf) {
        super("Usuário com CPF " + cpf + " não encontrado.");
    }
}
