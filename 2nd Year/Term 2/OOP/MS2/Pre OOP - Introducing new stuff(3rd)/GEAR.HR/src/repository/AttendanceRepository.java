package repository;

import model.AttendanceRecord;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV persistence for attendance records. Load/save only; no business logic.
 */
public class AttendanceRepository {
    private static final String FILE = "attendance_records.csv";
    private static final String HEADER = "EmployeeID,Date,Status,TimeIn,TimeOut";

    public List<AttendanceRecord> load() {
        List<AttendanceRecord> list = new ArrayList<>();
        if (!Files.exists(Paths.get(FILE))) return list;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 5) {
                    list.add(new AttendanceRecord(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            // return empty
        }
        return list;
    }

    public void save(List<AttendanceRecord> records) {
        if (records == null) return;
        try (PrintWriter w = new PrintWriter(new FileWriter(FILE))) {
            w.println(HEADER);
            for (AttendanceRecord r : records) {
                w.println(String.join(",",
                    r.getEmployeeId(), r.getDate(), r.getStatus(),
                    r.getTimeIn(), r.getTimeOut()
                ));
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
