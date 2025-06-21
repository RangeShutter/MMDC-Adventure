import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import javax.swing.table.*;

// Note: All information in this program are sample data for demonstration purposes 

public class EmployeeProfile {
    
    private static List<Employee> employees = new ArrayList<>();
    private static final String CSV_FILE = "employees.csv";
    private static JTable employeeTable;
    private static DefaultTableModel tableModel;

    // Static initializer block - loads employees from CSV when class is first loaded
    static {
        loadEmployeesFromCSV();
        if (employees.isEmpty()) {
            addSampleEmployees();  // Adds sample data if CSV is empty
        }
    }

    /**
     * Displays the main employee profile management screen
     * @param parentFrame The parent JFrame for positioning
     * @param userId The current user's ID (unused in current implementation)
     * @param role The current user's role (unused in current implementation)
     */
    public static void showProfileScreen(JFrame parentFrame, String userId, String role) {
        // Produces and configure main frame
        JFrame frame = new JFrame("Employee Management System");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);

        // Set up main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Produces table model with non-editable columns
        String[] columnNames = {"Employee Number", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        updateEmployeeTable();

        // Create button panel with employee management actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton viewButton = new JButton("View Employee");
        JButton newButton = new JButton("New Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(viewButton);
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Adds components to main panel
        mainPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Action listener for View button - shows details of selected employee
        viewButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String empNumber = (String) employeeTable.getValueAt(selectedRow, 0);
                Employee selectedEmployee = findEmployeeById(empNumber);
                if (selectedEmployee != null) {
                    showEmployeeDetails(frame, selectedEmployee);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an employee first", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action listener for New button - shows form to create new employee
        newButton.addActionListener(e -> showNewEmployeeForm(frame));

        // Action listener for Delete button - removes selected employee after confirmation
        deleteButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                String empNumber = (String) employeeTable.getValueAt(selectedRow, 0);
                String empName = (String) employeeTable.getValueAt(selectedRow, 2) + " " + 
                                (String) employeeTable.getValueAt(selectedRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(
                    frame, 
                    "Are you sure you want to delete employee " + empName + " (ID: " + empNumber + ")?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteEmployee(empNumber);
                    updateEmployeeTable();
                    JOptionPane.showMessageDialog(frame, "Employee deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an employee first", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action listener for Refresh button - reloads data from CSV
        refreshButton.addActionListener(e -> {
            loadEmployeesFromCSV();
            updateEmployeeTable();
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Deletes an employee from the system
     * @param empNumber The employee number of the employee to delete
     */
    private static void deleteEmployee(String empNumber) {
        employees.removeIf(e -> e.getEmployeeNumber().equals(empNumber));
        saveEmployeesToCSV();
    }

    /**
     * Displays detailed information about an employee including salary computation
     * @param parentFrame The parent frame for positioning
     * @param employee The employee object to display
     */
    private static void showEmployeeDetails(JFrame parentFrame, Employee employee) {
        // Create details frame
        JFrame detailsFrame = new JFrame("Employee Details - " + employee.getFirstName() + " " + employee.getLastName());
        detailsFrame.setSize(800, 600);
        detailsFrame.setLocationRelativeTo(parentFrame);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Employee details panel with all employee information
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addDetailField(detailsPanel, "Employee Number:", employee.getEmployeeNumber());
        addDetailField(detailsPanel, "Last Name:", employee.getLastName());
        addDetailField(detailsPanel, "First Name:", employee.getFirstName());
        addDetailField(detailsPanel, "SSS Number:", employee.getSssNumber());
        addDetailField(detailsPanel, "PhilHealth Number:", employee.getPhilHealthNumber());
        addDetailField(detailsPanel, "TIN:", employee.getTin());
        addDetailField(detailsPanel, "Pag-IBIG Number:", employee.getPagIbigNumber());
        addDetailField(detailsPanel, "Email:", employee.getEmail());
        addDetailField(detailsPanel, "Position:", employee.getPosition());
        addDetailField(detailsPanel, "Address:", employee.getAddress());
        addDetailField(detailsPanel, "Phone:", employee.getPhone());

        // Salary computation panel with month selection
        JPanel salaryPanel = new JPanel(new GridLayout(0, 2, 5, 3));
        JLabel monthLabel = new JLabel("Select Month:");
        JComboBox<String> monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December"
        });
        JButton computeButton = new JButton("Compute Salary");
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        salaryPanel.add(monthLabel);
        salaryPanel.add(monthCombo);
        salaryPanel.add(computeButton);
        salaryPanel.add(new JScrollPane(resultArea));

        // Action listener for Compute Salary button
        computeButton.addActionListener(e -> {
            String month = (String) monthCombo.getSelectedItem();
            String salaryInfo = SalaryComputation.computeSalary(employee, month);
            resultArea.setText(salaryInfo);
        });

        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(salaryPanel, BorderLayout.CENTER);

        detailsFrame.add(mainPanel);
        detailsFrame.setVisible(true);
    }

    /**
     * Displays a form to create a new employee record
     * @param parentFrame The parent frame for positioning
     */
    private static void showNewEmployeeForm(JFrame parentFrame) {
        JFrame newEmpFrame = new JFrame("New Employee");
        newEmpFrame.setSize(600, 600);
        newEmpFrame.setLocationRelativeTo(parentFrame);

        // Main form panel with grid layout
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form fields for all employee attributes
        JTextField empNumberField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField sssField = new JTextField();
        JTextField philHealthField = new JTextField();
        JTextField tinField = new JTextField();
        JTextField pagIbigField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField baseSalaryField = new JTextField();

        // Add all fields to the form panel
        addFormField(mainPanel, "Employee Number*:", empNumberField);
        addFormField(mainPanel, "Last Name*:", lastNameField);
        addFormField(mainPanel, "First Name*:", firstNameField);
        addFormField(mainPanel, "SSS Number:", sssField);
        addFormField(mainPanel, "PhilHealth Number:", philHealthField);
        addFormField(mainPanel, "TIN:", tinField);
        addFormField(mainPanel, "Pag-IBIG Number:", pagIbigField);
        addFormField(mainPanel, "Email*:", emailField);
        addFormField(mainPanel, "Position*:", positionField);
        addFormField(mainPanel, "Address:", addressField);
        addFormField(mainPanel, "Phone:", phoneField);
        addFormField(mainPanel, "Base Salary*:", baseSalaryField);

        // Submit button action - validates and saves new employee
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // Validate required fields
            if (empNumberField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                firstNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                positionField.getText().isEmpty() || baseSalaryField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(newEmpFrame, "Please fill all required fields (*)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double baseSalary = Double.parseDouble(baseSalaryField.getText());
                
                // Create new employee object
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

                // Add to list and save to CSV
                employees.add(newEmployee);
                saveEmployeesToCSV();
                
                // Initialize payroll record for the new employee
                Payroll.initializePayrollRecord(
                    empNumberField.getText(),
                    positionField.getText(),
                    baseSalary
                );
                
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
     */
    private static void addFormField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
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
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE))) {
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
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
    }
}