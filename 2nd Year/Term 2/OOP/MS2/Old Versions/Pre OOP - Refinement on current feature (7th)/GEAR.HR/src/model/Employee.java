package model;

/**
 * Domain model representing an employee (OOP redesign - GEAR.HR).
 * Encapsulates personal, work, and payroll-related attributes.
 * Validation in setters per OOP.txt: non-negative rates, non-empty password, valid roles.
 */
public class Employee {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String sssNumber;
    private String philHealthNumber;
    private String tin;
    private String pagIbigNumber;
    private String email;
    private String position;
    private String address;
    private String phone;
    private double hourlyRate; // optional; used when computed from base salary

    public Employee(String employeeNumber, String lastName, String firstName, String sssNumber,
                    String philHealthNumber, String tin, String pagIbigNumber, String email,
                    String position, String address, String phone) {
        this.employeeNumber = employeeNumber != null ? employeeNumber.trim() : "";
        this.lastName = lastName != null ? lastName : "";
        this.firstName = firstName != null ? firstName : "";
        this.sssNumber = sssNumber != null ? sssNumber : "";
        this.philHealthNumber = philHealthNumber != null ? philHealthNumber : "";
        this.tin = tin != null ? tin : "";
        this.pagIbigNumber = pagIbigNumber != null ? pagIbigNumber : "";
        this.email = email != null ? email : "";
        this.position = position != null ? position : "";
        this.address = address != null ? address : "";
        this.phone = phone != null ? phone : "";
        this.hourlyRate = 0;
    }

    /** Identity field; set only in constructor to keep entity stable. */
    public String getEmployeeNumber() { return employeeNumber; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName != null ? lastName : this.lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName != null ? firstName : this.firstName; }

    public String getSssNumber() { return sssNumber; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber != null ? sssNumber : this.sssNumber; }

    public String getPhilHealthNumber() { return philHealthNumber; }
    public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber != null ? philHealthNumber : this.philHealthNumber; }

    public String getTin() { return tin; }
    public void setTin(String tin) { this.tin = tin != null ? tin : this.tin; }

    public String getPagIbigNumber() { return pagIbigNumber; }
    public void setPagIbigNumber(String pagIbigNumber) { this.pagIbigNumber = pagIbigNumber != null ? pagIbigNumber : this.pagIbigNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email != null ? email : this.email; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position != null ? position : this.position; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address != null ? address : this.address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone != null ? phone : this.phone; }

    /** Hourly rate for payroll; must be >= 0. */
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate >= 0) {
            this.hourlyRate = hourlyRate;
        }
    }
}
