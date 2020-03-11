package roadapp.input;

import javax.swing.JTextField;

public class TextInput extends JTextField implements Input {

	public TextInput() {
		this.setColumns(20);
	}
	
	@Override
	public Object getInput() {
		// TODO Auto-generated method stub
		return this.getText();
	}

}
