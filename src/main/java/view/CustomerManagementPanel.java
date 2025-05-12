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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.Period;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import controller.CustomerController;
import model.Customer;
import model.dto.AccNumberType;
import utils.NRCHelper;

public class CustomerManagementPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private CustomerController controller;
	private CardLayout cardLayout;
	private String stateCode;
	private String regionCode;
	private String nrcType;
	private LocalDate dob;

	private List<Customer> customers;
	int selectedCustomerId;

	JTextField nameField;
	JTextField emailField;
	JTextField phoneField;
	JTextField addressField;
	JDateChooser datePicker;

	JComboBox<String> stateCodeCb;
	JComboBox<String> regionCodeCb;
	JComboBox<String> nrcTypeCb;
	JTextField nrcNumberField;
	JPanel accountPanel;
	JPanel accountInfoPanel;
	JPanel centerPanel;
	JLabel numAccountValueLbl;

	DefaultTableModel tableModel;
	JTable table;

	/*
	 * BorderLayout There are three panels: searchBarPanel, TopPanel, and
	 * tableScrollPane searchBarPanel -> FlowLayout {JTextField} topPanel ->
	 * GridLayout {formPanel, accountPanel} formPanel -> GridBagLayout {Labels,
	 * TextFields, Combobox, Date Picker(JCalendar} accountPanel -> GridBagLayout
	 * {Labels} tableScrollPane -> {JTable}
	 */

	public CustomerManagementPanel(CustomerController controller, int loggedInStaffId) {
		this.controller = controller;
		setLayout(new BorderLayout(0, 20));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// ==== Search bar panel ====
		JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchBarPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		JTextField searchField = new JTextField(20);
		searchField.setText("Search by CustomerID and NRC");
		searchBarPanel.add(searchField);
		searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search by CustomerID and NRC")) {
                	searchField.setText("");
                	searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                	searchField.setForeground(Color.GRAY);
                	searchField.setText("Search by CustomerID and NRC");
                }
            }
        });

		// ==== Top panel (Form + Account Info) ====
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		// == Form ==
		JPanel formPanel = new JPanel(new GridBagLayout());
//		formPanel.setPreferredSize(new Dimension(400, 320)); // Set preferred size
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		nameField = new JTextField(25);
		emailField = new JTextField(25);
		phoneField = new JTextField(25);
		addressField = new JTextField(25);
		
		nameField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();

				// Allow only letters (A-Z, a-z) and space
				if (!Character.isLetter(c) && !Character.isWhitespace(c) && !Character.isISOControl(c)) {
					e.consume(); // block anything that's not a letter, space, or control character
				}
			}
		});
		phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				String currentText = phoneField.getText();

				// Allow backspace and delete
				if (Character.isISOControl(c)) {
					return;
				}

				// Only allow digits
				if (!Character.isDigit(c)) {
					e.consume();
					return;
				}

				// Limit to 11 digits
				if (currentText.length() >= 11) {
					e.consume();
				}
			}
		});

		JPanel nrcInnerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints innerGbc = new GridBagConstraints();
		innerGbc.insets = new Insets(0, 1, 0, 1);
		innerGbc.weightx = 0;
		innerGbc.gridy = 0;
		innerGbc.fill = GridBagConstraints.HORIZONTAL;

		// 12/
		stateCodeCb = new JComboBox<String>();
		for (int i = 1; i <= 14; i++) {
			stateCodeCb.addItem(Integer.toString(i) + "/");
		}
		// TaMaNa
		regionCodeCb = new JComboBox<String>();
		List<String> regions = new NRCHelper().getRegions("1/");
		for (String region : regions) {
			regionCodeCb.addItem(region);
		}
		// N
		nrcTypeCb = new JComboBox<String>();
		nrcTypeCb.addItem("N");
		nrcTypeCb.addItem("E");
		nrcTypeCb.addItem("P");
		nrcTypeCb.addItem("T");
		nrcTypeCb.addItem("R");
		nrcTypeCb.addItem("S");
		// NRC number
		nrcNumberField = new JTextField(7);

		innerGbc.gridx = 0;
		innerGbc.gridy = 0;
		nrcInnerPanel.add(stateCodeCb, innerGbc);
		innerGbc.gridx++;
		nrcInnerPanel.add(regionCodeCb, innerGbc);
		innerGbc.gridx++;
		nrcInnerPanel.add(nrcTypeCb, innerGbc);
		innerGbc.weightx = 1.0;
		innerGbc.gridx++;
		nrcInnerPanel.add(nrcNumberField, innerGbc);

		// Date Picker for dob
		datePicker = new JDateChooser();
		datePicker.setPreferredSize(new Dimension(255, 25));

		// Form Label
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(generateLabel("Name"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("NRC"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Date of Birth"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Email"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Phone"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Address"), gbc);

		// Form TextFields
		gbc.gridx = 1;
		gbc.gridy = 0;
		formPanel.add(nameField, gbc);
		gbc.gridy = 1;
		formPanel.add(nrcInnerPanel, gbc);
		gbc.gridy = 2;
		formPanel.add(datePicker, gbc);
		gbc.gridy = 3;
		formPanel.add(emailField, gbc);
		gbc.gridy = 4;
		formPanel.add(phoneField, gbc);
		gbc.gridy = 5;
		formPanel.add(addressField, gbc);

		// Action Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

		JButton btnSave = addActionButton("Save", new Color(0x353535));
		JButton btnUpdate = addActionButton("Update", new Color(0x353535));
		JButton btnClear = addActionButton("Clear", new Color(0x353535));

		buttonPanel.add(btnSave);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnClear);

		gbc.gridy = 6;
		formPanel.add(buttonPanel, gbc);

		// combo box listener
		stateCodeCb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) stateCodeCb.getSelectedItem();
				stateCode = selectedItem;
				regionCodeCb.removeAllItems();
				List<String> regions = new NRCHelper().getRegions(stateCode);
				for (String region : regions) {
					regionCodeCb.addItem(region);
				}
			}
		});

		regionCodeCb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) regionCodeCb.getSelectedItem();
				regionCode = selectedItem;
			}
		});

		nrcTypeCb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) nrcTypeCb.getSelectedItem();
				nrcType = selectedItem;
			}
		});
		nrcNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				String currentText = nrcNumberField.getText();

				// Allow backspace and delete
				if (Character.isISOControl(c)) {
					return;
				}

				// Only allow digits
				if (!Character.isDigit(c)) {
					e.consume();
					return;
				}

				// Limit to 11 digits
				if (currentText.length() >= 6) {
					e.consume();
				}
			}
		});

		// == Account Details Panel ==
		cardLayout = new CardLayout();
		accountPanel = new JPanel(cardLayout);
//		accountPanel.setPreferredSize(new Dimension(300, 300));
		JPanel emptyPanel = new JPanel(new BorderLayout());
		JLabel emptyLabel = new JLabel("No customer is selected!", SwingConstants.CENTER);
		emptyPanel.add(emptyLabel, BorderLayout.CENTER);
		accountInfoPanel = new JPanel(new GridBagLayout());
		JScrollPane accountInfoScroll = new JScrollPane(accountInfoPanel);
		accountInfoScroll.setBorder(BorderFactory.createEmptyBorder());
		accountInfoScroll.setPreferredSize(new Dimension(300, 300));

		accountPanel.add(emptyPanel, "empty");
		accountPanel.add(accountInfoScroll, "details");
		cardLayout.show(accountPanel, "empty");

		topPanel.add(formPanel);
		topPanel.add(accountPanel);
		
		centerPanel = new JPanel(new BorderLayout(0, 20));

		add(searchBarPanel, BorderLayout.NORTH);
		centerPanel.add(topPanel, BorderLayout.NORTH);
		initTable();
		add(centerPanel, BorderLayout.CENTER);
//		add(topPanel, BorderLayout.CENTER);
//		add(tableScrollPane, BorderLayout.SOUTH);
		refreshTable(controller.fetchCustomers());

		datePicker.addPropertyChangeListener("date", new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
		        if ("date".equals(evt.getPropertyName())) {
		            Date selectedDate = datePicker.getDate();
		            if (selectedDate != null) {
		                dob = selectedDate.toInstant()
		                                  .atZone(ZoneId.systemDefault())
		                                  .toLocalDate();

		                // Check if the person is at least 18 years old
		                if (dob != null && !isAdult(dob)) {
		                    JOptionPane.showMessageDialog(null, "You must be at least 18 years old to proceed.", "Age Restriction", JOptionPane.WARNING_MESSAGE);
		                    // Optionally, reset the date picker or handle accordingly
		                    datePicker.setDate(null);
		                    dob = null;
		                }
		            } else {
		                // User cleared the date, so clear our dob variable too
		                dob = null;
		            }
		        }
		    }

		    private boolean isAdult(LocalDate dob) {
		        // Calculate the age from the date of birth
		        LocalDate currentDate = LocalDate.now();
		        int age = Period.between(dob, currentDate).getYears();
		        return age >= 18;
		    }
		});
		
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				searchData(searchField.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				searchData(searchField.getText());			
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				searchData(searchField.getText());
			}
			private void searchData(String nrc) {
				if(nrc.isBlank() || nrc.equals("Search by CustomerID and NRC")) {
					refreshTable(controller.fetchCustomers());
				} else {
					Customer customer = controller.fetchCustomerByNrc(nrc);
					refreshTable(new ArrayList<Customer>(List.of(customer)));
				}
			}
		});

		btnSave.addActionListener(e -> {
			 String email = emailField.getText();
			    
			    // Validate email format
			    if (!isEmailFormat(email)) {
			        JOptionPane.showMessageDialog(null, "Invalid email format. Please enter a valid email address.", 
			                                      "Invalid Input", JOptionPane.ERROR_MESSAGE);
			        return; // Stop further execution if the email format is invalid
			    }
			Customer customer = new Customer();
			customer.setName(nameField.getText());
			customer.setDob(dob);
			customer.setNrc(stateCode + regionCode + "(" + nrcType + ")" + nrcNumberField.getText());
			customer.setEmail(email);
			customer.setPhone(phoneField.getText());
			customer.setAddress(addressField.getText());
			customer.setStaffId(loggedInStaffId);

			int row = controller.addNewCustomer(customer);
			if(row == 1) {
				JOptionPane.showMessageDialog(null,  "Customer Saved successfully!!", "Success", JOptionPane.INFORMATION_MESSAGE);
				clearForm();
				cardLayout.show(accountPanel, "empty"); // switch back to the blank card
				refreshTable(controller.fetchCustomers());
			}else if (!isEmailFormat(email)) {
				JOptionPane.showMessageDialog(this, "Invalid email format! Example: example@domain.com", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
			else {
				JOptionPane.showMessageDialog(null,  "Failed to save new customer!!", "Failed", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		btnUpdate.addActionListener(e -> {
			 String email = emailField.getText();
			    
			    // Validate email format
			    if (!isEmailFormat(email)) {
			        JOptionPane.showMessageDialog(null, "Invalid email format. Please enter a valid email address.", 
			                                      "Invalid Input", JOptionPane.ERROR_MESSAGE);
			        return; // Stop further execution if the email format is invalid
			    }
			Customer customer = new Customer();
			customer.setId(selectedCustomerId);
			customer.setName(nameField.getText());
			customer.setDob(dob);
			customer.setNrc(stateCode + regionCode + "(" + nrcType + ")" + nrcNumberField.getText());
			customer.setEmail(email);
			customer.setPhone(phoneField.getText());
			customer.setAddress(addressField.getText());
			customer.setStaffId(loggedInStaffId);

			int row = controller.editCustomerById(customer);
			if(row == 1) {
				JOptionPane.showMessageDialog(null,  "Customer Updated successfully!!", "Success", JOptionPane.INFORMATION_MESSAGE);
				clearForm();
				cardLayout.show(accountPanel, "empty"); // switch back to the blank card
				refreshTable(controller.fetchCustomers());
			} else {
				JOptionPane.showMessageDialog(null,  "Failed to update customer!!", "Failed", JOptionPane.ERROR_MESSAGE);
			}
		});

		btnClear.addActionListener(e -> {
			clearForm(); // wipe all inputs
			table.clearSelection(); // optional: un-highlight any selected row
			cardLayout.show(accountPanel, "empty"); // switch back to the blank card
		});
	}
	public static boolean isEmailFormat(String email) {
		if (email == null || email.trim().isEmpty())
			return false;

		int at = email.indexOf("@");
		int dot = email.lastIndexOf(".");

		// Basic checks for position and presence
		if (at < 1 || dot < at + 2 || dot + 2 >= email.length() || email.contains(" ")) {
			return false;
		}

		String domainExtension = email.substring(dot + 1);
		return domainExtension.equalsIgnoreCase("com");
	}

	private void initTable() {
		String[] cols = { "CustomerID", "Name", "NRC", "DOB", "Email", "Phone", "Address", "Since" };
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

		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int viewRow = table.getSelectedRow();
				if (viewRow != -1) {
					int modelRow = table.convertRowIndexToModel(viewRow);
					Customer selected = customers.get(modelRow);
					selectedCustomerId = selected.getId();
					// populate form fields:
					nameField.setText(selected.getName());
					emailField.setText(selected.getEmail());
					phoneField.setText(selected.getPhone());
					addressField.setText(selected.getAddress());
					LocalDate dob = selected.getDob();
					// Step 1: Convert LocalDate to LocalDateTime
					LocalDateTime localDateTime = dob.atStartOfDay(); // Start of the day
					// Step 2: Convert LocalDateTime to ZonedDateTime
					ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault()); // Use system default
																								// time zone
					// Step 3: Convert ZonedDateTime to java.util.Date
					Date utilDate = Date.from(zonedDateTime.toInstant());
					datePicker.setDate(utilDate);
					populateNrcFields(selected.getNrc());
					cardLayout.show(accountPanel, "details");
					populateAccountDetails();
				}
			}
		});
		JScrollPane tableScrollPane = new JScrollPane(table);
//		tableScrollPane.setPreferredSize(new Dimension(700, 280)); // width, height
		centerPanel.add(tableScrollPane, BorderLayout.CENTER);
	}

	private void refreshTable(List<Customer> customerlist) {
		customers = customerlist;
		tableModel.setRowCount(0);
		for (Customer c : customers) {
			tableModel.addRow(new Object[] {c.getId(), c.getName(), c.getNrc(), c.getDob(), c.getEmail(), c.getPhone(),
					c.getAddress(), c.getCreatedAt() });
		}
		tableModel.fireTableDataChanged();
	}

	private void populateAccountDetails() {
		// 1) clear out everything
		accountInfoPanel.removeAll();

		// 2) reset your constraints to row 0
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 20, 5);

		// 3) re-add "Number of Accounts:" and its value
		gbc.gridx = 0;
		gbc.gridy = 0;
		accountInfoPanel.add(new JLabel("Number of Accounts:"), gbc);

		gbc.gridx = 1;
		numAccountValueLbl = new JLabel(Long.toString(controller.fetchNumberOfAccounts(selectedCustomerId)));
		accountInfoPanel.add(numAccountValueLbl, gbc);

		// 4) fetch the dynamic rows
		List<AccNumberType> rows = controller.fetchAccNumberAndType(selectedCustomerId);

		// 5) for each account, add two rows
		for (int i = 0; i < rows.size(); i++) {
			AccNumberType at = rows.get(i);

			// Account Number label
			gbc.gridy = 1 + i * 2;
			gbc.gridx = 0;
			gbc.insets = new Insets(0, 5, 0, 5);
			accountInfoPanel.add(new JLabel("Account Number:"), gbc);

			// Account Number value
			gbc.gridx = 1;
			accountInfoPanel.add(new JLabel(at.accNumber()), gbc);

			// Account Type label
			gbc.gridy = 2 + i * 2;
			gbc.gridx = 0;
			gbc.insets = new Insets(0, 5, 20, 5);
			accountInfoPanel.add(new JLabel("Account Type:"), gbc);

			// Account Type value
			gbc.gridx = 1;
			accountInfoPanel.add(new JLabel(at.accType()), gbc);
		}

		// 6) IMPORTANT: tell Swing to re-layout & repaint
		accountInfoPanel.revalidate();
		accountInfoPanel.repaint();

		// 7) show the “details” card
		cardLayout.show(accountPanel, "details");
	}

	private void populateNrcFields(String fullNrc) {
		// fullNrc looks like "12/BaHaNa(N)108656"
		// 1) Split off the state code (everything up to and including the slash)
		int slash = fullNrc.indexOf('/') + 1;
		String state = fullNrc.substring(0, slash); // "12/"
		String rest = fullNrc.substring(slash); // "BaHaNa(N)108656"

		// 2) Split the region (up to the '(' ) and the rest
		int paren = rest.indexOf('(');
		String region = rest.substring(0, paren); // "BaHaNa"
		String after = rest.substring(paren + 1); // "N)108656"

		// 3) Split type (up to the ')') and the final number
		int closeParen = after.indexOf(')');
		String type = after.substring(0, closeParen); // "N"
		String number = after.substring(closeParen + 1); // "108656"

		// 4) Now set them on your UI!
		// If your state‐combo listener repopulates regions, do that first:
		stateCodeCb.setSelectedItem(state);
		// (this will trigger your ActionListener which calls getRegions(state))
		regionCodeCb.setSelectedItem(region);
		nrcTypeCb.setSelectedItem(type);
		nrcNumberField.setText(number);
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

	private JLabel generateLabel(String label) {
		JLabel formLabel = new JLabel(label + ":");
		formLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		return formLabel;
	}

	private void clearForm() {
		// clear text fields
		nameField.setText("");
		emailField.setText("");
		phoneField.setText("");
		addressField.setText("");

		// clear date picker
		datePicker.setDate(null);

		// reset NRC combo boxes/text
		stateCodeCb.setSelectedIndex(0); // goes back to "1/"
		// repopulate regions for the default state:
		regionCodeCb.removeAllItems();
		for (String r : new NRCHelper().getRegions((String) stateCodeCb.getSelectedItem())) {
			regionCodeCb.addItem(r);
		}
		nrcTypeCb.setSelectedIndex(0); // goes back to "N"
		nrcNumberField.setText("");

		// reset any internal state vars (if you use them):
		selectedCustomerId = -1;
		dob = null; // or "" depending on your logic
		stateCode = null;
		regionCode = null;
		nrcType = null;
	}

}
