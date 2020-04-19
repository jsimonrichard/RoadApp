package RoadApp.parameter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class MacroParameterPanel extends JPanel {
	
	public MacroPanel parent;
	MacroParameterPanel this2;
	
	protected String fieldname;
	public SpinnerModel weightmodel;
	public Object[][] parameters;

	/**
	 * Create the panel.
	 * @wbp.parser.constructor
	 */
	public MacroParameterPanel(MacroPanel parent, String fieldname) {
		this2 = this;
		this.parent = parent;
		this.fieldname = fieldname;
		
		Object[][] initparameters = {};
		parameters = initparameters;
		initialize();
	}
	
	public MacroParameterPanel(MacroPanel parent) {
		this2 = this;
		this.parent = parent;
		
		Object[][] initparameters = {};
		parameters = initparameters;
		initialize();
	}
	
	public void initialize() {
		setLayout(new BorderLayout(0, 0));
		JLabel lblNewLabel = new JLabel(fieldname);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel_3 = new JPanel();
		add(panel_3);
		
		JLabel lblNewLabel_3 = new JLabel("Weight:");
		panel_3.add(lblNewLabel_3);
		
		weightmodel = new SpinnerNumberModel(0.5, 0, 1, 0.05);   
		
		JSpinner weight = new JSpinner(weightmodel);
		weight.setPreferredSize(new Dimension(75, 26));
		panel_3.add(weight);
		
		JButton btnNewButton = new JButton("Edit Parameters");
		btnNewButton.addActionListener(new EditWeights());
		panel_3.add(btnNewButton);
	}
	
	public class EditWeights implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] colnames = {"Column", "Weight"};
			String[] coltypes = {"column", "weight"};
			ParameterWindow pw = new ParameterWindow(this2, colnames, coltypes, parameters);
			pw.frame.setVisible(true);
		}
		
	}

}
