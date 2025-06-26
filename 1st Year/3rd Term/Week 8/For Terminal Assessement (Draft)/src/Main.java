import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/* 
 * Main class for the Employee Management System
 * Note: All information in this program are sample data for demonstration purposes
 */
public class Main {
    // Static variables to store current user information
    private static String currentUserId;
    private static String currentUserRole;
    private static String currentUserEmail;

    // Modern formal color scheme (matching User.java)
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color HEADER_DARK = new Color(34, 34, 34); // #222222
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color BORDER_GREY = new Color(68, 68, 68); // #444444
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(180, 180, 180);
    private static final Color TEXT_BLACK = Color.BLACK;
    private static final Color ACCENT = new Color(120, 120, 120); // Subtle accent
    private static final Color BUTTON_BLUE = new Color(52, 152, 219); // #3498db

    /**
     * Displays the main application screen after successful login
     * @param userId The logged-in user's ID
     * @param role The user's role (e.g., Admin, HR, Employee)
     * @param email The user's email address
     */
    public static void showMainScreen(String userId, String role, String email) {
        // Store current user information
        currentUserId = userId;
        currentUserRole = role;
        currentUserEmail = email;

        // Create and configure main application frame
        JFrame frame = new JFrame("Employee Management System");
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setResizable(false);

        // Set application icon (if available)
        try {
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }

        // Create menu bar with File menu
        JMenuBar menuBar = createMenuBar(frame);
        frame.setJMenuBar(menuBar);

        // Create main panel with modern design
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BG_WHITE);

        // Create header panel
        JPanel headerPanel = createHeaderPanel(userId, role, email);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create content panel with cards
        JPanel contentPanel = createContentPanel(frame, userId, role);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Create footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Creates a modern menu bar
     */
    private static JMenuBar createMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BUTTON_BLUE);
        menuBar.setBorderPainted(false);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(TEXT_WHITE);
        fileMenu.setFont(new Font("Garet", Font.PLAIN, 14));
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setFont(new Font("Garet", Font.PLAIN, 12));
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                frame, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                User.showLoginScreen(null);
            }
        });
        
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);

        return menuBar;
    }

    /**
     * Creates a modern header panel with user information
     */
    private static JPanel createHeaderPanel(String userId, String role, String email) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));

        JLabel titleLabel = new JLabel("GEAR.HR");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Choose a module to manage");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        // User info panel (optional, can be below title)
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        userInfoPanel.setOpaque(false);
        JLabel userLabel = new JLabel(role + ": " + userId);
        userLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        userLabel.setForeground(TEXT_WHITE);
        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        emailLabel.setForeground(TEXT_WHITE);
        userInfoPanel.add(userLabel);
        userInfoPanel.add(new JLabel("|"));
        userInfoPanel.add(emailLabel);

        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setOpaque(false);
        headerContent.add(titlePanel, BorderLayout.CENTER);
        headerContent.add(userInfoPanel, BorderLayout.SOUTH);

        headerPanel.add(headerContent, BorderLayout.CENTER);
        return headerPanel;
    }

    /**
     * Creates the main content panel with feature cards
     */
    private static JPanel createContentPanel(JFrame frame, String userId, String role) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BG_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Create feature cards
        JPanel attendanceCard = createFeatureCard(
            "Attendance Management",
            "Track employee attendance, view reports, and manage time records",
            "Manage Attendance",
            e -> Attendance.showAttendanceScreen(frame, userId, role),
            BUTTON_BLUE
        );

        JPanel profileCard = createFeatureCard(
            "Employee Profiles",
            "View, add, edit, and manage employee information and payroll",
            "Manage Profiles",
            e -> EmployeeProfile.showProfileScreen(frame, userId, role),
            BUTTON_BLUE
        );

        // Add cards to content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(attendanceCard, gbc);

        gbc.gridx = 1;
        contentPanel.add(profileCard, gbc);

        return contentPanel;
    }

    /**
     * Creates a modern feature card with hover effects
     */
    private static JPanel createFeatureCard(String title, String description, String buttonText, 
                                          ActionListener action, Color buttonColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 40;
                g2.setColor(CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.setColor(BORDER_GREY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(350, 250));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Garet", Font.PLAIN, 14));
        descArea.setForeground(ACCENT);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Button
        JButton actionButton = createModernButton(buttonText, buttonColor, TEXT_WHITE);
        actionButton.addActionListener(action);

        // Layout components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(descArea, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.CENTER);
        card.add(actionButton, BorderLayout.SOUTH);

        return card;
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
                int arc = getHeight();
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Garet", Font.BOLD, 16));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 56));
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
        JLabel footerLabel = new JLabel("© 2025 GEAR.HR - All rights reserved");
        footerLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        footerLabel.setForeground(TEXT_WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel versionLabel = new JLabel("Version 2.0 - Enhanced UI & Functionality");
        versionLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        versionLabel.setForeground(TEXT_GREY);
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel, BorderLayout.NORTH);
        footerPanel.add(versionLabel, BorderLayout.SOUTH);
        return footerPanel;
    }

    /**
     * Main entry point for the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Start the application by showing the login screen
        User.showLoginScreen(null);
    }
}