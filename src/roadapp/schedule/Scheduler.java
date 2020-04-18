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
import java.util.Vector;

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
	
	String[] output_colnames = {"ID", "Score", "Recommended Contractor", "Cost with that Contractor"};
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
				System.out.println("ID not assigned");
			}
			
			id_lists.add(ids);
		}
		
		System.out.println("Finding and Calculating Scores");
		// Calculate Scores
		ArrayList<Double> damage_scores = getScores(macropanel.damage_panel.parameters, id_lists.get(0).length);
		ArrayList<Double> traffic_scores = getScores(macropanel.traffic_panel.parameters, id_lists.get(0).length);
		
		ArrayList<Double> damage_weighted_scores = multiply(damage_scores, (double) macropanel.damage_panel.weightmodel.getValue());
		ArrayList<Double> traffic_weighted_scores = multiply(traffic_scores, (double) macropanel.traffic_panel.weightmodel.getValue());
		
		ArrayList<ArrayList<Double>> weighted_scores = new ArrayList<ArrayList<Double>>();
		weighted_scores.add(damage_weighted_scores);
		weighted_scores.add(traffic_weighted_scores);
		
		Double[] scores = sum(weighted_scores);
		
		// Data for cost
		ArrayList<Double> length_scores = getScoresNoWeights(macropanel.length_panel.parameters, id_lists.get(0).length);
		
		// Sort
		System.out.println("Sorting...");
		Object[][] data = {id_lists.get(0), scores, length_scores.toArray()};
		Object[][] sorted_data = sortWithDouble(transpose(data), 1, -1);
		System.out.println("Finished!");
		
		// Adding Recommended Contractors
		System.out.println("Adding Recommended Contractors");
		
		// Calculate Cost
		Object[][] transposed_sorted_data = transpose(sorted_data);
		
		Object[][] contractor_results = scheduleContractors(transposed_sorted_data[2]);
		String[] contractors = (String[]) contractor_results[0];
		Double[] costs = (Double[]) contractor_results[1];
		
		// Append data
		Object[][] data_with_contractors = Arrays.copyOf(transposed_sorted_data, transposed_sorted_data.length+1);
		data_with_contractors[data_with_contractors.length-2] = contractors;
		data_with_contractors[data_with_contractors.length-1] = costs;
		
		System.out.println(data_with_contractors.length);
		
		// To output...
		output = transpose(data_with_contractors);
		
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
	
	public ArrayList<Double> getScoresNoWeights(Object[][] parameters, int length) {
		ArrayList<Double> scores = new ArrayList<Double>(Collections.nCopies(length, 0.0));
		
		for(Object[] row : parameters) {
			if(column_value_map.containsKey(row[0])) {
				for(int i = 0; i<column_value_map.get(row[0]).length; i++) {
					try {
						scores.set(i, scores.get(i) + Double.valueOf( column_value_map.get(row[0])[i].toString() ));
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

	public Object[][] sortWithDouble(Object[][] data, int sort_column_index, int direction) {
		
		Arrays.sort(data, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
			    try {
					return direction*Double.compare((Double) o1[sort_column_index], (Double) o2[sort_column_index]);
				} catch (Exception e) {
					return direction*Double.compare(Double.valueOf((String) o1[sort_column_index]), Double.valueOf((String) o2[sort_column_index]));
				}
			}
		});
		
		return data;
	}
	
	private Object[][] scheduleContractors(Object[] length_scores) {
		// Get data from contractor panel and convert to array
		Object[] contractor_data_halfway = contractorpanel.model.getDataVector().toArray();
		Object[][] contractor_data = new Object[contractor_data_halfway.length][((Vector) contractor_data_halfway[0]).size()];
		for(int i = 0; i < contractor_data_halfway.length; i++) {
			contractor_data[i] = ((Vector) contractor_data_halfway[i]).toArray();
		}
		
		System.out.println((String) contractor_data[0][0]);
		
		// Sort
		contractor_data = sortWithDouble(contractor_data, 2, 1);
		
		System.out.println((String) contractor_data[0][0]);
		
		// Set up needed variables
		Double sum_of_rates = 0.0;
		String[] output_contractors = new String[length_scores.length];
		Double[] output_costs = new Double[length_scores.length];
		int road_index = 0;
		
		// Loop through available contractors
		for(int i = 0; i < contractor_data.length; i++) {
			// Assign to as many roads as possible
			int job_index = 0;
			while(job_index < (int) contractor_data[i][1] && road_index < length_scores.length) {
				// Assign Values
				output_contractors[road_index] = (String) contractor_data[i][0];
				output_costs[road_index] = ((Double) contractor_data[i][2]) * ((Double) length_scores[road_index]);
				
				// Increment
				job_index++;
				road_index++;
			}
			
			// Add to sum for average
			sum_of_rates += (Double) contractor_data[i][2];
		}
		
		System.out.println(road_index);
		
		// Loop through the rest of the roads and give them the average rate
		Double average_rate = sum_of_rates / contractor_data.length;
		
		while(road_index < length_scores.length) {
			output_contractors[road_index] = "Estimate (No contractors/teams left)";
			output_costs[road_index] = average_rate * ((Double) length_scores[road_index]);
			
			road_index++;
		}
		
		Object[][] output = {output_contractors, output_costs};
		return output;
	}
	
	public Object[][] transpose(Object[][] data) {
		int rows = data.length;
		int columns = data[0].length;
		Object[][] temp = new Object[columns][rows];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				temp[j][i] = data[i][j];
			}
		}
		return temp;
	}

	public Object[][] getOutput() {
		return output;
	}

	public String[] getOutputColnames() {
		return output_colnames;
	}
}
