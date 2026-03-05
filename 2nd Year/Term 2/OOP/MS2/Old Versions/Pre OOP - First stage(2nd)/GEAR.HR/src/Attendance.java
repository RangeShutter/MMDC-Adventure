import model.AttendanceRecord;
import service.AttendanceService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import javax.swing.table.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Attendance UI; attendance data delegated to AttendanceService (OOP redesign).
 */
public class Attendance {
    private static final AttendanceService attendanceService = new AttendanceService();

    private static JTable attendanceTable;
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

    // Add static references for form fields
    private static JComboBox<String> employeeComboBox;
    private static JTextField dateField;
    private static JComboBox<String> statusComboBox;
    private static JTextField timeInField;
    private static JTextField timeOutField;

    /**
     * Displays the attendance management screen.
     * Sets up the main UI for recording, viewing, and managing attendance records.
     *
     * @param parentFrame The parent JFrame for positioning
     * @param userId The ID of the logged-in user
     * @param role The role of the logged-in user
     */
    public static void showAttendanceScreen(JFrame parentFrame, String userId, String role) {
        JFrame attendanceFrame = createAttendanceFrame(parentFrame);
        JPanel mainPanel = createMainPanel();
        
        // Create and add header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create and add content panel
        JPanel contentPanel = createContentPanel(attendanceFrame, userId, role);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Create and add footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        attendanceFrame.add(mainPanel);
        attendanceFrame.setVisible(true);
    }

    /**
     * Creates and configures the attendance management frame.
     *
     * @param parentFrame The parent JFrame for positioning
     * @return The created JFrame for the attendance screen
     */
    private static JFrame createAttendanceFrame(JFrame parentFrame) {
        JFrame attendanceFrame = new JFrame("Attendance Management System");
        attendanceFrame.setSize(1200, 1000);
        attendanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        attendanceFrame.setLocationRelativeTo(parentFrame);
        attendanceFrame.setResizable(false);

        // Set application icon if available
        try {
            attendanceFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo/Icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        return attendanceFrame;
    }

    /**
     * Creates the main panel for the attendance screen.
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
     * Creates the header panel for the attendance screen.
     *
     * @return JPanel containing the header content
     */
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_DARK);
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
        JLabel titleLabel = new JLabel("Attendance Management");
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
        JLabel subtitleLabel = new JLabel("Track employee attendance and manage time records");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    /**
     * Creates the main content panel for the attendance screen.
     *
     * @param attendanceFrame The parent JFrame for positioning
     * @param userId The ID of the logged-in user
     * @param role The role of the logged-in user
     * @return JPanel containing the main content
     */
    private static JPanel createContentPanel(JFrame attendanceFrame, String userId, String role) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create input panel for recording attendance
        JPanel inputPanel = createInputPanel(attendanceFrame);
        contentPanel.add(inputPanel, BorderLayout.NORTH);

        // Create table panel for displaying attendance records
        JPanel tablePanel = createTablePanel();
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(0, 30, 0, 30)
        ));
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        return contentPanel;
    }

    /**
     * Creates the input panel for recording new attendance entries.
     *
     * @param attendanceFrame The parent JFrame for positioning
     * @return JPanel containing the input form
     */
    private static JPanel createInputPanel(JFrame attendanceFrame) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(CARD_WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(30, 30, 90, 30)
        ));

        // Create title label
        JLabel titleLabel = createInputPanelTitleLabel();

        // Create form panel with input fields
        JPanel formPanel = createFormPanel(attendanceFrame);

        // Create button panel with action buttons
        JPanel buttonPanel = createButtonPanel(attendanceFrame);

        inputPanel.add(titleLabel, BorderLayout.NORTH);
        inputPanel.add(formPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputPanel;
    }

    /**
     * Creates the title label for the input panel.
     *
     * @return JLabel for the input panel title
     */
    private static JLabel createInputPanelTitleLabel() {
        JLabel titleLabel = new JLabel("Record Attendance");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return titleLabel;
    }

    /**
     * Creates the form panel for attendance entry.
     *
     * @param attendanceFrame The parent JFrame for positioning
     * @return JPanel containing the form fields
     */
    private static JPanel createFormPanel(JFrame attendanceFrame) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 10, 8, 10);
        constraints.anchor = GridBagConstraints.WEST;

        // Create form components and assign to static fields
        employeeComboBox = createEmployeeComboBox();
        dateField = createDateField();
        statusComboBox = createStatusComboBox();
        timeInField = createTimeField("08:00");
        timeOutField = createTimeField("17:00");

        // Add form fields to panel
        addFormField(formPanel, "Employee:", employeeComboBox, constraints, 0, 0);
        addFormField(formPanel, "Date:", dateField, constraints, 0, 2);
        addFormField(formPanel, "Status:", statusComboBox, constraints, 1, 0);
        addFormField(formPanel, "Time In:", timeInField, constraints, 1, 2);
        addFormField(formPanel, "Time Out:", timeOutField, constraints, 1, 4);

        return formPanel;
    }

    /**
     * Creates the employee selection combo box.
     *
     * @return JComboBox for selecting an employee
     */
    private static JComboBox<String> createEmployeeComboBox() {
        List<String> employeeOptions = getEmployeeOptions();
        JComboBox<String> employeeComboBox = new JComboBox<>(employeeOptions.toArray(new String[0]));
        employeeComboBox.setFont(new Font("Garet", Font.PLAIN, 12));
        employeeComboBox.setPreferredSize(new Dimension(200, 30));
        return employeeComboBox;
    }

    /**
     * Creates the date input field with current date.
     *
     * @return JTextField for date input
     */
    private static JTextField createDateField() {
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 15);
        dateField.setFont(new Font("Garet", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return dateField;
    }

    /**
     * Creates the status selection combo box.
     *
     * @return JComboBox for selecting attendance status
     */
    private static JComboBox<String> createStatusComboBox() {
        String[] statusOptions = {"Present", "Absent", "Late", "On Leave", "Half Day"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setFont(new Font("Garet", Font.PLAIN, 12));
        statusComboBox.setPreferredSize(new Dimension(150, 30));
        return statusComboBox;
    }

    /**
     * Creates a time input field with default value.
     *
     * @param defaultValue The default time value (e.g., "08:00")
     * @return JTextField for time input
     */
    private static JTextField createTimeField(String defaultValue) {
        JTextField timeField = new JTextField(defaultValue, 10);
        timeField.setFont(new Font("Garet", Font.PLAIN, 12));
        timeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return timeField;
    }

    /**
     * Adds a form field (label and input) to the form panel.
     *
     * @param formPanel The panel to add the field to
     * @param labelText The label text
     * @param component The input component
     * @param constraints The GridBagConstraints to use
     * @param row The row number
     * @param col The column number
     */
    private static void addFormField(JPanel formPanel, String labelText, JComponent component, 
                                   GridBagConstraints constraints, int row, int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Garet", Font.BOLD, 12));
        label.setForeground(TEXT_BLACK);

        constraints.gridx = col;
        constraints.gridy = row;
        formPanel.add(label, constraints);
        
        constraints.gridx = col + 1;
        formPanel.add(component, constraints);
    }

    /**
     * Creates the button panel with action buttons for attendance actions.
     *
     * @param attendanceFrame The parent JFrame for positioning
     * @return JPanel containing the action buttons
     */
    private static JPanel createButtonPanel(JFrame attendanceFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton recordButton = createStyledButton("Record Attendance", BUTTON_ORANGE);
        JButton clearButton = createStyledButton("Clear", ACCENT_GREY);
        JButton refreshButton = createStyledButton("Refresh", ACCENT_GREY);

        // Add action listeners
        recordButton.addActionListener(e -> handleRecordAttendance(attendanceFrame));
        clearButton.addActionListener(e -> handleClearAllRecords(attendanceFrame));
        refreshButton.addActionListener(e -> handleRefreshData(attendanceFrame));

        buttonPanel.add(recordButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    /**
     * Creates the attendance table panel for displaying records.
     *
     * @return JPanel containing the attendance table
     */
    private static JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(0, 30, 0, 30)
        ));
        
        // Create table title
        JLabel tableTitle = createTableTitleLabel();

        // Create table model and table
        createAttendanceTable();

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 600));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Creates the table title label for the attendance table.
     *
     * @return JLabel for the table title
     */
    private static JLabel createTableTitleLabel() {
        JLabel tableTitle = new JLabel("Attendance Records");
        tableTitle.setFont(new Font("Garet", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_BLACK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return tableTitle;
    }

    /**
     * Creates the attendance table and sets up the table model.
     */
    private static void createAttendanceTable() {
        String[] columnNames = {"Employee ID", "Date", "Status", "Time In", "Time Out", "Hours Worked"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);
        styleAttendanceTable();
        updateAttendanceTable();
    }

    /**
     * Styles the attendance table for a modern appearance.
     */
    private static void styleAttendanceTable() {
        attendanceTable.setFont(new Font("Garet", Font.PLAIN, 12));
        attendanceTable.setRowHeight(30);
        attendanceTable.setGridColor(new Color(220, 220, 220));
        attendanceTable.setSelectionBackground(BUTTON_ORANGE);
        attendanceTable.setSelectionForeground(TEXT_WHITE);
        attendanceTable.setShowGrid(true);
        attendanceTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Style table header
        attendanceTable.getTableHeader().setFont(new Font("Garet", Font.BOLD, 12));
        attendanceTable.getTableHeader().setBackground(BUTTON_ORANGE);
        attendanceTable.getTableHeader().setForeground(TEXT_WHITE);
        attendanceTable.getTableHeader().setBorder(BorderFactory.createLineBorder(BUTTON_ORANGE));
    }

    /**
     * Creates a modern styled button with rounded corners.
     *
     * @param text The button text
     * @param backgroundColor The background color
     * @return JButton with custom styling
     */
    private static JButton createStyledButton(String text, Color backgroundColor) {
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
        button.setForeground(TEXT_WHITE);
        button.setBackground(backgroundColor);
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
     * Creates the footer panel for the attendance screen.
     *
     * @return JPanel containing the footer content
     */
    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(HEADER_DARK);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel copyrightLabel = new JLabel("© 2025 GEAR.HR - Attendance Tracking");
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
     * Gets employee options for the dropdown from EmployeeProfile.
     *
     * @return List of employee options for the combo box
     */
    private static List<String> getEmployeeOptions() {
        List<String> options = new ArrayList<>();
        options.add("Select Employee");
        try {
            List<model.Employee> employees = EmployeeProfile.getAllEmployees();
            options.addAll(
                employees.stream()
                    .map(emp -> emp.getEmployeeNumber() + " - " + emp.getFirstName() + " " + emp.getLastName())
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            options.add("1001 - Colin Bactong");
            options.add("1002 - Charlize Bactong");
            options.add("1003 - Angelica");
        }
        return options;
    }

    /**
     * Updates the attendance table with current data.
     */
    private static void updateAttendanceTable() {
        tableModel.setRowCount(0);
        for (AttendanceRecord record : attendanceService.getAllRecords()) {
            tableModel.addRow(new Object[]{
                record.getEmployeeId(),
                record.getDate(),
                record.getStatus(),
                record.getTimeIn(),
                record.getTimeOut(),
                record.getHoursWorked()
            });
        }
    }

    /**
     * Loads attendance records from the attendance_records.csv file into memory.
     * Ensures that all attendance data is loaded into the attendanceRecords map.
     * Handles missing or malformed files gracefully.
     */
    /**
     * Handles recording new attendance entry.
     * Reads form values, validates input, creates and saves a new attendance record.
     * Updates the table and clears the form.
     *
     * @param attendanceFrame The parent JFrame for positioning
     */
    private static void handleRecordAttendance(JFrame attendanceFrame) {
        // Read values from form fields
        String employeeSelection = (String) employeeComboBox.getSelectedItem();
        String date = dateField.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();
        String timeIn = timeInField.getText().trim();
        String timeOut = timeOutField.getText().trim();

        // Validate input
        if (employeeSelection == null || employeeSelection.equals("Select Employee")) {
            showModernMessage(attendanceFrame, "Please select an employee.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (date.isEmpty() || status.isEmpty() || timeIn.isEmpty() || timeOut.isEmpty()) {
            showModernMessage(attendanceFrame, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Extract employee ID from selection
        String employeeId = employeeSelection.split(" - ")[0];
        if (attendanceService.hasRecord(employeeId, date)) {
            showModernMessage(attendanceFrame, "Attendance for this employee on this date already exists.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            return;
        }
        AttendanceRecord record = new AttendanceRecord(employeeId, date, status, timeIn, timeOut);
        attendanceService.addRecord(record);
        updateAttendanceTable();
        showModernMessage(attendanceFrame, "Attendance recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        // Optionally clear fields for next entry
        employeeComboBox.setSelectedIndex(0);
        dateField.setText(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        statusComboBox.setSelectedIndex(0);
        timeInField.setText("08:00");
        timeOutField.setText("17:00");
    }

    /**
     * Handles clearing all attendance records.
     * Prompts for confirmation, clears all records, updates the table, and saves to CSV.
     *
     * @param attendanceFrame The parent JFrame for positioning
     */
    private static void handleClearAllRecords(JFrame attendanceFrame) {
        int confirm = JOptionPane.showConfirmDialog(attendanceFrame, 
            "Are you sure you want to clear ALL attendance records? This cannot be undone.", 
            "Confirm Clear All", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            attendanceService.clearAll();
            updateAttendanceTable();
            showModernMessage(attendanceFrame, "All attendance records have been cleared.", "Records Cleared", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Handles refreshing attendance data from CSV.
     * Updates the table and shows a confirmation message.
     *
     * @param attendanceFrame The parent JFrame for positioning
     */
    private static void handleRefreshData(JFrame attendanceFrame) {
        attendanceService.loadAttendanceRecordsFromCSV();
        updateAttendanceTable();
        showModernMessage(attendanceFrame, "Data refreshed successfully", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Called when an employee is deleted; delegates to AttendanceService. */
    public static void removeAttendanceRecords(String employeeId) {
        attendanceService.removeAttendanceRecords(employeeId);
    }
}
