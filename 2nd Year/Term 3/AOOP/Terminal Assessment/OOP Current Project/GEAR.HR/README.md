# GEAR.HR

**Important:** In this repository, **`GEAR.HR` is the project root folder**. Open/run the project from the `GEAR.HR/` directory (not from a parent folder) so relative paths like `database.properties`, `sql/...`, and `Logo/...` resolve correctly.

GEAR.HR is a desktop HR system built in **Java Swing** for managing **employees**, **attendance**, **leave requests**, and **payroll**.  
Storage is a **MySQL database (`gear.hr`) accessed via JDBC** (the only persistence layer). Sample data and default accounts ship as SQL scripts under `sql/`. Navigation and permissions adapt to the user’s role (`RoleGroup`).

## How to run

- Main entry point: [`src/ui/Main.java`](src/ui/Main.java)  
- Run `Main`: splash screen → login.

### Database setup (MySQL + JDBC)

1. Install **MySQL Server 8.x** and create the schema by running [`sql/schema.sql`](sql/schema.sql) (creates database `gear.hr` and its six tables).
2. Load the sample data and default accounts by running [`sql/seed.sql`](sql/seed.sql) (employees 10001–10038, matching login accounts, attendance, leave, payroll settings, and a couple of IT tickets).
3. Configure the connection in [`database.properties`](database.properties) at the project root (`db.url`, `db.user`, `db.password`).
4. The **MySQL Connector/J** driver jar lives in `lib/` and is registered in [`.classpath`](.classpath). When compiling manually, add it to the classpath:

```
javac -cp "src;lib/mysql-connector-j-9.7.0.jar" -d out src/**/*.java
java  -cp "out;lib/mysql-connector-j-9.7.0.jar" ui.Main
```

### Reporting (JasperReports)

Department payroll summaries can be exported to **PDF via JasperReports**. The report template is [`src/reports/payroll_summary.jrxml`](src/reports/payroll_summary.jrxml), filled by [`src/service/JasperPayrollReportService.java`](src/service/JasperPayrollReportService.java). The required JasperReports jars (`jasperreports-6.17.0`, `itext-2.1.7.js2`, `commons-*`, `ecj`) live in `lib/` and are registered in [`.classpath`](.classpath), so no build tool is needed.

**How to verify JasperReports export:**

1. Run `sql/schema.sql` and `sql/seed.sql`, then start the app and log in as an HR, Payroll, or IT/Admin user (see default accounts below).
2. Open **Employee Profile** → **Payroll Summary** tab.
3. Select a **department** (HR, Payroll, IT/Admin, or Normal Employee) and a **month**, then click **Generate Summary**.
4. Click **Export PDF (JasperReports)** and save the file. The PDF lists only employees in the selected department—not all employees.

### Default test accounts

After running `sql/seed.sql` you can log in with these accounts (one per `RoleGroup`):

| Role group | userId | password |
|------------|--------|----------|
| HR | `10006` | `HRManager#99` |
| Payroll | `10011` | `Payroll2024!` |
| IT / Admin | `10005` | `IT_System2024` |
| Normal employee | `10032` | `SalesMoto#2024` |

### Sample data and payroll figures

- Payroll rows for MotorPH-style employees **10001–10034** align with base salary and allowances; **SSS, PhilHealth, Pag-IBIG, and withholding tax** follow [`src/util/PayrollUtils.java`](src/util/PayrollUtils.java).
- Employees **10035–10038** also have payroll rows in `sql/seed.sql`. Editing payroll in the app updates the `payroll_settings` table via JDBC.

## Role groups

Roles come from the `user_credentials` table and are mapped in [`src/service/RoleGroup.java`](src/service/RoleGroup.java).

### Role strings per group

- **HR:** `HR Manager`, `HR Team Leader`, `HR Rank and File`
- **Payroll:** `Payroll Manager`, `Payroll Team Leader`, `Payroll Rank and File`, `Accounting Head`, `Account Manager`, `Account Team Leader`, `Account Rank and File`
- **IT/Admin:** `IT`, `IT Operations and Systems`
- **Normal employee:** any other role string

### Sidebar / modules

**Normal employee**

- My Attendance  
- **My Profile & Payroll** (opens profile + salary tabs and **Personal Payroll** read-only table)  
- My Leave  

**HR** (Personal Account collapsible + Directives)

- Personal: My Attendance, My Profile & Payroll, My Leave  
- Directives: Attendance Management, Employee Profile, Leave Management  

**Payroll**

- Personal: same as above  
- Directives: Payroll Management, View Attendance, View Leave Requests  

**IT/Admin**

- Personal: same as above  
- Directives: Attendance Management, Employee Profile & Payroll Management, Leave Management  

## Feature notes

### Status enums

Fixed-state values use type-safe Java enums in `src/model/` instead of loose string constants:

- `EmploymentStatus` — `regular`, `probationary`
- `LeaveStatus` — `Pending`, `Approved`, `Rejected`
- `AttendanceStatus` — `Present`, `Absent`, `Late`, `On Leave`, `Half Day`
- `TicketStatus` — `Pending`, `Resolved`

Models parse database strings via `fromString()` at the persistence boundary, reducing invalid or misspelled status values.

### Employee Profile / Payroll Management (`EmployeeProfile`)

- **HR, Payroll, IT/Admin:** three tabs — **Employee Directory**, **Employee Payroll Data** (table from the employees + payroll_settings tables), and **Payroll Summary**.
- **Column sorting:** click a header to sort; one column at a time; numbers vs text handled by [`src/util/TableColumnSortUtil.java`](src/util/TableColumnSortUtil.java).
- **Edit payroll (Employee Payroll Data tab + employee detail):**
  - **IT/Admin** and **Payroll:** view and edit  
  - **HR:** view only (no Edit Payroll on payroll tab; read-only employee detail for payroll footer)
- **Payroll Summary tab (HR, Payroll, IT/Admin):** pick a **department** (HR, Payroll, IT/Admin, Normal Employee) and a month, **Generate Summary** to build a department-specific payroll report (computed live from the database via [`src/service/PayrollReport.java`](src/service/PayrollReport.java) `formatSummary`), then **Save to .txt** (`PayrollSummary_<Department>_<Month>.txt`) or **Export PDF (JasperReports)**.
- **Normal employee:** outer tabs **My Profile** (personal info + salary computation) and **Personal Payroll** (read-only payroll row).

### Attendance

- **HR** and **IT/Admin:** **Delete** removes the **selected** attendance row (after confirm); **Clear** still clears all records (HR/IT only).  
- **Payroll** and normal employees: no delete/clear all (as implemented in [`src/ui/AttendanceScreen.java`](src/ui/AttendanceScreen.java)).

## Testing different roles

1. Update the `role` column in the `user_credentials` table (e.g. via `sql/seed.sql` or directly in MySQL) — set it to an exact string from the lists above.  
2. Restart the app or log out/in with that userId.

Login is loaded by [`src/service/AuthenticationService.java`](src/service/AuthenticationService.java) through the JDBC `user_credentials` repository.

## Unit tests (JUnit)

A JUnit 4 suite lives under [`test/`](test) and runs **without a database** (in-memory fakes where needed).

**Test classes:**

| Class | Coverage |
|-------|----------|
| `PayrollUtilsTest` | SSS, PhilHealth, Pag-IBIG, withholding tax |
| `PayrollProcessorTest` | Payroll gross/net computation |
| `PayrollReportTest` | Department summary formatting and totals |
| `RoleGroupTest` | Role-to-`RoleGroup` mapping |
| `StatusEnumTest` | `LeaveStatus`, `TicketStatus`, `AttendanceStatus`, `EmploymentStatus` parsing |

**Compile and run** (from the `GEAR.HR/` project root, after compiling `src/` to `bin/`):

```
javac -encoding UTF-8 -cp "lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar;bin" -d bin test\*.java
java -cp "bin;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" org.junit.runner.JUnitCore PayrollUtilsTest PayrollProcessorTest PayrollReportTest RoleGroupTest StatusEnumTest
```

All 20 tests should report `OK`.
