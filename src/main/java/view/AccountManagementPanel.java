package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import model.Account;
import model.AccountType;
import model.AccountType.OwnershipType;
import model.AccountType.Type;
import model.Staff;
import repository.AccountRepository;
import utils.AccountNumberGenerator;

public class AccountManagementPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtSearch;
	private JTextField txtBalance;
	private JLabel lblAccountNo;
	private DefaultTableModel tableModel;
	private CardLayout cardLayout;
	private JTable table;
	private JPanel topPanel;
	JPanel accountPanel;
	JPanel accountInfoPanel;
	JPanel centerPanel;

	JComboBox<String> accountTypeCb;
	JComboBox<String> statusCb;
	JComboBox<String> ownershipCb;

	private JPanel customerFieldsPanel;
	private int loggedInStaffId;

	public AccountManagementPanel(int loggedIntStaffId) {
		this.loggedInStaffId = loggedIntStaffId;
		setLayout(new BorderLayout(0, 20));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// ==== Search bar panel ====
		JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchBarPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		JTextField searchField = new JTextField(20);
		searchField.setText("Search by AccNo");
		searchField.setForeground(Color.GRAY);
		searchBarPanel.add(searchField);
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchField.getText().equals("Search by AccNo")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setForeground(Color.GRAY);
					searchField.setText("Search by AccNo");
				}
			}
		});
		searchField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				char c = evt.getKeyChar();
				if (!Character.isDigit(c) && c != '\b') {
					evt.consume(); // Only allow digits and backspace
				}
			}

			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				filterTableByAccountNumber(searchField.getText().trim());
			}
		});
		// ==== Top panel (Form + Account Info) ====
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		// == Form ==
		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtBalance = new JTextField(25);
		txtBalance.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				char c = evt.getKeyChar();
				if (!(Character.isDigit(c) || c == '.' || c == '\b')) {
					evt.consume(); // Only allow digits, decimal point, and backspace
				}

				// Allow only one decimal point
				if (c == '.' && txtBalance.getText().contains(".")) {
					evt.consume();
				}
			}
		});
		
		ItemListener generateListener = e -> {
            // Only react to selection events
            if (e.getStateChange() == ItemEvent.SELECTED) {
                generateAccountNumberIfReady();
            }
        };

		JPanel ownerInnerPanel = new JPanel();
		ownerInnerPanel.setLayout(new BoxLayout(ownerInnerPanel, BoxLayout.X_AXIS));

		ownershipCb = new JComboBox<>();
		ownershipCb.addItem("Select Ownership");
		ownershipCb.addItem("SINGLE");
		ownershipCb.addItem("JOINT");

		ownershipCb.setPreferredSize(new Dimension(220, 30));

		JButton btnAdd = addActionButton("Add", new Color(0x353535));
		btnAdd.setEnabled(false);
		btnAdd.setPreferredSize(new Dimension(80, 30));

		btnAdd.addActionListener(e -> {
			String ownership = (String) ownershipCb.getSelectedItem();
			if ("JOINT".equals(ownership)) {
				int currentFields = customerFieldsPanel.getComponentCount();
				if (currentFields < 5) {
					addCustomerIdField();
				} else {
					JOptionPane.showMessageDialog(this, "Maximum of 5 Customer ID fields allowed.", "Limit Reached",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Add is only available for JOINT ownership.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		ownerInnerPanel.add(ownershipCb);
		ownerInnerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		ownerInnerPanel.add(btnAdd);

		ownershipCb.addActionListener(e -> {
			String selected = (String) ownershipCb.getSelectedItem();
			btnAdd.setEnabled("JOINT".equals(selected));
		});

		accountTypeCb = new JComboBox<>();
		accountTypeCb.addItem("Select Type");
		accountTypeCb.addItem("SAVING");
		accountTypeCb.addItem("FIXED_90D");
		accountTypeCb.addItem("FIXED_180D");
		accountTypeCb.addItem("FIXED_360D");
		accountTypeCb.addItem("FIXED_720D");
		accountTypeCb.addItem("FIXED_1080D");
		
		accountTypeCb.addItemListener(generateListener);
		ownershipCb.addItemListener(generateListener);

		statusCb = new JComboBox<>();
		statusCb.addItem("Select Status");
		statusCb.addItem("ACTIVE");
		statusCb.addItem("INACTIVE");
		statusCb.addItem("EXPIRED");
		
		statusCb.setSelectedItem("ACTIVE");
		//statusCb.setEnabled(false);

		// Form Labels and Fields
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(generateLabel("Account Number"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Account Type"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Balance"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Status"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Ownership Type"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		lblAccountNo = generateLabel("");
		formPanel.add(lblAccountNo, gbc);
		gbc.gridy = 1;
		formPanel.add(accountTypeCb, gbc);
		gbc.gridy = 2;
		formPanel.add(txtBalance, gbc);
		gbc.gridy = 3;
		formPanel.add(statusCb, gbc);
		gbc.gridy = 4;
		formPanel.add(ownerInnerPanel, gbc);

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton btnSave = addActionButton("Save", new Color(0x353535));
		btnSave.addActionListener(e -> saveAccount());

		JButton btnUpdate = addActionButton("Update", new Color(0x353535));
		btnUpdate.addActionListener(e -> updateAccount());

		JButton btnClear = addActionButton("Clear", new Color(0x353535));
		btnClear.addActionListener(e -> Clear());

		buttonPanel.add(btnSave);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnClear);

		gbc.gridy = 6;
		formPanel.add(buttonPanel, gbc);

		// == Account Details Panel ==
		cardLayout = new CardLayout();
		accountPanel = new JPanel(cardLayout);

		JPanel emptyPanel = new JPanel(new BorderLayout());
		customerFieldsPanel = new JPanel();
		customerFieldsPanel.setLayout(new BoxLayout(customerFieldsPanel, BoxLayout.Y_AXIS));
		emptyPanel.add(customerFieldsPanel, BorderLayout.NORTH);

		addCustomerIdField(); // add one field by default

		accountPanel.add(emptyPanel, "empty");
		cardLayout.show(accountPanel, "empty");

		topPanel.add(formPanel);
		topPanel.add(Box.createRigidArea(new Dimension(40, 0))); // spacing between form and panel
		topPanel.add(accountPanel);

		centerPanel = new JPanel(new BorderLayout(0, 20));

		add(searchBarPanel, BorderLayout.NORTH);
		centerPanel.add(topPanel, BorderLayout.NORTH);
		initTable();
		loadAccountsIntoTable();
		add(centerPanel, BorderLayout.CENTER);
	}

	private JLabel generateLabel(String label) {
		JLabel formLabel = new JLabel(label + ":");
		formLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		return formLabel;
	}

	private void addCustomerIdField() {
		JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("Customer ID:");
		JTextField textField = new JTextField(15);

		// Add a DocumentFilter to allow only numeric input
		((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				if (string.matches("[0-9]*")) { // Allow only digits
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if (text.matches("[0-9]*")) { // Allow only digits
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		rowPanel.add(label);
		rowPanel.add(textField);

		customerFieldsPanel.add(rowPanel);
		customerFieldsPanel.revalidate();
		customerFieldsPanel.repaint();
	}

	private void initTable() {
		String[] cols = { "Account Number", "Account Type", "Ownership Type", "Balance", "Number of Customer",
				"Status" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		table = new JTable(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(40);

		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(700, 200));
		centerPanel.add(tableScrollPane, BorderLayout.CENTER);

		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
				populateFormFromSelectedRow(table.getSelectedRow());
			}
		});

	}

	private JButton addActionButton(String name, Color hoverColor) {
		JButton button = new JButton(name);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(80, 30));
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		button.setFocusPainted(false);

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setForeground(Color.WHITE);
				button.setBackground(hoverColor);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setForeground(Color.BLACK);
				button.setBackground(Color.WHITE);
			}
		});

		return button;
	}

	private void Clear() {
		// Reset dropdowns to the first item (i.e., default selection)
		accountTypeCb.setSelectedIndex(0);
		statusCb.setSelectedIndex(0);
		ownershipCb.setSelectedIndex(0);

		// Reset text fields
		txtBalance.setText("");

		// Reset label
		lblAccountNo.setText("110100000006"); // or any placeholder like "Auto-generated"

		// Clear all added Customer ID fields and keep only the default one
		customerFieldsPanel.removeAll(); // Remove all customer ID fields
		addCustomerIdField(); // Add back the default customer ID field

		// Revalidate and repaint the panel to reflect changes
		customerFieldsPanel.revalidate();
		customerFieldsPanel.repaint();
	}

	private void saveAccount() {
		// Validate required fields
		String typeStr = (String) accountTypeCb.getSelectedItem();
		String statusStr = (String) statusCb.getSelectedItem();
		String ownershipStr = (String) ownershipCb.getSelectedItem();
		String balanceStr = txtBalance.getText().trim();

		if ("Select Type".equals(typeStr) || "Select Status".equals(statusStr)
				|| "Select Ownership".equals(ownershipStr) || balanceStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		BigDecimal balance;
		try {
			balance = new BigDecimal(balanceStr);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid balance amount.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Collect customer IDs
		List<Integer> customerIds = new java.util.ArrayList<>();
		for (Component comp : customerFieldsPanel.getComponents()) {
			if (comp instanceof JPanel row) {
				for (Component inner : row.getComponents()) {
					if (inner instanceof JTextField tf) {
						try {
							int id = Integer.parseInt(tf.getText().trim());
							customerIds.add(id);
						} catch (NumberFormatException ignored) {
							JOptionPane.showMessageDialog(this, "Invalid Customer ID. Please enter numbers only.",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}
			}
		}

		if (customerIds.isEmpty()) {
			JOptionPane.showMessageDialog(this, "At least one Customer ID is required.", "Validation Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Determine account_type_id based on the combination of account type and
		// ownership type
		int accountTypeId = getAccountTypeId(typeStr, ownershipStr);

		if (accountTypeId == -1) {
			JOptionPane.showMessageDialog(this, "Invalid account type or ownership type combination.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		

		// Build Account object
		Account account = new Account();
		account.setAccountNumber(lblAccountNo.getText().trim());
		account.setBalance(balance);
		account.setStatus(Account.AccountStatus.valueOf(statusStr));
		account.setOwnershipType(AccountType.OwnershipType.valueOf(ownershipStr));
		account.setCreatedAt(java.time.LocalDateTime.now());
		
		// Dummy staff for now — replace with logged-in staff
		Staff dummyStaff = new Staff();
		dummyStaff.setId(loggedInStaffId);
		account.setCreatedBy(dummyStaff);
		
		// Setup accountType
		AccountType accType = new AccountType();
		accType.setId(accountTypeId); // Set the correct account type ID
		account.setAccountType(accType);

		AccountRepository repo = new AccountRepository();
		int result = repo.insertAccount(account, customerIds);
		if (result != -1) {
			JOptionPane.showMessageDialog(this, "Account saved successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			Clear(); // clear fields
			refreshTable();
		} else {
			JOptionPane.showMessageDialog(this, "Failed to save account.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void generateAccountNumberIfReady() {
        Object accountTypeSelection = accountTypeCb.getSelectedItem();
        Object ownershipTypeSelection = ownershipCb.getSelectedItem();
        // Ensure both have valid selections (not the placeholder)
        if (accountTypeSelection != null && ownershipTypeSelection != null &&
            !"Select Type".equals(accountTypeSelection) && !"Select Ownership".equals(ownershipTypeSelection)) {
            // Call your AccountNumberGenerator utility, for example:
            String generatedAccountNumber = AccountNumberGenerator.generateNewAccountNumber(Type.valueOf(accountTypeSelection.toString()), OwnershipType.valueOf(ownershipTypeSelection.toString()));
            lblAccountNo.setText(generatedAccountNumber);
        } else {
            // Clear account number if either selection is invalid
            lblAccountNo.setText("");
        }
    }

	// Method to return account_type_id based on the account type and ownership type
	private int getAccountTypeId(String typeStr, String ownershipStr) {
		if ("SAVING".equals(typeStr)) {
			return "SINGLE".equals(ownershipStr) ? 1 : ("JOINT".equals(ownershipStr) ? 2 : -1);
		} else if ("FIXED_90D".equals(typeStr)) {
			return "SINGLE".equals(ownershipStr) ? 3 : ("JOINT".equals(ownershipStr) ? 4 : -1);
		} else if ("FIXED_180D".equals(typeStr)) {
			return "SINGLE".equals(ownershipStr) ? 5 : ("JOINT".equals(ownershipStr) ? 6 : -1);
		} else if ("FIXED_360D".equals(typeStr)) {
			return "SINGLE".equals(ownershipStr) ? 7 : ("JOINT".equals(ownershipStr) ? 8 : -1);
		} else if ("FIXED_720D".equals(typeStr)) {
			return "SINGLE".equals(ownershipStr) ? 9 : ("JOINT".equals(ownershipStr) ? 10 : -1);
		} else if ("FIXED_1080D".equals(typeStr)) {
			return "SINGLE".equals(ownershipStr) ? 11 : ("JOINT".equals(ownershipStr) ? 12 : -1);
		}
		return -1; // Invalid combination
	}

	private void loadAccountsIntoTable() {
		AccountRepository accountRepository = new AccountRepository();
		List<Account> accounts = accountRepository.getAllAccounts();

		// Clear existing data in table
		tableModel.setRowCount(0);

		// Add rows to the tableModel
		for (Account account : accounts) {
			AccountType accountType = account.getAccountType();
			Object[] row = new Object[] { account.getAccountNumber(), accountType.getType(), // AccountType Type (e.g.,
																								// SAVING, FIXED_90D,
																								// etc.)
					accountType.getOwnershipType(), // AccountType Ownership (e.g., SINGLE, JOINT)
					account.getBalance(),
//	            accountType.getMinimumBalance(),  // AccountType minimum balance
					accountRepository.countCustomersByAccount(account.getId()), account.getStatus() };
			tableModel.addRow(row);
		}
	}

	private void filterTableByAccountNumber(String searchText) {
		// Reset table to initial state
		AccountRepository accountRepository = new AccountRepository();
		List<Account> accounts = accountRepository.getAllAccounts();

		// Filter the accounts based on the search text
		List<Account> filteredAccounts = accounts.stream()
				.filter(account -> account.getAccountNumber().contains(searchText)).toList();

		// Clear existing rows
		tableModel.setRowCount(0);

		// Add the filtered accounts to the table
		for (Account account : filteredAccounts) {
			AccountType accountType = account.getAccountType();
			Object[] row = new Object[] { account.getAccountNumber(), accountType.getType(),
					accountType.getOwnershipType(), account.getBalance(),
					accountRepository.countCustomersByAccount(account.getId()), account.getStatus() };
			tableModel.addRow(row);
		}
	}

	private void populateFormFromSelectedRow(int rowIndex) {
		String accNumber = table.getValueAt(rowIndex, 0).toString();
		String accType = table.getValueAt(rowIndex, 1).toString();
		String ownership = table.getValueAt(rowIndex, 2).toString();
		String balance = table.getValueAt(rowIndex, 3).toString();
		String status = table.getValueAt(rowIndex, 5).toString();

		lblAccountNo.setText(accNumber);
		txtBalance.setText(balance);
		accountTypeCb.setSelectedItem(accType);
		ownershipCb.setSelectedItem(ownership);
		statusCb.setSelectedItem(status);

		lblAccountNo.setEnabled(false); // Disable editing

		// Clear previous customer fields
		customerFieldsPanel.removeAll();

		// Fetch customer IDs for the selected account number
		AccountRepository repo = new AccountRepository();
		List<Integer> customerIds = repo.getCustomerIdsByAccountNumber(accNumber); // You must implement this method

		for (Integer customerId : customerIds) {
			JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel label = new JLabel("Customer ID:");
			JTextField textField = new JTextField(15);
			textField.setText(customerId.toString());
			rowPanel.add(label);
			rowPanel.add(textField);
			customerFieldsPanel.add(rowPanel);
		}

		// Refresh panel
		customerFieldsPanel.revalidate();
		customerFieldsPanel.repaint();
	}

	private void updateAccount() {
		String accNumber = lblAccountNo.getText().trim();
		String typeStr = (String) accountTypeCb.getSelectedItem();
		String statusStr = (String) statusCb.getSelectedItem();
		String ownershipStr = (String) ownershipCb.getSelectedItem();
		String balanceStr = txtBalance.getText().trim();

		if ("Select Type".equals(typeStr) || "Select Status".equals(statusStr)
				|| "Select Ownership".equals(ownershipStr) || balanceStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		BigDecimal balance;
		try {
			balance = new BigDecimal(balanceStr);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid balance amount.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<Integer> customerIds = new java.util.ArrayList<>();
		for (Component comp : customerFieldsPanel.getComponents()) {
			if (comp instanceof JPanel row) {
				for (Component inner : row.getComponents()) {
					if (inner instanceof JTextField tf) {
						try {
							int id = Integer.parseInt(tf.getText().trim());
							customerIds.add(id);
						} catch (NumberFormatException ignored) {
							JOptionPane.showMessageDialog(this, "Invalid Customer ID. Please enter numbers only.",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}
			}
		}

		if (customerIds.isEmpty()) {
			JOptionPane.showMessageDialog(this, "At least one Customer ID is required.", "Validation Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int accountTypeId = getAccountTypeId(typeStr, ownershipStr);
		if (accountTypeId == -1) {
			JOptionPane.showMessageDialog(this, "Invalid account type or ownership type combination.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Account updatedAccount = new Account();
		updatedAccount.setAccountNumber(accNumber);
		updatedAccount.setBalance(balance);
		updatedAccount.setStatus(Account.AccountStatus.valueOf(statusStr));
		updatedAccount.setOwnershipType(AccountType.OwnershipType.valueOf(ownershipStr));
		updatedAccount.setCreatedAt(java.time.LocalDateTime.now()); // or createdAt can be fetched from DB

		// Dummy staff — should be set to the actual logged-in staff
		Staff dummyStaff = new Staff();
		dummyStaff.setId(1);
		updatedAccount.setCreatedBy(dummyStaff);

		AccountType accType = new AccountType();
		accType.setId(accountTypeId);
		updatedAccount.setAccountType(accType);

		AccountRepository repo = new AccountRepository();
		boolean result = repo.updateAccount(updatedAccount, customerIds);
		if (result) {
			JOptionPane.showMessageDialog(this, "Account updated successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			Clear();
			loadAccountsIntoTable(); // refresh the table
			refreshTable();
		} else {
			JOptionPane.showMessageDialog(this, "Failed to update account.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void refreshTable() {
		AccountRepository accountRepository = new AccountRepository();
		List<Account> accounts = accountRepository.getAllAccounts();

		// Clear existing rows in the table model
		tableModel.setRowCount(0);

		// Add fresh rows to the table model
		for (Account account : accounts) {
			AccountType accountType = account.getAccountType();
			Object[] row = new Object[] { account.getAccountNumber(), accountType.getType(),
					accountType.getOwnershipType(), account.getBalance(),
					accountRepository.countCustomersByAccount(account.getId()), account.getStatus() };
			tableModel.addRow(row);
		}
	}
}
