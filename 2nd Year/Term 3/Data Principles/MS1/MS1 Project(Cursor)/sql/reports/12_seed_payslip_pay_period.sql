-- MotorPH Payroll System - M2: Monthly coverage via two semi-monthly cutoffs
-- Seeds Payroll + Payslip for both June cutoffs (two payslips per employee).
-- Earnings and deductions are computed in vw_EmployeePayslipReport (Option A split).

USE payrollsystem_db;

-- Cutoff 1: first half of June
SET @period1_start = '2024-06-01';
SET @period1_end = '2024-06-15';
SET @issue1 = '2024-06-16';

-- Cutoff 2: second half of June
SET @period2_start = '2024-06-16';
SET @period2_end = '2024-06-30';
SET @issue2 = '2024-07-01';

-- Workbench safe-update mode (Error 1175) blocks DELETE with subqueries
SET @OLD_SQL_SAFE_UPDATES = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

-- Remove prior seed for both June cutoffs (safe for re-run)
DELETE FROM Deduction
WHERE DeductionID IN (
    SELECT DeductionID FROM (
        SELECT d.DeductionID
        FROM Deduction d
        INNER JOIN Payroll p ON d.PayrollID = p.PayrollID
        WHERE (
            (p.PayPeriodStart = @period1_start AND p.PayPeriodEnd = @period1_end)
            OR (p.PayPeriodStart = @period2_start AND p.PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_deductions
);

DELETE FROM Payslip
WHERE PayslipID IN (
    SELECT PayslipID FROM (
        SELECT ps.PayslipID
        FROM Payslip ps
        INNER JOIN Payroll p ON ps.PayrollID = p.PayrollID
        WHERE (
            (p.PayPeriodStart = @period1_start AND p.PayPeriodEnd = @period1_end)
            OR (p.PayPeriodStart = @period2_start AND p.PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_payslips
);

DELETE FROM Payroll
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID
        FROM Payroll
        WHERE (
            (PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end)
            OR (PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_payrolls
);

SET SQL_SAFE_UPDATES = @OLD_SQL_SAFE_UPDATES;

-- Placeholder gross/net (report view computes actual amounts)
-- Cutoff 1
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period1_start,
    @period1_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue1
FROM Payroll
WHERE PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end;

-- Cutoff 2
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period2_start,
    @period2_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue2
FROM Payroll
WHERE PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end;

SELECT '=== June payroll seed (two cutoffs) ===' AS Section;

SELECT PayPeriodStart, PayPeriodEnd, COUNT(*) AS payroll_rows
FROM Payroll
WHERE (
    (PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end)
    OR (PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end)
)
GROUP BY PayPeriodStart, PayPeriodEnd
ORDER BY PayPeriodStart;
