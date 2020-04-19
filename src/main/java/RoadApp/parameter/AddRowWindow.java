package RoadApp.parameter;

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

import RoadApp.input.ColumnInput;
import RoadApp.input.DatabaseInput;
import RoadApp.input.FileInput;
import RoadApp.input.Input;
import RoadApp.input.SpinnerInput;
import RoadApp.input.TextInput;
import RoadApp.window.MainWindow;

import java.awt.Insets;

public class AddRowWindow extends SettingsWindow {
	
	public ParameterPanel parent;

	public AddRowWindow(ParameterPanel p, MainWindow mw, String[] c, String[] t) {
		super(mw, c, t);
		parent = p;
		
		// Change listener for save button
		save_button.removeActionListener(save_listener);
		save_button.addActionListener(new SaveRow());
	}
	
	public AddRowWindow(ParameterPanel p, String[] c, String[] t) {
		super(c, t);
		parent = p;
		
		// Change listener for save button
		save_button.removeActionListener(save_listener);
		save_button.addActionListener(new SaveRow());
	}

	public class SaveRow implements ActionListener {

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
				parent.window.data_loaded = false;
			}
			
			frame.setVisible(false);
			frame.dispose();
		}
		
	}

}
