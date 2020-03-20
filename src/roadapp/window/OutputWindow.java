package roadapp.window;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import roadapp.csv.NewCSV;

public class OutputWindow {

	public JFrame frame;
	
	Object[][] schedule;
	String[] colnames = {"ID", "Score"};
	private JTable table;

	/**
	 * Create the application.
	 */
	public OutputWindow(Object[][] ds) {
		this.schedule = ds;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable(schedule, colnames);
		scrollPane.setViewportView(table);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new Back());
		panel.add(btnNewButton);
		
		JButton btnNewButton2 = new JButton("Save Output");
		btnNewButton2.addActionListener(new Save());
		panel.add(btnNewButton2);
		
		
		JButton btnNewButton1 = new JButton("Close App");
		btnNewButton1.addActionListener(new Close());
		panel.add(btnNewButton1);
	}
	
	public class Close implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	public class Back implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
		
	}
	
	public class Save implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setDialogTitle("Save Output");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.addChoosableFileFilter(filter);
			
			int returnValue = jfc.showSaveDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				// Get file
				File file = jfc.getSelectedFile();
				file.getPath();
				try {
					// Open file, read data, and set.
					NewCSV csvio = new NewCSV(file, colnames);
					csvio.write(schedule);
					csvio.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}

}
