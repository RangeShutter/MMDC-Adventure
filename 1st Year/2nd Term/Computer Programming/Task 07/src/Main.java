public class Main {
    public static void main(String[] args) {
        // Create static employee data
        Employee employee1 = new Employee(
                1, "Sela Cruz", "Juan", "1990-05-15", "123 Main St, Manila", "09123456789",
                "123456789", "987654321", "123-456-789", "123456789012", "Active", "Software Engineer",
                "Maria Santos", 50000.0, 1500.0, 1000.0, 800.0, 25000.0, 250.0
        );

        Employee employee2 = new Employee(
                2, "Santos", "Maria", "1985-10-20", "456 Elm St, Quezon City", "09987654321",
                "987654321", "123456789", "987-654-321", "987654321098", "Active", "HR Manager",
                "Pedro Reyes", 60000.0, 1500.0, 1000.0, 800.0, 30000.0, 300.0
        );

        Employee employee3 = new Employee(
                3, "Reyes", "Pedro", "1980-03-25", "789 Oak St, Cebu", "09112233445",
                "456789123", "321654987", "456-789-123", "456789123456", "Active", "Sales Associate",
                "Juan Dela Cruz", 45000.0, 1500.0, 1000.0, 800.0, 22500.0, 225.0
        );

        // Display employee information in the console
        System.out.println("Employee Information:");
        System.out.println("=====================");
        employee1.displayEmployeeInfo();
        employee2.displayEmployeeInfo();
        employee3.displayEmployeeInfo();
    }
}
