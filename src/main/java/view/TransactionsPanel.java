package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.TransactionController;

public class TransactionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton selectedButton; // Track the currently selected button
	private JPanel mainFormPanel;
	private CardLayout cardLayout;

	/**
	 * Create the panel.
	 */
	public TransactionsPanel(TransactionController controller) {
		setLayout(new BorderLayout(0, 0));

		// ========= Tabs Section =========
		JPanel tabWrapper = new JPanel();
		tabWrapper.setLayout(new BoxLayout(tabWrapper, BoxLayout.Y_AXIS));
		tabWrapper.setPreferredSize(new Dimension(getWidth(), 100));

//		tabWrapper.add(Box.createVerticalStrut(30));
		tabWrapper.add(Box.createVerticalGlue());

		JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		JButton btnDeposit = addTabButton("Deposit", new Color(0x007030), viewname -> showPanel(viewname));
		JButton btnWithdraw = addTabButton("Withdraw", new Color(0xe0162b), viewname -> showPanel(viewname));
		JButton btnTransfer = addTabButton("Transfer", new Color(0x326e9d), viewname -> showPanel(viewname));
		tabPanel.add(btnDeposit);
		tabPanel.add(btnWithdraw);
		tabPanel.add(btnTransfer);

		Dimension tabButtonSize = new Dimension(100, 30);
		btnDeposit.setPreferredSize(tabButtonSize);
		btnWithdraw.setPreferredSize(tabButtonSize);
		btnTransfer.setPreferredSize(tabButtonSize);

		tabWrapper.add(tabPanel);
		tabWrapper.add(Box.createVerticalGlue());
		tabWrapper.setBorder(null);

		add(tabWrapper, BorderLayout.NORTH);

		// Set the default selected button to "Dashboard"
		selectedButton = btnDeposit; // Assuming "Dashboard" is the first button added
		selectedButton.setBackground(new Color(0x007030)); // Set to hover color
		selectedButton.setForeground(Color.WHITE);

		// ==== Form Panel ====	
		// 🔹 Init main form area with CardLayout
		cardLayout = new CardLayout();
		mainFormPanel = new JPanel(cardLayout);
		add(mainFormPanel, BorderLayout.CENTER);
		
		mainFormPanel.add(new DepositPanel(controller), "Deposit");
		mainFormPanel.add(new WithdrawPanel(controller), "Withdraw");
		mainFormPanel.add(new TransferPanel(controller), "Transfer");
		showPanel("Deposit");
	}

	private JButton addTabButton(String name, Color hoverColor, Consumer<String> callback) {
		JButton button = new JButton(name);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(100, 30)); // Set preferred size

		// Make the button background transparent
		button.setOpaque(true); // Make the button opaque
		button.setContentAreaFilled(true); // Allow to fill the button area
		button.setForeground(Color.BLACK); // Set text color
		button.setBackground(Color.WHITE); // Default color
		button.setFocusPainted(false); // Remove focus border

		// Add hover effect
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				if (button != selectedButton) { // Only change if not selected
					button.setForeground(Color.WHITE);
					button.setBackground(hoverColor); // Change background on hover
				}
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				if (button != selectedButton) { // Only revert if not selected
					button.setForeground(Color.BLACK);
					button.setBackground(Color.WHITE); // Change background on hover exit
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (selectedButton != null) {
					// Reset the previously selected button
					selectedButton.setForeground(Color.BLACK);
					selectedButton.setBackground(Color.WHITE);
				}
				// Set the new selected button
				selectedButton = button;
				button.setForeground(Color.WHITE);
				button.setBackground(hoverColor); // Retain hover color
				callback.accept(name);
			}
		});

		return button;
	}

	// 🔹 Show a panel by name
	public void showPanel(String name) {
		cardLayout.show(mainFormPanel, name);
	}

}
