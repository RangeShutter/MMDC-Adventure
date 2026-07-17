-- =============================================================================

-- MotorPH Payroll System - FINALIZED database script (Terminal Assessment)

-- Database: payrollsystem_db | MySQL 8.0+

-- Includes: MS1 schema/seed + M2 semi-monthly tax, pay periods, report views,

--           and stored procedures (definitions only).

-- Test cases: run 16_terminal_assessment_test_cases.sql separately.

-- Regenerate: python sql/build_terminal_assessment.py

-- =============================================================================



-- >>> BEGIN 01_create_database.sql

-- MotorPH Payroll System - Step 1: Create database
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS payrollsystem_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE payrollsystem_db;
SHOW TABLES;
SELECT COUNT(*) AS total_employees FROM Employee;
SELECT e.EmployeeID, e.FirstName, e.LastName, d.DepartmentName,
       es.StatusName, s.BaseSalary
FROM Employee e
JOIN Department d ON e.DepartmentID = d.DepartmentID
JOIN EmploymentStatus es ON e.StatusID = es.StatusID
JOIN Salary s ON e.EmployeeID = s.EmployeeID
WHERE e.EmployeeID = 10001;
SELECT * FROM GovernmentID WHERE EmployeeID = 10001;
SELECT BenefitType, Amount FROM Benefit WHERE EmployeeID = 10001;
SELECT COUNT(*) FROM SSSContributionBracket;
SELECT BracketID, RangeDescription, ContributionAmount
FROM SSSContributionBracket ORDER BY BracketID LIMIT 3;

-- <<< END 01_create_database.sql



-- >>> BEGIN 02_schema.sql

-- MotorPH Payroll System - Step 2-4: Schema (20 design tables + 4 statutory reference tables)
-- MySQL 8.0+ (CHECK constraints enforced)

USE payrollsystem_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS RolePermission;
DROP TABLE IF EXISTS UserRole;
DROP TABLE IF EXISTS UserAccount;
DROP TABLE IF EXISTS Deduction;
DROP TABLE IF EXISTS Payslip;
DROP TABLE IF EXISTS Payroll;
DROP TABLE IF EXISTS Overtime;
DROP TABLE IF EXISTS `Leave`;
DROP TABLE IF EXISTS Attendance;
DROP TABLE IF EXISTS Benefit;
DROP TABLE IF EXISTS Salary;
DROP TABLE IF EXISTS GovernmentID;
DROP TABLE IF EXISTS EmployeeAddress;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Department;
DROP TABLE IF EXISTS WithholdingTaxBracket;
DROP TABLE IF EXISTS PagibigContributionRate;
DROP TABLE IF EXISTS PhilhealthContributionRate;
DROP TABLE IF EXISTS SSSContributionBracket;
DROP TABLE IF EXISTS Permission;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS ApprovalStatus;
DROP TABLE IF EXISTS LeaveType;
DROP TABLE IF EXISTS EmploymentStatus;

SET FOREIGN_KEY_CHECKS = 1;

-- Lookup tables
CREATE TABLE EmploymentStatus (
    StatusID INT NOT NULL AUTO_INCREMENT,
    StatusName VARCHAR(50) NOT NULL,
    PRIMARY KEY (StatusID),
    UNIQUE KEY uq_employment_status_name (StatusName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE LeaveType (
    LeaveTypeID INT NOT NULL AUTO_INCREMENT,
    LeaveTypeName VARCHAR(100) NOT NULL,
    Description VARCHAR(255) NULL,
    PRIMARY KEY (LeaveTypeID),
    UNIQUE KEY uq_leave_type_name (LeaveTypeName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ApprovalStatus (
    ApprovalStatusID INT NOT NULL AUTO_INCREMENT,
    StatusName VARCHAR(50) NOT NULL,
    PRIMARY KEY (ApprovalStatusID),
    UNIQUE KEY uq_approval_status_name (StatusName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Role (
    RoleID INT NOT NULL AUTO_INCREMENT,
    RoleName VARCHAR(100) NOT NULL,
    PRIMARY KEY (RoleID),
    UNIQUE KEY uq_role_name (RoleName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Permission (
    PermissionID INT NOT NULL AUTO_INCREMENT,
    PermissionName VARCHAR(100) NOT NULL,
    Description VARCHAR(255) NULL,
    PRIMARY KEY (PermissionID),
    UNIQUE KEY uq_permission_name (PermissionName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Department (
    DepartmentID INT NOT NULL AUTO_INCREMENT,
    DepartmentName VARCHAR(100) NOT NULL,
    ManagerID INT NULL,
    PRIMARY KEY (DepartmentID),
    UNIQUE KEY uq_department_name (DepartmentName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Employee (
    EmployeeID INT NOT NULL,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Address VARCHAR(255) NULL,
    ContactNumber VARCHAR(20) NOT NULL,
    Position VARCHAR(100) NOT NULL,
    DepartmentID INT NOT NULL,
    StatusID INT NOT NULL,
    PRIMARY KEY (EmployeeID),
    CONSTRAINT fk_employee_department
        FOREIGN KEY (DepartmentID) REFERENCES Department (DepartmentID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_employee_status
        FOREIGN KEY (StatusID) REFERENCES EmploymentStatus (StatusID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    -- DateOfBirth <= today (design doc): MySQL CHECK cannot use CURDATE() (Error 3814); validate in app
    CONSTRAINT chk_employee_dob CHECK (DateOfBirth >= '1900-01-01')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE Department
    ADD CONSTRAINT fk_department_manager
        FOREIGN KEY (ManagerID) REFERENCES Employee (EmployeeID)
        ON DELETE SET NULL ON UPDATE CASCADE;

CREATE TABLE EmployeeAddress (
    AddressID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    StreetName VARCHAR(500) NOT NULL,
    Barangay VARCHAR(100) NULL,
    City VARCHAR(100) NULL,
    Province VARCHAR(100) NULL,
    ZIPCode VARCHAR(10) NULL,
    PRIMARY KEY (AddressID),
    UNIQUE KEY uq_employee_address (EmployeeID),
    CONSTRAINT fk_address_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE GovernmentID (
    GovernmentID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    SSSNumber VARCHAR(20) NOT NULL,
    PhilHealthNumber VARCHAR(20) NOT NULL,
    TINNumber VARCHAR(20) NOT NULL,
    PagIBIGNumber VARCHAR(20) NOT NULL,
    PRIMARY KEY (GovernmentID),
    UNIQUE KEY uq_government_employee (EmployeeID),
    UNIQUE KEY uq_sss_number (SSSNumber),
    UNIQUE KEY uq_philhealth_number (PhilHealthNumber),
    UNIQUE KEY uq_tin_number (TINNumber),
    UNIQUE KEY uq_pagibig_number (PagIBIGNumber),
    CONSTRAINT fk_government_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Salary (
    SalaryID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    BaseSalary DECIMAL(12, 2) NOT NULL,
    PayFrequency VARCHAR(50) NOT NULL,
    EffectiveFrom DATE NOT NULL,
    EffectiveTo DATE NULL,
    PRIMARY KEY (SalaryID),
    CONSTRAINT fk_salary_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_salary_base CHECK (BaseSalary >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Benefit (
    BenefitID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    BenefitType VARCHAR(100) NOT NULL,
    Amount DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (BenefitID),
    CONSTRAINT fk_benefit_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_benefit_amount CHECK (Amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Attendance (
    AttendanceID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    Date DATE NOT NULL,
    TimeIn TIMESTAMP NOT NULL,
    TimeOut TIMESTAMP NOT NULL,
    PRIMARY KEY (AttendanceID),
    CONSTRAINT fk_attendance_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_attendance_time CHECK (TimeOut >= TimeIn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Overtime (
    OvertimeID INT NOT NULL AUTO_INCREMENT,
    AttendanceID INT NOT NULL,
    Hours DECIMAL(5, 2) NOT NULL,
    Rate DECIMAL(10, 2) NOT NULL,
    ApprovedBy INT NULL,
    ApprovalStatusID INT NOT NULL,
    ApprovalDate TIMESTAMP NULL,
    PRIMARY KEY (OvertimeID),
    CONSTRAINT fk_overtime_attendance
        FOREIGN KEY (AttendanceID) REFERENCES Attendance (AttendanceID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_overtime_approver
        FOREIGN KEY (ApprovedBy) REFERENCES Employee (EmployeeID)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_overtime_approval_status
        FOREIGN KEY (ApprovalStatusID) REFERENCES ApprovalStatus (ApprovalStatusID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_overtime_hours CHECK (Hours >= 0),
    CONSTRAINT chk_overtime_rate CHECK (Rate >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `Leave` (
    LeaveID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    LeaveTypeID INT NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    ApprovedBy INT NULL,
    ApprovalStatusID INT NOT NULL,
    ApprovalDate TIMESTAMP NULL,
    PRIMARY KEY (LeaveID),
    CONSTRAINT fk_leave_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_leave_type
        FOREIGN KEY (LeaveTypeID) REFERENCES LeaveType (LeaveTypeID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_leave_approver
        FOREIGN KEY (ApprovedBy) REFERENCES Employee (EmployeeID)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_leave_approval_status
        FOREIGN KEY (ApprovalStatusID) REFERENCES ApprovalStatus (ApprovalStatusID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_leave_dates CHECK (EndDate >= StartDate)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Payroll (
    PayrollID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    PayPeriodStart DATE NOT NULL,
    PayPeriodEnd DATE NOT NULL,
    GrossPay DECIMAL(12, 2) NOT NULL,
    NetPay DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (PayrollID),
    UNIQUE KEY uq_payroll_period (EmployeeID, PayPeriodStart, PayPeriodEnd),
    CONSTRAINT fk_payroll_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_payroll_gross CHECK (GrossPay >= 0),
    CONSTRAINT chk_payroll_net CHECK (NetPay >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Deduction (
    DeductionID INT NOT NULL AUTO_INCREMENT,
    PayrollID INT NOT NULL,
    DeductionType VARCHAR(100) NOT NULL,
    Amount DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (DeductionID),
    UNIQUE KEY uq_deduction_payroll_type (PayrollID, DeductionType),
    CONSTRAINT fk_deduction_payroll
        FOREIGN KEY (PayrollID) REFERENCES Payroll (PayrollID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_deduction_amount CHECK (Amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE Payslip (
    PayslipID INT NOT NULL AUTO_INCREMENT,
    PayrollID INT NOT NULL,
    IssueDate DATE NOT NULL,
    PRIMARY KEY (PayslipID),
    UNIQUE KEY uq_payslip_payroll (PayrollID),
    CONSTRAINT fk_payslip_payroll
        FOREIGN KEY (PayrollID) REFERENCES Payroll (PayrollID)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE UserAccount (
    UserID INT NOT NULL AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    Username VARCHAR(100) NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    PRIMARY KEY (UserID),
    UNIQUE KEY uq_user_username (Username),
    UNIQUE KEY uq_user_employee (EmployeeID),
    CONSTRAINT fk_user_employee
        FOREIGN KEY (EmployeeID) REFERENCES Employee (EmployeeID)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE UserRole (
    UserRoleID INT NOT NULL AUTO_INCREMENT,
    UserID INT NOT NULL,
    RoleID INT NOT NULL,
    PRIMARY KEY (UserRoleID),
    UNIQUE KEY uq_user_role (UserID, RoleID),
    CONSTRAINT fk_userrole_user
        FOREIGN KEY (UserID) REFERENCES UserAccount (UserID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_userrole_role
        FOREIGN KEY (RoleID) REFERENCES Role (RoleID)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE RolePermission (
    RolePermissionID INT NOT NULL AUTO_INCREMENT,
    RoleID INT NOT NULL,
    PermissionID INT NOT NULL,
    PRIMARY KEY (RolePermissionID),
    UNIQUE KEY uq_role_permission (RoleID, PermissionID),
    CONSTRAINT fk_rolepermission_role
        FOREIGN KEY (RoleID) REFERENCES Role (RoleID)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_rolepermission_permission
        FOREIGN KEY (PermissionID) REFERENCES Permission (PermissionID)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Statutory reference tables (MotorPH contribution/tax schedules; homework datasets)
CREATE TABLE SSSContributionBracket (
    BracketID INT NOT NULL AUTO_INCREMENT,
    RangeFrom DECIMAL(12, 2) NULL,
    RangeTo DECIMAL(12, 2) NULL,
    ContributionAmount DECIMAL(12, 2) NOT NULL,
    RangeDescription VARCHAR(100) NULL,
    PRIMARY KEY (BracketID),
    CONSTRAINT chk_sss_contribution CHECK (ContributionAmount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE PhilhealthContributionRate (
    RateID INT NOT NULL AUTO_INCREMENT,
    SalaryMin DECIMAL(12, 2) NOT NULL,
    SalaryMax DECIMAL(12, 2) NULL,
    PremiumRate DECIMAL(5, 4) NOT NULL,
    MonthlyPremiumCap DECIMAL(12, 2) NULL,
    Notes VARCHAR(255) NULL,
    PRIMARY KEY (RateID),
    CONSTRAINT chk_philhealth_premium CHECK (PremiumRate >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE PagibigContributionRate (
    RateID INT NOT NULL AUTO_INCREMENT,
    SalaryMin DECIMAL(12, 2) NOT NULL,
    SalaryMax DECIMAL(12, 2) NULL,
    EmployeeRate DECIMAL(5, 4) NOT NULL,
    EmployerRate DECIMAL(5, 4) NOT NULL,
    MaxContribution DECIMAL(12, 2) NOT NULL,
    Notes VARCHAR(255) NULL,
    PRIMARY KEY (RateID),
    CONSTRAINT chk_pagibig_max CHECK (MaxContribution >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE WithholdingTaxBracket (
    BracketID INT NOT NULL AUTO_INCREMENT,
    MonthlyRateMin DECIMAL(12, 2) NOT NULL,
    MonthlyRateMax DECIMAL(12, 2) NULL,
    TaxRuleDescription VARCHAR(255) NOT NULL,
    BaseTax DECIMAL(12, 2) NOT NULL DEFAULT 0,
    ExcessRate DECIMAL(5, 4) NULL,
    ExcessOver DECIMAL(12, 2) NULL,
    PRIMARY KEY (BracketID),
    CONSTRAINT chk_withholding_base CHECK (BaseTax >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- <<< END 02_schema.sql



-- >>> BEGIN 03_seed_lookup.sql

-- MotorPH Payroll System - Lookup and RBAC seed data

USE payrollsystem_db;

-- Employment status
INSERT INTO EmploymentStatus (StatusID, StatusName) VALUES
(1, 'Regular'),
(2, 'Probationary'),
(3, 'Resigned'),
(4, 'Terminated');

-- Leave types
INSERT INTO LeaveType (LeaveTypeID, LeaveTypeName, Description) VALUES
(1, 'Sick Leave', 'Medical or health-related leave'),
(2, 'Vacation Leave', 'Planned personal leave'),
(3, 'Emergency Leave', 'Unplanned urgent leave');

-- Approval statuses
INSERT INTO ApprovalStatus (ApprovalStatusID, StatusName) VALUES
(1, 'Pending'),
(2, 'Approved'),
(3, 'Rejected');

-- Departments (ManagerID updated after employees load in 04_seed_employees.sql)
INSERT INTO Department (DepartmentID, DepartmentName, ManagerID) VALUES
(1, 'Executive', NULL),
(2, 'Information Technology', NULL),
(3, 'Human Resources', NULL),
(4, 'Finance', NULL),
(5, 'Sales and Accounts', NULL),
(6, 'Marketing', NULL),
(7, 'Logistics', NULL),
(8, 'Customer Service', NULL);

-- Roles (RBAC)
INSERT INTO Role (RoleID, RoleName) VALUES
(1, 'HR Administrator'),
(2, 'Finance Officer'),
(3, 'Supervisor'),
(4, 'Employee'),
(5, 'System Administrator');

-- Permissions
INSERT INTO Permission (PermissionID, PermissionName, Description) VALUES
(1, 'manage_employees', 'Add, update, and delete employee records'),
(2, 'view_payroll', 'View payroll records and reports'),
(3, 'manage_payroll', 'Generate and manage payroll records'),
(4, 'approve_leave', 'Approve or reject leave requests'),
(5, 'approve_overtime', 'Approve or reject overtime requests'),
(6, 'manage_users', 'Manage user accounts, roles, and permissions'),
(7, 'view_own_records', 'View personal attendance, leave, and payslip');

-- Role-permission mappings
INSERT INTO RolePermission (RoleID, PermissionID) VALUES
(1, 1), (1, 4), (1, 5),
(2, 2), (2, 3),
(3, 4), (3, 5), (3, 2),
(4, 7),
(5, 1), (5, 2), (5, 3), (5, 6);

-- <<< END 03_seed_lookup.sql



-- >>> BEGIN 04_seed_employees.sql

-- MotorPH Payroll System - Employee and related seed data
-- Generated from document/MotorPH_Employee Details.md
-- Generated: 2026-06-01T05:50:31

USE payrollsystem_db;

-- Employees
INSERT INTO Employee (EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber, Position, DepartmentID, StatusID) VALUES
(10001, 'Manuel III', 'Garcia', '1983-10-11', NULL, '966-860-270', 'Chief Executive Officer', 1, 1),
(10002, 'Antonio', 'Lim', '1988-06-19', NULL, '171-867-411', 'Chief Operating Officer', 1, 1),
(10003, 'Bianca Sofia', 'Aquino', '1989-08-04', NULL, '966-889-370', 'Chief Finance Officer', 1, 1),
(10004, 'Isabella', 'Reyes', '1994-06-16', NULL, '786-868-477', 'Chief Marketing Officer', 1, 1),
(10005, 'Eduard', 'Hernandez', '1989-09-23', NULL, '088-861-012', 'IT Operations and Systems', 2, 1),
(10006, 'Andrea Mae', 'Villanueva', '1988-02-14', NULL, '918-621-603', 'HR Manager', 3, 1),
(10007, 'Brad', 'San Jose', '1996-03-15', NULL, '797-009-261', 'HR Team Leader', 3, 1),
(10008, 'Alice', 'Romualdez', '1992-05-14', NULL, '983-606-799', 'HR Rank and File', 3, 1),
(10009, 'Rosie', 'Atienza', '1948-09-24', NULL, '266-036-427', 'HR Rank and File', 3, 1),
(10010, 'Roderick', 'Alvaro', '1988-03-30', NULL, '053-381-386', 'Accounting Head', 4, 1),
(10011, 'Anthony', 'Salcedo', '1993-09-14', NULL, '070-766-300', 'Payroll Manager', 4, 1),
(10012, 'Josie', 'Lopez', '1987-01-14', NULL, '478-355-427', 'Payroll Team Leader', 4, 1),
(10013, 'Martha', 'Farala', '1942-01-11', NULL, '329-034-366', 'Payroll Rank and File', 4, 1),
(10014, 'Leila', 'Martinez', '1970-07-11', NULL, '877-110-749', 'Payroll Rank and File', 4, 1),
(10015, 'Fredrick', 'Romualdez', '1985-03-10', NULL, '023-079-009', 'Account Manager', 5, 1),
(10016, 'Christian', 'Mata', '1987-10-21', NULL, '783-776-744', 'Account Team Leader', 5, 1),
(10017, 'Selena', 'De Leon', '1975-02-20', NULL, '975-432-139', 'Account Team Leader', 5, 1),
(10018, 'Allison', 'San Jose', '1986-06-24', NULL, '179-075-129', 'Account Rank and File', 5, 1),
(10019, 'Cydney', 'Rosario', '1996-10-06', NULL, '868-819-912', 'Account Rank and File', 5, 1),
(10020, 'Mark', 'Bautista', '1991-02-12', NULL, '683-725-348', 'Account Rank and File', 5, 1),
(10021, 'Darlene', 'Lazaro', '1985-11-25', NULL, '740-721-558', 'Account Rank and File', 5, 2),
(10022, 'Kolby', 'Delos Santos', '1980-02-26', NULL, '739-443-033', 'Account Rank and File', 5, 2),
(10023, 'Vella', 'Santos', '1983-12-31', NULL, '955-879-269', 'Account Rank and File', 5, 2),
(10024, 'Tomas', 'Del Rosario', '1978-12-18', NULL, '882-550-989', 'Account Rank and File', 5, 2),
(10025, 'Jacklyn', 'Tolentino', '1984-05-19', NULL, '675-757-366', 'Account Rank and File', 5, 2),
(10026, 'Percival', 'Gutierrez', '1970-12-18', NULL, '512-899-876', 'Account Rank and File', 5, 2),
(10027, 'Garfield', 'Manalaysay', '1986-08-28', NULL, '948-628-136', 'Account Rank and File', 5, 2),
(10028, 'Lizeth', 'Villegas', '1981-12-12', NULL, '332-372-215', 'Account Rank and File', 5, 2),
(10029, 'Carol', 'Ramos', '1978-08-20', NULL, '250-700-389', 'Account Rank and File', 5, 2),
(10030, 'Emelia', 'Maceda', '1973-04-14', NULL, '973-358-041', 'Account Rank and File', 5, 2),
(10031, 'Delia', 'Aguilar', '1989-01-27', NULL, '529-705-439', 'Account Rank and File', 5, 2),
(10032, 'John Rafael', 'Castro', '1992-02-09', NULL, '332-424-955', 'Sales & Marketing', 6, 1),
(10033, 'Carlos Ian', 'Martinez', '1990-11-16', NULL, '078-854-208', 'Supply Chain and Logistics', 7, 1),
(10034, 'Beatriz', 'Santos', '1990-08-07', NULL, '526-639-511', 'Customer Service and Relations', 8, 1);

-- Assign department managers
UPDATE Department SET ManagerID = 10001 WHERE DepartmentID = 1;
UPDATE Department SET ManagerID = 10005 WHERE DepartmentID = 2;
UPDATE Department SET ManagerID = 10006 WHERE DepartmentID = 3;
UPDATE Department SET ManagerID = 10010 WHERE DepartmentID = 4;
UPDATE Department SET ManagerID = 10015 WHERE DepartmentID = 5;
UPDATE Department SET ManagerID = 10004 WHERE DepartmentID = 6;
UPDATE Department SET ManagerID = 10033 WHERE DepartmentID = 7;
UPDATE Department SET ManagerID = 10034 WHERE DepartmentID = 8;

-- Employee addresses (full address in StreetName)
INSERT INTO EmployeeAddress (EmployeeID, StreetName, Barangay, City, Province, ZIPCode) VALUES
(10001, 'Valero Carpark Building Valero Street 1227, Makati City', NULL, NULL, NULL, NULL),
(10002, 'San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite', NULL, NULL, NULL, NULL),
(10003, 'Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City', NULL, NULL, NULL, NULL),
(10004, '460 Solanda Street Intramuros 1000, Manila', NULL, NULL, NULL, NULL),
(10005, 'National Highway, Gingoog,  Misamis Occidental', NULL, NULL, NULL, NULL),
(10006, '17/85 Stracke Via Suite 042, Poblacion, Las Piñas 4783 Dinagat Islands', NULL, NULL, NULL, NULL),
(10007, '99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi', NULL, NULL, NULL, NULL),
(10008, '12A/33 Upton Isle Apt. 420, Roxas City 1814 Surigao del Norte', NULL, NULL, NULL, NULL),
(10009, '90A Dibbert Terrace Apt. 190, San Lorenzo 6056 Davao del Norte', NULL, NULL, NULL, NULL),
(10010, '#284 T. Morato corner, Scout Rallos Street, Quezon City', NULL, NULL, NULL, NULL),
(10011, '93/54 Shanahan Alley Apt. 183, Santo Tomas 1572 Masbate', NULL, NULL, NULL, NULL),
(10012, '49 Springs Apt. 266, Poblacion, Taguig 3200 Occidental Mindoro', NULL, NULL, NULL, NULL),
(10013, '42/25 Sawayn Stream, Ubay 1208 Zamboanga del Norte', NULL, NULL, NULL, NULL),
(10014, '37/46 Kulas Roads, Maragondon 0962 Quirino', NULL, NULL, NULL, NULL),
(10015, '22A/52 Lubowitz Meadows, Pililla 4895 Zambales', NULL, NULL, NULL, NULL),
(10016, '90 O''Keefe Spur Apt. 379, Catigbian 2772 Sulu', NULL, NULL, NULL, NULL),
(10017, '89A Armstrong Trace, Compostela 7874 Maguindanao', NULL, NULL, NULL, NULL),
(10018, '08 Grant Drive Suite 406, Poblacion, Iloilo City 9186 La Union', NULL, NULL, NULL, NULL),
(10019, '93A/21 Berge Points, Tapaz 2180 Quezon', NULL, NULL, NULL, NULL),
(10020, '65 Murphy Center Suite 094, Poblacion, Palayan 5636 Quirino', NULL, NULL, NULL, NULL),
(10021, '47A/94 Larkin Plaza Apt. 179, Poblacion, Caloocan 2751 Quirino', NULL, NULL, NULL, NULL),
(10022, '06A Gulgowski Extensions, Bongabon 6085 Zamboanga del Sur', NULL, NULL, NULL, NULL),
(10023, '99A Padberg Spring, Poblacion, Mabalacat 3959 Lanao del Sur', NULL, NULL, NULL, NULL),
(10024, '80A/48 Ledner Ridges, Poblacion, Kabankalan 8870 Marinduque', NULL, NULL, NULL, NULL),
(10025, '96/48 Watsica Flats Suite 734, Poblacion, Malolos 1844 Ifugao', NULL, NULL, NULL, NULL),
(10026, '58A Wilderman Walks, Poblacion, Digos 5822 Davao del Sur', NULL, NULL, NULL, NULL),
(10027, '60 Goyette Valley Suite 219, Poblacion, Tabuk 3159 Lanao del Sur', NULL, NULL, NULL, NULL),
(10028, '66/77 Mann Views, Luisiana 1263 Dinagat Islands', NULL, NULL, NULL, NULL),
(10029, '72/70 Stamm Spurs, Bustos 4550 Iloilo', NULL, NULL, NULL, NULL),
(10030, '50A/83 Bahringer Oval Suite 145, Kiamba 7688 Nueva Ecija', NULL, NULL, NULL, NULL),
(10031, '95 Cremin Junction, Surallah 2809 Cotabato', NULL, NULL, NULL, NULL),
(10032, 'Hi-way, Yati, Liloan Cebu', NULL, NULL, NULL, NULL),
(10033, 'Bulala, Camalaniugan', NULL, NULL, NULL, NULL),
(10034, 'Agapita Building, Metro Manila', NULL, NULL, NULL, NULL);

INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber) VALUES
(10001, '44-4506057-3', '820126853951', '442-605-657-000', '691295330870'),
(10002, '52-2061274-9', '331735646338', '683-102-776-000', '663904995411'),
(10003, '30-8870406-2', '177451189665', '971-711-280-000', '171519773969'),
(10004, '40-2511815-0', '341911411254', '876-809-437-000', '416946776041'),
(10005, '50-5577638-1', '957436191812', '031-702-374-000', '952347222457'),
(10006, '49-1632020-8', '382189453145', '317-674-022-000', '441093369646'),
(10007, '40-2400714-1', '239192926939', '672-474-690-000', '210850209964'),
(10008, '55-4476527-2', '545652640232', '888-572-294-000', '211385556888'),
(10009, '41-0644692-3', '708988234853', '604-997-793-000', '260107732354'),
(10010, '64-7605054-4', '578114853194', '525-420-419-000', '799254095212'),
(10011, '26-9647608-3', '126445315651', '210-805-911-000', '218002473454'),
(10012, '44-8563448-3', '431709011012', '218-489-737-000', '113071293354'),
(10013, '45-5656375-0', '233693897247', '210-835-851-000', '631130283546'),
(10014, '27-2090996-4', '515741057496', '275-792-513-000', '101205445886'),
(10015, '26-8768374-1', '308366860059', '598-065-761-000', '223057707853'),
(10016, '49-2959312-6', '824187961962', '103-100-522-000', '631052853464'),
(10017, '27-2090208-8', '587272469938', '482-259-498-000', '719007608464'),
(10018, '45-3251383-0', '745148459521', '121-203-336-000', '114901859343'),
(10019, '49-1629900-2', '579253435499', '122-244-511-000', '265104358643'),
(10020, '49-1647342-5', '399665157135', '273-970-941-000', '260054585575'),
(10021, '45-5617168-2', '606386917510', '354-650-951-000', '104907708845'),
(10022, '52-0109570-6', '357451271274', '187-500-345-000', '113017988667'),
(10023, '52-9883524-3', '548670482885', '101-558-994-000', '360028104576'),
(10024, '45-5866331-6', '953901539995', '560-735-732-000', '913108649964'),
(10025, '47-1692793-0', '753800654114', '841-177-857-000', '210546661243'),
(10026, '40-9504657-8', '797639382265', '502-995-671-000', '210897095686'),
(10027, '45-3298166-4', '810909286264', '336-676-445-000', '211274476563'),
(10028, '40-2400719-4', '934389652994', '210-395-397-000', '122238077997'),
(10029, '60-1152206-4', '351830469744', '395-032-717-000', '212141893454'),
(10030, '54-1331005-0', '465087894112', '215-973-013-000', '515012579765'),
(10031, '52-1859253-1', '136451303068', '599-312-588-000', '110018813465'),
(10032, '26-7145133-4', '601644902402', '404-768-309-000', '697764000000'),
(10033, '11-5062972-7', '380685387212', '256-436-296-000', '993372963726'),
(10034, '20-2987501-5', '918460050077', '911-529-713-000', '874042259378');

INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo) VALUES
(10001, 90000.00, 'Monthly', '2024-01-01', NULL),
(10002, 60000.00, 'Monthly', '2024-01-01', NULL),
(10003, 60000.00, 'Monthly', '2024-01-01', NULL),
(10004, 60000.00, 'Monthly', '2024-01-01', NULL),
(10005, 52670.00, 'Monthly', '2024-01-01', NULL),
(10006, 52670.00, 'Monthly', '2024-01-01', NULL),
(10007, 42975.00, 'Monthly', '2024-01-01', NULL),
(10008, 22500.00, 'Monthly', '2024-01-01', NULL),
(10009, 22500.00, 'Monthly', '2024-01-01', NULL),
(10010, 52670.00, 'Monthly', '2024-01-01', NULL),
(10011, 50825.00, 'Monthly', '2024-01-01', NULL),
(10012, 38475.00, 'Monthly', '2024-01-01', NULL),
(10013, 24000.00, 'Monthly', '2024-01-01', NULL),
(10014, 24000.00, 'Monthly', '2024-01-01', NULL),
(10015, 53500.00, 'Monthly', '2024-01-01', NULL),
(10016, 42975.00, 'Monthly', '2024-01-01', NULL),
(10017, 41850.00, 'Monthly', '2024-01-01', NULL),
(10018, 22500.00, 'Monthly', '2024-01-01', NULL),
(10019, 22500.00, 'Monthly', '2024-01-01', NULL),
(10020, 23250.00, 'Monthly', '2024-01-01', NULL),
(10021, 23250.00, 'Monthly', '2024-01-01', NULL),
(10022, 24000.00, 'Monthly', '2024-01-01', NULL),
(10023, 22500.00, 'Monthly', '2024-01-01', NULL),
(10024, 22500.00, 'Monthly', '2024-01-01', NULL),
(10025, 24000.00, 'Monthly', '2024-01-01', NULL),
(10026, 24750.00, 'Monthly', '2024-01-01', NULL),
(10027, 24750.00, 'Monthly', '2024-01-01', NULL),
(10028, 24000.00, 'Monthly', '2024-01-01', NULL),
(10029, 22500.00, 'Monthly', '2024-01-01', NULL),
(10030, 22500.00, 'Monthly', '2024-01-01', NULL),
(10031, 22500.00, 'Monthly', '2024-01-01', NULL),
(10032, 52670.00, 'Monthly', '2024-01-01', NULL),
(10033, 52670.00, 'Monthly', '2024-01-01', NULL),
(10034, 52670.00, 'Monthly', '2024-01-01', NULL);

INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES
(10001, 'Rice Subsidy', 1500.00),
(10001, 'Phone Allowance', 2000.00),
(10001, 'Clothing Allowance', 1000.00),
(10002, 'Rice Subsidy', 1500.00),
(10002, 'Phone Allowance', 2000.00),
(10002, 'Clothing Allowance', 1000.00),
(10003, 'Rice Subsidy', 1500.00),
(10003, 'Phone Allowance', 2000.00),
(10003, 'Clothing Allowance', 1000.00),
(10004, 'Rice Subsidy', 1500.00),
(10004, 'Phone Allowance', 2000.00),
(10004, 'Clothing Allowance', 1000.00),
(10005, 'Rice Subsidy', 1500.00),
(10005, 'Phone Allowance', 1000.00),
(10005, 'Clothing Allowance', 1000.00),
(10006, 'Rice Subsidy', 1500.00),
(10006, 'Phone Allowance', 1000.00),
(10006, 'Clothing Allowance', 1000.00),
(10007, 'Rice Subsidy', 1500.00),
(10007, 'Phone Allowance', 800.00),
(10007, 'Clothing Allowance', 800.00),
(10008, 'Rice Subsidy', 1500.00),
(10008, 'Phone Allowance', 500.00),
(10008, 'Clothing Allowance', 500.00),
(10009, 'Rice Subsidy', 1500.00),
(10009, 'Phone Allowance', 500.00),
(10009, 'Clothing Allowance', 500.00),
(10010, 'Rice Subsidy', 1500.00),
(10010, 'Phone Allowance', 1000.00),
(10010, 'Clothing Allowance', 1000.00),
(10011, 'Rice Subsidy', 1500.00),
(10011, 'Phone Allowance', 1000.00),
(10011, 'Clothing Allowance', 1000.00),
(10012, 'Rice Subsidy', 1500.00),
(10012, 'Phone Allowance', 800.00),
(10012, 'Clothing Allowance', 800.00),
(10013, 'Rice Subsidy', 1500.00),
(10013, 'Phone Allowance', 500.00),
(10013, 'Clothing Allowance', 500.00),
(10014, 'Rice Subsidy', 1500.00),
(10014, 'Phone Allowance', 500.00),
(10014, 'Clothing Allowance', 500.00),
(10015, 'Rice Subsidy', 1500.00),
(10015, 'Phone Allowance', 1000.00),
(10015, 'Clothing Allowance', 1000.00),
(10016, 'Rice Subsidy', 1500.00),
(10016, 'Phone Allowance', 800.00),
(10016, 'Clothing Allowance', 800.00),
(10017, 'Rice Subsidy', 1500.00),
(10017, 'Phone Allowance', 800.00),
(10017, 'Clothing Allowance', 800.00),
(10018, 'Rice Subsidy', 1500.00),
(10018, 'Phone Allowance', 500.00),
(10018, 'Clothing Allowance', 500.00),
(10019, 'Rice Subsidy', 1500.00),
(10019, 'Phone Allowance', 500.00),
(10019, 'Clothing Allowance', 500.00),
(10020, 'Rice Subsidy', 1500.00),
(10020, 'Phone Allowance', 500.00),
(10020, 'Clothing Allowance', 500.00),
(10021, 'Rice Subsidy', 1500.00),
(10021, 'Phone Allowance', 500.00),
(10021, 'Clothing Allowance', 500.00),
(10022, 'Rice Subsidy', 1500.00),
(10022, 'Phone Allowance', 500.00),
(10022, 'Clothing Allowance', 500.00),
(10023, 'Rice Subsidy', 1500.00),
(10023, 'Phone Allowance', 500.00),
(10023, 'Clothing Allowance', 500.00),
(10024, 'Rice Subsidy', 1500.00),
(10024, 'Phone Allowance', 500.00),
(10024, 'Clothing Allowance', 500.00),
(10025, 'Rice Subsidy', 1500.00),
(10025, 'Phone Allowance', 500.00),
(10025, 'Clothing Allowance', 500.00),
(10026, 'Rice Subsidy', 1500.00),
(10026, 'Phone Allowance', 500.00),
(10026, 'Clothing Allowance', 500.00),
(10027, 'Rice Subsidy', 1500.00),
(10027, 'Phone Allowance', 500.00),
(10027, 'Clothing Allowance', 500.00),
(10028, 'Rice Subsidy', 1500.00),
(10028, 'Phone Allowance', 500.00),
(10028, 'Clothing Allowance', 500.00),
(10029, 'Rice Subsidy', 1500.00),
(10029, 'Phone Allowance', 500.00),
(10029, 'Clothing Allowance', 500.00),
(10030, 'Rice Subsidy', 1500.00),
(10030, 'Phone Allowance', 500.00),
(10030, 'Clothing Allowance', 500.00),
(10031, 'Rice Subsidy', 1500.00),
(10031, 'Phone Allowance', 500.00),
(10031, 'Clothing Allowance', 500.00),
(10032, 'Rice Subsidy', 1500.00),
(10032, 'Phone Allowance', 1000.00),
(10032, 'Clothing Allowance', 1000.00),
(10033, 'Rice Subsidy', 1500.00),
(10033, 'Phone Allowance', 1000.00),
(10033, 'Clothing Allowance', 1000.00),
(10034, 'Rice Subsidy', 1500.00),
(10034, 'Phone Allowance', 1000.00),
(10034, 'Clothing Allowance', 1000.00);

-- <<< END 04_seed_employees.sql



-- >>> BEGIN 05_seed_statutory.sql

-- MotorPH Payroll System - Statutory contribution and withholding tax reference data
-- Source: document/SSS Contribution.md, Philhealth Contribution.md,
--         Pag-ibig Contribution.md, Witholding Tax.md

USE payrollsystem_db;

-- SSS contribution brackets (46 rows)
INSERT INTO SSSContributionBracket (RangeFrom, RangeTo, ContributionAmount, RangeDescription) VALUES
(0.00, 3249.99, 135.00, 'Below 3,250'),
(3250.00, 3750.00, 157.50, '3,250 - 3,750'),
(3750.01, 4250.00, 180.00, '3,750 - 4,250'),
(4250.01, 4750.00, 202.50, '4,250 - 4,750'),
(4750.01, 5250.00, 225.00, '4,750 - 5,250'),
(5250.01, 5750.00, 247.50, '5,250 - 5,750'),
(5750.01, 6250.00, 270.00, '5,750 - 6,250'),
(6250.01, 6750.00, 292.50, '6,250 - 6,750'),
(6750.01, 7250.00, 315.00, '6,750 - 7,250'),
(7250.01, 7750.00, 337.50, '7,250 - 7,750'),
(7750.01, 8250.00, 360.00, '7,750 - 8,250'),
(8250.01, 8750.00, 382.50, '8,250 - 8,750'),
(8750.01, 9250.00, 405.00, '8,750 - 9,250'),
(9250.01, 9750.00, 427.50, '9,250 - 9,750'),
(9750.01, 10250.00, 450.00, '9,750 - 10,250'),
(10250.01, 10750.00, 472.50, '10,250 - 10,750'),
(10750.01, 11250.00, 495.00, '10,750 - 11,250'),
(11250.01, 11750.00, 517.50, '11,250 - 11,750'),
(11750.01, 12250.00, 540.00, '11,750 - 12,250'),
(12250.01, 12750.00, 562.50, '12,250 - 12,750'),
(12750.01, 13250.00, 585.00, '12,750 - 13,250'),
(13250.01, 13750.00, 607.50, '13,250 - 13,750'),
(13750.01, 14250.00, 630.00, '13,750 - 14,250'),
(14250.01, 14750.00, 652.50, '14,250 - 14,750'),
(14750.01, 15250.00, 675.00, '14,750 - 15,250'),
(15250.01, 15750.00, 697.50, '15,250 - 15,750'),
(15750.01, 16250.00, 720.00, '15,750 - 16,250'),
(16250.01, 16750.00, 742.50, '16,250 - 16,750'),
(16750.01, 17250.00, 765.00, '16,750 - 17,250'),
(17250.01, 17750.00, 787.50, '17,250 - 17,750'),
(17750.01, 18250.00, 810.00, '17,750 - 18,250'),
(18250.01, 18750.00, 832.50, '18,250 - 18,750'),
(18750.01, 19250.00, 855.00, '18,750 - 19,250'),
(19250.01, 19750.00, 877.50, '19,250 - 19,750'),
(19750.01, 20250.00, 900.00, '19,750 - 20,250'),
(20250.01, 20750.00, 922.50, '20,250 - 20,750'),
(20750.01, 21250.00, 945.00, '20,750 - 21,250'),
(21250.01, 21750.00, 967.50, '21,250 - 21,750'),
(21750.01, 22250.00, 990.00, '21,750 - 22,250'),
(22250.01, 22750.00, 1012.50, '22,250 - 22,750'),
(22750.01, 23250.00, 1035.00, '22,750 - 23,250'),
(23250.01, 23750.00, 1057.50, '23,250 - 23,750'),
(23750.01, 24250.00, 1080.00, '23,750 - 24,250'),
(24250.01, 24750.00, 1102.50, '24,250 - 24,750'),
(24750.00, NULL, 1125.00, '24,750 - Over');

-- PhilHealth contribution rates (3 tiers from dataset)
INSERT INTO PhilhealthContributionRate (SalaryMin, SalaryMax, PremiumRate, MonthlyPremiumCap, Notes) VALUES
(0.00, 10000.00, 0.0300, 300.00, 'Monthly basic salary 10,000'),
(10000.01, 59999.99, 0.0300, 1800.00, '10,000.01 to 59,999.99 - premium 300 up to 1,800'),
(60000.00, NULL, 0.0300, 1800.00, 'Monthly basic salary 60,000 - cap 1,800');

-- Pag-IBIG contribution rates
INSERT INTO PagibigContributionRate (SalaryMin, SalaryMax, EmployeeRate, EmployerRate, MaxContribution, Notes) VALUES
(1000.00, 1500.00, 0.0100, 0.0200, 100.00, 'At least 1,000 to 1,500'),
(1500.01, NULL, 0.0200, 0.0200, 100.00, 'Over 1,500 - maximum contribution 100');

-- Withholding tax brackets (BIR graduated rates)
INSERT INTO WithholdingTaxBracket (MonthlyRateMin, MonthlyRateMax, TaxRuleDescription, BaseTax, ExcessRate, ExcessOver) VALUES
(0.00, 20832.00, '20,832 and below - No withholding tax', 0.00, NULL, NULL),
(20833.00, 33332.99, '20,833 to below 33,333 - 20% in excess of 20,833', 0.00, 0.2000, 20833.00),
(33333.00, 66666.99, '33,333 to below 66,667 - 2,500 plus 25% in excess of 33,333', 2500.00, 0.2500, 33333.00),
(66667.00, 166666.99, '66,667 to below 166,667 - 10,833 plus 30% in excess of 66,667', 10833.00, 0.3000, 66667.00),
(166667.00, 666666.99, '166,667 to below 666,667 - 40,833.33 plus 32% in excess over 166,667', 40833.33, 0.3200, 166667.00),
(666667.00, NULL, '666,667 and above - 200,833.33 plus 35% in excess of 666,667', 200833.33, 0.3500, 666667.00);

-- <<< END 05_seed_statutory.sql



-- >>> BEGIN reports/11_schema_semi_monthly_tax.sql

-- MotorPH Payroll System - M2: Semi-monthly withholding tax brackets
-- Converted from monthly MotorPH/BIR table (document/Witholding Tax.md)
-- Rules: Min/Max/Base/ExcessOver divided by 2; ExcessRate unchanged

USE payrollsystem_db;

DROP TABLE IF EXISTS WithholdingTaxBracketSemiMonthly;

CREATE TABLE WithholdingTaxBracketSemiMonthly (
    BracketID INT NOT NULL AUTO_INCREMENT,
    SemiMonthlyRateMin DECIMAL(12, 2) NOT NULL,
    SemiMonthlyRateMax DECIMAL(12, 2) NULL,
    TaxRuleDescription VARCHAR(255) NOT NULL,
    BaseTax DECIMAL(12, 2) NOT NULL DEFAULT 0,
    ExcessRate DECIMAL(5, 4) NULL,
    ExcessOver DECIMAL(12, 2) NULL,
    PRIMARY KEY (BracketID),
    CONSTRAINT chk_semi_withholding_base CHECK (BaseTax >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Monthly source -> semi-monthly (Option A payroll; tax brackets halved per M2 spec)
INSERT INTO WithholdingTaxBracketSemiMonthly
    (SemiMonthlyRateMin, SemiMonthlyRateMax, TaxRuleDescription, BaseTax, ExcessRate, ExcessOver)
VALUES
(0.00, 10416.00,
 '10,416 and below - No withholding tax (from monthly 20,832)',
 0.00, NULL, NULL),
(10417.00, 16666.00,
 '10,417 to 16,666 - 20% in excess of 10,417 (from monthly 20,833-33,332)',
 0.00, 0.2000, 10417.00),
(16667.00, 33333.00,
 '16,667 to 33,333 - 1,250 plus 25% in excess of 16,667 (from monthly 33,333-66,666)',
 1250.00, 0.2500, 16667.00),
(33334.00, 83333.00,
 '33,334 to 83,333 - 5,416.50 plus 30% in excess of 33,334 (from monthly 66,667-166,666)',
 5416.50, 0.3000, 33334.00),
(83334.00, 333333.00,
 '83,334 to 333,333 - 20,416.67 plus 32% in excess of 83,334 (from monthly 166,667-666,666)',
 20416.67, 0.3200, 83334.00),
(333334.00, NULL,
 '333,334 and above - 100,416.67 plus 35% in excess of 333,334 (from monthly 666,667+)',
 100416.67, 0.3500, 333334.00);

-- <<< END reports/11_schema_semi_monthly_tax.sql



-- >>> BEGIN reports/12_seed_payslip_pay_period.sql

-- MotorPH Payroll System - M2: Monthly coverage via two semi-monthly cutoffs
-- Seeds Payroll + Payslip for both June cutoffs (two payslips per employee).
-- Earnings and deductions are computed in vw_EmployeePayslipReport (Option A split).

USE payrollsystem_db;

-- Cutoff 1: first half of June
SET @period1_start = '2024-06-01';
SET @period1_end = '2024-06-15';
SET @issue1 = '2024-06-16';

-- Cutoff 2: second half of June
SET @period2_start = '2024-06-16';
SET @period2_end = '2024-06-30';
SET @issue2 = '2024-07-01';

-- Workbench safe-update mode (Error 1175) blocks DELETE with subqueries
SET @OLD_SQL_SAFE_UPDATES = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

-- Remove prior seed for both June cutoffs (safe for re-run)
DELETE FROM Deduction
WHERE DeductionID IN (
    SELECT DeductionID FROM (
        SELECT d.DeductionID
        FROM Deduction d
        INNER JOIN Payroll p ON d.PayrollID = p.PayrollID
        WHERE (
            (p.PayPeriodStart = @period1_start AND p.PayPeriodEnd = @period1_end)
            OR (p.PayPeriodStart = @period2_start AND p.PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_deductions
);

DELETE FROM Payslip
WHERE PayslipID IN (
    SELECT PayslipID FROM (
        SELECT ps.PayslipID
        FROM Payslip ps
        INNER JOIN Payroll p ON ps.PayrollID = p.PayrollID
        WHERE (
            (p.PayPeriodStart = @period1_start AND p.PayPeriodEnd = @period1_end)
            OR (p.PayPeriodStart = @period2_start AND p.PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_payslips
);

DELETE FROM Payroll
WHERE PayrollID IN (
    SELECT PayrollID FROM (
        SELECT PayrollID
        FROM Payroll
        WHERE (
            (PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end)
            OR (PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end)
        )
    ) AS to_delete_payrolls
);

SET SQL_SAFE_UPDATES = @OLD_SQL_SAFE_UPDATES;

-- Placeholder gross/net (report view computes actual amounts)
-- Cutoff 1
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period1_start,
    @period1_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue1
FROM Payroll
WHERE PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end;

-- Cutoff 2
INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay)
SELECT
    e.EmployeeID,
    @period2_start,
    @period2_end,
    ROUND(s.BaseSalary / 2, 2),
    ROUND(s.BaseSalary / 2 * 0.85, 2)
FROM Employee e
INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID;

INSERT INTO Payslip (PayrollID, IssueDate)
SELECT PayrollID, @issue2
FROM Payroll
WHERE PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end;

SELECT '=== June payroll seed (two cutoffs) ===' AS Section;

SELECT PayPeriodStart, PayPeriodEnd, COUNT(*) AS payroll_rows
FROM Payroll
WHERE (
    (PayPeriodStart = @period1_start AND PayPeriodEnd = @period1_end)
    OR (PayPeriodStart = @period2_start AND PayPeriodEnd = @period2_end)
)
GROUP BY PayPeriodStart, PayPeriodEnd
ORDER BY PayPeriodStart;

-- <<< END reports/12_seed_payslip_pay_period.sql



-- >>> BEGIN employee_payslip_report.sql (VIEW only)

USE payrollsystem_db;

DROP VIEW IF EXISTS vw_EmployeePayslipReport;

CREATE VIEW vw_EmployeePayslipReport AS
WITH employee_benefits AS (
    SELECT
        EmployeeID,
        SUM(CASE WHEN BenefitType = 'Rice Subsidy' THEN Amount ELSE 0 END) AS rice_monthly,
        SUM(CASE WHEN BenefitType = 'Phone Allowance' THEN Amount ELSE 0 END) AS phone_monthly,
        SUM(CASE WHEN BenefitType = 'Clothing Allowance' THEN Amount ELSE 0 END) AS clothing_monthly
    FROM Benefit
    GROUP BY EmployeeID
),
payslip_base AS (
    SELECT
        e.EmployeeID,
        CONCAT(e.LastName, ', ', e.FirstName) AS EmployeeName,
        e.FirstName,
        e.LastName,
        e.Position,
        d.DepartmentName,
        CONCAT(e.Position, ' / ', d.DepartmentName) AS PositionDepartment,
        ea.StreetName AS Address,
        g.SSSNumber,
        g.PhilHealthNumber,
        g.TINNumber,
        g.PagIBIGNumber,
        s.BaseSalary AS MonthlySalary,
        p.PayPeriodStart,
        p.PayPeriodEnd,
        ps.IssueDate,
        -- Official template: Daily Rate = Monthly Salary / 20 working days
        ROUND(s.BaseSalary / 20, 2) AS DailyRate,
        -- Semi-monthly cutoff assumes 10 worked days (half of 20)
        10 AS DaysWorked,
        0.00 AS Overtime,
        COALESCE(b.rice_monthly, 0) AS RiceSubsidy,
        COALESCE(b.phone_monthly, 0) AS PhoneAllowance,
        COALESCE(b.clothing_monthly, 0) AS ClothingAllowance
    FROM Employee e
    INNER JOIN Department d ON e.DepartmentID = d.DepartmentID
    INNER JOIN GovernmentID g ON e.EmployeeID = g.EmployeeID
    INNER JOIN Salary s ON e.EmployeeID = s.EmployeeID
    INNER JOIN Payroll p
        ON e.EmployeeID = p.EmployeeID
       AND (
            (p.PayPeriodStart = DATE('2024-06-01') AND p.PayPeriodEnd = DATE('2024-06-15'))
            OR (p.PayPeriodStart = DATE('2024-06-16') AND p.PayPeriodEnd = DATE('2024-06-30'))
       )
    INNER JOIN Payslip ps ON p.PayrollID = ps.PayrollID
    LEFT JOIN EmployeeAddress ea ON e.EmployeeID = ea.EmployeeID
    LEFT JOIN employee_benefits b ON e.EmployeeID = b.EmployeeID
),
earnings AS (
    SELECT
        pb.*,
        -- Template GROSS INCOME = Daily Rate × Days Worked + Overtime (benefits NOT included)
        ROUND(pb.DailyRate * pb.DaysWorked + pb.Overtime, 2) AS GrossIncome,
        ROUND(
            pb.RiceSubsidy + pb.PhoneAllowance + pb.ClothingAllowance,
            2
        ) AS BenefitsTotal
    FROM payslip_base pb
),
statutory AS (
    SELECT
        er.*,
        ROUND(
            COALESCE(
                (
                    SELECT sb.ContributionAmount
                    FROM SSSContributionBracket sb
                    WHERE er.MonthlySalary >= sb.RangeFrom
                      AND (sb.RangeTo IS NULL OR er.MonthlySalary <= sb.RangeTo)
                    ORDER BY sb.RangeFrom DESC
                    LIMIT 1
                ),
                0
            ) / 2,
            2
        ) AS SSSDeduction,
        ROUND(LEAST(er.MonthlySalary * 0.03, 1800.00) * 0.5 / 2, 2) AS PhilHealthDeduction,
        ROUND(
            LEAST(
                er.MonthlySalary * CASE
                    WHEN er.MonthlySalary >= 1000.00 AND er.MonthlySalary <= 1500.00 THEN 0.0100
                    ELSE 0.0200
                END,
                100.00
            ) / 2,
            2
        ) AS PagibigDeduction
    FROM earnings er
),
taxable AS (
    SELECT
        st.*,
        -- Taxable base: work gross + half of monthly benefits − statutory (keeps Option A tax)
        ROUND(
            st.GrossIncome
            + (st.BenefitsTotal / 2)
            - st.SSSDeduction
            - st.PhilHealthDeduction
            - st.PagibigDeduction,
            2
        ) AS TaxableIncome
    FROM statutory st
),
taxed AS (
    SELECT
        tx.*,
        ROUND(
            COALESCE(
                (
                    SELECT
                        t.BaseTax
                        + GREATEST(tx.TaxableIncome - COALESCE(t.ExcessOver, 0), 0)
                          * COALESCE(t.ExcessRate, 0)
                    FROM WithholdingTaxBracketSemiMonthly t
                    WHERE tx.TaxableIncome >= t.SemiMonthlyRateMin
                      AND (
                          t.SemiMonthlyRateMax IS NULL
                          OR tx.TaxableIncome <= t.SemiMonthlyRateMax
                      )
                    ORDER BY t.SemiMonthlyRateMin DESC
                    LIMIT 1
                ),
                0
            ),
            2
        ) AS WithholdingTax
    FROM taxable tx
)
SELECT
    -- Header (official template labels)
    CONCAT(
        EmployeeID, '-',
        DATE_FORMAT(PayPeriodEnd, '%Y-%m-%d')
    ) AS `Payslip No`,
    EmployeeID AS `Employee ID`,
    EmployeeName AS `Employee Name`,
    PayPeriodStart AS `Period Start Date`,
    PayPeriodEnd AS `Period End Date`,
    PositionDepartment AS `Employee Position/Department`,
    -- Earnings
    MonthlySalary AS `Monthly Salary`,
    DailyRate AS `Daily Rate`,
    DaysWorked AS `Days Worked`,
    Overtime AS `Overtime`,
    GrossIncome AS `Gross Income`,
    -- Benefits (full monthly amounts as on official template)
    RiceSubsidy AS `Rice Subsidy`,
    PhoneAllowance AS `Phone Allowance`,
    ClothingAllowance AS `Clothing Allowance`,
    BenefitsTotal AS `Benefits`,
    -- Deductions (Option A split)
    SSSNumber AS `Social Security No.`,
    SSSDeduction AS `Social Security System`,
    PhilHealthNumber AS `Philhealth No.`,
    PhilHealthDeduction AS `Philhealth`,
    PagIBIGNumber AS `Pag-ibig No.`,
    PagibigDeduction AS `Pag-Ibig`,
    TINNumber AS `TIN`,
    WithholdingTax AS `Withholding Tax`,
    ROUND(
        SSSDeduction + PhilHealthDeduction + PagibigDeduction + WithholdingTax,
        2
    ) AS `Total Deductions`,
    -- Summary
    GrossIncome AS `Summary Gross Income`,
    BenefitsTotal AS `Summary Benefits`,
    ROUND(
        SSSDeduction + PhilHealthDeduction + PagibigDeduction + WithholdingTax,
        2
    ) AS `Summary Deductions`,
    -- TAKE HOME PAY = Gross Income + Benefits − Deductions (official template)
    ROUND(
        GrossIncome
        + BenefitsTotal
        - SSSDeduction
        - PhilHealthDeduction
        - PagibigDeduction
        - WithholdingTax,
        2
    ) AS `Take Home Pay`,
    -- Supporting fields used by summary/procedures
    Position,
    DepartmentName,
    Address,
    IssueDate,
    'Semi-Monthly' AS PayFrequency,
    TaxableIncome
FROM taxed;

-- <<< END employee_payslip_report.sql



-- >>> BEGIN employees_payroll_summary_report.sql (VIEWS only)

USE payrollsystem_db;



DROP VIEW IF EXISTS vw_EmployeePayrollOverallTotals;
DROP VIEW IF EXISTS vw_EmployeePayrollSummaryByDepartment;
DROP VIEW IF EXISTS vw_EmployeePayrollSummaryReport;

CREATE VIEW vw_EmployeePayrollSummaryReport AS
SELECT
    `Employee ID` AS `Employee No`,
    MAX(`Employee Name`) AS `Employee Full Name`,
    MAX(Position) AS `Position`,
    MAX(DepartmentName) AS `Department`,
    DATE_FORMAT(MIN(`Period Start Date`), '%M %Y') AS `Pay Period (Month)`,
    ROUND(SUM(`Gross Income`), 2) AS `Gross Income`,
    MAX(`Social Security No.`) AS `Social Security No.`,
    ROUND(SUM(`Social Security System`), 2) AS `Social Security Contribution`,
    MAX(`Philhealth No.`) AS `Philhealth No.`,
    ROUND(SUM(`Philhealth`), 2) AS `Philhealth Contribution`,
    MAX(`Pag-ibig No.`) AS `Pag-ibig No.`,
    ROUND(SUM(`Pag-Ibig`), 2) AS `Pag-Ibig Contribution`,
    MAX(`TIN`) AS `TIN`,
    ROUND(SUM(`Withholding Tax`), 2) AS `Withholding Tax`,
    ROUND(SUM(`Take Home Pay`), 2) AS `Net Pay`
FROM vw_EmployeePayslipReport
GROUP BY `Employee ID`;

CREATE VIEW vw_EmployeePayrollSummaryByDepartment AS
SELECT
    `Pay Period (Month)`,
    `Department`,
    COUNT(*) AS `Total Employees`,
    ROUND(SUM(`Gross Income`), 2) AS `Total Gross Pay`,
    ROUND(SUM(`Net Pay`), 2) AS `Total Net Pay`
FROM vw_EmployeePayrollSummaryReport
GROUP BY `Pay Period (Month)`, `Department`;

CREATE VIEW vw_EmployeePayrollOverallTotals AS
SELECT
    `Pay Period (Month)`,
    COUNT(*) AS `Total Employees`,
    ROUND(SUM(`Gross Income`), 2) AS `Overall Gross Pay`,
    ROUND(SUM(`Net Pay`), 2) AS `Overall Net Pay`
FROM vw_EmployeePayrollSummaryReport
GROUP BY `Pay Period (Month)`;

-- <<< END employees_payroll_summary_report.sql



-- >>> BEGIN 14_m2_report_procedures.sql (PROCEDURES only)

USE payrollsystem_db;

DROP PROCEDURE IF EXISTS sp_GetEmployeePayslip;

DELIMITER //

CREATE PROCEDURE sp_GetEmployeePayslip(IN p_EmployeeID INT)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Employee WHERE EmployeeID = p_EmployeeID
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid EmployeeID: employee not found in Employee table';
    END IF;

    SELECT
        `Payslip No`,
        `Employee ID`,
        `Employee Name`,
        `Period Start Date`,
        `Period End Date`,
        `Employee Position/Department`,
        `Monthly Salary`,
        `Daily Rate`,
        `Days Worked`,
        `Overtime`,
        `Gross Income`,
        `Rice Subsidy`,
        `Phone Allowance`,
        `Clothing Allowance`,
        `Benefits`,
        `Social Security System`,
        `Philhealth`,
        `Pag-Ibig`,
        `Withholding Tax`,
        `Total Deductions`,
        `Summary Gross Income`,
        `Summary Benefits`,
        `Summary Deductions`,
        `Take Home Pay`
    FROM vw_EmployeePayslipReport
    WHERE `Employee ID` = p_EmployeeID
    ORDER BY `Period Start Date`;
END //

DROP PROCEDURE IF EXISTS sp_GetEmployeePayslipByPeriod //

CREATE PROCEDURE sp_GetEmployeePayslipByPeriod(
    IN p_EmployeeID INT,
    IN p_Start DATE,
    IN p_End DATE
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Employee WHERE EmployeeID = p_EmployeeID
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid EmployeeID: employee not found in Employee table';
    END IF;

    SELECT
        `Payslip No`,
        `Employee ID`,
        `Employee Name`,
        `Period Start Date`,
        `Period End Date`,
        `Employee Position/Department`,
        `Monthly Salary`,
        `Daily Rate`,
        `Days Worked`,
        `Overtime`,
        `Gross Income`,
        `Rice Subsidy`,
        `Phone Allowance`,
        `Clothing Allowance`,
        `Benefits`,
        `Social Security System`,
        `Philhealth`,
        `Pag-Ibig`,
        `Withholding Tax`,
        `Total Deductions`,
        `Summary Gross Income`,
        `Summary Benefits`,
        `Summary Deductions`,
        `Take Home Pay`
    FROM vw_EmployeePayslipReport
    WHERE `Employee ID` = p_EmployeeID
      AND `Period Start Date` = p_Start
      AND `Period End Date` = p_End;
END //

DROP PROCEDURE IF EXISTS sp_GetEmployeePayrollSummary //

CREATE PROCEDURE sp_GetEmployeePayrollSummary()
BEGIN
    SELECT
        `Employee No`,
        `Employee Full Name`,
        `Position`,
        `Department`,
        `Pay Period (Month)`,
        `Gross Income`,
        `Social Security No.`,
        `Social Security Contribution`,
        `Philhealth No.`,
        `Philhealth Contribution`,
        `Pag-ibig No.`,
        `Pag-Ibig Contribution`,
        `TIN`,
        `Withholding Tax`,
        `Net Pay`
    FROM vw_EmployeePayrollSummaryReport
    ORDER BY `Employee No`;
END //

DROP PROCEDURE IF EXISTS sp_GetPayrollSummaryByDepartment //

CREATE PROCEDURE sp_GetPayrollSummaryByDepartment()
BEGIN
    SELECT
        `Pay Period (Month)`,
        `Department`,
        `Total Employees`,
        `Total Gross Pay`,
        `Total Net Pay`
    FROM vw_EmployeePayrollSummaryByDepartment
    ORDER BY `Department`;
END //

DROP PROCEDURE IF EXISTS sp_GetPayrollGrandTotals //

CREATE PROCEDURE sp_GetPayrollGrandTotals()
BEGIN
    SELECT
        `Pay Period (Month)`,
        `Total Employees`,
        `Overall Gross Pay`,
        `Overall Net Pay`
    FROM vw_EmployeePayrollOverallTotals;
END //

DELIMITER ;

-- <<< END 14_m2_report_procedures.sql



SELECT 'payrollsystem_db_final.sql deploy complete' AS Status;

SELECT COUNT(*) AS EmployeeCount FROM Employee;

SELECT COUNT(*) AS PayslipViewRows FROM vw_EmployeePayslipReport;

SELECT COUNT(*) AS SummaryViewRows FROM vw_EmployeePayrollSummaryReport;
