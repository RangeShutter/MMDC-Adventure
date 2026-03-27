package tools;

import util.PayrollUtils;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * One-shot utility: rebuild {@code csv/payroll_records.csv} from {@code EmployeeData.md}
 * (employee IDs 10001–10034). Deductions use {@link PayrollUtils} (same as the app).
 * Employees 10035–10038 are not in EmployeeData.md and are omitted from the CSV
 * (they use {@code PayrollProcessor} defaults until edited in the UI).
 */
public final class GeneratePayrollRecordsCsv {

    private static final class Row {
        final int id;
        final double base;
        final double rice;
        final double phone;
        final double clothing;

        Row(int id, double base, double rice, double phone, double clothing) {
            this.id = id;
            this.base = base;
            this.rice = rice;
            this.phone = phone;
            this.clothing = clothing;
        }
    }

    public static void main(String[] args) throws Exception {
        List<Row> rows = new ArrayList<>();
        // Data from EmployeeData.md (Basic Salary, Rice Subsidy, Phone Allowance, Clothing Allowance)
        rows.add(new Row(10001, 90000, 1500, 2000, 1000));
        rows.add(new Row(10002, 60000, 1500, 2000, 1000));
        rows.add(new Row(10003, 60000, 1500, 2000, 1000));
        rows.add(new Row(10004, 60000, 1500, 2000, 1000));
        rows.add(new Row(10005, 52670, 1500, 1000, 1000));
        rows.add(new Row(10006, 52670, 1500, 1000, 1000));
        rows.add(new Row(10007, 42975, 1500, 800, 800));
        rows.add(new Row(10008, 22500, 1500, 500, 500));
        rows.add(new Row(10009, 22500, 1500, 500, 500));
        rows.add(new Row(10010, 52670, 1500, 1000, 1000));
        rows.add(new Row(10011, 50825, 1500, 1000, 1000));
        rows.add(new Row(10012, 38475, 1500, 800, 800));
        rows.add(new Row(10013, 24000, 1500, 500, 500));
        rows.add(new Row(10014, 24000, 1500, 500, 500));
        rows.add(new Row(10015, 53500, 1500, 1000, 1000));
        rows.add(new Row(10016, 42975, 1500, 800, 800));
        rows.add(new Row(10017, 41850, 1500, 800, 800));
        rows.add(new Row(10018, 22500, 1500, 500, 500));
        rows.add(new Row(10019, 22500, 1500, 500, 500));
        rows.add(new Row(10020, 23250, 1500, 500, 500));
        rows.add(new Row(10021, 23250, 1500, 500, 500));
        rows.add(new Row(10022, 24000, 1500, 500, 500));
        rows.add(new Row(10023, 22500, 1500, 500, 500));
        rows.add(new Row(10024, 22500, 1500, 500, 500));
        rows.add(new Row(10025, 24000, 1500, 500, 500));
        rows.add(new Row(10026, 24750, 1500, 500, 500));
        rows.add(new Row(10027, 24750, 1500, 500, 500));
        rows.add(new Row(10028, 24000, 1500, 500, 500));
        rows.add(new Row(10029, 22500, 1500, 500, 500));
        rows.add(new Row(10030, 22500, 1500, 500, 500));
        rows.add(new Row(10031, 22500, 1500, 500, 500));
        rows.add(new Row(10032, 52670, 1500, 1000, 1000));
        rows.add(new Row(10033, 52670, 1500, 1000, 1000));
        rows.add(new Row(10034, 52670, 1500, 1000, 1000));

        rows.sort(Comparator.comparingInt(r -> r.id));

        Path out = Paths.get("csv/payroll_records.csv");
        Files.createDirectories(out.getParent());

        try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(out, StandardCharsets.UTF_8))) {
            w.println("EmployeeID,BaseSalary,SSSAmount,PhilHealthAmount,PagIBIGAmount,WithholdingTax,RiceSubsidy,PhoneAllowance,ClothingAllowance");
            for (Row r : rows) {
                double sss = PayrollUtils.calculateSSSAmount(r.base);
                double phil = PayrollUtils.calculatePhilHealthAmount(r.base);
                double pag = PayrollUtils.calculatePagIbigAmount(r.base);
                double tax = PayrollUtils.calculateWithholdingTax(r.base, r.rice, r.phone, r.clothing);
                w.printf(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.1f,%.1f,%.1f%n",
                    r.id, r.base, sss, phil, pag, tax, r.rice, r.phone, r.clothing);
            }
        }
        System.out.println("Wrote " + out.toAbsolutePath() + " (" + rows.size() + " rows).");
    }

    private GeneratePayrollRecordsCsv() {}
}
