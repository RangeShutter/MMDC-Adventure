package service;

import repository.AttendanceRepository;
import repository.EmployeeRepository;
import repository.LeaveRequestRepository;
import repository.PayrollRepository;

/**
 * Composition root: creates repositories and services; passed into UI (DI).
 */
public class ApplicationContext {
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final AttendanceRepository attendanceRepository = new AttendanceRepository();
    private final PayrollRepository payrollRepository = new PayrollRepository();
    private final LeaveRequestRepository leaveRequestRepository = new LeaveRequestRepository();

    private final AuthenticationService authenticationService = new AuthenticationService();
    private final EmployeeService employeeService = new EmployeeService(employeeRepository);
    private final AttendanceService attendanceService = new AttendanceService(attendanceRepository);
    private final PayrollProcessor payrollProcessor = new PayrollProcessor(payrollRepository);
    private final LeaveService leaveService = new LeaveService(leaveRequestRepository);

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public AttendanceService getAttendanceService() {
        return attendanceService;
    }

    public PayrollProcessor getPayrollProcessor() {
        return payrollProcessor;
    }

    public LeaveService getLeaveService() {
        return leaveService;
    }
}
