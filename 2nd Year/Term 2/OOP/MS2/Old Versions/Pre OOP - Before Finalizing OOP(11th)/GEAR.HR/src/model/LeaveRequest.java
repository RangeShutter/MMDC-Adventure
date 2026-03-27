package model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Domain model for a leave request (OOP redesign - GEAR.HR).
 * [INHERITANCE] Child class extends AbstractEntity.
 * Validates start date precedes end date and status is in allowed set.
 */
public class LeaveRequest extends AbstractEntity {
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_REJECTED = "Rejected";

    private static final String[] ALLOWED_STATUSES = { STATUS_PENDING, STATUS_APPROVED, STATUS_REJECTED };

    private String employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;

    public LeaveRequest(String employeeId, LocalDate startDate, LocalDate endDate, String reason, String status) {
        super(employeeId != null ? employeeId : "");
        this.employeeId = employeeId != null ? employeeId : "";
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason != null ? reason : "";
        setStatus(status);
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId != null ? employeeId : this.employeeId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason != null ? reason : this.reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        setStatus(status, false);
    }

    /** [POLYMORPHISM - Overloading] Same method name, different parameter: skipValidation bypasses allowed-status check when true. */
    public void setStatus(String status, boolean skipValidation) {
        if (skipValidation && status != null && !status.trim().isEmpty()) {
            this.status = status.trim();
            return;
        }
        if (status != null) {
            for (String s : ALLOWED_STATUSES) {
                if (s.equalsIgnoreCase(status)) {
                    this.status = s;
                    return;
                }
            }
        }
        this.status = STATUS_PENDING;
    }

    /** [INTERFACE] Validatable: delegates to isValidDateRange(). */
    @Override
    public boolean isValid() {
        return isValidDateRange();
    }

    /** Returns true if start date is before or equal to end date. */
    public boolean isValidDateRange() {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    /** Parse date from yyyy-MM-dd; returns null on failure. */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
