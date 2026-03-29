MotorPH Payroll System Documentation

Prepared and Presented by:

***<Insert Learner's Name Here>***

***<Insert Learner Program and Specialization)***

---

**Intellectual Property Notice**

This template is an exclusive property of **Mapua-Malayan Digital College** and is protected under **Republic Act No. 8293**, also known as the *Intellectual Property Code of the Philippines* (IP Code). It is provided solely for educational purposes within this course. Students may use this template to complete their tasks but may not **modify, distribute, sell, upload,** or **claim ownership** of the template itself. Such actions constitute copyright infringement under **Sections 172, 177, and 216** of the IP Code and may result in legal consequences. Unauthorized use beyond this course may result in legal or academic consequences.

Additionally, students must comply with the **Mapua-Malayan Digital College Student Handbook**, particularly with the following provisions:

- **Offenses Related to MMDC IT**:
  - **Section 6.2** - Unauthorized copying of files
  - **Section 6.8** - Extraction of protected, copyrighted, and/or confidential information by electronic means using MMDC IT infrastructure
- **Offenses Related to MMDC Admin, IT, and Operations**:
  - **Section 4.5** - Unauthorized collection or extraction of money, checks, or other instruments of monetary equivalent in connection with matters pertaining to MMDC

Violations of these policies may result in **disciplinary actions ranging from suspension to dismissal**, in accordance with the Student Handbook.

For permissions or inquiries, please contact MMDC-ISD at [isd@mmdc.mcl.edu.ph](mailto:isd@mmdc.mcl.edu.ph).

# TABLE OF CONTENTS

**[1 Introduction & System Overview 4](#_5v5pz5r5mgr2)**

**[2 Purpose of This Document 4](#_uudvgasg70ib)**

**[3 Final OOP Design Summary 4](#_cht890wphpwu)**

[3.1. Class Overview 4](#_1j2wxg7gpyt)

[3.2. GUI → Backend Workflow Summary 4](#_5uyfutifhkb)

[3.3. Key Improvements After Refactoring 5](#_chtilg907g87)

**[4 Testing & Refinement Summary 5](#_cn9l0xcay15c)**

[4.1. Internal Testing (Week 10) 5](#_ld6egn1g10bp)

[4.2. External QA Feedback (Weeks 11-12) 5](#_muzd3t7nb42a)

[4.3. Remaining Known Issues 5](#_1dpnrcfb1wp9)

[4.4. Final Improvements Made 6](#_9d80q5quk7lb)

**[5 Final Output Links & Submission Details 6](#_nqx37lsz4nb4)**

[5.1. Final GitHub Repository Link 6](#_v223if469dbj)

[5.2. Additional Files (If Any) 6](#_27kq2l83do9x)

**[6 Appendix A. Team Contributions Table 6](#_xfj55sd4pjwc)**

# Introduction & System Overview

This final documentation describes **GEAR.HR**, the completed object-oriented implementation of a MotorPH-style **employee, attendance, leave, and payroll** system. The document summarizes how the application is structured in Java, how the graphical interface ties to services and CSV-backed persistence, how internal smoke testing and external QA were run, and what refinements were made before submission.

GEAR.HR is a **Java Swing** desktop application. Users sign in against `user_credentials.csv`; the app maps each role string to a **RoleGroup** (HR, Payroll, IT/Admin, or Normal employee) and shows a **role-based sidebar** in `Main`. Modules cover employee directory and profile (including salary computation and payroll editing where permitted), attendance recording or view-only access, leave requests and approvals, payroll processing using **PayrollProcessor** and **PayrollUtils**, and IT-admin features such as **User Credential Management** and IT tickets. Data is loaded and saved under the project’s `csv/` folder so the system remains suitable for coursework demonstration without a database server.

# Purpose of This Document

This document serves as the **capstone record** for the whole GEAR.HR codebase and delivery. It compiles the **final OOP design** (layers, main classes, and how the GUI reaches backend logic). It records **internal testing** from Week 10 using the smoke test checklist (launch, login, RBAC, payroll computation, attendance, leave, CSV resilience, and logout). It summarizes **external QA** from Weeks 11–12, including tester observations and enhancement ideas. It lists **remaining known issues** from the final known-issues tracker and the **fixes and improvements** applied after testing. Finally, it points to the **final GitHub repository** (placeholder below) that holds the submitted source code and README.

# Final OOP Design Summary

## Class Overview

The **model** layer uses `Identifiable`, `Validatable`, and `AbstractEntity` so domain types share a common contract; concrete entities include **Employee**, **AttendanceRecord**, **LeaveRequest**, **PayrollData**, **PayrollResult**, **UserCredential**, and **ItTicket**. **Repositories** implement interfaces such as `IEmployeeRepository`, `IAttendanceRepository`, `ILeaveRequestRepository`, `IPayrollRepository`, `IUserCredentialRepository`, and `IItTicketRepository`; list-style CSV stores extend **AbstractCsvListRepository** (shared parsing and save behavior), while **PayrollRepository** handles map-based payroll CSV rows. **Services** encapsulate use cases: **AuthenticationService**, **EmployeeService**, **AttendanceService**, **LeaveService**, **PayrollProcessor** (with **PayrollReport** and **PayrollUtils** for formulas and formatted output), **UserCredentialService**, and **ItTicketService**. **ApplicationContext** is the composition root that constructs repositories and services and exposes them to the UI. The **ui** package provides **SplashScreen**, **User** (login), **Main** (dashboard), module screens (**EmployeeProfile**, **AttendanceScreen**, **LeaveManagementScreen**, **UserCredentialManagementScreen**) that implement **ModuleScreen**, with **BaseModuleScreen** supplying shared frame layout, plus helpers like **InputFilters** and **TableColumnSortUtil**. Utilities **EmployeeValidationUtil** and **CredentialValidationUtil** centralize field validation.

## GUI → Backend Workflow Summary

On startup, the app builds an **ApplicationContext** and passes it into **User.showLoginScreen** and, after successful authentication via **IAuthenticationService**, into **Main.showMainScreen**. **Main** resolves **RoleGroup.fromRole** and opens each module by calling **ModuleScreen.show** on the chosen screen, passing the same context. Screens call service methods on that context (for example, **EmployeeProfile** uses **PayrollProcessor** and **EmployeeService**; **AttendanceScreen** uses **AttendanceService**; **LeaveManagementScreen** uses **LeaveService**), and services delegate persistence to repository implementations. The UI does not read CSV files directly for business operations; repositories and services own file I/O and domain rules.

## Key Improvements After Refactoring

- **Layered architecture**: Clear separation among model, repository, service, and UI packages, with **ApplicationContext** as a single place to wire dependencies.
- **Abstractions and polymorphism**: Service and repository **interfaces** allow callers (including tests or future swaps) to depend on contracts rather than concrete CSV classes.
- **Reusable CSV infrastructure**: **AbstractCsvListRepository** factors out common load/save and line-splitting behavior; **PayrollRepository** specializes map-based payroll storage.
- **Payroll coherence**: **PayrollProcessor** orchestrates payroll data and computation; **PayrollUtils** concentrates statutory calculations; **PayrollReport** formats output for display and text receipts.
- **RBAC consistency**: **RoleGroup** drives sidebar entries and screen-level permissions (for example, Payroll view-only attendance and leave, HR/IT payroll edit rights).
- **Extended admin capabilities**: **UserCredentialService** / **UserCredentialManagementScreen** and **ItTicket** / **ItTicketService** support credential and ticket workflows aligned with IT Admin use cases.

# Testing & Refinement Summary

## Internal Testing (Week 10)

- **Launch and auth**: GUI opens without errors; splash displays and transitions to login; valid credentials open the main dashboard and invalid credentials show an error without opening the dashboard.
- **RBAC and navigation**: Sidebar menu items match role group (HR, Payroll, IT/Admin, Normal); Payroll attendance is view-only without the record form, consistent with **RoleGroup** behavior.
- **Payroll and data paths**: Compute Salary runs **PayrollProcessor**, updates the salary tab with breakdown and totals, and receipt download works; missing **payroll_records.csv** does not crash the app (empty payroll handled).
- **Attendance and leave**: Attendance can be saved and appears after refresh; leave submits with overlap checks; invalid date/time formats are rejected with clear messages.
- **Stability**: Employee object instantiation after login succeeds; logout disposes the main window and returns to login; smoke notes confirm re-verification after fixes (for example, payroll display and missing CSV handling).

## External QA Feedback (Weeks 11-12)

- Across the **External QA** checklist, testers marked **Issue?** as **No** for listed scenarios: launch (with and without logo assets), login success and failure messaging, dashboard and logout confirmation, RBAC and sidebar access per role, employee CRUD and validation, payroll view/edit rules, attendance and leave flows, and CSV persistence.
- Testers suggested **UX enhancements** (optional): loading indicator if startup is slow, password show/hide, “Forgot password” flow, and ensuring logout confirmation is highly visible.
- **IT/Admin** feedback included considering an **audit log** for sensitive actions (payroll and employee data changes).
- **Validation and messaging**: suggestions included more specific duplicate-field errors, disabling Save until required fields are complete, friendlier numeric payroll errors, and immediate feedback when leave end date is chosen.
- **Attendance**: notes mentioned preferences such as stronger “clear all” wording, optional auto time-in/out, and stricter or different date-picker behavior; the **implemented** system uses a default date of today in **yyyy-MM-dd** with validation on save.
- **Employee number**: the live application validates **five-digit** employee numbers (MotorPH-style IDs such as 10001); any checklist text implying four digits should be read as superseded by the codebase.

## Remaining Known Issues

- The **Known Issues List** (final version) contains **no open items**: every tracked issue is marked **Resolved**, with verification notes recorded for each.

## Final Improvements Made

- **Salary tab display**: Refreshed the payroll result panel after **PayrollReport.format()** so compute results appear immediately in the GUI.
- **CSV resilience**: Repositories and authentication handle missing or unreadable CSV files with guards and exceptions, avoiding crashes and allowing empty modules where appropriate.
- **Attendance integrity**: Duplicate employee+date records are blocked with a clear message; worked hours use validated time in/out and display correctly in the table.
- **Leave rules**: **LeaveRequest.overlapsWith** and **LeaveService** reject overlapping ranges, including same-day edge cases.
- **Payroll–attendance linkage**: **PayrollProcessor.processPayroll** pulls monthly worked hours via **AttendanceService** before gross/net calculations.
- **Payroll keys and new employees**: Payroll map keys use **employee number** consistently; new employees trigger payroll row creation/update in the add flow so payroll data exists for new IDs.
- **Role visibility**: Payroll users no longer see attendance record/clear controls meant for HR/IT; **RoleGroup** and **AttendanceScreen** stay aligned.
- **Receipt and validation**: Payroll receipt write failures (for example read-only paths) surface in a dialog; **EmployeeValidationUtil** aligns add/update validation (including five-digit employee number and government ID formats).

# Final Output Links & Submission Details

## Final GitHub Repository Link

**[Insert final repository URL]** — This repository must contain the final GEAR.HR source code, updates through submission, and the project **README** (including how to run from the project root and how CSV data files are used).

## Additional Files (If Any)

# Appendix A. Team Contributions Table

Each team member completes one row in the table below, including self-rating and peer ratings, using the scale provided.

**Rating Scale (same as earlier milestones):**


| **Rating** | **Meaning**                                 |
| ---------- | ------------------------------------------- |
| 3          | Strong contribution / exceeded expectations |
| 2          | Expected contribution / satisfactory        |
| 1          | Minimal contribution / incomplete           |
| 0          | No contribution / did not participate       |


**Team Contributions & Ratings Table**


| **Team Member Name** | **Summary of Contributions** | **Self-Rating** | **Member A Rating** | **Member B Rating** | **Member C Rating** |
| -------------------- | ---------------------------- | --------------- | ------------------- | ------------------- | ------------------- |
| Colin Bactong | Core architecture and payroll stack: **Identifiable** / **Validatable** / **AbstractEntity**; **PayrollUtils**; **AbstractCsvListRepository** with **EmployeeRepository** and **AttendanceRepository**; **AttendanceService**; **PayrollProcessor** and **PayrollReport**; **ModuleScreen** / **BaseModuleScreen** and **Main** refactor (with Charlize); **EmployeeProfile** refactor (with Charlize); **AuthContext** and **AuthenticationService** alignment; IT tickets and **UserCredential** administration end-to-end; payroll–attendance month key and locale fixes; shared tasks on UI hardening and tests (per Refactoring Plan SECTION D). | 3 | 3 | 3 | 3 |
| Charlize Bactong | Domain and auth: **Employee** model; **AuthenticationService**; **LeaveRequest** / **LeaveService** / **LeaveManagementScreen** (optional scope); **Main** and **EmployeeProfile** refactor with Colin; **InputFilters** with Angelica; ensured CSV files and headers exist at startup across repositories; shared correctness, validation alignment, and testing tasks in late weeks (per Refactoring Plan SECTION D). | 3 | 3 | 3 | 3 |
| Angelica Mae Calipayan | Models and services: **AttendanceRecord**; **PayrollData** / **PayrollResult**; **EmployeeService** with delete coordination; **User** / **Main** wiring with Yzabelle; **AttendanceScreen** and **RoleGroup** / sidebar with Yzabelle; **InputFilters** with Charlize; **LeaveManagementScreen** integration with Yzabelle; shared late-week fixes and tests (per Refactoring Plan SECTION D). | 3 | 3 | 3 | 3 |
| Yzabelle Grace Crane | Infrastructure and UI polish: **PayrollRepository**; **ApplicationContext** composition root; **User** / **Main** and **AttendanceScreen** / **RoleGroup** work with Angelica; **LeaveManagementScreen** with Angelica; **TableColumnSortUtil** on **EmployeeProfile** tables; shared late-week correctness and testing tasks (per Refactoring Plan SECTION D). | 3 | 3 | 3 | 3 |


