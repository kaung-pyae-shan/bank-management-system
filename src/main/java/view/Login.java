package view;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import config.DependenciesConfig;
import controller.LoginController;
import model.Staff.Role;
import view.common.MainView;

public class Login extends JFrame {
	
	private LoginController controller;

    public Login(LoginController controller) {
        setTitle("Bank Management System - Login");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(173, 207, 145)); // Classic dark gray
        sidebar.setBounds(0, 0, 250, 500);
        sidebar.setLayout(null);
        getContentPane().add(sidebar);

        // Logo
        ImageIcon originalIcon = new ImageIcon(Login.class.getResource("/bank_logo.jpg"));

        // Resize the image to fit the JLabel size (100x100)
        Image img = originalIcon.getImage();
        Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImg);

        // Create JLabel with the resized image
        JLabel logoLabel = new JLabel(resizedIcon);
        logoLabel.setBounds(75, 100, 100, 100); // Position
        sidebar.add(logoLabel);
        // Bank name
        JLabel bankName = new JLabel("Bank Management System");
        bankName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bankName.setForeground(Color.WHITE);
        bankName.setBounds(10, 220, 230, 30);
        sidebar.add(bankName);

        // Login Panel (Right Side)
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(240, 240, 240)); // Light off-white gray
        loginPanel.setBounds(250, 0, 450, 500);
        getContentPane().add(loginPanel);

        JLabel loginTitle = new JLabel("Login to Your Account");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginTitle.setForeground(new Color(30, 30, 30));
        loginTitle.setBounds(89, 41, 300, 40);
        loginPanel.add(loginTitle);

        // Email Field (with Label)
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(new RoundedBorder(12));
        emailField.setBounds(75, 140, 300, 40);
        emailField.setToolTipText("");
        loginPanel.add(emailField);

        JLabel emailLabel = new JLabel("Enter your email");
        emailLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        emailLabel.setForeground(Color.GRAY);
        emailLabel.setBounds(75, 140, 300, 40);
        loginPanel.add(emailLabel);

        // Password Field (with Label)
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(new RoundedBorder(12));
        passwordField.setBounds(75, 216, 300, 40);
        passwordField.setToolTipText("Enter your password");
        loginPanel.add(passwordField);

        JLabel passwordLabel = new JLabel("Enter your password");
        passwordLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        passwordLabel.setForeground(Color.GRAY);
        passwordLabel.setBounds(75, 215, 300, 40);
        loginPanel.add(passwordLabel);

        // Focus listeners for hiding/showing labels
        emailField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("")) {
                    emailLabel.setVisible(false); // Hide label when focused
                }
            }

            public void focusLost(FocusEvent e) {
                if (emailField.getText().equals("")) {
                    emailLabel.setVisible(true); // Show label if field is empty
                }
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("")) {
                    passwordLabel.setVisible(false); // Hide label when focused
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("")) {
                    passwordLabel.setVisible(true); // Show label if field is empty
                }
            }
        });

        // Login Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(75, 309, 300, 45);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setBackground(new Color(173, 207, 145)); // Classic dark gray
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(new RoundedBorder(12));
        loginPanel.add(loginBtn);
        
        JLabel lblNewLabel = new JLabel("Enter your Email");
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNewLabel.setBounds(75, 114, 113, 18);
        loginPanel.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("Enter your password");
        lblNewLabel_1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNewLabel_1.setBounds(75, 194, 113, 18);
        loginPanel.add(lblNewLabel_1);
        
        JCheckBox chckbxNewCheckBox = new JCheckBox("show password");
        chckbxNewCheckBox.setFont(new Font("Segoe UI", Font.BOLD, 11));
        chckbxNewCheckBox.setBounds(75, 263, 129, 27);
        loginPanel.add(chckbxNewCheckBox);
        
        chckbxNewCheckBox.addActionListener(e -> {
            if (chckbxNewCheckBox.isSelected()) {
                // Show password
                passwordField.setEchoChar((char) 0); // Echo character 0 shows the password
            } else {
                // Hide password
                passwordField.setEchoChar('*'); // Default echo character
            }
        });
        
        loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				char[] passwordChar = passwordField.getPassword();
				String password = String.valueOf(passwordChar);
				int staffId = controller.login(email, password);
				Role role = controller.getRoleForCurrentLoggedInStaff(staffId);
				if(staffId > 0) {
					MainView mainView = new MainView(new DependenciesConfig(), staffId, role);
					mainView.setVisible(true);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Incorrect Email or Password", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
    }

    // Rounded Border Class for fields and button
    class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
//    }
}
