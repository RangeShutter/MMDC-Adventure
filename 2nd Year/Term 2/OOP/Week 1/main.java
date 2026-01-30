import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Employee {
    private String id;
    private String name;
    private String position;
    private double basicSalary;

    public Employee(String id, String name, String position, double basicSalary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.basicSalary = basicSalary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public double getBasicSalary() {
        return basicSalary;
    }
}

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = new ArrayList<>();

        int choice;

        do {
            System.out.println("====================================");
            System.out.println("        SIMPLE PAYROLL SYSTEM       ");
            System.out.println("====================================");
            System.out.println("1. Add Employee Payroll Info");
            System.out.println("2. View All Payroll Records");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            // Handle non-integer menu input safely
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number (1-3): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    System.out.println("\n--- Add Employee Payroll Info ---");

                    System.out.print("Enter Employee ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Enter Employee Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Position: ");
                    String position = scanner.nextLine();

                    System.out.print("Enter Basic Salary: ");
                    double basicSalary;

                    // Handle non-number salary safely
                    while (!scanner.hasNextDouble()) {
                        System.out.print("Invalid amount. Enter Basic Salary (number): ");
                        scanner.next();
                    }
                    basicSalary = scanner.nextDouble();
                    scanner.nextLine(); // consume leftover newline

                    Employee emp = new Employee(id, name, position, basicSalary);
                    employees.add(emp);

                    System.out.println("Employee payroll information saved!\n");
                    break;

                case 2:
                    System.out.println("\n--- Payroll Records ---");
                    if (employees.isEmpty()) {
                        System.out.println("No employee records found.\n");
                    } else {
                        System.out.printf("%-10s %-20s %-15s %-15s%n",
                                "ID", "Name", "Position", "Basic Salary");
                        System.out.println("-------------------------------------------------------------");
                        for (Employee e : employees) {
                            System.out.printf("%-10s %-20s %-15s %-15.2f%n",
                                    e.getId(), e.getName(), e.getPosition(), e.getBasicSalary());
                        }
                        System.out.println();
                    }
                    break;

                case 3:
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1-3.\n");
            }

        } while (choice != 3);

        scanner.close();
    }
}

