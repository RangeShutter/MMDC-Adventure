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
3. Validation queries use the same employee identity headers as `vw_EmployeePayrollSummaryReport` in `sql/reports/employees_payroll_summary_report.sql` (`Employee No`, `Employee Full Name`, `Position`, `Department`, `Social Security No.`, `Philhealth No.`, `Pag-ibig No.`, `TIN`), plus salary fields needed for create/update tests.
4. **MMDC-DBTC01  Verify Employee Data Storage & Integrity**
  1. **Create a New Employee Record**


| Test Case ID        | MMDC-DBTC01-A                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Create Employee Record                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| **Descriptor**      | This test case verifies the basic functionality of adding a new employee record to the database.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| **Actions**         | Write a query that will insert the following employee records into the database: **Billy Lloyd Calasang Basic Information** Birthdate: January 22, 1996 Address: 2nd Floor, Gaisano Mactan Mall, Pajo, Lapu-lapu City, Cebu Phone Number: 361-299-029 **Job Information** Position: HR Team Leader Status: Regular Immediate Supervisor: Andrea Mae Villanueva **Salary Information** Basic Salary: 42,975 Gross Semi-Monthly Rate: 21,488 Hourly Rate: 255.80 TIN: 824-311-682-000 Clothing Allowance: 800 Rice Allowance: 1,500 Phone Allowance: 800 **Social Security Information** SSS: 37-3379841-1 Philhealth: 632361534812 Pag-Ibig: 374357402374 **Jonathan Brosas Basic Information:** Birthdate: November 26, 1994 Address: A Fernando 1400, Valenzuela, Valenzuela Phone Number: 032-340-2015 **Job Information** Position: IT Technical Support Status: Probationary Immediate Supervisor: Eduard Hernandez **Salary Information** Basic Salary: 42,975 Gross Semi-Monthly Rate: 21,487.5 Hourly Rate: 255.80 TIN: 632-531-054-000 Clothing Allowance: 800 Rice Allowance: 1,500 Phone Allowance: 800 **Social Security Information** SSS: 92-4800602-9 Philhealth: 735270773421 Pag-Ibig: 632722676967 **Shella Mae Tejor Basic Information** Birthdate: March 1, 1994 Address: Ayala Avenue 1200, Makati City, Metro Manila Phone Number: 894-385-011 **Job Information** Position: Customer Service and Relations Status: Probationary Immediate Supervisor: Reyes, Isabella **Salary Information** Basic Salary: 52,670 Gross Semi-Monthly Rate: 26,335 Hourly Rate: 313.51 TIN: 327-367-815-000 Clothing Allowance: 1,000 Rice Allowance: 1,500 Phone Allowance: 1,000 **Social Security Information** SSS: 32-5213838-6 Philhealth: 675893056701 Pag-Ibig: 133337008927 |
| **Expected Result** | BEFORE create: **34** existing employees (`10001`–`10034`). AFTER create: full result grid shows **37** rows — existing employees remain as `EXISTING` and the three new hires appear as `NEW HIRE ADDED` (`10035`–`10037`). Nine benefit rows are stored. See expected output below.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **Actual Result**   | ***Insert the screenshot of the AFTER CREATE full roster result grid here (shows existing + NEW HIRE ADDED).***                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |




**SQL Query (MMDC-DBTC01-A)**

```sql
USE payrollsystem_db;

-- ---------------------------------------------------------------------------
-- 0) BEFORE: existing employees only (expect 34)
-- ---------------------------------------------------------------------------
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    es.StatusName AS `Status`,
    s.BaseSalary AS `Basic Salary`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`,
    'EXISTING' AS `Roster Status`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN EmploymentStatus es ON e.StatusID = es.StatusID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
ORDER BY e.EmployeeID;

SELECT COUNT(*) AS `Total Employees Before Create` FROM Employee; -- expect 34

-- ---------------------------------------------------------------------------
-- 1) Insert new employees into normalized tables
--    Explicit MotorPH IDs require IDENTITY_INSERT-style flag (EmployeeID is AUTO_INCREMENT)
-- ---------------------------------------------------------------------------
SET @ALLOW_EXPLICIT_EMPLOYEE_ID = 1;

-- Billy Lloyd Calasang — HR Team Leader, Regular (StatusID 1), Human Resources (3)
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

-- Jonathan Brosas — IT Technical Support, Probationary (StatusID 2), IT (2)
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

-- Shella Mae Tejor — Customer Service and Relations, Probationary, Customer Service (8)
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

SET @ALLOW_EXPLICIT_EMPLOYEE_ID = NULL;

-- ---------------------------------------------------------------------------
-- 2) AFTER: FULL roster result grid (existing + newly added)
--    Screenshot THIS grid — expect 37 rows; 10035–10037 = NEW HIRE ADDED
-- ---------------------------------------------------------------------------
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    es.StatusName AS `Status`,
    s.BaseSalary AS `Basic Salary`,
    ROUND(s.BaseSalary / 2, 2) AS `Gross Semi-Monthly Rate`,
    ROUND(s.BaseSalary / 20 / 8, 2) AS `Hourly Rate`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`,
    CASE
        WHEN e.EmployeeID IN (10035, 10036, 10037) THEN 'NEW HIRE ADDED'
        ELSE 'EXISTING'
    END AS `Roster Status`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN EmploymentStatus es ON e.StatusID = es.StatusID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
ORDER BY e.EmployeeID;

SELECT COUNT(*) AS `Total Employees After Create` FROM Employee; -- expect 37

SELECT
    SUM(CASE WHEN EmployeeID < 10035 THEN 1 ELSE 0 END) AS `Existing Employees`,
    SUM(CASE WHEN EmployeeID IN (10035, 10036, 10037) THEN 1 ELSE 0 END) AS `New Employees Added`
FROM Employee;
-- expect Existing = 34, New Employees Added = 3

-- New hires detail (proof they were added)
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.DateOfBirth,
    ea.StreetName AS Address,
    e.ContactNumber,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    es.StatusName AS `Status`,
    s.BaseSalary AS `Basic Salary`,
    ROUND(s.BaseSalary / 2, 2) AS `Gross Semi-Monthly Rate`,
    ROUND(s.BaseSalary / 20 / 8, 2) AS `Hourly Rate`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`,
    'NEW HIRE ADDED' AS `Roster Status`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN EmploymentStatus es ON e.StatusID = es.StatusID
INNER JOIN EmployeeAddress ea ON e.EmployeeID = ea.EmployeeID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
WHERE e.EmployeeID IN (10035, 10036, 10037)
ORDER BY e.EmployeeID;

-- Benefits for new employees (expect 9 rows)
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    b.BenefitType,
    b.Amount
FROM Employee e
INNER JOIN Benefit b ON e.EmployeeID = b.EmployeeID
WHERE e.EmployeeID IN (10035, 10036, 10037)
ORDER BY e.EmployeeID, b.BenefitType;
```

**Explicit Expected Result (MMDC-DBTC01-A) — Result Grid**


| Check         | Expected                                                     |
| ------------- | ------------------------------------------------------------ |
| BEFORE create | **34** rows (`10001`–`10034`), `Roster Status` = EXISTING    |
| AFTER create  | **37** rows (`10001`–`10037`)                                |
| Count proof   | `Existing Employees` = **34**, `New Employees Added` = **3** |
| Benefit rows  | **9** (Clothing / Rice / Phone for each new employee)        |


**Sample AFTER CREATE result grid** (existing employees remain; new hires appear at the end):


| Employee No | Employee Full Name                  | Position                       | Department             | Status       | Basic Salary | Roster Status      |
| ----------- | ----------------------------------- | ------------------------------ | ---------------------- | ------------ | ------------ | ------------------ |
| 10001       | Manuel III Garcia                   | Chief Executive Officer        | …                      | Regular      | 90000.00     | EXISTING           |
| 10002       | Antonio Lim                         | Chief Operating Officer        | …                      | Regular      | 60000.00     | EXISTING           |
| …           | … *(10003–10033 remain EXISTING)* … | …                              | …                      | …            | …            | EXISTING           |
| 10034       | Beatriz Santos                      | Customer Service and Relations | …                      | Regular      | 52670.00     | EXISTING           |
| **10035**   | **Billy Lloyd Calasang**            | HR Team Leader                 | Human Resources        | Regular      | 42975.00     | **NEW HIRE ADDED** |
| **10036**   | **Jonathan Brosas**                 | IT Technical Support           | Information Technology | Probationary | 42975.00     | **NEW HIRE ADDED** |
| **10037**   | **Shella Mae Tejor**                | Customer Service and Relations | Customer Service       | Probationary | 52670.00     | **NEW HIRE ADDED** |





| Employee No | Employee Full Name   | Status       | Department             | Basic Salary | Gross Semi-Monthly Rate | Hourly Rate | Social Security No. | Philhealth No. | Pag-ibig No. | TIN             | Roster Status  |
| ----------- | -------------------- | ------------ | ---------------------- | ------------ | ----------------------- | ----------- | ------------------- | -------------- | ------------ | --------------- | -------------- |
| 10035       | Billy Lloyd Calasang | Regular      | Human Resources        | 42975.00     | 21487.50                | 268.59      | 37-3379841-1        | 632361534812   | 374357402374 | 824-311-682-000 | NEW HIRE ADDED |
| 10036       | Jonathan Brosas      | Probationary | Information Technology | 42975.00     | 21487.50                | 268.59      | 92-4800602-9        | 735270773421   | 632722676967 | 632-531-054-000 | NEW HIRE ADDED |
| 10037       | Shella Mae Tejor     | Probationary | Customer Service       | 52670.00     | 26335.00                | 329.19      | 32-5213838-6        | 675893056701   | 133337008927 | 327-367-815-000 | NEW HIRE ADDED |


- **Pass criteria:** Inserts succeed; AFTER CREATE full roster shows all **34 existing** employees plus the **3 NEW HIRE ADDED** rows (`10035`–`10037`). Screenshot the full AFTER CREATE result grid for Actual Result.

1. **Update Existing Employee Information**


| Test Case ID        | MMDC-DBTC01-B                                                                                                                                                                                                                                                                                                                       |
| ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Update Employee Information                                                                                                                                                                                                                                                                                                         |
| **Descriptor**      | This test case ensures that the database allows modifications to existing employee records.                                                                                                                                                                                                                                         |
| **Actions**         | Write a query that will display the following employee records: Carlos Ian Martinez Beatriz Santos John Rafael Castro Shella Mae Tejor Update the fields related to salary information based on the gross salary provided below: Carlos Ian Martinez 23,000 Beatriz Santos 25,000 John Rafael Castro 23,000 Shella Mae Tejor 25,000 |
| **Expected Result** | The four employees are retrieved with full summary-style identity columns; `Basic Salary` updates to **23000.00** / **25000.00** / **23000.00** / **25000.00**. Other identity fields (Position, Department, SSS, Philhealth, Pag-ibig, TIN) remain unchanged.                                                                      |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                                                                                                                                                                                                                            |


**SQL Query (MMDC-DBTC01-B)**

```sql
USE payrollsystem_db;

-- BEFORE update: target employees with summary-style columns + current salary
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    s.BaseSalary AS `Basic Salary`,
    ROUND(s.BaseSalary / 2, 2) AS `Gross Semi-Monthly Rate`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
WHERE e.EmployeeID IN (10032, 10033, 10034, 10037)
ORDER BY e.EmployeeID;

-- Also show them inside the full existing roster context
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    s.BaseSalary AS `Basic Salary`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
ORDER BY e.EmployeeID;

UPDATE Salary SET BaseSalary = 23000.00 WHERE EmployeeID = 10033; -- Carlos Ian Martinez
UPDATE Salary SET BaseSalary = 25000.00 WHERE EmployeeID = 10034; -- Beatriz Santos
UPDATE Salary SET BaseSalary = 23000.00 WHERE EmployeeID = 10032; -- John Rafael Castro
UPDATE Salary SET BaseSalary = 25000.00 WHERE EmployeeID = 10037; -- Shella Mae Tejor

-- AFTER update (screenshot this result grid)
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    s.BaseSalary AS `Basic Salary`,
    ROUND(s.BaseSalary / 2, 2) AS `Gross Semi-Monthly Rate`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
WHERE e.EmployeeID IN (10032, 10033, 10034, 10037)
ORDER BY e.EmployeeID;
```

**Explicit Expected Result (MMDC-DBTC01-B)**


| Employee No | Employee Full Name  | Basic Salary (AFTER) | Gross Semi-Monthly Rate |
| ----------- | ------------------- | -------------------- | ----------------------- |
| 10032       | John Rafael Castro  | 23000.00             | 11500.00                |
| 10033       | Carlos Ian Martinez | 23000.00             | 11500.00                |
| 10034       | Beatriz Santos      | 25000.00             | 12500.00                |
| 10037       | Shella Mae Tejor    | 25000.00             | 12500.00                |


- Position, Department, Social Security No., Philhealth No., Pag-ibig No., and TIN for these employees remain the same as before the update.
- **Pass criteria:** BEFORE returns 4 employees; each `UPDATE` affects 1 row; AFTER shows the four updated basic salaries above.

1. **Delete Employee Record**


| Test Case ID        | MMDC-DBTC001-C                                                                                                                                                                                                       |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Delete Employee Record                                                                                                                                                                                               |
| **Objective**       | This test case verifies that the database can properly delete an employee record and that the corresponding data is removed.                                                                                         |
| **Actions**         | Write a query that will delete the following employee records from the database: 29 Carol Ramos 30 Emelia Maceda 31 Delia Aguilar                                                                                    |
| **Expected Result** | Employees **10029**, **10030**, and **10031** are removed from the roster and from `vw_EmployeePayrollSummaryReport`. Final count for those IDs is **0**. Remaining employees still show with summary-style columns. |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                                                                                                             |


**SQL Query (MMDC-DBTC01-C)**

```sql
USE payrollsystem_db;

-- BEFORE delete: target employees with summary-style columns
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    s.BaseSalary AS `Basic Salary`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
WHERE e.EmployeeID IN (10029, 10030, 10031)
ORDER BY e.EmployeeID;
-- Expect 3 rows: Carol Ramos, Emelia Maceda, Delia Aguilar

SELECT COUNT(*) AS `Total Employees Before Delete` FROM Employee;

-- Existing payroll summary still includes them (if payslip data exists)
SELECT `Employee No`, `Employee Full Name`, `Position`, `Department`,
       `Gross Income`, `Social Security No.`, `Philhealth No.`,
       `Pag-ibig No.`, `TIN`, `Net Pay`
FROM vw_EmployeePayrollSummaryReport
WHERE `Employee No` IN (10029, 10030, 10031)
ORDER BY `Employee No`;

-- Delete child rows first (FK ON DELETE RESTRICT), then parent Employee rows
DELETE FROM Deduction
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID FROM Payroll WHERE EmployeeID IN (10029, 10030, 10031)
    ) AS p
);
DELETE FROM Payslip
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID FROM Payroll WHERE EmployeeID IN (10029, 10030, 10031)
    ) AS p
);
DELETE FROM Payroll WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Benefit WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Salary WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM GovernmentID WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM EmployeeAddress WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Attendance WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Overtime WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM `Leave` WHERE EmployeeID IN (10029, 10030, 10031);
DELETE FROM Employee WHERE EmployeeID IN (10029, 10030, 10031);

-- AFTER delete
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`
FROM Employee e
WHERE e.EmployeeID IN (10029, 10030, 10031);
-- Expect 0 rows

SELECT COUNT(*) AS `RemainingDeletedEmployees`
FROM Employee
WHERE EmployeeID IN (10029, 10030, 10031);
-- Expect 0

SELECT COUNT(*) AS `Total Employees After Delete` FROM Employee;
-- Expect previous total minus 3

-- Remaining roster still shows other employees with summary-style columns
SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`,
    e.Position AS `Position`,
    d.DepartmentName AS `Department`,
    g.SSSNumber AS `Social Security No.`,
    g.PhilHealthNumber AS `Philhealth No.`,
    g.PagIBIGNumber AS `Pag-ibig No.`,
    g.TINNumber AS `TIN`
FROM Employee e
INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
ORDER BY e.EmployeeID;

SELECT `Employee No`, `Employee Full Name`, `Net Pay`
FROM vw_EmployeePayrollSummaryReport
WHERE `Employee No` IN (10029, 10030, 10031);
-- Expect 0 rows
```

**Explicit Expected Result (MMDC-DBTC01-C)**


| Phase                            | Result                                                                                                                                      |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| BEFORE target SELECT             | 3 rows: **10029** Carol Ramos, **10030** Emelia Maceda, **10031** Delia Aguilar (with Position, Department, SSS, Philhealth, Pag-ibig, TIN) |
| AFTER target SELECT              | **0 rows**                                                                                                                                  |
| AFTER `COUNT(*)` for deleted IDs | **0**                                                                                                                                       |
| AFTER payroll summary filter     | **0 rows** for 10029–10031                                                                                                                  |
| Remaining roster                 | All other employees still listed with summary-style columns                                                                                 |


- **Pass criteria:** Deletes succeed; deleted IDs gone from `Employee` and from `vw_EmployeePayrollSummaryReport`; other employee data remains intact.

1. **MMDC-DBTC02  Verify Employee Data Constraints**
  1. **Check Employee ID Uniqueness**


| Test Case ID        | MMDC-DBTC02-A                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Check Employee ID Uniqueness                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| **Objective**       | This test case verifies that the database enforces the uniqueness constraint for Employee IDs, preventing the addition of employees with a predefined ID.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| **Actions**         | Attempt to add a new employee record: **Basic Information** Employee Number: 40 Employee Name: Mac Arnold Almirol Birthdate: October 8, 1996 Address: Unit 2802 One San Miguel Bldg, Shaw Blvd Cor San Miguel Ave, Ortigas Ctr 1605, Pasig City Phone Number: 477- 771-607 **Job Information** Position: IT Technical Support Status: Probationary Immediate Supervisor: Eduard Hernandez **Salary Information** Basic Salary: 42,975 Gross Semi-Monthly Rate: 21,487.5 Hourly Rate: 255.80 TIN: 936-540-856-000 Clothing Allowance: 800 Rice Allowance: 1,500 Phone Allowance: 800 **Social Security Information** SSS: 36-4160536-4 Philhealth: 862055202862 Pag-Ibig: 521301652682 |
| **Expected Result** | The database rejects the addition and displays an error message indicating the violation of the unique / identity constraint for Employee IDs. (`EmployeeID` is **AUTO_INCREMENT**; predefined ID **40** is not allowed when IDENTITY_INSERT is OFF — same as the sample screenshot.) |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |


**SQL Query (MMDC-DBTC02-A)**

> **Schema note:** `Employee.EmployeeID` is **AUTO_INCREMENT** (identity). Inserting predefined Employee Number **40** with identity insert OFF must fail — matching the sample: *Cannot insert explicit value for identity column … when IDENTITY_INSERT is set to OFF.*

```sql
USE payrollsystem_db;

SHOW COLUMNS FROM Employee LIKE 'EmployeeID';
-- Extra: auto_increment

SET @ALLOW_EXPLICIT_EMPLOYEE_ID = NULL;

-- Attempt to add Mac Arnold Almirol with predefined Employee Number 40
-- MUST FAIL (Error 544)
INSERT INTO Employee (
    EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    40, 'Mac Arnold', 'Almirol', '1996-10-08',
    'Unit 2802 One San Miguel Bldg, Shaw Blvd Cor San Miguel Ave, Ortigas Ctr 1605, Pasig City',
    '477-771-607', 'IT Technical Support', 2, 2
);
```

**Explicit Expected Result (MMDC-DBTC02-A) — Messages / Action Output**

```
Error Code: 544
Cannot insert explicit value for identity column in table 'Employee' when IDENTITY_INSERT is set to OFF.
```

| Outcome | Detail |
|---------|--------|
| Insert result | **Rejected** — Mac Arnold Almirol is **not** added |
| Error code | **544** |
| Error message | Cannot insert explicit value for identity column in table 'Employee' when IDENTITY_INSERT is set to OFF. |
| Pass criteria | Workbench shows red **X** / Error 544 (same meaning as the sample screenshot) |


1. **Check Null Values**


| Test Case ID        | MMDC-DBTC02-B                                                                                                                                                                                                                      |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Test Case Title** | Check Null Values                                                                                                                                                                                                                  |
| **Objective**       | This test case verifies that the database enforces the NOT NULL constraint for mandatory fields, ensuring that essential information is always provided.                                                                           |
| **Actions**         | Attempt to add a new employee record with the following information: Employee Name: Ian Correa Birthdate: December 16, 1996 Address: 8435 West Service RoadMarcelo Green Village South Superhighway, Paranaque City                |
| **Expected Result** | The database rejects the addition and displays an error message indicating the violation of the NOT NULL constraint for essential fields. (`EmployeeID` is auto-generated; `ContactNumber` and `Position` are left NULL.) |
| **Actual Result**   | ***Insert a screenshot of your test case result here.***                                                                                                                                                                           |


**SQL Query (MMDC-DBTC02-B)**

> **Schema note:** Omit `EmployeeID` so **AUTO_INCREMENT** assigns it. Homework supplies only name, birthdate, and address — set `ContactNumber` and `Position` to `NULL` to prove NOT NULL.

```sql
USE payrollsystem_db;

SELECT COUNT(*) AS `Total Employees Before Null Test` FROM Employee;

SET @ALLOW_EXPLICIT_EMPLOYEE_ID = NULL;

-- EmployeeID omitted (AUTO_INCREMENT). ContactNumber and Position are NULL.
-- MUST FAIL (Error 1048)
INSERT INTO Employee (
    FirstName, LastName, DateOfBirth, Address, ContactNumber,
    Position, DepartmentID, StatusID
) VALUES (
    'Ian', 'Correa', '1996-12-16',
    '8435 West Service Road Marcelo Green Village South Superhighway, Paranaque City',
    NULL,
    NULL,
    2,
    2
);

SELECT COUNT(*) AS `Total Employees After Null Test` FROM Employee;

SELECT
    e.EmployeeID AS `Employee No`,
    CONCAT(e.FirstName, ' ', e.LastName) AS `Employee Full Name`
FROM Employee e
WHERE e.FirstName = 'Ian' AND e.LastName = 'Correa';
-- Expect 0 rows
```

**Explicit Expected Result (MMDC-DBTC02-B) — Messages / Action Output**

```
Error Code: 1048
Column 'ContactNumber' cannot be null
```

| Outcome | Detail |
|---------|--------|
| Insert result | **Rejected** — Ian Correa is **not** added |
| Error code | **1048** |
| Error message | Column 'ContactNumber' cannot be null (or Position) |
| Pass criteria | Workbench shows red **X** / Error 1048 |


---

## Schema Adaptation & Execution Notes

| Homework reference | MotorPH normalized schema |
|--------------------|---------------------------|
| Employee ID column | **AUTO_INCREMENT** (identity) |
| Employee numbers 1–34 | IDs **10001–10034** (seeded with `@ALLOW_EXPLICIT_EMPLOYEE_ID = 1`) |
| New create employees | **10035–10037** (same flag ON during create test) |
| Delete IDs 29, 30, 31 | **10029**, **10030**, **10031** |
| Uniqueness / predefined ID 40 | Insert **40** with identity insert OFF → **Error 544** (sample-style message) |
| Immediate Supervisor | **Not stored** on `Employee` (3NF) |
| Gross Semi-Monthly / Hourly Rate | **Derived** from `Salary.BaseSalary` |
| Null test | Omit `EmployeeID` (auto); NULL on `ContactNumber` / `Position` → **Error 1048** |

### How to run in MySQL Workbench

1. **Redeploy** the database (`Terminal Assessment/payrollsystem_db_final.sql` or MS1 01–05) so `EmployeeID` is AUTO_INCREMENT and the identity trigger exists.
2. Run success-path tests: **DBTC01-A → DBTC01-B → DBTC01-C**.
3. Run **DBTC02-A** alone — expect **Error 544** (screenshot Messages; red X = pass).
4. Run **DBTC02-B** alone — expect **Error 1048** (screenshot Messages; red X = pass).
5. Tip: *Stop SQL execution on error* helps isolate constraint tests.
6. Full script: `Terminal Assessment/16_terminal_assessment_test_cases.sql`.
