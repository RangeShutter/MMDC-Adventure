import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.PayrollResult;
import service.PayrollReport;

/**
 * Unit tests for {@link PayrollReport#formatSummary(List, String, String)} verifying the
 * department header, employee count, and totals reflect only the supplied (filtered) results.
 */
public class PayrollReportTest {

    private PayrollResult result(String id, String name, double gross, double deductions,
                                 double allowances, double net) {
        return new PayrollResult(id, name, "HR Rank and File", "March",
            100.0, 160.0, gross, 20000.0,
            0, 0, 0, 0, deductions,
            0, 0, 0, allowances, net);
    }

    @Test
    public void summaryShowsDepartmentHeaderAndAggregatedTotals() {
        List<PayrollResult> results = new ArrayList<>();
        results.add(result("10006", "Andrea Villanueva", 16000, 1800, 3500, 17700));
        results.add(result("10008", "Alice Romualdez", 12000, 1200, 2500, 13300));

        String text = PayrollReport.formatSummary(results, "March", "HR");

        assertTrue(text.contains("PAYROLL SUMMARY FOR MARCH"));
        assertTrue(text.contains("Department: HR"));
        assertTrue(text.contains("DEPARTMENT TOTALS"));
        assertTrue(text.contains("Employees: 2"));
        // Totals: gross 28,000.00 ; net 31,000.00
        assertTrue(text.contains("Total Gross Pay: \u20b128,000.00"));
        assertTrue(text.contains("Total Net Salary: \u20b131,000.00"));
        // Only the two supplied employees appear.
        assertTrue(text.contains("Employee ID: 10006"));
        assertTrue(text.contains("Employee ID: 10008"));
    }

    @Test
    public void emptyDepartmentProducesZeroTotalsAndNotice() {
        String text = PayrollReport.formatSummary(new ArrayList<>(), "April", "Payroll");

        assertTrue(text.contains("Department: PAYROLL"));
        assertTrue(text.contains("No employee payroll records available for this department and month."));
        assertTrue(text.contains("Employees: 0"));
    }

    @Test
    public void nullDepartmentFallsBackToAllDepartments() {
        String text = PayrollReport.formatSummary(new ArrayList<>(), "May", null);
        assertTrue(text.contains("Department: ALL DEPARTMENTS"));
    }
}
