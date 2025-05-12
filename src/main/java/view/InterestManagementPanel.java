package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import controller.InterestController;
import model.AccountType.Type;

public class InterestManagementPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public InterestManagementPanel(InterestController controller, int loggedInStaffId) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Interest Settings
		JPanel interestSettingPanel = new JPanel(new GridBagLayout());

		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel lblSetting = new JLabel("-- Interest Settings --");
		lblSetting.setFont(new Font("Dialog", Font.BOLD, 15));
		interestSettingPanel.add(lblSetting, gbc);
		gbc.gridy++;
		interestSettingPanel.add(generateLabel("Account Type"), gbc);
		gbc.gridy++;
		interestSettingPanel.add(generateLabel("Interest Rate"), gbc);

		JComboBox<String> cmbAccTypeSetting = new JComboBox<>(new String[] { "Select type", "SAVING", "FIXED_90D",
				"FIXED_180D", "FIXED_360D", "FIXED_720D", "FIXED_1080D" });
		JTextField interestRateField = new JTextField(25);
		interestRateField.setEditable(false);

		gbc.gridx = 1;
		gbc.gridy = 1;
		interestSettingPanel.add(cmbAccTypeSetting, gbc);
		gbc.gridy++;
		interestSettingPanel.add(interestRateField, gbc);

		// Action Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		JButton btnUpdate = addActionButton("Update", new Color(0x353535));
		JButton btnClear = addActionButton("Clear", new Color(0x353535));
		btnUpdate.setEnabled(false);
		btnClear.setEnabled(false);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnClear);

		cmbAccTypeSetting.addActionListener(e -> {
			String input = cmbAccTypeSetting.getSelectedItem().toString();

			if (cmbAccTypeSetting.getSelectedIndex() == 0) {
				interestRateField.setText("");
				interestRateField.setEditable(false);
				btnUpdate.setEnabled(false);
				btnClear.setEnabled(false);
			} else {
				interestRateField.setText(controller.findInterestRate(Type.valueOf(input)).toString());
				interestRateField.setEditable(true);
				btnUpdate.setEnabled(true);
				btnClear.setEnabled(true);
			}
		});

		btnUpdate.addActionListener(e -> {
			String input = cmbAccTypeSetting.getSelectedItem().toString();

			BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(interestRateField.getText()));
			if (controller.isSameInterestRate(Type.valueOf(input), rate)) {
				JOptionPane.showMessageDialog(null,
						"The interest rate for " + input + " account is already set to the same value.", "Success",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			int row = controller.changeInterestRate(Type.valueOf(input), rate);
			if (row > 0) {
				interestRateField.setText(controller.findInterestRate(Type.valueOf(input)).toString());
				JOptionPane.showMessageDialog(null, "Interest rate for " + input + " account type changed successfully",
						"Success", JOptionPane.INFORMATION_MESSAGE);
			}

		});

		btnClear.addActionListener(e -> {
			cmbAccTypeSetting.setSelectedIndex(0);
			interestRateField.setText("");
			interestRateField.setEditable(false);
			btnUpdate.setEnabled(false);
			btnClear.setEnabled(false);
		});

		gbc.gridy++;
		interestSettingPanel.add(buttonPanel, gbc);

		// Apply Interest
		JPanel applyInterestPanel = new JPanel(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel lblApply = new JLabel("-- Apply Interests --");
		lblApply.setFont(new Font("Dialog", Font.BOLD, 15));
		applyInterestPanel.add(lblApply, gbc);
		gbc.gridy++;
		applyInterestPanel.add(generateLabel("Account Type"), gbc);
		gbc.gridy++;
		applyInterestPanel.add(generateLabel("Number of accounts to be applied"), gbc);
		gbc.gridy++;
		applyInterestPanel.add(generateLabel("Interest rate per annum"), gbc);

		JComboBox<String> cmbAccTypeApply = new JComboBox<>(new String[] { "Select type", "SAVING" });
		JTextField numberOfAccountsField = new JTextField(25);
		numberOfAccountsField.setEditable(false);
		JTextField applyInterestRateField = new JTextField(25);
		applyInterestRateField.setEditable(false);

		gbc.gridx = 1;
		gbc.gridy = 1;
		applyInterestPanel.add(cmbAccTypeApply, gbc);
		gbc.gridy++;
		applyInterestPanel.add(numberOfAccountsField, gbc);
		gbc.gridy++;
		applyInterestPanel.add(applyInterestRateField, gbc);

		// Action Buttons
		JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		JButton btnApply = addActionButton("Apply", new Color(0x353535));
		btnApply.setEnabled(false);
		;
		buttonPanel1.add(btnApply);
		gbc.gridy++;
		applyInterestPanel.add(buttonPanel1, gbc);

		btnApply.addActionListener(e -> {
			int row = controller.updateInterestToAllActiveSaving(loggedInStaffId);
			if (row > 0) {
				JOptionPane.showMessageDialog(null, "Applied interest to " + row + " savings account successfully!",
						"Success", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		cmbAccTypeApply.addActionListener(e -> {
			String input = cmbAccTypeApply.getSelectedItem().toString();

			if (cmbAccTypeApply.getSelectedIndex() == 0) {
				applyInterestRateField.setText("");
				return;
			}
			numberOfAccountsField.setText(String.valueOf(controller.findSavingCount()));
			applyInterestRateField.setText(controller.findInterestRate(Type.valueOf(input)).toString());
			if(isFirstDayOfMonth()) {
				btnApply.setEnabled(true);
			}
		});
		
		controller.updateInterestToAllFixed();

		applyInterestPanel.setBorder(new MatteBorder(2, 0, 0, 0, Color.GRAY));
		add(interestSettingPanel);
		add(applyInterestPanel);
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
	
	private boolean isFirstDayOfMonth() {
        LocalDate today = LocalDate.now(); // Get the current date
        return today.getDayOfMonth() == 1; // Check if it's the first day of the month
    }
}
