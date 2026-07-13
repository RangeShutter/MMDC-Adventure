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
