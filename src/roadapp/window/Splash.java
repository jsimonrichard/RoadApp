package roadapp.window;

import javax.swing.*;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Splash extends JFrame {
 
    WindowHandler handler;
	private JLabel imglabel;
 
    public Splash(WindowHandler handler, ImageIcon img) {
        super("Splash");
        this.handler = handler;
        
        setSize(500, 293);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        imglabel = new JLabel(img);
        add(imglabel);
        
        setVisible(true);
 
        handler.load();
    }
}