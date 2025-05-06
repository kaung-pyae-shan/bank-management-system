package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Customer;
import repository.CardRepository;

public class CardManagement extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField txtSearch;
	private JTextField txtAccountNumber;
	private JTextField txtAccountHolder;
	private JTextField txtEmail;
	private JTextField txtPhone;
	private DefaultTableModel tableModel;
	private CardRepository cardRepository = new CardRepository();
	private JLabel lblCardNo;
	private JTable table;
	private JPanel topPanel;

	public CardManagement() {

		setLayout(new BorderLayout(0, 20));

		// ==== Search bar panel ====
		JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchBarPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		txtSearch = new JTextField(20);
		txtSearch.setText("Search");
		searchBarPanel.add(txtSearch);
		txtSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtSearch.getText().equals("Search")) {
					txtSearch.setText("");
					txtSearch.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtSearch.getText().isEmpty()) {
					txtSearch.setForeground(Color.GRAY);
					txtSearch.setText("Search");
				}
			}
		});

		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String keyword = txtSearch.getText().trim();
				searchCards(keyword);
			}
		});
		
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(0, 20));

		// == Form ==
		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtAccountNumber = new JTextField(25);
		txtAccountHolder = new JTextField(25);
		txtEmail = new JTextField(25);
		txtPhone = new JTextField(25);

		JComboBox<String> cmbCardType = new JComboBox<>(new String[] { "Select type", "CREDIT", "DEBIT" });
		JComboBox<String> cmbStatus = new JComboBox<>(
				new String[] { "Select status", "ACTIVE", "INACTIVE", "EXPIRED" });

		// Form Label
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(generateLabel("Card Number"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Account Number"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Account Holder"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Email"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Phone"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Card Type"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Status"), gbc);

		// Form TextFields
		gbc.gridx = 1;
		gbc.gridy = 0;
		lblCardNo = new JLabel("9879 1341 8651 8213");
		formPanel.add(lblCardNo, gbc);
		gbc.gridy = 1;
		formPanel.add(txtAccountNumber, gbc);
		gbc.gridy = 2;
		formPanel.add(txtAccountHolder, gbc);
		gbc.gridy = 3;
		formPanel.add(txtEmail, gbc);
		gbc.gridy = 4;
		formPanel.add(txtPhone, gbc);
		gbc.gridy = 5;
		formPanel.add(cmbCardType, gbc);
		gbc.gridy = 6;
		formPanel.add(cmbStatus, gbc);

		txtAccountNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					fetchCustomerData();
				}
			}
		});

		// Action Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

		JButton btnSave = addActionButton("Save", new Color(0x353535));
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

		JButton btnClear = addActionButton("Clear", new Color(0x353535));
		btnClear.addActionListener(e -> {
			txtAccountNumber.setText("");
			txtAccountHolder.setText("");
			txtEmail.setText("");
			txtPhone.setText("");
			cmbCardType.setSelectedIndex(0);
			cmbStatus.setSelectedIndex(0);
		});

		buttonPanel.add(btnSave);
		buttonPanel.add(btnClear);

		gbc.gridy = 7;
		formPanel.add(buttonPanel, gbc);

		topPanel.add(formPanel, BorderLayout.NORTH);
		initTable();

		add(searchBarPanel, BorderLayout.NORTH);
		add(topPanel, BorderLayout.CENTER);
		

		// ===================
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

	private JLabel generateLabel(String label) {
		JLabel formLabel = new JLabel(label + ":");
		formLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		return formLabel;
	}

	private JButton addActionButton(String name, Color hoverColor) {
		JButton button = new JButton(name);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(80, 30)); // Set preferred size

		// Make the button background transparent
		button.setOpaque(true); // Make the button opaque
		button.setContentAreaFilled(true); // Allow to fill the button area
		button.setForeground(Color.BLACK); // Set text color
		button.setBackground(Color.WHITE); // Default color
		button.setFocusPainted(false); // Remove focus border

		// Add hover effect
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setForeground(Color.WHITE);
				button.setBackground(hoverColor); // Change background on hover
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setForeground(Color.BLACK);
				button.setBackground(Color.WHITE); // Change background on hover exit
			}
		});
		return button;
	}
	
	private void initTable() {
		String[] cols = { "Card Number", "Account Number", "Card Holder", "Email", "Phone", "Card Type",
				"Issued Date", "Expiry Date", "Status" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(40);


		JScrollPane tableScrollPane = new JScrollPane(table);
//		tableScrollPane.setPreferredSize(new Dimension(700, 280)); // width, height
		topPanel.add(tableScrollPane, BorderLayout.CENTER);
		loadCardsIntoTable();
	}
}
