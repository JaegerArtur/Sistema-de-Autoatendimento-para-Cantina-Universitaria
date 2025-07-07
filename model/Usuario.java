package model;
import model.enums.TipoUsuario;

/**
 * Classe abstrata que representa um usuário do sistema.<br>
 * Pode ser estendida por tipos específicos como {@link Membro}, {@link Visitante} ou {@link Admin}
 * Contém os dados básicos como nome e métodos genéricos.
 * 
 * @author Grupo Artur, João e Miguel 
 * @version 1.0
 */
public abstract class Usuario
{
    /**
     * Nome completo do usuário.
     */
   private String nome;
   
   /**
     * Construtor para inicializar o nome do usuário.
     * 
     * @param nome o nome do usuário
     */
   public Usuario (String nome) {
       this.nome = nome;
   }
   
   /**
    * Retorna o nome do usuário
    * 
    * @return Nome do usuário em texto.
    */
   public String getNome() {
       return this.nome;
   }
   
   /**
    * Define o nome do usuário
    * 
    * @param nome Novo nome
    */
   public void setNome(String nome) {
       this.nome = nome;
   }
   
   /**
    * Método abstrato para exibir o tipo do usuário.
    * 
    * @return Tipo do usuário.
    */
   public abstract TipoUsuario getTipoUsuario();
}
