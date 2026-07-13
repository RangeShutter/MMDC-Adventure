-- MotorPH Payroll System - Step 1: Create database
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS payrollsystem_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE payrollsystem_db;
SHOW TABLES;
SELECT COUNT(*) AS total_employees FROM Employee;
SELECT e.EmployeeID, e.FirstName, e.LastName, d.DepartmentName,
       es.StatusName, s.BaseSalary
FROM Employee e
JOIN Department d ON e.DepartmentID = d.DepartmentID
JOIN EmploymentStatus es ON e.StatusID = es.StatusID
JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID = 10001;
SELECT * FROM GovernmentID WHERE EmployeeID = 10001;
SELECT BenefitType, Amount FROM Benefit WHERE EmployeeID = 10001;
SELECT COUNT(*) FROM SSSContributionBracket;
SELECT BracketID, RangeDescription, ContributionAmount
FROM SSSContributionBracket ORDER BY BracketID LIMIT 3;