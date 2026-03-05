package service;

import model.LeaveRequest;
import repository.LeaveRequestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages leave requests; persistence delegated to LeaveRequestRepository (OOP redesign - GEAR.HR).
 */
public class LeaveService {
    private final LeaveRequestRepository repository;
    private final List<LeaveRequest> leaveRequests = new ArrayList<>();

    public LeaveService(LeaveRequestRepository repository) {
        this.repository = repository;
        leaveRequests.addAll(repository.load());
    }

    public void loadLeaveRequestsFromCSV() {
        leaveRequests.clear();
        leaveRequests.addAll(repository.load());
    }

    private void save() {
        repository.save(new ArrayList<>(leaveRequests));
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    public List<LeaveRequest> getLeaveRequestsByEmployee(String employeeId) {
        return leaveRequests.stream()
            .filter(lr -> employeeId != null && employeeId.equals(lr.getEmployeeId()))
            .collect(Collectors.toList());
    }

    public void addLeaveRequest(LeaveRequest request) {
        if (request != null && request.isValidDateRange()) {
            leaveRequests.add(request);
            save();
        }
    }

    public void updateLeaveRequestStatus(String employeeId, LocalDate startDate, String newStatus) {
        for (LeaveRequest lr : leaveRequests) {
            if (employeeId.equals(lr.getEmployeeId()) && startDate.equals(lr.getStartDate())) {
                lr.setStatus(newStatus);
                save();
                return;
            }
        }
    }
}
