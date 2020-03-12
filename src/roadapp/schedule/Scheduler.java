package roadapp.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import roadapp.csv.OpenCSV;
import roadapp.parameter.MacroPanel;
import roadapp.parameter.ParameterPanel;

public class Scheduler {
	HashMap<String,String> id_map;
	HashMap<String,String[]> colname_map;
	HashMap<String,String> path_map;
	MacroPanel macropanel;
	ParameterPanel contractorpanel;
	
	int roads;
	HashMap<String,Object[]> column_value_map;
	
	Object[][] output;
	
	public Scheduler(HashMap<String,String> id_map, HashMap<String,String[]> colname_map, HashMap<String,String> path_map, MacroPanel macropanel, ParameterPanel contractorpanel) {
		this.id_map = id_map;
		this.colname_map = colname_map;
		this.path_map = path_map;
		this.macropanel = macropanel;
		this.contractorpanel = contractorpanel;
	}
	
	public void calculate() throws IOException {
		// Load Values
		HashSet colnames = getColumns();
		column_value_map = new HashMap<String,Object[]>();

		for(String database_name : colname_map.keySet()) {
			// Get data
			String path = path_map.get(database_name);
			OpenCSV csvio = new OpenCSV(path, false); // read as if there are no columns
			Object[][] data = csvio.read();
			csvio.close();
			
			roads = data.length; // set to latest dataset size
			
			// Check if any of the columns are needed
			for(int colindex = 0; colindex>data[0].length; colindex++) {
				String address = String.format("%s/%s", database_name, data[0][colindex]);
				// if col is needed
				if(colnames.contains(address)) {
					// find values
					Object[] values = new Object[data.length-1];
					for(int rowindex = 1; rowindex<roads; rowindex++) { // don't include column name
						values[rowindex-1] = data[rowindex][colindex];
					}
					// add to map
					column_value_map.put(address, values);
				}
			}
		}
		
		double[] damage_scores = getScores(macropanel.damage_panel.parameters);
		double[] length_scores = getScores(macropanel.length_panel.parameters);
		double[] traffic_scores = getScores(macropanel.traffic_panel.parameters);
		
		double[] damage_weighted_scores = multiply(damage_scores, (double) macropanel.damage_panel.weightmodel.getValue());
		double[] length_weighted_scores = multiply(length_scores, (double) macropanel.length_panel.weightmodel.getValue());
		double[] traffic_weighted_scores = multiply(traffic_scores, (double) macropanel.traffic_panel.weightmodel.getValue());
		
		double[][] weighted_scores = {damage_weighted_scores, length_weighted_scores, traffic_weighted_scores};
		
		double[] scores = sum(weighted_scores);
		
		
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
	
	public double[] getScores(Object[][] parameters) {
		double[] scores = new double[roads];
		for(Object[] row : parameters) {
			if(column_value_map.containsKey(row[0])) {
				for(int i = 0; i<roads; i++) {
					scores[i] += (double) row[i] * (double) column_value_map.get(row[0])[i];
				}
			}
		}
		
		return scores;
	}
	
	public double[] sum(double[][] arrays) {
		double[] out = new double[arrays[0].length];
		for(int i = 0; i<out.length; i++) {
			for(int k = 0; k<arrays.length; k++) {
				out[i] += arrays[k][i];
			}
		}
		
		return out;
	}
	
	public double[] multiply(double[] array, double scalar) {
		double[] out = new double[array.length];
		for(int i = 0; i<out.length; i++) {
			out[i] = array[i] + scalar;
		}
		
		return out;
	}
	
	public Object[][] getOutput() {
		return output;
	}
}
