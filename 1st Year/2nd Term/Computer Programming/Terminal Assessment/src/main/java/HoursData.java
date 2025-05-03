/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ransel
 */
import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the hours worked and overtime data for each employee.
 */
public class HoursData {
    private int employeeNumber;
    private int assumedRegularHours; // Typically 40 hours per week
    private Map<String, Map<Integer, Double[]>> monthlyHours; // Month -> Week -> [RegularHours, Overtime]

    /**
     * Constructor for HoursData.
     *
     * @param employeeNumber      The employee's ID number.
     * @param assumedRegularHours The assumed regular working hours per week (e.g., 40 hours).
     */
    public HoursData(int employeeNumber, int assumedRegularHours) {
        this.employeeNumber = employeeNumber;
        this.assumedRegularHours = assumedRegularHours;
        this.monthlyHours = new HashMap<>();
    }

    /**
     * Adds hours data for a specific month and week.
     *
     * @param month        The month (e.g., "June").
     * @param week         The week number (1-4).
     * @param regularHours The regular hours worked.
     * @param overtime     The overtime hours worked.
     */
    public void addHours(String month, int week, double regularHours, double overtime) {
        monthlyHours.computeIfAbsent(month, k -> new HashMap<>())
                    .put(week, new Double[]{regularHours, overtime});
    }

    /**
     * Retrieves hours data for a specific month and week.
     *
     * @param month The month (e.g., "June").
     * @param week  The week number (1-4).
     * @return An array containing regular hours and overtime, or null if no data exists.
     */
    public Double[] getHours(String month, int week) {
        if (monthlyHours.containsKey(month)) { // Fixed: Added missing closing parenthesis
            return monthlyHours.get(month).get(week);
        }
        return null; // No data for this month
    }

    /**
     * Retrieves all monthly hours data.
     *
     * @return A map containing all months and their corresponding weekly hours data.
     */
    public Map<String, Map<Integer, Double[]>> getAllMonthlyHours() {
        return monthlyHours;
    }

    // Getters
    /**
     * Gets the employee's ID number.
     *
     * @return The employee's ID number.
     */
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Gets the assumed regular working hours per week.
     *
     * @return The assumed regular working hours per week.
     */
    public int getAssumedRegularHours() {
        return assumedRegularHours;
    }
}   
