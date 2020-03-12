package roadapp.window;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import roadapp.parameter.IDParameterPanel;
import roadapp.parameter.MacroParameterPanel;
import roadapp.parameter.ParameterPanel;

import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class ParameterWindow {

	public MacroParameterPanel parent;
	
	public JFrame frame;
	private ParameterPanel panel;

	/**
	 * Create the application.
	 */
	/**
	 * @wbp.parser.constructor
	 */
	public ParameterWindow(MacroParameterPanel parent, String[] cn, String[] ct, Object[][] data)  {
		this.parent = parent;
		panel = new ParameterPanel(this, cn, ct, data);
		initialize();
	}
	
	public ParameterWindow(MacroParameterPanel parent, String[] cn, String[] ct)  {
		this.parent = parent;
		panel = new ParameterPanel(this, cn, ct);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Save Parameters");
		btnNewButton.addActionListener(new Save());
		panel_1.add(btnNewButton);
	}
	
	public class Save implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object[] parameters_unparsed = panel.model.getDataVector().toArray();
			Object[][] parameters = new Object[parameters_unparsed.length][];
			
			int i = 0;
			for(Object row : parameters_unparsed) {
				parameters[i++] = ((Vector) row).toArray();
			}
			
			parent.parameters = parameters;
			frame.dispose();
		}
		
	}

}
