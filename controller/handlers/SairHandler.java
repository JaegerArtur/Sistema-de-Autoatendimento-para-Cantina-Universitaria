/**
 * Handler responsável por processar a ação de sair/voltar para a tela de login.
 * <p>
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0

 */
package controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.TelaLogin;
import javax.swing.JFrame;

public class SairHandler implements ActionListener {
    /** Frame/janela que será fechado quando o handler for acionado. */
    private JFrame frame;
    
    /**
     * Constrói um novo handler para sair da tela atual.
     * 
     * @param frame a janela que será fechada quando o usuário sair
     */
    public SairHandler(JFrame frame) {
        this.frame = frame;
    }
    
    /**
     * Processa o evento de saída da tela atual.
     * <p>
     * @param e o evento de ação que disparou este handler
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
        new TelaLogin();
    }
}
