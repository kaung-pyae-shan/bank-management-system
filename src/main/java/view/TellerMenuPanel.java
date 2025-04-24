package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class TellerMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TellerMenuPanel(Consumer<String> navCallback) {
		setBackground(new Color(0x353535));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(300, 700));
		
		add(Box.createVerticalStrut(40));
		
		// Create and configure the logo text area
        JTextArea logoTextArea = new JTextArea("Bank Management System");
        logoTextArea.setForeground(Color.WHITE);
        logoTextArea.setBackground(new Color(0x353535)); // Same as sidebar
        logoTextArea.setLineWrap(true); // Enable line wrapping
        logoTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        logoTextArea.setEditable(false); // Make it non-editable
        logoTextArea.setOpaque(false); // Make it transparent
        logoTextArea.setFont(new Font("Arial", Font.BOLD, 24)); // Set font
        logoTextArea.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the text area

        // Set preferred size for the text area
        logoTextArea.setPreferredSize(new Dimension(280, 60)); // Adjust height as needed
        logoTextArea.setMaximumSize(new Dimension(280, 60)); // Adjust height as needed

        // Add the logo text area to the panel
        add(logoTextArea);

		// Top padding
		add(Box.createVerticalStrut(50));

		// Nav buttons
		addNavButton("Dashboard", navCallback);
		addNavButton("Customer Management", navCallback);
		addNavButton("Account Management", navCallback);
		addNavButton("Card Management", navCallback);
		addNavButton("Transactions", navCallback);
		addNavButton("Transaction Logs", navCallback);

		// Push everything above up
		add(Box.createVerticalGlue());

		// Logout button at the bottom
		addNavButton("Logout", action -> showLogoutConfirmation("Logout"));

		add(Box.createVerticalStrut(20)); // bottom padding
	}

	private void addNavButton(String name, Consumer<String> callback) {
		JButton button = new JButton(name);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(300, 40)); // Set preferred size
        button.setMaximumSize(new Dimension(300, 40)); // Set maximum size
        
     // Make the button background transparent
        button.setOpaque(true); // Make the button opaque
        button.setContentAreaFilled(true); // Allow to fill the button area
        button.setForeground(Color.white); // Set text color
        button.setBackground(new Color(0x353535)); // same as sidebar
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(null); // Remove border
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Paddings
        
        
     // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x595959)); // Change background on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	button.setBackground(new Color(0x353535)); // Change background on hover exit
            }
        });
		button.addActionListener(e -> callback.accept(name));
		add(button);
	}
	
	private void showLogoutConfirmation(String action) {
        int response = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Proceed with logout
            System.out.println("User  logged out.");
            // Add your logout logic here
        } else {
            // Cancel logout
            System.out.println("Logout canceled.");
        }
    }
}
