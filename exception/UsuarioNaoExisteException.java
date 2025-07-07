package exception;

public class UsuarioNaoExisteException extends Exception
{
    public UsuarioNaoExisteException(String cpf) {
        super("Usuário com CPF " + cpf + " não encontrado.");
    }
}
