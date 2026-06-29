package service;

import repository.IAttendanceRepository;
import repository.IEmployeeRepository;
import repository.ILeaveRequestRepository;
import repository.IPayrollRepository;
import repository.IUserCredentialRepository;
import repository.IItTicketRepository;
import repository.AttendanceJdbcRepository;
import repository.EmployeeJdbcRepository;
import repository.ItTicketJdbcRepository;
import repository.LeaveRequestJdbcRepository;
import repository.PayrollJdbcRepository;
import repository.UserCredentialJdbcRepository;

/**
 * Composition root: creates repositories and services; passed into UI (DI).
 * [POLYMORPHISM] Stores and exposes interface types so callers can depend on abstractions.
 * Persistence is JDBC-only (MySQL); connection settings come from database.properties.
 */
public class ApplicationContext {

    /** [POLYMORPHISM] Concrete JDBC instances typed as repository interfaces. */
    private final IEmployeeRepository employeeRepository = new EmployeeJdbcRepository();
    private final IAttendanceRepository attendanceRepository = new AttendanceJdbcRepository();
    private final IPayrollRepository payrollRepository = new PayrollJdbcRepository();
    private final ILeaveRequestRepository leaveRequestRepository = new LeaveRequestJdbcRepository();
    private final IUserCredentialRepository userCredentialRepository = new UserCredentialJdbcRepository();
    private final IItTicketRepository itTicketRepository = new ItTicketJdbcRepository();

    /** [POLYMORPHISM] Auth reads credentials through the JDBC repository abstraction ({@link UserCredentialJdbcRepository}). */
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
}
