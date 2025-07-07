package controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.TelaLogin;
import javax.swing.JFrame;

public class SairHandler implements ActionListener {
    private JFrame frame;
    public SairHandler(JFrame frame) {
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
        new TelaLogin();
    }
}
