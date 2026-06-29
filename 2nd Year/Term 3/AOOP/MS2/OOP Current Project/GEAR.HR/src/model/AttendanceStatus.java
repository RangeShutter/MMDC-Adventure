package model;

/**
 * Fixed set of daily attendance states (OOP redesign - GEAR.HR).
 * [ENCAPSULATION] Type-safe replacement for the raw status strings used by the attendance UI.
 */
public enum AttendanceStatus {
    PRESENT("Present"),
    ABSENT("Absent"),
    LATE("Late"),
    ON_LEAVE("On Leave"),
    HALF_DAY("Half Day");

    private final String label;

    AttendanceStatus(String label) {
        this.label = label;
    }

    /** Canonical display/storage text for this status. */
    public String getLabel() {
        return label;
    }

    /** True when this status does not require time-in/time-out values. */
    public boolean requiresNoTime() {
        return this == ABSENT || this == ON_LEAVE;
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
    public static AttendanceStatus fromString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        for (AttendanceStatus status : values()) {
            if (status.label.equalsIgnoreCase(trimmed) || status.name().equalsIgnoreCase(trimmed)) {
                return status;
            }
        }
        return null;
    }

    /** Tolerant parse used at storage boundaries: falls back to {@code def} on unknown input. */
    public static AttendanceStatus fromStringOrDefault(String value, AttendanceStatus def) {
        AttendanceStatus status = fromString(value);
        return status != null ? status : def;
    }

    /** Labels in declaration order; convenient for populating combo boxes. */
    public static String[] labels() {
        AttendanceStatus[] values = values();
        String[] out = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = values[i].label;
        }
        return out;
    }
}
