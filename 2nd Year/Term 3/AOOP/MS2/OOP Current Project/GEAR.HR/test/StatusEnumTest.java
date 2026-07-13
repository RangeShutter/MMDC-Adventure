import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.AttendanceStatus;
import model.EmploymentStatus;
import model.LeaveStatus;
import model.TicketStatus;

/**
 * Unit tests for the status enums: case-insensitive parsing, tolerant defaults,
 * canonical labels, and helper behavior.
 */
public class StatusEnumTest {

    @Test
    public void leaveStatusParsesLabelsCaseInsensitively() {
        assertEquals(LeaveStatus.APPROVED, LeaveStatus.fromString("approved"));
        assertEquals(LeaveStatus.PENDING, LeaveStatus.fromString("PENDING"));
        assertNull(LeaveStatus.fromString("bogus"));
        assertEquals(LeaveStatus.PENDING, LeaveStatus.fromStringOrDefault("bogus", LeaveStatus.PENDING));
        assertEquals("Approved", LeaveStatus.APPROVED.getLabel());
        assertArrayEquals(new String[]{"Pending", "Approved", "Rejected"}, LeaveStatus.labels());
    }

    @Test
    public void ticketStatusParsesAndExposesLabels() {
        assertEquals(TicketStatus.RESOLVED, TicketStatus.fromString("Resolved"));
        assertNull(TicketStatus.fromString("closed"));
        assertArrayEquals(new String[]{"Pending", "Resolved"}, TicketStatus.labels());
    }

    @Test
    public void attendanceStatusParsesMultiWordLabelAndTimeRule() {
        assertEquals(AttendanceStatus.ON_LEAVE, AttendanceStatus.fromString("On Leave"));
        assertTrue(AttendanceStatus.ON_LEAVE.requiresNoTime());
        assertTrue(AttendanceStatus.ABSENT.requiresNoTime());
        assertFalse(AttendanceStatus.PRESENT.requiresNoTime());
        assertFalse(AttendanceStatus.LATE.requiresNoTime());
    }

    @Test
    public void employmentStatusUsesLowercaseLabels() {
        assertEquals(EmploymentStatus.REGULAR, EmploymentStatus.fromString("REGULAR"));
        assertEquals(EmploymentStatus.PROBATIONARY, EmploymentStatus.fromString("probationary"));
        assertEquals("regular", EmploymentStatus.REGULAR.getLabel());
        assertNull(EmploymentStatus.fromString("contractual"));
    }
}
