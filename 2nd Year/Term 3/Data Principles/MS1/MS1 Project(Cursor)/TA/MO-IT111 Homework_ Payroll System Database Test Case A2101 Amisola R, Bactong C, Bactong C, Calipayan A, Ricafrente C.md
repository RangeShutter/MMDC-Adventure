

# **Intellectual Property Notice**

This template is an exclusive property of **Mapua-Malayan Digital College** and is protected under **Republic Act No. 8293**, also known as the *Intellectual Property Code of the Philippines* (IP Code). It is provided solely for educational purposes within this course. Students may use this template to complete their tasks, but may not **modify, distribute, sell, upload,** or **claim ownership** of the template itself. Such actions constitute copyright infringement under **Sections 172, 177, and 216** of the IP Code and may result in legal consequences. Unauthorized use beyond this course may result in legal or academic consequences.

Additionally, students must comply with the **Mapua-Malayan Digital College Student Handbook**, particularly with the following provisions:

- **Offenses Related to MMDC IT**:  
  - **Section 6.2** – Unauthorized copying of files  
  - **Section 6.8** – Extraction of protected, copyrighted, and/or confidential information by electronic means using MMDC IT infrastructure
- **Offenses Related to MMDC Admin, IT, and Operations**:  
  - **Section 4.5** – Unauthorized collection or extraction of money, checks, or other instruments of monetary equivalent in connection with matters pertaining to MMDC

Violations of these policies may result in **disciplinary actions ranging from suspension to dismissal**, in accordance with the Student Handbook.

For permissions or inquiries, please contact MMDC-ISD at [isd@mmdc.mcl.edu.ph](mailto:isd@mmdc.mcl.edu.ph).


| MO-IT111: Database Principles & Applications |     |
| -------------------------------------------- | --- |
| **Payroll System Database Test Case**        |     |



| Project Manager:            | Charlize Bactong   |
| --------------------------- | ------------------ |
| **Project Manager:**        | Charlize Bactong   |
| **Database Designer:**      | Angelica Calipayan |
| **Program and Year Level:** | BSIT 2nd year      |


**Instructions:**

1. Access your MotorPH payroll system database from your DBMS tool and perform the following test cases provided below.
2. Include a screenshot of your test result in the ‘Actual Result’ row on each table.
3. **MMDC-DBTC01  Verify Employee Data Storage & Integrity**
  1. **Create a New Employee Record**


| Test Case ID        | MMDC-DBTC01-A                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Create Employee Record                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| **Descriptor**      | This test case verifies the basic functionality of adding a new employee record to the database.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| **Actions**         | Write a query that will insert the following employee records into the database: **Billy Lloyd Calasang Basic Information** Birthdate: January 22, 1996 Address: 2nd Floor, Gaisano Mactan Mall, Pajo, Lapu-lapu City, Cebu Phone Number: 361-299-029 **Job Information** Position: HR Team Leader Status: Regular Immediate Supervisor: Andrea Mae Villanueva **Salary Information** Basic Salary: 42,975 Gross Semi-Monthly Rate: 21,488 Hourly Rate: 255.80 TIN: 824-311-682-000 Clothing Allowance: 800 Rice Allowance: 1,500 Phone Allowance: 800 **Social Security Information** SSS: 37-3379841-1 Philhealth: 632361534812 Pag-Ibig: 374357402374 **Jonathan Brosas Basic Information:** Birthdate: November 26, 1994 Address: A Fernando 1400, Valenzuela, Valenzuela Phone Number: 032-340-2015 **Job Information** Position: IT Technical Support Status: Probationary Immediate Supervisor: Eduard Hernandez **Salary Information** Basic Salary: 42,975 Gross Semi-Monthly Rate: 21,487.5 Hourly Rate: 255.80 TIN: 632-531-054-000 Clothing Allowance: 800 Rice Allowance: 1,500 Phone Allowance: 800 **Social Security Information** SSS: 92-4800602-9 Philhealth: 735270773421 Pag-Ibig: 632722676967 **Shella Mae Tejor Basic Information** Birthdate: March 1, 1994 Address: Ayala Avenue 1200, Makati City, Metro Manila Phone Number: 894-385-011 **Job Information** Position: Customer Service and Relations Status: Probationary Immediate Supervisor: Reyes, Isabella **Salary Information** Basic Salary: 52,670 Gross Semi-Monthly Rate: 26,335 Hourly Rate: 313.51 TIN: 327-367-815-000 Clothing Allowance: 1,000 Rice Allowance: 1,500 Phone Allowance: 1,000 **Social Security Information** SSS: 32-5213838-6 Philhealth: 675893056701 Pag-Ibig: 133337008927 |
| **Expected Result** | Three new employees are stored with MotorPH IDs **10035**, **10036**, and **10037**. Validation query returns **3 rows** with correct personal, job, salary, and government ID data. Nine benefit rows are stored (3 per employee). See explicit expected output below. |
| **Actual Result**   | ***Insert the screenshot or the link to your test case result here.***                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |


**SQL Query (MMDC-DBTC01-A)**

```sql
USE payrollsystem_db;

-- 1) Billy Lloyd Calasang — HR Team Leader, Regular (StatusID 1), Human Resources (Dept 3)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10035, 'Billy Lloyd', 'Calasang', '1996-01-22',
    '2nd Floor, Gaisano Mactan Mall, Pajo, Lapu-lapu City, Cebu',
    '361-299-029', 'HR Team Leader', 3, 1
);

INSERT INTO EmployeeAddress (EmployeeID, StreetName)
VALUES (10035, '2nd Floor, Gaisano Mactan Mall, Pajo, Lapu-lapu City, Cebu');

INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo)
VALUES (10035, 42975.00, 'Monthly', '2024-01-01', NULL);

INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES
(10035, 'Clothing Allowance', 800.00),
(10035, 'Rice Subsidy', 1500.00),
(10035, 'Phone Allowance', 800.00);

INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber)
VALUES (10035, '37-3379841-1', '632361534812', '824-311-682-000', '374357402374');

-- 2) Jonathan Brosas — IT Technical Support, Probationary (StatusID 2), IT (Dept 2)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10036, 'Jonathan', 'Brosas', '1994-11-26',
    'A Fernando 1400, Valenzuela, Valenzuela',
    '032-340-2015', 'IT Technical Support', 2, 2
);

INSERT INTO EmployeeAddress (EmployeeID, StreetName)
VALUES (10036, 'A Fernando 1400, Valenzuela, Valenzuela');

INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo)
VALUES (10036, 42975.00, 'Monthly', '2024-01-01', NULL);

INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES
(10036, 'Clothing Allowance', 800.00),
(10036, 'Rice Subsidy', 1500.00),
(10036, 'Phone Allowance', 800.00);

INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber)
VALUES (10036, '92-4800602-9', '735270773421', '632-531-054-000', '632722676967');

-- 3) Shella Mae Tejor — Customer Service and Relations, Probationary, Customer Service (Dept 8)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10037, 'Shella Mae', 'Tejor', '1994-03-01',
    'Ayala Avenue 1200, Makati City, Metro Manila',
    '894-385-011', 'Customer Service and Relations', 8, 2
);

INSERT INTO EmployeeAddress (EmployeeID, StreetName)
VALUES (10037, 'Ayala Avenue 1200, Makati City, Metro Manila');

INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo)
VALUES (10037, 52670.00, 'Monthly', '2024-01-01', NULL);

INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES
(10037, 'Clothing Allowance', 1000.00),
(10037, 'Rice Subsidy', 1500.00),
(10037, 'Phone Allowance', 1000.00);

INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber)
VALUES (10037, '32-5213838-6', '675893056701', '327-367-815-000', '133337008927');

-- Validation: employee details (screenshot this result grid)
SELECT
    e.EmployeeID,
    CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
    e.DateOfBirth,
    ea.StreetName AS Address,
    e.ContactNumber,
    e.Position,
    d.DepartmentName,
    es.StatusName,
    s.BaseSalary,
    ROUND(s.BaseSalary / 2, 2) AS GrossSemiMonthlyRate,
    ROUND(s.BaseSalary / 20 / 8, 2) AS HourlyRateApprox,
    g.TINNumber,
    g.SSSNumber,
    g.PhilHealthNumber,
    g.PagIBIGNumber
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN EmploymentStatus es ON e.StatusID = es.StatusID
INNER JOIN EmployeeAddress ea ON e.EmployeeID = ea.EmployeeID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
WHERE e.EmployeeID IN (10035, 10036, 10037)
ORDER BY e.EmployeeID;

-- Validation: benefits (expect 9 rows)
SELECT e.EmployeeID, CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
       b.BenefitType, b.Amount
FROM Employee e
INNER JOIN Benefit b ON e.EmployeeID = b.EmployeeID
WHERE e.EmployeeID IN (10035, 10036, 10037)
ORDER BY e.EmployeeID, b.BenefitType;
```

**Explicit Expected Result (MMDC-DBTC01-A)**

| EmployeeID | EmployeeName | Status | Department | BaseSalary | GrossSemiMonthlyRate | HourlyRateApprox |
|------------|--------------|--------|------------|------------|----------------------|------------------|
| 10035 | Billy Lloyd Calasang | Regular | Human Resources | 42975.00 | 21487.50 | 268.59 |
| 10036 | Jonathan Brosas | Probationary | Information Technology | 42975.00 | 21487.50 | 268.59 |
| 10037 | Shella Mae Tejor | Probationary | Customer Service | 52670.00 | 26335.00 | 329.19 |

- **Benefit rows:** 9 total (Clothing/Rice/Phone allowances per employee as specified in Actions).
- **Government IDs:** TIN, SSS, PhilHealth, and Pag-IBIG numbers match the homework values for each employee.
- **Pass criteria:** All `INSERT` statements succeed; validation queries return 3 employee rows and 9 benefit rows.


1. **Update Existing Employee Information**


| Test Case ID        | MMDC-DBTC01-B                                                                                                                                                                                                                                                                                                                       |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Update Employee Information                                                                                                                                                                                                                                                                                                         |
| **Descriptor**      | This test case ensures that the database allows modifications to existing employee records.                                                                                                                                                                                                                                         |
| **Actions**         | Write a query that will display the following employee records: Carlos Ian Martinez Beatriz Santos John Rafael Castro Shella Mae Tejor Update the fields related to salary information based on the gross salary provided below: Carlos Ian Martinez 23,000 Beatriz Santos 25,000 John Rafael Castro 23,000 Shella Mae Tejor 25,000 |
| **Expected Result** | Four employees are retrieved; `Salary.BaseSalary` is updated to **23000.00** for Martinez and Castro and **25000.00** for Santos and Tejor. Post-update validation shows the new values. See explicit expected output below. |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                                                                                                                                                                                                                            |


**SQL Query (MMDC-DBTC01-B)**

```sql
USE payrollsystem_db;

-- BEFORE update (screenshot this result grid)
SELECT
    e.EmployeeID,
    CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
    e.Position,
    s.BaseSalary AS CurrentBaseSalary
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID IN (10033, 10034, 10032, 10037)
ORDER BY e.EmployeeID;

UPDATE Salary SET BaseSalary = 23000.00 WHERE EmployeeID = 10033; -- Carlos Ian Martinez
UPDATE Salary SET BaseSalary = 25000.00 WHERE EmployeeID = 10034; -- Beatriz Santos
UPDATE Salary SET BaseSalary = 23000.00 WHERE EmployeeID = 10032; -- John Rafael Castro
UPDATE Salary SET BaseSalary = 25000.00 WHERE EmployeeID = 10037; -- Shella Mae Tejor

-- AFTER update (screenshot this result grid)
SELECT
    e.EmployeeID,
    CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeName,
    e.Position,
    s.BaseSalary AS UpdatedBaseSalary
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID IN (10033, 10034, 10032, 10037)
ORDER BY e.EmployeeID;
```

**Explicit Expected Result (MMDC-DBTC01-B)**

| EmployeeID | EmployeeName | UpdatedBaseSalary |
|------------|--------------|-------------------|
| 10032 | John Rafael Castro | 23000.00 |
| 10033 | Carlos Ian Martinez | 23000.00 |
| 10034 | Beatriz Santos | 25000.00 |
| 10037 | Shella Mae Tejor | 25000.00 |

- **Pass criteria:** BEFORE query returns 4 employees; each `UPDATE` affects 1 row; AFTER query shows the four updated salaries above.


1. **Delete Employee Record**


| Test Case ID        | MMDC-DBTC001-C                                                                                                                    |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Delete Employee Record                                                                                                            |
| **Objective**       | This test case verifies that the database can properly delete an employee record and that the corresponding data is removed.      |
| **Actions**         | Write a query that will delete the following employee records from the database: 29 Carol Ramos 30 Emelia Maceda 31 Delia Aguilar |
| **Expected Result** | Employees **10029** (Carol Ramos), **10030** (Emelia Maceda), and **10031** (Delia Aguilar) are removed. Final `SELECT` returns **0 rows**; `COUNT(*)` = **0**. See explicit expected output below. |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                          |


**SQL Query (MMDC-DBTC01-C)**

```sql
USE payrollsystem_db;

-- BEFORE delete (screenshot — expect 3 rows)
SELECT EmployeeID, FirstName, LastName, Position
FROM Employee
WHERE EmployeeID IN (10029, 10030, 10031);

-- Delete child rows first (FK ON DELETE RESTRICT), then parent Employee rows
DELETE FROM Benefit WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Salary WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM GovernmentID WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM EmployeeAddress WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Attendance WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Overtime WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM `Leave` WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Employee WHERE EmployeeID IN (10029, 10030, 10031);

-- AFTER delete (screenshot — expect 0 rows)
SELECT EmployeeID, FirstName, LastName
FROM Employee
WHERE EmployeeID IN (10029, 10030, 10031);

SELECT COUNT(*) AS RemainingDeletedEmployees
FROM Employee
WHERE EmployeeID IN (10029, 10030, 10031);
```

**Explicit Expected Result (MMDC-DBTC01-C)**

| Phase | Result |
|-------|--------|
| BEFORE | 3 rows: 10029 Carol Ramos, 10030 Emelia Maceda, 10031 Delia Aguilar |
| AFTER `SELECT` | **0 rows** (empty result grid) |
| AFTER `COUNT(*)` | **RemainingDeletedEmployees = 0** |

- **Pass criteria:** All delete statements succeed; no employee rows remain for IDs 10029–10031.


1. **MMDC-DBTC02  Verify Employee Data Constraints**
  1. **Check Employee ID Uniqueness**


| Test Case ID        | MMDC-DBTC02-A                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Check Employee ID Uniqueness                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| **Objective**       | This test case verifies that the database enforces the uniqueness constraint for Employee IDs, preventing the addition of employees with a predefined ID.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| **Actions**         | Attempt to add a new employee record: **Basic Information** Employee Number: 40 Employee Name: Mac Arnold Almirol Birthdate: October 8, 1996 Address: Unit 2802 One San Miguel Bldg, Shaw Blvd Cor San Miguel Ave, Ortigas Ctr 1605, Pasig City Phone Number: 477- 771-607 **Job Information** Position: IT Technical Support Status: Probationary Immediate Supervisor: Eduard Hernandez **Salary Information** Basic Salary: 42,975 Gross Semi-Monthly Rate: 21,487.5 Hourly Rate: 255.80 TIN: 936-540-856-000 Clothing Allowance: 800 Rice Allowance: 1,500 Phone Allowance: 800 **Social Security Information** SSS: 36-4160536-4 Philhealth: 862055202862 Pag-Ibig: 521301652682 |
| **Expected Result** | The database **rejects** the insert with **MySQL Error 1062** — duplicate entry for primary key on `EmployeeID`. A red **X** in Workbench Action Output means **PASS** (constraint enforced). |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |


**SQL Query (MMDC-DBTC02-A)**

> **Schema note:** Homework Employee Number **40** is not in the MotorPH seed data. To prove the uniqueness constraint, this test uses **existing** `EmployeeID = 10001` with the Mac Arnold Almirol details from the homework.

```sql
USE payrollsystem_db;

-- This INSERT must FAIL (Error 1062 — duplicate primary key)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10001, 'Mac Arnold', 'Almirol', '1996-10-08',
    'Unit 2802 One San Miguel Bldg, Shaw Blvd Cor San Miguel Ave, Ortigas Ctr 1605, Pasig City',
    '477-771-607', 'IT Technical Support', 2, 2
);
```

**Explicit Expected Result (MMDC-DBTC02-A)**

| Outcome | Detail |
|---------|--------|
| Insert result | **Rejected** — no new row created |
| Error code | **1062** |
| Error message (typical) | `Duplicate entry '10001' for key 'employee.PRIMARY'` |
| Pass criteria | Database refuses duplicate `EmployeeID`; Workbench shows red X / error in Action Output |


1. **Check Null Values**


| Test Case ID        | MMDC-DBTC02-B                                                                                                                                                                                                                      |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Check Null Values                                                                                                                                                                                                                  |
| **Objective**       | This test case verifies that the database enforces the NOT NULL constraint for mandatory fields, ensuring that essential information is always provided.                                                                           |
| **Actions**         | Attempt to add a new employee record with the following information: Employee Name: Ian Correa Birthdate: December 16, 1996 Address: 8435 West Service RoadMarcelo Green Village South Superhighway, Paranaque City                |
| **Expected Result** | The database **rejects** the insert with **MySQL Error 1048** — column cannot be null (on `ContactNumber` and/or `Position`). A red **X** in Workbench Action Output means **PASS** (NOT NULL enforced). |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                                                                                                                           |


**SQL Query (MMDC-DBTC02-B)**

```sql
USE payrollsystem_db;

-- This INSERT must FAIL (NULL ContactNumber and Position)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    10099, 'Ian', 'Correa', '1996-12-16',
    '8435 West Service Road Marcelo Green Village South Superhighway, Paranaque City',
    NULL,
    NULL,
    2,
    2
);
```

**Explicit Expected Result (MMDC-DBTC02-B)**

| Outcome | Detail |
|---------|--------|
| Insert result | **Rejected** — no new row created |
| Error code | **1048** |
| Error message (typical) | `Column 'ContactNumber' cannot be null` (or `Column 'Position' cannot be null`) |
| Pass criteria | Database refuses NULL in mandatory fields; Workbench shows red X / error in Action Output |


---

## Schema Adaptation & Execution Notes

| Homework reference | MotorPH normalized schema |
|--------------------|---------------------------|
| Employee numbers 1–34 | IDs **10001–10034**; new test employees **10035–10037** |
| Delete IDs 29, 30, 31 | **10029**, **10030**, **10031** (Carol Ramos, Emelia Maceda, Delia Aguilar) |
| Uniqueness test Employee No. 40 | Duplicate insert on **existing** `EmployeeID = 10001` |
| Immediate Supervisor | **Not stored** on `Employee` (3NF); organizational role only |
| Gross Semi-Monthly / Hourly Rate | **Derived** in `SELECT` from `Salary.BaseSalary` (`/ 2` and `/ 20 / 8`); not separate columns |

### How to run in MySQL Workbench

1. Deploy the database first: run `Terminal Assessment/payrollsystem_db_final.sql` (or MS1 scripts 01–05).
2. Run success-path tests **in order**: DBTC01-A → DBTC01-B → DBTC01-C. Screenshot each validation result grid.
3. Run **DBTC02-A** and **DBTC02-B** as **separate** executions — each invalid `INSERT` must produce an error. Screenshot the Action Output (red X = pass).
4. Tip: *Edit → Preferences → SQL Editor → Stop SQL execution on error* helps isolate constraint tests.
5. Full runnable script (with cleanup): `Terminal Assessment/16_terminal_assessment_test_cases.sql`.
