package roadapp.parameter;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import java.awt.FlowLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import roadapp.input.ColumnInput;
import roadapp.input.FileInput;
import roadapp.input.Input;
import roadapp.input.SpinnerInput;
import roadapp.input.TextInput;

import com.jgoodies.forms.layout.FormSpecs;
import java.awt.Insets;

public class AddRowWindow {
	
	public ParameterPanel parent;

	JFrame frame;
	private String[] colnames;
	private String[] coltypes;
	private Input[] inputs;

	public AddRowWindow(ParameterPanel p, String[] c, String[] t) {
		parent = p;
		colnames = c;
		coltypes = t;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	/**
	 * 
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 100 + colnames.length*25);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Add a Row");
		panel_5.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new Cancel());
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new Add());
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		//gbl_panel_2.columnWidths = new int[]{77, 371};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		GridBagConstraints working_gbc_fieldLabel = new GridBagConstraints();
		working_gbc_fieldLabel.anchor = GridBagConstraints.EAST;
		working_gbc_fieldLabel.fill = GridBagConstraints.HORIZONTAL;
		working_gbc_fieldLabel.insets = new Insets(0, 0, 0, 5);
		working_gbc_fieldLabel.gridx = 0;
		working_gbc_fieldLabel.gridy = 0;
		
		GridBagConstraints working_gbc_input = new GridBagConstraints();
		working_gbc_input.anchor = GridBagConstraints.WEST;
		working_gbc_input.fill = GridBagConstraints.HORIZONTAL;
		working_gbc_input.gridx = 1;
		working_gbc_input.gridy = 0;
		
		
		// Generate Fields for rows;
		JLabel[] fieldLabels = new JLabel[colnames.length];
		inputs = new Input[colnames.length];
		for(int i=0;i<colnames.length;i++) {
			// Update y
			working_gbc_fieldLabel.gridy = i;
			working_gbc_input.gridy = i;
			
			// Special Cases
			if(coltypes[i] == "column") {
				colnames[i] = "Database";
			}
			
			fieldLabels[i] = new JLabel(String.format("%s:", colnames[i]));
			panel_2.add(fieldLabels[i], working_gbc_fieldLabel);
			
			switch(coltypes[i]) {
			case "string":
				inputs[i] = new TextInput();
				break;
			
			case "file":
				inputs[i] = new FileInput();
				break;
			
			case "column":
				inputs[i] = new ColumnInput(this);
				break;
				
			case "weight":
				inputs[i] = new SpinnerInput(new SpinnerNumberModel(0.5, 0, 1, 0.05));
				break;
			
			case "int":
				SpinnerNumberModel spinnermodel1 = new SpinnerNumberModel();
				spinnermodel1.setValue(0);
				spinnermodel1.setMinimum(0);
				spinnermodel1.setStepSize(1);
				inputs[i] = new SpinnerInput(spinnermodel1);
				break;
			
			case "cost":
				SpinnerNumberModel spinnermodel2 = new SpinnerNumberModel();
				spinnermodel2.setValue(0.0);
				spinnermodel2.setStepSize(10);
				inputs[i] = new SpinnerInput(spinnermodel2);
				break;
				
			}
			panel_2.add((Component) inputs[i], working_gbc_input);
		}
		
	}
	
	public class Add implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object[] row = new Object[colnames.length];
			for(int i=0;i<colnames.length;i++) {
				row[i] = inputs[i].getInput();
			}
			
			parent.model.addRow(row);
			
			// Force user to reload data if applicable
			if(parent.isdatapanel) {
				parent.window.showMacroWarningLabel();
			}
			
			frame.setVisible(false);
			frame.dispose();
		}
		
	}
	
	public class Cancel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false);
			frame.dispose();
		}
		
	}

}
