import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.*;

/**
 * Handles salary computation for employees including base salary, allowances, and deductions
 * Note: Contains sample data for demonstration purposes
 */
public class SalaryComputation {
    // Maps employee IDs to their payroll records
    private static Map<String, PayrollData> payrollData = new HashMap<>();
    private static final String PAYROLL_CSV = "payroll_records.csv";

    // Static initializer - loads payroll data from CSV
    static {
        loadPayrollDataFromCSV();
        if (payrollData.isEmpty()) {
            initializeSamplePayrollData();
            savePayrollDataToCSV();
        }
    }

    /**
     * Computes and formats the salary details for an employee
     * @param employee The employee object containing personal details
     * @param month The month for which salary is being computed
     * @return Formatted string with detailed salary breakdown
     */
    public static String computeSalary(EmployeeProfile.Employee employee, String month) {
        // Get employee ID (use employee number as ID)
        String employeeId = employee.getEmployeeNumber();
        
        // Get payroll data for this employee, use default if not found
        PayrollData data = payrollData.getOrDefault(employeeId, getDefaultPayrollData());
        
        // Calculate total allowances
        double totalAllowances = data.riceSubsidy + data.phoneAllowance + data.clothingAllowance;
        
        // Calculate total deductions
        double sssDeduction = data.baseSalary * data.sssRate;
        double philHealthDeduction = data.baseSalary * data.philHealthRate;
        double pagIbigDeduction = data.baseSalary * data.pagIbigRate;
        double taxDeduction = data.baseSalary * data.withholdingTax;
        double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + taxDeduction;
        
        // Calculate net salary
        double netSalary = data.baseSalary - totalDeductions + totalAllowances;

        // Format and return detailed salary computation
        return String.format(
            "Salary Computation for %s %s (%s)\n" +
            "Month: %s\n\n" +
            "Base Salary: ₱%,.2f\n\n" +
            "Allowances:\n" +
            "  Rice Subsidy: ₱%,.2f\n" +
            "  Phone Allowance: ₱%,.2f\n" +
            "  Clothing Allowance: ₱%,.2f\n" +
            "  Total Allowances: ₱%,.2f\n\n" +
            "Deductions:\n" +
            "  SSS (%.1f%%): ₱%,.2f\n" +
            "  PhilHealth (%.1f%%): ₱%,.2f\n" +
            "  Pag-IBIG (%.1f%%): ₱%,.2f\n" +
            "  Withholding Tax (%.1f%%): ₱%,.2f\n" +
            "  Total Deductions: ₱%,.2f\n\n" +
            "-------------------------\n" +
            "Net Salary: ₱%,.2f",
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmployeeNumber(),
            month,
            data.baseSalary,
            data.riceSubsidy,
            data.phoneAllowance,
            data.clothingAllowance,
            totalAllowances,
            data.sssRate * 100,
            sssDeduction,
            data.philHealthRate * 100,
            philHealthDeduction,
            data.pagIbigRate * 100,
            pagIbigDeduction,
            data.withholdingTax * 100,
            taxDeduction,
            totalDeductions,
            netSalary
        );
    }

    /**
     * Loads payroll data from CSV file
     */
    private static void loadPayrollDataFromCSV() {
        if (!Files.exists(Paths.get(PAYROLL_CSV))) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PAYROLL_CSV))) {
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYROLL_CSV))) {
            // Write CSV header
            writer.println("EmployeeID,BaseSalary,SSSRate,PhilHealthRate,PagIBIGRate,WithholdingTax,RiceSubsidy,PhoneAllowance,ClothingAllowance");
            
            // Write each record
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
     * Initializes sample payroll data
     */
    private static void initializeSamplePayrollData() {
        payrollData.put("1001", new PayrollData(40000.0, 0.045f, 0.04f, 0.02f, 0.15f, 1500.0f, 1000.0f, 800.0f));
        payrollData.put("1002", new PayrollData(60000.0, 0.045f, 0.04f, 0.02f, 0.12f, 1500.0f, 800.0f, 600.0f));
        payrollData.put("1003", new PayrollData(35000.0, 0.045f, 0.04f, 0.02f, 0.12f, 1500.0f, 800.0f, 600.0f));
        payrollData.put("1004", new PayrollData(40000.0, 0.045f, 0.04f, 0.02f, 0.15f, 1500.0f, 1000.0f, 800.0f));
    }

    /**
     * Returns default payroll data for employees not found in the system
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
     * @param employeeId The employee ID to remove
     */
    public static void removePayrollData(String employeeId) {
        payrollData.remove(employeeId);
        savePayrollDataToCSV();
    }

    /**
     * Inner class to hold payroll data
     */
    public static class PayrollData {
        public double baseSalary;
        public float sssRate;
        public float philHealthRate;
        public float pagIbigRate;
        public float withholdingTax;
        public float riceSubsidy;
        public float phoneAllowance;
        public float clothingAllowance;

        public PayrollData(double baseSalary, float sssRate, float philHealthRate, float pagIbigRate,
                          float withholdingTax, float riceSubsidy, float phoneAllowance, float clothingAllowance) {
            this.baseSalary = baseSalary;
            this.sssRate = sssRate;
            this.philHealthRate = philHealthRate;
            this.pagIbigRate = pagIbigRate;
            this.withholdingTax = withholdingTax;
            this.riceSubsidy = riceSubsidy;
            this.phoneAllowance = phoneAllowance;
            this.clothingAllowance = clothingAllowance;
        }
    }
}