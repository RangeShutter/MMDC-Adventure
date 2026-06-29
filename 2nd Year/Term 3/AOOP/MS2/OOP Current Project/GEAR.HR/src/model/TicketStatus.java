package model;

/**
 * Fixed set of IT support ticket states (OOP redesign - GEAR.HR).
 * [ENCAPSULATION] Type-safe replacement for the previous String status constants.
 */
public enum TicketStatus {
    PENDING("Pending"),
    RESOLVED("Resolved");

    private final String label;

    TicketStatus(String label) {
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
    public static TicketStatus fromString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        for (TicketStatus status : values()) {
            if (status.label.equalsIgnoreCase(trimmed) || status.name().equalsIgnoreCase(trimmed)) {
                return status;
            }
        }
        return null;
    }

    /** Tolerant parse used at storage boundaries: falls back to {@code def} on unknown input. */
    public static TicketStatus fromStringOrDefault(String value, TicketStatus def) {
        TicketStatus status = fromString(value);
        return status != null ? status : def;
    }

    /** Labels in declaration order; convenient for populating combo boxes. */
    public static String[] labels() {
        TicketStatus[] values = values();
        String[] out = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = values[i].label;
        }
        return out;
    }
}
