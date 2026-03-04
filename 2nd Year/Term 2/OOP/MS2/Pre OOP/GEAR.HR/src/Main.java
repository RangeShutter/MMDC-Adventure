import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main class for the Employee Management System
 * Provides the central dashboard with navigation to different modules
 * Note: All information in this program are sample data for demonstration purposes
 */
public class Main {
    // Current user session information
    private static String currentUserId;
    private static String currentUserRole;
    private static String currentUserEmail;

    // Application color scheme
    private static final Color BACKGROUND_WHITE = Color.WHITE;
    private static final Color HEADER_DARK = new Color(34, 34, 34);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color BORDER_GREY = new Color(68, 68, 68);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(180, 180, 180);
    private static final Color TEXT_BLACK = Color.BLACK;
    private static final Color ACCENT_GREY = new Color(120, 120, 120);
    private static final Color BUTTON_ORANGE = new Color(255, 153, 28);
    private static final Color GRADIENT_START = new Color(93, 224, 230);
    private static final Color GRADIENT_END = new Color(0, 74, 173);

    /**
     * Displays the main application dashboard after successful login.
     * Sets up the main navigation interface with access to all modules.
     *
     * @param userId The logged-in user's ID
     * @param role The user's role (e.g., Developer, Manager, HR)
     * @param email The user's email address
     */
    public static void showMainScreen(String userId, String role, String email) {
        // Fetch role and email from user_credentials.csv for the given userId
        String[] roleAndEmail = User.getRoleAndEmail(userId);
        String actualRole = roleAndEmail[0];
        String actualEmail = roleAndEmail[1];
        currentUserId = userId;
        currentUserRole = actualRole;
        currentUserEmail = actualEmail;

        // Create and configure main application frame
        JFrame mainFrame = createMainFrame();
        
        // Create menu bar with logout functionality
        JMenuBar menuBar = createMenuBar(mainFrame);
        mainFrame.setJMenuBar(menuBar);

        // Create main panel with modern design
        JPanel mainPanel = createMainPanel();
        
        // Create and add header panel
        JPanel headerPanel = createHeaderPanel(userId, actualRole, actualEmail);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create and add content panel with feature cards
        JPanel contentPanel = createContentPanel(mainFrame, userId, actualRole);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Create and add footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    /**
     * Creates and configures the main application frame.
     * Sets up window properties, icon, and positioning.
     *
     * @return Configured JFrame for main application
     */
    private static JFrame createMainFrame() {
        JFrame mainFrame = new JFrame("Employee Management System");
        mainFrame.setSize(1000, 750);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        // Set application icon if available
        try {
            mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        return mainFrame;
    }

    /**
     * Creates the main panel with background styling.
     *
     * @return JPanel with BorderLayout and white background
     */
    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        return mainPanel;
    }

    /**
     * Creates a menu bar with logout functionality.
     * Provides File menu with logout option.
     *
     * @param mainFrame The parent frame for logout handling
     * @return JMenuBar with logout functionality
     */
    private static JMenuBar createMenuBar(JFrame mainFrame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(128, 128, 128));
        menuBar.setBorderPainted(false);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(TEXT_WHITE);
        fileMenu.setFont(new Font("Garet", Font.PLAIN, 14));
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setFont(new Font("Garet", Font.PLAIN, 12));
        logoutItem.addActionListener(e -> handleLogout(mainFrame));
        
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);

        return menuBar;
    }

    /**
     * Handles user logout with confirmation dialog.
     * Closes main application and returns to login screen.
     *
     * @param mainFrame The main application frame to close
     */
    private static void handleLogout(JFrame mainFrame) {
        int confirm = JOptionPane.showConfirmDialog(
            mainFrame, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            mainFrame.dispose();
            User.showLoginScreen(null);
        }
    }

    /**
     * Creates the header panel with gradient background and user information.
     * Displays company branding and current user details.
     *
     * @param userId The current user's ID
     * @param role The current user's role
     * @param email The current user's email
     * @return JPanel with gradient background and user information
     */
    private static JPanel createHeaderPanel(String userId, String role, String email) {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
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
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));

        // Create title section with logo
        JPanel titleSection = createTitleSection();
        
        // Create user information section
        JPanel userInfoSection = createUserInfoSection(role, userId, email);

        // Combine title and user info
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setOpaque(false);
        headerContent.add(titleSection, BorderLayout.CENTER);
        headerContent.add(userInfoSection, BorderLayout.SOUTH);

        headerPanel.add(headerContent, BorderLayout.CENTER);
        return headerPanel;
    }

    /**
     * Creates the title section with company logo and branding.
     *
     * @return JPanel with company title and subtitle
     */
    private static JPanel createTitleSection() {
        JLabel titleLabel = createTitleLabel();
        JLabel subtitleLabel = createSubtitleLabel();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        return titlePanel;
    }

    /**
     * Creates the title label with company logo or fallback text.
     *
     * @return JLabel with company name and logo
     */
    private static JLabel createTitleLabel() {
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/3.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(290, 90, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedLogo);
            JLabel titleLabel = new JLabel(resizedIcon);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return titleLabel;
        } catch (Exception e) {
            // Fallback to text if logo not found
            JLabel titleLabel = new JLabel("GEAR.HR");
            titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
            titleLabel.setForeground(TEXT_WHITE);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return titleLabel;
        }
    }

    /**
     * Creates the subtitle label with application description.
     *
     * @return JLabel with application subtitle
     */
    private static JLabel createSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Choose a module to manage");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    /**
     * Creates the user information section displaying current user details.
     *
     * @param role The current user's role
     * @param userId The current user's ID
     * @param email The current user's email
     * @return JPanel with user information display
     */
    private static JPanel createUserInfoSection(String role, String userId, String email) {
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        userInfoPanel.setOpaque(false);
        
        JLabel roleLabel = new JLabel(role + ": " + userId);
        roleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        roleLabel.setForeground(TEXT_WHITE);
        
        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        emailLabel.setForeground(TEXT_WHITE);
        
        userInfoPanel.add(roleLabel);
        userInfoPanel.add(new JLabel("|"));
        userInfoPanel.add(emailLabel);

        return userInfoPanel;
    }

    /**
     * Creates the main content panel with feature cards.
     * Displays navigation cards for different system modules.
     *
     * @param mainFrame The parent frame for navigation
     * @param userId The current user's ID
     * @param role The current user's role
     * @return JPanel with feature cards
     */
    private static JPanel createContentPanel(JFrame mainFrame, String userId, String role) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(15, 15, 15, 15);

        // Create attendance management card
        JPanel attendanceCard = createFeatureCard(
            "Attendance Management",
            "Track employee attendance, view reports, and manage time records",
            "Manage Attendance",
            e -> Attendance.showAttendanceScreen(mainFrame, userId, role),
            BUTTON_ORANGE
        );

        // Create employee profile card
        JPanel profileCard = createFeatureCard(
            "Employee Profiles",
            "View, add, edit, and manage employee information and payroll",
            "Manage Profiles",
            e -> EmployeeProfile.showProfileScreen(mainFrame, userId, role),
            BUTTON_ORANGE
        );

        // Add cards to content panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(attendanceCard, constraints);

        constraints.gridx = 1;
        contentPanel.add(profileCard, constraints);

        return contentPanel;
    }

    /**
     * Creates a feature card with title, description, and action button.
     * Provides consistent styling for navigation cards.
     *
     * @param title The card title
     * @param description The card description
     * @param buttonText The button text
     * @param action The action to perform when button is clicked
     * @param buttonColor The button background color
     * @return JPanel representing a feature card
     */
    private static JPanel createFeatureCard(String title, String description, String buttonText, 
                                          ActionListener action, Color buttonColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 40;
                g2d.setColor(CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2d.setColor(BORDER_GREY);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
                g2d.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(350, 250));

        // Create title label
        JLabel titleLabel = createCardTitleLabel(title);

        // Create description area
        JTextArea descriptionArea = createCardDescriptionArea(description);

        // Create action button
        JButton actionButton = createCardButton(buttonText, buttonColor, action);

        // Layout components
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(titleLabel, BorderLayout.NORTH);
        topSection.add(descriptionArea, BorderLayout.CENTER);

        card.add(topSection, BorderLayout.CENTER);
        card.add(actionButton, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Creates a title label for feature cards.
     *
     * @param title The title text
     * @return JLabel with card title styling
     */
    private static JLabel createCardTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    /**
     * Creates a description area for feature cards.
     *
     * @param description The description text
     * @return JTextArea with card description styling
     */
    private static JTextArea createCardDescriptionArea(String description) {
        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Garet", Font.PLAIN, 14));
        descriptionArea.setForeground(ACCENT_GREY);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return descriptionArea;
    }

    /**
     * Creates an action button for feature cards.
     *
     * @param buttonText The button text
     * @param buttonColor The button background color
     * @param action The action to perform
     * @return JButton with card button styling
     */
    private static JButton createCardButton(String buttonText, Color buttonColor, ActionListener action) {
        JButton actionButton = createStyledButton(buttonText, buttonColor, TEXT_WHITE);
        actionButton.addActionListener(action);
        return actionButton;
    }

    /**
     * Creates a styled button with custom colors and rounded corners.
     *
     * @param text The button text
     * @param backgroundColor The background color
     * @param foregroundColor The text color
     * @return JButton with custom styling
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
        button.setFont(new Font("Garet", Font.BOLD, 16));
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 56));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates the footer panel with copyright information.
     *
     * @return JPanel with footer content
     */
    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout()) {
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
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel copyrightLabel = new JLabel("© 2025 GEAR.HR - All rights reserved");
        copyrightLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        copyrightLabel.setForeground(TEXT_WHITE);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel versionLabel = new JLabel("Version 2.0 - Enhanced UI & Functionality");
        versionLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        versionLabel.setForeground(TEXT_GREY);
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(copyrightLabel, BorderLayout.NORTH);
        footerPanel.add(versionLabel, BorderLayout.SOUTH);
        return footerPanel;
    }

    /**
     * Main entry point for the application.
     * Displays splash screen and then login screen.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Show splash screen first, then launch login
        SplashScreen.showSplash(() -> User.showLoginScreen(null));
    }
}