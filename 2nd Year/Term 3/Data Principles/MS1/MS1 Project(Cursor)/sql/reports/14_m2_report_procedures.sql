-- =============================================================================
-- MotorPH Payroll System - M2: Report Stored Procedures
-- Wraps vw_EmployeePayslipReport and vw_EmployeePayrollSummaryReport
--
-- Payslip columns match the official MotorPH Employee Payslip template.
-- Monthly coverage = two cutoffs; deductions Option A (split).
-- Summary = monthly rollup of both cutoffs.
-- =============================================================================

USE payrollsystem_db;

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
        `Payslip No`,
        `Employee ID`,
        `Employee Name`,
        `Period Start Date`,
        `Period End Date`,
        `Employee Position/Department`,
        `Monthly Salary`,
        `Daily Rate`,
        `Days Worked`,
        `Overtime`,
        `Gross Income`,
        `Rice Subsidy`,
        `Phone Allowance`,
        `Clothing Allowance`,
        `Benefits`,
        `Social Security System`,
        `Philhealth`,
        `Pag-Ibig`,
        `Withholding Tax`,
        `Total Deductions`,
        `Summary Gross Income`,
        `Summary Benefits`,
        `Summary Deductions`,
        `Take Home Pay`
    FROM vw_EmployeePayslipReport
    WHERE `Employee ID` = p_EmployeeID
    ORDER BY `Period Start Date`;
END //

DROP PROCEDURE IF EXISTS sp_GetEmployeePayslipByPeriod //

CREATE PROCEDURE sp_GetEmployeePayslipByPeriod(
    IN p_EmployeeID INT,
    IN p_Start DATE,
    IN p_End DATE
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Employee WHERE EmployeeID = p_EmployeeID
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid EmployeeID: employee not found in Employee table';
    END IF;

    SELECT
        `Payslip No`,
        `Employee ID`,
        `Employee Name`,
        `Period Start Date`,
        `Period End Date`,
        `Employee Position/Department`,
        `Monthly Salary`,
        `Daily Rate`,
        `Days Worked`,
        `Overtime`,
        `Gross Income`,
        `Rice Subsidy`,
        `Phone Allowance`,
        `Clothing Allowance`,
        `Benefits`,
        `Social Security System`,
        `Philhealth`,
        `Pag-Ibig`,
        `Withholding Tax`,
        `Total Deductions`,
        `Summary Gross Income`,
        `Summary Benefits`,
        `Summary Deductions`,
        `Take Home Pay`
    FROM vw_EmployeePayslipReport
    WHERE `Employee ID` = p_EmployeeID
      AND `Period Start Date` = p_Start
      AND `Period End Date` = p_End;
END //

DROP PROCEDURE IF EXISTS sp_GetEmployeePayrollSummary //

CREATE PROCEDURE sp_GetEmployeePayrollSummary()
BEGIN
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
END //

DROP PROCEDURE IF EXISTS sp_GetPayrollSummaryByDepartment //

CREATE PROCEDURE sp_GetPayrollSummaryByDepartment()
BEGIN
    SELECT
        `Pay Period (Month)`,
        `Department`,
        `Total Employees`,
        `Total Gross Pay`,
        `Total Net Pay`
    FROM vw_EmployeePayrollSummaryByDepartment
    ORDER BY `Department`;
END //

DROP PROCEDURE IF EXISTS sp_GetPayrollGrandTotals //

CREATE PROCEDURE sp_GetPayrollGrandTotals()
BEGIN
    SELECT
        `Pay Period (Month)`,
        `Total Employees`,
        `Overall Gross Pay`,
        `Overall Net Pay`
    FROM vw_EmployeePayrollOverallTotals;
END //

DELIMITER ;

SELECT '=== sp_GetEmployeePayslip(10013) - both cutoffs (template layout) ===' AS Section;
CALL sp_GetEmployeePayslip(10013);

SELECT '=== sp_GetEmployeePayslipByPeriod(10013, 2024-06-01, 2024-06-15) ===' AS Section;
CALL sp_GetEmployeePayslipByPeriod(10013, '2024-06-01', '2024-06-15');

SELECT '=== sp_GetEmployeePayrollSummary() ===' AS Section;
CALL sp_GetEmployeePayrollSummary();

SELECT '=== sp_GetPayrollSummaryByDepartment() ===' AS Section;
CALL sp_GetPayrollSummaryByDepartment();

SELECT '=== sp_GetPayrollGrandTotals() ===' AS Section;
CALL sp_GetPayrollGrandTotals();

SELECT '=== Consistency: row counts (payslip 2x employees; summary 1x) ===' AS Section;

SELECT
    (SELECT COUNT(*) FROM vw_EmployeePayslipReport) AS PayslipViewRows,
    (SELECT COUNT(*) FROM vw_EmployeePayrollSummaryReport) AS SummaryViewRows,
    (SELECT COUNT(*) FROM Employee) AS EmployeeRows,
    IF(
        (SELECT COUNT(*) FROM vw_EmployeePayslipReport)
        = (SELECT COUNT(*) FROM Employee) * 2
        AND (SELECT COUNT(*) FROM vw_EmployeePayrollSummaryReport)
        = (SELECT COUNT(*) FROM Employee),
        'CONSISTENT',
        'MISMATCH'
    ) AS RowCountCheck;

SELECT '=== Consistency: Employee 10013 Take Home Pay (expected 13317.40 each cutoff) ===' AS Section;

SELECT
    `Employee ID`,
    `Period Start Date`,
    `Period End Date`,
    `Gross Income`,
    `Benefits`,
    `Total Deductions`,
    `Take Home Pay`,
    IF(`Take Home Pay` = 13317.40, 'CONSISTENT', 'CHECK VALUES') AS ExpectedTakeHomeCheck
FROM vw_EmployeePayslipReport
WHERE `Employee ID` = 10013
ORDER BY `Period Start Date`;

SELECT '=== Consistency: 10013 monthly Net Pay = sum of Take Home Pay ===' AS Section;

SELECT
    s.`Employee No`,
    s.`Net Pay` AS MonthlyNet,
    ROUND(SUM(p.`Take Home Pay`), 2) AS SumCutoffTakeHome,
    IF(s.`Net Pay` = ROUND(SUM(p.`Take Home Pay`), 2), 'CONSISTENT', 'MISMATCH') AS MonthlyRollupCheck
FROM vw_EmployeePayrollSummaryReport s
INNER JOIN vw_EmployeePayslipReport p ON p.`Employee ID` = s.`Employee No`
WHERE s.`Employee No` = 10013
GROUP BY s.`Employee No`, s.`Net Pay`;

SHOW CREATE PROCEDURE sp_GetEmployeePayslip;
SHOW CREATE PROCEDURE sp_GetEmployeePayslipByPeriod;
SHOW CREATE PROCEDURE sp_GetEmployeePayrollSummary;
SHOW CREATE PROCEDURE sp_GetPayrollSummaryByDepartment;
SHOW CREATE PROCEDURE sp_GetPayrollGrandTotals;
