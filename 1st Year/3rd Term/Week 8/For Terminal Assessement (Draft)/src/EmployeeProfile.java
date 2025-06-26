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
 * EmployeeProfile class handles employee information management and payroll integration
 * Provides comprehensive interface for managing employee data, payroll records, and salary computations
 * Note: All information in this program are sample data for demonstration purposes
 */
public class EmployeeProfile {
    
    // Employee data storage
    private static final List<Employee> employees = new ArrayList<>();
    private static final String EMPLOYEES_CSV_FILE = "employees.csv";
    
    // UI components
    private static JTable employeeTable;
    private static DefaultTableModel tableModel;
    
    // Payroll data storage
    private static final Map<String, PayrollRecord> payrollRecords = new HashMap<>();
    private static final String PAYROLL_CSV_FILE = "payroll_records.csv";

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

    // Initialize employee and payroll data from CSV files
    static {
        loadEmployeesFromCSV();
        loadPayrollRecordsFromCSV();
        
        if (employees.isEmpty()) {
            addSampleEmployees();
        }
        
        if (payrollRecords.isEmpty()) {
            initializeSamplePayrollData();
            savePayrollRecordsToCSV();
        }
    }

    /**
     * Displays the main employee profile management screen
     * Creates a comprehensive interface for managing employee information and payroll data
     * @param parentFrame The parent JFrame for positioning
     * @param userId The current user's ID (unused in current implementation)
     * @param role The current user's role (unused in current implementation)
     */
    public static void showProfileScreen(JFrame parentFrame, String userId, String role) {
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
     * Creates and configures the employee profile management frame
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
     * Creates the main panel with background styling
     */
    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        return mainPanel;
    }

    /**
     * Creates the header panel with gradient background and title
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
     * Creates the header title label
     */
    private static JLabel createHeaderTitleLabel() {
        JLabel titleLabel = new JLabel("Employee Profile Management");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    /**
     * Creates the header subtitle label
     */
    private static JLabel createHeaderSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Manage employee information and payroll data");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    /**
     * Creates the main content panel with employee table and action buttons
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
     * Creates the employee table panel with modern styling
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
     * Creates the table title label
     */
    private static JLabel createTableTitleLabel() {
        JLabel tableTitle = new JLabel("Employee Directory");
        tableTitle.setFont(new Font("Garet", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_BLACK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return tableTitle;
    }

    /**
     * Creates the employee table with model and styling
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
     * Styles the employee table with modern appearance
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
     * Updates the employee table with current employee data
     */
    private static void updateEmployeeTable() {
        tableModel.setRowCount(0);  // Clear existing rows
        for (Employee emp : employees) {
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
     * Creates the button panel with modern styling
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
            loadEmployeesFromCSV();
            loadPayrollRecordsFromCSV();
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
     * Creates a modern styled button
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
     * Shows a modern styled message dialog
     */
    private static void showModernMessage(JFrame parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    /**
     * Creates a footer panel
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
     * Deletes an employee and all associated payroll records
     * @param empNumber The employee number to delete
     */
    private static void deleteEmployee(String empNumber) {
        // Remove employee from the employees list
        employees.removeIf(e -> e.getEmployeeNumber().equals(empNumber));
        
        // Remove payroll record from EmployeeProfile
        payrollRecords.remove(empNumber);
        
        // Remove payroll record from SalaryComputation class
        SalaryComputation.removePayrollData(empNumber);
        
        // Remove attendance records from Attendance class
        Attendance.removeAttendanceRecords(empNumber);
        
        // Save updated data to CSV files
        saveEmployeesToCSV();
        savePayrollRecordsToCSV();
        
        // Update the employee table display
        updateEmployeeTable();
        
        // Show confirmation message
        showModernMessage(null, "Employee and all associated records (payroll & attendance) deleted successfully", "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays detailed information about an employee including salary computation
     * @param parentFrame The parent frame for positioning
     * @param employee The employee object to display
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
     * Creates the header panel for employee details
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
     * Creates the content panel for employee details
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
     * Creates the personal information panel
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
     * Creates the salary computation panel
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

        controlsPanel.add(monthLabel);
        controlsPanel.add(monthCombo);
        controlsPanel.add(computeButton);

        // Result area
        JTextArea resultArea = new JTextArea(15, 50);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setEditable(false);
        resultArea.setBackground(BACKGROUND_WHITE);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Action listener for compute button
        computeButton.addActionListener(e -> {
            String month = (String) monthCombo.getSelectedItem();
            String salaryInfo = SalaryComputation.computeSalary(employee, month);
            resultArea.setText(salaryInfo);
        });

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(controlsPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Adds a modern styled detail field to a panel
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
     * Shows the payroll edit dialog for an employee
     * @param parentFrame The parent frame for positioning
     * @param employee The employee whose payroll to edit
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

        // Get or create payroll record for this employee
        PayrollRecord record = payrollRecords.get(employee.getEmployeeNumber());
        if (record == null) {
            // Initialize payroll record if it doesn't exist
            initializePayrollRecord(employee.getEmployeeNumber(), employee.getPosition(), 35000.0);
            record = payrollRecords.get(employee.getEmployeeNumber());
        }

        // Base Salary field
        JLabel salaryLabel = new JLabel("Base Salary:");
        JTextField salaryField = new JTextField(String.valueOf(record.baseSalary), 15);
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
        JLabel sssLabel = new JLabel("SSS Rate (%):");
        JTextField sssField = new JTextField(String.valueOf(record.sssRate * 100), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        mainPanel.add(sssLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(sssField, gbc);

        // PhilHealth rate field
        JLabel philHealthLabel = new JLabel("PhilHealth Rate (%):");
        JTextField philHealthField = new JTextField(String.valueOf(record.philHealthRate * 100), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(philHealthLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(philHealthField, gbc);

        // Pag-IBIG rate field
        JLabel pagIbigLabel = new JLabel("Pag-IBIG Rate (%):");
        JTextField pagIbigField = new JTextField(String.valueOf(record.pagIbigRate * 100), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(pagIbigLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(pagIbigField, gbc);

        // Withholding tax field
        JLabel taxLabel = new JLabel("Withholding Tax Rate (%):");
        JTextField taxField = new JTextField(String.valueOf(record.withHoldingTax * 100), 15);
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

        // Rice subsidy field
        JLabel riceLabel = new JLabel("Rice Subsidy:");
        JTextField riceField = new JTextField(String.valueOf(record.riceSubsidy), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        mainPanel.add(riceLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(riceField, gbc);

        // Phone allowance field
        JLabel phoneLabel = new JLabel("Phone Allowance:");
        JTextField phoneField = new JTextField(String.valueOf(record.phoneAllowance), 15);
        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);

        // Clothing allowance field
        JLabel clothingLabel = new JLabel("Clothing Allowance:");
        JTextField clothingField = new JTextField(String.valueOf(record.clothingAllowance), 15);
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
                float sssRate = Float.parseFloat(sssField.getText()) / 100;
                float philHealthRate = Float.parseFloat(philHealthField.getText()) / 100;
                float pagIbigRate = Float.parseFloat(pagIbigField.getText()) / 100;
                float withHoldingTax = Float.parseFloat(taxField.getText()) / 100;
                float riceSubsidy = Float.parseFloat(riceField.getText());
                float phoneAllowance = Float.parseFloat(phoneField.getText());
                float clothingAllowance = Float.parseFloat(clothingField.getText());

                PayrollRecord newRecord = new PayrollRecord(
                    baseSalary, sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                    riceSubsidy, phoneAllowance, clothingAllowance
                );

                payrollRecords.put(employee.getEmployeeNumber(), newRecord);
                savePayrollRecordsToCSV();
                
                // Update SalaryComputation class with the new data
                SalaryComputation.PayrollData salaryData = new SalaryComputation.PayrollData(
                    baseSalary, sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                    riceSubsidy, phoneAllowance, clothingAllowance
                );
                SalaryComputation.updatePayrollData(employee.getEmployeeNumber(), salaryData);
                
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

    /**
     * Updates the summary area with current payroll calculations
     * @param summaryArea The text area to update
     * @param record The payroll record to display
     */
    private static void updateSummaryArea(JTextArea summaryArea, PayrollRecord record) {
        StringBuilder summary = new StringBuilder();
        summary.append("Base Salary: ₱").append(String.format("%.2f", record.baseSalary)).append("\n");
        summary.append("Total Deductions: ₱").append(String.format("%.2f", record.calculateTotalDeductions())).append("\n");
        summary.append("  - SSS: ₱").append(String.format("%.2f", record.getSSSDeduction())).append("\n");
        summary.append("  - PhilHealth: ₱").append(String.format("%.2f", record.getPhilHealthDeduction())).append("\n");
        summary.append("  - Pag-IBIG: ₱").append(String.format("%.2f", record.getPagIbigDeduction())).append("\n");
        summary.append("  - Tax: ₱").append(String.format("%.2f", record.getTaxDeduction())).append("\n");
        summary.append("Total Allowances: ₱").append(String.format("%.2f", record.calculateTotalAllowances())).append("\n");
        summary.append("  - Rice: ₱").append(String.format("%.2f", record.riceSubsidy)).append("\n");
        summary.append("  - Phone: ₱").append(String.format("%.2f", record.phoneAllowance)).append("\n");
        summary.append("  - Clothing: ₱").append(String.format("%.2f", record.clothingAllowance)).append("\n");
        summary.append("Net Salary: ₱").append(String.format("%.2f", record.calculateNetSalary()));
        
        summaryArea.setText(summary.toString());
    }

    /**
     * Initializes a new payroll record for an employee with default rates based on position
     * @param employeeId The employee's ID
     * @param position The employee's position (affects default rates)
     * @param baseSalary The employee's base salary
     */
    private static void initializePayrollRecord(String employeeId, String position, double baseSalary) {
        // Only create record if it doesn't exist
        if (!payrollRecords.containsKey(employeeId)) {
            // Default deduction rates
            float sssRate = 0.045f;       // 4.5%
            float philHealthRate = 0.04f;  // 4%
            float pagIbigRate = 0.02f;    // 2%
            float withHoldingTax = 0.15f; // 15% (default)
            
            // Default allowances
            float riceSubsidy = 1500;
            float phoneAllowance = 1000;
            float clothingAllowance = 800;

            // Adjust rates based on position
            if (position.equalsIgnoreCase("Manager")) {
                withHoldingTax = 0.12f;
                phoneAllowance = 800;
                clothingAllowance = 600;
            } else if (position.equalsIgnoreCase("HR")) {
                withHoldingTax = 0.12f;
            }

            // Create and store new payroll record
            PayrollRecord record = new PayrollRecord(
                baseSalary, 
                sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                riceSubsidy, phoneAllowance, clothingAllowance
            );
            
            payrollRecords.put(employeeId, record);
            
            // Also update SalaryComputation class
            SalaryComputation.PayrollData salaryData = new SalaryComputation.PayrollData(
                baseSalary, sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                riceSubsidy, phoneAllowance, clothingAllowance
            );
            SalaryComputation.updatePayrollData(employeeId, salaryData);
        }
    }

    /**
     * Initializes sample payroll data for demonstration purposes
     */
    private static void initializeSamplePayrollData() {
        payrollRecords.put("1001", new PayrollRecord(35000, 
            0.045f, 0.04f, 0.02f, 0.15f, 1500, 1000, 800));
        
        payrollRecords.put("1002", new PayrollRecord(60000, 
            0.045f, 0.04f, 0.02f, 0.12f, 1500, 800, 600));
            
        // Also update SalaryComputation class with the same data
        SalaryComputation.PayrollData data1 = new SalaryComputation.PayrollData(
            35000, 0.045f, 0.04f, 0.02f, 0.15f, 1500, 1000, 800
        );
        SalaryComputation.updatePayrollData("1001", data1);
        
        SalaryComputation.PayrollData data2 = new SalaryComputation.PayrollData(
            60000, 0.045f, 0.04f, 0.02f, 0.12f, 1500, 800, 600
        );
        SalaryComputation.updatePayrollData("1002", data2);
    }

    /**
     * Loads payroll records from CSV file into memory
     */
    private static void loadPayrollRecordsFromCSV() {
        if (!Files.exists(Paths.get(PAYROLL_CSV_FILE))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PAYROLL_CSV_FILE))) {
            reader.readLine(); // Skip header line

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8) {
                    String employeeId = data[0];
                    double baseSalary = Double.parseDouble(data[1]);
                    float sssRate = Float.parseFloat(data[2]);
                    float philHealthRate = Float.parseFloat(data[3]);
                    float pagIbigRate = Float.parseFloat(data[4]);
                    float withHoldingTax = Float.parseFloat(data[5]);
                    float riceSubsidy = Float.parseFloat(data[6]);
                    float phoneAllowance = Float.parseFloat(data[7]);
                    float clothingAllowance = Float.parseFloat(data[8]);
                    
                    payrollRecords.put(employeeId, new PayrollRecord(
                        baseSalary, sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                        riceSubsidy, phoneAllowance, clothingAllowance
                    ));
                    
                    // Also update SalaryComputation class
                    SalaryComputation.PayrollData salaryData = new SalaryComputation.PayrollData(
                        baseSalary, sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                        riceSubsidy, phoneAllowance, clothingAllowance
                    );
                    SalaryComputation.updatePayrollData(employeeId, salaryData);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading payroll records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves all payroll records to CSV file
     */
    private static void savePayrollRecordsToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYROLL_CSV_FILE))) {
            // Write CSV header
            writer.println("EmployeeID,BaseSalary,SSSRate,PhilHealthRate,PagIBIGRate,WithholdingTax,RiceSubsidy,PhoneAllowance,ClothingAllowance");
            // Write each record
            for (Map.Entry<String, PayrollRecord> entry : payrollRecords.entrySet()) {
                PayrollRecord record = entry.getValue();
                writer.println(String.join(",",
                    entry.getKey(),
                    String.valueOf(record.baseSalary),
                    String.valueOf(record.sssRate),
                    String.valueOf(record.philHealthRate),
                    String.valueOf(record.pagIbigRate),
                    String.valueOf(record.withHoldingTax),
                    String.valueOf(record.riceSubsidy),
                    String.valueOf(record.phoneAllowance),
                    String.valueOf(record.clothingAllowance)
                ));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving payroll records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays form to update an existing employee's information
     * @param parentFrame The parent frame for positioning
     * @param employee The employee to update
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

            saveEmployeesToCSV();
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
     * Displays form to create a new employee
     * @param parentFrame The parent frame for positioning
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

        JButton submitButton = new JButton("Add Employee");
        submitButton.addActionListener(e -> {
            try {
                // Create new employee
                Employee newEmployee = new Employee(
                    empNumberField.getText(),
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

                employees.add(newEmployee);
                saveEmployeesToCSV();

                // Initialize payroll record for new employee
                double baseSalary = Double.parseDouble(salaryField.getText());
                initializePayrollRecord(newEmployee.getEmployeeNumber(), newEmployee.getPosition(), baseSalary);
                savePayrollRecordsToCSV();
                
                updateEmployeeTable();
                newEmpFrame.dispose();
                JOptionPane.showMessageDialog(parentFrame, "Employee added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(newEmpFrame, "Please enter a valid number for base salary", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        newEmpFrame.add(mainPanel, BorderLayout.CENTER);
        newEmpFrame.add(buttonPanel, BorderLayout.SOUTH);
        newEmpFrame.setVisible(true);
    }

    /**
     * Helper method to add a read-only detail field to a panel
     * @param panel The panel to add the field to
     * @param label The label text for the field
     * @param value The value to display
     */
    private static void addDetailField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value != null ? value : ""));
    }

    /**
     * Helper method to add an editable form field to a panel
     * @param panel The panel to add the field to
     * @param label The label text for the field
     * @param field The text field component
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

    /**
     * Finds an employee by their employee number
     * @param empNumber The employee number to search for
     * @return The Employee object if found, null otherwise
     */
    private static Employee findEmployeeById(String empNumber) {
        for (Employee emp : employees) {
            if (emp.getEmployeeNumber().equals(empNumber)) {
                return emp;
            }
        }
        return null;
    }

    /**
     * Loads employee data from the CSV file into memory
     */
    private static void loadEmployeesFromCSV() {
        employees.clear();
        if (!Files.exists(Paths.get(EMPLOYEES_CSV_FILE))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(EMPLOYEES_CSV_FILE))) {
            // Skip header line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                // Split line while handling quoted commas
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (data.length >= 11) {
                    employees.add(new Employee(
                        data[0], data[1], data[2], data[3], data[4], 
                        data[5], data[6], data[7], data[8], data[9], data[10]
                    ));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves all employee data to the CSV file
     */
    private static void saveEmployeesToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_CSV_FILE))) {
            // Write CSV header
            writer.println("EmployeeNumber,LastName,FirstName,SSS,PhilHealth,TIN,PagIBIG,Email,Position,Address,Phone");
            for (Employee emp : employees) {
                writer.println(String.join(",",
                    emp.getEmployeeNumber(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getSssNumber(),
                    emp.getPhilHealthNumber(),
                    emp.getTin(),
                    emp.getPagIbigNumber(),
                    emp.getEmail(),
                    emp.getPosition(),
                    emp.getAddress(),
                    emp.getPhone()
                ));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds sample employee data if the system is empty
     */
    private static void addSampleEmployees() {
        employees.add(new Employee(
            "1001", "Bactong", "Colin", "123456789", "123456789012", "123-456-789", "123456789012",
            "Colin@MotorPH.com", "Developer", "Leyte, Palo", "0960 270 7931"
        ));
        employees.add(new Employee(
            "1002", "Bactong", "Charlize", "987654321", "210987654321", "987-654-321", "210987654321",
            "Charlize@MotorPH.com", "Manager", "Negros Occidental Silay City", "555-0202"
        ));
        saveEmployeesToCSV();
    }

    /**
     * Returns a copy of the current list of employees (for use in other classes)
     */
    public static List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    /**
     * Inner class representing an Employee with all their personal and work information
     */
    static class Employee {
        private String employeeNumber;
        private String lastName;
        private String firstName;
        private String sssNumber;
        private String philHealthNumber;
        private String tin;
        private String pagIbigNumber;
        private String email;
        private String position;
        private String address;
        private String phone;

        public Employee(String employeeNumber, String lastName, String firstName, String sssNumber,
                       String philHealthNumber, String tin, String pagIbigNumber, String email,
                       String position, String address, String phone) {
            this.employeeNumber = employeeNumber;
            this.lastName = lastName;
            this.firstName = firstName;
            this.sssNumber = sssNumber;
            this.philHealthNumber = philHealthNumber;
            this.tin = tin;
            this.pagIbigNumber = pagIbigNumber;
            this.email = email;
            this.position = position;
            this.address = address;
            this.phone = phone;
        }

        // Getter methods for all employee properties
        public String getEmployeeNumber() { return employeeNumber; }
        public String getLastName() { return lastName; }
        public String getFirstName() { return firstName; }
        public String getSssNumber() { return sssNumber; }
        public String getPhilHealthNumber() { return philHealthNumber; }
        public String getTin() { return tin; }
        public String getPagIbigNumber() { return pagIbigNumber; }
        public String getEmail() { return email; }
        public String getPosition() { return position; }
        public String getAddress() { return address; }
        public String getPhone() { return phone; }

        // Setter methods for all employee properties
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }
        public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }
        public void setTin(String tin) { this.tin = tin; }
        public void setPagIbigNumber(String pagIbigNumber) { this.pagIbigNumber = pagIbigNumber; }
        public void setEmail(String email) { this.email = email; }
        public void setPosition(String position) { this.position = position; }
        public void setAddress(String address) { this.address = address; }
        public void setPhone(String phone) { this.phone = phone; }
    }

    /**
     * Inner class representing a payroll record with all salary components
     */
    static class PayrollRecord {
        private double baseSalary;
        private float sssRate;
        private float philHealthRate;
        private float pagIbigRate;
        private float withHoldingTax;
        private float riceSubsidy;
        private float phoneAllowance;
        private float clothingAllowance;

        /**
         * Creates a new payroll record
         * @param baseSalary The base salary amount
         * @param sssRate SSS contribution rate
         * @param philHealthRate PhilHealth contribution rate
         * @param pagIbigRate Pag-IBIG contribution rate
         * @param withHoldingTax Tax withholding rate
         * @param riceSubsidy Rice allowance amount
         * @param phoneAllowance Phone allowance amount
         * @param clothingAllowance Clothing allowance amount
         */
        public PayrollRecord(double baseSalary, 
                           float sssRate, float philHealthRate, float pagIbigRate, float withHoldingTax,
                           float riceSubsidy, float phoneAllowance, float clothingAllowance) {
            this.baseSalary = baseSalary;
            this.sssRate = sssRate;
            this.philHealthRate = philHealthRate;
            this.pagIbigRate = pagIbigRate;
            this.withHoldingTax = withHoldingTax;
            this.riceSubsidy = riceSubsidy;
            this.phoneAllowance = phoneAllowance;
            this.clothingAllowance = clothingAllowance;
        }

        // Deduction calculation methods
        public double getSSSDeduction() { return baseSalary * sssRate; }
        public double getPhilHealthDeduction() { return baseSalary * philHealthRate; }
        public double getPagIbigDeduction() { return baseSalary * pagIbigRate; }
        public double getTaxDeduction() { return baseSalary * withHoldingTax; }

        /**
         * Calculates total deductions (SSS, PhilHealth, Pag-IBIG, Tax)
         * @return Total deductions amount
         */
        public double calculateTotalDeductions() {
            return getSSSDeduction() + getPhilHealthDeduction() + 
                   getPagIbigDeduction() + getTaxDeduction();
        }

        /**
         * Calculates total allowances (Rice, Phone, Clothing)
         * @return Total allowances amount
         */
        public double calculateTotalAllowances() {
            return riceSubsidy + phoneAllowance + clothingAllowance;
        }

        /**
         * Calculates net salary (Base - Deductions + Allowances)
         * @return Net salary amount
         */
        public double calculateNetSalary() {
            return baseSalary - calculateTotalDeductions() + calculateTotalAllowances();
        }
    }
}