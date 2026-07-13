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

-- MotorPH Payroll System - M2: Standard semi-monthly pay period metadata
-- Provides PayPeriodStart, PayPeriodEnd, and Payslip IssueDate for the payslip view.
-- Earnings and deductions are computed in vw_EmployeePayslipReport.

USE payrollsystem_db;

SET @period_start = '2024-06-01';
SET @period_end = '2024-06-15';
SET @issue_date = '2024-06-16';

-- Workbench safe-update mode (Error 1175) blocks DELETE with subqueries
SET @OLD_SQL_SAFE_UPDATES = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

-- Remove prior seed for this pay period (safe for re-run)
DELETE FROM Deduction
WHERE DeductionID IN (
    SELECT DeductionID FROM (
        SELECT d.DeductionID
        FROM Deduction d
        INNER JOIN Payroll p ON d.PayrollID = p.PayrollID
        WHERE p.PayPeriodStart = @period_start AND p.PayPeriodEnd = @period_end
    ) AS to_delete_deductions
);

DELETE FROM Payslip
WHERE PayslipID IN (
    SELECT PayslipID FROM (
        SELECT ps.PayslipID
        FROM Payslip ps
        INNER JOIN Payroll p ON ps.PayrollID = p.PayrollID
        WHERE p.PayPeriodStart = @period_start AND p.PayPeriodEnd = @period_end
    ) AS to_delete_payslips
);

DELETE FROM Payroll
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID
        FROM Payroll
        WHERE PayPeriodStart = @period_start AND PayPeriodEnd = @period_end
    ) AS to_delete_payrolls
);

SET SQL_SAFE_UPDATES = @OLD_SQL_SAFE_UPDATES;

-- Placeholder gross/net (report view computes actual amounts)
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period_start,
    @period_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue_date
FROM Payroll
WHERE PayPeriodStart = @period_start AND PayPeriodEnd = @period_end;

SELECT COUNT(*) AS payroll_rows_seeded
FROM Payroll
WHERE PayPeriodStart = @period_start AND PayPeriodEnd = @period_end;

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
        CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
        e.FirstName,
        e.LastName,
        e.Position,
        d.DepartmentName,
        ea.StreetName AS Address,
        g.SSSNumber,
        g.PhilHealthNumber,
        g.TINNumber,
        g.PagIBIGNumber,
        s.BaseSalary AS MonthlyBasicSalary,
        COALESCE(p.PayPeriodStart, DATE('2024-06-01')) AS PayPeriodStart,
        COALESCE(p.PayPeriodEnd, DATE('2024-06-15')) AS PayPeriodEnd,
        COALESCE(ps.IssueDate, DATE('2024-06-16')) AS IssueDate,
        ROUND(s.BaseSalary / 2, 2) AS BasicPaySemi,
        ROUND(COALESCE(b.rice_monthly, 0) / 2, 2) AS RiceSubsidySemi,
        ROUND(COALESCE(b.phone_monthly, 0) / 2, 2) AS PhoneAllowanceSemi,
        ROUND(COALESCE(b.clothing_monthly, 0) / 2, 2) AS ClothingAllowanceSemi
    FROM Employee e
    INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
    INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
    INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
    LEFT JOIN EmployeeAddress ea ON e.EmployeeID = ea.EmployeeID
    LEFT JOIN Payroll p
        ON e.EmployeeID = p.EmployeeID
       AND p.PayPeriodStart = DATE('2024-06-01')
       AND p.PayPeriodEnd = DATE('2024-06-15')
    LEFT JOIN Payslip ps ON p.PayrollID = ps.PayrollID
    LEFT JOIN employee_benefits b ON e.EmployeeID = b.EmployeeID
),
earnings AS (
    SELECT
        pb.*,
        ROUND(
            pb.BasicPaySemi + pb.RiceSubsidySemi + pb.PhoneAllowanceSemi + pb.ClothingAllowanceSemi,
            2
        ) AS GrossPay
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
                    WHERE er.MonthlyBasicSalary >= sb.RangeFrom
                      AND (sb.RangeTo IS NULL OR er.MonthlyBasicSalary <= sb.RangeTo)
                    ORDER BY sb.RangeFrom DESC
                    LIMIT 1
                ),
                0
            ) / 2,
            2
        ) AS SSSDeduction,
        ROUND(LEAST(er.MonthlyBasicSalary * 0.03, 1800.00) * 0.5 / 2, 2) AS PhilHealthDeduction,
        ROUND(
            LEAST(
                er.MonthlyBasicSalary * CASE
                    WHEN er.MonthlyBasicSalary >= 1000.00 AND er.MonthlyBasicSalary <= 1500.00 THEN 0.0100
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
        ROUND(
            st.GrossPay - st.SSSDeduction - st.PhilHealthDeduction - st.PagibigDeduction,
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
    'Semi-Monthly' AS PayFrequency,
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
    ROUND(
        SSSDeduction + PhilHealthDeduction + PagibigDeduction + WithholdingTax,
        2
    ) AS TotalDeductions,
    ROUND(
        GrossPay - SSSDeduction - PhilHealthDeduction - PagibigDeduction - WithholdingTax,
        2
    ) AS NetPay
FROM taxed;

DROP VIEW IF EXISTS vw_EmployeePayrollSummaryReport;

CREATE VIEW vw_EmployeePayrollSummaryReport AS
SELECT
    EmployeeID,
    EmployeeName,
    Position,
    DepartmentName,
    PayPeriodStart,
    PayPeriodEnd,
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
    WithholdingTax,
    TotalDeductions,
    NetPay
FROM vw_EmployeePayslipReport;

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
        EmployeeID,
        EmployeeName,
        Position,
        DepartmentName,
        PayPeriodStart,
        PayPeriodEnd,
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
        WithholdingTax,
        TotalDeductions,
        NetPay
    FROM vw_EmployeePayrollSummaryReport
    ORDER BY EmployeeID;
END //

-- ---------------------------------------------------------------------------
-- sp_GetPayrollSummaryByDepartment: aggregated totals by department
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_GetPayrollSummaryByDepartment //

CREATE PROCEDURE sp_GetPayrollSummaryByDepartment()
BEGIN
    SELECT
        DepartmentName,
        COUNT(*) AS EmployeeCount,
        ROUND(SUM(GrossPay), 2) AS DepartmentGrossPay,
        ROUND(SUM(TotalDeductions), 2) AS DepartmentDeductions,
        ROUND(SUM(NetPay), 2) AS DepartmentNetPay
    FROM vw_EmployeePayrollSummaryReport
    GROUP BY DepartmentName
    ORDER BY DepartmentName;
END //

-- ---------------------------------------------------------------------------
-- sp_GetPayrollGrandTotals: period-level grand totals
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_GetPayrollGrandTotals //

CREATE PROCEDURE sp_GetPayrollGrandTotals()
BEGIN
    SELECT
        COUNT(*) AS EmployeeCount,
        PayPeriodStart,
        PayPeriodEnd,
        ROUND(SUM(GrossPay), 2) AS TotalGrossPay,
        ROUND(SUM(SSSDeduction), 2) AS TotalSSS,
        ROUND(SUM(PhilHealthDeduction), 2) AS TotalPhilHealth,
        ROUND(SUM(PagibigDeduction), 2) AS TotalPagibig,
        ROUND(SUM(WithholdingTax), 2) AS TotalWithholdingTax,
        ROUND(SUM(TotalDeductions), 2) AS TotalDeductions,
        ROUND(SUM(NetPay), 2) AS TotalNetPay
    FROM vw_EmployeePayrollSummaryReport
    GROUP BY PayPeriodStart, PayPeriodEnd;
END //

DELIMITER ;

SELECT 'M2 deploy complete. Test with:' AS NextStep;
SELECT COUNT(*) AS PayslipRows FROM vw_EmployeePayslipReport;
SELECT COUNT(*) AS SummaryRows FROM vw_EmployeePayrollSummaryReport;
CALL sp_GetEmployeePayslip(10013);
