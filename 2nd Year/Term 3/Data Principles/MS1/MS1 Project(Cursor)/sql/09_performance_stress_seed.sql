-- =============================================================================
-- MotorPH Payroll System - OPTIONAL stress-test data generator
-- Creates bulk Attendance, Payroll, and Deduction rows to test performance
-- at higher volume. Safe to skip for normal homework demo.
--
-- WARNING: Inserts ~6,120 attendance rows (34 employees x 180 days) and
--          34 payroll + 136 deduction rows. Run 08_performance_test.sql
--          BEFORE and AFTER to compare query Duration in SHOW PROFILES.
-- =============================================================================

USE payrollsystem_db;

SELECT '=== STRESS SEED START ===' AS Section;

-- Skip if already seeded (idempotent guard)
SET @stress_exists = (
    SELECT COUNT(*) FROM Attendance WHERE EmployeeID = 10001 AND Date = '2023-01-02'
);

SELECT IF(@stress_exists > 0,
    'Stress data already present. Delete Attendance/Payroll/Deduction test rows or skip.',
    'Inserting stress data...') AS Status;

-- Only insert when guard count is 0
-- 180 workdays per employee (2023-01-02 to ~2023-09) using recursive CTE
INSERT INTO Attendance (EmployeeID, Date, TimeIn, TimeOut)
WITH RECURSIVE
    emp AS (SELECT EmployeeID FROM Employee),
    day_nums AS (
        SELECT 0 AS n
        UNION ALL
        SELECT n + 1 FROM day_nums WHERE n < 179
    )
SELECT
    e.EmployeeID,
    DATE_ADD('2023-01-02', INTERVAL d.n DAY),
    TIMESTAMP(DATE_ADD('2023-01-02', INTERVAL d.n DAY), '08:00:00'),
    TIMESTAMP(DATE_ADD('2023-01-02', INTERVAL d.n DAY), '17:00:00')
FROM emp e
CROSS JOIN day_nums d
WHERE @stress_exists = 0
  AND DAYOFWEEK(DATE_ADD('2023-01-02', INTERVAL d.n DAY)) NOT IN (1, 7);

-- One payroll period per employee (stress payroll module)
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    '2023-06-01',
    '2023-06-15',
    s.BaseSalary / 2,
    (s.BaseSalary / 2) * 0.85
FROM Employee e
JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE @stress_exists = 0;

-- Four deductions per payroll row
INSERT INTO Deduction (PayrollID, DeductionType, Amount)
SELECT p.PayrollID, dt.DeductionType, dt.Amount
FROM Payroll p
CROSS JOIN (
    SELECT 'SSS' AS DeductionType, 562.50 AS Amount
    UNION ALL SELECT 'PhilHealth', 375.00
    UNION ALL SELECT 'Pag-IBIG', 100.00
    UNION ALL SELECT 'Withholding Tax', 400.00
) dt
WHERE p.PayPeriodStart = '2023-06-01'
  AND @stress_exists = 0;

SELECT '=== STRESS SEED ROW COUNTS ===' AS Section;

SELECT 'Attendance' AS TableName, COUNT(*) AS RowCount FROM Attendance
UNION ALL SELECT 'Payroll', COUNT(*) FROM Payroll
UNION ALL SELECT 'Deduction', COUNT(*) FROM Deduction;

SELECT '=== Re-run 08_performance_test.sql and compare SHOW PROFILES Duration ===' AS NextStep;

-- To remove stress data later (optional cleanup):
-- DELETE d FROM Deduction d JOIN Payroll p ON d.PayrollID = p.PayrollID WHERE p.PayPeriodStart = '2023-06-01';
-- DELETE FROM Payroll WHERE PayPeriodStart = '2023-06-01';
-- DELETE FROM Attendance WHERE Date BETWEEN '2023-01-02' AND DATE_ADD('2023-01-02', INTERVAL 179 DAY);
