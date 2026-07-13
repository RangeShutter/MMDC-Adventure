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
