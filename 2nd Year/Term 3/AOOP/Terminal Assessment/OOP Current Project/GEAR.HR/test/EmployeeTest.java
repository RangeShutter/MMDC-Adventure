import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Employee;
import model.EmploymentStatus;

/**
 * Unit tests for {@link Employee}: display name formatting, validity checks,
 * and the {@code setStatus} overload.
 */
public class EmployeeTest {

    private Employee validEmployee(String employeeNumber) {
        return new Employee(employeeNumber, "Dela Cruz", "Juan", "12-3456789-0", "123456789012",
            "123-456-789-000", "123456789012", "juan@motorph.com", "Developer", "regular",
            "Manila", "555-123-456");
    }

    @Test
    public void getDisplayNameWithoutIdReturnsFirstAndLastName() {
        Employee emp = validEmployee("10001");
        assertEquals("Juan Dela Cruz", emp.getDisplayName(false));
    }

    @Test
    public void getDisplayNameWithIdAppendsEmployeeNumber() {
        Employee emp = validEmployee("10001");
        assertEquals("Juan Dela Cruz (10001)", emp.getDisplayName(true));
    }

    @Test
    public void isValidReturnsTrueAndNoValidationErrorForWellFormedEmployee() {
        Employee emp = validEmployee("10001");
        assertTrue(emp.isValid());
        assertNull(emp.getValidationError());
    }

    @Test
    public void isValidReturnsFalseAndValidationErrorForMalformedSss() {
        Employee emp = new Employee("10001", "Dela Cruz", "Juan", "not-an-sss", "123456789012",
            "123-456-789-000", "123456789012", "juan@motorph.com", "Developer", "regular",
            "Manila", "555-123-456");

        assertFalse(emp.isValid());
        assertNotNull(emp.getValidationError());
    }

    @Test
    public void setStatusWithEnumOverloadSwitchesStatusDirectly() {
        Employee emp = validEmployee("10001");
        assertEquals(EmploymentStatus.REGULAR, emp.getStatusEnum());

        emp.setStatus(EmploymentStatus.PROBATIONARY);

        assertEquals(EmploymentStatus.PROBATIONARY, emp.getStatusEnum());
        assertEquals("probationary", emp.getStatus());
    }

    @Test
    public void setHourlyRateIgnoresNegativeValues() {
        Employee emp = validEmployee("10001");
        emp.setHourlyRate(150.0);

        emp.setHourlyRate(-50.0);

        assertEquals(150.0, emp.getHourlyRate(), 0.001);
    }
}
