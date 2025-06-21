import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/*
 * User class handles user authentication and login functionality
 * Note: All information in this program are sample data for demonstration purposes
 */
public class User {
    // Stores user credentials and information (userID: [password, role, email])
    private static HashMap<String, String[]> users = new HashMap<>();

    // Static initializer - loads sample user data
    static {
        // Pre-populated users (userID: [password, role, email])
        users.put("1003", new String[]{"developer123", "Developer", "Angelica@MotorPH.com"});
        users.put("1002", new String[]{"manager123", "Manager", "Charlize@MotorPH.com"});
        users.put("1001", new String[]{"Satsuma18", "Developer", "Colin@MotorPH.com"});
        users.put("Guest", new String[]{"Guest", "Guest", "Guest@MotorPH.com"});
    }

    /**
     * Displays the login screen for the application
     * @param parentFrame The parent frame for positioning (can be null)
     */
    public static void showLoginScreen(JFrame parentFrame) {
        // Create and configure login frame
        JFrame loginFrame = new JFrame("Employee Login");
        loginFrame.setSize(800, 600);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());
        loginFrame.setLocationRelativeTo(parentFrame);
        
        // Decorative panels (visual elements only)
        JPanel Panel1 = new JPanel();
        JPanel Panel2 = new JPanel();
        Panel1.setBackground(Color.BLUE);
        Panel1.setPreferredSize(new Dimension(25,25));
        Panel2.setBackground(Color.GRAY);
        Panel1.setPreferredSize(new Dimension(10,10));
        
        loginFrame.add(Panel1,BorderLayout.SOUTH);
        loginFrame.add(Panel2,BorderLayout.NORTH);

        // Main login panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        panel.setBackground(Color.YELLOW);
        
        // Company name label
        JLabel companyName = new JLabel("MOTORPH");
        companyName.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(companyName, gbc);

        // System title label
        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Guest access information
        JLabel guestLabel = new JLabel("<html><i>Don't have an account? Use \"Guest\" for both ID and Password</i></html>");
        guestLabel.setForeground(Color.GRAY);
        gbc.gridy++;
        panel.add(guestLabel, gbc);

        // User ID input field
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("User ID:"), gbc);
        JTextField userIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);

        // Password input field
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        // Login button action handler
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String password = new String(passwordField.getPassword());

                // Check credentials
                if (users.containsKey(userId) && users.get(userId)[0].equals(password)) {
                    loginFrame.dispose(); // Close login window
                    String role = users.get(userId)[1];
                    String email = users.get(userId)[2];
                    Main.showMainScreen(userId, role, email); // Open main application
                } else {
                    JOptionPane.showMessageDialog(loginFrame, 
                        "Invalid credentials!", 
                        "Login Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }
}