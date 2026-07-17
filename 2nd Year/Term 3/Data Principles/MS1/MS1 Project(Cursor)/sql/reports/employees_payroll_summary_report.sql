-- =============================================================================
-- MotorPH Payroll System - M2: Employees Payroll Summary Database Report
-- Creates template-aligned MONTHLY detail and management-level aggregate views.
--
-- Monthly summary = rollup of BOTH June cutoffs from vw_EmployeePayslipReport
-- (one row per employee for the full month).
--
-- Official summary headers (aliases):
--   Employee No, Employee Full Name, Position, Department, Pay Period (Month),
--   Gross Income, Social Security No., Social Security Contribution,
--   Philhealth No., Philhealth Contribution, Pag-ibig No., Pag-Ibig Contribution,
--   TIN, Withholding Tax, Net Pay
-- =============================================================================

USE payrollsystem_db;

DROP VIEW IF EXISTS vw_EmployeePayrollOverallTotals;
DROP VIEW IF EXISTS vw_EmployeePayrollSummaryByDepartment;
DROP VIEW IF EXISTS vw_EmployeePayrollSummaryReport;

CREATE VIEW vw_EmployeePayrollSummaryReport AS
SELECT
    `Employee ID` AS `Employee No`,
    MAX(`Employee Name`) AS `Employee Full Name`,
    MAX(Position) AS `Position`,
    MAX(DepartmentName) AS `Department`,
    DATE_FORMAT(MIN(`Period Start Date`), '%M %Y') AS `Pay Period (Month)`,
    ROUND(SUM(`Gross Income`), 2) AS `Gross Income`,
    MAX(`Social Security No.`) AS `Social Security No.`,
    ROUND(SUM(`Social Security System`), 2) AS `Social Security Contribution`,
    MAX(`Philhealth No.`) AS `Philhealth No.`,
    ROUND(SUM(`Philhealth`), 2) AS `Philhealth Contribution`,
    MAX(`Pag-ibig No.`) AS `Pag-ibig No.`,
    ROUND(SUM(`Pag-Ibig`), 2) AS `Pag-Ibig Contribution`,
    MAX(`TIN`) AS `TIN`,
    ROUND(SUM(`Withholding Tax`), 2) AS `Withholding Tax`,
    ROUND(SUM(`Take Home Pay`), 2) AS `Net Pay`
FROM vw_EmployeePayslipReport
GROUP BY `Employee ID`;

CREATE VIEW vw_EmployeePayrollSummaryByDepartment AS
SELECT
    `Pay Period (Month)`,
    `Department`,
    COUNT(*) AS `Total Employees`,
    ROUND(SUM(`Gross Income`), 2) AS `Total Gross Pay`,
    ROUND(SUM(`Net Pay`), 2) AS `Total Net Pay`
FROM vw_EmployeePayrollSummaryReport
GROUP BY `Pay Period (Month)`, `Department`;

CREATE VIEW vw_EmployeePayrollOverallTotals AS
SELECT
    `Pay Period (Month)`,
    COUNT(*) AS `Total Employees`,
    ROUND(SUM(`Gross Income`), 2) AS `Overall Gross Pay`,
    ROUND(SUM(`Net Pay`), 2) AS `Overall Net Pay`
FROM vw_EmployeePayrollSummaryReport
GROUP BY `Pay Period (Month)`;

-- =============================================================================
-- VERIFICATION: Official template monthly detail summary
-- =============================================================================

SELECT '=== Employee Payroll Summary Report (Monthly Rollup) ===' AS Section;

SELECT
    `Employee No`,
    `Employee Full Name`,
    `Position`,
    `Department`,
    `Pay Period (Month)`,
    `Gross Income`,
    `Social Security No.`,
    `Social Security Contribution`,
    `Philhealth No.`,
    `Philhealth Contribution`,
    `Pag-ibig No.`,
    `Pag-Ibig Contribution`,
    `TIN`,
    `Withholding Tax`,
    `Net Pay`
FROM vw_EmployeePayrollSummaryReport
ORDER BY `Employee No`;

SELECT '=== Payroll Management Summary by Department ===' AS Section;

SELECT
    `Pay Period (Month)`,
    `Department`,
    `Total Employees`,
    `Total Gross Pay`,
    `Total Net Pay`
FROM vw_EmployeePayrollSummaryByDepartment
ORDER BY `Department`;

SELECT '=== Payroll Management Overall Totals ===' AS Section;

SELECT
    `Pay Period (Month)`,
    `Total Employees`,
    `Overall Gross Pay`,
    `Overall Net Pay`
FROM vw_EmployeePayrollOverallTotals;

-- =============================================================================
-- ALIASES / COLUMN HEADER CHECKER (official payroll summary template)
-- =============================================================================

SELECT '=== Aliases Column Header Checker ===' AS Section;

SELECT
    expected.OrdinalPosition,
    expected.ExpectedHeader,
    actual.COLUMN_NAME AS ActualHeader,
    IF(
        actual.COLUMN_NAME IS NOT NULL
        AND actual.COLUMN_NAME = expected.ExpectedHeader,
        'PASS',
        'FAIL'
    ) AS HeaderCheck
FROM (
    SELECT 1 AS OrdinalPosition, 'Employee No' AS ExpectedHeader
    UNION ALL SELECT 2, 'Employee Full Name'
    UNION ALL SELECT 3, 'Position'
    UNION ALL SELECT 4, 'Department'
    UNION ALL SELECT 5, 'Pay Period (Month)'
    UNION ALL SELECT 6, 'Gross Income'
    UNION ALL SELECT 7, 'Social Security No.'
    UNION ALL SELECT 8, 'Social Security Contribution'
    UNION ALL SELECT 9, 'Philhealth No.'
    UNION ALL SELECT 10, 'Philhealth Contribution'
    UNION ALL SELECT 11, 'Pag-ibig No.'
    UNION ALL SELECT 12, 'Pag-Ibig Contribution'
    UNION ALL SELECT 13, 'TIN'
    UNION ALL SELECT 14, 'Withholding Tax'
    UNION ALL SELECT 15, 'Net Pay'
) AS expected
LEFT JOIN information_schema.COLUMNS actual
    ON actual.TABLE_SCHEMA = 'payrollsystem_db'
   AND actual.TABLE_NAME = 'vw_EmployeePayrollSummaryReport'
   AND actual.COLUMN_NAME = expected.ExpectedHeader
ORDER BY expected.OrdinalPosition;

SELECT
    IF(
        (
            SELECT COUNT(*)
            FROM (
                SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
                UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
                UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
            ) x
        ) = (
            SELECT COUNT(*)
            FROM information_schema.COLUMNS
            WHERE TABLE_SCHEMA = 'payrollsystem_db'
              AND TABLE_NAME = 'vw_EmployeePayrollSummaryReport'
              AND COLUMN_NAME IN (
                  'Employee No',
                  'Employee Full Name',
                  'Position',
                  'Department',
                  'Pay Period (Month)',
                  'Gross Income',
                  'Social Security No.',
                  'Social Security Contribution',
                  'Philhealth No.',
                  'Philhealth Contribution',
                  'Pag-ibig No.',
                  'Pag-Ibig Contribution',
                  'TIN',
                  'Withholding Tax',
                  'Net Pay'
              )
        ),
        'ALL 15 OFFICIAL SUMMARY HEADERS PRESENT',
        'MISSING OR MISMATCHED HEADERS'
    ) AS HeaderCheckerSummary;

SELECT '=== Consistency: 10013 monthly Net Pay = sum of two Take Home Pay ===' AS Section;

SELECT
    s.`Employee No`,
    s.`Pay Period (Month)`,
    s.`Gross Income` AS MonthlyGross,
    s.`Net Pay` AS MonthlyNet,
    ROUND(SUM(p.`Gross Income`), 2) AS SumCutoffGross,
    ROUND(SUM(p.`Take Home Pay`), 2) AS SumCutoffTakeHome,
    IF(
        s.`Net Pay` = ROUND(SUM(p.`Take Home Pay`), 2)
        AND s.`Gross Income` = ROUND(SUM(p.`Gross Income`), 2),
        'CONSISTENT',
        'MISMATCH'
    ) AS MonthlyRollupCheck
FROM vw_EmployeePayrollSummaryReport s
INNER JOIN vw_EmployeePayslipReport p ON p.`Employee ID` = s.`Employee No`
WHERE s.`Employee No` = 10013
GROUP BY s.`Employee No`, s.`Pay Period (Month)`, s.`Gross Income`, s.`Net Pay`;

SHOW CREATE VIEW vw_EmployeePayrollSummaryReport;
SHOW CREATE VIEW vw_EmployeePayrollSummaryByDepartment;
SHOW CREATE VIEW vw_EmployeePayrollOverallTotals;
