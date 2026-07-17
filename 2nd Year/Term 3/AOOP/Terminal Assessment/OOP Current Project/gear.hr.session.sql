-- GEAR.HR MySQL schema (CSV -> JDBC migration seed)
--
-- This file creates tables only (no INSERTs). After running it, tables will be empty.
-- To load seed data from GEAR.HR/csv/*.csv, run tools.SeedMySqlFromCsv from the GEAR.HR/ folder
-- (see README.md "Seed MySQL from existing CSV files").
--
-- Notes:
-- - Uses CSV/business IDs as primary keys.
-- - Intentionally omits foreign keys for import robustness (CSV seed may contain IDs
--   that don't exist in the employees.csv example).
-- - Attendance time fields are stored as VARCHAR to preserve the existing domain parsing
--   format (e.g., "8:59" and "17:00").

CREATE TABLE IF NOT EXISTS employees (
  employee_number VARCHAR(5) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  sss_number VARCHAR(32) NOT NULL,
  phil_health_number VARCHAR(32) NOT NULL,
  tin VARCHAR(32) NOT NULL,
  pag_ibig_number VARCHAR(32) NOT NULL,
  email VARCHAR(255) NOT NULL,
  position VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL,
  address TEXT NOT NULL,
  phone VARCHAR(32) NOT NULL,
  PRIMARY KEY (employee_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS attendance_records (
  employee_id VARCHAR(5) NOT NULL,
  record_date DATE NOT NULL,
  status VARCHAR(32) NOT NULL,
  time_in VARCHAR(8) NOT NULL,
  time_out VARCHAR(8) NOT NULL,
  PRIMARY KEY (employee_id, record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS leave_requests (
  employee_id VARCHAR(5) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  reason VARCHAR(1024) NOT NULL,
  status VARCHAR(32) NOT NULL,
  PRIMARY KEY (employee_id, start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS payroll_settings (
  employee_id VARCHAR(5) NOT NULL,
  base_salary DOUBLE NOT NULL,
  hourly_rate DOUBLE NOT NULL,
  sss_amount DOUBLE NOT NULL,
  phil_health_amount DOUBLE NOT NULL,
  pag_ibig_amount DOUBLE NOT NULL,
  withholding_tax DOUBLE NOT NULL,
  rice_subsidy DOUBLE NOT NULL,
  phone_allowance DOUBLE NOT NULL,
  clothing_allowance DOUBLE NOT NULL,
  PRIMARY KEY (employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_credentials (
  user_id VARCHAR(64) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS it_tickets (
  ticket_id VARCHAR(32) NOT NULL,
  user_id_requestor VARCHAR(64) NOT NULL,
  type_of_request VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL,
  PRIMARY KEY (ticket_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

