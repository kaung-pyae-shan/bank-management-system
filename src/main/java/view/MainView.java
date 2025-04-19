package view;

import java.awt.*;
import javax.swing.*;

import controller.DashboardController;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel mainContentPanel;  // 🔹 Make it a class-level field
	private CardLayout cardLayout;

	public MainView() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		setTitle("Bank Management System");

		contentPane = new JPanel(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// 🔹 Init menu with listener
		MenuPanel menuPanel = new MenuPanel(viewName -> showPanel(viewName));
		menuPanel.setPreferredSize(new Dimension(300, 700));
		contentPane.add(menuPanel, BorderLayout.WEST);

		// 🔹 Init main content area with CardLayout
		cardLayout = new CardLayout();
		mainContentPanel = new JPanel(cardLayout);
		mainContentPanel.setBackground(new Color(0x3c6e71));
		contentPane.add(mainContentPanel, BorderLayout.CENTER);

		// 🔹 Add views to the CardLayout
		mainContentPanel.add(new DashboardPanel(new DashboardController()), "Dashboard");
		mainContentPanel.add(new TransactionsPanel(), "Transactions");
		// Add more panels later: e.g. mainContentPanel.add(new CustomerPanel(), "Customers");

		// Show default
		showPanel("Dashboard");
	}

	// 🔹 Show a panel by name
	public void showPanel(String name) {
		cardLayout.show(mainContentPanel, name);
	}
}
