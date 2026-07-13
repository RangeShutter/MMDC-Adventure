-- =============================================================================
-- MotorPH Payroll System - Performance Test
-- Run in MySQL Workbench AFTER payrollsystem_db is deployed and seeded (01-05).
-- MySQL 8.0+ recommended (EXPLAIN ANALYZE, profiling).
-- =============================================================================

USE payrollsystem_db;

SELECT '=== PERFORMANCE TEST START ===' AS Section, NOW() AS TestTime;

-- -----------------------------------------------------------------------------
-- 1. BASELINE: Database size and row counts
-- -----------------------------------------------------------------------------
SELECT '=== 1. Baseline row counts ===' AS Section;

SELECT 'Employee' AS TableName, COUNT(*) AS RowCount FROM Employee
UNION ALL SELECT 'Salary', COUNT(*) FROM Salary
UNION ALL SELECT 'Benefit', COUNT(*) FROM Benefit
UNION ALL SELECT 'GovernmentID', COUNT(*) FROM GovernmentID
UNION ALL SELECT 'EmployeeAddress', COUNT(*) FROM EmployeeAddress
UNION ALL SELECT 'Attendance', COUNT(*) FROM Attendance
UNION ALL SELECT 'Payroll', COUNT(*) FROM Payroll
UNION ALL SELECT 'Deduction', COUNT(*) FROM Deduction
UNION ALL SELECT 'SSSContributionBracket', COUNT(*) FROM SSSContributionBracket
UNION ALL SELECT 'WithholdingTaxBracket', COUNT(*) FROM WithholdingTaxBracket;

SELECT
    table_name AS TableName,
    ROUND(data_length / 1024, 2) AS DataKB,
    ROUND(index_length / 1024, 2) AS IndexKB,
    table_rows AS EstimatedRows
FROM information_schema.tables
WHERE table_schema = 'payrollsystem_db'
  AND table_type = 'BASE TABLE'
ORDER BY (data_length + index_length) DESC;

-- -----------------------------------------------------------------------------
-- 2. ENABLE QUERY PROFILING (timing per query)
-- -----------------------------------------------------------------------------
SELECT '=== 2. Query profiling (duration in seconds) ===' AS Section;

SET profiling = 1;

-- Test Q1: Count all employees
SELECT COUNT(*) AS employee_count FROM Employee;

-- Test Q2: Employee list with department and status (common HR report)
SELECT e.EmployeeID, e.FirstName, e.LastName, d.DepartmentName, es.StatusName
FROM Employee e
JOIN Department d ON e.DepartmentID = d.DepartmentID
JOIN EmploymentStatus es ON e.StatusID = es.StatusID
ORDER BY e.EmployeeID;

-- Test Q3: Full employee profile (multi-table join - payroll prep)
SELECT e.EmployeeID, e.FirstName, e.LastName, s.BaseSalary,
       g.SSSNumber, g.PhilHealthNumber, g.TINNumber, g.PagIBIGNumber,
       COUNT(b.BenefitID) AS benefit_count, SUM(b.Amount) AS total_benefits
FROM Employee e
JOIN Salary s ON e.EmployeeID = s.EmployeeID
JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
LEFT JOIN Benefit b ON e.EmployeeID = b.EmployeeID
WHERE e.EmployeeID BETWEEN 10001 AND 10034
GROUP BY e.EmployeeID, e.FirstName, e.LastName, s.BaseSalary,
         g.SSSNumber, g.PhilHealthNumber, g.TINNumber, g.PagIBIGNumber;

-- Test Q4: SSS contribution lookup (statutory bracket search)
SELECT ContributionAmount, RangeDescription
FROM SSSContributionBracket
WHERE 52670.00 >= RangeFrom
  AND (RangeTo IS NULL OR 52670.00 <= RangeTo)
LIMIT 1;

-- Test Q5: Department summary with manager name
SELECT d.DepartmentName,
       COUNT(e.EmployeeID) AS employee_count,
       CONCAT(m.FirstName, ' ', m.LastName) AS manager_name
FROM Department d
LEFT JOIN Employee e ON d.DepartmentID = e.DepartmentID
LEFT JOIN Employee m ON d.ManagerID = m.EmployeeID
GROUP BY d.DepartmentID, d.DepartmentName, m.FirstName, m.LastName
ORDER BY d.DepartmentID;

-- Test Q6: Benefit totals by type (aggregation)
SELECT BenefitType, COUNT(*) AS records, SUM(Amount) AS total_amount
FROM Benefit
GROUP BY BenefitType
ORDER BY total_amount DESC;

-- Test Q7: Simulate payroll duplicate check (indexed unique lookup)
SELECT PayrollID
FROM Payroll
WHERE EmployeeID = 10001
  AND PayPeriodStart = '2024-06-01'
  AND PayPeriodEnd = '2024-06-15';

SHOW PROFILES;

-- Last 7 queries = Q1 through Q7 (check Duration column; expect low values on small dataset)

SET profiling = 0;

-- -----------------------------------------------------------------------------
-- 3. EXPLAIN ANALYZE - execution plan and actual timing (MySQL 8.0.18+)
-- -----------------------------------------------------------------------------
SELECT '=== 3. EXPLAIN ANALYZE (execution plans) ===' AS Section;

EXPLAIN ANALYZE
SELECT e.EmployeeID, e.FirstName, e.LastName, d.DepartmentName, s.BaseSalary
FROM Employee e
JOIN Department d ON e.DepartmentID = d.DepartmentID
JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID = 10001;

EXPLAIN ANALYZE
SELECT ContributionAmount
FROM SSSContributionBracket
WHERE 25000.00 >= RangeFrom
  AND (RangeTo IS NULL OR 25000.00 <= RangeTo)
LIMIT 1;

EXPLAIN ANALYZE
SELECT e.EmployeeID, COUNT(b.BenefitID) AS benefits
FROM Employee e
LEFT JOIN Benefit b ON e.EmployeeID = b.EmployeeID
GROUP BY e.EmployeeID;

-- -----------------------------------------------------------------------------
-- 4. MICRO-BENCHMARK (repeat operation 100,000 times)
-- -----------------------------------------------------------------------------
SELECT '=== 4. BENCHMARK (lower elapsed time = faster) ===' AS Section;

SELECT BENCHMARK(100000, (SELECT COUNT(*) FROM Employee)) AS bench_employee_count;
SELECT BENCHMARK(10000, (
    SELECT ContributionAmount FROM SSSContributionBracket
    WHERE 30000 >= RangeFrom AND (RangeTo IS NULL OR 30000 <= RangeTo) LIMIT 1
)) AS bench_sss_lookup;

-- -----------------------------------------------------------------------------
-- 5. INDEX USAGE CHECK (foreign keys should use indexes)
-- -----------------------------------------------------------------------------
SELECT '=== 5. Indexes on key tables ===' AS Section;

SHOW INDEX FROM Employee;
SHOW INDEX FROM Salary;
SHOW INDEX FROM Benefit;
SHOW INDEX FROM Payroll;

-- -----------------------------------------------------------------------------
-- 6. OPTIONAL STRESS COMPARISON
-- Run 09_performance_stress_seed.sql first, then re-run section 2 (profiling)
-- to compare Duration before vs after bulk attendance/payroll data.
-- -----------------------------------------------------------------------------

SELECT '=== PERFORMANCE TEST COMPLETE ===' AS Section, NOW() AS TestTime;

-- HOW TO READ RESULTS IN WORKBENCH:
-- 1. SHOW PROFILES -> Duration column (seconds). Under ~0.01s is excellent on this dataset.
-- 2. EXPLAIN ANALYZE -> look for "actual time" and avoid full table scans on large tables.
-- 3. BENCHMARK -> compare before/after stress seed.
-- 4. For presentation: screenshot SHOW PROFILES + one EXPLAIN ANALYZE result.
