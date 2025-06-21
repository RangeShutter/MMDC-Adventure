import java.util.HashMap;
import java.util.Map;

/**
 * Handles salary computation for employees including base salary, allowances, and deductions
 * Note: Contains sample data for demonstration purposes
 */
public class SalaryComputation {
    // Maps positions to their respective base salaries
    private static Map<String, Double> baseSalaries = new HashMap<>();
    // Maps allowance types to their respective amounts
    private static Map<String, Double> allowanceRates = new HashMap<>();

    // Static initializer - loads sample salary and allowance data
    static {
        // Initialize base salaries for different positions
        baseSalaries.put("Developer", 35000.0);
        baseSalaries.put("Manager", 60000.0);
        baseSalaries.put("HR", 40000.0);
        
        // Initialize standard allowance amounts
        allowanceRates.put("Rice", 1500.0);      // Monthly rice subsidy
        allowanceRates.put("Phone", 1000.0);     // Monthly phone allowance
        allowanceRates.put("Clothing", 800.0);    // Monthly clothing allowance
    }

    /**
     * Computes and formats the salary details for an employee
     * @param employee The employee object containing personal details
     * @param month The month for which salary is being computed
     * @return Formatted string with detailed salary breakdown
     */
    public static String computeSalary(EmployeeProfile.Employee employee, String month) {
        // Get base salary based on position, default to 30000 if position not found
        double baseSalary = baseSalaries.getOrDefault(employee.getPosition(), 30000.0);
        
        // Calculate total allowances (sum of all allowance types)
        double allowances = allowanceRates.values().stream()
                                       .mapToDouble(Double::doubleValue)
                                       .sum();
        
        // Calculate total deductions (SSS, PhilHealth, Tax)
        double deductions = calculateDeductions(baseSalary);
        
        // Calculate net salary (base - deductions + allowances)
        double netSalary = baseSalary - deductions + allowances;

        // Format and return salary computation details
        return String.format(
            "Salary Computation for %s %s (%s)\n" +
            "Month: %s\n\n" +
            "Base Salary: ₱%,.2f\n" +
            "Allowances: ₱%,.2f\n" +
            "Deductions: ₱%,.2f\n" +
            "-------------------------\n" +
            "Net Salary: ₱%,.2f",
            employee.getFirstName(),      // Employee first name
            employee.getLastName(),       // Employee last name
            employee.getEmployeeNumber(), // Employee ID
            month,                        // Computation month
            baseSalary,                   // Base salary amount
            allowances,                   // Total allowances
            deductions,                   // Total deductions
            netSalary                     // Final net salary
        );
    }

    /**
     * Calculates total deductions from base salary
     * @param baseSalary The employee's base salary
     * @return Total deductions amount
     */
    private static double calculateDeductions(double baseSalary) {
        // Sample deduction calculation:
        return (baseSalary * 0.045) + // SSS contribution (4.5%)
               (baseSalary * 0.04) +  // PhilHealth contribution (4%)
               (baseSalary * 0.15);   // Withholding tax (15%)
    }
}