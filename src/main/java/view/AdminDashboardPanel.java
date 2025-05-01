package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controller.DashboardController;
import model.dto.AdminDashboardStats;
import model.dto.RecentTransaction;
import utils.UpdateablePanel;

public class AdminDashboardPanel extends JPanel implements UpdateablePanel {

	private static final long serialVersionUID = 1L;
	private DashboardController controller;
	private JScrollPane tableScroll;
	private JPanel centerPanel;
	private JTable table;

	private JLabel customerValueLabel, accountValueLabel, balanceValueLabel, pendingValueLabel;

	public AdminDashboardPanel(DashboardController controller) {
		this.controller = controller;
		setLayout(new BorderLayout(20, 20));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		/*
		 * ========================== Section: Header ==========================
		 */
		// Header panel with welcome and date
		JPanel topPanel = new JPanel(new BorderLayout());
		JLabel welcomeLabel = new JLabel("Welcome, Admin John Doe!");
		JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy EEEE")));
		topPanel.add(welcomeLabel, BorderLayout.WEST);
		topPanel.add(dateLabel, BorderLayout.EAST);

		/*
		 * ========================== Section: Stats ==========================
		 */
		// Fetching Stats Data
		customerValueLabel = new JLabel();
		accountValueLabel = new JLabel();
		balanceValueLabel = new JLabel();
		pendingValueLabel = new JLabel();

		AdminDashboardStats stats = controller.fetchAdminDashboardStats();
		customerValueLabel.setText(String.valueOf(stats.getCustomers()));
		accountValueLabel.setText(String.valueOf(stats.getAccounts()));
		balanceValueLabel.setText(stats.getBankBalance() == null ? "0" : stats.getBankBalance().toString());
		pendingValueLabel.setText(String.valueOf(stats.getTransactionTodays()));

		// Styling Stats Box Panels
		JPanel customerStatsPanel = createStatBox("Total Customers", customerValueLabel);
		JPanel accountStatsPanel = createStatBox("Total Accounts", accountValueLabel);
		JPanel balanceStatsPanel = createStatBox("Bank Balance", balanceValueLabel);
		JPanel pendingStatsPanel = createStatBox("Today Transactions", pendingValueLabel);

		customerStatsPanel.setBackground(new Color(0x5fa8d3));
		accountStatsPanel.setBackground(new Color(0x9b5de5));
		balanceStatsPanel.setBackground(new Color(0xe5383b));
		pendingStatsPanel.setBackground(new Color(0xf17300));

		// Stats section
		JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
		statsPanel.setPreferredSize(new Dimension(600, 300));
		statsPanel.add(customerStatsPanel);
		statsPanel.add(accountStatsPanel);
		statsPanel.add(balanceStatsPanel);
		statsPanel.add(pendingStatsPanel);

		/*
		 * ========================== Section: Recent Transactions
		 * ==========================
		 */
		// Pending Transactions Label
		JLabel pendingLabel = new JLabel("Recent Transactions");
		pendingLabel.setFont(new Font("Arial", Font.BOLD, 14));
		pendingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		table = generateTable();

		// Create a JScrollPane and add the table to it
		tableScroll = new JScrollPane(table);

		// Middle section that holds stats and transactions
		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(Box.createVerticalStrut(10));
		centerPanel.add(statsPanel);
		centerPanel.add(Box.createVerticalStrut(60));
		centerPanel.add(pendingLabel);
		centerPanel.add(Box.createVerticalStrut(10));
		centerPanel.add(tableScroll);

		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
		add(centerPanel, BorderLayout.CENTER);
	}

	private JTable generateTable() {
		// Fetch the data from the controller
		// --------------------------------------- Add Login Staff Id
		// ----------------------------
		List<RecentTransaction> transactions = controller.fetchAdminDashboardTable();
		// --------------------------------------- Add Login Staff Id
		// ----------------------------

		// Define the column names
		String[] columns = { "Reference Number", "Customer", "Type", "Amount", "Date", "Staff Id" };

		// Create the DefaultTableModel without specifying the number of rows
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// Disable table editing
				return false;
			}
		};

		// Add rows to the model
		for (RecentTransaction trx : transactions) {
			model.addRow(trx.toObject());
		}

		// Create the JTable with the model
		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // Disable column reordering
		table.setRowHeight(40);
		return table;
	}

	private JPanel createStatBox(String title, JLabel valueLabel) {
		JPanel box = new JPanel();
		box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.add(Box.createVerticalStrut(10));
		box.add(titleLabel);

		valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
		valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		box.add(Box.createVerticalStrut(20));
		box.add(valueLabel);

		return box;
	}

	@Override
	public void updateData() {
	    // Fetch updated stats
	    AdminDashboardStats stats = controller.fetchAdminDashboardStats();
	    customerValueLabel.setText(String.valueOf(stats.getCustomers()));
	    accountValueLabel.setText(String.valueOf(stats.getAccounts()));
	    balanceValueLabel.setText(stats.getBankBalance() == null ? "0" : stats.getBankBalance().toString());
	    pendingValueLabel.setText(String.valueOf(stats.getTransactionTodays()));

	    // Create a new table
	    JTable newTable = generateTable();
	    JScrollPane newTableScroll = new JScrollPane(newTable);

	    // Remove the old table scroll pane if it exists
	    if (tableScroll != null) {
	        centerPanel.remove(tableScroll);
	    }

	    // Add the new table scroll pane
	    centerPanel.add(newTableScroll);
	    tableScroll = newTableScroll; // Update the reference

	    // Refresh the UI
	    centerPanel.revalidate();
	    centerPanel.repaint();
	}
}
