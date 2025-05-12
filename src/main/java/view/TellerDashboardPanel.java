package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.DashboardController;
import model.dto.RecentTransaction;
import model.dto.TellerDashboardStats;
import model.dto.TransactionDetail;
import utils.UpdateablePanel;

public class TellerDashboardPanel extends JPanel implements UpdateablePanel {

	private static final long serialVersionUID = 1L;
	private DashboardController controller;
	private JScrollPane tableScroll;
	private JPanel centerPanel;
	private JTable table;
	private DefaultTableModel tableModel;
	private int loggedInStaffId;

	private JLabel customerValueLabel, accountValueLabel, cardValueLabel, transactionValueLabel;

	public TellerDashboardPanel(DashboardController controller, int loggedInStaffId) {
		this.controller = controller;
		this.loggedInStaffId = loggedInStaffId;
		setLayout(new BorderLayout(20, 20));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		/*
		 * ========================== Section: Header ==========================
		 */
		// Header panel with welcome and date
		JPanel topPanel = new JPanel(new BorderLayout());
		JLabel welcomeLabel = new JLabel("Welcome, Teller Jame Smith!");
		JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy EEEE")));
		topPanel.add(welcomeLabel, BorderLayout.WEST);
		topPanel.add(dateLabel, BorderLayout.EAST);

		/*
		 * ========================== Section: Stats ==========================
		 */
		// Fetching Stats Data
		customerValueLabel = new JLabel();
		accountValueLabel = new JLabel();
		cardValueLabel = new JLabel();
		transactionValueLabel = new JLabel();

		// --------------------------------------- Add Login Staff Id
		// ----------------------------
		TellerDashboardStats stats = controller.fetchTellerDashboardStats(loggedInStaffId);
		// --------------------------------------- Add Login Staff Id
		// ----------------------------
		customerValueLabel.setText(String.valueOf(stats.getCustomers()));
		accountValueLabel.setText(String.valueOf(stats.getAccounts()));
		cardValueLabel.setText(String.valueOf(stats.getCards()));
		transactionValueLabel.setText(String.valueOf(stats.getTransactionTodays()));

		// Styling Stats Box Panels
		JPanel customerStatsPanel = createStatBox("Customers you created", customerValueLabel);
		JPanel accountStatsPanel = createStatBox("Accounts you created", accountValueLabel);
		JPanel cardStatsPanel = createStatBox("Card you issued", cardValueLabel);
		JPanel transactionStatsPanel = createStatBox("Transactions today", transactionValueLabel);

		customerStatsPanel.setBackground(new Color(0x5fa8d3));
		accountStatsPanel.setBackground(new Color(0x9b5de5));
		cardStatsPanel.setBackground(new Color(0xe5383b));
		transactionStatsPanel.setBackground(new Color(0xf17300));

		// Stats section
		JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
		statsPanel.setPreferredSize(new Dimension(600, 300));
		statsPanel.add(customerStatsPanel);
		statsPanel.add(accountStatsPanel);
		statsPanel.add(cardStatsPanel);
		statsPanel.add(transactionStatsPanel);

		// Recent Transactions Label
		JLabel recentLabel = new JLabel("Recent Transactions");
		recentLabel.setFont(new Font("Arial", Font.BOLD, 14));
		recentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Middle section that holds stats and transactions
		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(Box.createVerticalStrut(10));
		centerPanel.add(statsPanel);
		centerPanel.add(Box.createVerticalStrut(60));
		centerPanel.add(recentLabel);
		centerPanel.add(Box.createVerticalStrut(10));
//		centerPanel.add(tableScroll);
		initTable(loggedInStaffId);
		refreshTable(controller.fetchTellerDashboardTable(loggedInStaffId));

		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
		add(centerPanel, BorderLayout.CENTER);
	}

	private void initTable(int staffID) {
		String[] tellerCols = { "Reference No.", "Timestamp", "Type", "Amount", "FromAccount", "ToAccount" };

		tableModel = new DefaultTableModel(tellerCols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(40);

		// Create a number formatter with comma grouping
		NumberFormat currencyFormat = NumberFormat.getNumberInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		// Custom renderer for financial values with comma formatting
		DefaultTableCellRenderer financialRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setValue(Object value) {
				if (value instanceof BigDecimal) {
					value = currencyFormat.format(((BigDecimal) value).doubleValue());
				}
				super.setValue(value);
			}
		};
		financialRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(financialRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(700, 280)); // width, height
		centerPanel.add(tableScrollPane);
	}

	private void refreshTable(List<TransactionDetail> trxDetails) {
		tableModel.setRowCount(0);

		for (TransactionDetail trx : trxDetails) {
			Object[] rowData = { trx.referenceNo(),
					trx.timestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
					trx.type(),
					trx.amount(), 
					trx.fromAccount() == null ? "N/A" : trx.fromAccount(),
					trx.toAccount() == null ? "N/A" : trx.toAccount() };
			tableModel.addRow(rowData);
		}
		tableModel.fireTableDataChanged();
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
		TellerDashboardStats stats = controller.fetchTellerDashboardStats(loggedInStaffId);
		customerValueLabel.setText(String.valueOf(stats.getCustomers()));
		accountValueLabel.setText(String.valueOf(stats.getAccounts()));
		cardValueLabel.setText(String.valueOf(stats.getCards()));
		transactionValueLabel.setText(String.valueOf(stats.getTransactionTodays()));

		refreshTable(controller.fetchTellerDashboardTable(loggedInStaffId));
	}

}
