import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/*
 * Attendance class handles employee attendance tracking
 * Note: All information in this program are sample data for demonstration purposes
 */
public class Attendance {
    // Stores attendance records with key format "employeeID|date"
    private static Map<String, String> attendanceRecords = new HashMap<>();

    /**
     * Displays the attendance management screen
     * @param parentFrame The parent frame for positioning
     * @param userId The current user's ID
     * @param role The current user's role
     */
    public static void showAttendanceScreen(JFrame parentFrame, String userId, String role) {
        // Create and configure attendance frame
        JFrame frame = new JFrame("Attendance Management");
        frame.setSize(900, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);

        // Main panel with border layout
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top panel with input controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel empIdLabel = new JLabel("Employee ID:");
        JTextField empIdField = new JTextField(10);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(10);
        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Late", "On Leave"});
        
        JButton addButton = new JButton("Record Attendance");
        
        // Add components to top panel
        topPanel.add(empIdLabel);
        topPanel.add(empIdField);
        topPanel.add(dateLabel);
        topPanel.add(dateField);
        topPanel.add(statusLabel);
        topPanel.add(statusCombo);
        topPanel.add(addButton);

        // Create table with attendance data
        String[] columns = {"Employee ID", "Date", "Status"};
        // Convert attendance records to table data format
        Object[][] data = attendanceRecords.entrySet().stream()
                .map(e -> {
                    String[] parts = e.getKey().split("\\|");
                    return new Object[]{parts[0], parts[1], e.getValue()};
                })
                .toArray(Object[][]::new);
        
        // Create table with scroll pane
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action listener for Record Attendance button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empId = empIdField.getText();
                String date = dateField.getText();
                String status = (String) statusCombo.getSelectedItem();
                
                // Validate input
                if (empId.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create composite key and store record
                String key = empId + "|" + date;
                attendanceRecords.put(key, status);
                
                // Update table data
                Object[][] newData = attendanceRecords.entrySet().stream()
                        .map(entry -> {
                            String[] parts = entry.getKey().split("\\|");
                            return new Object[]{parts[0], parts[1], entry.getValue()};
                        })
                        .toArray(Object[][]::new);
                
                // Update table model
                table.setModel(new javax.swing.table.DefaultTableModel(
                    newData,
                    columns
                ));
                
                // Clear input fields
                empIdField.setText("");
                dateField.setText("");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}