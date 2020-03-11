package roadapp.schedule;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class OutputWindow {

	private JFrame frame;
	
	private Object[][] schedule;

	/**
	 * Create the application.
	 */
	public OutputWindow(Object[][] schedule) {
		this.schedule = schedule;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
