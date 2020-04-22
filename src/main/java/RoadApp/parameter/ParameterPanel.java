package RoadApp.parameter;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import RoadApp.csv.NewCSV;
import RoadApp.csv.OpenCSV;
import RoadApp.window.MainWindow;

public class ParameterPanel extends JPanel {
	
	ParameterPanel this2 = this;
	
	MainWindow window;
	boolean parent_is_window = false;
	boolean isdatapanel = false;
	
	public ParameterWindow parent;
	
	private JTable table;
	public DefaultTableModel model;

	String[] colnames;
	String[] coltypes;

	public ParameterPanel(String[] cn, String[] ct, Object[][] data) {
		// Set variables
		colnames = cn;
		coltypes = ct;
		
		initialize(data);
	}
	

	public ParameterPanel(String[] cn, String[] ct) {
		// Set variables
		colnames = cn;
		coltypes = ct;
		
		Object[][] data = {};
		initialize(data);
	}
	
	public ParameterPanel(ParameterWindow parent, String[] cn, String[] ct, Object[][] data) {
		// Set variables
		this.parent = parent;
		
		colnames = cn;
		coltypes = ct;
		
		initialize(data);
	}
	

	public ParameterPanel(ParameterWindow parent, String[] cn, String[] ct) {
		// Set variables
		this.parent = parent;
		
		colnames = cn;
		coltypes = ct;
		
		Object[][] data = {};
		initialize(data);
	}
	
	public ParameterPanel(MainWindow window, boolean isdatapanel, String[] cn, String[] ct, Object[][] data) {
		// Set variables
		this.isdatapanel = isdatapanel;
		this.window = window;
		this.parent_is_window = true;
		
		colnames = cn;
		coltypes = ct;
		
		initialize(data);
	}
	

	public ParameterPanel(MainWindow window, boolean isdatapanel, String[] cn, String[] ct) {
		// Set variables
		this.isdatapanel = isdatapanel;
		this.window = window;
		this.parent_is_window = true;
		
		colnames = cn;
		coltypes = ct;
		
		Object[][] data = {};
		initialize(data);
	}
	
	public void initialize(Object[][] data) {
		
		// Add components
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new Add()); 
		panel.add(btnNewButton);
		
		JButton btnNewButton_2 = new JButton("Remove");
		btnNewButton_2.addActionListener(new Remove()); 
		panel.add(btnNewButton_2);
		
		JButton btnNewButton_1 = new JButton("Clear All");
		btnNewButton_1.addActionListener(new ClearAll());
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_3 = new JButton("Import");
		btnNewButton_3.addActionListener(new ImportParameters()); 
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Export");
		btnNewButton_4.addActionListener(new ExportParameters()); 
		panel.add(btnNewButton_4);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		model = new DefaultTableModel(data, colnames);
		table = new JTable(model);
		scrollPane.setViewportView(table);
	}
	
	public class Add implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AddRowWindow w;
			if(parent_is_window) {
				w = new AddRowWindow(this2, window, colnames, coltypes);
			} else {
				w = new AddRowWindow(this2, this2.parent.parent.parent.parent, colnames, coltypes);
			}
			
			w.frame.setVisible(true);
		}
		
	}
	
	public class Remove implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = table.getSelectedRow();
			try {
				model.removeRow(row);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	public class ClearAll implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
		}
		
	}
	
	public class ImportParameters implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// File Dialogue (only csv)
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView());
			jfc.setDialogTitle("Import Settings");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.addChoosableFileFilter(filter);
			
			int returnValue = jfc.showOpenDialog(null);
			
			// Read File
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				try {
					// Open file, read data, and set.
					OpenCSV csvio = new OpenCSV(file, true);
					String[] file_colnames = csvio.getColumns();
					Object[][] file_data = csvio.read();
					csvio.close();
					
					for(Object[] row : file_data) {
						model.addRow(row);
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public class ExportParameters implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView());
			jfc.setDialogTitle("Export Settings");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.addChoosableFileFilter(filter);
			
			int returnValue = jfc.showSaveDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				// Get file
				File file = jfc.getSelectedFile();
				file.getPath();
				try {
					// Get data
					Object[][] file_data = to2DimArray(model.getDataVector());
					
					// Open file, read data, and set.
					NewCSV csvio = new NewCSV(file, colnames);
					csvio.write(file_data);
					csvio.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    }

}
