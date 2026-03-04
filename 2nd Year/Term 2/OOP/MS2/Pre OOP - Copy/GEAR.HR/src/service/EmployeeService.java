package service;

import model.Employee;
import repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages employee data; persistence delegated to EmployeeRepository (OOP redesign - GEAR.HR).
 */
public class EmployeeService {
    private final EmployeeRepository repository;
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
        List<Employee> loaded = repository.load();
        employees.addAll(loaded);
        if (employees.isEmpty()) {
            addSampleEmployees();
        }
    }

    public void loadEmployeesFromCSV() {
        employees.clear();
        employees.addAll(repository.load());
    }

    public void saveEmployeesToCSV() {
        repository.save(new ArrayList<>(employees));
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
