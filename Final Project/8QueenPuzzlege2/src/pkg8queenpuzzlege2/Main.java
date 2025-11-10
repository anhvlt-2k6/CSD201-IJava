/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8queenpuzzlege2;

/**
 *
 * @author ADMIN
 */
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, just use the default L&F
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EightQueensChecker2();
            }
        });
    }
}
