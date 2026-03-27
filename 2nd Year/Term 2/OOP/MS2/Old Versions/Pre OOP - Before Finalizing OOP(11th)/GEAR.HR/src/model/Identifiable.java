package model;

/**
 * [INTERFACE] Defines entities that have a unique identifier for persistence and lookup.
 * Implemented by AbstractEntity and its subclasses (Employee, AttendanceRecord, LeaveRequest, PayrollResult).
 */
public interface Identifiable {
    /**
     * Returns the unique identifier of this entity.
     */
    String getId();
}
