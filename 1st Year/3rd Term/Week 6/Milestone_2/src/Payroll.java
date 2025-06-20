import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


/* Please take note all information given on this program are only samples such as the variables given */
public class Payroll {
    private static Map<String, PayrollRecord> payrollRecords = new HashMap<>();

    static {
        // Given variables and rates are only examples
        payrollRecords.put("Colin", new PayrollRecord(35000, 
            0.045f,  // SSS
            0.04f,   // PhilHealth
            0.02f,   // Pag-IBIG
            0.15f,   // Withholding Tax
            1500,    // Rice Subsidy
            1000,    // Phone Allowance
            800      // Clothing Allowance
        ));
        
        payrollRecords.put("Charlize", new PayrollRecord(60000, 
            0.045f,  // SSS
            0.04f,   // PhilHealth
            0.02f,   // Pag-IBIG
            0.12f,   // Withholding Tax
            1500,    // Rice Subsidy
            800,     // Phone Allowance
            600      // Clothing Allowance
        ));
           payrollRecords.put("Angelica", new PayrollRecord(35000, 
            0.045f,  // SSS
            0.04f,   // PhilHealth
            0.02f,   // Pag-IBIG
            0.12f,   // Withholding Tax
            1500,    // Rice Subsidy
            800,     // Phone Allowance
            600      // Clothing Allowance
        ));
    }

    public static void showPayrollScreen(JFrame parentFrame, String userId, String role) {
        JFrame frame = new JFrame("Payroll Management");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Calculate Payroll Tab
        JPanel calcPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee ID
        JLabel empIdLabel = new JLabel("Employee ID:");
        JTextField empIdField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        calcPanel.add(empIdLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(empIdField, gbc);

        // Base Salary
        JLabel salaryLabel = new JLabel("Base Salary:");
        JTextField salaryField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(salaryLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(salaryField, gbc);

        // Deductions Section
        JLabel deductionsLabel = new JLabel("Deductions:");
        deductionsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        calcPanel.add(deductionsLabel, gbc);

        // SSS
        JLabel sssLabel = new JLabel("SSS Rate (%):");
        JTextField sssField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        calcPanel.add(sssLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(sssField, gbc);

        // PhilHealth
        JLabel philHealthLabel = new JLabel("PhilHealth Rate (%):");
        JTextField philHealthField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(philHealthLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(philHealthField, gbc);

        // Pag-IBIG
        JLabel pagIbigLabel = new JLabel("Pag-IBIG Rate (%):");
        JTextField pagIbigField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(pagIbigLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(pagIbigField, gbc);

        // Withholding Tax
        JLabel taxLabel = new JLabel("Withholding Tax Rate (%):");
        JTextField taxField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(taxLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(taxField, gbc);

        // Allowances Section
        JLabel allowancesLabel = new JLabel("Allowances:");
        allowancesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        calcPanel.add(allowancesLabel, gbc);

        // Rice Subsidy
        JLabel riceLabel = new JLabel("Rice Subsidy:");
        JTextField riceField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        calcPanel.add(riceLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(riceField, gbc);

        // Phone Allowance
        JLabel phoneLabel = new JLabel("Phone Allowance:");
        JTextField phoneField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(phoneField, gbc);

        // Clothing Allowance
        JLabel clothingLabel = new JLabel("Clothing Allowance:");
        JTextField clothingField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        calcPanel.add(clothingLabel, gbc);
        gbc.gridx = 1;
        calcPanel.add(clothingField, gbc);

        // Calculate Button
        JButton calculateButton = new JButton("Calculate Payroll");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        calcPanel.add(calculateButton, gbc);

        // Results Area
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        gbc.gridy++;
        calcPanel.add(new JScrollPane(resultArea), gbc);

        tabbedPane.addTab("Calculate Payroll", calcPanel);

        // View Payroll Records Tab
        JPanel viewPanel = new JPanel(new BorderLayout());
        String[] columns = {
            "Employee ID", "Base Salary", 
            "SSS", "PhilHealth", "Pag-IBIG", "Tax", 
            "Rice", "Phone", "Clothing", 
            "Total Deductions", "Total Allowances", "Net Salary"
        };
        
        Object[][] data = payrollRecords.entrySet().stream()
                .map(e -> {
                    PayrollRecord r = e.getValue();
                    return new Object[]{
                        e.getKey(),
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
                    };
                })
                .toArray(Object[][]::new);
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("View Payroll Records", viewPanel);

        // Calculate button action
        calculateButton.addActionListener(e -> {
            try {
                String empId = empIdField.getText();
                double baseSalary = Double.parseDouble(salaryField.getText());
                float sssRate = Float.parseFloat(sssField.getText()) / 100;
                float philHealthRate = Float.parseFloat(philHealthField.getText()) / 100;
                float pagIbigRate = Float.parseFloat(pagIbigField.getText()) / 100;
                float withHoldingTax = Float.parseFloat(taxField.getText()) / 100;
                float riceSubsidy = Float.parseFloat(riceField.getText());
                float phoneAllowance = Float.parseFloat(phoneField.getText());
                float clothingAllowance = Float.parseFloat(clothingField.getText());

                PayrollRecord record = new PayrollRecord(
                    baseSalary, 
                    sssRate, philHealthRate, pagIbigRate, withHoldingTax,
                    riceSubsidy, phoneAllowance, clothingAllowance
                );
                
                payrollRecords.put(empId, record);

                // Display detailed results
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

                // Update the view table
                Object[][] newData = payrollRecords.entrySet().stream()
                        .map(entry -> {
                            PayrollRecord r = entry.getValue();
                            return new Object[]{
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
                            };
                        })
                        .toArray(Object[][]::new);
                table.setModel(new javax.swing.table.DefaultTableModel(newData, columns));
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Table selection listener to populate fields when viewing records
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String empId = table.getValueAt(row, 0).toString();
                    PayrollRecord record = payrollRecords.get(empId);
                    
                    empIdField.setText(empId);
                    salaryField.setText(table.getValueAt(row, 1).toString());
                    sssField.setText(String.valueOf(record.sssRate * 100));
                    philHealthField.setText(String.valueOf(record.philHealthRate * 100));
                    pagIbigField.setText(String.valueOf(record.pagIbigRate * 100));
                    taxField.setText(String.valueOf(record.withHoldingTax * 100));
                    riceField.setText(String.valueOf(record.riceSubsidy));
                    phoneField.setText(String.valueOf(record.phoneAllowance));
                    clothingField.setText(String.valueOf(record.clothingAllowance));
                    
                    tabbedPane.setSelectedIndex(0); // Switch to calculation tab
                }
            }
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    static class PayrollRecord {
        private double baseSalary;
        private float sssRate;
        private float philHealthRate;
        private float pagIbigRate;
        private float withHoldingTax;
        private float riceSubsidy;
        private float phoneAllowance;
        private float clothingAllowance;

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

        // Individual deduction calculations
        public double getSSSDeduction() {
            return baseSalary * sssRate;
        }

        public double getPhilHealthDeduction() {
            return baseSalary * philHealthRate;
        }

        public double getPagIbigDeduction() {
            return baseSalary * pagIbigRate;
        }

        public double getTaxDeduction() {
            return baseSalary * withHoldingTax;
        }

        // Total calculations
        public double calculateTotalDeductions() {
            return getSSSDeduction() + getPhilHealthDeduction() + 
                   getPagIbigDeduction() + getTaxDeduction();
        }

        public double calculateTotalAllowances() {
            return riceSubsidy + phoneAllowance + clothingAllowance;
        }

        public double calculateNetSalary() {
            return baseSalary - calculateTotalDeductions() + calculateTotalAllowances();
        }
    }
}