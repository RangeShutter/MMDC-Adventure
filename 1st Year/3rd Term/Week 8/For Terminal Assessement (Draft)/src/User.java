import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;

/**
 * User class handles user authentication and login functionality
 * Provides a modern login interface with credential validation
 * Note: All information in this program are sample data for demonstration purposes
 */
public class User {
    // User credentials storage: userId -> [password, role, email]
    private static final HashMap<String, String[]> userCredentials = new HashMap<>();

    // Application color scheme
    private static final Color BACKGROUND_WHITE = Color.WHITE;
    private static final Color HEADER_DARK = new Color(34, 34, 34);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(180, 180, 180);
    private static final Color TEXT_BLACK = Color.BLACK;
    private static final Color ACCENT_GREY = new Color(120, 120, 120);
    private static final Color LOGIN_BUTTON_ORANGE = new Color(255, 153, 28);
    private static final Color GRADIENT_START = new Color(93, 224, 230);
    private static final Color GRADIENT_END = new Color(0, 74, 173);

    // Initialize sample user data
    static {
        initializeSampleUsers();
    }

    /**
     * Initializes sample user credentials for demonstration
     */
    private static void initializeSampleUsers() {
        userCredentials.put("1003", new String[]{"developer123", "Developer", "Angelica@MotorPH.com"});
        userCredentials.put("1002", new String[]{"manager123", "Manager", "Charlize@MotorPH.com"});
        userCredentials.put("1001", new String[]{"Satsuma18", "Developer", "Colin@MotorPH.com"});
        userCredentials.put("Guest", new String[]{"Guest", "Guest", "Guest@MotorPH.com"});
    }

    /**
     * Displays the main login screen for the application
     * Creates a split-panel design with gradient background and login form
     * @param parentFrame The parent frame for positioning (can be null)
     */
    public static void showLoginScreen(JFrame parentFrame) {
        JFrame loginFrame = createLoginFrame(parentFrame);
        JPanel mainPanel = createMainPanel();
        
        // Create left panel with gradient background and logo
        JPanel leftPanel = createLeftPanel();
        
        // Create right panel with login form
        JPanel rightPanel = createRightPanel(loginFrame);
        
        // Assemble the interface
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);
    }

    /**
     * Creates and configures the main login frame
     */
    private static JFrame createLoginFrame(JFrame parentFrame) {
        JFrame loginFrame = new JFrame("GEAR.HR - Login");
        loginFrame.setSize(1000, 750);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(parentFrame);
        loginFrame.setResizable(false);
        
        // Set application icon if available
        try {
            loginFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        return loginFrame;
    }

    /**
     * Creates the main panel with split layout
     */
    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setPreferredSize(new Dimension(1000, 750));
        return mainPanel;
    }

    /**
     * Creates the left panel with gradient background, logo, and branding
     */
    private static JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, GRADIENT_START,
                    getWidth(), 0, GRADIENT_END
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        // Add logo
        JLabel logoLabel = createLogoLabel();
        leftPanel.add(logoLabel, BorderLayout.CENTER);

        // Add header text
        JLabel headerLabel = createHeaderLabel();
        leftPanel.add(headerLabel, BorderLayout.NORTH);

        // Add footer text
        JLabel footerLabel = createFooterLabel();
        leftPanel.add(footerLabel, BorderLayout.SOUTH);

        return leftPanel;
    }

    /**
     * Creates the logo label with company logo or fallback text
     */
    private static JLabel createLogoLabel() {
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/Icon.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedLogo);
            JLabel logoLabel = new JLabel(resizedIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return logoLabel;
        } catch (Exception e) {
            // Fallback to text if logo not found
            JLabel logoLabel = new JLabel("GEAR.HR");
            logoLabel.setFont(new Font("Garet", Font.BOLD, 32));
            logoLabel.setForeground(TEXT_WHITE);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return logoLabel;
        }
    }

    /**
     * Creates the header welcome text
     */
    private static JLabel createHeaderLabel() {
        JLabel headerLabel = new JLabel("Welcome to GEAR.HR");
        headerLabel.setFont(new Font("Garet", Font.BOLD, 22));
        headerLabel.setForeground(TEXT_WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return headerLabel;
    }

    /**
     * Creates the footer copyright text
     */
    private static JLabel createFooterLabel() {
        JLabel footerLabel = new JLabel("© 2025 GEAR.HR    |    Secure Login");
        footerLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        footerLabel.setForeground(TEXT_WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return footerLabel;
    }

    /**
     * Creates the right panel containing the login form
     */
    private static JPanel createRightPanel(JFrame loginFrame) {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BACKGROUND_WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;

        // Add title and subtitle
        JLabel titleLabel = createTitleLabel();
        rightPanel.add(titleLabel, constraints);

        constraints.gridy++;
        JLabel subtitleLabel = createSubtitleLabel();
        rightPanel.add(subtitleLabel, constraints);

        // Add username field
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        JLabel usernameLabel = createFieldLabel("Username:");
        rightPanel.add(usernameLabel, constraints);
        
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 20, 0);
        JTextField usernameField = createTextField();
        rightPanel.add(usernameField, constraints);
        constraints.insets = new Insets(10, 0, 10, 0);

        // Add password field
        constraints.gridy++;
        JLabel passwordLabel = createFieldLabel("Password:");
        rightPanel.add(passwordLabel, constraints);
        
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 20, 0);
        JPasswordField passwordField = createPasswordField();
        rightPanel.add(passwordField, constraints);
        constraints.insets = new Insets(10, 0, 10, 0);

        // Add guest access info
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        JLabel guestInfoLabel = createGuestInfoLabel();
        rightPanel.add(guestInfoLabel, constraints);

        // Add login button
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        JButton loginButton = createLoginButton(loginFrame, usernameField, passwordField);
        rightPanel.add(loginButton, constraints);

        return rightPanel;
    }

    /**
     * Creates the main title label
     */
    private static JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    /**
     * Creates the subtitle label
     */
    private static JLabel createSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Please enter your credentials");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 14));
        subtitleLabel.setForeground(ACCENT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    /**
     * Creates a field label with consistent styling
     */
    private static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Garet", Font.BOLD, 14));
        label.setForeground(TEXT_BLACK);
        return label;
    }

    /**
     * Creates a styled text field for input
     */
    private static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Garet", Font.PLAIN, 14));
        textField.setBackground(BACKGROUND_WHITE);
        textField.setForeground(TEXT_BLACK);
        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        textField.setPreferredSize(new Dimension(260, 32));
        return textField;
    }

    /**
     * Creates a styled password field for secure input
     */
    private static JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Garet", Font.PLAIN, 14));
        passwordField.setBackground(BACKGROUND_WHITE);
        passwordField.setForeground(TEXT_BLACK);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        passwordField.setPreferredSize(new Dimension(260, 32));
        return passwordField;
    }

    /**
     * Creates the guest access information label
     */
    private static JLabel createGuestInfoLabel() {
        JLabel guestLabel = new JLabel("Guest Access: Use 'Guest' for both ID and Password");
        guestLabel.setFont(new Font("Garet", Font.ITALIC, 12));
        guestLabel.setForeground(ACCENT_GREY);
        guestLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return guestLabel;
    }

    /**
     * Creates the login button with authentication logic
     */
    private static JButton createLoginButton(JFrame loginFrame, JTextField usernameField, JPasswordField passwordField) {
        JButton loginButton = createStyledButton("Sign In", LOGIN_BUTTON_ORANGE, TEXT_WHITE);
        loginButton.setPreferredSize(new Dimension(120, 36));
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser(loginFrame, usernameField, passwordField);
            }
        });
        
        return loginButton;
    }

    /**
     * Authenticates user credentials and handles login success/failure
     */
    private static void authenticateUser(JFrame loginFrame, JTextField usernameField, JPasswordField passwordField) {
        String userId = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (userCredentials.containsKey(userId) && userCredentials.get(userId)[0].equals(password)) {
            // Login successful
            loginFrame.dispose();
            String role = userCredentials.get(userId)[1];
            String email = userCredentials.get(userId)[2];
            Main.showMainScreen(userId, role, email);
        } else {
            // Login failed
            JOptionPane.showMessageDialog(loginFrame, 
                "Invalid credentials! Please check your User ID and Password.", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a modern styled button with rounded corners
     */
    private static JButton createStyledButton(String text, Color backgroundColor, Color foregroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = getHeight();
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Garet", Font.BOLD, 14));
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}