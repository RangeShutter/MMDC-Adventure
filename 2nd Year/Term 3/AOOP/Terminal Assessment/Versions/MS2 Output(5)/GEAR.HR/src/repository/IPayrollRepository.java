package repository;

import model.PayrollData;

import java.util.Map;

/**
 * [INTERFACE] JDBC persistence contract for payroll data (employeeId -> PayrollData).
 * Production implementation: {@link PayrollJdbcRepository} (MySQL table {@code payroll_settings}).
 * Alternative implementations may be supplied for unit tests.
 */
public interface IPayrollRepository {
    /**
     * [INTERFACE] Loads all payroll rows from the database via JDBC.
     * @return map of employeeId to PayrollData (empty if none or on JDBC error)
     */
    Map<String, PayrollData> load();

    /**
     * [INTERFACE] Persists payroll data to the database via JDBC (full table replace).
     * @param data map to save (null is ignored)
     */
    void save(Map<String, PayrollData> data);
}
