package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 853, 472);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        ImagePanel panel = new ImagePanel("D:/Banking/login.jpg");
        panel.setBounds(0, 0, 350, 421);
        contentPane.add(panel);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblLogin.setBounds(570, 38, 99, 34);
        contentPane.add(lblLogin);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        txtEmail.setBounds(492, 115, 236, 24);
        contentPane.add(txtEmail);
        txtEmail.setColumns(10);

        txtPassword = new JPasswordField();
        txtPassword.setToolTipText("Enter your passwor");
        txtPassword.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        txtPassword.setBounds(492, 165, 236, 24);
        txtPassword.setEchoChar('*');
        contentPane.add(txtPassword);
        txtPassword.setColumns(10);

        JCheckBox chckbxShowPassword = new JCheckBox("show password");
        chckbxShowPassword.setBounds(492, 199, 139, 27);
        chckbxShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chckbxShowPassword.isSelected()) {
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setEchoChar('*');
                }
            }
        });
        contentPane.add(chckbxShowPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btnLogin.setBounds(556, 264, 108, 27);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtEmail.getText();
                String password = new String(txtPassword.getPassword());

                // Dummy login validation logic
                if (username.equals("admin") && password.equals("admin123")) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    // Proceed to next screen or logic
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(btnLogin);
        
        JLabel lblemail = new JLabel("Enter your email");
        lblemail.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        lblemail.setBounds(492, 95, 119, 18);
        contentPane.add(lblemail);
        
        JLabel lblpassword = new JLabel("Enter your password");
        lblpassword.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        lblpassword.setBounds(492, 144, 119, 18);
        contentPane.add(lblpassword);
    }

    class ImagePanel extends JPanel {
        private BufferedImage image;
        public ImagePanel(String imagePath) {
            try {
                image = ImageIO.read(new File(imagePath));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}