package repository;

import model.LeaveRequest;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC persistence for leave requests (MySQL table: leave_requests). Load/save only; no business logic.
 * [INTERFACE] Implements ILeaveRequestRepository.
 * [INHERITANCE] Extends AbstractJdbcRepository for shared JDBC load/save; provides LeaveRequest-specific SQL and mapping.
 * [POLYMORPHISM] Can be used as ILeaveRequestRepository by callers.
 */
public class LeaveRequestJdbcRepository extends AbstractJdbcRepository<LeaveRequest> implements ILeaveRequestRepository {

    /** [ABSTRACTION] [INHERITANCE] Returns the leave_requests table name. */
    @Override
    protected String getTableName() {
        return "leave_requests";
    }

    /** [ABSTRACTION] [INHERITANCE] Returns the parameterized INSERT for one leave request row. */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO leave_requests (employee_id, start_date, end_date, reason, status) "
            + "VALUES (?,?,?,?,?)";
    }

    /** [ABSTRACTION] [INHERITANCE] Maps one result row to a LeaveRequest (DATE -> LocalDate). */
    @Override
    protected LeaveRequest mapRow(ResultSet rs) throws SQLException {
        Date start = rs.getDate("start_date");
        Date end = rs.getDate("end_date");
        if (start == null || end == null) return null;
        return new LeaveRequest(
            rs.getString("employee_id"),
            start.toLocalDate(),
            end.toLocalDate(),
            rs.getString("reason"),
            rs.getString("status")
        );
    }

    /** [ABSTRACTION] [INHERITANCE] Binds one LeaveRequest onto the INSERT parameters (LocalDate -> DATE). */
    @Override
    protected void bindInsert(PreparedStatement ps, LeaveRequest lr) throws SQLException {
        if (lr.getStartDate() == null || lr.getEndDate() == null) {
            throw new SQLException("Leave request dates must not be null for employee " + lr.getEmployeeId());
        }
        ps.setString(1, lr.getEmployeeId());
        ps.setDate(2, Date.valueOf(lr.getStartDate()));
        ps.setDate(3, Date.valueOf(lr.getEndDate()));
        ps.setString(4, lr.getReason());
        ps.setString(5, lr.getStatus());
    }

    /** [INTERFACE] Implements ILeaveRequestRepository.load. [INHERITANCE] Delegates to AbstractJdbcRepository.loadAll. */
    @Override
    public List<LeaveRequest> load() {
        return loadAll();
    }

    /** [INTERFACE] Implements ILeaveRequestRepository.save. [INHERITANCE] Delegates to AbstractJdbcRepository.replaceAll. */
    @Override
    public void save(List<LeaveRequest> requests) {
        replaceAll(requests);
    }
}
