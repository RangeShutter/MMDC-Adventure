package model;

/**
 * Fixed set of employment states (OOP redesign - GEAR.HR).
 * [ENCAPSULATION] Type-safe replacement for the free-text employee status string.
 * Labels are intentionally lower-case to match the existing data and validation rules.
 */
public enum EmploymentStatus {
    REGULAR("regular"),
    PROBATIONARY("probationary");

    private final String label;

    EmploymentStatus(String label) {
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
    public static EmploymentStatus fromString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        for (EmploymentStatus status : values()) {
            if (status.label.equalsIgnoreCase(trimmed) || status.name().equalsIgnoreCase(trimmed)) {
                return status;
            }
        }
        return null;
    }

    /** Tolerant parse used at storage boundaries: falls back to {@code def} on unknown input. */
    public static EmploymentStatus fromStringOrDefault(String value, EmploymentStatus def) {
        EmploymentStatus status = fromString(value);
        return status != null ? status : def;
    }

    /** Labels in declaration order; convenient for populating combo boxes. */
    public static String[] labels() {
        EmploymentStatus[] values = values();
        String[] out = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = values[i].label;
        }
        return out;
    }
}
