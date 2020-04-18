package roadapp.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class NewCSV {
	
	File file;
	PrintWriter printWriter;
	
	boolean hascolnames;
	String[] colnames;
	
	public NewCSV (File file, String[] colnames) throws IOException {
		this.file = file;
		printWriter = new PrintWriter(new FileWriter(file));
		
		hascolnames = true;
		this.colnames = colnames;
	}
	
	public NewCSV (String path, String[] colnames) throws IOException {
		printWriter = new PrintWriter(new FileWriter(path));
		
		hascolnames = true;
		this.colnames = colnames;
	}
	
	public void write(Object[][] data) {
		printWriter.println(String.join(",", colnames));
		
		for(Object[] row : data) {
			String[] stringrow = new String[row.length];
			for(int i=0;i<row.length;i++) {
				try {
					stringrow[i] = row[i].toString();
				} catch (Exception e) {
					stringrow[i] = "";
					e.printStackTrace();
				}
			}
			printWriter.println(String.join(",", stringrow));
		}
	}
	
	public void close() {
		printWriter.close();
	}
}
