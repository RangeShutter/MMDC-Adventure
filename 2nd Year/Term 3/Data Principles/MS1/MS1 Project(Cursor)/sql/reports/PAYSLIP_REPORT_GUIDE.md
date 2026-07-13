# Employee Payslip Database Report — M2 Guide

## Overview

The **Employee Payslip Database Report** is implemented as a MySQL **VIEW** named `vw_EmployeePayslipReport`. It uses joins, subqueries, conditional aggregation, and calculated columns to produce a complete **semi-monthly payslip** for each employee.

## Run order (after MS1)

1. `01_create_database.sql` through `05_seed_statutory.sql` (if not already deployed)
2. [`11_schema_semi_monthly_tax.sql`](11_schema_semi_monthly_tax.sql)
3. [`12_seed_payslip_pay_period.sql`](12_seed_payslip_pay_period.sql)
4. [`employee_payslip_report.sql`](employee_payslip_report.sql)
5. [`employees_payroll_summary_report.sql`](employees_payroll_summary_report.sql)
6. [`14_m2_report_procedures.sql`](14_m2_report_procedures.sql)

## Business rules implemented

### Earnings (semi-monthly)

| Column | Formula |
|--------|---------|
| BasicPaySemi | `Salary.BaseSalary / 2` |
| RiceSubsidySemi | Rice benefit / 2 |
| PhoneAllowanceSemi | Phone benefit / 2 |
| ClothingAllowanceSemi | Clothing benefit / 2 |
| **GrossPay** | Sum of all semi-monthly earnings |

### Statutory deductions — Option A (Split)

Monthly contribution is computed first, then **divided by 2** for each semi-monthly cutoff:

| Deduction | Logic |
|-----------|--------|
| **SSS** | Lookup `SSSContributionBracket` on monthly `BaseSalary`, then `/ 2` |
| **PhilHealth** | `MIN(BaseSalary × 3%, 1800) × 50% employee share / 2` |
| **Pag-IBIG** | `MIN(BaseSalary × rate, 100) / 2` (1% if 1,000–1,500, else 2%) |

### Withholding tax (semi-monthly)

1. **TaxableIncome** = `GrossPay − SSS − PhilHealth − Pag-IBIG` (all semi-monthly amounts)
2. Lookup **WithholdingTaxBracketSemiMonthly** (converted from MotorPH monthly table)
3. **Tax** = `BaseTax + (TaxableIncome − ExcessOver) × ExcessRate` when applicable

Monthly brackets were converted by halving **Min, Max, BaseTax, ExcessOver**; **rates unchanged**.

### Net pay

**NetPay** = `GrossPay − TotalDeductions`

---

## Tables and columns used

| Payslip field | Table.column |
|---------------|--------------|
| Employee ID, name, position | `Employee` |
| Department | `Department.DepartmentName` |
| Address | `EmployeeAddress.StreetName` |
| Government IDs | `GovernmentID` |
| Pay period / issue date | `Payroll`, `Payslip` |
| Basic salary | `Salary.BaseSalary` |
| Allowances | `Benefit` (aggregated by type) |
| SSS bracket | `SSSContributionBracket` |
| Tax brackets | `WithholdingTaxBracketSemiMonthly` |

---

## Verification — Employee 10013 (Martha Farala)

Monthly basic: **₱24,000**

| Field | Expected |
|-------|----------|
| BasicPaySemi | 12,000.00 |
| RiceSubsidySemi | 750.00 |
| PhoneAllowanceSemi | 250.00 |
| ClothingAllowanceSemi | 250.00 |
| **GrossPay** | **13,250.00** |
| SSSDeduction | 540.00 (monthly 1,080 ÷ 2) |
| PhilHealthDeduction | 180.00 |
| PagibigDeduction | 50.00 |
| TaxableIncome | 12,480.00 |
| WithholdingTax | 412.60 (20% over 10,417) |
| **NetPay** | **12,067.40** |

Query:

```sql
SELECT * FROM vw_EmployeePayslipReport WHERE EmployeeID = 10013;
```

Equivalent stored procedure (same row — reads from the view):

```sql
CALL sp_GetEmployeePayslip(10013);
```

---

## Verification — MotorPH tax sample (semi-monthly equivalent)

Monthly sample (₱25,000 salary): statutory 1,600, taxable 23,400, tax **513.40**.

Semi-monthly (Option A split): statutory **800**, taxable **11,700**, tax **~256.60**.

Use employee near ₱25,000 basic or validate manually:

```sql
-- Taxable 11,700 in bracket 10,417-16,666 at 20% over 10,417:
-- (11700 - 10417) * 0.20 = 256.60
```

---

## Verification — Employee 10001 (CEO)

Monthly basic: **₱90,000** — validates high-bracket tax and SSS max (1,125 semi = 562.50).

```sql
SELECT * FROM vw_EmployeePayslipReport WHERE EmployeeID = 10001;
```

---

## Submission screenshots checklist

1. `CREATE VIEW vw_EmployeePayslipReport` (from script or `SHOW CREATE VIEW`)
2. `SELECT * FROM vw_EmployeePayslipReport WHERE EmployeeID = 10013`
3. `CALL sp_GetEmployeePayslip(10013)` — same NetPay as step 2
4. Result showing **GrossPay**, each deduction, **NetPay**
5. Optional: SCHEMAS → Views → `vw_EmployeePayslipReport`

---

## Single-employee filter (homework)

```sql
USE payrollsystem_db;
SELECT * FROM vw_EmployeePayslipReport WHERE EmployeeID = 10013;
-- or
CALL sp_GetEmployeePayslip(10013);
```

Replace `10013` with any valid `EmployeeID` (10001–10034).

---

## Files for Milestone 2 submission

| File | Purpose |
|------|---------|
| `11_schema_semi_monthly_tax.sql` | Semi-monthly tax table + seed |
| `12_seed_payslip_pay_period.sql` | Pay period metadata |
| `employee_payslip_report.sql` | **VIEW definition + verification queries** |
| `14_m2_report_procedures.sql` | **Stored procedures** (`sp_GetEmployeePayslip`, etc.) |

Export `employee_payslip_report.sql` as your **Employee Payslip Database Report SQL script**. Include `14_m2_report_procedures.sql` for M2 stored-procedure requirement.
