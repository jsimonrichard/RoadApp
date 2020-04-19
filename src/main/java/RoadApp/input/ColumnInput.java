package RoadApp.input;

import javax.swing.JPanel;

import RoadApp.parameter.AddRowWindow;
import RoadApp.parameter.SettingsWindow;
import RoadApp.window.MainWindow;

import javax.swing.JLabel;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.Dimension;

public class ColumnInput extends JPanel implements Input {
	AddRowWindow parent1;
	SettingsWindow parent2;
	
	HashMap<String,String[]> colname_map;
	
	String[] colnames;
	
	DefaultComboBoxModel databasemodel;
	DefaultComboBoxModel columnmodel;
	
	/**
	 * Create the panel.
	 */
	
	public ColumnInput(MainWindow mw) {
		this.colname_map = mw.colname_map;
		
		// Get Colnames
		colnames = new String[colname_map.size()];
		int i = 0;
		for(String colname : colname_map.keySet()) {
			colnames[i++] = colname;
		}
		
		initialize();
	}

	public void initialize() {
		
		databasemodel = new DefaultComboBoxModel(colnames);
		JComboBox database = new JComboBox<String>(databasemodel);
		database.addActionListener(new DatabaseSelected());
		add(database);
		
		JLabel lblNewLabel_1 = new JLabel("Column:");
		add(lblNewLabel_1);
		
		columnmodel = new DefaultComboBoxModel();
		JComboBox column = new JComboBox<String>(columnmodel);
		column.setMaximumSize(new Dimension(200, 32767));
		column.setPreferredSize(new Dimension(150, 27));
		add(column);
		
		// A database is already selected...
		updateColumns();
	}
	
	public class DatabaseSelected implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			updateColumns();
		}
		
	}
	
	private void updateColumns() {
		// Get columns
		String[] columns = colname_map.get((String) databasemodel.getSelectedItem());
		
		// Set combo box
		columnmodel.removeAllElements();
		for(String column : columns) {
			columnmodel.addElement(column);
		}
	}

	@Override
	public Object getInput() {
		
		return String.format("%s/%s", databasemodel.getSelectedItem(), columnmodel.getSelectedItem());
	}

}
