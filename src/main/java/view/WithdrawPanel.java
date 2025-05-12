package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.TransactionController;
import model.dto.DepositWithdrawForm;

public class WithdrawPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField accNumberField;
	private JTextField accHolderField;
	private JTextField emailField;
	private JTextField phoneField;
	private JTextField addressField;
	private JTextField cusSinceField;
	private JTextField currentBalanceField;
	private JTextField amountField;

	private DepositWithdrawForm form;
	private int currentAccountId;

	/**
	 * Create the panel.
	 */
	public WithdrawPanel(TransactionController controller, int loggedInStaffId) {
		// ==== Form Panel ====
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 10, 15, 10);
		gbc.anchor = GridBagConstraints.WEST;

		String[] labels = { "Account Number", "Account Holder", "Email", "Phone", "Address", "Customer Since",
				"Current Balance", "Withdraw Amount" };

		// init form labels
		int y = 0;
		for (String label : labels) {
			gbc.gridx = 0;
			gbc.gridy = y;
			JLabel formLabel = new JLabel(label + ":");
			formLabel.setFont(new Font("Dialog", Font.BOLD, 13));
	
			if(label.equals("Withdraw Amount")) {
				gbc.insets = new Insets(35, 10, 13, 10);
				add((formLabel), gbc);
				continue;
			}
			add((formLabel), gbc);
			y++;
		}
		
		// init textfields
		accNumberField = new JTextField(25);
		accHolderField = new JTextField(25);
		emailField = new JTextField(25);
		phoneField = new JTextField(25);
		addressField = new JTextField(25);
		cusSinceField = new JTextField(25);
		currentBalanceField = new JTextField(25);
		amountField = new JTextField(25);
		
		accNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				char c = evt.getKeyChar();
				if (!(Character.isDigit(c) || c == '\b')) {
					evt.consume(); // Only allow digits, and backspace
				}
				String currentText = accNumberField.getText();

				if (currentText.length() >= 12) {
					evt.consume(); // Block non-digit or too many digits
				}
			}
		});
		
		amountField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				char c = evt.getKeyChar();
				if (!(Character.isDigit(c) || c == '.' || c == '\b')) {
					evt.consume(); // Only allow digits, decimal point, and backspace
				}

				// Allow only one decimal point
				if (c == '.' && amountField.getText().contains(".")) {
					evt.consume();
				}
			}
		});
		
		gbc.insets = new Insets(15, 10, 15, 10);

		gbc.gridx = 1;
		gbc.gridy = 0;
		add(accNumberField, gbc);
		
		gbc.gridy = 1;
		accHolderField.setEditable(false);
		add(accHolderField, gbc);
		
		gbc.gridy = 2;
		emailField.setEditable(false);
		add(emailField, gbc);
		
		gbc.gridy = 3;
		phoneField.setEditable(false);
		add(phoneField, gbc);
		
		gbc.gridy = 4;
		addressField.setEditable(false);
		add(addressField, gbc);
		
		gbc.gridy = 5;
		cusSinceField.setEditable(false);
		add(cusSinceField, gbc);
		
		gbc.gridy = 6;
		currentBalanceField.setEditable(false);
		add(currentBalanceField, gbc);
		
		gbc.gridy = 7;
		gbc.insets = new Insets(35, 10, 10, 10);
		add(amountField, gbc);

		JButton actionButton = new JButton("Withdraw");
		actionButton.setForeground(Color.white);
		actionButton.setBackground(new Color(0xe0162b));
		actionButton.setFocusPainted(false);
		actionButton.setPreferredSize(new Dimension(100, 30));
		gbc.gridx = 1;
		gbc.gridy++;
		gbc.insets = new Insets(5, 10, 10, 10);
		add(actionButton, gbc);	
		
		accNumberField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String accountNumber = accNumberField.getText();
				form = controller.fetchAccountAndCustomer(accountNumber);
				if(null != form) {
					accHolderField.setText(form.getAccountHolder());
					emailField.setText(form.getEmail());
					phoneField.setText(form.getPhone());
					addressField.setText(form.getAddress());
					cusSinceField.setText(form.getCustomerSince().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
					currentBalanceField.setText(form.getCurrentBalance().toString());
					currentAccountId = form.getAccountId();
				} else {
					accHolderField.setText("");
					emailField.setText("");
					phoneField.setText("");
					addressField.setText("");
					cusSinceField.setText("");
					currentBalanceField.setText("");
					currentAccountId = 0;
				}
			}
		});
		
		actionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BigDecimal amount = new BigDecimal(0);
				try {
					amount = new BigDecimal(amountField.getText());
					if(new BigDecimal(currentBalanceField.getText()).compareTo(amount) < 0) {
						JOptionPane.showMessageDialog(null,  "Insufficient Balance!!", "Failed", JOptionPane.ERROR_MESSAGE);
						return;
					}
					int row = controller.withdrawBalance(amount, currentAccountId, loggedInStaffId);
					if (row > 0) {
						JOptionPane.showMessageDialog(null,  "Withdrawn successfully!!", "Success", JOptionPane.INFORMATION_MESSAGE);
						form = controller.fetchAccountAndCustomer(accNumberField.getText());
						currentBalanceField.setText(form.getCurrentBalance().toString());
						amountField.setText("");
					} else {
						JOptionPane.showMessageDialog(null,  "Failed to deposit!!", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Enter valid input", "Error", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println(amount.toString());
			}
		});
	}
}
