package RoadApp.window;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

public class WindowHandler {
	// Initialize Windows
	MainWindow mainWindow;
	
	public void splash() throws InterruptedException {
		ImageIcon img = new ImageIcon(getClass().getResource("img/splash.png"));
		Splash s = new Splash(this, img);
        Thread.sleep(2000); // Wait for the splash screen (needs to allow time for image to appear).
        s.dispose();
        SwingUtilities.invokeLater(new Runnable(){
            public void run()
            {
            	mainWindow.show();
            }
        });
	}
	
	public void loadDirectly() {
		mainWindow = new MainWindow();
		mainWindow.show();
	}
	
	public void load() { // Called during the  splash screen
		mainWindow = new MainWindow();
	}
}
