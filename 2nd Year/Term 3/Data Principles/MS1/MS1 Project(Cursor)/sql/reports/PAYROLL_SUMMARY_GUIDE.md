# Employees Payroll Summary Database Report — Guide

## Purpose

Implements an official-template payroll summary layer for management reporting while reusing `vw_EmployeePayslipReport` as the computation source.

- **Detail summary:** official payroll summary template columns for each employee
- **Management summary:** department totals and overall payroll totals

## Run order

1. Complete payslip setup first ([`employee_payslip_report.sql`](employee_payslip_report.sql))
2. Run [`employees_payroll_summary_report.sql`](employees_payroll_summary_report.sql)
3. Run [`14_m2_report_procedures.sql`](14_m2_report_procedures.sql)

## Views and procedures

| Layer | Object | Role |
|-------|--------|------|
| View | `vw_EmployeePayrollSummaryReport` | Employee-level detail using official template column names |
| View | `vw_EmployeePayrollSummaryByDepartment` | Department-level totals (`Total Employees`, `Total Gross Pay`, `Total Net Pay`) |
| View | `vw_EmployeePayrollOverallTotals` | Overall payroll totals |
| Procedure | `sp_GetEmployeePayrollSummary()` | Returns template-aligned employee detail rows |
| Procedure | `sp_GetPayrollSummaryByDepartment()` | Returns department management aggregates |
| Procedure | `sp_GetPayrollGrandTotals()` | Returns overall payroll totals |

Built as layered views on `vw_EmployeePayslipReport` so formulas stay in one place and downstream reports only reshape or aggregate.

## Official template columns (detail view)

`vw_EmployeePayrollSummaryReport` now exposes:

- `Employee No`
- `Employee Full Name`
- `Position`
- `Department`
- `Gross Income`
- `Social Security No.`
- `Social Security Contribution`
- `Philhealth No.`
- `Philhealth Contribution`
- `Pag-ibig No.`
- `Pag-Ibig Contribution`
- `TIN`
- `Withholding Tax`
- `Net Pay`

## Key queries for submission

```sql
-- Detail report (official template columns)
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

- **VIEW** (detail + department aggregate + overall totals)
- **STORED PROCEDURE** (`sp_GetEmployeePayrollSummary`, `sp_GetPayrollSummaryByDepartment`, `sp_GetPayrollGrandTotals`)
- **JOIN** (inherited via payslip view)
- **Aggregation** (`SUM`, `COUNT`, `GROUP BY` in management views)
- **Subquery** (inherited via payslip view CTEs)

## Consistency

Script `14_m2_report_procedures.sql` includes checks that payslip and summary views return the same row counts and total NetPay. Procedures never recalculate — they `SELECT` from views only.

## Screenshots to capture

1. `SHOW CREATE VIEW vw_EmployeePayrollSummaryReport`
2. `SHOW CREATE VIEW vw_EmployeePayrollSummaryByDepartment`
3. `SHOW CREATE VIEW vw_EmployeePayrollOverallTotals`
4. Result grid for `CALL sp_GetEmployeePayrollSummary()`
5. Result grid for `CALL sp_GetPayrollSummaryByDepartment()`
6. Result grid for `CALL sp_GetPayrollGrandTotals()`

See also [`M2_PRESENTATION_STRUCTURE.md`](M2_PRESENTATION_STRUCTURE.md) for team presentation order.
