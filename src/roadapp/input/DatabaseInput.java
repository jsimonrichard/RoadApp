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
import roadapp.parameter.SettingsWindow;
import roadapp.window.MainWindow;

public class DatabaseInput extends JPanel implements Input {
	
	Object[] database_names;
	DefaultComboBoxModel databasemodel;
	
	/**
	 * Create the panel.
	 */
	public DatabaseInput(MainWindow mw) {
		this.database_names = mw.colname_map.keySet().toArray();
		initialize();
	}

	public void initialize() {
		
		databasemodel = new DefaultComboBoxModel(database_names);
		JComboBox database = new JComboBox<String>(databasemodel);
		add(database);
	}

	@Override
	public Object getInput() {
		return databasemodel.getSelectedItem();
	}

}
