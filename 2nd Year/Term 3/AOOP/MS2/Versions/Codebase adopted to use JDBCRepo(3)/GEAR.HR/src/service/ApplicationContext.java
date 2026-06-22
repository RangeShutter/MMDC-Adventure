package service;

import repository.IAttendanceRepository;
import repository.IEmployeeRepository;
import repository.ILeaveRequestRepository;
import repository.IPayrollRepository;
import repository.IUserCredentialRepository;
import repository.IItTicketRepository;
import repository.AttendanceRepository;
import repository.EmployeeRepository;
import repository.LeaveRequestRepository;
import repository.PayrollRepository;
import repository.UserCredentialRepository;
import repository.AttendanceJdbcRepository;
import repository.DatabaseConnectionManager;
import repository.EmployeeJdbcRepository;
import repository.ItTicketJdbcRepository;
import repository.LeaveRequestJdbcRepository;
import repository.PayrollJdbcRepository;
import repository.UserCredentialJdbcRepository;
import model.ItTicket;
import java.util.ArrayList;
import java.util.List;

/**
 * Composition root: creates repositories and services; passed into UI (DI).
 * [POLYMORPHISM] Stores and exposes interface types so callers can depend on abstractions.
 * Storage backend is selected once at startup from database.properties:
 * "jdbc" (default) wires MySQL repositories; "csv" wires the legacy CSV repositories.
 */
public class ApplicationContext {
    /** [ENCAPSULATION] Backend choice resolved once; the rest of the app never asks again. */
    private static final boolean USE_JDBC = !"csv".equals(DatabaseConnectionManager.getStorageMode());

    /** [POLYMORPHISM] Concrete instances typed as repository interfaces. */
    private final IEmployeeRepository employeeRepository = createEmployeeRepository();
    private final IAttendanceRepository attendanceRepository = createAttendanceRepository();
    private final IPayrollRepository payrollRepository = createPayrollRepository();
    private final ILeaveRequestRepository leaveRequestRepository = createLeaveRequestRepository();
    private final IUserCredentialRepository userCredentialRepository = createUserCredentialRepository();
    private final IItTicketRepository itTicketRepository = createItTicketRepository();

    /** [POLYMORPHISM] Auth reads credentials through the same repository abstraction (JDBC or CSV). */
    private final IAuthenticationService authenticationService = new AuthenticationService(userCredentialRepository);
    /** [POLYMORPHISM] Services typed as service interfaces. Credential service before employee service for DI. */
    private final IUserCredentialService userCredentialService = new UserCredentialService(userCredentialRepository, authenticationService);
    private final IEmployeeService employeeService = new EmployeeService(employeeRepository, userCredentialService);
    private final IAttendanceService attendanceService = new AttendanceService(attendanceRepository);
    private final PayrollProcessor payrollProcessor = new PayrollProcessor(payrollRepository, attendanceService);
    private final ILeaveService leaveService = new LeaveService(leaveRequestRepository);
    private final IItTicketService itTicketService = new ItTicketService(itTicketRepository, authenticationService);

    /** [INTERFACE][POLYMORPHISM] Exposes authentication via interface contract. */
    public IAuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /** [POLYMORPHISM] Exposes IEmployeeService so callers can use abstraction. */
    public IEmployeeService getEmployeeService() {
        return employeeService;
    }

    /** [POLYMORPHISM] Exposes IAttendanceService so callers can use abstraction. */
    public IAttendanceService getAttendanceService() {
        return attendanceService;
    }

    /** [POLYMORPHISM] Exposes PayrollProcessor for payroll UI and services. */
    public PayrollProcessor getPayrollProcessor() {
        return payrollProcessor;
    }

    /** [POLYMORPHISM] Exposes ILeaveService so callers can use abstraction. */
    public ILeaveService getLeaveService() {
        return leaveService;
    }

    /** [POLYMORPHISM] Exposes IUserCredentialService so callers can use abstraction. */
    public IUserCredentialService getUserCredentialService() {
        return userCredentialService;
    }

    /** [POLYMORPHISM] Exposes IItTicketService so callers can use abstraction. */
    public IItTicketService getItTicketService() {
        return itTicketService;
    }

    /** [ABSTRACTION] [POLYMORPHISM] Factory: JDBC (default) or legacy CSV employee repository. */
    private static IEmployeeRepository createEmployeeRepository() {
        return USE_JDBC ? new EmployeeJdbcRepository() : new EmployeeRepository();
    }

    /** [ABSTRACTION] [POLYMORPHISM] Factory: JDBC (default) or legacy CSV attendance repository. */
    private static IAttendanceRepository createAttendanceRepository() {
        return USE_JDBC ? new AttendanceJdbcRepository() : new AttendanceRepository();
    }

    /** [ABSTRACTION] [POLYMORPHISM] Factory: JDBC (default) or legacy CSV payroll repository. */
    private static IPayrollRepository createPayrollRepository() {
        return USE_JDBC ? new PayrollJdbcRepository() : new PayrollRepository();
    }

    /** [ABSTRACTION] [POLYMORPHISM] Factory: JDBC (default) or legacy CSV leave request repository. */
    private static ILeaveRequestRepository createLeaveRequestRepository() {
        return USE_JDBC ? new LeaveRequestJdbcRepository() : new LeaveRequestRepository();
    }

    /** [ABSTRACTION] [POLYMORPHISM] Factory: JDBC (default) or legacy CSV user credential repository. */
    private static IUserCredentialRepository createUserCredentialRepository() {
        return USE_JDBC ? new UserCredentialJdbcRepository() : new UserCredentialRepository();
    }

    /**
     * [ABSTRACTION] Factory: JDBC (default) or legacy CSV ticket repository, resolved
     * without hard compile dependency on the CSV class.
     * [POLYMORPHISM] Fallback is an anonymous {@link IItTicketRepository} (empty load, no-op save).
     */
    private static IItTicketRepository createItTicketRepository() {
        if (USE_JDBC) {
            return new ItTicketJdbcRepository();
        }
        try {
            Class<?> repoClass = Class.forName("repository.ItTicketRepository");
            Object instance = repoClass.getDeclaredConstructor().newInstance();
            if (instance instanceof IItTicketRepository) {
                return (IItTicketRepository) instance;
            }
        } catch (Exception ignored) {
            // fallback below
        }
        return new IItTicketRepository() {
            /** [INTERFACE] Implements IItTicketRepository.load (no-op empty list). */
            @Override
            public List<ItTicket> load() {
                return new ArrayList<>();
            }

            /** [INTERFACE] Implements IItTicketRepository.save (no-op). */
            @Override
            public void save(List<ItTicket> items) {
                // no-op fallback
            }
        };
    }
}
