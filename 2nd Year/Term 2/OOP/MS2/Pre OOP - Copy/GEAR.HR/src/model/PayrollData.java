package model;

/**
 * Domain model for payroll data per employee (OOP redesign - GEAR.HR).
 * Holds base salary, deductions, and allowances; used by PayrollProcessor.
 * Amounts are stored at creation time (computed by PayrollUtils in service layer).
 */
public class PayrollData {
    private final double baseSalary;
    private final double sssAmount;
    private final double philHealthAmount;
    private final double pagIbigAmount;
    private final float withholdingTax;
    private final float riceSubsidy;
    private final float phoneAllowance;
    private final float clothingAllowance;

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

    public double getBaseSalary() { return baseSalary; }
    public double getSssAmount() { return sssAmount; }
    public double getPhilHealthAmount() { return philHealthAmount; }
    public double getPagIbigAmount() { return pagIbigAmount; }
    public float getWithholdingTax() { return withholdingTax; }
    public float getRiceSubsidy() { return riceSubsidy; }
    public float getPhoneAllowance() { return phoneAllowance; }
    public float getClothingAllowance() { return clothingAllowance; }

    public double getSSSDeduction() { return sssAmount; }
    public double getPhilHealthDeduction() { return philHealthAmount; }
    public double getPagIbigDeduction() { return pagIbigAmount; }
    public double getTaxDeduction() { return withholdingTax; }
    public double calculateTotalDeductions() {
        return getSSSDeduction() + getPhilHealthDeduction() + getPagIbigDeduction() + getTaxDeduction();
    }
    public double calculateTotalAllowances() {
        return riceSubsidy + phoneAllowance + clothingAllowance;
    }
    public double calculateNetSalary() {
        return baseSalary - calculateTotalDeductions() + calculateTotalAllowances();
    }
}
