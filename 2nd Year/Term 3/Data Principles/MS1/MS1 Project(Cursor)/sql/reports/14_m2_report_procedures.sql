-- =============================================================================
-- MotorPH Payroll System - M2: Report Stored Procedures
-- Wraps vw_EmployeePayslipReport and vw_EmployeePayrollSummaryReport
-- (views remain the single source of truth for all calculations)
--
-- Prerequisites (run in order):
--   MS1 scripts 01-05, reports/11, reports/12,
--   employee_payslip_report.sql, employees_payroll_summary_report.sql
-- =============================================================================

USE payrollsystem_db;

-- ---------------------------------------------------------------------------
-- sp_GetEmployeePayslip: single-employee payslip report
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_GetEmployeePayslip;

DELIMITER //

CREATE PROCEDURE sp_GetEmployeePayslip(IN p_EmployeeID INT)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Employee WHERE EmployeeID = p_EmployeeID
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid EmployeeID: employee not found in Employee table';
    END IF;

    SELECT
        EmployeeID,
        EmployeeName,
        FirstName,
        LastName,
        Position,
        DepartmentName,
        Address,
        SSSNumber,
        PhilHealthNumber,
        TINNumber,
        PagIBIGNumber,
        PayPeriodStart,
        PayPeriodEnd,
        IssueDate,
        PayFrequency,
        MonthlyBasicSalary,
        BasicPaySemi,
        RiceSubsidySemi,
        PhoneAllowanceSemi,
        ClothingAllowanceSemi,
        GrossPay,
        SSSDeduction,
        PhilHealthDeduction,
        PagibigDeduction,
        TaxableIncome,
        WithholdingTax,
        TotalDeductions,
        NetPay
    FROM vw_EmployeePayslipReport
    WHERE EmployeeID = p_EmployeeID;
END //

-- ---------------------------------------------------------------------------
-- sp_GetEmployeePayrollSummary: all employees for the pay period
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_GetEmployeePayrollSummary //

CREATE PROCEDURE sp_GetEmployeePayrollSummary()
BEGIN
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
END //

-- ---------------------------------------------------------------------------
-- sp_GetPayrollSummaryByDepartment: aggregated totals by department
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_GetPayrollSummaryByDepartment //

CREATE PROCEDURE sp_GetPayrollSummaryByDepartment()
BEGIN
    SELECT
        `Department`,
        `Total Employees`,
        `Total Gross Pay`,
        `Total Net Pay`
    FROM vw_EmployeePayrollSummaryByDepartment
    ORDER BY `Department`;
END //

-- ---------------------------------------------------------------------------
-- sp_GetPayrollGrandTotals: period-level grand totals
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_GetPayrollGrandTotals //

CREATE PROCEDURE sp_GetPayrollGrandTotals()
BEGIN
    SELECT
        `Total Employees`,
        `Overall Gross Pay`,
        `Overall Net Pay`
    FROM vw_EmployeePayrollOverallTotals;
END //

DELIMITER ;

-- =============================================================================
-- VERIFICATION: Procedure calls (same output as querying views directly)
-- =============================================================================

SELECT '=== sp_GetEmployeePayslip(10013) ===' AS Section;
CALL sp_GetEmployeePayslip(10013);

SELECT '=== sp_GetEmployeePayrollSummary() - first 5 rows ===' AS Section;
CALL sp_GetEmployeePayrollSummary();

SELECT '=== sp_GetPayrollSummaryByDepartment() ===' AS Section;
CALL sp_GetPayrollSummaryByDepartment();

SELECT '=== sp_GetPayrollGrandTotals() ===' AS Section;
CALL sp_GetPayrollGrandTotals();

-- =============================================================================
-- CONSISTENCY: Views and procedures use the same underlying data
-- =============================================================================

SELECT '=== Consistency: payslip vs summary row counts ===' AS Section;

SELECT
    (SELECT COUNT(*) FROM vw_EmployeePayslipReport) AS PayslipViewRows,
    (SELECT COUNT(*) FROM vw_EmployeePayrollSummaryReport) AS SummaryViewRows,
    IF(
        (SELECT COUNT(*) FROM vw_EmployeePayslipReport)
        = (SELECT COUNT(*) FROM vw_EmployeePayrollSummaryReport),
        'CONSISTENT',
        'MISMATCH'
    ) AS RowCountCheck;

SELECT '=== Consistency: total NetPay payslip view vs summary view ===' AS Section;

SELECT
    ROUND(SUM(p.NetPay), 2) AS TotalNetFromPayslipView,
    ROUND(SUM(s.`Net Pay`), 2) AS TotalNetFromSummaryView,
    IF(
        ROUND(SUM(p.NetPay), 2) = ROUND(SUM(s.`Net Pay`), 2),
        'CONSISTENT',
        'MISMATCH'
    ) AS TotalNetPayCheck
FROM vw_EmployeePayslipReport p
INNER JOIN vw_EmployeePayrollSummaryReport s ON p.EmployeeID = s.`Employee No`;

SELECT '=== Consistency: Employee 10013 NetPay (expected 12067.40) ===' AS Section;

SELECT
    EmployeeID,
    NetPay AS ViewNetPay,
    IF(NetPay = 12067.40, 'CONSISTENT', 'CHECK VALUES') AS ExpectedNetPayCheck
FROM vw_EmployeePayslipReport
WHERE EmployeeID = 10013;

-- sp_GetEmployeePayslip(10013) returns the same row as the query above.

-- Export procedure definitions (for submission)
SHOW CREATE PROCEDURE sp_GetEmployeePayslip;
SHOW CREATE PROCEDURE sp_GetEmployeePayrollSummary;
SHOW CREATE PROCEDURE sp_GetPayrollSummaryByDepartment;
SHOW CREATE PROCEDURE sp_GetPayrollGrandTotals;
