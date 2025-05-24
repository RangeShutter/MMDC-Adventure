import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/* Please take note all information given on this program are only samples such as the variables given */

public class User {
    private static HashMap<String, String[]> users = new HashMap<>();

    static {
        // Pre-populated users (userID: [password, role, email])
        users.put("1003", new String[]{"developer123", "Developer", "Angelica@MotorPH.com"});
        users.put("1002", new String[]{"manager123", "Manager", "Charlize@MotorPH.com"});
        users.put("1001", new String[]{"Satsuma18", "Developer", "Colin@MotorPH.com"});
        users.put("Guest", new String[]{"Guest", "Guest", "Guest@MotorPH.com"});
    }

    public static void showLoginScreen(JFrame parentFrame) {
        JFrame loginFrame = new JFrame("Employee Login");
        loginFrame.setSize(800, 600);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setLocationRelativeTo(parentFrame);
        

        JPanel Panel1 = new JPanel();
        JPanel Panel2 = new JPanel();
        Panel1.setBackground(Color.BLUE);
        Panel1.setPreferredSize(new Dimension(25,25));
        Panel2.setBackground(Color.GRAY);
        Panel1.setPreferredSize(new Dimension(10,10));
        

        loginFrame.add(Panel1,BorderLayout.SOUTH);
        loginFrame.add(Panel2,BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.setBackground(Color.YELLOW);
        

        JLabel companyName = new JLabel("MOTORPH");
        companyName.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(companyName, gbc);

        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        

        JLabel guestLabel = new JLabel("<html><i>Don't have an account? Use \"Guest\" for both ID and Password</i></html>");
        guestLabel.setForeground(Color.GRAY);
        gbc.gridy++;
        panel.add(guestLabel, gbc);


        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("User ID:"), gbc);
        JTextField userIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String userId = userIdField.getText();
                String password = new String(passwordField.getPassword());

                if (users.containsKey(userId) && users.get(userId)[0].equals(password)) {
                    loginFrame.dispose();
                    String role = users.get(userId)[1];
                    String email = users.get(userId)[2];
                    Main.showMainScreen(userId, role, email);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials!", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }
}