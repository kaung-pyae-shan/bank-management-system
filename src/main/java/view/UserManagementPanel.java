package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import repository.UserRepository;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserManagementPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtName;
	private JTextField txtPassword;
	private JTextField txtEmail;
	private JTextField txtPhone;
	private JTable staffTable;
	private DefaultTableModel tableModel;
	private JComboBox<String> cmbRole;
	private int selectedStaffId = -1;
	private JTextField txtSearch;

	public UserManagementPanel() {
		setLayout(null);
		setSize(842, 620);

		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblName.setBounds(127, 95, 112, 18);
		add(lblName);

		txtName = new JTextField();
		txtName.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		txtName.setBounds(259, 94, 219, 24);
		add(txtName);
		txtName.setColumns(10);

		txtName.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();

				// Allow only letters (A-Z, a-z) and space
				if (!Character.isLetter(c) && !Character.isWhitespace(c) && !Character.isISOControl(c)) {
					e.consume(); // block anything that's not a letter, space, or control character
				}
			}
		});

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPassword.setBounds(127, 142, 112, 18);
		add(lblPassword);

		txtPassword = new JTextField();
		txtPassword.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		txtPassword.setBounds(259, 141, 219, 24);
		add(txtPassword);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblEmail.setBounds(127, 189, 112, 18);
		add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		txtEmail.setBounds(259, 188, 219, 24);
		add(txtEmail);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPhone.setBounds(127, 241, 112, 18);
		add(lblPhone);

		txtPhone = new JTextField();
		txtPhone.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		txtPhone.setBounds(259, 240, 219, 24);
		add(txtPhone);

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

		JLabel lblRole = new JLabel("Role");
		lblRole.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblRole.setBounds(127, 289, 112, 18);
		add(lblRole);

		cmbRole = new JComboBox<>(new DefaultComboBoxModel<>(new String[] { "", "ADMIN", "TELLER" }));
		cmbRole.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		cmbRole.setBounds(259, 287, 219, 26);
		add(cmbRole);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> saveStaff());
		btnSave.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnSave.setBounds(126, 345, 87, 27);
		add(btnSave);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(e -> updateStaff());
		btnUpdate.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnUpdate.setBounds(273, 345, 87, 27);
		add(btnUpdate);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnClear.setBounds(412, 345, 87, 27);
		add(btnClear);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 389, 822, 171);
		panel.setLayout(null);
		add(panel);

		String[] columnNames = { "StaffId", "Username", "Password", "Email", "Phone", "Role" };
		tableModel = new DefaultTableModel(columnNames, 0);
		staffTable = new JTable(tableModel);
		staffTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				fillFormFromSelectedRow();
			}
		});
		JScrollPane scrollPane = new JScrollPane(staffTable);
		scrollPane.setBounds(10, 14, 802, 108);
		panel.add(scrollPane);

		txtSearch = new JTextField("Search");
		txtSearch.setForeground(Color.GRAY);
		txtSearch.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		txtSearch.setBounds(281, 14, 197, 24);
		add(txtSearch);

		txtSearch.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (txtSearch.getText().equals("Search")) {
					txtSearch.setText("");
					txtSearch.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (txtSearch.getText().isEmpty()) {
					txtSearch.setForeground(Color.GRAY);
					txtSearch.setText("Search");
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

		setVisible(true);
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
			JOptionPane.showMessageDialog(this, "Please select a staff record to update.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
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
			UserRepository.updateStaff(selectedStaffId, name, password, email, phone, role);
			loadStaffData();
			clearFields();
		}

	}

	// Load staff data
	private void loadStaffData() {
		tableModel.setRowCount(0);
		try (ResultSet rs = UserRepository.getAllStaff()) {
			while (rs.next()) {
				tableModel.addRow(new Object[] { rs.getInt("staff_id"), rs.getString("username"),
						rs.getString("password"), rs.getString("email"), rs.getString("phone"), rs.getString("role") });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Clear form fields
	private void clearFields() {
		txtName.setText("");
		txtPassword.setText("");
		txtEmail.setText("");
		txtPhone.setText("");
		cmbRole.setSelectedIndex(0);
		selectedStaffId = -1;
		staffTable.clearSelection();
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

	// Fill form from table row
	private void fillFormFromSelectedRow() {
		int selectedRow = staffTable.getSelectedRow();
		if (selectedRow != -1) {
			selectedStaffId = (int) tableModel.getValueAt(selectedRow, 0);
			txtName.setText((String) tableModel.getValueAt(selectedRow, 1));
			txtPassword.setText((String) tableModel.getValueAt(selectedRow, 2));
			txtEmail.setText((String) tableModel.getValueAt(selectedRow, 3));
			txtPhone.setText((String) tableModel.getValueAt(selectedRow, 4));
			cmbRole.setSelectedItem((String) tableModel.getValueAt(selectedRow, 5));
		}
	}

	// Search staff
	private void searchStaff() {
		String keyword = txtSearch.getText().trim();
		if (keyword.equals("Search") || keyword.isEmpty()) {
			loadStaffData();
			return;
		}

		tableModel.setRowCount(0);
		try (ResultSet rs = UserRepository.searchStaff(keyword)) {
			while (rs.next()) {
				tableModel.addRow(new Object[] { rs.getInt("staff_id"), rs.getString("username"),
						rs.getString("password"), rs.getString("email"), rs.getString("phone"), rs.getString("role") });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
