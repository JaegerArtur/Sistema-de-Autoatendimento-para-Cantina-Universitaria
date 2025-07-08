
/**
 * Exceção lançada quando um usuário não é encontrado no sistema.
 * 
 *
 * @author Grupo Artur, João e Miguel
 * @version 1.0

 */
package exception;

public class UsuarioNaoExisteException extends Exception
{
    /**
     * Constrói uma nova exceção com informações específicas sobre o CPF não encontrado.
     * 
     * @param cpf o CPF do usuário que não foi encontrado no sistema
     */
    public UsuarioNaoExisteException(String cpf) {
        super("Usuário com CPF " + cpf + " não encontrado.");
    }
}
