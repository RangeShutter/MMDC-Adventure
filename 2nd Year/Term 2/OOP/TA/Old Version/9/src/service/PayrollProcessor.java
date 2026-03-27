package service;

import model.Employee;
import model.PayrollData;
import model.PayrollResult;
import repository.IPayrollRepository;
import util.PayrollUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Computes employee compensation; persistence delegated to repository (OOP redesign - GEAR.HR).
 * [POLYMORPHISM] Depends on IPayrollRepository so implementations can be swapped.
 */
public class PayrollProcessor {
    /** [INTERFACE] [POLYMORPHISM] Uses IPayrollRepository contract. */
    private final IPayrollRepository repository;
    private final IAttendanceService attendanceService;
    private final Map<String, PayrollData> payrollData = new HashMap<>();

    /**
     * [ENCAPSULATION] Injects repository and attendance service dependencies.
     * [POLYMORPHISM] Parameters are interface types ({@link IPayrollRepository}, {@link IAttendanceService}).
     */
    public PayrollProcessor(IPayrollRepository repository, IAttendanceService attendanceService) {
        this.repository = repository;
        this.attendanceService = attendanceService;
        payrollData.putAll(repository.load());
    }

    public void loadPayrollDataFromCSV() {
        payrollData.clear();
        payrollData.putAll(repository.load());
    }

    public void savePayrollDataToCSV() {
        Map<String, PayrollData> toSave = new HashMap<>();
        for (Map.Entry<String, PayrollData> e : payrollData.entrySet()) {
            PayrollData d = e.getValue();
            if (!d.isValid()) continue;
            double tax = PayrollUtils.calculateWithholdingTax(
                d.getBaseSalary(), d.getRiceSubsidy(), d.getPhoneAllowance(), d.getClothingAllowance()
            );
            PayrollData built = new PayrollData(
                d.getBaseSalary(), d.getHourlyRate(), d.getSssAmount(), d.getPhilHealthAmount(), d.getPagIbigAmount(),
                (float) tax, d.getRiceSubsidy(), d.getPhoneAllowance(), d.getClothingAllowance()
            );
            if (built.isValid()) toSave.put(e.getKey(), built);
        }
        repository.save(toSave);
    }

    private static PayrollData getDefaultPayrollData() {
        double baseSalary = 30000.0;
        double hourlyRate = baseSalary / 168.0;
        float rice = 1500.0f, phone = 1000.0f, cloth = 800.0f;
        return new PayrollData(baseSalary, hourlyRate,
            PayrollUtils.calculateSSSAmount(baseSalary), PayrollUtils.calculatePhilHealthAmount(baseSalary), PayrollUtils.calculatePagIbigAmount(baseSalary),
            (float) PayrollUtils.calculateWithholdingTax(baseSalary, rice, phone, cloth), rice, phone, cloth);
    }

    public PayrollData getPayrollData(String employeeId) {
        return payrollData.getOrDefault(employeeId, getDefaultPayrollData());
    }

    public void updatePayrollData(String employeeId, PayrollData data) {
        if (employeeId == null || data == null || !data.isValid()) return;
        payrollData.put(employeeId, data);
        savePayrollDataToCSV();
    }

    public void removePayrollData(String employeeId) {
        payrollData.remove(employeeId);
        savePayrollDataToCSV();
    }

    public PayrollResult processPayroll(Employee employee, String month) {
        if (employee == null || month == null) return null;
        PayrollData data = getPayrollData(employee.getEmployeeNumber());
        double hourlyRate = data.getHourlyRate();
        double workedHours = attendanceService != null
            ? attendanceService.getWorkedHoursForMonth(employee.getEmployeeNumber(), month)
            : 0.0;
        double grossPay = data.calculateGrossPay(workedHours);
        double sssDeduction = PayrollUtils.calculateSSSAmount(grossPay);
        double philHealthDeduction = PayrollUtils.calculatePhilHealthAmount(grossPay);
        double pagIbigDeduction = PayrollUtils.calculatePagIbigAmount(grossPay);
        double taxDeduction = PayrollUtils.calculateWithholdingTax(
            grossPay, data.getRiceSubsidy(), data.getPhoneAllowance(), data.getClothingAllowance()
        );
        double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + taxDeduction;
        double totalAllowances = data.calculateTotalAllowances();
        double netSalary = grossPay - totalDeductions + totalAllowances;
        String name = (employee.getFirstName() + " " + employee.getLastName()).trim();
        return new PayrollResult(
            employee.getEmployeeNumber(), name, employee.getPosition(), month,
            hourlyRate, workedHours, grossPay, data.getBaseSalary(), sssDeduction, philHealthDeduction, pagIbigDeduction,
            taxDeduction, totalDeductions,
            data.getRiceSubsidy(), data.getPhoneAllowance(), data.getClothingAllowance(), totalAllowances,
            netSalary
        );
    }
}
