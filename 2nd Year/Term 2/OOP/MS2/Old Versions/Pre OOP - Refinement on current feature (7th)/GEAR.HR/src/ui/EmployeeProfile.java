package ui;

import model.Employee;
import model.PayrollData;
import model.PayrollResult;
import service.AttendanceService;
import service.EmployeeService;
import service.PayrollProcessor;
import service.PayrollReport;
import util.PayrollUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.nio.file.*;
import javax.swing.table.*;

/**
 * EmployeeProfile UI; employee and payroll data delegated to services (OOP redesign).
 */
public class EmployeeProfile {

    /** Injected by showProfileScreen (DI). */
    private static EmployeeService employeeService;
    private static AttendanceService attendanceService;
    private static PayrollProcessor payrollProcessor;

    // UI components
    private static JTable employeeTable;
    private static DefaultTableModel tableModel;

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

    // Data loaded by EmployeeService and PayrollProcessor constructors

    /**
     * Displays the profile screen. Services are injected (DI).
     */
    public static void showProfileScreen(JFrame parentFrame, String userId, String role,
                                         EmployeeService es, AttendanceService as, PayrollProcessor pp) {
        employeeService = es;
        attendanceService = as;
        payrollProcessor = pp;
        JFrame profileFrame = createProfileFrame(parentFrame);
        JPanel mainPanel = createMainPanel();
        
        // Create and add header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create and add content panel
        JPanel contentPanel = createContentPanel(profileFrame, userId, role);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Create and add footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        profileFrame.add(mainPanel);
        profileFrame.setVisible(true);
    }

    /**
     * Creates the main profile frame.
     *
     * @param parentFrame The parent JFrame for positioning
     * @return The created JFrame for the profile screen
     */
    private static JFrame createProfileFrame(JFrame parentFrame) {
        JFrame profileFrame = new JFrame("GEAR.HR");
        profileFrame.setSize(1300, 800);
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setLocationRelativeTo(parentFrame);
        profileFrame.setResizable(false);

        // Set application icon if available
        try {
            profileFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        return profileFrame;
    }

    /**
     * Creates the main panel for the profile screen.
     *
     * @return JPanel containing the main content
     */
    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        return mainPanel;
    }

    /**
     * Creates the header panel for the profile screen.
     *
     * @return JPanel containing the header content
     */
    private static JPanel createHeaderPanel() {
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

        JLabel titleLabel = createHeaderTitleLabel();
        JLabel subtitleLabel = createHeaderSubtitleLabel();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    /**
     * Creates the header title label.
     *
     * @return JLabel for the header title
     */
    private static JLabel createHeaderTitleLabel() {
        JLabel titleLabel = new JLabel("Employee Profile Management");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    /**
     * Creates the header subtitle label.
     *
     * @return JLabel for the header subtitle
     */
    private static JLabel createHeaderSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Manage employee information and payroll data");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    /**
     * Creates the main content panel for the profile screen.
     *
     * @param profileFrame The parent JFrame for positioning
     * @param userId The ID of the logged-in user
     * @param role The role of the logged-in user
     * @return JPanel containing the main content
     */
    private static JPanel createContentPanel(JFrame profileFrame, String userId, String role) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create employee table panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Create action button panel
        JPanel buttonPanel = createButtonPanel(profileFrame, userId, role);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    /**
     * Creates the table panel for displaying employees.
     *
     * @return JPanel containing the employee table
     */
    private static JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout()) {
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
        tablePanel.setBackground(CARD_WHITE);
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create table title
        JLabel tableTitle = createTableTitleLabel();

        // Create employee table
        createEmployeeTable();

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Creates the table title label.
     *
     * @return JLabel for the table title
     */
    private static JLabel createTableTitleLabel() {
        JLabel tableTitle = new JLabel("Employee Directory");
        tableTitle.setFont(new Font("Garet", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_BLACK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return tableTitle;
    }

    /**
     * Creates the employee table and sets up the table model.
     */
    private static void createEmployeeTable() {
        String[] columnNames = {"Employee Number", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        
        // Apply styling to the table
        styleEmployeeTable();
        updateEmployeeTable();
    }

    /**
     * Styles the employee table for a modern appearance.
     */
    private static void styleEmployeeTable() {
        employeeTable.setFont(new Font("Garet", Font.PLAIN, 12));
        employeeTable.setRowHeight(30);
        employeeTable.setGridColor(new Color(220, 220, 220));
        employeeTable.setSelectionBackground(BUTTON_ORANGE);
        employeeTable.setSelectionForeground(TEXT_WHITE);
        employeeTable.setShowGrid(true);
        employeeTable.setIntercellSpacing(new Dimension(1, 1));

        // Style the table header
        employeeTable.getTableHeader().setFont(new Font("Garet", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(BUTTON_ORANGE);
        employeeTable.getTableHeader().setForeground(TEXT_WHITE);
        employeeTable.getTableHeader().setBorder(BorderFactory.createLineBorder(BUTTON_ORANGE));
    }

    /**
     * Updates the employee table with the current list of employees.
     */
    private static void updateEmployeeTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employeeService.getAllEmployees()) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeNumber(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getSssNumber(),
                emp.getPhilHealthNumber(),
                emp.getTin(),
                emp.getPagIbigNumber()
            });
        }
    }

    /**
     * Creates the button panel with action buttons for the profile screen.
     *
     * @param frame The parent JFrame for positioning
     * @param userId The ID of the logged-in user
     * @param role The role of the logged-in user
     * @return JPanel containing the action buttons
     */
    private static JPanel createButtonPanel(JFrame frame, String userId, String role) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Create modern buttons
        JButton viewButton = createModernButton("View Employee", BUTTON_ORANGE);
        JButton newButton = createModernButton("New Employee", ACCENT_GREY);
        JButton updateButton = createModernButton("Update Employee", ACCENT_GREY);
        JButton deleteButton = createModernButton("Delete Employee", ACCENT_GREY);
        JButton refreshButton = createModernButton("Refresh", ACCENT_GREY);

        // Add action listeners
        viewButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String empNumber = (String) employeeTable.getValueAt(selectedRow, 0);
                Employee selectedEmployee = findEmployeeById(empNumber);
                if (selectedEmployee != null) {
                    showEmployeeDetails(frame, selectedEmployee);
                }
            } else {
                showModernMessage(frame, "Please select an employee first", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        newButton.addActionListener(e -> showNewEmployeeForm(frame));

        updateButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String empNumber = (String) employeeTable.getValueAt(selectedRow, 0);
                Employee selectedEmployee = findEmployeeById(empNumber);
                if (selectedEmployee != null) {
                    showUpdateEmployeeForm(frame, selectedEmployee);
                }
            } else {
                showModernMessage(frame, "Please select an employee first", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String empNumber = (String) employeeTable.getValueAt(selectedRow, 0);
                String empName = (String) employeeTable.getValueAt(selectedRow, 2) + " " + 
                                (String) employeeTable.getValueAt(selectedRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(
                    frame, 
                    "🗑️ Are you sure you want to delete employee " + empName + " (ID: " + empNumber + ")?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteEmployee(empNumber);
                    updateEmployeeTable();
                    showModernMessage(frame, "Employee deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                showModernMessage(frame, "Please select an employee first", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshButton.addActionListener(e -> {
            employeeService.loadEmployeesFromCSV();
            payrollProcessor.loadPayrollDataFromCSV();
            updateEmployeeTable();
            showModernMessage(frame, "Data refreshed successfully", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add buttons to panel
        buttonPanel.add(viewButton);
        buttonPanel.add(newButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    /**
     * Creates a modern styled button with custom background color.
     *
     * @param text The button text
     * @param bg The background color
     * @return JButton with custom styling
     */
    private static JButton createModernButton(String text, Color bg) {
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
        button.setForeground(TEXT_WHITE);
        button.setBackground(bg);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 56));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Shows a modern styled message dialog.
     *
     * @param parent The parent JFrame for positioning
     * @param message The message to display
     * @param title The dialog title
     * @param messageType The type of message (JOptionPane constant)
     */
    private static void showModernMessage(JFrame parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    /**
     * Creates the footer panel for the profile screen.
     *
     * @return JPanel containing the footer content
     */
    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient from left to right
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(93, 224, 230), // #5de0e6 (left)
                    getWidth(), 0, new Color(0, 74, 173)  // #004aad (right)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel footerLabel = new JLabel("© 2025 GEAR.HR - Employee Profiles");
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
     * Deletes an employee and their payroll record.
     * Updates the table and saves changes to CSV.
     *
     * @param empNumber The employee number to delete
     */
    private static void deleteEmployee(String empNumber) {
        // Remove employee from the employees list
        employeeService.deleteEmployee(empNumber);
        attendanceService.removeAttendanceRecords(empNumber);
        payrollProcessor.removePayrollData(empNumber);
        
        // Update the employee table display
        updateEmployeeTable();
        
        // Show confirmation message
        showModernMessage(null, "Employee and all associated records (payroll & attendance) deleted successfully", "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows the details screen for a selected employee.
     *
     * @param parentFrame The parent JFrame for positioning
     * @param employee The Employee object to display
     */
    private static void showEmployeeDetails(JFrame parentFrame, Employee employee) {
        // Create details frame
        JFrame detailsFrame = new JFrame("Employee Details - " + employee.getFirstName() + " " + employee.getLastName());
        detailsFrame.setSize(900, 700);
        detailsFrame.setLocationRelativeTo(parentFrame);
        detailsFrame.setResizable(false);

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);

        // Create header panel
        JPanel headerPanel = createDetailsHeaderPanel(employee);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create content panel
        JPanel contentPanel = createDetailsContentPanel(detailsFrame, employee);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        detailsFrame.add(mainPanel);
        detailsFrame.setVisible(true);
    }

    /**
     * Creates the header panel for the employee details screen.
     *
     * @param employee The Employee object to display
     * @return JPanel containing the details header
     */
    private static JPanel createDetailsHeaderPanel(Employee employee) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(128, 128, 128)); // Grey color
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Employee name and ID
        JLabel nameLabel = new JLabel(employee.getFirstName() + " " + employee.getLastName());
        nameLabel.setFont(new Font("Garet", Font.BOLD, 24));
        nameLabel.setForeground(TEXT_WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel idLabel = new JLabel("Employee ID: " + employee.getEmployeeNumber() + " | " + employee.getEmail());
        idLabel.setFont(new Font("Garet", Font.PLAIN, 14));
        idLabel.setForeground(TEXT_GREY);
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(nameLabel, BorderLayout.NORTH);
        titlePanel.add(idLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    /**
     * Creates the content panel for the employee details screen.
     *
     * @param detailsFrame The parent JFrame for positioning
     * @param employee The Employee object to display
     * @return JPanel containing the details content
     */
    private static JPanel createDetailsContentPanel(JFrame detailsFrame, Employee employee) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Create tabbed pane for better organization
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Garet", Font.BOLD, 12));

        // Personal Information Tab
        JPanel personalInfoPanel = createPersonalInfoPanel(employee);
        tabbedPane.addTab("Personal Information", personalInfoPanel);

        // Salary Computation Tab
        JPanel salaryPanel = createSalaryComputationPanel(employee);
        tabbedPane.addTab("Salary Computation", salaryPanel);

        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add Payroll Edit button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton editPayrollButton = createModernButton("Edit Payroll", BUTTON_ORANGE);
        editPayrollButton.addActionListener(e -> {
            showPayrollEditDialog(detailsFrame, employee);
        });

        buttonPanel.add(editPayrollButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    /**
     * Creates the personal info panel for the employee details screen.
     *
     * @param employee The Employee object to display
     * @return JPanel containing the personal info
     */
    private static JPanel createPersonalInfoPanel(Employee employee) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Title
        JLabel titleLabel = new JLabel("Personal Information");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Details grid
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 15, 10));
        detailsPanel.setOpaque(false);

        addModernDetailField(detailsPanel, "Employee Number:", employee.getEmployeeNumber());
        addModernDetailField(detailsPanel, "Last Name:", employee.getLastName());
        addModernDetailField(detailsPanel, "First Name:", employee.getFirstName());
        addModernDetailField(detailsPanel, "Email:", employee.getEmail());
        addModernDetailField(detailsPanel, "Position:", employee.getPosition());
        addModernDetailField(detailsPanel, "Phone:", employee.getPhone());
        addModernDetailField(detailsPanel, "Address:", employee.getAddress());
        addModernDetailField(detailsPanel, "SSS Number:", employee.getSssNumber());
        addModernDetailField(detailsPanel, "PhilHealth Number:", employee.getPhilHealthNumber());
        addModernDetailField(detailsPanel, "TIN:", employee.getTin());
        addModernDetailField(detailsPanel, "Pag-IBIG Number:", employee.getPagIbigNumber());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the salary computation panel for the employee details screen.
     *
     * @param employee The Employee object to display
     * @return JPanel containing the salary computation info
     */
    private static JPanel createSalaryComputationPanel(Employee employee) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Title
        JLabel titleLabel = new JLabel("Salary Computation");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Controls panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlsPanel.setOpaque(false);

        JLabel monthLabel = new JLabel("Select Month:");
        monthLabel.setFont(new Font("Garet", Font.BOLD, 14));
        monthLabel.setForeground(TEXT_BLACK);

        JComboBox<String> monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December"
        });
        monthCombo.setFont(new Font("Garet", Font.PLAIN, 12));
        monthCombo.setPreferredSize(new Dimension(150, 30));

        JButton computeButton = createModernButton("Compute Salary", BUTTON_ORANGE);
        computeButton.setPreferredSize(new Dimension(150, 35));

        JButton downloadButton = createModernButton("Download Receipt", BUTTON_ORANGE);
        downloadButton.setPreferredSize(new Dimension(150, 35));

        controlsPanel.add(monthLabel);
        controlsPanel.add(monthCombo);
        controlsPanel.add(computeButton);
        controlsPanel.add(downloadButton);

        // Result area
        JTextArea resultArea = new JTextArea(15, 50);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setEditable(false);
        resultArea.setBackground(BACKGROUND_WHITE);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        computeButton.addActionListener(e -> {
            String month = (String) monthCombo.getSelectedItem();
            PayrollResult result = payrollProcessor.processPayroll(employee, month);
            resultArea.setText(PayrollReport.format(result));
        });

        downloadButton.addActionListener(e -> {
            String month = (String) monthCombo.getSelectedItem();
            PayrollResult result = payrollProcessor.processPayroll(employee, month);
            String receiptText = PayrollReport.format(result);
            String safeName = "PayrollReceipt_" + employee.getEmployeeNumber() + "_" + month.replace(" ", "") + ".txt";
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Payroll Receipt");
            chooser.setSelectedFile(new File(safeName));
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files (*.txt)", "txt"));
            int action = chooser.showSaveDialog(panel);
            if (action == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String path = file.getAbsolutePath();
                if (!path.toLowerCase().endsWith(".txt")) path += ".txt";
                try {
                    Files.write(Paths.get(path), receiptText.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                    JOptionPane.showMessageDialog(panel, "Payroll receipt saved to:\n" + path, "Download Complete", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Could not save file: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(controlsPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Adds a modern styled detail field to a panel.
     *
     * @param panel The panel to add the field to
     * @param label The label text
     * @param value The value to display
     */
    private static void addModernDetailField(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Garet", Font.BOLD, 12));
        labelComponent.setForeground(TEXT_BLACK);

        JLabel valueComponent = new JLabel(value != null ? value : "");
        valueComponent.setFont(new Font("Garet", Font.PLAIN, 12));
        valueComponent.setForeground(TEXT_GREY);
        valueComponent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    /**
     * Shows the payroll edit dialog for an employee.
     * Allows editing of salary, deductions, and allowances.
     *
     * @param parentFrame The parent JFrame for positioning
     * @param employee The Employee object to edit
     */
    private static void showPayrollEditDialog(JFrame parentFrame, Employee employee) {
        JFrame payrollFrame = new JFrame("Edit Payroll - " + employee.getFirstName() + " " + employee.getLastName());
        payrollFrame.setSize(500, 600);
        payrollFrame.setLocationRelativeTo(parentFrame);
        payrollFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        PayrollData record = payrollProcessor.getPayrollData(employee.getEmployeeNumber());
        if (record == null || record.getBaseSalary() <= 0) {
            initializePayrollRecord(employee.getEmployeeNumber(), employee.getPosition(), 35000.0);
            record = payrollProcessor.getPayrollData(employee.getEmployeeNumber());
        }
        final PayrollData finalRecord = record;

        JLabel salaryLabel = new JLabel("Base Salary:");
        JTextField salaryField = new JTextField(String.valueOf(record.getBaseSalary()), 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(salaryLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(salaryField, gbc);

        // Deductions section header
        JLabel deductionsLabel = new JLabel("Deductions:");
        deductionsLabel.setFont(new Font("Garet", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(deductionsLabel, gbc);

        // SSS rate field
        JLabel sssLabel = new JLabel("SSS Amount:");
        JTextField sssField = new JTextField(String.format("₱%.2f", record.getSSSDeduction()), 15);
        sssField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        mainPanel.add(sssLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(sssField, gbc);

        // PhilHealth rate field
        JLabel philHealthLabel = new JLabel("PhilHealth Amount:");
        JTextField philHealthField = new JTextField(String.format("₱%.2f", record.getPhilHealthDeduction()), 15);
        philHealthField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(philHealthLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(philHealthField, gbc);

        // Pag-IBIG rate field
        JLabel pagIbigLabel = new JLabel("Pag-IBIG Amount:");
        JTextField pagIbigField = new JTextField(String.format("₱%.2f", record.getPagIbigDeduction()), 15);
        pagIbigField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(pagIbigLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(pagIbigField, gbc);

        JLabel taxLabel = new JLabel("Withholding Tax (₱):");
        double computedTax = PayrollUtils.calculateWithholdingTax(record.getBaseSalary(), record.getRiceSubsidy(), record.getPhoneAllowance(), record.getClothingAllowance());
        JTextField taxField = new JTextField(String.format("%.2f", computedTax), 15);
        taxField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(taxLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(taxField, gbc);

        // Allowances section header
        JLabel allowancesLabel = new JLabel("Allowances:");
        allowancesLabel.setFont(new Font("Garet", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(allowancesLabel, gbc);

        JLabel riceLabel = new JLabel("Rice Subsidy:");
        JTextField riceField = new JTextField(String.valueOf(record.getRiceSubsidy()), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        mainPanel.add(riceLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(riceField, gbc);

        JLabel phoneLabel = new JLabel("Phone Allowance:");
        JTextField phoneField = new JTextField(String.valueOf(record.getPhoneAllowance()), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);

        JLabel clothingLabel = new JLabel("Clothing Allowance:");
        JTextField clothingField = new JTextField(String.valueOf(record.getClothingAllowance()), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(clothingLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(clothingField, gbc);

        // Summary section
        JLabel summaryLabel = new JLabel("Summary:");
        summaryLabel.setFont(new Font("Garet", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(summaryLabel, gbc);

        JTextArea summaryArea = new JTextArea(6, 30);
        summaryArea.setEditable(false);
        updateSummaryArea(summaryArea, record);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(new JScrollPane(summaryArea), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Update payroll record with new values
                double baseSalary = Double.parseDouble(salaryField.getText());
                double sssAmount = PayrollUtils.calculateSSSAmount(baseSalary);
                double philHealthAmount = PayrollUtils.calculatePhilHealthAmount(baseSalary);
                double pagIbigAmount = PayrollUtils.calculatePagIbigAmount(baseSalary);
                float riceSubsidy = Float.parseFloat(riceField.getText());
                float phoneAllowance = Float.parseFloat(phoneField.getText());
                float clothingAllowance = Float.parseFloat(clothingField.getText());
                double withHoldingTax = PayrollUtils.calculateWithholdingTax(baseSalary, riceSubsidy, phoneAllowance, clothingAllowance);

                PayrollData newRecord = new PayrollData(
                    baseSalary, sssAmount, philHealthAmount, pagIbigAmount, (float)withHoldingTax,
                    riceSubsidy, phoneAllowance, clothingAllowance
                );
                payrollProcessor.updatePayrollData(employee.getEmployeeNumber(), newRecord);
                updateSummaryArea(summaryArea, newRecord);

                JOptionPane.showMessageDialog(payrollFrame, "Payroll updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(payrollFrame, "Please enter valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> payrollFrame.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        payrollFrame.add(mainPanel, BorderLayout.CENTER);
        payrollFrame.add(buttonPanel, BorderLayout.SOUTH);
        payrollFrame.setVisible(true);
    }

    private static void updateSummaryArea(JTextArea summaryArea, PayrollData record) {
        StringBuilder summary = new StringBuilder();
        summary.append("Base Salary: ₱").append(String.format("%.2f", record.getBaseSalary())).append("\n");
        summary.append("Total Deductions: ₱").append(String.format("%.2f", record.calculateTotalDeductions())).append("\n");
        summary.append("  - SSS: ₱").append(String.format("%.2f", record.getSSSDeduction())).append("\n");
        summary.append("  - PhilHealth: ₱").append(String.format("%.2f", record.getPhilHealthDeduction())).append("\n");
        summary.append("  - Pag-IBIG: ₱").append(String.format("%.2f", record.getPagIbigDeduction())).append("\n");
        summary.append("  - Tax: ₱").append(String.format("%.2f", record.getTaxDeduction())).append("\n");
        summary.append("Total Allowances: ₱").append(String.format("%.2f", record.calculateTotalAllowances())).append("\n");
        summary.append("  - Rice: ₱").append(String.format("%.2f", record.getRiceSubsidy())).append("\n");
        summary.append("  - Phone: ₱").append(String.format("%.2f", record.getPhoneAllowance())).append("\n");
        summary.append("  - Clothing: ₱").append(String.format("%.2f", record.getClothingAllowance())).append("\n");
        summary.append("Net Salary: ₱").append(String.format("%.2f", record.calculateNetSalary()));
        summaryArea.setText(summary.toString());
    }

    /**
     * Initializes a new payroll record for an employee with default rates based on position.
     * Only creates a record if one does not already exist.
     *
     * @param employeeId The employee's ID
     * @param position The employee's position
     * @param baseSalary The employee's base salary
     */
    private static void initializePayrollRecord(String employeeId, String position, double baseSalary) {
        PayrollData existing = payrollProcessor.getPayrollData(employeeId);
        if (existing == null || existing.getBaseSalary() <= 0) {
            double sssAmount = PayrollUtils.calculateSSSAmount(baseSalary);
            double philHealthAmount = PayrollUtils.calculatePhilHealthAmount(baseSalary);
            double pagIbigAmount = PayrollUtils.calculatePagIbigAmount(baseSalary);
            float withHoldingTax = 0.15f;
            float riceSubsidy = 1500;
            float phoneAllowance = 1000;
            float clothingAllowance = 800;
            if (position != null && position.equalsIgnoreCase("Manager")) {
                withHoldingTax = 0.12f;
                phoneAllowance = 800;
                clothingAllowance = 600;
            } else if (position != null && position.equalsIgnoreCase("HR")) {
                withHoldingTax = 0.12f;
            }
            double tax = PayrollUtils.calculateWithholdingTax(baseSalary, riceSubsidy, phoneAllowance, clothingAllowance);
            PayrollData record = new PayrollData(baseSalary, sssAmount, philHealthAmount, pagIbigAmount, (float) tax, riceSubsidy, phoneAllowance, clothingAllowance);
            payrollProcessor.updatePayrollData(employeeId, record);
        }
    }

    /**
     * Shows the update employee form for editing employee details.
     *
     * @param parentFrame The parent JFrame for positioning
     * @param employee The Employee object to update
     */
    private static void showUpdateEmployeeForm(JFrame parentFrame, Employee employee) {
        JFrame updateEmpFrame = new JFrame("Update Employee - " + employee.getFirstName() + " " + employee.getLastName());
        updateEmpFrame.setSize(500, 600);
        updateEmpFrame.setLocationRelativeTo(parentFrame);
        updateEmpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create text fields with current employee data
        JTextField lastNameField = new JTextField(employee.getLastName(), 20);
        JTextField firstNameField = new JTextField(employee.getFirstName(), 20);
        JTextField sssField = new JTextField(employee.getSssNumber(), 20);
        JTextField philHealthField = new JTextField(employee.getPhilHealthNumber(), 20);
        JTextField tinField = new JTextField(employee.getTin(), 20);
        JTextField pagIbigField = new JTextField(employee.getPagIbigNumber(), 20);
        JTextField emailField = new JTextField(employee.getEmail(), 20);
        JTextField positionField = new JTextField(employee.getPosition(), 20);
        JTextField addressField = new JTextField(employee.getAddress(), 20);
        JTextField phoneField = new JTextField(employee.getPhone(), 20);

        // Add form fields
        addFormField(mainPanel, "Last Name:", lastNameField, gbc, 0);
        addFormField(mainPanel, "First Name:", firstNameField, gbc, 1);
        addFormField(mainPanel, "SSS Number:", sssField, gbc, 2);
        addFormField(mainPanel, "PhilHealth Number:", philHealthField, gbc, 3);
        addFormField(mainPanel, "TIN:", tinField, gbc, 4);
        addFormField(mainPanel, "Pag-IBIG Number:", pagIbigField, gbc, 5);
        addFormField(mainPanel, "Email:", emailField, gbc, 6);
        addFormField(mainPanel, "Position:", positionField, gbc, 7);
        addFormField(mainPanel, "Address:", addressField, gbc, 8);
        addFormField(mainPanel, "Phone:", phoneField, gbc, 9);

        JButton submitButton = new JButton("Update Employee");
        submitButton.addActionListener(e -> {
            // Update employee with new data
            employee.setLastName(lastNameField.getText());
            employee.setFirstName(firstNameField.getText());
            employee.setSssNumber(sssField.getText());
            employee.setPhilHealthNumber(philHealthField.getText());
            employee.setTin(tinField.getText());
            employee.setPagIbigNumber(pagIbigField.getText());
            employee.setEmail(emailField.getText());
            employee.setPosition(positionField.getText());
            employee.setAddress(addressField.getText());
            employee.setPhone(phoneField.getText());

            employeeService.updateEmployee(employee);
            updateEmployeeTable();
            updateEmpFrame.dispose();
            JOptionPane.showMessageDialog(parentFrame, "Employee updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        updateEmpFrame.add(mainPanel, BorderLayout.CENTER);
        updateEmpFrame.add(buttonPanel, BorderLayout.SOUTH);
        updateEmpFrame.setVisible(true);
    }

    /**
     * Shows the new employee form for adding a new employee.
     *
     * @param parentFrame The parent JFrame for positioning
     */
    private static void showNewEmployeeForm(JFrame parentFrame) {
        JFrame newEmpFrame = new JFrame("Add New Employee");
        newEmpFrame.setSize(500, 600);
        newEmpFrame.setLocationRelativeTo(parentFrame);
        newEmpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create text fields
        JTextField empNumberField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField firstNameField = new JTextField(20);
        JTextField sssField = new JTextField(20);
        JTextField philHealthField = new JTextField(20);
        JTextField tinField = new JTextField(20);
        JTextField pagIbigField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField positionField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField salaryField = new JTextField(20);
        JTextField riceSubsidyField = new JTextField("1500", 20);
        JTextField phoneAllowanceField = new JTextField("1000", 20);
        JTextField clothingAllowanceField = new JTextField("800", 20);

        // Add form fields
        addFormField(mainPanel, "Employee Number:", empNumberField, gbc, 0);
        addFormField(mainPanel, "Last Name:", lastNameField, gbc, 1);
        addFormField(mainPanel, "First Name:", firstNameField, gbc, 2);
        addFormField(mainPanel, "SSS Number:", sssField, gbc, 3);
        addFormField(mainPanel, "PhilHealth Number:", philHealthField, gbc, 4);
        addFormField(mainPanel, "TIN:", tinField, gbc, 5);
        addFormField(mainPanel, "Pag-IBIG Number:", pagIbigField, gbc, 6);
        addFormField(mainPanel, "Email:", emailField, gbc, 7);
        addFormField(mainPanel, "Position:", positionField, gbc, 8);
        addFormField(mainPanel, "Address:", addressField, gbc, 9);
        addFormField(mainPanel, "Phone:", phoneField, gbc, 10);
        addFormField(mainPanel, "Base Salary:", salaryField, gbc, 11);
        addFormField(mainPanel, "Rice Subsidy:", riceSubsidyField, gbc, 12);
        addFormField(mainPanel, "Phone Allowance:", phoneAllowanceField, gbc, 13);
        addFormField(mainPanel, "Clothing Allowance:", clothingAllowanceField, gbc, 14);

        JButton submitButton = new JButton("Add Employee");
        submitButton.addActionListener(e -> {
            try {
                String empNumber = empNumberField.getText();
                if (employeeService.findEmployeeById(empNumber) != null) {
                    JOptionPane.showMessageDialog(newEmpFrame, "Employee number already exists!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Employee newEmployee = new Employee(
                    empNumber,
                    lastNameField.getText(),
                    firstNameField.getText(),
                    sssField.getText(),
                    philHealthField.getText(),
                    tinField.getText(),
                    pagIbigField.getText(),
                    emailField.getText(),
                    positionField.getText(),
                    addressField.getText(),
                    phoneField.getText()
                );
                employeeService.addEmployee(newEmployee);

                double baseSalary = Double.parseDouble(salaryField.getText());
                float riceSubsidy = Float.parseFloat(riceSubsidyField.getText());
                float phoneAllowance = Float.parseFloat(phoneAllowanceField.getText());
                float clothingAllowance = Float.parseFloat(clothingAllowanceField.getText());
                double sssAmount = PayrollUtils.calculateSSSAmount(baseSalary);
                double philHealthAmount = PayrollUtils.calculatePhilHealthAmount(baseSalary);
                double pagIbigAmount = PayrollUtils.calculatePagIbigAmount(baseSalary);
                double withHoldingTax = PayrollUtils.calculateWithholdingTax(baseSalary, riceSubsidy, phoneAllowance, clothingAllowance);
                PayrollData record = new PayrollData(
                    baseSalary, sssAmount, philHealthAmount, pagIbigAmount, (float) withHoldingTax,
                    riceSubsidy, phoneAllowance, clothingAllowance
                );
                payrollProcessor.updatePayrollData(newEmployee.getEmployeeNumber(), record);

                updateEmployeeTable();
                newEmpFrame.dispose();
                JOptionPane.showMessageDialog(parentFrame, "Employee added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(newEmpFrame, "Please enter valid numbers for salary and allowances", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        newEmpFrame.add(mainPanel, BorderLayout.CENTER);
        newEmpFrame.add(buttonPanel, BorderLayout.SOUTH);
        newEmpFrame.setVisible(true);
    }

    /**
     * Adds a detail field (label and value) to a panel.
     *
     * @param panel The panel to add the field to
     * @param label The label text
     * @param value The value to display
     */
    private static void addDetailField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value != null ? value : ""));
    }

    /**
     * Adds a form field (label and input) to a panel.
     *
     * @param panel The panel to add the field to
     * @param label The label text
     * @param field The JTextField component
     * @param gbc The GridBagConstraints to use
     * @param row The row number
     */
    private static void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private static Employee findEmployeeById(String empNumber) {
        return employeeService.findEmployeeById(empNumber);
    }

    public static List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}