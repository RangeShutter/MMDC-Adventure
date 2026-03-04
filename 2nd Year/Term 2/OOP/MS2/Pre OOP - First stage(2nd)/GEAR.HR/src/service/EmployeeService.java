package service;

import model.Employee;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages employee data and 100employees.csv persistence (OOP redesign - GEAR.HR).
 */
public class EmployeeService {
    private static final String EMPLOYEES_CSV_FILE = "100employees.csv";
    private static final String HEADER = "EmployeeNumber,LastName,FirstName,SSS,PhilHealth,TIN,PagIBIG,Email,Position,Address,Phone";

    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService() {
        loadEmployeesFromCSV();
        if (employees.isEmpty()) {
            addSampleEmployees();
        }
    }

    public void loadEmployeesFromCSV() {
        employees.clear();
        if (!Files.exists(Paths.get(EMPLOYEES_CSV_FILE))) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(EMPLOYEES_CSV_FILE))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (data.length >= 11) {
                    employees.add(new Employee(
                        data[0], data[1], data[2], data[3], data[4],
                        data[5], data[6], data[7], data[8], data[9], data[10]
                    ));
                }
            }
        } catch (IOException e) {
            // leave list empty on error
        }
    }

    public void saveEmployeesToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_CSV_FILE))) {
            writer.println(HEADER);
            for (Employee emp : employees) {
                writer.println(String.join(",",
                    emp.getEmployeeNumber(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getSssNumber(),
                    emp.getPhilHealthNumber(),
                    emp.getTin(),
                    emp.getPagIbigNumber(),
                    emp.getEmail(),
                    emp.getPosition(),
                    emp.getAddress(),
                    emp.getPhone()
                ));
            }
        } catch (IOException e) {
            // ignore
        }
    }

    private void addSampleEmployees() {
        employees.add(new Employee(
            "1001", "Bactong", "Colin", "123456789", "123456789012", "123-456-789", "123456789012",
            "Colin@MotorPH.com", "Developer", "Leyte, Palo", "0960 270 7931"
        ));
        employees.add(new Employee(
            "1002", "Bactong", "Charlize", "987654321", "210987654321", "987-654-321", "210987654321",
            "Charlize@MotorPH.com", "Manager", "Negros Occidental Silay City", "555-0202"
        ));
        saveEmployeesToCSV();
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee findEmployeeById(String empNumber) {
        if (empNumber == null) return null;
        for (Employee emp : employees) {
            if (empNumber.equals(emp.getEmployeeNumber())) return emp;
        }
        return null;
    }

    public void addEmployee(Employee emp) {
        if (emp != null && findEmployeeById(emp.getEmployeeNumber()) == null) {
            employees.add(emp);
            saveEmployeesToCSV();
        }
    }

    public void updateEmployee(Employee emp) {
        if (emp == null) return;
        Employee existing = findEmployeeById(emp.getEmployeeNumber());
        if (existing != null) {
            existing.setLastName(emp.getLastName());
            existing.setFirstName(emp.getFirstName());
            existing.setSssNumber(emp.getSssNumber());
            existing.setPhilHealthNumber(emp.getPhilHealthNumber());
            existing.setTin(emp.getTin());
            existing.setPagIbigNumber(emp.getPagIbigNumber());
            existing.setEmail(emp.getEmail());
            existing.setPosition(emp.getPosition());
            existing.setAddress(emp.getAddress());
            existing.setPhone(emp.getPhone());
            saveEmployeesToCSV();
        }
    }

    public void deleteEmployee(String empNumber) {
        employees.removeIf(e -> empNumber != null && empNumber.equals(e.getEmployeeNumber()));
        saveEmployeesToCSV();
    }
}
