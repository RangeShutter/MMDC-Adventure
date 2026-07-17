-- =============================================================================
-- MotorPH M2 - Full deploy: tax table + pay period seed + views + procedures
--
-- Prerequisites: MS1 scripts 01-05 only (this file includes 11 and 12)
-- Regenerate after edits: python build_m2_reports.py
-- =============================================================================

-- MotorPH Payroll System - M2: Semi-monthly withholding tax brackets
-- Converted from monthly MotorPH/BIR table (document/Witholding Tax.md)
-- Rules: Min/Max/Base/ExcessOver divided by 2; ExcessRate unchanged

USE payrollsystem_db;

DROP TABLE IF EXISTS WithholdingTaxBracketSemiMonthly;

CREATE TABLE WithholdingTaxBracketSemiMonthly (
    BracketID INT NOT NULL AUTO_INCREMENT,
    SemiMonthlyRateMin DECIMAL(12, 2) NOT NULL,
    SemiMonthlyRateMax DECIMAL(12, 2) NULL,
    TaxRuleDescription VARCHAR(255) NOT NULL,
    BaseTax DECIMAL(12, 2) NOT NULL DEFAULT 0,
    ExcessRate DECIMAL(5, 4) NULL,
    ExcessOver DECIMAL(12, 2) NULL,
    PRIMARY KEY (BracketID),
    CONSTRAINT chk_semi_withholding_base CHECK (BaseTax >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Monthly source -> semi-monthly (Option A payroll; tax brackets halved per M2 spec)
INSERT INTO WithholdingTaxBracketSemiMonthly
    (SemiMonthlyRateMin, SemiMonthlyRateMax, TaxRuleDescription, BaseTax, ExcessRate, ExcessOver)
VALUES
(0.00, 10416.00,
 '10,416 and below - No withholding tax (from monthly 20,832)',
 0.00, NULL, NULL),
(10417.00, 16666.00,
 '10,417 to 16,666 - 20% in excess of 10,417 (from monthly 20,833-33,332)',
 0.00, 0.2000, 10417.00),
(16667.00, 33333.00,
 '16,667 to 33,333 - 1,250 plus 25% in excess of 16,667 (from monthly 33,333-66,666)',
 1250.00, 0.2500, 16667.00),
(33334.00, 83333.00,
 '33,334 to 83,333 - 5,416.50 plus 30% in excess of 33,334 (from monthly 66,667-166,666)',
 5416.50, 0.3000, 33334.00),
(83334.00, 333333.00,
 '83,334 to 333,333 - 20,416.67 plus 32% in excess of 83,334 (from monthly 166,667-666,666)',
 20416.67, 0.3200, 83334.00),
(333334.00, NULL,
 '333,334 and above - 100,416.67 plus 35% in excess of 333,334 (from monthly 666,667+)',
 100416.67, 0.3500, 333334.00);

-- MotorPH Payroll System - M2: Monthly coverage via two semi-monthly cutoffs
-- Seeds Payroll + Payslip for both June cutoffs (two payslips per employee).
-- Earnings and deductions are computed in vw_EmployeePayslipReport (Option A split).

USE payrollsystem_db;

-- Cutoff 1: first half of June
SET @period1_start = '2024-06-01';
SET @period1_end = '2024-06-15';
SET @issue1 = '2024-06-16';

-- Cutoff 2: second half of June
SET @period2_start = '2024-06-16';
SET @period2_end = '2024-06-30';
SET @issue2 = '2024-07-01';

-- Workbench safe-update mode (Error 1175) blocks DELETE with subqueries
SET @OLD_SQL_SAFE_UPDATES = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

-- Remove prior seed for both June cutoffs (safe for re-run)
DELETE FROM Deduction
WHERE DeductionID IN (
    SELECT DeductionID FROM (
        SELECT d.DeductionID
        FROM Deduction d
        INNER JOIN Payroll p ON d.PayrollID = p.PayrollID
        WHERE (
            (p.PayPeriodStart = @period1_start AND p.PayPeriodEnd = @period1_end)
            OR (p.PayPeriodStart = @period2_start AND p.PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_deductions
);

DELETE FROM Payslip
WHERE PayslipID IN (
    SELECT PayslipID FROM (
        SELECT ps.PayslipID
        FROM Payslip ps
        INNER JOIN Payroll p ON ps.PayrollID = p.PayrollID
        WHERE (
            (p.PayPeriodStart = @period1_start AND p.PayPeriodEnd = @period1_end)
            OR (p.PayPeriodStart = @period2_start AND p.PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_payslips
);

DELETE FROM Payroll
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID
        FROM Payroll
        WHERE (
            (PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end)
            OR (PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_payrolls
);

SET SQL_SAFE_UPDATES = @OLD_SQL_SAFE_UPDATES;

-- Placeholder gross/net (report view computes actual amounts)
-- Cutoff 1
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period1_start,
    @period1_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue1
FROM Payroll
WHERE PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end;

-- Cutoff 2
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period2_start,
    @period2_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue2
FROM Payroll
WHERE PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end;

SELECT '=== June payroll seed (two cutoffs) ===' AS Section;

SELECT PayPeriodStart, PayPeriodEnd, COUNT(*) AS payroll_rows
FROM Payroll
WHERE (
    (PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end)
    OR (PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end)
)
GROUP BY PayPeriodStart, PayPeriodEnd
ORDER BY PayPeriodStart;

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
