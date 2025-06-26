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

/*
 * Attendance class handles employee attendance tracking
 * Note: All information in this program are sample data for demonstration purposes
 */
public class Attendance {
    // Stores attendance records with key format "employeeID|date"
    private static Map<String, AttendanceRecord> attendanceRecords = new HashMap<>();
    private static final String ATTENDANCE_CSV = "attendance_records.csv";
    private static JTable attendanceTable;
    private static DefaultTableModel tableModel;

    // Modern formal color scheme (matching User/Main/EmployeeProfile)
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color HEADER_DARK = new Color(34, 34, 34); // #222222
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color BORDER_GREY = new Color(68, 68, 68); // #444444
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(180, 180, 180);
    private static final Color TEXT_BLACK = Color.BLACK;
    private static final Color ACCENT = new Color(120, 120, 120); // Subtle accent
    private static final Color BUTTON_BLUE = new Color(52, 152, 219); // #3498db

    // Static initializer - loads attendance data from CSV
    static {
        loadAttendanceRecordsFromCSV();
        if (attendanceRecords.isEmpty()) {
            initializeSampleAttendanceData();
            saveAttendanceRecordsToCSV();
        }
    }

    /**
     * Displays the attendance management screen
     * @param parentFrame The parent frame for positioning
     * @param userId The current user's ID
     * @param role The current user's role
     */
    public static void showAttendanceScreen(JFrame parentFrame, String userId, String role) {
        // Create and configure attendance frame
        JFrame frame = new JFrame("Attendance Management System");
        frame.setSize(1200, 1000);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);
        frame.setResizable(false);

        // Set application icon (if available)
        try {
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }

        // Main panel with modern design
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BG_WHITE);

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create content panel
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
     * Creates a modern header panel
     */
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));

        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Track employee attendance and manage time records");
        subtitleLabel.setFont(new Font("Garet", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_GREY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        return headerPanel;
    }

    /**
     * Creates the main content panel
     */
    private static JPanel createContentPanel(JFrame frame, String userId, String role) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create input panel
        JPanel inputPanel = createInputPanel(frame);
        contentPanel.add(inputPanel, BorderLayout.NORTH);

        // Create table panel
        JPanel tablePanel = createTablePanel();
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(0, 30, 0, 30)
        ));
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        return contentPanel;
    }

    /**
     * Creates the input panel for recording attendance
     */
    private static JPanel createInputPanel(JFrame frame) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(CARD_WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GREY, 2),
            BorderFactory.createEmptyBorder(30, 30, 90, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel("Record Attendance");
        titleLabel.setFont(new Font("Garet", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee selection
        JLabel empLabel = new JLabel("Employee:");
        empLabel.setFont(new Font("Garet", Font.BOLD, 12));
        empLabel.setForeground(TEXT_BLACK);

        // Get employee list from EmployeeProfile
        List<String> employeeOptions = getEmployeeOptions();
        JComboBox<String> empCombo = new JComboBox<>(employeeOptions.toArray(new String[0]));
        empCombo.setFont(new Font("Garet", Font.PLAIN, 12));
        empCombo.setPreferredSize(new Dimension(200, 30));

        // Date selection
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Garet", Font.BOLD, 12));
        dateLabel.setForeground(TEXT_BLACK);

        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 15);
        dateField.setFont(new Font("Garet", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Status selection
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Garet", Font.BOLD, 12));
        statusLabel.setForeground(TEXT_BLACK);

        String[] statusOptions = {"Present", "Absent", "Late", "On Leave", "Half Day"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setFont(new Font("Garet", Font.PLAIN, 12));
        statusCombo.setPreferredSize(new Dimension(150, 30));

        // Time in/out fields
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Garet", Font.BOLD, 12));
        timeInLabel.setForeground(TEXT_BLACK);

        JTextField timeInField = new JTextField("08:00", 10);
        timeInField.setFont(new Font("Garet", Font.PLAIN, 12));
        timeInField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel timeOutLabel = new JLabel("Time Out:");
        timeOutLabel.setFont(new Font("Garet", Font.BOLD, 12));
        timeOutLabel.setForeground(TEXT_BLACK);

        JTextField timeOutField = new JTextField("17:00", 10);
        timeOutField.setFont(new Font("Garet", Font.PLAIN, 12));
        timeOutField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Add form fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(empLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(empCombo, gbc);

        gbc.gridx = 2;
        formPanel.add(dateLabel, gbc);
        gbc.gridx = 3;
        formPanel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);

        gbc.gridx = 2;
        formPanel.add(timeInLabel, gbc);
        gbc.gridx = 3;
        formPanel.add(timeInField, gbc);

        gbc.gridx = 4;
        formPanel.add(timeOutLabel, gbc);
        gbc.gridx = 5;
        formPanel.add(timeOutField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton recordButton = createModernButton("Record Attendance", BUTTON_BLUE);
        JButton clearButton = createModernButton("Clear", ACCENT);
        JButton refreshButton = createModernButton("Refresh", ACCENT);

        // Action listeners
        recordButton.addActionListener(e -> {
            String empId = (String) empCombo.getSelectedItem();
            String date = dateField.getText();
            String status = (String) statusCombo.getSelectedItem();
            String timeIn = timeInField.getText();
            String timeOut = timeOutField.getText();

            if (empId == null || empId.equals("Select Employee") || date.isEmpty()) {
                showModernMessage(frame, "Please select an employee and enter a date", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Extract employee ID from the selected option
            String employeeId = empId.split(" - ")[0];

            // Create attendance record
            AttendanceRecord record = new AttendanceRecord(employeeId, date, status, timeIn, timeOut);
            String key = employeeId + "|" + date;
            attendanceRecords.put(key, record);
            saveAttendanceRecordsToCSV();
            updateAttendanceTable();

            showModernMessage(frame, "Attendance recorded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to clear ALL attendance records? This cannot be undone.", "Confirm Clear All", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                attendanceRecords.clear();
                updateAttendanceTable();
                saveAttendanceRecordsToCSV();
                showModernMessage(frame, "All attendance records have been cleared.", "Records Cleared", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        refreshButton.addActionListener(e -> {
            loadAttendanceRecordsFromCSV();
            updateAttendanceTable();
            showModernMessage(frame, "Data refreshed successfully", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(recordButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        inputPanel.add(titleLabel, BorderLayout.NORTH);
        inputPanel.add(formPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputPanel;
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
        // Table title
        JLabel tableTitle = new JLabel("Attendance Records");
        tableTitle.setFont(new Font("Garet", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_BLACK);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Create table model
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

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 600)); // Triple the height

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Styles the attendance table with modern appearance
     */
    private static void styleAttendanceTable() {
        attendanceTable.setFont(new Font("Garet", Font.PLAIN, 12));
        attendanceTable.setRowHeight(30);
        attendanceTable.setGridColor(new Color(220, 220, 220));
        attendanceTable.setSelectionBackground(BUTTON_BLUE);
        attendanceTable.setSelectionForeground(TEXT_WHITE);
        attendanceTable.setShowGrid(true);
        attendanceTable.setIntercellSpacing(new Dimension(1, 1));
        attendanceTable.getTableHeader().setFont(new Font("Garet", Font.BOLD, 12));
        attendanceTable.getTableHeader().setBackground(BUTTON_BLUE);
        attendanceTable.getTableHeader().setForeground(TEXT_WHITE);
        attendanceTable.getTableHeader().setBorder(BorderFactory.createLineBorder(BUTTON_BLUE));
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
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(HEADER_DARK);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel footerLabel = new JLabel("© 2025 GEAR.HR - Attendance Tracking");
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
     * Gets employee options for the dropdown
     */
    private static List<String> getEmployeeOptions() {
        List<String> options = new ArrayList<>();
        options.add("Select Employee");
        
        // This would ideally get the employee list from EmployeeProfile
        // For now, we'll use sample data
        options.add("1001 - Colin Bactong");
        options.add("1002 - Charlize Bactong");
        options.add("1003 - Angelica");
        
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
        if (!Files.exists(Paths.get(ATTENDANCE_CSV))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ATTENDANCE_CSV))) {
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_CSV))) {
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
     * Initializes sample attendance data
     */
    private static void initializeSampleAttendanceData() {
        attendanceRecords.put("1001|2024-01-15", new AttendanceRecord("1001", "2024-01-15", "Present", "08:00", "17:00"));
        attendanceRecords.put("1002|2024-01-15", new AttendanceRecord("1002", "2024-01-15", "Present", "08:30", "17:30"));
        attendanceRecords.put("1001|2024-01-16", new AttendanceRecord("1001", "2024-01-16", "Late", "09:15", "17:00"));
        attendanceRecords.put("1003|2024-01-16", new AttendanceRecord("1003", "2024-01-16", "Present", "08:00", "17:00"));
    }

    /**
     * Removes all attendance records for a specific employee
     * @param employeeId The employee ID whose attendance records should be removed
     */
    public static void removeAttendanceRecords(String employeeId) {
        // Remove all attendance records for this employee
        attendanceRecords.entrySet().removeIf(entry -> entry.getKey().startsWith(employeeId + "|"));
        saveAttendanceRecordsToCSV();
    }

    /**
     * Inner class representing an attendance record
     */
    static class AttendanceRecord {
        private String employeeId;
        private String date;
        private String status;
        private String timeIn;
        private String timeOut;

        public AttendanceRecord(String employeeId, String date, String status, String timeIn, String timeOut) {
            this.employeeId = employeeId;
            this.date = date;
            this.status = status;
            this.timeIn = timeIn;
            this.timeOut = timeOut;
        }

        // Getters
        public String getEmployeeId() { return employeeId; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
        public String getTimeIn() { return timeIn; }
        public String getTimeOut() { return timeOut; }

        /**
         * Calculates hours worked
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
}
