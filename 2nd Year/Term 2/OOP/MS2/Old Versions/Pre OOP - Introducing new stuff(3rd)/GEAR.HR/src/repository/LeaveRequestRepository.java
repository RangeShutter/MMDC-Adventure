package repository;

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

/**
 * CSV persistence for leave requests. Load/save only; no business logic.
 */
public class LeaveRequestRepository {
    private static final String FILE = "leave_requests.csv";
    private static final String HEADER = "EmployeeID,StartDate,EndDate,Reason,Status";

    public List<LeaveRequest> load() {
        List<LeaveRequest> list = new ArrayList<>();
        if (!Files.exists(Paths.get(FILE))) return list;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 5) {
                    LocalDate start = LeaveRequest.parseDate(data[1]);
                    LocalDate end = LeaveRequest.parseDate(data[2]);
                    if (start != null && end != null) {
                        list.add(new LeaveRequest(
                            data[0].trim(), start, end, data[3].trim(), data[4].trim()
                        ));
                    }
                }
            }
        } catch (IOException e) {
            // return empty
        }
        return list;
    }

    public void save(List<LeaveRequest> requests) {
        if (requests == null) return;
        try (PrintWriter w = new PrintWriter(new FileWriter(FILE))) {
            w.println(HEADER);
            for (LeaveRequest lr : requests) {
                w.println(String.join(",",
                    lr.getEmployeeId(),
                    lr.getStartDate() != null ? lr.getStartDate().toString() : "",
                    lr.getEndDate() != null ? lr.getEndDate().toString() : "",
                    lr.getReason(),
                    lr.getStatus()
                ));
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
