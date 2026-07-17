# Employee Payslip Database Report — M2 Guide

## Overview

The **Employee Payslip Database Report** is implemented as a MySQL **VIEW** named `vw_EmployeePayslipReport`. Column names and formulas match the **official MotorPH Employee Payslip template** (Header, Earnings, Benefits, Deductions, Summary).

Monthly coverage uses **two payslips** (June 1–15 and June 16–30). Statutory deductions are **split (/2)** on each cutoff (Option A).

## Run order (after MS1)

1. `01_create_database.sql` through `05_seed_statutory.sql` (if not already deployed)
2. [`11_schema_semi_monthly_tax.sql`](11_schema_semi_monthly_tax.sql)
3. [`12_seed_payslip_pay_period.sql`](12_seed_payslip_pay_period.sql)
4. [`employee_payslip_report.sql`](employee_payslip_report.sql)
5. [`employees_payroll_summary_report.sql`](employees_payroll_summary_report.sql)
6. [`14_m2_report_procedures.sql`](14_m2_report_procedures.sql)

## Official template mapping

| Template section | View columns / formula |
|------------------|------------------------|
| Payslip No | `{EmployeeID}-{PeriodEndDate}` |
| Employee ID / Name | `Employee ID`, `Employee Name` (`Last, First`) |
| Period | `Period Start Date`, `Period End Date` |
| Position/Department | `Employee Position/Department` (`Position / Department`) |
| Monthly Salary | `Monthly Salary` |
| Daily Rate | `Monthly Salary / 20` |
| Days Worked | `10` per cutoff |
| Overtime | `0` (unless overtime is later seeded) |
| **Gross Income** | `Daily Rate × Days Worked + Overtime` (**benefits not included**) |
| Rice / Phone / Clothing | Full monthly benefit amounts |
| **Benefits** | Sum of the three allowances |
| SSS / PhilHealth / Pag-IBIG | Option A: monthly amount ÷ 2 |
| Withholding Tax | Semi-monthly bracket on taxable income |
| **Total Deductions** | SSS + PhilHealth + Pag-IBIG + Tax |
| **Take Home Pay** | `Gross Income + Benefits − Total Deductions` |

## Business rules

### Earnings (per cutoff)

| Field | Formula |
|-------|---------|
| Daily Rate | `Monthly Salary / 20` |
| Days Worked | `10` |
| Gross Income | `Daily Rate × Days Worked + Overtime` |

### Benefits (as shown on template)

Full monthly amounts from `Benefit` (not divided for display / take-home).

### Statutory deductions — Option A (Split)

| Deduction | Logic |
|-----------|--------|
| SSS | Bracket on monthly salary, then `/ 2` |
| PhilHealth | `MIN(salary × 3%, 1800) × 50% / 2` |
| Pag-IBIG | `MIN(salary × rate, 100) / 2` |

### Withholding tax

Taxable income = `Gross Income + (Benefits / 2) − SSS − PhilHealth − Pag-IBIG`  
(then look up `WithholdingTaxBracketSemiMonthly`).

### Take Home Pay

**Take Home Pay** = `Gross Income + Benefits − Total Deductions`

## Monthly coverage (two payslips)

| Cutoff | Pay period | Issue date |
|--------|------------|------------|
| 1 | 2024-06-01 to 2024-06-15 | 2024-06-16 |
| 2 | 2024-06-16 to 2024-06-30 | 2024-07-01 |

## Verification — Employee 10013 (Martha Farala) vs template

Monthly salary: **₱24,000** — expected **per cutoff**:

| Field | Expected |
|-------|----------|
| Daily Rate | 1,200.00 |
| Days Worked | 10 |
| **Gross Income** | **12,000.00** |
| Rice Subsidy | 1,500.00 |
| Phone Allowance | 500.00 |
| Clothing Allowance | 500.00 |
| **Benefits** | **2,500.00** |
| Social Security System | 540.00 |
| Philhealth | 180.00 |
| Pag-Ibig | 50.00 |
| Withholding Tax | 412.60 |
| **Total Deductions** | **1,182.60** |
| **Take Home Pay** | **13,317.40** |

```sql
SELECT *
FROM vw_EmployeePayslipReport
WHERE `Employee ID` = 10013
ORDER BY `Period Start Date`;

CALL sp_GetEmployeePayslip(10013);
CALL sp_GetEmployeePayslipByPeriod(10013, '2024-06-01', '2024-06-15');
```

## Submission screenshots checklist

1. `SHOW CREATE VIEW vw_EmployeePayslipReport`
2. Result for Employee 10013 (both cutoffs) showing template columns
3. `CALL sp_GetEmployeePayslipByPeriod(10013, '2024-06-01', '2024-06-15')` matching Take Home **13,317.40**
4. Optional: SCHEMAS → Views → `vw_EmployeePayslipReport`

## Files for Milestone 2 submission

| File | Purpose |
|------|---------|
| `11_schema_semi_monthly_tax.sql` | Semi-monthly tax table + seed |
| `12_seed_payslip_pay_period.sql` | Both June cutoffs (Payroll + Payslip seed) |
| `employee_payslip_report.sql` | **VIEW definition + verification queries** |
| `14_m2_report_procedures.sql` | Stored procedures |

Export `employee_payslip_report.sql` as your Employee Payslip Database Report SQL script.
