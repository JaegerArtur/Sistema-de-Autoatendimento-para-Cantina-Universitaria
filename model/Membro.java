package model;
import model.enums.TipoUsuario;
import exception.SaldoInsuficienteException;

/**
 * Representa um membro da organização (aluno ou professor) que utiliza o sistema.<br>
 * Um membro possui informações pessoais, credenciais de acesso e saldo para efetuar compras.<p>
 * Herda os atributos e comportamentos básicos da classe {@code Usuario}.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public class Membro extends Usuario {
    public Membro() {
        super("");
    }

    private String cpf, senha;
    private double saldo;

    /**
     * Constrói um objeto {@code Membro} com os dados fornecidos.
     * 
     * @param nome o nome do membro
     * @param cpf o CPF do membro
     * @param senha a senha para autenticação
     * @param saldo o saldo inicial disponível
     */
    public Membro (String nome, String cpf, String senha, double saldo) {
        super(nome);
        this.cpf = cpf;
        this.senha = senha;
        this.saldo = saldo;
    }

    /**
     * Retorna o CPF do membro.
     * 
     * @return o CPF do membro
     */
    public String getCpf() {
        return this.cpf;
    }

    /**
     * Retorna o saldo atual do membro.
     * 
     * @return o saldo disponível
     */
    public double getSaldo() {
        return this.saldo;
    }

    /**
     * Adiciona o valor fornecido ao saldo do membro.
     * 
     * @param valor o valor a ser adicionado ao saldo
     */
    public void addSaldo(double valor) {
        this.saldo += valor;
    }

    /**
     * Subtrai o valor fornecido do saldo do membro, se houver saldo suficiente.
     * 
     * @param valor o valor a ser subtraído do saldo
     * @throws SaldoInsuficienteException se o valor for maior que o saldo atual
     */
    public void subtraiSaldo(double valor) throws exception.SaldoInsuficienteException {
        if (valor <= this.saldo) {
            this.saldo -= valor;
        } else {
            throw new SaldoInsuficienteException();
        }
    }
    
    /**
     * Verifica se a senha fornecida corresponde à senha do membro.
     * 
     * @param senha a senha informada
     * @return {@code true} se a senha estiver correta, {@code false} caso contrário
     */
    public boolean validaSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Retorna o tipo de usuário representado por esta classe.
     * 
     * @return {@code TipoUsuario.MEMBRO}
     */
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.MEMBRO;
    }
}
