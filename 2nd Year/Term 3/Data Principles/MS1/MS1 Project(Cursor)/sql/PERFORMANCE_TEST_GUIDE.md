# Performance Test Guide — payrollsystem_db

## Purpose

Measure how fast common payroll queries run on your MotorPH database, and verify that joins and lookups use indexes efficiently.

## Prerequisites

1. Deploy database: run `payrollsystem_db.sql` (or scripts 01–05).
2. MySQL **8.0+** (for `EXPLAIN ANALYZE`).

## Steps

### A. Baseline test (current data ~34 employees)

1. Open **MySQL Workbench**.
2. **File → Open SQL Script** → `08_performance_test.sql`
3. Click **Execute** (lightning bolt).
4. In **Result Grid**, find:
   - **Section 1** — row counts and table sizes (KB)
   - **SHOW PROFILES** — `Duration` column for Q1–Q7 (in seconds)
   - **EXPLAIN ANALYZE** — actual execution time per query plan
   - **BENCHMARK** — micro-timing for repeated operations

### B. Optional stress test (higher volume)

1. Run `09_performance_stress_seed.sql` (adds ~6,000+ attendance rows).
2. Run `08_performance_test.sql` again.
3. Compare `Duration` in **SHOW PROFILES** before vs after.

### C. What to report in presentation

| Metric | Where to find it | Good sign |
|--------|------------------|-----------|
| Query duration | `SHOW PROFILES` → Duration | Small values (e.g. &lt; 0.05s on laptop) |
| Index usage | `EXPLAIN ANALYZE` | Uses index / eq_ref on joins |
| Table size | Section 1 information_schema | Grows predictably with stress seed |
| Scalability | Before vs after stress seed | Duration increases but stays acceptable |

## Test queries covered

| ID | Query type | Simulates |
|----|------------|-----------|
| Q1 | COUNT | Dashboard employee total |
| Q2 | 3-table JOIN | HR employee list |
| Q3 | 4-table JOIN + GROUP BY | Payroll prep / full profile |
| Q4 | Statutory lookup | SSS bracket by salary |
| Q5 | JOIN + GROUP BY | Department headcount report |
| Q6 | Aggregation | Benefit summary |
| Q7 | Indexed lookup | Duplicate payroll check |

## Sample conclusion (for report)

> On the baseline dataset (34 employees), all seven profiled queries completed in under [X] seconds, confirming that normalized joins and foreign-key indexes support efficient retrieval for MotorPH’s current scale. After optional stress seeding (~6,000 attendance records), query duration increased to [Y] seconds, which remains acceptable for batch payroll processing. EXPLAIN ANALYZE showed index usage on primary and foreign keys, supporting the design’s scalability goals stated in the database design document.

## Cleanup stress data

Uncomment the `DELETE` statements at the bottom of `09_performance_stress_seed.sql` if you need to reset.
