package roadapp.parameter;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Font;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import roadapp.window.MainWindow;

import com.jgoodies.forms.layout.FormSpecs;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import java.awt.BorderLayout;
import javax.swing.JSpinner;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MacroPanel extends JPanel {
	
	public MainWindow parent;
	
	public MacroParameterPanel damage_panel;
	public MacroParameterPanel length_panel;
	public MacroParameterPanel traffic_panel;
	public IDParameterPanel id_panel;

	/**
	 * Create the panel.
	 */
	public MacroPanel(MainWindow parent) {
		this.parent = parent;
		
		setLayout(new GridLayout(0, 1, 0, 0));
		
		damage_panel = new MacroParameterPanel(this, "Damage");
		add(damage_panel);
		
		length_panel = new MacroParameterPanel(this, "Length");
		add(length_panel);
		
		traffic_panel = new MacroParameterPanel(this, "Traffic Volume");
		add(traffic_panel);
		
		id_panel = new IDParameterPanel(this);
		add(id_panel);
	}
}
