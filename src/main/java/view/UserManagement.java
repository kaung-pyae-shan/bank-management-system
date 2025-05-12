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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import repository.UserRepository;

public class UserManagement extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtName;
	private JTextField txtPassword;
	private JTextField txtEmail;
	private JTextField txtPhone;
	private DefaultTableModel tableModel;
	private JComboBox<String> cmbRole;
	private int selectedStaffId = -1;
	private JTextField txtSearch;
	private JTable table;
	private JPanel topPanel;

	public UserManagement() {

		setLayout(new BorderLayout(0, 20));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// ==== Search bar panel ====
		JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchBarPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		txtSearch = new JTextField(20);
		txtSearch.setText("Search by keyword");
		searchBarPanel.add(txtSearch);

		txtSearch.addFocusListener(new FocusAdapter() {
		    public void focusGained(FocusEvent e) {
		        if (txtSearch.getText().equals("Search by keyword")) {
		            txtSearch.setText("");
		            txtSearch.setForeground(Color.BLACK);
		        }
		    }

		    public void focusLost(FocusEvent e) {
		        if (txtSearch.getText().trim().isEmpty()) {
		            txtSearch.setForeground(Color.GRAY);
		            txtSearch.setText("Search by Name");
		            loadStaffData();
		        }
		    }
		});

		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				searchStaff();
			}

			public void removeUpdate(DocumentEvent e) {
				searchStaff();
			}

			public void changedUpdate(DocumentEvent e) {
				searchStaff();
			}
		});
		

		// ==== Top panel (Form + Account Info) ====
		topPanel = new JPanel();
//		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setLayout(new BorderLayout(0, 20));

		// == Form ==
		JPanel formPanel = new JPanel(new GridBagLayout());
//		formPanel.setPreferredSize(new Dimension(400, 320)); // Set preferred size
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtName = new JTextField(25);
		txtPassword = new JTextField(25);
		txtEmail = new JTextField(25);
		txtPhone = new JTextField(25);

		txtName.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();

				// Allow only letters (A-Z, a-z) and space
				if (!Character.isLetter(c) && !Character.isWhitespace(c) && !Character.isISOControl(c)) {
					e.consume(); // block anything that's not a letter, space, or control character
				}
			}
		});

		txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				String currentText = txtPhone.getText();

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

		cmbRole = new JComboBox<>(new DefaultComboBoxModel<>(new String[] { "-Select Role-", "ADMIN", "TELLER" }));

		// Form Label
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(generateLabel("Name"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Password"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Email"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Phone"), gbc);
		gbc.gridy++;
		formPanel.add(generateLabel("Role"), gbc);

		// Form TextFields
		gbc.gridx = 1;
		gbc.gridy = 0;
		formPanel.add(txtName, gbc);
		gbc.gridy++;
		formPanel.add(txtPassword, gbc);
		gbc.gridy++;
		formPanel.add(txtEmail, gbc);
		gbc.gridy++;
		formPanel.add(txtPhone, gbc);
		gbc.gridy++;
		formPanel.add(cmbRole, gbc);

		// Action Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

		JButton btnSave = addActionButton("Save", new Color(0x353535));
		btnSave.addActionListener(e -> saveStaff());

		JButton btnUpdate = addActionButton("Update", new Color(0x353535));
		btnUpdate.addActionListener(e -> updateStaff());

		JButton btnClear = addActionButton("Clear", new Color(0x353535));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});

		buttonPanel.add(btnSave);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnClear);

		gbc.gridy++;
		formPanel.add(buttonPanel, gbc);

		topPanel.add(formPanel, BorderLayout.NORTH);
		initTable();

		add(searchBarPanel, BorderLayout.NORTH);
		add(topPanel, BorderLayout.CENTER);

		// ===============
		loadStaffData();
	}

	// Save Staff
	private void saveStaff() {
		String name = txtName.getText().trim();
		String password = txtPassword.getText().trim();
		String email = txtEmail.getText().trim();
		String phone = txtPhone.getText().trim();
		String role = (String) cmbRole.getSelectedItem();

		if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || role.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (!isEmailFormat(email)) {
			JOptionPane.showMessageDialog(this, "Invalid email format! Example: example@domain.com", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			UserRepository.saveStaff(name, password, email, phone, role);
			loadStaffData();
			clearFields();
		}

	}

	// Update Staff
	private void updateStaff() {
	    if (selectedStaffId == -1) {
	        JOptionPane.showMessageDialog(this, "Please select a staff record to update.");
	        return;
	    }

	    String username = txtName.getText().trim();
	    String email = txtEmail.getText().trim();
	    String phone = txtPhone.getText().trim();
	    String role = (String) cmbRole.getSelectedItem();

	    if (username.isEmpty() || email.isEmpty() || phone.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
	        return;
	    }else if (!isEmailFormat(email)) {
			JOptionPane.showMessageDialog(this, "Invalid email format! Example: example@domain.com", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		} 

	    try {
	        boolean success = UserRepository.updateStaff(selectedStaffId, username, email, phone, role);
	        if (success) {
	            JOptionPane.showMessageDialog(this, "Staff updated successfully.");
	            clearFields();
	            loadStaffData();
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to update staff.");
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, "Error updating staff: " + e.getMessage());
	        e.printStackTrace(); // Also log for debugging
	    }
	}

	private void loadStaffData() {
	    tableModel.setRowCount(0);
	    try (ResultSet rs = UserRepository.getAllStaff()) {
	        while (rs.next()) {
	            tableModel.addRow(new Object[] {
	                rs.getInt("staff_id"),
	                rs.getString("username"),
	                rs.getString("email"),
	                rs.getString("phone"),
	                rs.getString("role")
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void clearFields() {
	    txtName.setText("");
	    txtPassword.setText("");
	    txtPassword.setEditable(true); // Re-enable for new entry
	    txtEmail.setText("");
	    txtPhone.setText("");
	    cmbRole.setSelectedIndex(0);
	    selectedStaffId = -1;
	    txtSearch.setText("Search by keyword");
	    loadStaffData();
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

	private void fillFormFromSelectedRow() {
	    int selectedRow = table.getSelectedRow();
	    if (selectedRow != -1) {
	        selectedStaffId = (int) tableModel.getValueAt(selectedRow, 0);
	        txtName.setText((String) tableModel.getValueAt(selectedRow, 1));
	        txtEmail.setText((String) tableModel.getValueAt(selectedRow, 2));
	        txtPhone.setText((String) tableModel.getValueAt(selectedRow, 3));
	        cmbRole.setSelectedItem((String) tableModel.getValueAt(selectedRow, 4));
	        txtPassword.setText("");  // Clear it instead
	        txtPassword.setEditable(false); // Disable password field
	    }
	}


	private void searchStaff() {
	    String keyword = txtSearch.getText().trim();
	    if (keyword.equals("Search") || keyword.isEmpty()) {
	        loadStaffData();
	        return;
	    }

	    tableModel.setRowCount(0);
	    try (ResultSet rs = UserRepository.searchStaff(keyword)) {
	        while (rs.next()) {
	            tableModel.addRow(new Object[] {
	                rs.getInt("staff_id"),
	                rs.getString("username"),
	                rs.getString("email"),
	                rs.getString("phone"),
	                rs.getString("role")
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
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
	    String[] cols = { "StaffId", "Username", "Email", "Phone", "Role" }; // Removed "Password"
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
	            fillFormFromSelectedRow();
	        }
	    });

	    JScrollPane tableScrollPane = new JScrollPane(table);
	    topPanel.add(tableScrollPane, BorderLayout.CENTER);
	}

}
