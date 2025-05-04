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
import view.CardManagement;
import view.CustomerManagementPanel;
import view.InterestManagementPanel;
import view.TellerDashboardPanel;
import view.TellerMenuPanel;
import view.TransactionLogsPanel;
import view.TransactionsPanel;
import view.UserManagement;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel mainContentPanel;
	private CardLayout cardLayout;

	public MainView(DependenciesConfig config, int loggedInStaffId) {
		
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		setTitle("Bank Management System");
		setLocationRelativeTo(null);

		contentPane = new JPanel(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// 🔹 Init menu with listener
		// --------------Add Login logic here----------------------
//		AdminMenuPanel adminMenuPanel = new AdminMenuPanel(viewName -> showPanel(viewName));
		TellerMenuPanel tellerMenuPanel = new TellerMenuPanel(viewName -> showPanel(viewName));
		tellerMenuPanel.setPreferredSize(new Dimension(300, 700));
		contentPane.add(tellerMenuPanel, BorderLayout.WEST);

		// 🔹 Init main content area with CardLayout
		cardLayout = new CardLayout();
		mainContentPanel = new JPanel(cardLayout);
		mainContentPanel.setBackground(new Color(0x3c6e71));
		contentPane.add(mainContentPanel, BorderLayout.CENTER);

		// 🔹 Add views to the CardLayout
		// ------------------- Add login logic here -----------------------
//		mainContentPanel.add(new AdminDashboardPanel(config.getDashboardController()), "Dashboard");
		mainContentPanel.add(new TellerDashboardPanel(config.getDashboardController(), loggedInStaffId), "Dashboard");
		mainContentPanel.add(new UserManagement(), "User Management");
		mainContentPanel.add(new CustomerManagementPanel(config.getCustomerController(), loggedInStaffId), "Customer Management");
		mainContentPanel.add(new AccountManagementPanel(), "Account Management");
		mainContentPanel.add(new CardManagement(), "Card Management");
		mainContentPanel.add(new TransactionsPanel(config.getTransactionController(), loggedInStaffId), "Transactions");
		mainContentPanel.add(new InterestManagementPanel(), "Interest Management");
		mainContentPanel.add(new TransactionLogsPanel(config.getTransactionController(), loggedInStaffId), "Transaction Logs");
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
