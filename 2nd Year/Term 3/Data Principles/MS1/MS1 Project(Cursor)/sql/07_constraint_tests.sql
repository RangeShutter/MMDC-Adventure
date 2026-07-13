-- MotorPH Payroll System - Step 7: Constraint validation (expected failures)
-- Run ONE test block at a time in MySQL Workbench. Each failing statement should error.

USE payrollsystem_db;

-- Test 1: NOT NULL on Employee.FirstName (expect ERROR 1048)
-- INSERT INTO Employee (EmployeeID, FirstName, LastName, DateOfBirth, ContactNumber, Position, DepartmentID, StatusID)
-- VALUES (99999, NULL, 'Test', '2000-01-01', '000-000-000', 'Tester', 1, 1);

-- Test 2: UNIQUE username (expect ERROR 1062 on second INSERT)
-- INSERT INTO UserAccount (EmployeeID, Username, PasswordHash) VALUES
-- (10001, 'test.duplicate.user', '$2y$10$abcdefghijklmnopqrstuv');
-- INSERT INTO UserAccount (EmployeeID, Username, PasswordHash) VALUES
-- (10002, 'test.duplicate.user', '$2y$10$abcdefghijklmnopqrstuv');

-- Test 3: CHECK TimeOut >= TimeIn (expect ERROR 3819)
-- INSERT INTO Attendance (EmployeeID, Date, TimeIn, TimeOut) VALUES
-- (10001, '2024-06-01', '2024-06-01 17:00:00', '2024-06-01 08:00:00');

-- Test 4: UNIQUE payroll period (expect ERROR 1062 on second INSERT)
-- INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay) VALUES
-- (10001, '2024-06-01', '2024-06-15', 45000.00, 40000.00);
-- INSERT INTO Payroll (EmployeeID, PayPeriodStart, PayPeriodEnd, GrossPay, NetPay) VALUES
-- (10001, '2024-06-01', '2024-06-15', 45000.00, 39000.00);

-- Test 5: FK invalid EmployeeID on Salary (expect ERROR 1452)
-- INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo) VALUES
-- (99999, 30000.00, 'Monthly', '2024-01-01', NULL);
