package repository;

import model.AttendanceRecord;

import java.util.List;

/**
 * [INTERFACE] JDBC persistence contract for attendance records.
 * Production implementation: {@link AttendanceJdbcRepository} (MySQL table {@code attendance_records}).
 * Alternative implementations may be supplied for unit tests.
 */
public interface IAttendanceRepository {
    /**
     * [INTERFACE] Loads all attendance records from the database via JDBC.
     * @return list of records (empty if none or on JDBC error)
     */
    List<AttendanceRecord> load();

    /**
     * [INTERFACE] Persists attendance records to the database via JDBC (full table replace).
     * @param records list to save (null is ignored)
     */
    void save(List<AttendanceRecord> records);
}
