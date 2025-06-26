import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;

/*
 * User class handles user authentication and login functionality
 * Note: All information in this program are sample data for demonstration purposes
 */
public class User {
    // Stores user credentials and information (userID: [password, role, email])
    private static HashMap<String, String[]> users = new HashMap<>();

    // Modern formal color scheme
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color HEADER_DARK = new Color(34, 34, 34); // #222222
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color BORDER_GREY = new Color(68, 68, 68); // #444444
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(180, 180, 180);
    private static final Color TEXT_BLACK = Color.BLACK;
    private static final Color ACCENT = new Color(120, 120, 120); // Subtle accent
    private static final Color BUTTON_BLUE = new Color(52, 152, 219); // #3498db

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
        JFrame loginFrame = new JFrame("GEAR.HR - Login");
        loginFrame.setSize(1000, 750);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(parentFrame);
        loginFrame.setResizable(false);

        // Set application icon (if available)
        try {
            loginFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        } catch (Exception e) {}

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BG_WHITE);

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel loginCardPanel = createLoginCardPanel(loginFrame);
        loginCardPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(loginCardPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);
    }

    /**
     * Creates a modern header panel
     */
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));

        JLabel titleLabel = new JLabel("GEAR.HR");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Please sign in to continue");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    /**
     * Creates the main login card panel
     */
    private static JPanel createLoginCardPanel(JFrame loginFrame) {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(BG_WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        JPanel loginCard = createLoginCard(loginFrame);
        cardPanel.add(loginCard);
        return cardPanel;
    }

    /**
     * Creates the login card with form elements
     */
    private static JPanel createLoginCard(JFrame loginFrame) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 40; // Fixed arc for rounded rectangle
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.setColor(BORDER_GREY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setOpaque(false); // We'll paint the background ourselves
        card.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        card.setPreferredSize(new Dimension(500, 450));

        // Add shadow effect (optional, for modern look)
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            card.getBorder()
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);

        // Logo
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/Icon.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(180, 70, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedLogo);
            JLabel logoLabel = new JLabel(resizedIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            formPanel.add(logoLabel, gbc);
        } catch (Exception e) {
            System.out.println("Logo not found: " + e.getMessage());
        }

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Garet", Font.BOLD, 22));
        welcomeLabel.setForeground(TEXT_BLACK);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(welcomeLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Please sign in to continue");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 14));
        subtitleLabel.setForeground(ACCENT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        formPanel.add(subtitleLabel, gbc);

        // Username field
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Garet", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_BLACK);
        formPanel.add(usernameLabel, gbc);
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Garet", Font.PLAIN, 14));
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setForeground(TEXT_BLACK);
        usernameField.setOpaque(true);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Garet", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_BLACK);
        formPanel.add(passwordLabel, gbc);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Garet", Font.PLAIN, 14));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setForeground(TEXT_BLACK);
        passwordField.setOpaque(true);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Guest access information
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel guestLabel = new JLabel("Guest Access: Use 'Guest' for both ID and Password");
        guestLabel.setFont(new Font("Garet", Font.ITALIC, 12));
        guestLabel.setForeground(ACCENT);
        guestLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(guestLabel, gbc);

        // Login button
        gbc.gridy = 6;
        JButton loginButton = createModernButton("Sign In", BUTTON_BLUE, TEXT_WHITE);
        formPanel.add(loginButton, gbc);

        // Login button action handler
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (users.containsKey(userId) && users.get(userId)[0].equals(password)) {
                    loginFrame.dispose();
                    String role = users.get(userId)[1];
                    String email = users.get(userId)[2];
                    Main.showMainScreen(userId, role, email);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, 
                        "Invalid credentials! Please check your User ID and Password.", 
                        "Login Error", 
                        JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    passwordField.requestFocus();
                }
            }
        });

        // Add enter key listener for quick login
        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        };
        usernameField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
        usernameField.getActionMap().put("enter", enterAction);
        passwordField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
        passwordField.getActionMap().put("enter", enterAction);

        card.add(formPanel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Creates a modern styled text field
     */
    private static JTextField createModernTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }

    /**
     * Creates a modern styled password field
     */
    private static JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }

    /**
     * Creates a modern styled button
     */
    private static JButton createModernButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = getHeight(); // Use full height for a pill/oval look
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Garet", Font.BOLD, 16));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setOpaque(false); // Let paintComponent handle background
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 56)); // Wider and taller for a pill look
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a footer panel with system information
     */
    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(HEADER_DARK);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel footerLabel = new JLabel("© 2025 GEAR.HR - Secure Login");
        footerLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        footerLabel.setForeground(TEXT_WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel versionLabel = new JLabel("Version 2.0 - Enhanced Security & UI & Functionality");
        versionLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        versionLabel.setForeground(TEXT_GREY);
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel, BorderLayout.NORTH);
        footerPanel.add(versionLabel, BorderLayout.SOUTH);
        return footerPanel;
    }
}