package roadapp.parameter;

import javax.swing.JPanel;

import roadapp.window.ParameterWindow;

import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IDParameterPanel extends MacroParameterPanel {

	MacroPanel parent;
	IDParameterPanel this2;
	public Object[][] parameters;
	
	/**
	 * Create the panel.
	 */
	public IDParameterPanel(MacroPanel parent) {
		super(parent);
	}
	
	@Override
	public void initialize() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton = new JButton("Choose ID Columns");
		add(btnNewButton);
	}
	
	public class ChooseIDColumns implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] colnames = {"Database", "ID Column"};
			String[] coltypes = {"database", "column"};
			ParameterWindow pw = new ParameterWindow(this2, colnames, coltypes, parameters);
		}
		
	}

}
