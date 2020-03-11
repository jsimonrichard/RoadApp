package roadapp.schedule;

import java.util.HashMap;

import roadapp.parameter.MacroPanel;
import roadapp.parameter.ParameterPanel;

public class Scheduler {
	HashMap<String,String[]> colname_map;
	HashMap<String,String> path_map;
	MacroPanel macropanel;
	ParameterPanel contractorpanel;
	
	Object[][] output;
	
	public Scheduler(HashMap<String,String[]> colname_map, HashMap<String,String> path_map, MacroPanel macropanel, ParameterPanel contractorpanel) {
		this.colname_map = colname_map;
		this.path_map = path_map;
		this.macropanel = macropanel;
		this.contractorpanel = contractorpanel;
	}
	
	public void calculate() {
		
	}
	
	public Object[][] getOutput() {
		return output;
	}
}
