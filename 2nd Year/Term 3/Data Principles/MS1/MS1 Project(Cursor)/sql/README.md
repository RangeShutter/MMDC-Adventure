# payrollsystem_db — MySQL Scripts

MotorPH Payroll System database implementation for Data Principles MS1 homework (steps 1–8).

## Requirements

- **MySQL 8.0+** (CHECK constraints enforced)
- Database name: `payrollsystem_db`

## Quick start (MySQL Workbench)

1. Open MySQL Workbench and connect to your local server.
2. **File → Open SQL Script** and run scripts **in order**:
   - `01_create_database.sql`
   - `02_schema.sql`
   - `03_seed_lookup.sql`
   - `04_seed_employees.sql`
   - `05_seed_statutory.sql`
   - `06_validation_and_queries.sql`
3. For step 7 constraint tests, run blocks in `07_constraint_tests.sql` one at a time.

**Or** run the combined script:

```bash
mysql -u root -p < payrollsystem_db.sql
```

(Regenerate combined file after edits: `python build_combined.py`)

## Schema overview

| Category | Tables |
|----------|--------|
| Design doc (20) | `Employee`, `Department`, `EmploymentStatus`, `EmployeeAddress`, `GovernmentID`, `Salary`, `Benefit`, `Attendance`, `Overtime`, `Leave`, `LeaveType`, `ApprovalStatus`, `Payroll`, `Deduction`, `Payslip`, `UserAccount`, `Role`, `Permission`, `UserRole`, `RolePermission` |
| Statutory reference (4) | `SSSContributionBracket`, `PhilhealthContributionRate`, `PagibigContributionRate`, `WithholdingTaxBracket` |
| M2 report (1) | `WithholdingTaxBracketSemiMonthly` |

Transactional tables (`Attendance`, `Payroll`, etc.) are created empty; employee and rate data come from `document/` markdown files.

## Expected row counts (after seed)

| Table | Rows |
|-------|------|
| Employee | 34 |
| Department | 8 |
| EmploymentStatus | 4 |
| EmployeeAddress | 34 |
| GovernmentID | 34 |
| Salary | 34 |
| Benefit | 102 |
| SSSContributionBracket | 45 |
| PhilhealthContributionRate | 3 |
| PagibigContributionRate | 2 |
| WithholdingTaxBracket | 6 |
| Role | 5 |
| Permission | 7 |
| RolePermission | 11 |

## Regenerate employee seed

If `document/MotorPH_Employee Details.md` changes:

```bash
python generate_seed_data.py
```

## Files

| File | Purpose |
|------|---------|
| `01_create_database.sql` | Create `payrollsystem_db` |
| `02_schema.sql` | All tables, PKs, FKs, constraints |
| `03_seed_lookup.sql` | Lookups, departments, RBAC |
| `04_seed_employees.sql` | 34 employees + address, gov IDs, salary, benefits |
| `05_seed_statutory.sql` | SSS, PhilHealth, Pag-IBIG, withholding tax |
| `06_validation_and_queries.sql` | Verification SELECTs |
| `07_constraint_tests.sql` | Optional constraint failure tests |
| `10_live_demo.sql` | **Live presentation demo**: full payroll flow + automated constraint tests |
| `LIVE_DEMO_SCRIPT.md` | Speaker script for live Workbench demo |
| `08_performance_test.sql` | Performance test: profiling, EXPLAIN ANALYZE, benchmarks |
| `09_performance_stress_seed.sql` | Optional bulk data for before/after performance comparison |
| `PERFORMANCE_TEST_GUIDE.md` | How to run tests and report results |
| `payrollsystem_db.sql` | Combined deploy script |
| `build_m2_reports.py` | Regenerate `reports/13_m2_run_all_reports.sql` and `reports/15_m2_full_deploy.sql` |

## Milestone 2 — Database Reports (Views + Stored Procedures)

After MS1 scripts (01–05), run in order:

1. [`reports/11_schema_semi_monthly_tax.sql`](reports/11_schema_semi_monthly_tax.sql)
2. [`reports/12_seed_payslip_pay_period.sql`](reports/12_seed_payslip_pay_period.sql)
3. [`reports/employee_payslip_report.sql`](reports/employee_payslip_report.sql)
4. [`reports/employees_payroll_summary_report.sql`](reports/employees_payroll_summary_report.sql)
5. [`reports/14_m2_report_procedures.sql`](reports/14_m2_report_procedures.sql)

**One-shot Workbench deploy (after MS1 01–05):**

- [`reports/15_m2_full_deploy.sql`](reports/15_m2_full_deploy.sql) — steps 1–5 combined (11 + 12 + views + procedures)
- [`reports/13_m2_run_all_reports.sql`](reports/13_m2_run_all_reports.sql) — views + procedures only (after 11 and 12)

Regenerate combined M2 scripts after edits: `python build_m2_reports.py`

**Error 1146?** Summary and procedures depend on `vw_EmployeePayslipReport`. Run step 3 first, or use `13` / `15` above.

### Views (calculation layer)

| View | Script |
|------|--------|
| `vw_EmployeePayslipReport` | `employee_payslip_report.sql` |
| `vw_EmployeePayrollSummaryReport` | `employees_payroll_summary_report.sql` (official payroll summary template columns) |
| `vw_EmployeePayrollSummaryByDepartment` | `employees_payroll_summary_report.sql` (department management rollup) |
| `vw_EmployeePayrollOverallTotals` | `employees_payroll_summary_report.sql` (overall payroll totals) |

### Stored procedures (report access layer)

| Procedure | Purpose |
|-----------|---------|
| `sp_GetEmployeePayslip(EmployeeID)` | Single-employee payslip |
| `sp_GetEmployeePayrollSummary()` | All employees |
| `sp_GetPayrollSummaryByDepartment()` | Department management totals |
| `sp_GetPayrollGrandTotals()` | Overall payroll totals |

Views hold all calculations; procedures read from views so payslip detail and management totals stay consistent.

```sql
-- Payslip (single employee)
CALL sp_GetEmployeePayslip(10013);

-- Summary (all employees)
CALL sp_GetEmployeePayrollSummary();

-- Department totals
CALL sp_GetPayrollSummaryByDepartment();

-- Overall totals
CALL sp_GetPayrollGrandTotals();
```

See [`reports/PAYSLIP_REPORT_GUIDE.md`](reports/PAYSLIP_REPORT_GUIDE.md), [`reports/PAYROLL_SUMMARY_GUIDE.md`](reports/PAYROLL_SUMMARY_GUIDE.md), and [`reports/M2_PRESENTATION_STRUCTURE.md`](reports/M2_PRESENTATION_STRUCTURE.md).
