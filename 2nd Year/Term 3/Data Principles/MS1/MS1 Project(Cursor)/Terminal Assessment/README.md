# Terminal Assessment — Submission Package

**Course:** MO-IT111 Database Principles & Applications  
**Database:** `payrollsystem_db`

## What to upload

| File | Purpose |
|------|---------|
| [`payrollsystem_db_final.sql`](payrollsystem_db_final.sql) | **Finalized Payroll System Database SQL script** (schema, seed, M2 reports) |
| [`16_terminal_assessment_test_cases.sql`](16_terminal_assessment_test_cases.sql) | Executable homework test cases MMDC-DBTC01 / DBTC02 |
| [`TEST_CASE_RESULTS.md`](TEST_CASE_RESULTS.md) | PASS/FAIL documentation + screenshot placeholders |

## How to deploy and test (MySQL Workbench)

1. **File → Open SQL Script** → run `payrollsystem_db_final.sql` (full deploy).
2. Open `16_terminal_assessment_test_cases.sql`.
3. Run sections in order:
   - **MMDC-DBTC01-A** Create employees → screenshot result grid
   - **MMDC-DBTC01-B** Update salaries → screenshot before/after
   - **MMDC-DBTC01-C** Delete employees → screenshot count = 0
   - **MMDC-DBTC02-A** Duplicate EmployeeID → **red X / Error 1062 = PASS**
   - **MMDC-DBTC02-B** NULL mandatory fields → **red X / Error 1048 = PASS**
4. Paste screenshots into `TEST_CASE_RESULTS.md`.

## Regenerate the final SQL after report changes

```bash
python sql/build_terminal_assessment.py
```

## Quick ID map

| Homework | This DB |
|----------|---------|
| New employees | 10035, 10036, 10037 |
| Delete 29 / 30 / 31 | 10029 / 10030 / 10031 |
| Uniqueness “ID 40” | Duplicate insert of **10001** |
