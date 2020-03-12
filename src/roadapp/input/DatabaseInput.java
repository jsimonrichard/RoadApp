package roadapp.input;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import roadapp.input.ColumnInput.DatabaseSelected;
import roadapp.parameter.AddRowWindow;

public class DatabaseInput extends JPanel implements Input {
	AddRowWindow parent;
	
	HashMap<String,String[]> colname_map;
	
	String[] colnames;
	
	DefaultComboBoxModel databasemodel;
	
	/**
	 * Create the panel.
	 */
	public DatabaseInput(AddRowWindow parent) {
		this.parent = parent;
		this.colname_map = this.parent.parent.parent.parent.parent.parent.colname_map;
		
		initialize();
	}
	
	public void initialize() {
		
		databasemodel = new DefaultComboBoxModel(colnames);
		JComboBox database = new JComboBox<String>(databasemodel);
		add(database);
	}

	@Override
	public Object getInput() {
		return databasemodel.getSelectedItem();
	}

}