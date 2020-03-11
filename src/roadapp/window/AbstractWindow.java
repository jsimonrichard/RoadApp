package roadapp.window;

import java.awt.*;
import javax.swing.*;

public abstract class AbstractWindow {
	WindowHandler handler;
	
	JFrame frame;
	String caption;
	
	AbstractWindow(WindowHandler handler, String caption) {
		this.handler = handler;
		
		frame = new JFrame(caption);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		load();
	}
	
	abstract public void load();
	
	public void show() {
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void hide() {
		frame.setVisible(false);
	}
}
