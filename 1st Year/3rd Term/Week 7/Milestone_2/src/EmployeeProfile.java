import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import javax.swing.table.*;

public class EmployeeProfile {
    
    private static List<Employee> employees = new ArrayList<>();
    private static final String CSV_FILE = "employees.csv";
    private static JTable employeeTable;
    private static DefaultTableModel tableModel;

    static {
        loadEmployeesFromCSV();
        if (employees.isEmpty()) {
            addSampleEmployees();
        }
    }

    public static void showProfileScreen(JFrame parentFrame, String userId, String role) {
        JFrame frame = new JFrame("Employee Management System");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model with the required columns
        String[] columnNames = {"Employee Number", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        employeeTable = new JTable(tableModel);
        updateEmployeeTable();

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton viewButton = new JButton("View Employee");
        JButton newButton = new JButton("New Employee");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(viewButton);
        buttonPanel.add(newButton);
        buttonPanel.add(refreshButton);

        // Add components to main panel
        mainPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
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

        newButton.addActionListener(e -> showNewEmployeeForm(frame));

        refreshButton.addActionListener(e -> {
            loadEmployeesFromCSV();
            updateEmployeeTable();
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void showEmployeeDetails(JFrame parentFrame, Employee employee) {
        JFrame detailsFrame = new JFrame("Employee Details - " + employee.getFirstName() + " " + employee.getLastName());
        detailsFrame.setSize(800, 600);
        detailsFrame.setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Employee details panel
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

        // Salary computation panel
        JPanel salaryPanel = new JPanel(new GridLayout(0, 2, 5, 5));
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

    private static void showNewEmployeeForm(JFrame parentFrame) {
        JFrame newEmpFrame = new JFrame("New Employee");
        newEmpFrame.setSize(600, 500);
        newEmpFrame.setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form fields
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

        // Add fields to panel
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

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // Validate required fields
            if (empNumberField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                firstNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                positionField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(newEmpFrame, "Please fill all required fields (*)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

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

            // Add to list and save
            employees.add(newEmployee);
            saveEmployeesToCSV();
            updateEmployeeTable();
            newEmpFrame.dispose();
            JOptionPane.showMessageDialog(parentFrame, "Employee added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        newEmpFrame.add(mainPanel, BorderLayout.CENTER);
        newEmpFrame.add(buttonPanel, BorderLayout.SOUTH);
        newEmpFrame.setVisible(true);
    }

    // Helper methods
    private static void addDetailField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value != null ? value : ""));
    }

    private static void addFormField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private static void updateEmployeeTable() {
        tableModel.setRowCount(0);
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

    private static Employee findEmployeeById(String empNumber) {
        return employees.stream()
                .filter(e -> e.getEmployeeNumber().equals(empNumber))
                .findFirst()
                .orElse(null);
    }

    private static void loadEmployeesFromCSV() {
        employees.clear();
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            // Skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
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

    private static void saveEmployeesToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
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

        // Getters
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