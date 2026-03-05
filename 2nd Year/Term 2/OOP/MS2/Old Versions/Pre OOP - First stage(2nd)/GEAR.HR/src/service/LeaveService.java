package service;

import model.LeaveRequest;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages leave requests and leave_requests.csv persistence (OOP redesign - GEAR.HR).
 */
public class LeaveService {
    private static final String LEAVE_CSV_FILE = "leave_requests.csv";
    private static final String HEADER = "EmployeeID,StartDate,EndDate,Reason,Status";

    private final List<LeaveRequest> leaveRequests = new ArrayList<>();

    public LeaveService() {
        loadLeaveRequestsFromCSV();
    }

    public void loadLeaveRequestsFromCSV() {
        leaveRequests.clear();
        if (!Files.exists(Paths.get(LEAVE_CSV_FILE))) return;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(LEAVE_CSV_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 5) {
                    LocalDate start = LeaveRequest.parseDate(data[1]);
                    LocalDate end = LeaveRequest.parseDate(data[2]);
                    if (start != null && end != null) {
                        leaveRequests.add(new LeaveRequest(
                            data[0].trim(), start, end, data[3].trim(), data[4].trim()
                        ));
                    }
                }
            }
        } catch (IOException e) {
            // leave empty
        }
    }

    public void saveLeaveRequestsToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEAVE_CSV_FILE))) {
            writer.println(HEADER);
            for (LeaveRequest lr : leaveRequests) {
                writer.println(String.join(",",
                    lr.getEmployeeId(),
                    lr.getStartDate().toString(),
                    lr.getEndDate().toString(),
                    lr.getReason(),
                    lr.getStatus()
                ));
            }
        } catch (IOException e) {
            // ignore
        }
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
            saveLeaveRequestsToCSV();
        }
    }

    public void updateLeaveRequestStatus(String employeeId, LocalDate startDate, String newStatus) {
        for (LeaveRequest lr : leaveRequests) {
            if (employeeId.equals(lr.getEmployeeId()) && startDate.equals(lr.getStartDate())) {
                lr.setStatus(newStatus);
                saveLeaveRequestsToCSV();
                return;
            }
        }
    }
}
