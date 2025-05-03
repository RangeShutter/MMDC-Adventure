/**
 * This code is made by Ransel Sumatra.
 * GitHub: https://github.com/ranselcrackers/
 * GitHub Username: ranselcrackers
 * Specialization: BSIT Major in Marketing Technologies at Mapúa Malayan Colleges Laguna
 */


import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static Employee currentUser;
    private static List<Employee> employees;

    public static void main(String[] args) {
        WelcomeScreen.displayWelcomeScreen();
        employees = EmployeeDataInitializer.initializeEmployees();
        authenticateUser();
        displayPayrollMenu();
    }

    private static void authenticateUser() {
        while (true) {
            System.out.print("Enter Employee ID: ");
            int empId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            currentUser = EmployeeDataInitializer.getEmployee(empId, employees);

            if (currentUser != null && currentUser.validatePassword(password)) {
                System.out.println("\nWelcome, " + currentUser.getFullName() + "!");
                return;
            }
            System.out.println("Invalid credentials. Please try again.\n");
        }
    }

    private static void displayPayrollMenu() {
        while (true) {
            System.out.println("\nPlease choose a month to see your payroll:");
            System.out.println("1. June");
            System.out.println("2. July");
            System.out.println("3. August");
            System.out.println("4. September");
            System.out.println("5. October");
            System.out.println("6. November");
            System.out.println("7. December");
            System.out.print("Enter your choice (1-7, or 0 to exit): ");
            int monthChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (monthChoice == 0) {
                System.out.println("Exiting payroll system. Goodbye!");
                break;
            }

            if (monthChoice < 1 || monthChoice > 7) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            String selectedMonth = getMonthName(monthChoice);
            System.out.println("\nPlease choose the week you want to see for your payroll:");
            System.out.println("1. Week 1");
            System.out.println("2. Week 2");
            System.out.println("3. Week 3");
            System.out.println("4. Week 4");
            System.out.print("Enter your choice (1-4): ");
            int weekChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (weekChoice < 1 || weekChoice > 4) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            // Calculate and display payroll
            PayrollCalculator.calculatePayroll(currentUser, selectedMonth, weekChoice);
        }
    }

    private static String getMonthName(int monthChoice) {
        switch (monthChoice) {
            case 1: return "June";
            case 2: return "July";
            case 3: return "August";
            case 4: return "September";
            case 5: return "October";
            case 6: return "November";
            case 7: return "December";
            default: return "Invalid";
        }
    }
}