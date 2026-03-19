package cinema.main;

import cinema.debug.DummyData;
import cinema.ui.*;

/**
 *
 * @author 
 */
public class Main {
    public static void main(String[] args) {
        DummyData.generateMovieDummyData(); 
		DummyData.generateUserDummyData(); 
		DummyData.generateShowtimeDummyData();
		
		try {
			com.formdev.flatlaf.FlatLightLaf.setup();
		} catch (Exception ex) {
			System.err.println("Failed to init FlatLaf" + ex);
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
