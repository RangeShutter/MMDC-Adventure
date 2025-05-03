/**
 * This code is made by Ransel Sumatra.
 * GitHub: https://github.com/ranselcrackers/
 * GitHub Username: ranselcrackers
 * Specialization: BSIT Major in Marketing Technologies at Mapúa Malayan Colleges Laguna
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class initializes employee data, including hours worked.
 */
public class EmployeeDataInitializer {
    public static List<Employee> initializeEmployees() {
        List<Employee> employees = new ArrayList<>();

        // Add employee data
        employees.add(createEmployee(
            10001, "Garcia", "Manuel III", "password123", 
            "Chief Executive Officer", 535.71, 
            new double[][]{{42.5, 2.5}, {43.5, 3.5}, {43.5, 3.5}, {43.5, 3.5}}, 
            "44-4506057-3", "820126853951", "442-605-657-000", "691295330870"
        ));

        employees.add(createEmployee(
            10002, "Lim", "Antonio", "password123", 
            "Software Developer", 500.00, 
            new double[][]{{46.5, 6.5}, {45.5, 5.5}, {45.5, 5.5}, {45.5, 5.5}}, 
            "123456789", "123456789012", "123-456-789-000", "123456789012"
        ));

        // Add the remaining 32 employees similarly...

        return employees;
    }

    private static Employee createEmployee(int empId, String lastName, String firstName, 
                                          String password, String position, double hourlyRate, 
                                          double[][] juneHours, String sssNumber, 
                                          String philhealthNumber, String tinNumber, 
                                          String pagibigNumber) {
        HoursData hoursData = new HoursData(empId, 40); // Assumed regular hours: 40

        // Add June hours
        for (int week = 0; week < juneHours.length; week++) {
            hoursData.addHours("June", week + 1, juneHours[week][0], juneHours[week][1]);
        }

        // Add placeholder data for other months
        String[] otherMonths = {"July", "August", "September", "October", "November", "December"};
        for (String month : otherMonths) {
            for (int week = 1; week <= 4; week++) {
                hoursData.addHours(month, week, 0.0, 0.0); // Placeholder data
            }
        }

        return new Employee(empId, lastName, firstName, password, position, hourlyRate, 
                           hoursData, sssNumber, philhealthNumber, tinNumber, pagibigNumber);
    }

    public static Employee getEmployee(int empId, List<Employee> employees) {
        for (Employee emp : employees) {
            if (emp.getEmployeeNumber() == empId) {
                return emp;
            }
        }
        return null; // Employee not found
    }
}