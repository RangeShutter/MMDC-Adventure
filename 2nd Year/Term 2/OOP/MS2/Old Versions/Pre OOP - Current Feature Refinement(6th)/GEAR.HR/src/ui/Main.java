package ui;

import service.ApplicationContext;

import javax.swing.*;
import java.awt.*;
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
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color BUTTON_ORANGE = new Color(255, 153, 28);
    private static final Color GRADIENT_START = new Color(93, 224, 230);
    private static final Color GRADIENT_END = new Color(0, 74, 173);

    public static void showMainScreen(String userId, String role, String email, ApplicationContext ctx) {
        appContext = ctx;
        currentUserId = userId;
        currentUserRole = role;
        currentUserEmail = email;

        JFrame mainFrame = createMainFrame();
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createSidebarPanel(mainFrame, userId, role, email, ctx), BorderLayout.WEST);
        mainPanel.add(createContentPanel(mainFrame, userId, role, email, ctx), BorderLayout.CENTER);
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

    private static void handleLogout(JFrame mainFrame) {
        int confirm = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            mainFrame.dispose();
            User.showLoginScreen(null, appContext);
        }
    }

    private static JPanel createSidebarPanel(JFrame mainFrame, String userId, String role, String email, ApplicationContext ctx) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(HEADER_DARK);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Garet", Font.BOLD, 12));
        emailLabel.setForeground(TEXT_WHITE);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Garet", Font.PLAIN, 10));
        roleLabel.setForeground(TEXT_WHITE);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(emailLabel);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(roleLabel);
        sidebar.add(Box.createVerticalStrut(16));

        JButton attendanceBtn = createSidebarButton("Attendance Management", e ->
            AttendanceScreen.showAttendanceScreen(mainFrame, userId, role, ctx.getAttendanceService(), ctx.getEmployeeService()));
        JButton profileBtn = createSidebarButton("Employee Profile", e ->
            EmployeeProfile.showProfileScreen(mainFrame, userId, role, ctx.getEmployeeService(), ctx.getAttendanceService(), ctx.getPayrollProcessor()));
        JButton leaveBtn = createSidebarButton("Leave Management", e ->
            LeaveManagementScreen.showLeaveScreen(mainFrame, userId, role, ctx.getLeaveService(), ctx.getEmployeeService()));

        sidebar.add(attendanceBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(profileBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(leaveBtn);
        sidebar.add(Box.createVerticalGlue());
        JButton logoutBtn = createSidebarButton("Logout", e -> handleLogout(mainFrame));
        sidebar.add(logoutBtn);
        return sidebar;
    }

    private static JButton createSidebarButton(String text, ActionListener action) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(BUTTON_ORANGE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Garet", Font.BOLD, 11));
        btn.setForeground(TEXT_WHITE);
        btn.setBackground(BUTTON_ORANGE);
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.addActionListener(action);
        return btn;
    }

    private static JPanel createContentPanel(JFrame mainFrame, String userId, String role, String email, ApplicationContext ctx) {
        JPanel contentPanel = new JPanel(new BorderLayout()) {
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
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel centerContent = new JPanel(new BorderLayout());
        centerContent.setOpaque(false);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        JLabel logoLabel = createContentLogoLabel();
        logoPanel.add(logoLabel);
        centerContent.add(logoPanel, BorderLayout.NORTH);

        JLabel copyrightLabel = new JLabel("© 2025 GEAR.HR - All rights reserved");
        copyrightLabel.setFont(new Font("Garet", Font.PLAIN, 12));
        copyrightLabel.setForeground(TEXT_WHITE);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setOpaque(false);
        southPanel.add(copyrightLabel);
        centerContent.add(southPanel, BorderLayout.SOUTH);

        contentPanel.add(centerContent, BorderLayout.CENTER);
        return contentPanel;
    }

    private static JLabel createContentLogoLabel() {
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/3.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(290, 90, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(resizedLogo));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return logoLabel;
        } catch (Exception e) {
            JLabel logoLabel = new JLabel("GEAR.HR");
            logoLabel.setFont(new Font("Garet", Font.BOLD, 32));
            logoLabel.setForeground(TEXT_WHITE);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return logoLabel;
        }
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ApplicationContext();
        SplashScreen.showSplash(() -> User.showLoginScreen(null, ctx));
    }
}
