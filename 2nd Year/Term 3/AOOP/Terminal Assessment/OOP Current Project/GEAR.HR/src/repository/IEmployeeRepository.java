package repository;

import model.Employee;

import java.util.List;

/**
 * [INTERFACE] JDBC persistence contract for employee data.
 * Production implementation: {@link EmployeeJdbcRepository} (MySQL table {@code employees}).
 * Alternative implementations may be supplied for unit tests.
 */
public interface IEmployeeRepository {
    /**
     * [INTERFACE] Loads all employees from the database via JDBC.
     * @return list of employees (empty if none or on JDBC error)
     */
    List<Employee> load();

    /**
     * [INTERFACE] Persists employees to the database via JDBC (full table replace).
     * @param employees list to save (null is ignored)
     */
    void save(List<Employee> employees);
}
