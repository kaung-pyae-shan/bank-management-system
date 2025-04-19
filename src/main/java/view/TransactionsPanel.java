package view;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class TransactionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public TransactionsPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("This is Transactions");
		lblNewLabel.setBounds(169, 137, 91, 13);
		add(lblNewLabel);

	}

}
