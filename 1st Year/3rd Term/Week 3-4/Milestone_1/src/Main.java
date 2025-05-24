import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Please take note all information given on this program are only samples such as the variables given */

public class Main {
    private static String currentUserId;
    private static String currentUserRole;
    private static String currentUserEmail;

    public static void showMainScreen(String userId, String role, String email) {
        currentUserId = userId;
        currentUserRole = role;
        currentUserEmail = email;

        JFrame frame = new JFrame("Employee Management System - Welcome " + userId);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            frame.dispose();
            User.showLoginScreen(null);
        });
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // Main panel with buttons
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBackground(Color.YELLOW);
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel welcomeLabel = new JLabel("Welcome, " + role + ": " + userId + " (" + email + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(welcomeLabel, gbc);

        // Attendance Button
        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton attendanceBtn = new JButton("Attendance");
        attendanceBtn.setPreferredSize(new Dimension(200, 80));
        attendanceBtn.addActionListener(e -> Attendance.showAttendanceScreen(frame, userId, role));
        mainPanel.add(attendanceBtn, gbc);

        // Employee Profile Button
        gbc.gridx = 1;
        JButton profileBtn = new JButton("Employee Profiles");
        profileBtn.setPreferredSize(new Dimension(200, 80));
        profileBtn.addActionListener(e -> EmployeeProfile.showProfileScreen(frame, userId, role));
        mainPanel.add(profileBtn, gbc);

        // Payroll Button
        gbc.gridx = 0;
        gbc.gridy++;
        JButton payrollBtn = new JButton("Payroll");
        payrollBtn.setPreferredSize(new Dimension(200, 80));
        payrollBtn.addActionListener(e -> Payroll.showPayrollScreen(frame, userId, role));
        mainPanel.add(payrollBtn, gbc);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        User.showLoginScreen(null);
    }
}
