-- MotorPH Payroll System - Employee AUTO_INCREMENT identity guard
-- =============================================================================
-- PURPOSE: Block explicit EmployeeID inserts unless:
--            SET @ALLOW_EXPLICIT_EMPLOYEE_ID = 1;
-- (Matches sample: cannot insert explicit value for identity column ...)
--
-- HOW TO RUN (important):
--   Open this file in a MySQL Workbench SQL Editor tab and click Execute.
--   Do NOT use File > Run SQL Script for this file if DELIMITER fails (Error 1064).
--
-- Prerequisites: payrollsystem_db exists; Employee table exists (run 02_schema.sql first).
-- =============================================================================

USE payrollsystem_db;

DROP TRIGGER IF EXISTS trg_employee_block_explicit_id;

DELIMITER //
CREATE TRIGGER trg_employee_block_explicit_id
BEFORE INSERT ON Employee
FOR EACH ROW
BEGIN
    IF (IFNULL(@ALLOW_EXPLICIT_EMPLOYEE_ID, 0) <> 1)
       AND (NEW.EmployeeID IS NOT NULL)
       AND (NEW.EmployeeID <> 0) THEN
        SIGNAL SQLSTATE '45000'
            SET MYSQL_ERRNO = 544,
                MESSAGE_TEXT = 'Cannot insert explicit value for identity column in table Employee when IDENTITY_INSERT is set to OFF.';
    END IF;
END//
DELIMITER ;
