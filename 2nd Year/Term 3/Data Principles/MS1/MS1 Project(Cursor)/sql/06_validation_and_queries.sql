-- MotorPH Payroll System - Step 7-8: Validation tests and verification queries
-- Run after all seed scripts. Constraint failure tests use transactions that roll back.

USE payrollsystem_db;

-- ============================================================================
-- STEP 8: Verification SELECT queries
-- ============================================================================

SELECT '=== Table inventory ===' AS Section;
SHOW TABLES;

SELECT '=== Row counts ===' AS Section;
SELECT 'Employee' AS TableName, COUNT(*) AS RowCount FROM Employee
UNION ALL SELECT 'Department', COUNT(*) FROM Department
UNION ALL SELECT 'EmploymentStatus', COUNT(*) FROM EmploymentStatus
UNION ALL SELECT 'EmployeeAddress', COUNT(*) FROM EmployeeAddress
UNION ALL SELECT 'GovernmentID', COUNT(*) FROM GovernmentID
UNION ALL SELECT 'Salary', COUNT(*) FROM Salary
UNION ALL SELECT 'Benefit', COUNT(*) FROM Benefit
UNION ALL SELECT 'SSSContributionBracket', COUNT(*) FROM SSSContributionBracket
UNION ALL SELECT 'PhilhealthContributionRate', COUNT(*) FROM PhilhealthContributionRate
UNION ALL SELECT 'PagibigContributionRate', COUNT(*) FROM PagibigContributionRate
UNION ALL SELECT 'WithholdingTaxBracket', COUNT(*) FROM WithholdingTaxBracket
UNION ALL SELECT 'Role', COUNT(*) FROM Role
UNION ALL SELECT 'Permission', COUNT(*) FROM Permission
UNION ALL SELECT 'RolePermission', COUNT(*) FROM RolePermission;

SELECT '=== Employees with department and status (sample) ===' AS Section;
SELECT
    e.EmployeeID,
    e.FirstName,
    e.LastName,
    e.Position,
    d.DepartmentName,
    es.StatusName,
    e.ContactNumber
FROM Employee e
JOIN Department d ON e.DepartmentID = d.DepartmentID
JOIN EmploymentStatus es ON e.StatusID = es.StatusID
ORDER BY e.EmployeeID
LIMIT 10;

SELECT '=== Employee 10001 full profile ===' AS Section;
SELECT
    e.EmployeeID,
    e.FirstName,
    e.LastName,
    e.DateOfBirth,
    d.DepartmentName,
    g.SSSNumber,
    g.PhilHealthNumber,
    g.TINNumber,
    g.PagIBIGNumber,
    s.BaseSalary,
    s.PayFrequency
FROM Employee e
JOIN Department d ON e.DepartmentID = d.DepartmentID
JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID = 10001;

SELECT '=== Benefits for employee 10001 ===' AS Section;
SELECT BenefitType, Amount
FROM Benefit
WHERE EmployeeID = 10001;

SELECT '=== SSS brackets (first 5 and last) ===' AS Section;
(SELECT BracketID, RangeFrom, RangeTo, ContributionAmount, RangeDescription
 FROM SSSContributionBracket ORDER BY BracketID LIMIT 5)
UNION ALL
(SELECT BracketID, RangeFrom, RangeTo, ContributionAmount, RangeDescription
 FROM SSSContributionBracket ORDER BY BracketID DESC LIMIT 1);

SELECT '=== PhilHealth rates ===' AS Section;
SELECT * FROM PhilhealthContributionRate;

SELECT '=== Pag-IBIG rates ===' AS Section;
SELECT * FROM PagibigContributionRate;

SELECT '=== Withholding tax brackets ===' AS Section;
SELECT BracketID, MonthlyRateMin, MonthlyRateMax, TaxRuleDescription, BaseTax, ExcessRate, ExcessOver
FROM WithholdingTaxBracket
ORDER BY BracketID;

SELECT '=== Departments with managers ===' AS Section;
SELECT
    d.DepartmentID,
    d.DepartmentName,
    d.ManagerID,
    CONCAT(m.FirstName, ' ', m.LastName) AS ManagerName
FROM Department d
LEFT JOIN Employee m ON d.ManagerID = m.EmployeeID
ORDER BY d.DepartmentID;

-- Constraint validation tests are in 07_constraint_tests.sql (run each block separately in Workbench).
