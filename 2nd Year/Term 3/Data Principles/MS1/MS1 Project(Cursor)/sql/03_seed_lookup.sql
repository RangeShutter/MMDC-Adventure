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
