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
    private static final String PAYROLL_CSV_FILE = "GEAR.HR/payroll_records.csv";

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
                if (data.length >= 8) {
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
                double computedTax = calculateWithholdingTax(data.baseSalary, data.riceSubsidy, data.phoneAllowance, data.clothingAllowance);
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
        payrollData.put("1001", new PayrollData(base1, calculateSSSAmount(base1), calculatePhilHealthAmount(base1), calculatePagIbigAmount(base1), (float)calculateWithholdingTax(base1, rice1, phone1, cloth1), rice1, phone1, cloth1));
        payrollData.put("1002", new PayrollData(base2, calculateSSSAmount(base2), calculatePhilHealthAmount(base2), calculatePagIbigAmount(base2), (float)calculateWithholdingTax(base2, rice2, phone2, cloth2), rice2, phone2, cloth2));
        payrollData.put("1003", new PayrollData(base3, calculateSSSAmount(base3), calculatePhilHealthAmount(base3), calculatePagIbigAmount(base3), (float)calculateWithholdingTax(base3, rice3, phone3, cloth3), rice3, phone3, cloth3));
    }

    /**
     * Returns default payroll data for new employees.
     *
     * @return PayrollData object with default values
     */
    private static PayrollData getDefaultPayrollData() {
        double baseSalary = 30000.0;
        float rice = 1500.0f, phone = 1000.0f, cloth = 800.0f;
        return new PayrollData(baseSalary, calculateSSSAmount(baseSalary), calculatePhilHealthAmount(baseSalary), calculatePagIbigAmount(baseSalary), (float)calculateWithholdingTax(baseSalary, rice, phone, cloth), rice, phone, cloth);
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
            // Use the same formula as EmployeeProfile.java
            return calculateWithholdingTax(baseSalary, riceSubsidy, phoneAllowance, clothingAllowance);
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
    private static double calculateSSSAmount(double baseSalary) {
        double[][] SSS_BRACKETS = {
            {0, 5249.99, 250},
            {5250, 5749.99, 275},
            {5750, 6249.99, 300},
            {6250, 6749.99, 325},
            {6750, 7249.99, 350},
            {7250, 7749.99, 375},
            {7750, 8249.99, 400},
            {8250, 8749.99, 425},
            {8750, 9249.99, 450},
            {9250, 9749.99, 475},
            {9750, 10249.99, 500},
            {10250, 10749.99, 525},
            {10750, 11249.99, 550},
            {11250, 11749.99, 575},
            {11750, 12249.99, 600},
            {12250, 12749.99, 625},
            {12750, 13249.99, 650},
            {13250, 13749.99, 675},
            {13750, 14249.99, 700},
            {14250, 14749.99, 725},
            {14750, 15249.99, 750},
            {15250, 15749.99, 775},
            {15750, 16249.99, 800},
            {16250, 16749.99, 825},
            {16750, 17249.99, 850},
            {17250, 17749.99, 875},
            {17750, 18249.99, 900},
            {18250, 18749.99, 925},
            {18750, 19249.99, 950},
            {19250, 19749.99, 975},
            {19750, 20249.99, 1000},
            {20250, 20749.99, 1025},
            {20750, 21249.99, 1050},
            {21250, 21749.99, 1075},
            {21750, 22249.99, 1100},
            {22250, 22749.99, 1125},
            {22750, 23249.99, 1150},
            {23250, 23749.99, 1175},
            {23750, 24249.99, 1200},
            {24250, 24749.99, 1225},
            {24750, 25249.99, 1250},
            {25250, 25749.99, 1275},
            {25750, 26249.99, 1300},
            {26250, 26749.99, 1325},
            {26750, 27249.99, 1350},
            {27250, 27749.99, 1375},
            {27750, 28249.99, 1400},
            {28250, 28749.99, 1425},
            {28750, 29249.99, 1450},
            {29250, 29749.99, 1475},
            {29750, 30249.99, 1500},
            {30250, 30749.99, 1525},
            {30750, 31249.99, 1550},
            {31250, 31749.99, 1575},
            {31750, 32249.99, 1600},
            {32250, 32749.99, 1625},
            {32750, 33249.99, 1650},
            {33250, 33749.99, 1675},
            {33750, 34249.99, 1700},
            {34250, 34749.99, 1725},
            {34750, 9999999.99, 1750}
        };
        for (double[] bracket : SSS_BRACKETS) {
            if (baseSalary >= bracket[0] && baseSalary <= bracket[1]) {
                return bracket[2];
            }
        }
        return 1750;
    }

    /**
     * Calculates PhilHealth amount with 5% rate, min ₱500, max ₱5000.
     *
     * @param baseSalary The base salary
     * @return The PhilHealth contribution amount
     */
    private static double calculatePhilHealthAmount(double baseSalary) {
        double philHealthAmount = baseSalary * 0.05;
        return Math.max(500.0, Math.min(philHealthAmount, 5000.0));
    }

    /**
     * Calculates Pag-IBIG amount with 2% rate, capped at ₱200.
     *
     * @param baseSalary The base salary
     * @return The Pag-IBIG contribution amount
     */
    private static double calculatePagIbigAmount(double baseSalary) {
        double pagIbigAmount = baseSalary * 0.02;
        return Math.min(pagIbigAmount, 200.0);
    }

    /**
     * Calculates withholding tax based on the latest government table.
     *
     * @param baseSalary The base salary
     * @param riceSubsidy The rice subsidy
     * @param phoneAllowance The phone allowance
     * @param clothingAllowance The clothing allowance
     * @return The computed withholding tax
     */
    private static double calculateWithholdingTax(double baseSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance) {
        double netTaxableComp = baseSalary + phoneAllowance + riceSubsidy + clothingAllowance;
        double subtrahend = 0;
        double percent = 0;
        double addend = 0;

        if (netTaxableComp <= 20833) {
            subtrahend = 0;
            percent = 0;
            addend = 0;
        } else if (netTaxableComp <= 33332) {
            subtrahend = 20833;
            percent = 0.20;
            addend = 0;
        } else if (netTaxableComp <= 66666) {
            subtrahend = 33333;
            percent = 0.25;
            addend = 2500.00;
        } else if (netTaxableComp <= 166666) {
            subtrahend = 66667;
            percent = 0.30;
            addend = 10833.33;
        } else if (netTaxableComp <= 666666) {
            subtrahend = 166667;
            percent = 0.32;
            addend = 40833.33;
        } else {
            subtrahend = 666667;
            percent = 0.35;
            addend = 200833.33;
        }
        double taxExcess = netTaxableComp - subtrahend;
        if (taxExcess < 0) taxExcess = 0;
        return (taxExcess * percent) + addend;
    }
}