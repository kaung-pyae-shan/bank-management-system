package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MenuPanel(Consumer<String> navCallback) {
		setBackground(new Color(0x353535));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(300, 700));

		// Top padding
		add(Box.createVerticalStrut(80));

		// Nav buttons
		addNavButton("Dashboard", navCallback);
		addNavButton("Customers", navCallback);
		addNavButton("Accounts", navCallback);
		addNavButton("Transactions", navCallback);

		// Push everything above up
		add(Box.createVerticalGlue());

		// Logout button at the bottom
		addNavButton("Logout", action -> showLogoutConfirmation("Logout"));

		add(Box.createVerticalStrut(20)); // bottom padding
	}

	private void addNavButton(String name, Consumer<String> callback) {
		JButton button = new JButton(name);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(300, 60)); // Set preferred size
        button.setMaximumSize(new Dimension(300, 60)); // Set maximum size
        
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
