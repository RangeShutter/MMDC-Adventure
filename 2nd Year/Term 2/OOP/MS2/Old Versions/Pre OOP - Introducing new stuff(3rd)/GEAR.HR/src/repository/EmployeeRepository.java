package repository;

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
 * CSV persistence for employees (100employees.csv). Load/save only; no business logic.
 */
public class EmployeeRepository {
    private static final String FILE = "100employees.csv";
    private static final String HEADER = "EmployeeNumber,LastName,FirstName,SSS,PhilHealth,TIN,PagIBIG,Email,Position,Address,Phone";

    public List<Employee> load() {
        List<Employee> list = new ArrayList<>();
        if (!Files.exists(Paths.get(FILE))) return list;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (data.length >= 11) {
                    list.add(new Employee(
                        data[0], data[1], data[2], data[3], data[4],
                        data[5], data[6], data[7], data[8], data[9], data[10]
                    ));
                }
            }
        } catch (IOException e) {
            // return empty
        }
        return list;
    }

    public void save(List<Employee> employees) {
        if (employees == null) return;
        try (PrintWriter w = new PrintWriter(new FileWriter(FILE))) {
            w.println(HEADER);
            for (Employee emp : employees) {
                w.println(String.join(",",
                    emp.getEmployeeNumber(), emp.getLastName(), emp.getFirstName(),
                    emp.getSssNumber(), emp.getPhilHealthNumber(), emp.getTin(),
                    emp.getPagIbigNumber(), emp.getEmail(), emp.getPosition(),
                    emp.getAddress(), emp.getPhone()
                ));
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
