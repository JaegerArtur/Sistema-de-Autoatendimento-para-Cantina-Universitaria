package model;
import model.enums.TipoUsuario;

/**
 * Representa um administrador do sistema.<br>
 * Um admin possui credenciais de acesso e permissão para gerar relatórios, 
 * alterar estoque e informações de usuários.
 * <p>
 * Herda os atributos e comportamentos básicos da classe {@code Usuario}.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public class Admin extends Usuario {
    public Admin() {
        super("");
    }

    private String login, senha;

    /**
     * Cria um novo objeto {@code Admin} com nome, login e senha definidos.
     *
     * @param nome  o nome do administrador
     * @param login o nome de usuário para autenticação
     * @param senha a senha de acesso
     */
    public Admin(String nome, String login, String senha) {
        super(nome);
        this.login = login;
        this.senha = senha;
    }

    /**
     * Verifica se a senha informada corresponde à senha do administrador.
     *
     * @param senha a senha a ser validada
     * @return {@code true} se a senha for correta, {@code false} caso contrário
     */
    public boolean validaSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Retorna o tipo de usuário {@code ADMIN}, representando um administrador do sistema.
     *
     * @return o tipo {@code TipoUsuario.ADMIN}
     */
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.ADMIN;
    }

    /**
     * Retorna o login do administrador.
     * @return login
     */
    public String getLogin() {
        return this.login;
    }
}
