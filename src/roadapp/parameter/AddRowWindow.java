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
import roadapp.input.DatabaseInput;
import roadapp.input.FileInput;
import roadapp.input.Input;
import roadapp.input.SpinnerInput;
import roadapp.input.TextInput;
import roadapp.window.MainWindow;

import com.jgoodies.forms.layout.FormSpecs;
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
