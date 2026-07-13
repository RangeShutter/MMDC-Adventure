-- MotorPH Payroll System - M2: Standard semi-monthly pay period metadata
-- Provides PayPeriodStart, PayPeriodEnd, and Payslip IssueDate for the payslip view.
-- Earnings and deductions are computed in vw_EmployeePayslipReport.

USE payrollsystem_db;

SET @period_start = '2024-06-01';
SET @period_end = '2024-06-15';
SET @issue_date = '2024-06-16';

-- Workbench safe-update mode (Error 1175) blocks DELETE with subqueries
SET @OLD_SQL_SAFE_UPDATES = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

-- Remove prior seed for this pay period (safe for re-run)
DELETE FROM Deduction
WHERE DeductionID IN (
    SELECT DeductionID FROM (
        SELECT d.DeductionID
        FROM Deduction d
        INNER JOIN Payroll p ON d.PayrollID = p.PayrollID
        WHERE p.PayPeriodStart = @period_start AND p.PayPeriodEnd = @period_end
    ) AS to_delete_deductions
);

DELETE FROM Payslip
WHERE PayslipID IN (
    SELECT PayslipID FROM (
        SELECT ps.PayslipID
        FROM Payslip ps
        INNER JOIN Payroll p ON ps.PayrollID = p.PayrollID
        WHERE p.PayPeriodStart = @period_start AND p.PayPeriodEnd = @period_end
    ) AS to_delete_payslips
);

DELETE FROM Payroll
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID
        FROM Payroll
        WHERE PayPeriodStart = @period_start AND PayPeriodEnd = @period_end
    ) AS to_delete_payrolls
);

SET SQL_SAFE_UPDATES = @OLD_SQL_SAFE_UPDATES;

-- Placeholder gross/net (report view computes actual amounts)
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period_start,
    @period_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue_date
FROM Payroll
WHERE PayPeriodStart = @period_start AND PayPeriodEnd = @period_end;

SELECT COUNT(*) AS payroll_rows_seeded
FROM Payroll
WHERE PayPeriodStart = @period_start AND PayPeriodEnd = @period_end;
