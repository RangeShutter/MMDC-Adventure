package repository;

import model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC persistence for employees (MySQL table: employees). Load/save only; no business logic.
 * [INTERFACE] Implements IEmployeeRepository.
 * [INHERITANCE] Extends AbstractJdbcRepository for shared JDBC load/save; provides Employee-specific SQL and mapping.
 * [POLYMORPHISM] Can be used as IEmployeeRepository by callers.
 */
public class EmployeeJdbcRepository extends AbstractJdbcRepository<Employee> implements IEmployeeRepository {

    /** [ABSTRACTION] [INHERITANCE] Returns the employees table name. */
    @Override
    protected String getTableName() {
        return "employees";
    }

    /** [ABSTRACTION] [INHERITANCE] Returns the parameterized INSERT for one employee row. */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO employees (employee_number, last_name, first_name, sss_number, "
            + "phil_health_number, tin, pag_ibig_number, email, position, status, address, phone) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    /** [ABSTRACTION] [INHERITANCE] Maps one result row to an Employee. */
    @Override
    protected Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getString("employee_number"),
            rs.getString("last_name"),
            rs.getString("first_name"),
            rs.getString("sss_number"),
            rs.getString("phil_health_number"),
            rs.getString("tin"),
            rs.getString("pag_ibig_number"),
            rs.getString("email"),
            rs.getString("position"),
            rs.getString("status"),
            rs.getString("address"),
            rs.getString("phone")
        );
    }

    /** [ABSTRACTION] [INHERITANCE] Binds one Employee onto the INSERT parameters. */
    @Override
    protected void bindInsert(PreparedStatement ps, Employee emp) throws SQLException {
        ps.setString(1, emp.getEmployeeNumber());
        ps.setString(2, emp.getLastName());
        ps.setString(3, emp.getFirstName());
        ps.setString(4, emp.getSssNumber());
        ps.setString(5, emp.getPhilHealthNumber());
        ps.setString(6, emp.getTin());
        ps.setString(7, emp.getPagIbigNumber());
        ps.setString(8, emp.getEmail());
        ps.setString(9, emp.getPosition());
        ps.setString(10, emp.getStatus());
        ps.setString(11, emp.getAddress());
        ps.setString(12, emp.getPhone());
    }

    /** [INTERFACE] Implements IEmployeeRepository.load. [INHERITANCE] Delegates to AbstractJdbcRepository.loadAll. */
    @Override
    public List<Employee> load() {
        return loadAll();
    }

    /** [INTERFACE] Implements IEmployeeRepository.save. [INHERITANCE] Delegates to AbstractJdbcRepository.replaceAll. */
    @Override
    public void save(List<Employee> employees) {
        replaceAll(employees);
    }
}
