package view.common;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import config.DependenciesConfig;
import utils.UpdateablePanel;
import view.AccountManagementPanel;
import view.AccountStatusControlPanel;
import view.AdminDashboardPanel;
import view.AdminMenuPanel;
import view.CardManagementPanel;
import view.CustomerManagementPanel;
import view.InterestManagementPanel;
import view.TransactionLogsPanel;
import view.TransactionsPanel;
import view.UserManagementPanel;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel mainContentPanel;
	private CardLayout cardLayout;

	public MainView(DependenciesConfig config) {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		setTitle("Bank Management System");

		contentPane = new JPanel(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// 🔹 Init menu with listener
		// --------------Add Login logic here----------------------
		AdminMenuPanel adminMenuPanel = new AdminMenuPanel(viewName -> showPanel(viewName));
//		TellerMenuPanel tellerMenuPanel = new TellerMenuPanel(viewName -> showPanel(viewName));
		adminMenuPanel.setPreferredSize(new Dimension(300, 700));
		contentPane.add(adminMenuPanel, BorderLayout.WEST);

		// 🔹 Init main content area with CardLayout
		cardLayout = new CardLayout();
		mainContentPanel = new JPanel(cardLayout);
		mainContentPanel.setBackground(new Color(0x3c6e71));
		contentPane.add(mainContentPanel, BorderLayout.CENTER);

		// 🔹 Add views to the CardLayout
		// ------------------- Add login logic here -----------------------
		mainContentPanel.add(new AdminDashboardPanel(config.getDashboardController()), "Dashboard");
//		mainContentPanel.add(new TellerDashboardPanel(config.getDashboardController()), "Dashboard");
		mainContentPanel.add(new UserManagementPanel(), "User Management");
		mainContentPanel.add(new CustomerManagementPanel(), "Customer Management");
		mainContentPanel.add(new AccountManagementPanel(), "Account Management");
		mainContentPanel.add(new CardManagementPanel(), "Card Management");
		mainContentPanel.add(new TransactionsPanel(config.getTransactionController()), "Transactions");
		mainContentPanel.add(new InterestManagementPanel(), "Interest Management");
		mainContentPanel.add(new TransactionLogsPanel(), "Transaction Logs");
		mainContentPanel.add(new AccountStatusControlPanel(), "Account Status Control");
		// Add more panels later: e.g. mainContentPanel.add(new CustomerPanel(), "Customers");

		// Show default
		showPanel("Dashboard");
	}

	// 🔹 Show a panel by name
	public void showPanel(String name) {
//		cardLayout.show(mainContentPanel, name);
		// Show the panel
	    cardLayout.show(mainContentPanel, name);
	    
	    // Get the currently displayed panel
	    Component[] components = mainContentPanel.getComponents();
	    for (Component component : components) {
	        if (component.isVisible()) {
	            // Check if the component is an instance of UpdateablePanel
	            if (component instanceof UpdateablePanel) {
	                ((UpdateablePanel) component).updateData(); // Call updateData
	            }
	        }
	    }
	}
}
