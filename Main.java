/**
 * Classe principal do Sistema de Autoatendimento para Cantina Universitária.
 * <p>
 * Esta classe é responsável por inicializar a aplicação, criando e exibindo
 * a tela de login inicial do sistema. O sistema simula um terminal de
 * autoatendimento semelhante aos utilizados em redes de fast-food, onde
 * clientes (membros, visitantes) e administradores podem interagir com
 * uma interface gráfica para realizar pedidos e gerenciar o sistema.
 * <p>
 * 
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */

import view.TelaLogin;

public class Main {
    /**
     * Método principal que inicia a aplicação do sistema de autoatendimento.
     * <p>
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TelaLogin();
        });
    }
}
