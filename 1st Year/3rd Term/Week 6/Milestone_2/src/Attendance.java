import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/* Please take note all information given on this program are only samples such as the variables given */

public class Attendance {
    private static Map<String, String> attendanceRecords = new HashMap<>();

    public static void showAttendanceScreen(JFrame parentFrame, String userId, String role) {
        JFrame frame = new JFrame("Attendance Management");
        frame.setSize(900, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top panel with controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel empIdLabel = new JLabel("Employee ID:");
        JTextField empIdField = new JTextField(10);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(10);
        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Late", "On Leave"});
        
        JButton addButton = new JButton("Record Attendance");
        topPanel.add(empIdLabel);
        topPanel.add(empIdField);
        topPanel.add(dateLabel);
        topPanel.add(dateField);
        topPanel.add(statusLabel);
        topPanel.add(statusCombo);
        topPanel.add(addButton);

        // Center panel with table
        String[] columns = {"Employee ID", "Date", "Status"};
        Object[][] data = attendanceRecords.entrySet().stream()
                .map(e -> {
                    String[] parts = e.getKey().split("\\|");
                    return new Object[]{parts[0], parts[1], e.getValue()};
                })
                .toArray(Object[][]::new);
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empId = empIdField.getText();
                String date = dateField.getText();
                String status = (String) statusCombo.getSelectedItem();
                
                if (empId.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String key = empId + "|" + date;
                attendanceRecords.put(key, status);
                
                // Update table
                Object[][] newData = attendanceRecords.entrySet().stream()
                        .map(entry -> {
                            String[] parts = entry.getKey().split("\\|");
                            return new Object[]{parts[0], parts[1], entry.getValue()};
                        })
                        .toArray(Object[][]::new);
                
                table.setModel(new javax.swing.table.DefaultTableModel(
                    newData,
                    columns
                ));
                
                empIdField.setText("");
                dateField.setText("");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
