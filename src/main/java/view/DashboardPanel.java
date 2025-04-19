package view;

import javax.swing.*;

import controller.DashboardController;
import model.dto.AdminDashboardStats;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends JPanel {
	
	private JLabel customerLabel, accountLabel, balanceLabel, pendingLabel;

    public DashboardPanel(DashboardController controller) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with welcome and date
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, Admin John Doe!");
        JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy EEEE")));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(dateLabel, BorderLayout.EAST);
        
        
        // Fetching Stats Data
        customerLabel = new JLabel();
        accountLabel = new JLabel();
        balanceLabel = new JLabel();
        pendingLabel = new JLabel();
        
        AdminDashboardStats stats = controller.fetchAdminDashboardStats();
        customerLabel.setText(String.valueOf(stats.getCustomers()));
        accountLabel.setText(String.valueOf(stats.getAccounts()));
        balanceLabel.setText(stats.getBankBalance().toString());
        pendingLabel.setText(String.valueOf(stats.getPendingTransactions()));
        
        // Styling Stats Box Panels
        JPanel customerStatsPanel = createStatBox("Total Customers", customerLabel);
        JPanel accountStatsPanel = createStatBox("Total Accounts", accountLabel);
        JPanel balanceStatsPanel = createStatBox("Bank Balance", balanceLabel);
        JPanel pendingStatsPanel = createStatBox("Pending Transactions", pendingLabel);
        
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
        

        // Pending Transactions Label
        JLabel pendingLabel = new JLabel("Pending Transactions");
        pendingLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Table for pending transactions
        String[] columns = {"Trx Id", "Customer", "Type", "Amount", "Date", "Action"};
        Object[][] data = {
                {"#####", "John Doe", "Withdraw", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Deposit", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Deposit", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Transfer", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Withdraw", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Withdraw", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Deposit", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Deposit", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Transfer", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Withdraw", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Withdraw", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Deposit", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Deposit", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Transfer", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
                {"#####", "John Doe", "Withdraw", "1,000,000", "04/23/2025", "[Approve] [Reject]"},
        };
        JTable table = new JTable(data, columns);
        JScrollPane tableScroll = new JScrollPane(table);

        // Middle section that holds stats and transactions
        JPanel centerPanel = new JPanel();
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

    private JPanel createStatBox(String title, JLabel valueLabel) {
        JPanel box = new JPanel();
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        box.setPreferredSize(new Dimension(100, 60));
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
        System.out.println(valueLabel.getText());

        return box;
    }
}
