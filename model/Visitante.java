package model;
import model.enums.TipoUsuario;

/**
 * Representa um visitante que utiliza o sistema.<br>
 * Herda os atributos e comportamentos básicos da classe Usuario.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */

public class Visitante extends Usuario {
    public Visitante() {
        super("");
    }

    public Visitante (String nome) {
        super(nome);
    }
    
    /**
     * Retorna o tipo de usuário correspondente a essa classe.
     * 
     * @return TipoUsuario.VISITANTE
     */
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.VISITANTE;
    }
}
