import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import model.AttendanceRecord;
import model.Employee;
import model.PayrollData;
import model.PayrollResult;
import repository.IPayrollRepository;
import service.AttendanceService;
import service.PayrollProcessor;
import repository.IAttendanceRepository;

/**
 * Unit tests for {@link PayrollProcessor#processPayroll(Employee, String)} using a fake
 * {@link IPayrollRepository} and a real {@link AttendanceService} backed by a fake repository,
 * so gross/net math can be verified without a database.
 */
public class PayrollProcessorTest {

    private static final double DELTA = 0.01;

    /** In-memory payroll repository for one employee. */
    private static class FakePayrollRepository implements IPayrollRepository {
        private final Map<String, PayrollData> data;
        FakePayrollRepository(Map<String, PayrollData> data) { this.data = data; }
        @Override public Map<String, PayrollData> load() { return new HashMap<>(data); }
        @Override public void save(Map<String, PayrollData> d) { /* no-op for tests */ }
    }

    /** In-memory attendance repository returning a fixed set of records. */
    private static class FakeAttendanceRepository implements IAttendanceRepository {
        private final List<AttendanceRecord> records;
        FakeAttendanceRepository(List<AttendanceRecord> records) { this.records = records; }
        @Override public List<AttendanceRecord> load() { return new java.util.ArrayList<>(records); }
        @Override public void save(List<AttendanceRecord> r) { /* no-op for tests */ }
    }

    private Employee employee(String id) {
        return new Employee(id, "Dela Cruz", "Juan", "12-3456789-0", "123456789012",
            "123-456-789-000", "123456789012", "juan@motorph.com", "Developer", "regular",
            "Manila", "555-123-456");
    }

    @Test
    public void processPayrollComputesGrossAndNetFromAttendanceHours() {
        Map<String, PayrollData> map = new HashMap<>();
        // hourlyRate 100, allowances rice 1500 / phone 1000 / clothing 800
        map.put("10001", new PayrollData(16000, 100, 0, 0, 0, 0f, 1500f, 1000f, 800f));

        // Two 8-hour days in March 2026 => 16 worked hours.
        List<AttendanceRecord> recs = new java.util.ArrayList<>();
        recs.add(new AttendanceRecord("10001", "2026-03-03", "Present", "08:00", "16:00"));
        recs.add(new AttendanceRecord("10001", "2026-03-04", "Present", "08:00", "16:00"));

        PayrollProcessor processor = new PayrollProcessor(
            new FakePayrollRepository(map), new AttendanceService(new FakeAttendanceRepository(recs)));

        PayrollResult result = processor.processPayroll(employee("10001"), "March");
        assertNotNull(result);

        double expectedGross = 100 * 16; // 1600
        assertEquals(expectedGross, result.getGrossPay(), DELTA);

        double expectedDeductions =
            util.PayrollUtils.calculateSSSAmount(expectedGross)
            + util.PayrollUtils.calculatePhilHealthAmount(expectedGross)
            + util.PayrollUtils.calculatePagIbigAmount(expectedGross)
            + util.PayrollUtils.calculateWithholdingTax(expectedGross, 1500f, 1000f, 800f);
        double expectedAllowances = 1500 + 1000 + 800;
        double expectedNet = expectedGross - expectedDeductions + expectedAllowances;

        assertEquals(expectedDeductions, result.getTotalDeductions(), DELTA);
        assertEquals(expectedAllowances, result.getTotalAllowances(), DELTA);
        assertEquals(expectedNet, result.getNetSalary(), DELTA);
    }

    @Test
    public void processPayrollWithoutAttendanceYieldsZeroGross() {
        Map<String, PayrollData> map = new HashMap<>();
        map.put("10001", new PayrollData(16000, 100, 0, 0, 0, 0f, 0f, 0f, 0f));

        PayrollProcessor processor = new PayrollProcessor(
            new FakePayrollRepository(map),
            new AttendanceService(new FakeAttendanceRepository(new java.util.ArrayList<>())));

        PayrollResult result = processor.processPayroll(employee("10001"), "March");
        assertNotNull(result);
        assertEquals(0.0, result.getGrossPay(), DELTA);
    }
}
