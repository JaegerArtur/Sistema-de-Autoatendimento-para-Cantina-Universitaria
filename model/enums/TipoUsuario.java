package model.enums;

/**
 * Representa os tipos de usuários que podem utilizar o sistema.
 * 
 * Os tipos incluem ALUNO, PROFESSOR e VISITANTE.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public enum TipoUsuario {
    
    /**
     * Usuário do tipo membro, com acesso a saldo e descontos.
     */
    MEMBRO,
    
    /**
     * Usuário do tipo admin, com acesso a relatórios e demais recursos administrativos.
     */
    ADMIN,
    
    /**
     * Usuário do tipo visitante, sem acesso a saldo.
     */
    VISITANTE;
}

