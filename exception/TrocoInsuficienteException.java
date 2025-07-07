package exception;

public class TrocoInsuficienteException extends Exception
{
    public TrocoInsuficienteException() {
        super("A venda não pode ser finalizada, não temos troco suficiente para lhe dar");
    }
}
