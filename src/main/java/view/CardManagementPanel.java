package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import repository.CardRepository;
import model.Customer;

import java.awt.event.*;
import java.util.List;

public class CardManagementPanel extends JPanel {

	private JTextField txtSearch;
	private JTextField txtAccountNumber;
	private JTextField txtAccountHolder;
	private JTextField txtEmail;
	private JTextField txtPhone;
	private JTable tableCard;
	private DefaultTableModel tableModel;
	private CardRepository cardRepository = new CardRepository();
	private JLabel lblCardNo;

	public CardManagementPanel() {
		setLayout(null);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setPreferredSize(new Dimension(900, 650));

		txtSearch = new JTextField();
		txtSearch.setBounds(340, 7, 200, 18);
		add(txtSearch);

		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String keyword = txtSearch.getText().trim();
				searchCards(keyword);
			}
		});

		JLabel lblAccountNumber_1 = new JLabel("Card Number");
		lblAccountNumber_1.setFont(new Font("Dialog", Font.BOLD, 13));
		lblAccountNumber_1.setBounds(70, 50, 146, 18);
		add(lblAccountNumber_1);

		lblCardNo = new JLabel(generateCardNumber());
		lblCardNo.setFont(new Font("Dialog", Font.BOLD, 13));
		lblCardNo.setBounds(200, 50, 200, 18);
		add(lblCardNo);

		JLabel lblAccountNumber = new JLabel("Account Number");
		lblAccountNumber.setFont(new Font("Dialog", Font.BOLD, 13));
		lblAccountNumber.setBounds(70, 90, 146, 18);
		add(lblAccountNumber);

		txtAccountNumber = new JTextField();
		txtAccountNumber.setFont(new Font("Dialog", Font.BOLD, 13));
		txtAccountNumber.setBounds(200, 90, 260, 20);
		add(txtAccountNumber);

		txtAccountNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					fetchCustomerData();
				}
			}
		});

		JLabel lblAccountHolder = new JLabel("Account Holder");
		lblAccountHolder.setFont(new Font("Dialog", Font.BOLD, 13));
		lblAccountHolder.setBounds(70, 130, 146, 18);
		add(lblAccountHolder);

		txtAccountHolder = new JTextField();
		txtAccountHolder.setFont(new Font("Dialog", Font.BOLD, 13));
		txtAccountHolder.setBounds(200, 130, 260, 20);
		add(txtAccountHolder);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Dialog", Font.BOLD, 13));
		lblEmail.setBounds(70, 170, 146, 18);
		add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Dialog", Font.BOLD, 13));
		txtEmail.setBounds(200, 170, 260, 20);
		add(txtEmail);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Dialog", Font.BOLD, 13));
		lblPhone.setBounds(70, 210, 146, 18);
		add(lblPhone);

		txtPhone = new JTextField();
		txtPhone.setFont(new Font("Dialog", Font.BOLD, 13));
		txtPhone.setBounds(200, 210, 260, 20);
		add(txtPhone);

		JComboBox<String> cmbCardType = new JComboBox<>(new String[] { "Select type", "CREDIT", "DEBIT" });
		cmbCardType.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		cmbCardType.setBounds(200, 250, 260, 20);
		add(cmbCardType);

		JComboBox<String> cmbStatus = new JComboBox<>(new String[] { "Select status", "ACTIVE", "INACTIVE", "EXPIRED" });
		cmbStatus.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		cmbStatus.setBounds(200, 290, 260, 20);
		add(cmbStatus);

		JLabel lblCardType = new JLabel("Card Type");
		lblCardType.setFont(new Font("Dialog", Font.BOLD, 13));
		lblCardType.setBounds(70, 250, 146, 18);
		add(lblCardType);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("Dialog", Font.BOLD, 13));
		lblStatus.setBounds(70, 290, 146, 18);
		add(lblStatus);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> {
			String accountNumber = txtAccountNumber.getText();
			String cardNumber = lblCardNo.getText();
			String cardType = (String) cmbCardType.getSelectedItem();
			String status = (String) cmbStatus.getSelectedItem();

			if (accountNumber.isEmpty() || cardType.isEmpty()) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						"Please fill in all required fields.");
				return;
			}

			try {
				int accountId = cardRepository.findAccountIdByAccountNumber(accountNumber);
				if (accountId == 0) {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Account not found.");
					return;
				}

				boolean success = cardRepository.saveCard(accountId, cardNumber, cardType, status);
				if (success) {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Card saved successfully.");
					loadCardsIntoTable();
					lblCardNo.setText(generateCardNumber()); // generate new card number after save
				} else {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Failed to save card.");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						"An error occurred: " + ex.getMessage());
			}
		});
		btnSave.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnSave.setBounds(200, 330, 112, 27);
		add(btnSave);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(e -> {
			txtAccountNumber.setText("");
			txtAccountHolder.setText("");
			txtEmail.setText("");
			txtPhone.setText("");
			cmbCardType.setSelectedIndex(0);
			cmbStatus.setSelectedIndex(0);
		});
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnClear.setBounds(335, 330, 112, 27);
		add(btnClear);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 396, 860, 200);
		add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 860, 200);
		panel.add(scrollPane);

		String[] columnNames = { "Card Number", "Account Number", "Card Holder", "Email", "Phone", "Card Type",
				"Issued Date", "Expiry Date", "Status" };
		tableModel = new DefaultTableModel(columnNames, 0);
		tableCard = new JTable(tableModel);
		scrollPane.setViewportView(tableCard);

		loadCardsIntoTable();
	}

	private String generateCardNumber() {
		StringBuilder cardNumber = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			int digit = (int) (Math.random() * 10);
			cardNumber.append(digit);
			if ((i + 1) % 4 == 0 && i != 15) {
				cardNumber.append(" ");
			}
		}
		return cardNumber.toString();
	}

	private void loadCardsIntoTable() {
		tableModel.setRowCount(0);

		List<Object[]> cards = cardRepository.findAllCards();
		for (Object[] row : cards) {
			java.sql.Date issuedDate = (java.sql.Date) row[6];
			java.sql.Date expiryDate = (java.sql.Date) row[7];
			tableModel.addRow(new Object[] { row[0], row[1], row[2], row[3], row[4], row[5],
					issuedDate != null ? issuedDate.toLocalDate() : "",
					expiryDate != null ? expiryDate.toLocalDate() : "", row[8] });
		}
	}

	private void searchCards(String keyword) {
		tableModel.setRowCount(0); 

		List<Object[]> cards = cardRepository.searchCards(keyword);
		for (Object[] row : cards) {
			java.sql.Date issuedDate = (java.sql.Date) row[6];
			java.sql.Date expiryDate = (java.sql.Date) row[7];
			tableModel.addRow(new Object[] { row[0], row[1], row[2], row[3], row[4], row[5],
					issuedDate != null ? issuedDate.toLocalDate() : "",
					expiryDate != null ? expiryDate.toLocalDate() : "", row[8] });
		}
	}

	private void fetchCustomerData() {
		String accountNumber = txtAccountNumber.getText();
		if (accountNumber.isEmpty())
			return;

		Customer customer = cardRepository.findCustomerByAccountNumber(accountNumber);
		if (customer != null) {
			txtAccountHolder.setText(customer.getName());
			txtEmail.setText(customer.getEmail());
			txtPhone.setText(customer.getPhone());
		} else {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
					"No customer found for account number: " + accountNumber);
			txtAccountHolder.setText("");
			txtEmail.setText("");
			txtPhone.setText("");
		}
	}
}
