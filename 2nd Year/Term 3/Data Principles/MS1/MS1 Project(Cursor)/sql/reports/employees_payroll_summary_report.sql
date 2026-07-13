-- =============================================================================
-- MotorPH Payroll System - M2: Employees Payroll Summary Database Report
-- Creates template-aligned detail and management-level aggregate views.
--
-- Prerequisites:
--   MS1 scripts (01-05), reports/11, reports/12,
--   reports/employee_payslip_report.sql (vw_EmployeePayslipReport must exist)
-- =============================================================================

USE payrollsystem_db;

-- PREREQUISITE: vw_EmployeePayslipReport must exist.
-- Error 1146 = you skipped employee_payslip_report.sql OR it failed earlier.
-- Quick fix: run 13_m2_run_all_reports.sql (creates both views in order).
-- Or run employee_payslip_report.sql first, confirm green checkmark on CREATE VIEW.

DROP VIEW IF EXISTS vw_EmployeePayrollOverallTotals;
DROP VIEW IF EXISTS vw_EmployeePayrollSummaryByDepartment;
DROP VIEW IF EXISTS vw_EmployeePayrollSummaryReport;

CREATE VIEW vw_EmployeePayrollSummaryReport AS
SELECT
    EmployeeID AS `Employee No`,
    EmployeeName AS `Employee Full Name`,
    Position AS `Position`,
    DepartmentName AS `Department`,
    GrossPay AS `Gross Income`,
    SSSNumber AS `Social Security No.`,
    SSSDeduction AS `Social Security Contribution`,
    PhilHealthNumber AS `Philhealth No.`,
    PhilHealthDeduction AS `Philhealth Contribution`,
    PagIBIGNumber AS `Pag-ibig No.`,
    PagibigDeduction AS `Pag-Ibig Contribution`,
    TINNumber AS `TIN`,
    WithholdingTax AS `Withholding Tax`,
    NetPay AS `Net Pay`
FROM vw_EmployeePayslipReport;

-- =============================================================================
-- MANAGEMENT VIEW 1: Department-level summary
-- =============================================================================

CREATE VIEW vw_EmployeePayrollSummaryByDepartment AS
SELECT
    `Department`,
    COUNT(*) AS `Total Employees`,
    ROUND(SUM(`Gross Income`), 2) AS `Total Gross Pay`,
    ROUND(SUM(`Net Pay`), 2) AS `Total Net Pay`
FROM vw_EmployeePayrollSummaryReport
GROUP BY `Department`;

-- =============================================================================
-- MANAGEMENT VIEW 2: Overall payroll totals
-- =============================================================================

CREATE VIEW vw_EmployeePayrollOverallTotals AS
SELECT
    COUNT(*) AS `Total Employees`,
    ROUND(SUM(`Gross Income`), 2) AS `Overall Gross Pay`,
    ROUND(SUM(`Net Pay`), 2) AS `Overall Net Pay`
FROM vw_EmployeePayrollSummaryReport;

-- =============================================================================
-- VERIFICATION: Official template detail summary
-- =============================================================================

SELECT '=== Employee Payroll Summary Report (Official Template Columns) ===' AS Section;

SELECT
    `Employee No`,
    `Employee Full Name`,
    `Position`,
    `Department`,
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

-- =============================================================================
-- VERIFICATION: Department-level management summary
-- =============================================================================

SELECT '=== Payroll Management Summary by Department ===' AS Section;

SELECT
    `Department`,
    `Total Employees`,
    `Total Gross Pay`,
    `Total Net Pay`
FROM vw_EmployeePayrollSummaryByDepartment
ORDER BY `Department`;

-- =============================================================================
-- VERIFICATION: Overall payroll totals
-- =============================================================================

SELECT '=== Payroll Management Overall Totals ===' AS Section;

SELECT
    `Total Employees`,
    `Overall Gross Pay`,
    `Overall Net Pay`
FROM vw_EmployeePayrollOverallTotals;

-- Export view definitions (for submission)
SHOW CREATE VIEW vw_EmployeePayrollSummaryReport;
SHOW CREATE VIEW vw_EmployeePayrollSummaryByDepartment;
SHOW CREATE VIEW vw_EmployeePayrollOverallTotals;
