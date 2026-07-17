package model;

/**
 * Fixed set of leave-request states (OOP redesign - GEAR.HR).
 * [ENCAPSULATION] Replaces loose String constants with a type-safe enum so invalid or
 * misspelled status values cannot enter the system. The {@link #getLabel()} value is the
 * canonical text persisted in the database and shown in the UI.
 */
public enum LeaveStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String label;

    LeaveStatus(String label) {
        this.label = label;
    }

    /** Canonical display/storage text for this status. */
    public String getLabel() {
        return label;
    }

    /** [OVERRIDING] Returns the label so the enum prints cleanly in tables and reports. */
    @Override
    public String toString() {
        return label;
    }

    /**
     * Parses a status from its label or enum name (case-insensitive).
     * @return the matching status, or {@code null} when no match is found
     */
    public static LeaveStatus fromString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        for (LeaveStatus status : values()) {
            if (status.label.equalsIgnoreCase(trimmed) || status.name().equalsIgnoreCase(trimmed)) {
                return status;
            }
        }
        return null;
    }

    /** Tolerant parse used at storage boundaries: falls back to {@code def} on unknown input. */
    public static LeaveStatus fromStringOrDefault(String value, LeaveStatus def) {
        LeaveStatus status = fromString(value);
        return status != null ? status : def;
    }

    /** Labels in declaration order; convenient for populating combo boxes. */
    public static String[] labels() {
        LeaveStatus[] values = values();
        String[] out = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = values[i].label;
        }
        return out;
    }
}
