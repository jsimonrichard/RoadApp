package RoadApp.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FileInput extends JButton implements Input {
	
	JFileChooser jfc;
	File file;
	String path;
	
	public FileInput() {
		super("Open File");
		jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Chose a CSV File");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(filter);
		
		addActionListener(new OpenFile());
	}
	
	public FileInput(String text) {
		super(text);
		jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Chose a CSV File");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(filter);
		
		addActionListener(new OpenFile());
	}
	
	public class OpenFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				path = file.getAbsolutePath();
			}
			
			setText(String.format("File: %s", path));
		}
		
	}
	
	@Override
	public Object getInput() {
		return path;
	}

}
