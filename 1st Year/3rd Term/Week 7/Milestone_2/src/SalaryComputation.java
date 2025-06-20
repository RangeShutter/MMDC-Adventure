import java.util.HashMap;
import java.util.Map;

public class SalaryComputation {
    private static Map<String, Double> baseSalaries = new HashMap<>();
    private static Map<String, Double> allowanceRates = new HashMap<>();

    static {
        // Initialize with sample data
        baseSalaries.put("Developer", 35000.0);
        baseSalaries.put("Manager", 60000.0);
        baseSalaries.put("HR", 40000.0);
        
        allowanceRates.put("Rice", 1500.0);
        allowanceRates.put("Phone", 1000.0);
        allowanceRates.put("Clothing", 800.0);
    }

    public static String computeSalary(EmployeeProfile.Employee employee, String month) {
        double baseSalary = baseSalaries.getOrDefault(employee.getPosition(), 30000.0);
        double allowances = allowanceRates.values().stream().mapToDouble(Double::doubleValue).sum();
        double deductions = calculateDeductions(baseSalary);
        double netSalary = baseSalary - deductions + allowances;

        return String.format(
            "Salary Computation for %s %s (%s)\n" +
            "Month: %s\n\n" +
            "Base Salary: ₱%,.2f\n" +
            "Allowances: ₱%,.2f\n" +
            "Deductions: ₱%,.2f\n" +
            "-------------------------\n" +
            "Net Salary: ₱%,.2f",
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmployeeNumber(),
            month,
            baseSalary,
            allowances,
            deductions,
            netSalary
        );
    }

    private static double calculateDeductions(double baseSalary) {
        // Sample deduction calculation (SSS, PhilHealth, Tax)
        return (baseSalary * 0.045) + // SSS
               (baseSalary * 0.04) +  // PhilHealth
               (baseSalary * 0.15);   // Tax
    }
}