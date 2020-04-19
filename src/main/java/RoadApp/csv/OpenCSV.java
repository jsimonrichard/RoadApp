package RoadApp.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenCSV {
	
	File file;
	Scanner scanner;
	PrintWriter printWriter;
	
	boolean hascolnames;
	String[] colnames;
	
	// Constructors
	public OpenCSV (File file) throws IOException {
		this.file = file;
		scanner = new Scanner(file);
		
		hascolnames = false;
	}
	
	public OpenCSV (File file, boolean hascolnames) throws IOException {
		this.file = file;
		scanner = new Scanner(file);
		
		this.hascolnames = hascolnames;
	}
	
	public OpenCSV (String path) throws FileNotFoundException, IOException {
		file = new File(path);
		scanner = new Scanner(file);
		
		hascolnames = false;
	}
	
	public OpenCSV (String path, boolean hascolnames) throws IOException {
		file = new File(path);
		scanner = new Scanner(file);
		
		this.hascolnames = hascolnames;
	}
	
	// Methods
	
	public String[] getColumns() {
		if(colnames == null & scanner.hasNextLine()) {
			colnames = scanner.nextLine().split(",");
		}
		return colnames;
	}
	
	public Object[][] read() {
		if(colnames == null & hascolnames & scanner.hasNextLine()) {
			colnames = scanner.nextLine().split(",");
		}
		
		List<Object[]> data = new ArrayList<Object[]>(); // Init data
		while (scanner.hasNextLine()) {
			data.add(scanner.nextLine().split(",")); // Append lines to data
		}
		
		return data.toArray(new Object[0][0]); // Convert to array and return
	}
	
	public void close() {
		scanner.close();
	}
}
