package roadapp.schedule;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import roadapp.csv.OpenCSV;
import roadapp.parameter.MacroPanel;
import roadapp.parameter.ParameterPanel;
import roadapp.window.MainWindow;

public class Scheduler {
	
	JFrame parent_frame;
	
	HashMap<String,String> id_map;
	HashMap<String,String[]> colname_map;
	HashMap<String,String> path_map;
	MacroPanel macropanel;
	ParameterPanel contractorpanel;
	
	ArrayList<Object[]> id_lists;
	HashMap<String,Object[]> column_value_map;
	
	Object[][] output;
	
	public Scheduler(JFrame parent_frame, HashMap<String,String> id_map, HashMap<String,String[]> colname_map, HashMap<String,String> path_map, MacroPanel macropanel, ParameterPanel contractorpanel) {
		this.parent_frame = parent_frame;
		
		this.id_map = id_map;
		this.colname_map = colname_map;
		this.path_map = path_map;
		this.macropanel = macropanel;
		this.contractorpanel = contractorpanel;
	}
	
	public int calculate() throws IOException {
		//Calculating Time Elapsed...
		long start = System.currentTimeMillis();
		
		// Load Values
		HashSet colnames = getColumns();
		column_value_map = new HashMap<String,Object[]>();
		
		id_lists = new ArrayList<Object[]>();
		
		System.out.println("Loading Databases");
		for(String database_name : colname_map.keySet()) {
			// Get data
			String path = path_map.get(database_name);
			OpenCSV csvio = new OpenCSV(path, false); // read as if there are no columns
			Object[][] data = csvio.read();
			csvio.close();
			
			// Check Id column
			String id_column_address;
			try {
				id_column_address = id_map.get(database_name);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(parent_frame,
						String.format("No ID column found for %s. \n Scheduling aborted.", database_name),
						"ID Column not Found",
					    JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return 0;
			}
			
			// Tf any of the columns are needed then add them to map
			// Also check for id column
			Object[] ids = new Object[data.length-1];
			boolean ids_assigned = false;
			
			for(int colindex = 0; colindex < data[0].length; colindex++) {
				String address = String.format("%s/%s", database_name, data[0][colindex]);
				// Check if it's the id column
				if(id_column_address.equals(address)) {
					System.out.println(String.format("Found ID Column for %s.", database_name));
					ids_assigned = true;
					for(int rowindex = 1; rowindex<data.length; rowindex++) { // don't include column name
						ids[rowindex-1] = data[rowindex][colindex];
					}
				}
				
				// if col is needed
				if(colnames.contains(address)) {
					// find values
					Object[] values = new Object[data.length-1];
					for(int rowindex = 1; rowindex<data.length; rowindex++) { // don't include column name
						values[rowindex-1] = data[rowindex][colindex];
					}
					// add to map
					column_value_map.put(address, values);
				}
			}
			
			if(!ids_assigned) {
				System.out.println("ID not assign");
			}
			
			id_lists.add(ids);
		}
		
		System.out.println("Finding and Calculating Scores");
		ArrayList<Double> damage_scores = getScores(macropanel.damage_panel.parameters, id_lists.get(0).length);
		ArrayList<Double> length_scores = getScores(macropanel.length_panel.parameters, id_lists.get(0).length);
		ArrayList<Double> traffic_scores = getScores(macropanel.traffic_panel.parameters, id_lists.get(0).length);
		
		ArrayList<Double> damage_weighted_scores = multiply(damage_scores, (double) macropanel.damage_panel.weightmodel.getValue());
		ArrayList<Double> length_weighted_scores = multiply(length_scores, (double) macropanel.length_panel.weightmodel.getValue());
		ArrayList<Double> traffic_weighted_scores = multiply(traffic_scores, (double) macropanel.traffic_panel.weightmodel.getValue());
		
		ArrayList<ArrayList<Double>> weighted_scores = new ArrayList<ArrayList<Double>>();
		weighted_scores.add(damage_weighted_scores);
		weighted_scores.add(length_weighted_scores);
		weighted_scores.add(traffic_weighted_scores);
		
		Double[] scores = sum(weighted_scores);
		
		System.out.println("Sorting Scores");
		output = sort(id_lists.get(0), scores);
		System.out.println("Finished!");
		
		// Calculate Time Elapsed...
		long end = System.currentTimeMillis();
		double duration = (end - start)/1000;
		System.out.println(String.format("%f seconds elapsed.", duration));
		return 1;
	}
	
	public HashSet getColumns() {
		HashSet columns = new HashSet<String>();
		for(Object[] row : macropanel.damage_panel.parameters) {
			columns.add(row[0]);
		}
		
		for(Object[] row : macropanel.length_panel.parameters) {
			columns.add(row[0]);
		}
		
		for(Object[] row : macropanel.traffic_panel.parameters) {
			columns.add(row[0]);
		}
		
		return columns;
	}
	
	// Generic function to find the index of an element in an object array in Java
	public static int find(Object[] a, Object target)
	{
		for (int i = 0; i < a.length; i++)
			if (target.equals(a[i]))
				return i;

		return -1;
	}
	
	public ArrayList<Double> getScores(Object[][] parameters, int length) {
		ArrayList<Double> scores = new ArrayList<Double>(Collections.nCopies(length, 0.0));
		
		for(Object[] row : parameters) {
			if(column_value_map.containsKey(row[0])) {
				for(int i = 0; i<column_value_map.get(row[0]).length; i++) {
					try {
						scores.set(i, scores.get(i) + Double.valueOf( row[1].toString() ) * Double.valueOf( column_value_map.get(row[0])[i].toString() ));
					} catch (NumberFormatException e) {
						System.out.println(String.format("%s did not cast.", row[0]));
						System.out.println(String.format("Value: %s", column_value_map.get(row[0])[i]));
					}
				}
			}
		}
		
		return scores;
	}
	
	public Double[] sum(ArrayList<ArrayList<Double>> weighted_scores) {
		Double[] out = new Double[weighted_scores.get(0).size()];
		Arrays.fill(out, new Double(0));
		for(int i = 0; i<out.length; i++) {
			for(int k = 0; k<weighted_scores.size(); k++) {
				out[i] += weighted_scores.get(k).get(i);
			}
		}
		
		return out;
	}
	
	public ArrayList<Double> multiply(ArrayList<Double> damage_scores, double scalar) {
		ArrayList<Double> out = new ArrayList<Double>();
		for(int i = 0; i<damage_scores.size(); i++) {
			out.add(damage_scores.get(i) * scalar);
		}
		
		return out;
	}
	
	public Object[][] sort(Object[] ids, Double[] scores) {
		// Turn into table
		
		Double[][] road_table = new Double[ids.length][2];
		for(int i = 0; i<road_table.length; i++) {
			Double[] row = {Double.valueOf((String) ids[i]), scores[i]};
			road_table[i] = row;
		}
		
		Arrays.sort(road_table, new Comparator<Double[]>() {
			  public int compare(Double[] o1, Double[] o2) {
				// - signs make the list decrease
			    if (o1[1] == o2[1]) {
			      return -Double.compare(o1[0], o2[0]);
			    } else {
			      return -Double.compare(o1[1], o2[1]);
			    }
			  }
			});
		
		return road_table;
	}
	
	public Object[][] getOutput() {
		return output;
	}
}
