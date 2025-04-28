package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import utils.NRCHelper;

public class CustomerManagementPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
	
	/*
	 * BorderLayout
	 * There are three panels: searchBarPanel, TopPanel, and tableScrollPane
	 * searchBarPanel -> FlowLayout {JTextField}
	 * topPanel -> GridLayout {formPanel, accountPanel}
	 * 			formPanel -> GridBagLayout {Labels, TextFields, Combobox, Date Picker(JCalendar}
	 * 			accountPanel -> GridBagLayout {Labels}
	 * tableScrollPane -> {JTable}
	 */

	public CustomerManagementPanel() {
		setLayout(new BorderLayout());
		
		// ==== Search bar panel ====
		JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JTextField searchField = new JTextField(20);
		searchBarPanel.add(searchField);
		
		// ==== Top panel (Form + Account Info) ====
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		// == Form ==
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setPreferredSize(new Dimension(400, 300)); // Set preferred size
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(13, 10, 13, 10);
		gbc.anchor = GridBagConstraints.WEST;
		
		JTextField nameField = new JTextField(25);
		JTextField emailField = new JTextField(25);
		JTextField phoneField = new JTextField(25);
		JTextField addressField = new JTextField(25);
		
		JPanel nrcInnerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints innerGbc = new GridBagConstraints();
		innerGbc.insets = new Insets(0, 2, 0, 2);
		innerGbc.gridy = 0;
		innerGbc.fill = GridBagConstraints.HORIZONTAL;
		
		// 12/
		JComboBox<String> stateCodeCb = new JComboBox<String>();
		for(int i = 1; i <= 14; i++) {
			stateCodeCb.addItem(Integer.toString(i) + "/");
		}
		// TaMaNa
		JComboBox<String> regionCodeCb = new JComboBox<String>();
		List<String> regions = new NRCHelper().getRegions("1");
		for(String region : regions) {
			regionCodeCb.addItem(region);
		}
		// N
		JComboBox<String> nrcTypeCb = new JComboBox<String>();
		nrcTypeCb.addItem("N");
		nrcTypeCb.addItem("E");
		nrcTypeCb.addItem("P");
		nrcTypeCb.addItem("T");
		nrcTypeCb.addItem("R");
		nrcTypeCb.addItem("S");
		// NRC number
		JTextField nrcNumberField = new JTextField(7);
		
		innerGbc.gridx = 0;
		innerGbc.gridy = 0;
		nrcInnerPanel.add(stateCodeCb, innerGbc);
		innerGbc.gridx++;
		nrcInnerPanel.add(regionCodeCb, innerGbc);
		innerGbc.gridx++;
		nrcInnerPanel.add(nrcTypeCb, innerGbc);
		innerGbc.gridx++;
		nrcInnerPanel.add(nrcNumberField, innerGbc);
		
		// Date Picker for dob
		JDateChooser datePicker = new JDateChooser();
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
		
		// == Account Details Panel ==
		cardLayout = new CardLayout();
		JPanel accountPanel = new JPanel(cardLayout);
		accountPanel.setPreferredSize(new Dimension(300, 300)); // Set preferred size
		JPanel emptyPanel = new JPanel();
		JPanel accountInfoPanel = new JPanel(new GridBagLayout());
		JScrollPane accountInfoScroll = new JScrollPane(accountInfoPanel);
		
		accountPanel.add(emptyPanel);
		accountPanel.add(accountInfoScroll);
		
		topPanel.add(formPanel);
		topPanel.add(accountPanel);
		
		
		// ==== Table Panel ====
		String[] columns = {"Name", "NRC", "DOB", "Email", "Phone", "Address"};
		Object[][] data = {}; // Your data here
		JTable table = new JTable(data, columns);
		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(700, 300)); // width, height
		
		add(searchBarPanel, BorderLayout.NORTH);
		add(topPanel, BorderLayout.CENTER);
		add(tableScrollPane, BorderLayout.SOUTH);
		
		datePicker.addPropertyChangeListener("date", new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
		        // This code will execute when the date is changed
		        Date selectedDate = datePicker.getDate();
		        System.out.println("Selected date: " + selectedDate);
		    }
		});
	}
	
	private JLabel generateLabel(String label) {
		JLabel formLabel = new JLabel(label + ":");
		formLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		return formLabel;
	}

}
