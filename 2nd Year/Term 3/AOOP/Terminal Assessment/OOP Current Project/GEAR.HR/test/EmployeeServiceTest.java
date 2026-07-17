import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import model.Employee;
import model.UserCredential;
import repository.IEmployeeRepository;
import service.EmployeeService;
import service.IUserCredentialService;

/**
 * Unit tests for {@link EmployeeService} using a fake {@link IEmployeeRepository}
 * (in-memory list) and a fake {@link IUserCredentialService}, so employee CRUD and
 * credential-sync behavior can be verified without a database.
 */
public class EmployeeServiceTest {

    /** In-memory employee repository; captures the last snapshot passed to save(). */
    private static class FakeEmployeeRepository implements IEmployeeRepository {
        private final List<Employee> data;
        private List<Employee> savedSnapshot;

        FakeEmployeeRepository(List<Employee> data) { this.data = data; }

        @Override public List<Employee> load() { return new ArrayList<>(data); }

        @Override public void save(List<Employee> employees) {
            this.savedSnapshot = employees != null ? new ArrayList<>(employees) : null;
        }
    }

    /** Stub credential service; configurable results and call tracking, no persistence. */
    private static class FakeUserCredentialService implements IUserCredentialService {
        String upsertResult;
        String patchResult;
        String deletedUserId;

        @Override public void reload() { /* no-op for tests */ }

        @Override public List<UserCredential> getAllCredentials() { return new ArrayList<>(); }

        @Override public Set<String> getDistinctRoles() { return new HashSet<>(); }

        @Override public UserCredential findByUserId(String userId) { return null; }

        @Override public String updateCredential(String userId, String password, String role, String email) {
            return null;
        }

        @Override public String upsertCredentialForNewEmployee(String userId, String email, String role) {
            return upsertResult;
        }

        @Override public String patchCredentialFromEmployee(String userId, String email, String role) {
            return patchResult;
        }

        @Override public void deleteCredentialByUserId(String userId) {
            this.deletedUserId = userId;
        }
    }

    private Employee employee(String employeeNumber) {
        return new Employee(employeeNumber, "Dela Cruz", "Juan", "12-3456789-0", "123456789012",
            "123-456-789-000", "123456789012", "juan@motorph.com", "Developer", "regular",
            "Manila", "555-123-456");
    }

    @Test
    public void constructorLoadsEmployeesFromRepository() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        EmployeeService service = new EmployeeService(new FakeEmployeeRepository(seed), new FakeUserCredentialService());

        List<Employee> all = service.getAllEmployees();
        assertEquals(1, all.size());
        assertEquals("10001", all.get(0).getEmployeeNumber());
    }

    @Test
    public void findEmployeeByIdReturnsMatchOrNullWhenMissing() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        EmployeeService service = new EmployeeService(new FakeEmployeeRepository(seed), new FakeUserCredentialService());

        assertNotNull(service.findEmployeeById("10001"));
        assertNull(service.findEmployeeById("99999"));
    }

    @Test
    public void findEmployeeBySssAndByEmailReturnMatchOrNullWhenMissing() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        EmployeeService service = new EmployeeService(new FakeEmployeeRepository(seed), new FakeUserCredentialService());

        assertNotNull(service.findEmployeeBySss("12-3456789-0"));
        assertNull(service.findEmployeeBySss("00-0000000-0"));

        assertNotNull(service.findEmployeeByEmail("juan@motorph.com"));
        assertNull(service.findEmployeeByEmail("missing@motorph.com"));
    }

    @Test
    public void addEmployeeRejectsInvalidEmployee() {
        List<Employee> seed = new ArrayList<>();
        EmployeeService service = new EmployeeService(new FakeEmployeeRepository(seed), new FakeUserCredentialService());

        // Employee number is blank, which fails EmployeeValidationUtil's required-field check.
        Employee invalid = employee("");
        service.addEmployee(invalid);

        assertTrue(service.getAllEmployees().isEmpty());
    }

    @Test
    public void addEmployeeRejectsDuplicateEmployeeNumber() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        EmployeeService service = new EmployeeService(new FakeEmployeeRepository(seed), new FakeUserCredentialService());

        service.addEmployee(employee("10001"));

        assertEquals(1, service.getAllEmployees().size());
    }

    @Test
    public void addEmployeeSucceedsPersistsAndSyncsCredential() {
        FakeEmployeeRepository repository = new FakeEmployeeRepository(new ArrayList<>());
        FakeUserCredentialService credentials = new FakeUserCredentialService();
        credentials.upsertResult = null; // credential sync succeeds
        EmployeeService service = new EmployeeService(repository, credentials);

        String result = service.addEmployee(employee("10001"));

        assertNull(result);
        assertEquals(1, service.getAllEmployees().size());
        assertNotNull(repository.savedSnapshot);
        assertEquals(1, repository.savedSnapshot.size());
    }

    @Test
    public void addEmployeeRollsBackWhenCredentialSyncFails() {
        FakeEmployeeRepository repository = new FakeEmployeeRepository(new ArrayList<>());
        FakeUserCredentialService credentials = new FakeUserCredentialService();
        credentials.upsertResult = "Email already in use.";
        EmployeeService service = new EmployeeService(repository, credentials);

        String result = service.addEmployee(employee("10001"));

        assertEquals("Email already in use.", result);
        assertTrue(service.getAllEmployees().isEmpty());
        assertNotNull(repository.savedSnapshot);
        assertTrue(repository.savedSnapshot.isEmpty());
    }

    @Test
    public void updateEmployeeUpdatesFieldsAndSyncsCredential() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        FakeEmployeeRepository repository = new FakeEmployeeRepository(seed);
        FakeUserCredentialService credentials = new FakeUserCredentialService();
        credentials.patchResult = null;
        EmployeeService service = new EmployeeService(repository, credentials);

        Employee changes = employee("10001");
        changes.setPosition("Senior Developer");
        changes.setEmail("juan.delacruz@motorph.com");

        String result = service.updateEmployee(changes);

        assertNull(result);
        Employee updated = service.findEmployeeById("10001");
        assertEquals("Senior Developer", updated.getPosition());
        assertEquals("juan.delacruz@motorph.com", updated.getEmail());
        assertNotNull(repository.savedSnapshot);
    }

    @Test
    public void updateEmployeeReturnsNullWhenEmployeeNotFound() {
        EmployeeService service = new EmployeeService(new FakeEmployeeRepository(new ArrayList<>()), new FakeUserCredentialService());

        String result = service.updateEmployee(employee("10001"));

        assertNull(result);
    }

    @Test
    public void updateEmployeeDoesNotPersistWhenResultingEmployeeIsInvalid() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        FakeEmployeeRepository repository = new FakeEmployeeRepository(seed);
        EmployeeService service = new EmployeeService(repository, new FakeUserCredentialService());

        Employee changes = employee("10001");
        changes.setEmail("not-an-email"); // fails EmployeeValidationUtil.validateEmail

        String result = service.updateEmployee(changes);

        assertNull(result);
        assertNull("saveEmployees() must not run when the update leaves the employee invalid", repository.savedSnapshot);
    }

    @Test
    public void deleteEmployeeRemovesEmployeeAndDeletesCredential() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        FakeEmployeeRepository repository = new FakeEmployeeRepository(seed);
        FakeUserCredentialService credentials = new FakeUserCredentialService();
        EmployeeService service = new EmployeeService(repository, credentials);

        service.deleteEmployee("10001");

        assertTrue(service.getAllEmployees().isEmpty());
        assertNotNull(repository.savedSnapshot);
        assertTrue(repository.savedSnapshot.isEmpty());
        assertEquals("10001", credentials.deletedUserId);
    }

    @Test
    public void reloadEmployeesRereadsFromRepository() {
        List<Employee> seed = new ArrayList<>();
        seed.add(employee("10001"));
        FakeEmployeeRepository repository = new FakeEmployeeRepository(seed);
        EmployeeService service = new EmployeeService(repository, new FakeUserCredentialService());

        assertEquals(1, service.getAllEmployees().size());

        seed.add(employee("10002"));
        service.reloadEmployees();

        assertEquals(2, service.getAllEmployees().size());
        assertFalse(service.getAllEmployees().isEmpty());
    }
}
