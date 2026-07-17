import static org.junit.Assert.assertEquals;

import org.junit.Test;
import service.RoleGroup;

/**
 * Unit tests for {@link RoleGroup#fromRole(String)} role-to-department classification.
 */
public class RoleGroupTest {

    @Test
    public void hrRolesMapToHrGroup() {
        assertEquals(RoleGroup.HR, RoleGroup.fromRole("HR Manager"));
        assertEquals(RoleGroup.HR, RoleGroup.fromRole("HR Rank and File"));
    }

    @Test
    public void payrollAndAccountRolesMapToPayrollGroup() {
        assertEquals(RoleGroup.PAYROLL, RoleGroup.fromRole("Payroll Manager"));
        assertEquals(RoleGroup.PAYROLL, RoleGroup.fromRole("Account Rank and File"));
        assertEquals(RoleGroup.PAYROLL, RoleGroup.fromRole("Accounting Head"));
    }

    @Test
    public void itRolesMapToItAdminGroup() {
        assertEquals(RoleGroup.IT_ADMIN, RoleGroup.fromRole("IT"));
        assertEquals(RoleGroup.IT_ADMIN, RoleGroup.fromRole("IT Operations and Systems"));
    }

    @Test
    public void unknownAndNullRolesMapToNormal() {
        assertEquals(RoleGroup.NORMAL, RoleGroup.fromRole("Chief Executive Officer"));
        assertEquals(RoleGroup.NORMAL, RoleGroup.fromRole("Developer"));
        assertEquals(RoleGroup.NORMAL, RoleGroup.fromRole(null));
    }

    @Test
    public void roleMatchingTrimsWhitespace() {
        assertEquals(RoleGroup.HR, RoleGroup.fromRole("  HR Manager  "));
    }
}
