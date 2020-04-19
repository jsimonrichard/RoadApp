package RoadApp.input;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class SpinnerInput extends JSpinner implements Input {

	public SpinnerInput(SpinnerNumberModel model) {
		super(model);
	}
	
	@Override
	public Object getInput() {
		return getValue();
	}

}
