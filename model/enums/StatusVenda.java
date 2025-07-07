package model.enums;

/**
 * Enumeração que representa os possíveis status de uma venda
 * no sistema de autoatendimento da cantina universitária.
 * 
 * Cada status define o estado atual de uma transação, podendo
 * ser usada para controle, histórico e geração de relatórios.
 * 
 * Os statut incluem CONCLUIDA, CANCELADA e PENDENTE.
 * 
 * @author Grupo Artur, João e Miguel
 * @version 1.0
 */
public enum StatusVenda {
    
    /**
     * A venda foi concluída com sucesso.
     */
    CONCLUIDA,

    /**
     * A venda foi cancelada antes da finalização.
     */
    CANCELADA
}

