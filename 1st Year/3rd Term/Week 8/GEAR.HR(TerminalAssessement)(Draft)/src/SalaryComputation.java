import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.*;

/**
 * SalaryComputation class handles employee salary calculations and payroll data management
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
     * Computes and formats the salary details for an employee
     * Calculates gross salary, deductions, allowances, and net salary
     * @param employee The employee object containing personal details
     * @param month The month for which salary is being computed
     * @return Formatted string with detailed salary breakdown
     */
    public static String computeSalary(EmployeeProfile.Employee employee, String month) {
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
     * Loads payroll data from CSV file into memory
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
                if (data.length >= 8) {
                    String employeeId = data[0];
                    double baseSalary = Double.parseDouble(data[1]);
                    float sssRate = Float.parseFloat(data[2]);
                    float philHealthRate = Float.parseFloat(data[3]);
                    float pagIbigRate = Float.parseFloat(data[4]);
                    float withholdingTax = Float.parseFloat(data[5]);
                    float riceSubsidy = Float.parseFloat(data[6]);
                    float phoneAllowance = Float.parseFloat(data[7]);
                    float clothingAllowance = Float.parseFloat(data[8]);

                    payrollData.put(employeeId, new PayrollData(
                        baseSalary, sssRate, philHealthRate, pagIbigRate, withholdingTax,
                        riceSubsidy, phoneAllowance, clothingAllowance
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payroll data: " + e.getMessage());
        }
    }

    /**
     * Saves payroll data to CSV file
     */
    private static void savePayrollDataToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYROLL_CSV_FILE))) {
            // Write CSV header
            writer.println("EmployeeID,BaseSalary,SSSRate,PhilHealthRate,PagIBIGRate,WithholdingTax,RiceSubsidy,PhoneAllowance,ClothingAllowance");
            
            // Write each payroll record
            for (Map.Entry<String, PayrollData> entry : payrollData.entrySet()) {
                PayrollData data = entry.getValue();
                writer.println(String.join(",",
                    entry.getKey(),
                    String.valueOf(data.baseSalary),
                    String.valueOf(data.sssRate),
                    String.valueOf(data.philHealthRate),
                    String.valueOf(data.pagIbigRate),
                    String.valueOf(data.withholdingTax),
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
     * Initializes sample payroll data for demonstration purposes
     */
    private static void initializeSamplePayrollData() {
        payrollData.put("1001", new PayrollData(35000.0, 0.045f, 0.04f, 0.02f, 0.15f, 1500.0f, 1000.0f, 800.0f));
        payrollData.put("1002", new PayrollData(60000.0, 0.045f, 0.04f, 0.02f, 0.12f, 1500.0f, 800.0f, 600.0f));
        payrollData.put("1003", new PayrollData(45000.0, 0.045f, 0.04f, 0.02f, 0.13f, 1500.0f, 900.0f, 700.0f));
    }

    /**
     * Returns default payroll data for new employees
     */
    private static PayrollData getDefaultPayrollData() {
        return new PayrollData(30000.0, 0.045f, 0.04f, 0.02f, 0.15f, 1500.0f, 1000.0f, 800.0f);
    }

    /**
     * Updates payroll data for an employee
     * @param employeeId The employee ID
     * @param data The new payroll data
     */
    public static void updatePayrollData(String employeeId, PayrollData data) {
        payrollData.put(employeeId, data);
        savePayrollDataToCSV();
    }

    /**
     * Gets payroll data for an employee
     * @param employeeId The employee ID
     * @return The payroll data or default if not found
     */
    public static PayrollData getPayrollData(String employeeId) {
        return payrollData.getOrDefault(employeeId, getDefaultPayrollData());
    }

    /**
     * Removes payroll data for an employee
     * Called when an employee is deleted from the system
     * @param employeeId The employee ID to remove
     */
    public static void removePayrollData(String employeeId) {
        payrollData.remove(employeeId);
        savePayrollDataToCSV();
    }

    /**
     * Inner class to hold payroll data for an employee
     * Contains all salary-related information including rates and allowances
     */
    static class PayrollData {
        private final double baseSalary;
        private final float sssRate;
        private final float philHealthRate;
        private final float pagIbigRate;
        private final float withholdingTax;
        private final float riceSubsidy;
        private final float phoneAllowance;
        private final float clothingAllowance;

        /**
         * Creates a new PayrollData object with all salary components
         * @param baseSalary The base monthly salary
         * @param sssRate SSS contribution rate
         * @param philHealthRate PhilHealth contribution rate
         * @param pagIbigRate Pag-IBIG contribution rate
         * @param withholdingTax Withholding tax rate
         * @param riceSubsidy Monthly rice subsidy amount
         * @param phoneAllowance Monthly phone allowance amount
         * @param clothingAllowance Monthly clothing allowance amount
         */
        public PayrollData(double baseSalary, 
                          float sssRate, float philHealthRate, float pagIbigRate, float withholdingTax,
                          float riceSubsidy, float phoneAllowance, float clothingAllowance) {
            this.baseSalary = baseSalary;
            this.sssRate = sssRate;
            this.philHealthRate = philHealthRate;
            this.pagIbigRate = pagIbigRate;
            this.withholdingTax = withholdingTax;
            this.riceSubsidy = riceSubsidy;
            this.phoneAllowance = phoneAllowance;
            this.clothingAllowance = clothingAllowance;
        }

        // Getter methods for deductions
        public double getSSSDeduction() { return baseSalary * sssRate; }
        public double getPhilHealthDeduction() { return baseSalary * philHealthRate; }
        public double getPagIbigDeduction() { return baseSalary * pagIbigRate; }
        public double getTaxDeduction() { return baseSalary * withholdingTax; }

        // Getter methods for allowances
        public double getRiceSubsidy() { return riceSubsidy; }
        public double getPhoneAllowance() { return phoneAllowance; }
        public double getClothingAllowance() { return clothingAllowance; }

        /**
         * Calculates the total deductions from the base salary
         * @return Total amount of all deductions
         */
        public double calculateTotalDeductions() {
            return getSSSDeduction() + getPhilHealthDeduction() + 
                   getPagIbigDeduction() + getTaxDeduction();
        }

        /**
         * Calculates the total allowances
         * @return Total amount of all allowances
         */
        public double calculateTotalAllowances() {
            return riceSubsidy + phoneAllowance + clothingAllowance;
        }

        /**
         * Calculates the net salary after deductions and allowances
         * @return Net salary amount
         */
        public double calculateNetSalary() {
            return baseSalary - calculateTotalDeductions() + calculateTotalAllowances();
        }
    }
}