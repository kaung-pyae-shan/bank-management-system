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

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.TransactionController;
import model.dto.DepositWithdrawForm;

public class TransferPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField fromAccNumberField;
	private JTextField fromAccHolderField;
	private JTextField currentBalanceField;
	private JTextField toAccNumberField;
	private JTextField toAccHolderField;
	private JTextField amountField;

	private DepositWithdrawForm form;
	private int fromAccountId;
	private int toAccountId;

	/**
	 * Create the panel.
	 */
	public TransferPanel(TransactionController controller) {
		// ==== Form Panel ====
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.anchor = GridBagConstraints.WEST;
		
		// init textfields;
		fromAccNumberField = new JTextField(25);
		fromAccHolderField = new JTextField(25);
		currentBalanceField = new JTextField(25);
		toAccNumberField = new JTextField(25);
		toAccHolderField = new JTextField(25);
		amountField = new JTextField(25);

		// Add form labels
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(generateLabel("From Account"), gbc);
		
		gbc.gridy++;
		add(generateLabel("Account Holder"), gbc);
		
		gbc.gridy++;
		add(generateLabel("Current Balance"), gbc);
		
		gbc.insets = new Insets(30, 10, 13, 10);
		gbc.gridy++;
		add(generateLabel("To Account"), gbc);
		
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.gridy++;
		add(generateLabel("Account Holder"), gbc);
		
		gbc.insets = new Insets(30, 10, 13, 10);
		gbc.gridy++;
		add(generateLabel("Transfer Amount"), gbc);
		
		// Add Form TextFields
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(13, 10, 13, 10);
		add(fromAccNumberField, gbc);
		
		gbc.gridy++;
		fromAccHolderField.setEditable(false);
		add(fromAccHolderField, gbc);
		
		gbc.gridy++;
		currentBalanceField.setEditable(false);
		add(currentBalanceField, gbc);
		
		gbc.gridy++;
		gbc.insets = new Insets(30, 10, 13, 10);
		add(toAccNumberField, gbc);
		
		gbc.gridy++;
		gbc.insets = new Insets(13, 10, 13, 10);
		toAccHolderField.setEditable(false);
		add(toAccHolderField, gbc);
		
		gbc.gridy++;
		gbc.insets = new Insets(30, 10, 13, 10);
		add(amountField, gbc);
		
		JButton actionButton = new JButton("Transfer");
		actionButton.setForeground(Color.white);
		actionButton.setBackground(new Color(0xe0162b));
		actionButton.setFocusPainted(false);
		actionButton.setPreferredSize(new Dimension(100, 30));
		gbc.gridx = 1;
		gbc.gridy++;
		gbc.insets = new Insets(5, 10, 10, 10);
		add(actionButton, gbc);

		// Push all form contents to top by adding glue
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		add(Box.createVerticalGlue(), gbc);
		
		
		fromAccNumberField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String accountNumber = fromAccNumberField.getText();
				form = controller.fetchAccountAndCustomer(accountNumber);
				if(null != form) {
					fromAccHolderField.setText(form.getAccountHolder());
					currentBalanceField.setText(form.getCurrentBalance().toString());
					fromAccountId = form.getAccountId();
				} else {
					toAccHolderField.setText("");
					currentBalanceField.setText("");
					fromAccountId = 0;
				}
			}
		});
		
		toAccNumberField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String accountNumber = toAccNumberField.getText();
				form = controller.fetchAccountAndCustomer(accountNumber);
				if(null != form) {
					toAccHolderField.setText(form.getAccountHolder());
					toAccountId = form.getAccountId();
				} else {
					toAccHolderField.setText("");
					currentBalanceField.setText("");
					toAccountId = 0;
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
					int row = controller.transfer(fromAccountId, toAccountId, amount);
					if (row > 0) {
						JOptionPane.showMessageDialog(null,  "Transferred successfully!!", "Success", JOptionPane.INFORMATION_MESSAGE);
						form = controller.fetchAccountAndCustomer(fromAccNumberField.getText());
						currentBalanceField.setText(form.getCurrentBalance().toString());
						amountField.setText("");
					} else {
						JOptionPane.showMessageDialog(null,  "Failed to transfer!!", "Failed", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Enter valid input", "Error", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println(amount.toString());
			}
		});
	}
	
	private JLabel generateLabel(String label) {
		JLabel formLabel = new JLabel(label + ":");
		formLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		return formLabel;
	}

}
