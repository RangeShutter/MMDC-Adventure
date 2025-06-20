import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/* Please take note all information given on this program are only samples such as the variables given */

public class EmployeeProfile {
    private static List<Employee> employees = new ArrayList<>();
    private static final String CSV_FILE = "employees.csv";

    static {
        loadEmployeesFromCSV();
        // Add sample data if CSV is empty
        if (employees.isEmpty()) {
            employees.add(new Employee("1001", "Colin Bactong", "Colin@MotorPH.com", "Developer", "Leyte, Palo", "0960 270 7931"));
            employees.add(new Employee("1002", "Charlize Bactong", "Charlize@MotorPH.com", "Manager", "Negros Occidental Silay City", "555-0202"));
            employees.add(new Employee("1003", "Angelica Mae Calipayan", "Angelica@MotorPH.com", "Developer", "Negros Occidental Silay City", "555-0202"));
            saveEmployeesToCSV();
        }
    }

    public static void showProfileScreen(JFrame parentFrame, String userId, String role) {
        JFrame frame = new JFrame("Employee Profile Management");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);

        JTabbedPane tabbedPane = new JTabbedPane();

        // View All Employees Tab
        JPanel viewPanel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Name", "Email", "Position", "Address", "Phone"};
        Object[][] data = employees.stream()
                .map(e -> new Object[]{e.getId(), e.getName(), e.getEmail(), e.getPosition(), e.getAddress(), e.getPhone()})
                .toArray(Object[][]::new);
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("View Employees", viewPanel);

        // Add/Edit Employee Tab
        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel idLabel = new JLabel("Employee ID:");
        JTextField idField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        editPanel.add(idField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        editPanel.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        editPanel.add(emailField, gbc);

        JLabel positionLabel = new JLabel("Position:");
        JTextField positionField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(positionLabel, gbc);
        gbc.gridx = 1;
        editPanel.add(positionField, gbc);

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        editPanel.add(addressField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        editPanel.add(phoneField, gbc);

        JButton addButton = new JButton("Add Employee");
        JButton updateButton = new JButton("Update Employee");
        updateButton.setEnabled(false); // Initially disabled
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setEnabled(false); // Initially disabled
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        editPanel.add(buttonPanel, gbc);

        tabbedPane.addTab("Add/Edit Employee", editPanel);

        // Button actions
    addButton.addActionListener(e -> {
    String id = idField.getText().trim();
    
    // Validate required fields
    if (nameField.getText().trim().isEmpty() || 
        emailField.getText().trim().isEmpty() ||
        positionField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Name, Email, and Position are required fields", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    Employee emp = new Employee(
        id,
        nameField.getText().trim(),
        emailField.getText().trim(),
        positionField.getText().trim(),
        addressField.getText().trim(),
        phoneField.getText().trim()
    );
    
    // Check if employee exists (update) or new (add)
    boolean exists = employees.stream().anyMatch(e2 -> e2.getId().equals(id));
    if (exists) {
        JOptionPane.showMessageDialog(frame, "Employee ID already exists. Use Update instead.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    employees.add(emp);
    saveEmployeesToCSV();
    updateTable(table, columns);
    clearFields(idField, nameField, emailField, positionField, addressField, phoneField);
    updateButton.setEnabled(false);
    deleteButton.setEnabled(false);
});
        updateButton.addActionListener(e -> {
            String id = idField.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No employee selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            employees.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst()
                .ifPresent(emp -> {
                    emp.setName(nameField.getText());
                    emp.setEmail(emailField.getText());
                    emp.setPosition(positionField.getText());
                    emp.setAddress(addressField.getText());
                    emp.setPhone(phoneField.getText());
                    saveEmployeesToCSV();
                    updateTable(table, columns);
                });
        });

        deleteButton.addActionListener(e -> {
            String id = idField.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No employee selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(
                frame, 
                "Are you sure you want to delete this employee?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                employees.removeIf(emp -> emp.getId().equals(id));
                saveEmployeesToCSV();
                updateTable(table, columns);
                clearFields(idField, nameField, emailField, positionField, addressField, phoneField);
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idField.setText(table.getValueAt(row, 0).toString());
                    nameField.setText(table.getValueAt(row, 1).toString());
                    emailField.setText(table.getValueAt(row, 2).toString());
                    positionField.setText(table.getValueAt(row, 3).toString());
                    addressField.setText(table.getValueAt(row, 4).toString());
                    phoneField.setText(table.getValueAt(row, 5).toString());
                    
                    // Enable update and delete buttons
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private static void updateTable(JTable table, String[] columns) {
        Object[][] newData = employees.stream()
                .map(e -> new Object[]{e.getId(), e.getName(), e.getEmail(), e.getPosition(), e.getAddress(), e.getPhone()})
                .toArray(Object[][]::new);
        table.setModel(new javax.swing.table.DefaultTableModel(newData, columns));
    }

    private static void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
    
    private static String generateNewEmployeeId() {
        int maxId = employees.stream()
            .mapToInt(e -> Integer.parseInt(e.getId()))
            .max()
            .orElse(1000);
        return String.valueOf(maxId + 1);
    }
    
    private static void saveEmployeesToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            writer.println("ID,Name,Email,Position,Address,Phone");
            for (Employee emp : employees) {
                writer.println(String.format("%s,\"%s\",%s,%s,\"%s\",%s",
                    emp.getId(),
                    emp.getName(),
                    emp.getEmail(),
                    emp.getPosition(),
                    emp.getAddress(),
                    emp.getPhone()));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void loadEmployeesFromCSV() {
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length == 6) {
                    // Remove quotes if present
                    String id = parts[0];
                    String name = parts[1].replace("\"", "");
                    String email = parts[2];
                    String position = parts[3];
                    String address = parts[4].replace("\"", "");
                    String phone = parts[5];
                    
                    employees.add(new Employee(id, name, email, position, address, phone));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class Employee {
        private String id;
        private String name;
        private String email;
        private String position;
        private String address;
        private String phone;

        public Employee(String id, String name, String email, String position, String address, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.position = position;
            this.address = address;
            this.phone = phone;
        }

        // Getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
}