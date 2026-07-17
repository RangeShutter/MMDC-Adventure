package repository;

import model.LeaveRequest;

import java.util.List;

/**
 * [INTERFACE] JDBC persistence contract for leave requests.
 * Production implementation: {@link LeaveRequestJdbcRepository} (MySQL table {@code leave_requests}).
 * Alternative implementations may be supplied for unit tests.
 */
public interface ILeaveRequestRepository {
    /**
     * [INTERFACE] Loads all leave requests from the database via JDBC.
     * @return list of requests (empty if none or on JDBC error)
     */
    List<LeaveRequest> load();

    /**
     * [INTERFACE] Persists leave requests to the database via JDBC (full table replace).
     * @param requests list to save (null is ignored)
     */
    void save(List<LeaveRequest> requests);
}
