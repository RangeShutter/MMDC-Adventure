-- =============================================================================
-- MotorPH Payroll System - M2: Employee Payslip Database Report
-- Creates VIEW vw_EmployeePayslipReport
--
-- Layout and formulas match the official MotorPH Employee Payslip template:
--   Header, Earnings, Benefits, Deductions, Summary (Take Home Pay)
--
-- Monthly coverage = TWO payslips (cutoffs) for June:
--   2024-06-01..15 and 2024-06-16..30
-- Statutory deductions: Option A (Split) — monthly SSS/PhilHealth/Pag-IBIG / 2
-- Tax: semi-monthly brackets after statutory deductions (per cutoff)
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

-- =============================================================================
-- VERIFICATION: Official template check — Employee 10013, cutoff 1
-- Expected (template):
--   Gross Income 12,000.00 | Benefits 2,500.00 | Total Deductions 1,182.60
--   Take Home Pay 13,317.40
-- =============================================================================

SELECT '=== Employee 10013 Payslips (both cutoffs; template layout) ===' AS Section;

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
WHERE `Employee ID` = 10013
ORDER BY `Period Start Date`;

SELECT '=== Template match check (10013, Jun 1-15) ===' AS Section;

SELECT
    `Gross Income`,
    `Benefits`,
    `Total Deductions`,
    `Take Home Pay`,
    IF(
        `Gross Income` = 12000.00
        AND `Benefits` = 2500.00
        AND `Total Deductions` = 1182.60
        AND `Take Home Pay` = 13317.40,
        'MATCHES TEMPLATE',
        'CHECK VALUES'
    ) AS TemplateCheck
FROM vw_EmployeePayslipReport
WHERE `Employee ID` = 10013
  AND `Period Start Date` = DATE('2024-06-01')
  AND `Period End Date` = DATE('2024-06-15');

SHOW CREATE VIEW vw_EmployeePayslipReport;
