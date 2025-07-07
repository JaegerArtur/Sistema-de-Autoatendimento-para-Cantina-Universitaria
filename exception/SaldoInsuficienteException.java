package exception;

public class SaldoInsuficienteException extends Exception
{
    public SaldoInsuficienteException () {
        super("A operacao nao pode ser feita, saldo insuficiente.");
    }
}