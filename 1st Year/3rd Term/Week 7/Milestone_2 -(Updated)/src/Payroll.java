import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.*;

// Note: All information in this program are sample data for demonstration purposes 

public class Payroll {
    // Stores payroll records with employee ID as key
    private static Map<String, PayrollRecord> payrollRecords = new HashMap<>();
    private static final String PAYROLL_CSV = "payroll_records.csv";

    // Static initializer - loads payroll data when class is first loaded
    static {
        loadPayrollRecordsFromCSV();
        
        // Initialize sample data if no records exist
        if (payrollRecords.isEmpty()) {
            initializeSampleData();
            savePayrollRecordsToCSV();
        }
    }

    /**
     * Initializes a new payroll record for an employee with default rates based on position
     * @param employeeId The employee's ID
     * @param position The employee's position (affects default rates)
     * @param baseSalary The employee's base salary
     */
    public static void initializePayrollRecord(String employeeId, String position, double baseSalary) {
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
            savePayrollRecordsToCSV();
        }
    }

    /**
     * Initializes sample payroll data for demonstration purposes
     */
    private static void initializeSampleData() {
        payrollRecords.put("Colin", new PayrollRecord(35000, 
            0.045f, 0.04f, 0.02f, 0.15f, 1500, 1000, 800));
        
        payrollRecords.put("Charlize", new PayrollRecord(60000, 
            0.045f, 0.04f, 0.02f, 0.12f, 1500, 800, 600));
        
        payrollRecords.put("Angelica", new PayrollRecord(35000, 
            0.045f, 0.04f, 0.02f, 0.12f, 1500, 800, 600));
    }

    /**
     * Displays the payroll management screen with tabs for calculation and viewing records
     * @param parentFrame The parent frame for positioning
     * @param userId The current user's ID (unused in current implementation)
     * @param role The current user's role (unused in current implementation)
     */
    public static void showPayrollScreen(JFrame parentFrame, String userId, String role) {
        // Create and configure main frame
        JFrame frame = new JFrame("Payroll Management");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);

        // Create tabbed interface
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create Calculate Payroll tab
        JPanel calcPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee ID field
        JLabel empIdLabel = new JLabel("Employee ID:");
        JTextField empIdField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        calcPanel.add(empIdLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(empIdField, gbc);

        // Base Salary field
        JLabel salaryLabel = new JLabel("Base Salary:");
        JTextField salaryField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(salaryLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(salaryField, gbc);

        // Deductions section header
        JLabel deductionsLabel = new JLabel("Deductions:");
        deductionsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        calcPanel.add(deductionsLabel, gbc);

        // SSS rate field
        JLabel sssLabel = new JLabel("SSS Rate (%):");
        JTextField sssField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        calcPanel.add(sssLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(sssField, gbc);

        // PhilHealth rate field
        JLabel philHealthLabel = new JLabel("PhilHealth Rate (%):");
        JTextField philHealthField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(philHealthLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(philHealthField, gbc);

        // Pag-IBIG rate field
        JLabel pagIbigLabel = new JLabel("Pag-IBIG Rate (%):");
        JTextField pagIbigField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(pagIbigLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(pagIbigField, gbc);

        // Withholding tax field
        JLabel taxLabel = new JLabel("Withholding Tax Rate (%):");
        JTextField taxField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(taxLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(taxField, gbc);

        // Allowances section header
        JLabel allowancesLabel = new JLabel("Allowances:");
        allowancesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        calcPanel.add(allowancesLabel, gbc);

        // Rice subsidy field
        JLabel riceLabel = new JLabel("Rice Subsidy:");
        JTextField riceField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        calcPanel.add(riceLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(riceField, gbc);

        // Phone allowance field
        JLabel phoneLabel = new JLabel("Phone Allowance:");
        JTextField phoneField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(phoneField, gbc);

        // Clothing allowance field
        JLabel clothingLabel = new JLabel("Clothing Allowance:");
        JTextField clothingField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(clothingLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(clothingField, gbc);

        // Calculate button
        JButton calculateButton = new JButton("Calculate Payroll");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        calcPanel.add(calculateButton, gbc);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        calcPanel.add(refreshButton, gbc);

        // Results display area
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        calcPanel.add(new JScrollPane(resultArea), gbc);

        tabbedPane.addTab("Calculate Payroll", calcPanel);

        // Create View Payroll Records tab
        JPanel viewPanel = new JPanel(new BorderLayout());
        String[] columns = {
            "Employee ID", "Base Salary", 
            "SSS", "PhilHealth", "Pag-IBIG", "Tax", 
            "Rice", "Phone", "Clothing", 
            "Total Deductions", "Total Allowances", "Net Salary"
        };
        
        // Create non-editable table model
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        refreshPayrollTable(tableModel);
        
        // Refresh button action
        refreshButton.addActionListener(e -> refreshPayrollTable(tableModel));

        JScrollPane scrollPane = new JScrollPane(table);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("View Payroll Records", viewPanel);

        // Calculate button action - performs payroll calculation
        calculateButton.addActionListener(e -> {
            try {
                // Get all input values
                String empId = empIdField.getText();
                double baseSalary = Double.parseDouble(salaryField.getText());
                float sssRate = Float.parseFloat(sssField.getText()) / 100;
                float philHealthRate = Float.parseFloat(philHealthField.getText()) / 100;
                float pagIbigRate = Float.parseFloat(pagIbigField.getText()) / 100;
                float withHoldingTax = Float.parseFloat(taxField.getText()) / 100;
                float riceSubsidy = Float.parseFloat(riceField.getText());
                float phoneAllowance = Float.parseFloat(phoneField.getText());
                float clothingAllowance = Float.parseFloat(clothingField.getText());

                // Create new payroll record
                PayrollRecord record = new PayrollRecord(
                    baseSalary, 
                    sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                    riceSubsidy, phoneAllowance, clothingAllowance
                );
                
                // Save record and update CSV
                payrollRecords.put(empId, record);
                savePayrollRecordsToCSV();

                // Display formatted results
                resultArea.setText(String.format(
                    "PAYROLL CALCULATION FOR %s\n" +
                    "=================================\n" +
                    "Base Salary:        ₱%,.2f\n\n" +
                    "DEDUCTIONS:\n" +
                    "SSS (%.2f%%):       ₱%,.2f\n" +
                    "PhilHealth (%.2f%%): ₱%,.2f\n" +
                    "Pag-IBIG (%.2f%%):   ₱%,.2f\n" +
                    "Withholding Tax (%.2f%%): ₱%,.2f\n" +
                    "Total Deductions:   ₱%,.2f\n\n" +
                    "ALLOWANCES:\n" +
                    "Rice Subsidy:       ₱%,.2f\n" +
                    "Phone Allowance:    ₱%,.2f\n" +
                    "Clothing Allowance: ₱%,.2f\n" +
                    "Total Allowances:   ₱%,.2f\n\n" +
                    "NET SALARY:         ₱%,.2f",
                    empId,
                    record.baseSalary,
                    record.sssRate * 100, record.getSSSDeduction(),
                    record.philHealthRate * 100, record.getPhilHealthDeduction(),
                    record.pagIbigRate * 100, record.getPagIbigDeduction(),
                    record.withHoldingTax * 100, record.getTaxDeduction(),
                    record.calculateTotalDeductions(),
                    record.riceSubsidy,
                    record.phoneAllowance,
                    record.clothingAllowance,
                    record.calculateTotalAllowances(),
                    record.calculateNetSalary()
                ));

                refreshPayrollTable(tableModel);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Table selection listener - populates fields when a record is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String empId = table.getValueAt(row, 0).toString();
                    PayrollRecord record = payrollRecords.get(empId);
                    
                    // Populate fields with selected record's data
                    empIdField.setText(empId);
                    salaryField.setText(table.getValueAt(row, 1).toString());
                    sssField.setText(String.valueOf(record.sssRate * 100));
                    philHealthField.setText(String.valueOf(record.philHealthRate * 100));
                    pagIbigField.setText(String.valueOf(record.pagIbigRate * 100));
                    taxField.setText(String.valueOf(record.withHoldingTax * 100));
                    riceField.setText(String.valueOf(record.riceSubsidy));
                    phoneField.setText(String.valueOf(record.phoneAllowance));
                    clothingField.setText(String.valueOf(record.clothingAllowance));
                    
                    // Switch to calculation tab
                    tabbedPane.setSelectedIndex(0);
                }
            }
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    /**
     * Refreshes the payroll records table with current data
     * @param tableModel The table model to update
     */
    private static void refreshPayrollTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);  // Clear existing data
        // Add all payroll records to the table
        for (Map.Entry<String, PayrollRecord> entry : payrollRecords.entrySet()) {
            PayrollRecord r = entry.getValue();
            tableModel.addRow(new Object[]{
                entry.getKey(),
                r.baseSalary,
                String.format("%.2f%%", r.sssRate * 100),
                String.format("%.2f%%", r.philHealthRate * 100),
                String.format("%.2f%%", r.pagIbigRate * 100),
                String.format("%.2f%%", r.withHoldingTax * 100),
                r.riceSubsidy,
                r.phoneAllowance,
                r.clothingAllowance,
                r.calculateTotalDeductions(),
                r.calculateTotalAllowances(),
                r.calculateNetSalary()
            });
        }
    }

    /**
     * Loads payroll records from CSV file into memory
     */
    private static void loadPayrollRecordsFromCSV() {
        if (!Files.exists(Paths.get(PAYROLL_CSV))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PAYROLL_CSV))) {
            reader.readLine(); // Skip header line

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8) {
                    payrollRecords.put(data[0], new PayrollRecord(
                        Double.parseDouble(data[1]),
                        Float.parseFloat(data[2]),
                        Float.parseFloat(data[3]),
                        Float.parseFloat(data[4]),
                        Float.parseFloat(data[5]),
                        Float.parseFloat(data[6]),
                        Float.parseFloat(data[7]),
                        Float.parseFloat(data[8])
                    ));
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYROLL_CSV))) {
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