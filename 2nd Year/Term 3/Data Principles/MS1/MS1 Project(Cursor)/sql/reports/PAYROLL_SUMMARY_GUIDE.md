# Employees Payroll Summary Database Report — Guide

## Purpose

Implements an official-template **monthly** payroll summary layer for management reporting while reusing `vw_EmployeePayslipReport` as the computation source.

- **Payslip layer:** two semi-monthly cutoffs per employee (earnings/deductions split)
- **Detail summary:** one monthly row per employee (sum of both cutoffs) with official template columns
- **Management summary:** department totals and overall payroll totals

## Run order

1. Complete payslip setup first ([`employee_payslip_report.sql`](employee_payslip_report.sql))
2. Run [`employees_payroll_summary_report.sql`](employees_payroll_summary_report.sql)
3. Run [`14_m2_report_procedures.sql`](14_m2_report_procedures.sql)

## Views and procedures

| Layer | Object | Role |
|-------|--------|------|
| View | `vw_EmployeePayrollSummaryReport` | Monthly rollup (1 row/employee) using official template column names |
| View | `vw_EmployeePayrollSummaryByDepartment` | Department-level totals (`Total Employees`, `Total Gross Pay`, `Total Net Pay`) |
| View | `vw_EmployeePayrollOverallTotals` | Overall monthly payroll totals |
| Procedure | `sp_GetEmployeePayrollSummary()` | Returns template-aligned monthly employee rows |
| Procedure | `sp_GetPayrollSummaryByDepartment()` | Returns department management aggregates |
| Procedure | `sp_GetPayrollGrandTotals()` | Returns overall payroll totals |

Built as layered views on `vw_EmployeePayslipReport` so formulas stay in one place. The summary **aggregates both cutoffs** with `SUM(...)` / `GROUP BY EmployeeID`.

## Official template columns (detail view)

`vw_EmployeePayrollSummaryReport` exposes:

- `Employee No`
- `Employee Full Name`
- `Position`
- `Department`
- `Pay Period (Month)` (e.g. `June 2024`)
- `Gross Income` (sum of both cutoffs)
- `Social Security No.`
- `Social Security Contribution` (sum of both cutoffs)
- `Philhealth No.`
- `Philhealth Contribution`
- `Pag-ibig No.`
- `Pag-Ibig Contribution`
- `TIN`
- `Withholding Tax`
- `Net Pay` (sum of both cutoffs)

Run the **Aliases Column Header Checker** section in `employees_payroll_summary_report.sql` to verify all 15 headers are present.

## Key queries for submission

```sql
-- Monthly detail report (official template columns)
SELECT * FROM vw_EmployeePayrollSummaryReport ORDER BY `Employee No`;

-- Department management summary
SELECT * FROM vw_EmployeePayrollSummaryByDepartment ORDER BY `Department`;

-- Overall payroll totals
SELECT * FROM vw_EmployeePayrollOverallTotals;

-- Stored procedures (same view-backed outputs)
CALL sp_GetEmployeePayrollSummary();
CALL sp_GetPayrollSummaryByDepartment();
CALL sp_GetPayrollGrandTotals();
```

## SQL techniques demonstrated

- **VIEW** (monthly detail + department aggregate + overall totals)
- **STORED PROCEDURE** (`sp_GetEmployeePayrollSummary`, `sp_GetPayrollSummaryByDepartment`, `sp_GetPayrollGrandTotals`)
- **JOIN** (inherited via payslip view)
- **Aggregation** (`SUM`, `COUNT`, `GROUP BY` for monthly rollup and management views)
- **Subquery** (inherited via payslip view CTEs)

## Consistency

Script `14_m2_report_procedures.sql` includes checks that:

- Payslip rows ≈ **2 × employee count** (two cutoffs)
- Summary rows ≈ **1 × employee count** (monthly rollup)
- `SUM(payslip.\`Take Home Pay\`)` = `SUM(summary.\`Net Pay\`)`
- For employee 10013: per-cutoff Take Home Pay **13,317.40**; monthly Net Pay **26,634.80**

## Screenshots to capture

1. `SHOW CREATE VIEW vw_EmployeePayrollSummaryReport`
2. `SHOW CREATE VIEW vw_EmployeePayrollSummaryByDepartment`
3. `SHOW CREATE VIEW vw_EmployeePayrollOverallTotals`
4. Result grid for `CALL sp_GetEmployeePayrollSummary()`
5. Result grid for `CALL sp_GetPayrollSummaryByDepartment()`
6. Result grid for `CALL sp_GetPayrollGrandTotals()`

See also [`M2_PRESENTATION_STRUCTURE.md`](M2_PRESENTATION_STRUCTURE.md) for team presentation order.
