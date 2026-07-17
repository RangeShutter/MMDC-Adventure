import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import model.Employee;
import util.EmployeeValidationUtil;

/**
 * Unit tests for the format validators in {@link EmployeeValidationUtil} that back
 * {@link Employee#isValid()} at persistence boundaries.
 */
public class EmployeeValidationUtilTest {

    @Test
    public void validateEmployeeNumberAcceptsExactlyFiveDigits() {
        assertNull(EmployeeValidationUtil.validateEmployeeNumber("10001"));
        assertNotNull(EmployeeValidationUtil.validateEmployeeNumber("1001A"));
    }

    @Test
    public void validateStatusAcceptsOnlyRegularOrProbationary() {
        assertNull(EmployeeValidationUtil.validateStatus("regular"));
        assertNull(EmployeeValidationUtil.validateStatus("probationary"));
        assertNotNull(EmployeeValidationUtil.validateStatus("contractual"));
    }

    @Test
    public void validateSssRequiresTwoSevenOneDigitGroups() {
        assertNull(EmployeeValidationUtil.validateSss("12-3456789-0"));
        assertNotNull(EmployeeValidationUtil.validateSss("123456789"));
    }

    @Test
    public void validatePhilHealthAndPagIbigRequireTwelveDigits() {
        assertNull(EmployeeValidationUtil.validatePhilHealth("123456789012"));
        assertNotNull(EmployeeValidationUtil.validatePhilHealth("12345"));

        assertNull(EmployeeValidationUtil.validatePagIbig("123456789012"));
        assertNotNull(EmployeeValidationUtil.validatePagIbig("12345"));
    }

    @Test
    public void validateTinRequiresFourGroupsOfThreeDigits() {
        assertNull(EmployeeValidationUtil.validateTin("123-456-789-000"));
        assertNotNull(EmployeeValidationUtil.validateTin("123456789000"));
    }

    @Test
    public void validateEmailRejectsInvalidFormat() {
        assertNull(EmployeeValidationUtil.validateEmail("name@example.com"));
        assertNotNull(EmployeeValidationUtil.validateEmail("invalid-email"));
    }

    @Test
    public void validatePhoneRequiresThreeGroupsOfThreeDigits() {
        assertNull(EmployeeValidationUtil.validatePhone("555-123-456"));
        assertNotNull(EmployeeValidationUtil.validatePhone("5551234"));
    }

    @Test
    public void validateCharactersOnlyRejectsDigitsButAllowsBlank() {
        assertNull(EmployeeValidationUtil.validateCharactersOnly("Dela Cruz", "Last Name"));
        assertNull(EmployeeValidationUtil.validateCharactersOnly("", "Last Name"));
        assertNotNull(EmployeeValidationUtil.validateCharactersOnly("Dela Cruz3", "Last Name"));
    }

    @Test
    public void validateEmployeeReturnsNullForFullyValidEmployee() {
        Employee valid = new Employee("10001", "Dela Cruz", "Juan", "12-3456789-0", "123456789012",
            "123-456-789-000", "123456789012", "juan@motorph.com", "Developer", "regular",
            "Manila", "555-123-456");

        assertNull(EmployeeValidationUtil.validateEmployee(valid));
    }

    @Test
    public void validateEmployeeReturnsEmployeeNumberErrorFirst() {
        Employee malformedNumber = new Employee("1001A", "Dela Cruz", "Juan", "12-3456789-0", "123456789012",
            "123-456-789-000", "123456789012", "juan@motorph.com", "Developer", "regular",
            "Manila", "555-123-456");

        String error = EmployeeValidationUtil.validateEmployee(malformedNumber);
        assertNotNull(error);
        assertNotNull(EmployeeValidationUtil.validateEmployeeNumber(malformedNumber.getEmployeeNumber()));
    }
}
