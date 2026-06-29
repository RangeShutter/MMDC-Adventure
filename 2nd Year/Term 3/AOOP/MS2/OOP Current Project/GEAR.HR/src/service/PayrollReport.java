package service;

import model.PayrollResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Formats payroll computation results for display (OOP redesign - GEAR.HR).
 * [ABSTRACTION] Separates presentation text from payroll computation ({@link PayrollProcessor}).
 */
public class PayrollReport {

    /** [ABSTRACTION] Static helper builds human-readable report from a {@link PayrollResult}. */
    public static String format(PayrollResult r) {
        if (r == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("SALARY COMPUTATION FOR ").append(r.getMonth().toUpperCase()).append("\n");
        sb.append("=====================================\n\n");
        sb.append("Employee: ").append(r.getEmployeeName()).append("\n");
        sb.append("Employee ID: ").append(r.getEmployeeId()).append("\n");
        sb.append("Position: ").append(r.getPosition()).append("\n\n");
        sb.append("GROSS SALARY:\n");
        sb.append("Base Salary: ₱").append(String.format("%,.2f", r.getBaseSalary())).append("\n");
        sb.append("Hourly Rate: ₱").append(String.format("%,.2f", r.getHourlyRate())).append("\n");
        sb.append("Worked Hours: ").append(String.format("%,.2f", r.getWorkedHours())).append("\n");
        sb.append("Gross Pay (Hourly x Hours): ₱").append(String.format("%,.2f", r.getGrossPay())).append("\n\n");
        sb.append("DEDUCTIONS:\n");
        sb.append("SSS: ₱").append(String.format("%,.2f", r.getSssDeduction())).append("\n");
        sb.append("PhilHealth: ₱").append(String.format("%,.2f", r.getPhilHealthDeduction())).append("\n");
        sb.append("Pag-IBIG: ₱").append(String.format("%,.2f", r.getPagIbigDeduction())).append("\n");
        sb.append("Withholding Tax: ₱").append(String.format("%,.2f", r.getTaxDeduction())).append("\n");
        sb.append("Total Deductions: ₱").append(String.format("%,.2f", r.getTotalDeductions())).append("\n\n");
        sb.append("ALLOWANCES:\n");
        sb.append("Rice Subsidy: ₱").append(String.format("%,.2f", r.getRiceSubsidy())).append("\n");
        sb.append("Phone Allowance: ₱").append(String.format("%,.2f", r.getPhoneAllowance())).append("\n");
        sb.append("Clothing Allowance: ₱").append(String.format("%,.2f", r.getClothingAllowance())).append("\n");
        sb.append("Total Allowances: ₱").append(String.format("%,.2f", r.getTotalAllowances())).append("\n\n");
        sb.append("NET SALARY: ₱").append(String.format("%,.2f", r.getNetSalary())).append("\n");
        return sb.toString();
    }

    /**
     * [ABSTRACTION] Builds a department-specific payroll summary report for a given month.
     * Lists one block per employee in the department plus grand totals for that department only.
     *
     * @param results    computed payroll results for the selected department (null/empty entries are skipped)
     * @param month      the payroll month the summary covers
     * @param department the department/role group label the report is scoped to
     * @return formatted department payroll summary text
     */
    public static String formatSummary(List<PayrollResult> results, String month, String department) {
        StringBuilder sb = new StringBuilder();
        String generatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String monthLabel = month != null ? month.toUpperCase() : "";
        String departmentLabel = department != null && !department.trim().isEmpty()
            ? department.trim().toUpperCase() : "ALL DEPARTMENTS";

        sb.append("PAYROLL SUMMARY FOR ").append(monthLabel).append("\n");
        sb.append("Department: ").append(departmentLabel).append("\n");
        sb.append("=====================================\n");
        sb.append("Generated: ").append(generatedAt).append("\n\n");

        int employeeCount = 0;
        double totalGross = 0.0;
        double totalDeductions = 0.0;
        double totalAllowances = 0.0;
        double totalNet = 0.0;

        if (results != null) {
            for (PayrollResult r : results) {
                if (r == null) continue;
                employeeCount++;
                totalGross += r.getGrossPay();
                totalDeductions += r.getTotalDeductions();
                totalAllowances += r.getTotalAllowances();
                totalNet += r.getNetSalary();

                sb.append("Employee ID: ").append(r.getEmployeeId()).append("\n");
                sb.append("Name: ").append(r.getEmployeeName()).append("\n");
                sb.append("Position: ").append(r.getPosition()).append("\n");
                sb.append("  Worked Hours: ").append(String.format("%,.2f", r.getWorkedHours())).append("\n");
                sb.append("  Gross Pay: ₱").append(String.format("%,.2f", r.getGrossPay())).append("\n");
                sb.append("  Total Deductions: ₱").append(String.format("%,.2f", r.getTotalDeductions())).append("\n");
                sb.append("  Total Allowances: ₱").append(String.format("%,.2f", r.getTotalAllowances())).append("\n");
                sb.append("  Net Salary: ₱").append(String.format("%,.2f", r.getNetSalary())).append("\n");
                sb.append("-------------------------------------\n");
            }
        }

        if (employeeCount == 0) {
            sb.append("No employee payroll records available for this department and month.\n");
            sb.append("-------------------------------------\n");
        }

        sb.append("\nDEPARTMENT TOTALS\n");
        sb.append("=====================================\n");
        sb.append("Employees: ").append(employeeCount).append("\n");
        sb.append("Total Gross Pay: ₱").append(String.format("%,.2f", totalGross)).append("\n");
        sb.append("Total Deductions: ₱").append(String.format("%,.2f", totalDeductions)).append("\n");
        sb.append("Total Allowances: ₱").append(String.format("%,.2f", totalAllowances)).append("\n");
        sb.append("Total Net Salary: ₱").append(String.format("%,.2f", totalNet)).append("\n");
        return sb.toString();
    }
}
