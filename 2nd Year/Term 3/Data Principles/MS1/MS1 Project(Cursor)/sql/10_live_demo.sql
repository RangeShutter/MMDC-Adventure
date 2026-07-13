-- =============================================================================
-- MotorPH Payroll System - LIVE DEMO (full flow + constraints)
-- Run in MySQL Workbench AFTER payrollsystem_db is deployed (payrollsystem_db.sql).
--
-- HOW TO RUN:
--   Option A: Execute entire script (lightning bolt) - Parts 1-4 run automatically.
--   Option B: Highlight one SECTION at a time and Execute for presentation pacing.
--
-- Part 5 uses a stored procedure to test constraints WITHOUT stopping the script.
-- =============================================================================

USE payrollsystem_db;

-- =============================================================================
-- PART 1: VERIFY DATABASE IS LOADED
-- =============================================================================
SELECT '=== PART 1: Database loaded? ===' AS DemoSection;

SHOW TABLES;

SELECT 'Employee' AS tbl, COUNT(*) AS row_count FROM Employee
UNION ALL SELECT 'Salary', COUNT(*) FROM Salary
UNION ALL SELECT 'Benefit', COUNT(*) FROM Benefit
UNION ALL SELECT 'GovernmentID', COUNT(*) FROM GovernmentID
UNION ALL SELECT 'SSSContributionBracket', COUNT(*) FROM SSSContributionBracket;

SELECT 'Expected: 34 employees, 45 SSS brackets' AS Note;

-- =============================================================================
-- PART 2: STEP 1 - MASTER DATA (already in database)
-- =============================================================================
SELECT '=== PART 2: Step 1 - Employee master data ===' AS DemoSection;

SELECT e.EmployeeID, e.FirstName, e.LastName, e.Position,
       d.DepartmentName, es.StatusName, s.BaseSalary
FROM Employee e
JOIN Department d ON e.DepartmentID = d.DepartmentID
JOIN EmploymentStatus es ON e.StatusID = es.StatusID
JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID = 10001;

SELECT g.SSSNumber, g.PhilHealthNumber, g.TINNumber, g.PagIBIGNumber
FROM GovernmentID g WHERE g.EmployeeID = 10001;

SELECT BenefitType, Amount FROM Benefit WHERE EmployeeID = 10001;

-- =============================================================================
-- PART 3: STEPS 2-5 - SIMULATE FULL PAYROLL FLOW (Employee 10001)
-- =============================================================================
SELECT '=== PART 3: Steps 2-5 - Live payroll simulation ===' AS DemoSection;

-- Clean up previous demo rows (safe to re-run)
-- Workbench "safe update mode" (Error 1175) blocks JOIN DELETEs; use PK-based deletes
DELETE FROM Deduction
WHERE DeductionID IN (
    SELECT DeductionID FROM (
        SELECT d.DeductionID
        FROM Deduction d
        INNER JOIN Payroll p ON d.PayrollID = p.PayrollID
        WHERE p.EmployeeID = 10001 AND p.PayPeriodStart = '2024-06-01'
    ) AS demo_deductions
);

DELETE FROM Payslip
WHERE PayslipID IN (
    SELECT PayslipID FROM (
        SELECT ps.PayslipID
        FROM Payslip ps
        INNER JOIN Payroll p ON ps.PayrollID = p.PayrollID
        WHERE p.EmployeeID = 10001 AND p.PayPeriodStart = '2024-06-01'
    ) AS demo_payslips
);

DELETE FROM Payroll
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID
        FROM Payroll
        WHERE EmployeeID = 10001 AND PayPeriodStart = '2024-06-01'
    ) AS demo_payrolls
);

DELETE FROM Overtime
WHERE OvertimeID IN (
    SELECT OvertimeID FROM (
        SELECT o.OvertimeID
        FROM Overtime o
        INNER JOIN Attendance a ON o.AttendanceID = a.AttendanceID
        WHERE a.EmployeeID = 10001 AND a.Date = '2024-06-03'
    ) AS demo_overtime
);

DELETE FROM Attendance
WHERE AttendanceID IN (
    SELECT AttendanceID FROM (
        SELECT AttendanceID
        FROM Attendance
        WHERE EmployeeID = 10001 AND Date = '2024-06-03'
    ) AS demo_attendance
);

-- STEP 2: Attendance + approved overtime
SELECT '--- Step 2: Record attendance and overtime ---' AS Step;

INSERT INTO Attendance (EmployeeID, Date, TimeIn, TimeOut)
VALUES (10001, '2024-06-03', '2024-06-03 08:00:00', '2024-06-03 17:00:00');

SET @demo_attendance_id = LAST_INSERT_ID();

INSERT INTO Overtime (AttendanceID, Hours, Rate, ApprovedBy, ApprovalStatusID, ApprovalDate)
VALUES (@demo_attendance_id, 2.00, 535.71, 10006, 2, '2024-06-03 18:00:00');

SELECT a.AttendanceID, a.Date, a.TimeIn, a.TimeOut, o.Hours, o.Rate, ast.StatusName AS OT_Status
FROM Attendance a
JOIN Overtime o ON a.AttendanceID = o.AttendanceID
JOIN ApprovalStatus ast ON o.ApprovalStatusID = ast.ApprovalStatusID
WHERE a.EmployeeID = 10001;

-- STEP 3: Statutory lookup (application would calculate; we demonstrate lookup)
SELECT '--- Step 3: Look up SSS bracket for salary 90,000 ---' AS Step;

SELECT s.BaseSalary,
       b.ContributionAmount AS SSS_Contribution,
       b.RangeDescription
FROM Salary s
JOIN SSSContributionBracket b
  ON s.BaseSalary >= b.RangeFrom
 AND (b.RangeTo IS NULL OR s.BaseSalary <= b.RangeTo)
WHERE s.EmployeeID = 10001
LIMIT 1;

SELECT 'PhilHealth and Pag-IBIG rules:' AS Step;
SELECT * FROM PhilhealthContributionRate;
SELECT * FROM PagibigContributionRate;

SELECT BracketID, TaxRuleDescription, BaseTax, ExcessRate, ExcessOver
FROM WithholdingTaxBracket ORDER BY BracketID;

-- STEP 4: Save payroll + deductions (sample computed amounts)
SELECT '--- Step 4: Save Payroll and Deductions ---' AS Step;

INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
VALUES (10001, '2024-06-01', '2024-06-15', 45000.00, 38286.60);

SET @demo_payroll_id = LAST_INSERT_ID();

INSERT INTO Deduction (PayrollID, DeductionType, Amount) VALUES
(@demo_payroll_id, 'SSS', 1125.00),
(@demo_payroll_id, 'PhilHealth', 375.00),
(@demo_payroll_id, 'Pag-IBIG', 100.00),
(@demo_payroll_id, 'Withholding Tax', 5113.40);

SELECT p.PayrollID, p.PayPeriodStart, p.PayPeriodEnd, p.GrossPay, p.NetPay,
       d.DeductionType, d.Amount
FROM Payroll p
JOIN Deduction d ON p.PayrollID = d.PayrollID
WHERE p.PayrollID = @demo_payroll_id;

-- STEP 5: Generate payslip
SELECT '--- Step 5: Generate Payslip ---' AS Step;

INSERT INTO Payslip (PayrollID, IssueDate)
VALUES (@demo_payroll_id, '2024-06-16');

SELECT ps.PayslipID, ps.IssueDate,
       e.FirstName, e.LastName,
       p.GrossPay, p.NetPay,
       (SELECT SUM(Amount) FROM Deduction WHERE PayrollID = p.PayrollID) AS TotalDeductions
FROM Payslip ps
JOIN Payroll p ON ps.PayrollID = p.PayrollID
JOIN Employee e ON p.EmployeeID = e.EmployeeID
WHERE ps.PayrollID = @demo_payroll_id;

-- Full trace: Employee -> Attendance -> Payroll -> Deduction -> Payslip
SELECT '--- End-to-end trace for demo employee ---' AS Step;

SELECT
    e.EmployeeID,
    e.FirstName,
    e.LastName,
    a.Date AS WorkDate,
    o.Hours AS OTHours,
    p.PayPeriodStart,
    p.GrossPay,
    p.NetPay,
    ps.IssueDate AS PayslipDate
FROM Employee e
LEFT JOIN Attendance a ON e.EmployeeID = a.EmployeeID AND a.Date = '2024-06-03'
LEFT JOIN Overtime o ON a.AttendanceID = o.AttendanceID
LEFT JOIN Payroll p ON e.EmployeeID = p.EmployeeID AND p.PayPeriodStart = '2024-06-01'
LEFT JOIN Payslip ps ON p.PayrollID = ps.PayrollID
WHERE e.EmployeeID = 10001;

-- =============================================================================
-- PART 4: POSITIVE CONSTRAINT DEMO (valid data accepted)
-- =============================================================================
SELECT '=== PART 4: Valid data passes constraints ===' AS DemoSection;

SELECT 'Valid leave request (EndDate >= StartDate) - inserting...' AS Test;

INSERT INTO `Leave` (EmployeeID, LeaveTypeID, StartDate, EndDate, ApprovedBy, ApprovalStatusID)
VALUES (10007, 2, '2024-07-01', '2024-07-03', 10006, 2);

SELECT LeaveID, EmployeeID, StartDate, EndDate FROM `Leave`
WHERE EmployeeID = 10007 AND StartDate = '2024-07-01';

-- =============================================================================
-- PART 5: CONSTRAINT TESTS (automated - shows PASS = constraint blocked bad data)
-- =============================================================================
SELECT '=== PART 5: Constraint tests (automated) ===' AS DemoSection;

DROP PROCEDURE IF EXISTS sp_live_demo_constraints;

DELIMITER //

CREATE PROCEDURE sp_live_demo_constraints()
BEGIN
    DROP TEMPORARY TABLE IF EXISTS demo_constraint_results;
    CREATE TEMPORARY TABLE demo_constraint_results (
        TestNo INT,
        TestName VARCHAR(80),
        Expected VARCHAR(30),
        Result VARCHAR(30),
        Detail VARCHAR(255)
    );

    -- Test 1: NOT NULL on FirstName
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 @err = MESSAGE_TEXT;
            INSERT INTO demo_constraint_results VALUES
                (1, 'NOT NULL FirstName', 'REJECT', 'PASS', @err);
        END;
        INSERT INTO Employee (EmployeeID, FirstName, LastName, DateOfBirth, ContactNumber, Position, DepartmentID, StatusID)
        VALUES (99991, NULL, 'Invalid', '2000-01-01', '000-000', 'Test', 1, 1);
        INSERT INTO demo_constraint_results VALUES
            (1, 'NOT NULL FirstName', 'REJECT', 'FAIL', 'NULL name was allowed');
    END;

    -- Test 2: CHECK TimeOut >= TimeIn
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 @err = MESSAGE_TEXT;
            INSERT INTO demo_constraint_results VALUES
                (2, 'CHECK TimeOut >= TimeIn', 'REJECT', 'PASS', @err);
        END;
        INSERT INTO Attendance (EmployeeID, Date, TimeIn, TimeOut)
        VALUES (10002, '2024-06-04', '2024-06-04 17:00:00', '2024-06-04 08:00:00');
        INSERT INTO demo_constraint_results VALUES
            (2, 'CHECK TimeOut >= TimeIn', 'REJECT', 'FAIL', 'Invalid times were allowed');
    END;

    -- Test 3: CHECK negative salary
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 @err = MESSAGE_TEXT;
            INSERT INTO demo_constraint_results VALUES
                (3, 'CHECK BaseSalary >= 0', 'REJECT', 'PASS', @err);
        END;
        INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom)
        VALUES (10002, -1000.00, 'Monthly', '2024-01-01');
        INSERT INTO demo_constraint_results VALUES
            (3, 'CHECK BaseSalary >= 0', 'REJECT', 'FAIL', 'Negative salary allowed');
    END;

    -- Test 4: FK invalid EmployeeID
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 @err = MESSAGE_TEXT;
            INSERT INTO demo_constraint_results VALUES
                (4, 'FK valid EmployeeID', 'REJECT', 'PASS', @err);
        END;
        INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom)
        VALUES (99999, 30000.00, 'Monthly', '2024-01-01');
        INSERT INTO demo_constraint_results VALUES
            (4, 'FK valid EmployeeID', 'REJECT', 'FAIL', 'Orphan salary allowed');
    END;

    -- Test 5: UNIQUE payroll period (first insert OK, second must fail)
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 @err = MESSAGE_TEXT;
            INSERT INTO demo_constraint_results VALUES
                (5, 'UNIQUE payroll period', 'REJECT duplicate', 'PASS', @err);
        END;
        INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
        VALUES (10002, '2024-08-01', '2024-08-15', 20000.00, 18000.00);
        INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
        VALUES (10002, '2024-08-01', '2024-08-15', 20000.00, 17000.00);
        INSERT INTO demo_constraint_results VALUES
            (5, 'UNIQUE payroll period', 'REJECT duplicate', 'FAIL', 'Duplicate payroll allowed');
    END;

    -- Test 6: CHECK leave dates EndDate >= StartDate
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 @err = MESSAGE_TEXT;
            INSERT INTO demo_constraint_results VALUES
                (6, 'CHECK EndDate >= StartDate', 'REJECT', 'PASS', @err);
        END;
        INSERT INTO `Leave` (EmployeeID, LeaveTypeID, StartDate, EndDate, ApprovalStatusID)
        VALUES (10008, 1, '2024-09-10', '2024-09-05', 1);
        INSERT INTO demo_constraint_results VALUES
            (6, 'CHECK EndDate >= StartDate', 'REJECT', 'FAIL', 'Invalid leave dates allowed');
    END;

    -- Test 7: UNIQUE duplicate government SSS number
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 @err = MESSAGE_TEXT;
            INSERT INTO demo_constraint_results VALUES
                (7, 'UNIQUE SSSNumber', 'REJECT', 'PASS', @err);
        END;
        INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber)
        VALUES (10003, '44-4506057-3', '999999999999', '999-999-999-000', '999999999999');
        INSERT INTO demo_constraint_results VALUES
            (7, 'UNIQUE SSSNumber', 'REJECT', 'FAIL', 'Duplicate SSS allowed');
    END;

    SELECT TestNo, TestName, Expected, Result, Detail
    FROM demo_constraint_results
    ORDER BY TestNo;

    SELECT IF(
        SUM(Result = 'FAIL') = 0,
        'ALL CONSTRAINT TESTS PASSED - database enforces rules correctly',
        'WARNING - some constraints did not block invalid data'
    ) AS Summary
    FROM demo_constraint_results;

    -- Cleanup test payroll row from Test 5 if first insert succeeded
    DELETE FROM Payroll
    WHERE PayrollID IN (
        SELECT PayrollID FROM (
            SELECT PayrollID FROM Payroll
            WHERE EmployeeID = 10002 AND PayPeriodStart = '2024-08-01'
        ) AS test_payrolls
    );
END //

DELIMITER ;

CALL sp_live_demo_constraints();

DROP PROCEDURE IF EXISTS sp_live_demo_constraints;

SELECT '=== LIVE DEMO COMPLETE ===' AS DemoSection;
SELECT 'Result PASS in Part 5 means the constraint correctly REJECTED bad data.' AS Note;
