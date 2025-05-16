
import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeDataBase {
    private ArrayList<String> employees = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        EmployeeDataBase manager = new EmployeeDataBase();
        manager.runMenu();
    }

    public void runMenu() {
        while (true) {
            System.out.println("\nEmployee List Manager");
            System.out.println("1. Add Employee");
            System.out.println("2. Remove Employee");
            System.out.println("3. Find Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. View All Employees");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    removeEmployee();
                    break;
                case 3:
                    findEmployee();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    viewAllEmployees();
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
        
    }
    private void addEmployee() {
        System.out.print("Enter employee name to add: ");
        String name = scanner.nextLine();
        employees.add(name);
        System.out.println(name + " added successfully.");
    }

    private void removeEmployee() {
        System.out.print("Enter employee name to remove: ");
        String name = scanner.nextLine();
        if (employees.remove(name)) {
            System.out.println(name + " removed successfully.");
        } else {
            System.out.println(name + " not found in the list.");
        }
    }

    private void findEmployee() {
        System.out.print("Enter employee name to find: ");
        String name = scanner.nextLine();
        if (employees.contains(name)) {
            System.out.println(name + " is in the list at position: " + employees.indexOf(name));
        } else {
            System.out.println(name + " not found in the list.");
        }
    }

    private void updateEmployee() {
        System.out.print("Enter current employee name: ");
        String oldName = scanner.nextLine();
        if (employees.contains(oldName)) {
            System.out.print("Enter new name: ");
            String newName = scanner.nextLine();
            int index = employees.indexOf(oldName);
            employees.set(index, newName);
            System.out.println("Updated " + oldName + " to " + newName);
        } else {
            System.out.println(oldName + " not found in the list.");
        }
    }

    private void viewAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees in the list.");
        } else {
            System.out.println("Current Employees:");
            for (int i = 0; i < employees.size(); i++) {
                System.out.println((i + 1) + ". " + employees.get(i));
            }
        }
    }
}