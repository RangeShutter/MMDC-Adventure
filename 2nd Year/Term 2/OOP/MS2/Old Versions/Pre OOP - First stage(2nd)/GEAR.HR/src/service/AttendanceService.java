package service;

import model.AttendanceRecord;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages attendance records and attendance_records.csv persistence (OOP redesign - GEAR.HR).
 */
public class AttendanceService {
    private static final String ATTENDANCE_CSV_FILE = "attendance_records.csv";
    private static final String HEADER = "EmployeeID,Date,Status,TimeIn,TimeOut";

    private final Map<String, AttendanceRecord> attendanceRecords = new LinkedHashMap<>();

    public AttendanceService() {
        loadAttendanceRecordsFromCSV();
    }

    public void loadAttendanceRecordsFromCSV() {
        attendanceRecords.clear();
        if (!Files.exists(Paths.get(ATTENDANCE_CSV_FILE))) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ATTENDANCE_CSV_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 5) {
                    String key = data[0] + "|" + data[1];
                    attendanceRecords.put(key, new AttendanceRecord(
                        data[0], data[1], data[2], data[3], data[4]
                    ));
                }
            }
        } catch (IOException e) {
            // leave map empty
        }
    }

    public void saveAttendanceRecordsToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_CSV_FILE))) {
            writer.println(HEADER);
            for (AttendanceRecord record : attendanceRecords.values()) {
                writer.println(String.join(",",
                    record.getEmployeeId(),
                    record.getDate(),
                    record.getStatus(),
                    record.getTimeIn(),
                    record.getTimeOut()
                ));
            }
        } catch (IOException e) {
            // ignore
        }
    }

    public List<AttendanceRecord> getAllRecords() {
        return new ArrayList<>(attendanceRecords.values());
    }

    public boolean hasRecord(String employeeId, String date) {
        return attendanceRecords.containsKey(employeeId + "|" + date);
    }

    public void addRecord(AttendanceRecord record) {
        if (record != null) {
            String key = record.getEmployeeId() + "|" + record.getDate();
            attendanceRecords.put(key, record);
            saveAttendanceRecordsToCSV();
        }
    }

    public void removeAttendanceRecords(String employeeId) {
        attendanceRecords.entrySet().removeIf(entry -> entry.getKey().startsWith(employeeId + "|"));
        saveAttendanceRecordsToCSV();
    }

    public void clearAll() {
        attendanceRecords.clear();
        saveAttendanceRecordsToCSV();
    }
}
