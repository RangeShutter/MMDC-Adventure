import model.Employee;
import model.PayrollData;
import model.PayrollResult;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Computes employee compensation from Employee and payroll data (OOP redesign - GEAR.HR).
 * Uses PayrollUtils for deductions/allowances; persists to payroll_records.csv.
 */
public class PayrollProcessor {
    private static final String PAYROLL_CSV_FILE = "payroll_records.csv";
    private static final String HEADER = "EmployeeID,BaseSalary,SSSAmount,PhilHealthAmount,PagIBIGAmount,WithholdingTax,RiceSubsidy,PhoneAllowance,ClothingAllowance";

    private final Map<String, PayrollData> payrollData = new HashMap<>();

    public PayrollProcessor() {
        loadPayrollDataFromCSV();
        if (payrollData.isEmpty()) {
            initializeSamplePayrollData();
            savePayrollDataToCSV();
        }
    }

    public void loadPayrollDataFromCSV() {
        payrollData.clear();
        if (!Files.exists(Paths.get(PAYROLL_CSV_FILE))) return;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PAYROLL_CSV_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 9) {
                    String employeeId = data[0].trim();
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
            // leave empty
        }
    }

    public void savePayrollDataToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYROLL_CSV_FILE))) {
            writer.println(HEADER);
            for (Map.Entry<String, PayrollData> entry : payrollData.entrySet()) {
                PayrollData data = entry.getValue();
                double computedTax = PayrollUtils.calculateWithholdingTax(
                    data.getBaseSalary(), data.getRiceSubsidy(), data.getPhoneAllowance(), data.getClothingAllowance()
                );
                writer.println(String.join(",",
                    entry.getKey(),
                    String.valueOf(data.getBaseSalary()),
                    String.valueOf(data.getSssAmount()),
                    String.valueOf(data.getPhilHealthAmount()),
                    String.valueOf(data.getPagIbigAmount()),
                    String.format("%.2f", computedTax),
                    String.valueOf(data.getRiceSubsidy()),
                    String.valueOf(data.getPhoneAllowance()),
                    String.valueOf(data.getClothingAllowance())
                ));
            }
        } catch (IOException e) {
            // ignore
        }
    }

    private void initializeSamplePayrollData() {
        double base1 = 35000.0, base2 = 60000.0, base3 = 45000.0;
        float rice1 = 1500.0f, phone1 = 1000.0f, cloth1 = 800.0f;
        float rice2 = 1500.0f, phone2 = 800.0f, cloth2 = 600.0f;
        float rice3 = 1500.0f, phone3 = 900.0f, cloth3 = 700.0f;
        payrollData.put("1001", new PayrollData(base1,
            PayrollUtils.calculateSSSAmount(base1), PayrollUtils.calculatePhilHealthAmount(base1), PayrollUtils.calculatePagIbigAmount(base1),
            (float) PayrollUtils.calculateWithholdingTax(base1, rice1, phone1, cloth1), rice1, phone1, cloth1));
        payrollData.put("1002", new PayrollData(base2,
            PayrollUtils.calculateSSSAmount(base2), PayrollUtils.calculatePhilHealthAmount(base2), PayrollUtils.calculatePagIbigAmount(base2),
            (float) PayrollUtils.calculateWithholdingTax(base2, rice2, phone2, cloth2), rice2, phone2, cloth2));
        payrollData.put("1003", new PayrollData(base3,
            PayrollUtils.calculateSSSAmount(base3), PayrollUtils.calculatePhilHealthAmount(base3), PayrollUtils.calculatePagIbigAmount(base3),
            (float) PayrollUtils.calculateWithholdingTax(base3, rice3, phone3, cloth3), rice3, phone3, cloth3));
    }

    private static PayrollData getDefaultPayrollData() {
        double baseSalary = 30000.0;
        float rice = 1500.0f, phone = 1000.0f, cloth = 800.0f;
        return new PayrollData(baseSalary,
            PayrollUtils.calculateSSSAmount(baseSalary), PayrollUtils.calculatePhilHealthAmount(baseSalary), PayrollUtils.calculatePagIbigAmount(baseSalary),
            (float) PayrollUtils.calculateWithholdingTax(baseSalary, rice, phone, cloth), rice, phone, cloth);
    }

    public PayrollData getPayrollData(String employeeId) {
        return payrollData.getOrDefault(employeeId, getDefaultPayrollData());
    }

    public void updatePayrollData(String employeeId, PayrollData data) {
        if (employeeId != null && data != null) {
            payrollData.put(employeeId, data);
            savePayrollDataToCSV();
        }
    }

    public void removePayrollData(String employeeId) {
        payrollData.remove(employeeId);
        savePayrollDataToCSV();
    }

    /**
     * Computes payroll for the given employee and month; returns a PayrollResult for reporting.
     */
    public PayrollResult processPayroll(Employee employee, String month) {
        if (employee == null || month == null) return null;
        PayrollData data = getPayrollData(employee.getEmployeeNumber());
        double baseSalary = data.getBaseSalary();
        double sssDeduction = data.getSSSDeduction();
        double philHealthDeduction = data.getPhilHealthDeduction();
        double pagIbigDeduction = data.getPagIbigDeduction();
        double taxDeduction = data.getTaxDeduction();
        double totalDeductions = data.calculateTotalDeductions();
        double totalAllowances = data.calculateTotalAllowances();
        double netSalary = data.calculateNetSalary();
        String name = (employee.getFirstName() + " " + employee.getLastName()).trim();
        return new PayrollResult(
            employee.getEmployeeNumber(), name, employee.getPosition(), month,
            baseSalary, sssDeduction, philHealthDeduction, pagIbigDeduction,
            taxDeduction, totalDeductions,
            data.getRiceSubsidy(), data.getPhoneAllowance(), data.getClothingAllowance(), totalAllowances,
            netSalary
        );
    }
}
