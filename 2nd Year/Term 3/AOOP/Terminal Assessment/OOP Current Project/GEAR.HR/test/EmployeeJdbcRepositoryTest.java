import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import model.Employee;
import repository.DatabaseConnectionManager;
import repository.EmployeeJdbcRepository;

/**
 * Integration test for {@link EmployeeJdbcRepository} against the real MySQL {@code employees}
 * table (see sql/schema.sql and sql/seed.sql). Read-only: only exercises {@code load()} so the
 * shared table is never mutated by the test suite.
 *
 * Skips itself via {@link Assume} when no database is reachable, so the rest of the suite still
 * runs cleanly without a database (README: "Unit tests run without a database using in-memory
 * fakes").
 */
public class EmployeeJdbcRepositoryTest {

    @Before
    public void assumeDatabaseIsReachable() {
        Connection conn = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
        } catch (SQLException e) {
            Assume.assumeNoException(
                "Skipping EmployeeJdbcRepositoryTest: no database available", e);
        } finally {
            DatabaseConnectionManager.closeConnection(conn);
        }
    }

    @Test
    public void loadReturnsEmployeesFromDatabaseWithMappedFields() {
        EmployeeJdbcRepository repo = new EmployeeJdbcRepository();
        List<Employee> employees = repo.load();

        assertNotNull(employees);
        assertFalse("Expected the seeded employees table to contain rows", employees.isEmpty());

        Employee first = employees.get(0);
        assertNotNull(first.getEmployeeNumber());
        assertFalse(first.getEmployeeNumber().isEmpty());
    }

    @Test
    public void loadMapsSeededEmployee10001WhenPresent() {
        EmployeeJdbcRepository repo = new EmployeeJdbcRepository();
        List<Employee> employees = repo.load();

        Employee emp10001 = null;
        for (Employee emp : employees) {
            if ("10001".equals(emp.getEmployeeNumber())) {
                emp10001 = emp;
                break;
            }
        }

        Assume.assumeTrue(
            "Skipping: seeded employee 10001 not found (run sql/seed.sql to enable this check)",
            emp10001 != null);

        assertNotNull(emp10001.getFirstName());
        assertNotNull(emp10001.getLastName());
        assertNotNull(emp10001.getStatusEnum());
    }
}
