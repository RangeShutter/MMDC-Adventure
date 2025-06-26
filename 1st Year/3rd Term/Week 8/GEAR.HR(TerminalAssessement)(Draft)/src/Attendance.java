import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.nio.file.*;
import javax.swing.table.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Attendance class handles employee attendance tracking and management
 * Provides interface for recording, viewing, and managing employee attendance records
 * Note: All information in this program are sample data for demonstration purposes
 */
public class Attendance {
    // Attendance data storage: key format "employeeID|date"
    private static final Map<String, AttendanceRecord> attendanceRecords = new HashMap<>();
    private static final String ATTENDANCE_CSV_FILE = "attendance_records.csv";
    
    // UI components
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

    // Initialize attendance data from CSV file
    static {
        loadAttendanceRecordsFromCSV();
        if (attendanceRecords.isEmpty()) {
            initializeSampleAttendanceData();
            saveAttendanceRecordsToCSV();
        }
    }

    /**
     * Displays the attendance management screen
     * Creates a comprehensive interface for managing employee attendance
     * @param parentFrame The parent frame for positioning
     * @param userId The current user's ID
     * @param role The current user's role
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
     * Creates and configures the attendance management frame
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
     * Creates the main panel with background styling
     */
    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_WHITE);
        return mainPanel;
    }

    /**
     * Creates the header panel with title and subtitle
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
     * Creates the header title label
     */
    private static JLabel createHeaderTitleLabel() {
        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    /**
     * Creates the header subtitle label
     */
    private static JLabel createHeaderSubtitleLabel() {
        JLabel subtitleLabel = new JLabel("Track employee attendance and manage time records");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return subtitleLabel;
    }

    /**
     * Creates the main content panel with input form and attendance table
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
     * Creates the input panel for recording new attendance entries
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
     * Creates the title label for the input panel
     */
    private static JLabel createInputPanelTitleLabel() {
        JLabel titleLabel = new JLabel("Record Attendance");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return titleLabel;
    }

    /**
     * Creates the form panel with all input fields
     */
    private static JPanel createFormPanel(JFrame attendanceFrame) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 10, 8, 10);
        constraints.anchor = GridBagConstraints.WEST;

        // Create form components
        JComboBox<String> employeeComboBox = createEmployeeComboBox();
        JTextField dateField = createDateField();
        JComboBox<String> statusComboBox = createStatusComboBox();
        JTextField timeInField = createTimeField("08:00");
        JTextField timeOutField = createTimeField("17:00");

        // Add form fields to panel
        addFormField(formPanel, "Employee:", employeeComboBox, constraints, 0, 0);
        addFormField(formPanel, "Date:", dateField, constraints, 0, 2);
        addFormField(formPanel, "Status:", statusComboBox, constraints, 1, 0);
        addFormField(formPanel, "Time In:", timeInField, constraints, 1, 2);
        addFormField(formPanel, "Time Out:", timeOutField, constraints, 1, 4);

        return formPanel;
    }

    /**
     * Creates the employee selection combo box
     */
    private static JComboBox<String> createEmployeeComboBox() {
        List<String> employeeOptions = getEmployeeOptions();
        JComboBox<String> employeeComboBox = new JComboBox<>(employeeOptions.toArray(new String[0]));
        employeeComboBox.setFont(new Font("Garet", Font.PLAIN, 12));
        employeeComboBox.setPreferredSize(new Dimension(200, 30));
        return employeeComboBox;
    }

    /**
     * Creates the date input field with current date
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
     * Creates the status selection combo box
     */
    private static JComboBox<String> createStatusComboBox() {
        String[] statusOptions = {"Present", "Absent", "Late", "On Leave", "Half Day"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setFont(new Font("Garet", Font.PLAIN, 12));
        statusComboBox.setPreferredSize(new Dimension(150, 30));
        return statusComboBox;
    }

    /**
     * Creates a time input field with default value
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
     * Adds a form field to the form panel
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
     * Creates the button panel with action buttons
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
     * Creates the attendance table panel
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
     * Creates the table title label
     */
    private static JLabel createTableTitleLabel() {
        JLabel tableTitle = new JLabel("Attendance Records");
        tableTitle.setFont(new Font("Garet", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_BLACK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        return tableTitle;
    }

    /**
     * Creates the attendance table with model and styling
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
     * Styles the attendance table with modern appearance
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
     * Creates a modern styled button with rounded corners
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
     * Shows a modern styled message dialog
     */
    private static void showModernMessage(JFrame parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    /**
     * Creates the footer panel with gradient background
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
     * Gets employee options for the dropdown from EmployeeProfile
     */
    private static List<String> getEmployeeOptions() {
        List<String> options = new ArrayList<>();
        options.add("Select Employee");
        
        // Get employees from EmployeeProfile
        try {
            java.util.List<EmployeeProfile.Employee> employees = EmployeeProfile.getAllEmployees();
            options.addAll(
                employees.stream()
                    .map(emp -> emp.getEmployeeNumber() + " - " + emp.getFirstName() + " " + emp.getLastName())
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            // Fallback to sample data if error
            options.add("1001 - Colin Bactong");
            options.add("1002 - Charlize Bactong");
            options.add("1003 - Angelica");
        }
        return options;
    }

    /**
     * Updates the attendance table with current data
     */
    private static void updateAttendanceTable() {
        tableModel.setRowCount(0);
        for (AttendanceRecord record : attendanceRecords.values()) {
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
     * Loads attendance records from CSV file
     */
    private static void loadAttendanceRecordsFromCSV() {
        if (!Files.exists(Paths.get(ATTENDANCE_CSV_FILE))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ATTENDANCE_CSV_FILE))) {
            reader.readLine(); // Skip header line

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String key = data[0] + "|" + data[1];
                    attendanceRecords.put(key, new AttendanceRecord(
                        data[0], data[1], data[2], data[3], data[4]
                    ));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading attendance records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves attendance records to CSV file
     */
    private static void saveAttendanceRecordsToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_CSV_FILE))) {
            // Write CSV header
            writer.println("EmployeeID,Date,Status,TimeIn,TimeOut");
            // Write each record
            for (AttendanceRecord record : attendanceRecords.values()) {
                writer.println(String.join(",",
                    record.getEmployeeId(),
                    record.getDate(),
                    record.getStatus(),
                    record.getTimeIn(),
                    record.getTimeOut()
                ));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving attendance records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Initializes sample attendance data for demonstration
     */
    private static void initializeSampleAttendanceData() {
        attendanceRecords.put("1001|2024-01-15", new AttendanceRecord("1001", "2024-01-15", "Present", "08:00", "17:00"));
        attendanceRecords.put("1002|2024-01-15", new AttendanceRecord("1002", "2024-01-15", "Present", "08:30", "17:30"));
        attendanceRecords.put("1001|2024-01-16", new AttendanceRecord("1001", "2024-01-16", "Late", "09:15", "17:00"));
        attendanceRecords.put("1003|2024-01-16", new AttendanceRecord("1003", "2024-01-16", "Present", "08:00", "17:00"));
    }

    /**
     * Removes all attendance records for a specific employee
     * Called when an employee is deleted from the system
     * @param employeeId The employee ID whose attendance records should be removed
     */
    public static void removeAttendanceRecords(String employeeId) {
        // Remove all attendance records for this employee
        attendanceRecords.entrySet().removeIf(entry -> entry.getKey().startsWith(employeeId + "|"));
        saveAttendanceRecordsToCSV();
    }

    /**
     * Inner class representing an attendance record
     * Contains all information about a single attendance entry
     */
    static class AttendanceRecord {
        private final String employeeId;
        private final String date;
        private final String status;
        private final String timeIn;
        private final String timeOut;

        /**
         * Creates a new attendance record
         * @param employeeId The employee's ID
         * @param date The attendance date
         * @param status The attendance status (Present, Absent, Late, etc.)
         * @param timeIn The time the employee checked in
         * @param timeOut The time the employee checked out
         */
        public AttendanceRecord(String employeeId, String date, String status, String timeIn, String timeOut) {
            this.employeeId = employeeId;
            this.date = date;
            this.status = status;
            this.timeIn = timeIn;
            this.timeOut = timeOut;
        }

        // Getter methods
        public String getEmployeeId() { return employeeId; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
        public String getTimeIn() { return timeIn; }
        public String getTimeOut() { return timeOut; }

        /**
         * Calculates and returns the hours worked based on time in and time out
         * @return Formatted string showing hours:minutes worked, or "N/A" if invalid
         */
        public String getHoursWorked() {
            if (timeIn.isEmpty() || timeOut.isEmpty()) {
                return "N/A";
            }
            
            try {
                String[] inParts = timeIn.split(":");
                String[] outParts = timeOut.split(":");
                
                int inHour = Integer.parseInt(inParts[0]);
                int inMin = Integer.parseInt(inParts[1]);
                int outHour = Integer.parseInt(outParts[0]);
                int outMin = Integer.parseInt(outParts[1]);
                
                int totalInMinutes = inHour * 60 + inMin;
                int totalOutMinutes = outHour * 60 + outMin;
                
                int diffMinutes = totalOutMinutes - totalInMinutes;
                int hours = diffMinutes / 60;
                int minutes = diffMinutes % 60;
                
                return String.format("%d:%02d", hours, minutes);
            } catch (Exception e) {
                return "N/A";
            }
        }
    }

    /**
     * Handles recording new attendance entry
     */
    private static void handleRecordAttendance(JFrame attendanceFrame) {
        // This method would need access to the form components
        // For now, show a placeholder message
        showModernMessage(attendanceFrame, "Attendance recording functionality would be implemented here", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles clearing all attendance records
     */
    private static void handleClearAllRecords(JFrame attendanceFrame) {
        int confirm = JOptionPane.showConfirmDialog(attendanceFrame, 
            "Are you sure you want to clear ALL attendance records? This cannot be undone.", 
            "Confirm Clear All", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            attendanceRecords.clear();
            updateAttendanceTable();
            saveAttendanceRecordsToCSV();
            showModernMessage(attendanceFrame, "All attendance records have been cleared.", "Records Cleared", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Handles refreshing attendance data
     */
    private static void handleRefreshData(JFrame attendanceFrame) {
        loadAttendanceRecordsFromCSV();
        updateAttendanceTable();
        showModernMessage(attendanceFrame, "Data refreshed successfully", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }
}
