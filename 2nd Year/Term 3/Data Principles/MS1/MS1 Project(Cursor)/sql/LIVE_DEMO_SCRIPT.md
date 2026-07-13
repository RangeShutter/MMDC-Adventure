# Live Demo Script — MotorPH Payroll Database

**File to run:** `10_live_demo.sql`  
**Time:** ~5–8 minutes  
**Prerequisite:** `payrollsystem_db` deployed with seed data

---

## Before you start (30 seconds)

1. Open **MySQL Workbench** → connect to local MySQL.
2. **File → Open SQL Script** → `sql/10_live_demo.sql`
3. Confirm **payrollsystem_db** exists in SCHEMAS.

**Say:**  
> "We will demonstrate how the MotorPH payroll database works end-to-end, then prove that constraints reject invalid data."

---

## Part 1 — Database loaded (30 sec)

**Execute** from top through Part 1 (or whole script).

**Point at results:**
- 24 tables
- 34 employees, 45 SSS brackets

**Say:**  
> "Master data and statutory rules are already loaded from MotorPH datasets."

---

## Part 2 — Step 1: Master data (1 min)

**Show:** Employee 10001 (CEO) with department, salary, government IDs, benefits.

**Say:**  
> "Employee is the center table. Salary, GovernmentID, and Benefit are separate normalized tables linked by foreign keys."

---

## Part 3 — Steps 2–5: Payroll flow (2–3 min)

| Step | What appears on screen | What to say |
|------|------------------------|-------------|
| **2** | Attendance + Overtime rows | "We record time-in, time-out, and approved overtime." |
| **3** | SSS bracket for ₱90,000 salary | "The app looks up statutory tables—SSS, PhilHealth, Pag-IBIG, tax—not hard-coded rates." |
| **4** | Payroll + 4 Deduction rows | "Computed results are stored: gross pay, net pay, and line-item deductions." |
| **5** | Payslip linked to payroll | "One payslip per payroll—full audit trail." |
| **Trace** | End-to-end join | "From employee to attendance to payroll to payslip in one query." |

---

## Part 4 — Valid data accepted (30 sec)

**Show:** Leave row inserted for employee 10007 (valid dates).

**Say:**  
> "Valid data passes—EndDate is after StartDate, so the CHECK constraint allows the insert."

---

## Part 5 — Constraints working (2 min)

**Show:** Table with 7 tests — **Result = PASS** for each.

**Say:**  
> "PASS here means the database **blocked** bad data, which is what we want."

| TestNo | What it proves |
|--------|----------------|
| 1 | NOT NULL — cannot insert employee without first name |
| 2 | CHECK — TimeOut must be ≥ TimeIn |
| 3 | CHECK — salary cannot be negative |
| 4 | FK — cannot add salary for non-existent employee |
| 5 | UNIQUE — duplicate payroll for same period rejected |
| 6 | CHECK — leave end date cannot be before start date |
| 7 | UNIQUE — duplicate SSS number rejected |

**Final line:**  
> "ALL CONSTRAINT TESTS PASSED — our database enforces integrity automatically."

---

## If something goes wrong

| Problem | Fix |
|---------|-----|
| Unknown database | Run `01_create_database.sql` first |
| Empty Employee table | Run seed scripts 03–05 |
| Part 5 shows FAIL | Check MySQL 8.0+; re-run `02_schema.sql` |
| Script stops on error | Use full `10_live_demo.sql` (Part 5 uses procedure so it won't stop) |

---

## Optional: Run section by section

Highlight from one `=== PART ===` line to the next, click **Execute Current Statement** or selection only—good for pacing during panel Q&A.

---

## One closing sentence

> "This demo shows the full payroll data flow and proves that primary keys, foreign keys, UNIQUE, and CHECK constraints protect MotorPH payroll data without relying only on the application."
