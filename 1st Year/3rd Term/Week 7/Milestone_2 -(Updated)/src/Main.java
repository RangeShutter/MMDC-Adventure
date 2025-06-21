import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* 
 * Main class for the Employee Management System
 * Note: All information in this program are sample data for demonstration purposes
 */
public class Main {
    // Static variables to store current user information
    private static String currentUserId;
    private static String currentUserRole;
    private static String currentUserEmail;

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
        JFrame frame = new JFrame("Employee Management System - Welcome " + userId);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window

        // Create menu bar with File menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        // Logout menu item
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            frame.dispose(); // Close current window
            User.showLoginScreen(null); // Return to login screen
        });
        
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // Create main panel with GridBagLayout for flexible component positioning
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBackground(Color.YELLOW); // Set background color
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontal space

        // Welcome label showing user information
        JLabel welcomeLabel = new JLabel("Welcome, " + role + ": " + userId + " (" + email + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.gridwidth = 2; // Span 2 columns
        mainPanel.add(welcomeLabel, gbc);

        // Attendance Button - Row 1, Column 0
        gbc.gridy++; // Move to next row
        gbc.gridwidth = 1; // Reset to single column span
        JButton attendanceBtn = new JButton("Attendance");
        attendanceBtn.setPreferredSize(new Dimension(200, 80));
        attendanceBtn.addActionListener(e -> Attendance.showAttendanceScreen(frame, userId, role));
        mainPanel.add(attendanceBtn, gbc);

        // Employee Profile Button - Row 1, Column 1
        gbc.gridx = 1; // Move to next column
        JButton profileBtn = new JButton("Employee Profiles");
        profileBtn.setPreferredSize(new Dimension(200, 80));
        profileBtn.addActionListener(e -> EmployeeProfile.showProfileScreen(frame, userId, role));
        mainPanel.add(profileBtn, gbc);

        // Payroll Button - Row 2, Column 0
        gbc.gridx = 0; // Back to first column
        gbc.gridy++; // Next row
        JButton payrollBtn = new JButton("Payroll");
        payrollBtn.setPreferredSize(new Dimension(200, 80));
        payrollBtn.addActionListener(e -> Payroll.showPayrollScreen(frame, userId, role));
        mainPanel.add(payrollBtn, gbc);

        // Add main panel to frame and make visible
        frame.add(mainPanel);
        frame.setVisible(true);
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