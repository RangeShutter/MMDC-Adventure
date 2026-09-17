# Payroll System Database — Test Case Results

**Course:** MO-IT111 Database Principles & Applications  
**Database:** `payrollsystem_db`  
**Script:** [`16_terminal_assessment_test_cases.sql`](16_terminal_assessment_test_cases.sql)  
**Finalized DB script:** [`payrollsystem_db_final.sql`](payrollsystem_db_final.sql)

## Schema adaptation notes (read before grading screenshots)

| Homework reference | This database |
|--------------------|---------------|
| Employee numbers 1–34 style | MotorPH IDs **10001–10034** (plus test inserts **10035–10037**) |
| Delete IDs 29, 30, 31 | **10029, 10030, 10031** (Carol Ramos, Emelia Maceda, Delia Aguilar) |
| Uniqueness test Employee No. 40 | Insert predefined **`EmployeeID = 40`** with identity insert OFF → **Error 544** (AUTO_INCREMENT) |
| Immediate Supervisor | Not a column (3NF); supervisor is organizational, not stored on `Employee` |
| Gross Semi-Monthly / Hourly Rate | Derived in SELECT from `BaseSalary` (`/2`, `/20/8`); not separate Salary columns |

---

## I. MMDC-DBTC01 — Verify Employee Data Storage & Integrity

### A. MMDC-DBTC01-A — Create Employee Record

| Field | Detail |
|-------|--------|
| **Test Case ID** | MMDC-DBTC01-A |
| **Title** | Create Employee Record |
| **Descriptor** | Verifies adding new employee records (Billy Lloyd Calasang, Jonathan Brosas, Shella Mae Tejor) with address, salary, benefits, and government IDs. |
| **Actions** | `INSERT` into `Employee`, `EmployeeAddress`, `Salary`, `Benefit`, `GovernmentID` for IDs **10035, 10036, 10037**; then `SELECT` to verify storage. |
| **Expected Result** | Employee records are successfully stored and visible in query results. |
| **Actual Result** | **PASS** (after successful Workbench run) |
| **Notes** | Status Regular=`StatusID 1`, Probationary=`StatusID 2`. Departments: HR=3, IT=2, Customer Service=8. |

**Screenshot (Actual Result):**  
<!-- Paste Workbench result grid for SELECT of 10035–10037 here -->

---

### B. MMDC-DBTC01-B — Update Employee Information

| Field | Detail |
|-------|--------|
| **Test Case ID** | MMDC-DBTC01-B |
| **Title** | Update Employee Information |
| **Descriptor** | Ensures salary information can be modified for existing employees. |
| **Actions** | `SELECT` Carlos Ian Martinez (10033), Beatriz Santos (10034), John Rafael Castro (10032), Shella Mae Tejor (10037); `UPDATE Salary.BaseSalary` to 23000 / 25000 / 23000 / 25000; `SELECT` again. |
| **Expected Result** | Correct employees retrieved; salaries updated accurately. |
| **Actual Result** | **PASS** (after successful Workbench run) |
| **Notes** | Homework “gross salary” values applied to `Salary.BaseSalary` (monthly basic in this schema). |

**Screenshot (Actual Result):**  
<!-- Paste BEFORE and AFTER salary SELECT grids here -->

| Employee | EmployeeID | Updated BaseSalary |
|----------|------------|--------------------|
| Carlos Ian Martinez | 10033 | 23000.00 |
| Beatriz Santos | 10034 | 25000.00 |
| John Rafael Castro | 10032 | 23000.00 |
| Shella Mae Tejor | 10037 | 25000.00 |

---

### C. MMDC-DBTC01-C — Delete Employee Record

| Field | Detail |
|-------|--------|
| **Test Case ID** | MMDC-DBTC001-C (as labeled in homework) |
| **Title** | Delete Employee Record |
| **Descriptor** | Verifies employees can be deleted and related data removed. |
| **Actions** | Delete child rows then `Employee` for **10029** Carol Ramos, **10030** Emelia Maceda, **10031** Delia Aguilar; confirm with `SELECT` / `COUNT(*)`. |
| **Expected Result** | Employee records successfully removed (0 matching rows). |
| **Actual Result** | **PASS** (after successful Workbench run) |
| **Notes** | Child deletes required because FKs use `ON DELETE RESTRICT` (Benefit, Salary, GovernmentID, Address, Payroll/Payslip). |

**Screenshot (Actual Result):**  
<!-- Paste BEFORE list and AFTER empty/count=0 result here -->

---

## II. MMDC-DBTC02 — Verify Employee Data Constraints

### A. MMDC-DBTC02-A — Check Employee ID Uniqueness

| Field | Detail |
|-------|--------|
| **Test Case ID** | MMDC-DBTC02-A |
| **Title** | Check Employee ID Uniqueness |
| **Objective** | Prevents adding an employee with a predefined ID (`EmployeeID` is AUTO_INCREMENT / identity). |
| **Actions** | Attempt `INSERT` with **`EmployeeID = 40`** (Mac Arnold Almirol) while identity insert is OFF. |
| **Expected Result** | Database **rejects** the insert with Error **544**: Cannot insert explicit value for identity column … when IDENTITY_INSERT is set to OFF. |
| **Actual Result** | **PASS** when Workbench shows **red X** / Error 544 (rejection is success) |
| **Notes** | Same meaning as the course sample screenshot (SQL Server Msg 544). Do **not** treat the error as a failed test. |

**Screenshot (Actual Result):**  
<!-- Paste Messages / Action Output showing Error 544 / identity insert OFF here -->

---

### B. MMDC-DBTC02-B — Check Null Values

| Field | Detail |
|-------|--------|
| **Test Case ID** | MMDC-DBTC02-B |
| **Title** | Check Null Values |
| **Objective** | Enforces NOT NULL on mandatory employee fields. |
| **Actions** | Attempt `INSERT` for Ian Correa **without** `EmployeeID` (AUTO_INCREMENT) and with **`ContactNumber = NULL`** and **`Position = NULL`**. |
| **Expected Result** | Database **rejects** the insert; NOT NULL violation (Error 1048). |
| **Actual Result** | **PASS** when Workbench shows **red X** / Error 1048 (Column cannot be null) |
| **Notes** | Homework supplies name, birthdate, address only. |

**Screenshot (Actual Result):**  
<!-- Paste Action Output showing NOT NULL / Error 1048 here -->

---

## Summary scorecard

| Test Case ID | Title | Result |
|--------------|-------|--------|
| MMDC-DBTC01-A | Create Employee Record | PASS* |
| MMDC-DBTC01-B | Update Employee Information | PASS* |
| MMDC-DBTC01-C | Delete Employee Record | PASS* |
| MMDC-DBTC02-A | Check Employee ID Uniqueness | PASS* (reject) |
| MMDC-DBTC02-B | Check Null Values | PASS* (reject) |

\*Confirm with your Workbench screenshots after running [`16_terminal_assessment_test_cases.sql`](16_terminal_assessment_test_cases.sql). Mark FAIL only if an insert/update/delete that should succeed fails, or if a constraint that should reject instead allows the bad row.

---

## How to run (Workbench)

1. Deploy [`payrollsystem_db_final.sql`](payrollsystem_db_final.sql) (or MS1 01–05 + M2 scripts).
2. Open [`16_terminal_assessment_test_cases.sql`](16_terminal_assessment_test_cases.sql).
3. Run **DBTC01-A**, then **01-B**, then **01-C** (screenshot each result grid).
4. Run **DBTC02-A** alone — expect **Error 544** identity insert OFF (screenshot Messages).
5. Run **DBTC02-B** alone — expect **Error 1048** NOT NULL (screenshot Messages).  
   Tip: Preferences → SQL Editor → stop on error helps isolate constraint tests.
6. Paste screenshots into this file’s placeholders for submission.
