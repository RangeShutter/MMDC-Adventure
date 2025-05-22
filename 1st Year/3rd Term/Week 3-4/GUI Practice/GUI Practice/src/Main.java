import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create and display the GradeCalculator GUI
        GradeCalculator calculator = new GradeCalculator();
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the entire app when window is closed
        calculator.setVisible(true);
    }
}