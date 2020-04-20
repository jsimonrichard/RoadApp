package RoadApp.parameter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LengthParameterPanel extends MacroParameterPanel {

	public LengthParameterPanel(MacroPanel parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	public LengthParameterPanel(MacroPanel parent, String fieldname) {
		super(parent, fieldname);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
		setLayout(new BorderLayout(0, 0));
		JLabel lblNewLabel = new JLabel(fieldname);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel_3 = new JPanel();
		add(panel_3);
		
		JButton btnNewButton = new JButton("Edit Parameters");
		btnNewButton.addActionListener(new EditWeights());
		panel_3.add(btnNewButton);
	}
	
	public class EditWeights implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] colnames = {"Column"};
			String[] coltypes = {"column"};
			ParameterWindow pw = new ParameterWindow(this2, "Edit Length Parameters", colnames, coltypes, parameters);
			pw.frame.setVisible(true);
		}
		
	}
}
