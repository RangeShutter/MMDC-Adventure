package model;

/**
 * DTO for payroll computation result (OOP redesign - GEAR.HR).
 * Carries computed breakdown for PayrollReport to format.
 */
public class PayrollResult {
    private final String employeeId;
    private final String employeeName;
    private final String position;
    private final String month;
    private final double baseSalary;
    private final double sssDeduction;
    private final double philHealthDeduction;
    private final double pagIbigDeduction;
    private final double taxDeduction;
    private final double totalDeductions;
    private final double riceSubsidy;
    private final double phoneAllowance;
    private final double clothingAllowance;
    private final double totalAllowances;
    private final double netSalary;

    public PayrollResult(String employeeId, String employeeName, String position, String month,
                         double baseSalary, double sssDeduction, double philHealthDeduction, double pagIbigDeduction,
                         double taxDeduction, double totalDeductions,
                         double riceSubsidy, double phoneAllowance, double clothingAllowance, double totalAllowances,
                         double netSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName != null ? employeeName : "";
        this.position = position != null ? position : "";
        this.month = month != null ? month : "";
        this.baseSalary = baseSalary;
        this.sssDeduction = sssDeduction;
        this.philHealthDeduction = philHealthDeduction;
        this.pagIbigDeduction = pagIbigDeduction;
        this.taxDeduction = taxDeduction;
        this.totalDeductions = totalDeductions;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.totalAllowances = totalAllowances;
        this.netSalary = netSalary;
    }

    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
    public String getMonth() { return month; }
    public double getBaseSalary() { return baseSalary; }
    public double getSssDeduction() { return sssDeduction; }
    public double getPhilHealthDeduction() { return philHealthDeduction; }
    public double getPagIbigDeduction() { return pagIbigDeduction; }
    public double getTaxDeduction() { return taxDeduction; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getTotalAllowances() { return totalAllowances; }
    public double getNetSalary() { return netSalary; }
}
