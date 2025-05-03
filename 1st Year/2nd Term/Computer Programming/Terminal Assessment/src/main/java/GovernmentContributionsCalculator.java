/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ransel
 */
public class GovernmentContributionsCalculator {
    public static double calculateSSS(double monthlySalary) {
        // SSS calculation logic
        if(monthlySalary < 3250) return 135.00;
        if(monthlySalary < 3750) return 157.50;
        // ... continue with all brackets
        return 1125.00; // For 24,750 and above
    }

    public static double calculatePhilhealth(double monthlySalary) {
        double premium = monthlySalary * 0.03;
        return Math.min(Math.max(premium, 300), 1800) / 2; // Employee share
    }

    public static double calculatePagIBIG(double monthlySalary) {
        return (monthlySalary > 1500) ? monthlySalary * 0.02 : monthlySalary * 0.01;
    }

    public static double calculateWithholdingTax(double taxableIncome) {
        // Withholding tax brackets implementation
        if(taxableIncome <= 20832) return 0;
        if(taxableIncome <= 33332) return (taxableIncome - 20833) * 0.20;
        // ... implement all brackets
        return (taxableIncome - 666667) * 0.35 + 200833.33;
    }
}