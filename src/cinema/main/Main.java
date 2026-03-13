package cinema.main;

import cinema.debug.DummyData;
import cinema.ui.*;

/**
 *
 * @author 
 */
public class Main {
    public static void main(String[] args) {
        DummyData.generateMovieDummyData(); DummyData.generateUserDummyData();
		
		try { // TS AI NGL
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginUI loginWindow = new LoginUI();
                loginWindow.setLocationRelativeTo(null);
                loginWindow.setVisible(true);
            }
        });
		
    }
}
