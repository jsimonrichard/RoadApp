package roadapp.window;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;

import roadapp.csv.OpenCSV;
import roadapp.parameter.MacroPanel;
import roadapp.parameter.ParameterPanel;
import roadapp.schedule.OutputWindow;
import roadapp.schedule.Scheduler;

import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;

public class MainWindow {

	private JFrame frame;
	
	// Global Panels
	private ParameterPanel datapanel;
	private JPanel macropanel;
	private ParameterPanel contractorpanel;
	
	// Defaults
	String[] datapanel_colnames = {"Name", "File Path"};
	String[] datapanel_coltypes = {"string", "file"};
	private Object[][] datapanel_default = {{"Road Inventory", "/Users/simonrichard/Desktop/Design Challenge Spring 2020/tims_csv_datasets/WGIS_ROAD_INVENTORY.csv"}};
	
	String[] contractorpanel_colnames = {"Name", "Availible Teams", "Cost per Unit"};
	String[] contractorpanel_coltypes = {"string", "int", "cost"};
	
	// Globally needed components
	private JLabel loaddata_warning_label;
	private MacroPanel macrosubpanel;
	
	// Data Variables
	HashMap<String,String> id_map;
	public HashMap<String,String[]> colname_map;
	HashMap<String,String> path_map;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		loaddata_warning_label = new JLabel("Please load data first.");
		loaddata_warning_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		macrosubpanel = new MacroPanel(this); // not used yet
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("RoadApp: Parametrized Scheduling");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
		frame.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Schedule");
		btnNewButton.setPreferredSize(new Dimension(100, 50));
		btnNewButton.addActionListener(new Schedule());
		
		btnNewButton.setMinimumSize(new Dimension(100, 50));
		frame.getContentPane().add(btnNewButton, BorderLayout.SOUTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// Data Pane
		JPanel panel = new JPanel();
		tabbedPane.addTab("Data", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("Load Data");
		btnNewButton_1.addActionListener(new LoadDatasets());
		panel_3.add(btnNewButton_1);
		
		// Add Data Panel
		datapanel = new ParameterPanel(this, true, datapanel_colnames, datapanel_coltypes, datapanel_default);
		panel.add(datapanel, BorderLayout.CENTER);
		
		// Macro Parameter Pane
		macropanel = new JPanel();
		tabbedPane.addTab("Macro Parameters", null, macropanel, null);
		macropanel.setLayout(new BorderLayout(0, 0));
		
		macropanel.add(loaddata_warning_label, BorderLayout.CENTER);
		
		// Contractor Pane
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Contractors", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		contractorpanel = new ParameterPanel(this, false, contractorpanel_colnames, contractorpanel_coltypes);
		panel_2.add(contractorpanel);
	}
	
	public void showMacroPanel() {
		macropanel.remove(loaddata_warning_label); // Remove label
		macropanel.add(macrosubpanel);
	}
	
	public void showMacroWarningLabel() {
		macropanel.remove(macrosubpanel);
		macropanel.add(loaddata_warning_label);
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public class LoadDatasets implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object[][] file_data = datapanel.to2DimArray(datapanel.model.getDataVector());
			
			colname_map = new HashMap<String,String[]>();
			path_map = new HashMap<String,String>();
			boolean done = false;
			for(Object[] row : file_data) {
				try {
					// Add path
					path_map.put((String) row[0], (String) row[1]);
					
					// Add columns
					System.out.println((String) row[1]);
					OpenCSV csvio = new OpenCSV((String) row[1], true); //  path
					String[] csv_columns = csvio.getColumns(); // Get columns from csv
					colname_map.put((String) row[0], (String[]) csv_columns); // Add to map with name as key
					done = true;
				} catch (IOException e1) {
					System.out.println("YEEE");
					JOptionPane.showMessageDialog(frame,
							String.format("%s cannot be found.\nLoading aborted.", (String) row[1]),
							"File not Found",
						    JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					break;
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(frame,
							"File name cannot be empty!",
							"File not Found",
						    JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					break;
				}
				
			}
			if(done) {
				showMacroPanel();
			}
			
			System.out.println(colname_map);
		}
		
	}
	
	public class Schedule implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Load id_map
			for(int i = 0; i<macrosubpanel.id_panel.parameters.length; i++) {
				id_map.put((String) macrosubpanel.id_panel.parameters[i][0], (String) macrosubpanel.id_panel.parameters[i][1]);
			}
			
			// Schedule
			Scheduler scheduler = new Scheduler(id_map, colname_map, path_map, macrosubpanel, contractorpanel);
			try {
				scheduler.calculate();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			OutputWindow outputwindow = new OutputWindow(scheduler.getOutput());
		}
		
	}

}
