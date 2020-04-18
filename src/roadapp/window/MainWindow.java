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
import roadapp.parameter.SettingsWindow;
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

	MainWindow this2 = this;
	
	public JFrame frame;
	
	// Global Panels
	private ParameterPanel datapanel;
	private JPanel macropanel;
	private ParameterPanel contractorpanel;
	
	// Defaults
	String[] datapanel_colnames = {"Name", "File Path"};
	String[] datapanel_coltypes = {"string", "file"};
	
	String[] contractorpanel_colnames = {"Name", "Number of Jobs to Schedule", "Cost per Length Unit"};
	String[] contractorpanel_coltypes = {"string", "int", "cost"};
	
	// Globally needed components
	private JLabel loaddata_warning_label;
	private MacroPanel macrosubpanel;
	
	private JButton schedule_button;
	
	// Data Variables
	public boolean data_loaded = false;
	
	HashMap<String,String> id_map;
	public HashMap<String,String[]> colname_map;
	HashMap<String,String> path_map;
	private JPanel panel_1;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		loaddata_warning_label = new JLabel("Please load the databases first. To do that, press the \"Load Data Paths and Columns\" button on the \"Data\" tab.");
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
		
		schedule_button = new JButton("Schedule");
		schedule_button.setPreferredSize(new Dimension(100, 50));
		schedule_button.addActionListener(new Schedule());
		schedule_button.setMinimumSize(new Dimension(100, 50));
		frame.getContentPane().add(schedule_button, BorderLayout.SOUTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// Data Pane
		JPanel panel = new JPanel();
		tabbedPane.addTab("Data", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		
		JButton btnNewButton_1 = new JButton("Load Data Paths and Columns");
		btnNewButton_1.addActionListener(new LoadDatasets());
		panel_3.add(btnNewButton_1);
		
		// Add Data Panel
		datapanel = new ParameterPanel(this, true, datapanel_colnames, datapanel_coltypes);
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
			for(Object[] row : file_data) {
				try {
					// Add path
					path_map.put((String) row[0], (String) row[1]);
					
					// Add columns
					System.out.println((String) row[1]);
					OpenCSV csvio = new OpenCSV((String) row[1], true); //  path
					String[] csv_columns = csvio.getColumns(); // Get columns from csv
					colname_map.put((String) row[0], (String[]) csv_columns); // Add to map with name as key
					data_loaded = true;
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
							String.format("%s cannot be found.\nLoading aborted.", (String) row[1]),
							"File not Found",
						    JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					data_loaded = false;
					break;
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(frame,
							"File name cannot be empty!",
							"File not Found",
						    JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					data_loaded = false;
					break;
				}
				
			}
			if(data_loaded) {
				showMacroPanel();
			}
			
		}
		
	}
	
	public class Schedule implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			schedule_button.setText("Scheduling...");
			try {
				Thread.sleep(5);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			// Load id_map
			System.out.println("Loading ID map");
			id_map = new HashMap<String,String>();
			for(int i = 0; i<macrosubpanel.id_panel.parameters.length; i++) {
				String database_name = ((String) macrosubpanel.id_panel.parameters[i][0]).split("/")[0];
				id_map.put(database_name, (String) macrosubpanel.id_panel.parameters[i][0]);
			}
			
			// Schedule
			Scheduler scheduler = new Scheduler(frame, id_map, colname_map, path_map, macrosubpanel, contractorpanel);
			System.out.println("Calculating...");
			try {
				scheduler.calculate();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			schedule_button.setText("Schedule");
			try {
				Thread.sleep(5);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			OutputWindow outputwindow = new OutputWindow(scheduler.getOutput(), scheduler.getOutputColnames());
			outputwindow.frame.setVisible(true);
		}
		
	}

}
