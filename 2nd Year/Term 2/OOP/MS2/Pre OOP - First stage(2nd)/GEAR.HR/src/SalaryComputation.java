import model.Employee;

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.*;

/**
 * SalaryComputation class (legacy); prefer PayrollProcessor + PayrollReport for new code.
 * Provides comprehensive salary computation including base salary, allowances, and deductions
 * Note: All information in this program are sample data for demonstration purposes
 */
public class SalaryComputation {
    // Payroll data storage: employeeId -> PayrollData
    private static final Map<String, PayrollData> payrollData = new HashMap<>();
    private static final String PAYROLL_CSV_FILE = "payroll_records.csv";

    // Initialize payroll data from CSV file
    static {
        loadPayrollDataFromCSV();
        if (payrollData.isEmpty()) {
            initializeSamplePayrollData();
            savePayrollDataToCSV();
        }
    }

    /**
     * Computes the salary breakdown for an employee for a given month.
     * Retrieves payroll data, calculates deductions and allowances, and formats the result.
     *
     * @param employee The Employee object
     * @param month The month for which to compute salary
     * @return Formatted string with detailed salary breakdown
     */
    public static String computeSalary(Employee employee, String month) {
        PayrollData data = getPayrollData(employee.getEmployeeNumber());
        
        double baseSalary = data.baseSalary;
        double sssDeduction = data.getSSSDeduction();
        double philHealthDeduction = data.getPhilHealthDeduction();
        double pagIbigDeduction = data.getPagIbigDeduction();
        double taxDeduction = data.getTaxDeduction();
        double totalDeductions = data.calculateTotalDeductions();
        double totalAllowances = data.calculateTotalAllowances();
        double netSalary = data.calculateNetSalary();

        StringBuilder result = new StringBuilder();
        result.append("SALARY COMPUTATION FOR ").append(month.toUpperCase()).append("\n");
        result.append("=====================================\n\n");
        result.append("Employee: ").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("\n");
        result.append("Employee ID: ").append(employee.getEmployeeNumber()).append("\n");
        result.append("Position: ").append(employee.getPosition()).append("\n\n");
        
        result.append("GROSS SALARY:\n");
        result.append("Base Salary: ₱").append(String.format("%,.2f", baseSalary)).append("\n\n");
        
        result.append("DEDUCTIONS:\n");
        result.append("SSS: ₱").append(String.format("%,.2f", sssDeduction)).append("\n");
        result.append("PhilHealth: ₱").append(String.format("%,.2f", philHealthDeduction)).append("\n");
        result.append("Pag-IBIG: ₱").append(String.format("%,.2f", pagIbigDeduction)).append("\n");
        result.append("Withholding Tax: ₱").append(String.format("%,.2f", taxDeduction)).append("\n");
        result.append("Total Deductions: ₱").append(String.format("%,.2f", totalDeductions)).append("\n\n");
        
        result.append("ALLOWANCES:\n");
        result.append("Rice Subsidy: ₱").append(String.format("%,.2f", data.getRiceSubsidy())).append("\n");
        result.append("Phone Allowance: ₱").append(String.format("%,.2f", data.getPhoneAllowance())).append("\n");
        result.append("Clothing Allowance: ₱").append(String.format("%,.2f", data.getClothingAllowance())).append("\n");
        result.append("Total Allowances: ₱").append(String.format("%,.2f", totalAllowances)).append("\n\n");
        
        result.append("NET SALARY: ₱").append(String.format("%,.2f", netSalary)).append("\n");
        
        return result.toString();
    }

    /**
     * Loads payroll data from the payroll_records.csv file into memory.
     * Ensures that all payroll data is loaded into the payrollData map.
     * Handles missing or malformed files gracefully.
     */
    private static void loadPayrollDataFromCSV() {
        if (!Files.exists(Paths.get(PAYROLL_CSV_FILE))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PAYROLL_CSV_FILE))) {
            reader.readLine(); // Skip header line

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) {
                    String employeeId = data[0];
                    double baseSalary = Double.parseDouble(data[1]);
                    double sssAmount = Double.parseDouble(data[2]);
                    double philHealthAmount = Double.parseDouble(data[3]);
                    double pagIbigAmount = Double.parseDouble(data[4]);
                    float withholdingTax = Float.parseFloat(data[5]);
                    float riceSubsidy = Float.parseFloat(data[6]);
                    float phoneAllowance = Float.parseFloat(data[7]);
                    float clothingAllowance = Float.parseFloat(data[8]);

                    payrollData.put(employeeId, new PayrollData(
                        baseSalary, sssAmount, philHealthAmount, pagIbigAmount, withholdingTax,
                        riceSubsidy, phoneAllowance, clothingAllowance
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payroll data: " + e.getMessage());
        }
    }

    /**
     * Saves all payroll data to the payroll_records.csv file.
     * Writes the current state of payrollData map to disk.
     * Handles file I/O errors gracefully.
     */
    private static void savePayrollDataToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYROLL_CSV_FILE))) {
            // Write CSV header
            writer.println("EmployeeID,BaseSalary,SSSAmount,PhilHealthAmount,PagIBIGAmount,WithholdingTax,RiceSubsidy,PhoneAllowance,ClothingAllowance");
            
            // Write each payroll record
            for (Map.Entry<String, PayrollData> entry : payrollData.entrySet()) {
                PayrollData data = entry.getValue();
                double computedTax = PayrollUtils.calculateWithholdingTax(
                    data.baseSalary, data.riceSubsidy, data.phoneAllowance, data.clothingAllowance
                );
                writer.println(String.join(",",
                    entry.getKey(),
                    String.valueOf(data.baseSalary),
                    String.valueOf(data.sssAmount),
                    String.valueOf(data.philHealthAmount),
                    String.valueOf(data.pagIbigAmount),
                    String.format("%.2f", computedTax),
                    String.valueOf(data.riceSubsidy),
                    String.valueOf(data.phoneAllowance),
                    String.valueOf(data.clothingAllowance)
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving payroll data: " + e.getMessage());
        }
    }

    /**
     * Initializes sample payroll data for demonstration purposes.
     */
    private static void initializeSamplePayrollData() {
        double base1 = 35000.0;
        double base2 = 60000.0;
        double base3 = 45000.0;
        float rice1 = 1500.0f, phone1 = 1000.0f, cloth1 = 800.0f;
        float rice2 = 1500.0f, phone2 = 800.0f, cloth2 = 600.0f;
        float rice3 = 1500.0f, phone3 = 900.0f, cloth3 = 700.0f;
        payrollData.put("1001", new PayrollData(
            base1,
            PayrollUtils.calculateSSSAmount(base1),
            PayrollUtils.calculatePhilHealthAmount(base1),
            PayrollUtils.calculatePagIbigAmount(base1),
            (float) PayrollUtils.calculateWithholdingTax(base1, rice1, phone1, cloth1),
            rice1, phone1, cloth1
        ));
        payrollData.put("1002", new PayrollData(
            base2,
            PayrollUtils.calculateSSSAmount(base2),
            PayrollUtils.calculatePhilHealthAmount(base2),
            PayrollUtils.calculatePagIbigAmount(base2),
            (float) PayrollUtils.calculateWithholdingTax(base2, rice2, phone2, cloth2),
            rice2, phone2, cloth2
        ));
        payrollData.put("1003", new PayrollData(
            base3,
            PayrollUtils.calculateSSSAmount(base3),
            PayrollUtils.calculatePhilHealthAmount(base3),
            PayrollUtils.calculatePagIbigAmount(base3),
            (float) PayrollUtils.calculateWithholdingTax(base3, rice3, phone3, cloth3),
            rice3, phone3, cloth3
        ));
    }

    /**
     * Returns default payroll data for new employees.
     *
     * @return PayrollData object with default values
     */
    private static PayrollData getDefaultPayrollData() {
        double baseSalary = 30000.0;
        float rice = 1500.0f, phone = 1000.0f, cloth = 800.0f;
        return new PayrollData(
            baseSalary,
            PayrollUtils.calculateSSSAmount(baseSalary),
            PayrollUtils.calculatePhilHealthAmount(baseSalary),
            PayrollUtils.calculatePagIbigAmount(baseSalary),
            (float) PayrollUtils.calculateWithholdingTax(baseSalary, rice, phone, cloth),
            rice, phone, cloth
        );
    }

    /**
     * Updates payroll data for an employee and saves to CSV.
     *
     * @param employeeId The employee ID
     * @param data The new payroll data
     */
    public static void updatePayrollData(String employeeId, PayrollData data) {
        payrollData.put(employeeId, data);
        savePayrollDataToCSV();
    }

    /**
     * Gets payroll data for an employee, or returns default if not found.
     *
     * @param employeeId The employee ID
     * @return The PayrollData object for the employee
     */
    public static PayrollData getPayrollData(String employeeId) {
        return payrollData.getOrDefault(employeeId, getDefaultPayrollData());
    }

    /**
     * Removes payroll data for an employee and saves to CSV.
     *
     * @param employeeId The employee ID to remove
     */
    public static void removePayrollData(String employeeId) {
        payrollData.remove(employeeId);
        savePayrollDataToCSV();
    }

    /**
     * Inner class to hold payroll data for an employee.
     * Contains all salary-related information including rates and allowances.
     */
    static class PayrollData {
        private final double baseSalary;
        private final double sssAmount;
        private final double philHealthAmount;
        private final double pagIbigAmount;
        private final float withholdingTax;
        private final float riceSubsidy;
        private final float phoneAllowance;
        private final float clothingAllowance;

        /**
         * Creates a new PayrollData object with all salary components.
         *
         * @param baseSalary The base monthly salary
         * @param sssAmount SSS contribution amount (computed)
         * @param philHealthAmount PhilHealth contribution amount (computed)
         * @param pagIbigAmount Pag-IBIG contribution amount (computed)
         * @param withholdingTax Withholding tax amount
         * @param riceSubsidy Monthly rice subsidy amount
         * @param phoneAllowance Monthly phone allowance amount
         * @param clothingAllowance Monthly clothing allowance amount
         */
        public PayrollData(double baseSalary, 
                          double sssAmount, double philHealthAmount, double pagIbigAmount, float withholdingTax,
                          float riceSubsidy, float phoneAllowance, float clothingAllowance) {
            this.baseSalary = baseSalary;
            this.sssAmount = sssAmount;
            this.philHealthAmount = philHealthAmount;
            this.pagIbigAmount = pagIbigAmount;
            this.withholdingTax = withholdingTax;
            this.riceSubsidy = riceSubsidy;
            this.phoneAllowance = phoneAllowance;
            this.clothingAllowance = clothingAllowance;
        }

        /**
         * Gets the SSS deduction amount.
         *
         * @return The SSS deduction
         */
        public double getSSSDeduction() { 
            return sssAmount;
        }

        /**
         * Gets the PhilHealth deduction amount.
         *
         * @return The PhilHealth deduction
         */
        public double getPhilHealthDeduction() { 
            return philHealthAmount;
        }

        /**
         * Gets the Pag-IBIG deduction amount.
         *
         * @return The Pag-IBIG deduction
         */
        public double getPagIbigDeduction() { 
            return pagIbigAmount;
        }

        /**
         * Gets the withholding tax deduction amount.
         *
         * @return The withholding tax deduction
         */
        public double getTaxDeduction() {
            return PayrollUtils.calculateWithholdingTax(baseSalary, riceSubsidy, phoneAllowance, clothingAllowance);
        }

        /**
         * Gets the rice subsidy allowance.
         *
         * @return The rice subsidy
         */
        public double getRiceSubsidy() { return riceSubsidy; }

        /**
         * Gets the phone allowance.
         *
         * @return The phone allowance
         */
        public double getPhoneAllowance() { return phoneAllowance; }

        /**
         * Gets the clothing allowance.
         *
         * @return The clothing allowance
         */
        public double getClothingAllowance() { return clothingAllowance; }

        /**
         * Calculates the total deductions from the base salary.
         *
         * @return Total amount of all deductions
         */
        public double calculateTotalDeductions() {
            return getSSSDeduction() + getPhilHealthDeduction() + 
                   getPagIbigDeduction() + getTaxDeduction();
        }

        /**
         * Calculates the total allowances.
         *
         * @return Total amount of all allowances
         */
        public double calculateTotalAllowances() {
            return riceSubsidy + phoneAllowance + clothingAllowance;
        }

        /**
         * Calculates the net salary after deductions and allowances.
         *
         * @return Net salary amount
         */
        public double calculateNetSalary() {
            return baseSalary - calculateTotalDeductions() + calculateTotalAllowances();
        }
    }

    /**
     * Calculates SSS amount based on salary brackets.
     *
     * @param baseSalary The base salary
     * @return The SSS contribution amount
     */
}