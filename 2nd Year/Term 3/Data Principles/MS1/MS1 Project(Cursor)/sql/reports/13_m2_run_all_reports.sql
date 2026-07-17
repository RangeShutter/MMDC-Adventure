-- =============================================================================
-- MotorPH M2 - Run BOTH report views + stored procedures (MySQL Workbench)
--
-- Use this file if employees_payroll_summary_report.sql fails with:
--   Error 1146: vw_employeepayslipreport doesn't exist
--
-- Prerequisites: MS1 scripts 01-05, then 11, then 12
-- Regenerate after edits: python build_m2_reports.py
-- =============================================================================

USE payrollsystem_db;

DROP VIEW IF EXISTS vw_EmployeePayslipReport;

CREATE VIEW vw_EmployeePayslipReport AS
WITH employee_benefits AS (
    SELECT
        EmployeeID,
        SUM(CASE WHEN BenefitType = 'Rice Subsidy' THEN Amount ELSE 0 END) AS rice_monthly,
        SUM(CASE WHEN BenefitType = 'Phone Allowance' THEN Amount ELSE 0 END) AS phone_monthly,
        SUM(CASE WHEN BenefitType = 'Clothing Allowance' THEN Amount ELSE 0 END) AS clothing_monthly
    FROM Benefit
    GROUP BY EmployeeID
),
payslip_base AS (
    SELECT
        e.EmployeeID,
        CONCAT(e.LastName, ', ', e.FirstName) AS EmployeeName,
        e.FirstName,
        e.LastName,
        e.Position,
        d.DepartmentName,
        CONCAT(e.Position, ' / ', d.DepartmentName) AS PositionDepartment,
        ea.StreetName AS Address,
        g.SSSNumber,
        g.PhilHealthNumber,
        g.TINNumber,
        g.PagIBIGNumber,
        s.BaseSalary AS MonthlySalary,
        p.PayPeriodStart,
        p.PayPeriodEnd,
        ps.IssueDate,
        -- Official template: Daily Rate = Monthly Salary / 20 working days
        ROUND(s.BaseSalary / 20, 2) AS DailyRate,
        -- Semi-monthly cutoff assumes 10 worked days (half of 20)
        10 AS DaysWorked,
        0.00 AS Overtime,
        COALESCE(b.rice_monthly, 0) AS RiceSubsidy,
        COALESCE(b.phone_monthly, 0) AS PhoneAllowance,
        COALESCE(b.clothing_monthly, 0) AS ClothingAllowance
    FROM Employee e
    INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
    INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
    INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
    INNER JOIN Payroll p
        ON e.EmployeeID = p.EmployeeID
       AND (
            (p.PayPeriodStart = DATE('2024-06-01') AND p.PayPeriodEnd = DATE('2024-06-15'))
            OR (p.PayPeriodStart = DATE('2024-06-16') AND p.PayPeriodEnd = DATE('2024-06-30'))
       )
    INNER JOIN Payslip ps ON p.PayrollID = ps.PayrollID
    LEFT JOIN EmployeeAddress ea ON e.EmployeeID = ea.EmployeeID
    LEFT JOIN employee_benefits b ON e.EmployeeID = b.EmployeeID
),
earnings AS (
    SELECT
        pb.*,
        -- Template GROSS INCOME = Daily Rate × Days Worked + Overtime (benefits NOT included)
        ROUND(pb.DailyRate * pb.DaysWorked + pb.Overtime, 2) AS GrossIncome,
        ROUND(
            pb.RiceSubsidy + pb.PhoneAllowance + pb.ClothingAllowance,
            2
        ) AS BenefitsTotal
    FROM payslip_base pb
),
statutory AS (
    SELECT
        er.*,
        ROUND(
            COALESCE(
                (
                    SELECT sb.ContributionAmount
                    FROM SSSContributionBracket sb
                    WHERE er.MonthlySalary >= sb.RangeFrom
                      AND (sb.RangeTo IS NULL OR er.MonthlySalary <= sb.RangeTo)
                    ORDER BY sb.RangeFrom DESC
                    LIMIT 1
                ),
                0
            ) / 2,
            2
        ) AS SSSDeduction,
        ROUND(LEAST(er.MonthlySalary * 0.03, 1800.00) * 0.5 / 2, 2) AS PhilHealthDeduction,
        ROUND(
            LEAST(
                er.MonthlySalary * CASE
                    WHEN er.MonthlySalary >= 1000.00 AND er.MonthlySalary <= 1500.00 THEN 0.0100
                    ELSE 0.0200
                END,
                100.00
            ) / 2,
            2
        ) AS PagibigDeduction
    FROM earnings er
),
taxable AS (
    SELECT
        st.*,
        -- Taxable base: work gross + half of monthly benefits − statutory (keeps Option A tax)
        ROUND(
            st.GrossIncome
            + (st.BenefitsTotal / 2)
            - st.SSSDeduction
            - st.PhilHealthDeduction
            - st.PagibigDeduction,
            2
        ) AS TaxableIncome
    FROM statutory st
),
taxed AS (
    SELECT
        tx.*,
        ROUND(
            COALESCE(
                (
                    SELECT
                        t.BaseTax
                        + GREATEST(tx.TaxableIncome - COALESCE(t.ExcessOver, 0), 0)
                          * COALESCE(t.ExcessRate, 0)
                    FROM WithholdingTaxBracketSemiMonthly t
                    WHERE tx.TaxableIncome >= t.SemiMonthlyRateMin
                      AND (
                          t.SemiMonthlyRateMax IS NULL
                          OR tx.TaxableIncome <= t.SemiMonthlyRateMax
                      )
                    ORDER BY t.SemiMonthlyRateMin DESC
                    LIMIT 1
                ),
                0
            ),
            2
        ) AS WithholdingTax
    FROM taxable tx
)
SELECT
    -- Header (official template labels)
    CONCAT(
        EmployeeID, '-',
        DATE_FORMAT(PayPeriodEnd, '%Y-%m-%d')
    ) AS `Payslip No`,
    EmployeeID AS `Employee ID`,
    EmployeeName AS `Employee Name`,
    PayPeriodStart AS `Period Start Date`,
    PayPeriodEnd AS `Period End Date`,
    PositionDepartment AS `Employee Position/Department`,
    -- Earnings
    MonthlySalary AS `Monthly Salary`,
    DailyRate AS `Daily Rate`,
    DaysWorked AS `Days Worked`,
    Overtime AS `Overtime`,
    GrossIncome AS `Gross Income`,
    -- Benefits (full monthly amounts as on official template)
    RiceSubsidy AS `Rice Subsidy`,
    PhoneAllowance AS `Phone Allowance`,
    ClothingAllowance AS `Clothing Allowance`,
    BenefitsTotal AS `Benefits`,
    -- Deductions (Option A split)
    SSSNumber AS `Social Security No.`,
    SSSDeduction AS `Social Security System`,
    PhilHealthNumber AS `Philhealth No.`,
    PhilHealthDeduction AS `Philhealth`,
    PagIBIGNumber AS `Pag-ibig No.`,
    PagibigDeduction AS `Pag-Ibig`,
    TINNumber AS `TIN`,
    WithholdingTax AS `Withholding Tax`,
    ROUND(
        SSSDeduction + PhilHealthDeduction + PagibigDeduction + WithholdingTax,
        2
    ) AS `Total Deductions`,
    -- Summary
    GrossIncome AS `Summary Gross Income`,
    BenefitsTotal AS `Summary Benefits`,
    ROUND(
        SSSDeduction + PhilHealthDeduction + PagibigDeduction + WithholdingTax,
        2
    ) AS `Summary Deductions`,
    -- TAKE HOME PAY = Gross Income + Benefits − Deductions (official template)
    ROUND(
        GrossIncome
        + BenefitsTotal
        - SSSDeduction
        - PhilHealthDeduction
        - PagibigDeduction
        - WithholdingTax,
        2
    ) AS `Take Home Pay`,
    -- Supporting fields used by summary/procedures
    Position,
    DepartmentName,
    Address,
    IssueDate,
    'Semi-Monthly' AS PayFrequency,
    TaxableIncome
FROM taxed;

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

SELECT 'M2 deploy complete. Test with:' AS NextStep;
SELECT COUNT(*) AS PayslipRows FROM vw_EmployeePayslipReport;  -- expect 68 (34 x 2 cutoffs)
SELECT COUNT(*) AS SummaryRows FROM vw_EmployeePayrollSummaryReport;  -- expect 34 (monthly)
CALL sp_GetEmployeePayslip(10013);
CALL sp_GetEmployeePayslipByPeriod(10013, '2024-06-01', '2024-06-15');
-- Expected Take Home Pay for 10013 per cutoff: 13317.40
