package ui;

import service.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main class for the Employee Management System. Central dashboard with navigation to modules.
 */
public class Main {
    private static String currentUserId;
    private static String currentUserRole;
    private static String currentUserEmail;
    private static ApplicationContext appContext;

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

    public static void showMainScreen(String userId, String role, String email, ApplicationContext ctx) {
        appContext = ctx;
        currentUserId = userId;
        currentUserRole = role;
        currentUserEmail = email;

        JFrame mainFrame = createMainFrame();
        JMenuBar menuBar = createMenuBar(mainFrame);
        mainFrame.setJMenuBar(menuBar);
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createHeaderPanel(userId, role, email), BorderLayout.NORTH);
        JPanel contentPanel = createContentPanel(mainFrame, userId, role, ctx);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private static JFrame createMainFrame() {
        JFrame mainFrame = new JFrame("Employee Management System");
        mainFrame.setSize(1000, 750);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        try {
            mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) {}
        return mainFrame;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        return mainPanel;
    }

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

    private static void handleLogout(JFrame mainFrame) {
        int confirm = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            mainFrame.dispose();
            User.showLoginScreen(null, appContext);
        }
    }

    private static JPanel createHeaderPanel(String userId, String role, String email) {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START, getWidth(), 0, GRADIENT_END);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));
        JPanel titleSection = createTitleSection();
        JPanel userInfoSection = createUserInfoSection(role, userId, email);
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setOpaque(false);
        headerContent.add(titleSection, BorderLayout.CENTER);
        headerContent.add(userInfoSection, BorderLayout.SOUTH);
        headerPanel.add(headerContent, BorderLayout.CENTER);
        return headerPanel;
    }

    private static JPanel createTitleSection() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(createTitleLabel(), BorderLayout.NORTH);
        titlePanel.add(createSubtitleLabel(), BorderLayout.CENTER);
        return titlePanel;
    }

    private static JLabel createTitleLabel() {
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/3.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(290, 90, Image.SCALE_SMOOTH);
            JLabel titleLabel = new JLabel(new ImageIcon(resizedLogo));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return titleLabel;
        } catch (Exception e) {
            JLabel titleLabel = new JLabel("GEAR.HR");
            titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
            titleLabel.setForeground(TEXT_WHITE);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return titleLabel;
        }
    }

    private static JLabel createSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Choose a module to manage");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

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

    private static JPanel createContentPanel(JFrame mainFrame, String userId, String role, ApplicationContext ctx) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(15, 15, 15, 15);

        JPanel attendanceCard = createFeatureCard(
            "Attendance Management",
            "Track employee attendance, view reports, and manage time records",
            "Manage Attendance",
            e -> AttendanceScreen.showAttendanceScreen(mainFrame, userId, role, ctx.getAttendanceService(), ctx.getEmployeeService()),
            BUTTON_ORANGE
        );
        JPanel profileCard = createFeatureCard(
            "Employee Profiles",
            "View, add, edit, and manage employee information and payroll",
            "Manage Profiles",
            e -> EmployeeProfile.showProfileScreen(mainFrame, userId, role, ctx.getEmployeeService(), ctx.getAttendanceService(), ctx.getPayrollProcessor()),
            BUTTON_ORANGE
        );
        JPanel leaveCard = createFeatureCard(
            "Leave Management",
            "View, submit, and update leave requests",
            "Manage Leave",
            e -> LeaveManagementScreen.showLeaveScreen(mainFrame, userId, role, ctx.getLeaveService(), ctx.getEmployeeService()),
            BUTTON_ORANGE
        );

        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPanel.add(attendanceCard, constraints);
        constraints.gridx = 1;
        contentPanel.add(profileCard, constraints);
        constraints.gridx = 2;
        contentPanel.add(leaveCard, constraints);
        return contentPanel;
    }

    private static JPanel createFeatureCard(String title, String description, String buttonText, ActionListener action, Color buttonColor) {
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
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Garet", Font.PLAIN, 14));
        descriptionArea.setForeground(ACCENT_GREY);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton actionButton = new JButton(buttonText) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(buttonColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        actionButton.setFont(new Font("Garet", Font.BOLD, 16));
        actionButton.setForeground(TEXT_WHITE);
        actionButton.setBackground(buttonColor);
        actionButton.setOpaque(false);
        actionButton.setBorderPainted(false);
        // Slightly smaller button to reduce visual footprint on the dashboard
        actionButton.setPreferredSize(new Dimension(180, 44));
        actionButton.addActionListener(action);
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(titleLabel, BorderLayout.NORTH);
        topSection.add(descriptionArea, BorderLayout.CENTER);
        card.add(topSection, BorderLayout.CENTER);
        card.add(actionButton, BorderLayout.SOUTH);
        return card;
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START, getWidth(), 0, GRADIENT_END);
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

    public static void main(String[] args) {
        ApplicationContext ctx = new ApplicationContext();
        SplashScreen.showSplash(() -> User.showLoginScreen(null, ctx));
    }
}
