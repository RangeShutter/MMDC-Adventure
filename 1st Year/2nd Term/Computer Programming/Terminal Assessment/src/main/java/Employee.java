/**
 * This code is made by Ransel Sumatra.
 * GitHub: https://github.com/ranselcrackers/
 * GitHub Username: ranselcrackers
 * Specialization: BSIT Major in Marketing Technologies at Mapúa Malayan Colleges Laguna
 */

/**
 * This class represents an employee and their details.
 */
public class Employee {
    private int employeeNumber;
    private String lastName;
    private String firstName;
    private String password;
    private String position; // Added position
    private double hourlyRate;
    private HoursData hoursData;

    // Government agency details
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagibigNumber;

    public Employee(int employeeNumber, String lastName, String firstName, 
                    String password, String position, double hourlyRate, 
                    HoursData hoursData, String sssNumber, String philhealthNumber, 
                    String tinNumber, String pagibigNumber) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.position = position;
        this.hourlyRate = hourlyRate;
        this.hoursData = hoursData;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
    }

    // Getters for all fields (except password)
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPosition() {
        return position;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getPagibigNumber() {
        return pagibigNumber;
    }

    public HoursData getHoursData() {
        return hoursData;
    }

    // Password validation
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}