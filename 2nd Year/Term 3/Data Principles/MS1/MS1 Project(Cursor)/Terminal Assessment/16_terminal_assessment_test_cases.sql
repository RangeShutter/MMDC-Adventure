-- =============================================================================
-- MotorPH Payroll System - Terminal Assessment Test Cases
-- Homework: MO-IT111 Payroll System Database Test Case
--
-- Prerequisites:
--   Run payrollsystem_db_final.sql (or MS1 01-05 + M2 reports) first.
--
-- ID mapping (MotorPH schema uses 100xx):
--   New employees  -> 10035, 10036, 10037
--   Delete 29/30/31 -> 10029, 10030, 10031
--   Uniqueness ID 40 -> attempt duplicate of existing 10001 (PK proof)
--
-- Run each section in MySQL Workbench and capture screenshots for
-- TEST_CASE_RESULTS.md. For DBTC02-A and DBTC02-B, a RED X (error) = PASS.
-- =============================================================================

USE payrollsystem_db;

-- ############################################################################
-- I. MMDC-DBTC01 - Verify Employee Data Storage & Integrity
-- ############################################################################

-- ============================================================================
-- MMDC-DBTC01-A: Create a New Employee Record
-- Expected: 3 employees stored successfully (10035, 10036, 10037)
-- ============================================================================

SELECT '=== MMDC-DBTC01-A: Create Employee Record ===' AS TestSection;

-- Cleanup if re-running this section
SET @OLD_SQL_SAFE_UPDATES = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

DELETE FROM Benefit WHERE EmployeeID IN (10035, 10036, 10037);
DELETE FROM Salary WHERE EmployeeID IN (10035, 10036, 10037);
DELETE FROM GovernmentID WHERE EmployeeID IN (10035, 10036, 10037);
DELETE FROM EmployeeAddress WHERE EmployeeID IN (10035, 10036, 10037);
DELETE FROM Employee WHERE EmployeeID IN (10035, 10036, 10037);

SET SQL_SAFE_UPDATES = @OLD_SQL_SAFE_UPDATES;

-- 1) Billy Lloyd Calasang — HR Team Leader, Regular, Dept Human Resources (3)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10035, 'Billy Lloyd', 'Calasang', '1996-01-22',
    '2nd Floor, Gaisano Mactan Mall, Pajo, Lapu-lapu City, Cebu',
    '361-299-029', 'HR Team Leader', 3, 1
);

INSERT INTO EmployeeAddress (EmployeeID, StreetName)
VALUES (10035, '2nd Floor, Gaisano Mactan Mall, Pajo, Lapu-lapu City, Cebu');

INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo)
VALUES (10035, 42975.00, 'Monthly', '2024-01-01', NULL);

INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES
(10035, 'Clothing Allowance', 800.00),
(10035, 'Rice Subsidy', 1500.00),
(10035, 'Phone Allowance', 800.00);

INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber)
VALUES (10035, '37-3379841-1', '632361534812', '824-311-682-000', '374357402374');

-- 2) Jonathan Brosas — IT Technical Support, Probationary, Dept IT (2)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10036, 'Jonathan', 'Brosas', '1994-11-26',
    'A Fernando 1400, Valenzuela, Valenzuela',
    '032-340-2015', 'IT Technical Support', 2, 2
);

INSERT INTO EmployeeAddress (EmployeeID, StreetName)
VALUES (10036, 'A Fernando 1400, Valenzuela, Valenzuela');

INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo)
VALUES (10036, 42975.00, 'Monthly', '2024-01-01', NULL);

INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES
(10036, 'Clothing Allowance', 800.00),
(10036, 'Rice Subsidy', 1500.00),
(10036, 'Phone Allowance', 800.00);

INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber)
VALUES (10036, '92-4800602-9', '735270773421', '632-531-054-000', '632722676967');

-- 3) Shella Mae Tejor — Customer Service and Relations, Probationary, Dept 8
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10037, 'Shella Mae', 'Tejor', '1994-03-01',
    'Ayala Avenue 1200, Makati City, Metro Manila',
    '894-385-011', 'Customer Service and Relations', 8, 2
);

INSERT INTO EmployeeAddress (EmployeeID, StreetName)
VALUES (10037, 'Ayala Avenue 1200, Makati City, Metro Manila');

INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo)
VALUES (10037, 52670.00, 'Monthly', '2024-01-01', NULL);

INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES
(10037, 'Clothing Allowance', 1000.00),
(10037, 'Rice Subsidy', 1500.00),
(10037, 'Phone Allowance', 1000.00);

INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber)
VALUES (10037, '32-5213838-6', '675893056701', '327-367-815-000', '133337008927');

-- Proof: stored employee records (screenshot this result)
SELECT
    e.EmployeeID,
    CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
    e.DateOfBirth,
    ea.StreetName AS Address,
    e.ContactNumber,
    e.Position,
    d.DepartmentName,
    es.StatusName,
    s.BaseSalary,
    ROUND(s.BaseSalary / 2, 2) AS GrossSemiMonthlyRate,
    ROUND(s.BaseSalary / 20 / 8, 2) AS HourlyRateApprox,
    g.TINNumber,
    g.SSSNumber,
    g.PhilHealthNumber,
    g.PagIBIGNumber
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN EmploymentStatus es ON e.StatusID = es.StatusID
INNER JOIN EmployeeAddress ea ON e.EmployeeID = ea.EmployeeID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
WHERE e.EmployeeID IN (10035, 10036, 10037)
ORDER BY e.EmployeeID;

SELECT
    e.EmployeeID,
    CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
    b.BenefitType,
    b.Amount
FROM Employee e
INNER JOIN Benefit b ON e.EmployeeID = b.EmployeeID
WHERE e.EmployeeID IN (10035, 10036, 10037)
ORDER BY e.EmployeeID, b.BenefitType;

SELECT 'MMDC-DBTC01-A COMPLETE — expect 3 employees stored. Screenshot Result Grid.' AS Note;

-- ============================================================================
-- MMDC-DBTC01-B: Update Existing Employee Information
-- Expected: salaries updated for Martinez 23000, Santos 25000, Castro 23000, Tejor 25000
-- ============================================================================

SELECT '=== MMDC-DBTC01-B: Update Employee Information (BEFORE) ===' AS TestSection;

SELECT
    e.EmployeeID,
    CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
    e.Position,
    s.BaseSalary AS CurrentBaseSalary
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID IN (10033, 10034, 10032, 10037)
   OR (e.FirstName = 'Carlos Ian' AND e.LastName = 'Martinez')
   OR (e.FirstName = 'Beatriz' AND e.LastName = 'Santos')
   OR (e.FirstName = 'John Rafael' AND e.LastName = 'Castro')
   OR (e.FirstName = 'Shella Mae' AND e.LastName = 'Tejor')
ORDER BY e.EmployeeID;

UPDATE Salary SET BaseSalary = 23000.00
WHERE EmployeeID = 10033; -- Carlos Ian Martinez

UPDATE Salary SET BaseSalary = 25000.00
WHERE EmployeeID = 10034; -- Beatriz Santos

UPDATE Salary SET BaseSalary = 23000.00
WHERE EmployeeID = 10032; -- John Rafael Castro

UPDATE Salary SET BaseSalary = 25000.00
WHERE EmployeeID = 10037; -- Shella Mae Tejor

SELECT '=== MMDC-DBTC01-B: Update Employee Information (AFTER) ===' AS TestSection;

SELECT
    e.EmployeeID,
    CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
    e.Position,
    s.BaseSalary AS UpdatedBaseSalary
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID IN (10033, 10034, 10032, 10037)
ORDER BY e.EmployeeID;

SELECT 'MMDC-DBTC01-B COMPLETE — expect UpdatedBaseSalary 23000/25000/23000/25000. Screenshot.' AS Note;

-- ============================================================================
-- MMDC-DBTC01-C: Delete Employee Record
-- Homework IDs 29/30/31 map to MotorPH 10029/10030/10031
-- Expected: Carol Ramos, Emelia Maceda, Delia Aguilar removed
-- ============================================================================

SELECT '=== MMDC-DBTC01-C: Delete Employee Record (BEFORE) ===' AS TestSection;

SELECT EmployeeID, FirstName, LastName, Position
FROM Employee
WHERE EmployeeID IN (10029, 10030, 10031);

-- Child rows first (FK ON DELETE RESTRICT); clear any M2 payroll links
SET SQL_SAFE_UPDATES = 0;

DELETE FROM Deduction
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID FROM Payroll WHERE EmployeeID IN (10029, 10030, 10031)
    ) AS p
);

DELETE FROM Payslip
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID FROM Payroll WHERE EmployeeID IN (10029, 10030, 10031)
    ) AS p
);

DELETE FROM Payroll WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Benefit WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Salary WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM GovernmentID WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM EmployeeAddress WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Attendance WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Overtime WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM `Leave` WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Employee WHERE EmployeeID IN (10029, 10030, 10031);

SET SQL_SAFE_UPDATES = @OLD_SQL_SAFE_UPDATES;

SELECT '=== MMDC-DBTC01-C: Delete Employee Record (AFTER) ===' AS TestSection;

SELECT EmployeeID, FirstName, LastName
FROM Employee
WHERE EmployeeID IN (10029, 10030, 10031);
-- Expect 0 rows

SELECT COUNT(*) AS RemainingDeletedEmployees
FROM Employee
WHERE EmployeeID IN (10029, 10030, 10031);
-- Expect 0

SELECT 'MMDC-DBTC01-C COMPLETE — expect 0 remaining rows. Screenshot.' AS Note;

-- ############################################################################
-- II. MMDC-DBTC02 - Verify Employee Data Constraints
-- ############################################################################

-- ============================================================================
-- MMDC-DBTC02-A: Check Employee ID Uniqueness
-- Homework "Employee Number 40" adapted: insert with EXISTING EmployeeID 10001
-- Expected: ERROR — duplicate primary key (RED X in Workbench = PASS)
-- ============================================================================

SELECT '=== MMDC-DBTC02-A: Check Employee ID Uniqueness ===' AS TestSection;
SELECT 'NEXT STATEMENT MUST FAIL (duplicate EmployeeID 10001). Red X = PASS.' AS Note;

-- This INSERT must be rejected (Error 1062 Duplicate entry for PRIMARY key)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10001, 'Mac Arnold', 'Almirol', '1996-10-08',
    'Unit 2802 One San Miguel Bldg, Shaw Blvd Cor San Miguel Ave, Ortigas Ctr 1605, Pasig City',
    '477-771-607', 'IT Technical Support', 2, 2
);

-- If you reached this line, uniqueness FAILED (should not run after error if stopped on error)
SELECT 'MMDC-DBTC02-A UNEXPECTED SUCCESS — uniqueness constraint may be missing' AS Warning;

-- ============================================================================
-- MMDC-DBTC02-B: Check Null Values
-- Expected: ERROR — NOT NULL violation on ContactNumber / Position (RED X = PASS)
-- Run this after acknowledging the previous error, or run this block alone.
-- ============================================================================

SELECT '=== MMDC-DBTC02-B: Check Null Values ===' AS TestSection;
SELECT 'NEXT STATEMENT MUST FAIL (NULL ContactNumber and Position). Red X = PASS.' AS Note;

-- Missing mandatory ContactNumber and Position (NOT NULL)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10099, 'Ian', 'Correa', '1996-12-16',
    '8435 West Service Road Marcelo Green Village South Superhighway, Paranaque City',
    NULL,
    NULL,
    2,
    2
);

SELECT 'MMDC-DBTC02-B UNEXPECTED SUCCESS — NOT NULL constraint may be missing' AS Warning;

SELECT '=== Terminal Assessment test script finished ===' AS Done;
