package repository;

import model.AttendanceRecord;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC persistence for attendance records (MySQL table: attendance_records). Load/save only; no business logic.
 * [INTERFACE] Implements IAttendanceRepository.
 * [INHERITANCE] Extends AbstractJdbcRepository for shared JDBC load/save; provides AttendanceRecord-specific SQL and mapping.
 * [POLYMORPHISM] Can be used as IAttendanceRepository by callers.
 */
public class AttendanceJdbcRepository extends AbstractJdbcRepository<AttendanceRecord> implements IAttendanceRepository {

    /** [ABSTRACTION] [INHERITANCE] Returns the attendance_records table name. */
    @Override
    protected String getTableName() {
        return "attendance_records";
    }

    /** [ABSTRACTION] [INHERITANCE] Returns the parameterized INSERT for one attendance row. */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO attendance_records (employee_id, record_date, status, time_in, time_out) "
            + "VALUES (?,?,?,?,?)";
    }

    /** [ABSTRACTION] [INHERITANCE] Maps one result row to an AttendanceRecord (DATE -> yyyy-MM-dd string). */
    @Override
    protected AttendanceRecord mapRow(ResultSet rs) throws SQLException {
        Date date = rs.getDate("record_date");
        return new AttendanceRecord(
            rs.getString("employee_id"),
            date != null ? date.toString() : "",
            rs.getString("status"),
            rs.getString("time_in"),
            rs.getString("time_out")
        );
    }

    /** [ABSTRACTION] [INHERITANCE] Binds one AttendanceRecord onto the INSERT parameters (yyyy-MM-dd string -> DATE). */
    @Override
    protected void bindInsert(PreparedStatement ps, AttendanceRecord r) throws SQLException {
        ps.setString(1, r.getEmployeeId());
        try {
            ps.setDate(2, Date.valueOf(r.getDate()));
        } catch (IllegalArgumentException e) {
            throw new SQLException("Invalid attendance date: " + r.getDate(), e);
        }
        ps.setString(3, r.getStatus());
        ps.setString(4, r.getTimeIn());
        ps.setString(5, r.getTimeOut());
    }

    /** [INTERFACE] Implements IAttendanceRepository.load. [INHERITANCE] Delegates to AbstractJdbcRepository.loadAll. */
    @Override
    public List<AttendanceRecord> load() {
        return loadAll();
    }

    /** [INTERFACE] Implements IAttendanceRepository.save. [INHERITANCE] Delegates to AbstractJdbcRepository.replaceAll. */
    @Override
    public void save(List<AttendanceRecord> records) {
        replaceAll(records);
    }
}
