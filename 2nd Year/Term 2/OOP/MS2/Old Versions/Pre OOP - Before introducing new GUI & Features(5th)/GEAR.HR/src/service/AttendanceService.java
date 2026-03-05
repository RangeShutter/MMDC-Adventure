package service;

import model.AttendanceRecord;
import repository.AttendanceRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages attendance records; persistence delegated to AttendanceRepository (OOP redesign - GEAR.HR).
 */
public class AttendanceService {
    private final AttendanceRepository repository;
    private final Map<String, AttendanceRecord> attendanceRecords = new LinkedHashMap<>();

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
        for (AttendanceRecord r : repository.load()) {
            String key = r.getEmployeeId() + "|" + r.getDate();
            attendanceRecords.put(key, r);
        }
    }

    public void loadAttendanceRecordsFromCSV() {
        attendanceRecords.clear();
        for (AttendanceRecord r : repository.load()) {
            String key = r.getEmployeeId() + "|" + r.getDate();
            attendanceRecords.put(key, r);
        }
    }

    private void save() {
        repository.save(new ArrayList<>(attendanceRecords.values()));
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
            save();
        }
    }

    public void removeAttendanceRecords(String employeeId) {
        attendanceRecords.entrySet().removeIf(entry -> entry.getKey().startsWith(employeeId + "|"));
        save();
    }

    public void clearAll() {
        attendanceRecords.clear();
        save();
    }
}
