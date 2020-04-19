package RoadApp.parameter;

import javax.swing.JPanel;
import java.awt.GridLayout;

import RoadApp.window.MainWindow;

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
		
		traffic_panel = new MacroParameterPanel(this, "Traffic Volume");
		add(traffic_panel);
		
		length_panel = new LengthParameterPanel(this, "Length");
		add(length_panel);
		
		id_panel = new IDParameterPanel(this);
		add(id_panel);
	}
}
