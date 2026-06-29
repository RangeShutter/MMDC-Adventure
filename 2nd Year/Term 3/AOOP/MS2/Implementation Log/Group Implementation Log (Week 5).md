# MO-IT113 - Advanced Object-Oriented Programming

## Group Implementation Progress Log - Week 5

Instructions:

- Starting Week 5, your group will maintain one shared Implementation Progress Log throughout Milestone 2.
- Each group member must update their own section, so everyone has visibility into what others have completed, challenges encountered, and where approaches may differ.
- After individual updates, your group will complete a Group Convergence Summary.

## Individual Progress Updates

### Colin Bactong - Team Leader


| SECTION                       | GUIDING PROMPTS                                                      | ANSWER                                                                                                                                                                                                                                                                                                                                 |
| ----------------------------- | -------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| What I Completed This Week    | Briefly describe the concrete work you completed in your own branch. | I reviewed the existing `GEAR.HR` codebase to identify where CSV persistence was being used across employees, attendance, leave requests, payroll records, user credentials, and IT tickets. I also drafted the initial migration direction: keep service behavior stable while moving file/database access behind repository classes. |
| Design Decisions Referenced   | Which Milestone 1 design decisions guided your work?                 | I followed the decision that persistence responsibilities should belong to repositories, not Swing screens or service calculations. This matched the planned OOP separation where `ApplicationContext` wires dependencies and services depend on interfaces instead of concrete CSV or JDBC classes.                                   |
| Challenges Encountered        | What issues, confusion, or friction did you encounter?               | The main challenge was that several methods still had legacy names such as `loadEmployeesFromCSV` and `savePayrollDataToCSV`, even though the goal was to support JDBC. This made it easy to confuse the method name with the actual backend being used.                                                                               |
| Resolutions or Current Status | How did you address these challenges?                                | I recommended keeping the legacy method names temporarily to avoid breaking UI/service calls, while making the repository implementation decide whether data comes from CSV or JDBC. This let the group migrate storage without rewriting every caller at once.                                                                        |
| Next Steps                    | What do you plan to work on next?                                    | Next, I will coordinate the shared repository direction and check how `DatabaseConnectionManager`, JDBC repository classes, and `ApplicationContext` can be integrated without changing payroll and attendance behavior.                                                                                                               |




### Charlize Bactong


| SECTION                       | GUIDING PROMPTS                                                      | ANSWER                                                                                                                                                                                                                                                                                                  |
| ----------------------------- | -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| What I Completed This Week    | Briefly describe the concrete work you completed in your own branch. | I traced the employee and attendance flows from the UI into `EmployeeService` and `AttendanceService`. I noted which parts needed to remain unchanged when the data source moved from CSV files to SQL tables, especially employee lookup, attendance row loading, and monthly worked-hour calculation. |
| Design Decisions Referenced   | Which Milestone 1 design decisions guided your work?                 | I referenced the design decision that business services should not know whether data comes from CSV or a database. The service layer should call `IEmployeeRepository` and `IAttendanceRepository`, while separate repository implementations handle the storage details.                               |
| Challenges Encountered        | What issues, confusion, or friction did you encounter?               | Attendance calculations depended on records being loaded into memory first, so I had to check whether switching the source to SQL would affect `getWorkedHoursForMonth`. I also noticed that malformed or blank attendance rows could affect payroll if not filtered properly.                          |
| Resolutions or Current Status | How did you address these challenges?                                | I confirmed that the current attendance service already skips invalid time-in/time-out values and filters by employee and month. The status is ready for repository migration as long as JDBC rows are mapped into the same `AttendanceRecord` model.                                                   |
| Next Steps                    | What do you plan to work on next?                                    | Next, I will compare CSV-loaded and SQL-loaded employee/attendance data to ensure both produce the same service results after the JDBC repositories are introduced.                                                                                                                                     |




### Angelica Mae Calipayan


| SECTION                       | GUIDING PROMPTS                                                      | ANSWER                                                                                                                                                                                                                                                                                             |
| ----------------------------- | -------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| What I Completed This Week    | Briefly describe the concrete work you completed in your own branch. | I reviewed the payroll flow around `PayrollProcessor`, `PayrollData`, and `PayrollUtils`. I identified which payroll values are stored as settings and which values are computed at runtime, so the future JDBC migration would not accidentally store computed payroll results as permanent data. |
| Design Decisions Referenced   | Which Milestone 1 design decisions guided your work?                 | I used the design decision that payroll computation must remain in service/model logic, while persistence stores only the data needed for computation. This supports the planned repository migration because `PayrollProcessor` can keep using `IPayrollRepository`.                              |
| Challenges Encountered        | What issues, confusion, or friction did you encounter?               | The payroll data had both base salary and hourly rate fields, and the actual monthly payroll uses hourly rate multiplied by attendance hours. It was important not to treat base salary as the direct net-pay input when planning repository storage.                                              |
| Resolutions or Current Status | How did you address these challenges?                                | I documented that `payroll_settings` should hold base salary, hourly rate, deductions, and allowances, while `PayrollResult` should remain computed on demand. This clarified the boundary between stored payroll settings and generated payroll output.                                           |
| Next Steps                    | What do you plan to work on next?                                    | Next, I will help validate the payroll repository mapping and confirm that SQL-loaded payroll settings still produce the same deduction and net salary calculations.                                                                                                                               |




### Chelsie Mae Ricafrante


| SECTION                       | GUIDING PROMPTS                                                      | ANSWER                                                                                                                                                                                                                                                                         |
| ----------------------------- | -------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| What I Completed This Week    | Briefly describe the concrete work you completed in your own branch. | I reviewed the user role and UI access flow, especially how HR, Payroll, Accounting, and IT users reach employee profile and payroll management screens. I also started collecting documentation notes for the transition from CSV storage to a JDBC-backed repository design. |
| Design Decisions Referenced   | Which Milestone 1 design decisions guided your work?                 | I referenced the decision that access control should be centralized through `RoleGroup`, and that UI screens should not contain persistence-specific checks. This keeps role visibility separate from whether the storage backend is CSV or JDBC.                              |
| Challenges Encountered        | What issues, confusion, or friction did you encounter?               | Some payroll access was role-dependent, while the persistence migration was storage-dependent. The challenge was making sure these concerns stayed separate and that new database work did not change who can view or edit payroll screens.                                    |
| Resolutions or Current Status | How did you address these challenges?                                | I mapped the role groups and noted that Payroll/Accounting users can share payroll directives through `RoleGroup.PAYROLL`. The status is ready for later documentation and test planning updates after the JDBC implementation is finalized.                                   |
| Next Steps                    | What do you plan to work on next?                                    | Next, I will help update project documentation and test planning so the JDBC repository transition and payroll/reporting access rules are clearly reflected.                                                                                                                   |




## Group Convergence Summary


| SECTION                       | GUIDING PROMPTS                                                                                                        | ANSWER                                                                                                                                                                                                                                                                                           |
| ----------------------------- | ---------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Where Our Approaches Aligned  | What similarities did you notice across individual implementations?                                                    | All members agreed that CSV access should be moved behind repository classes and that services/UI should continue using the same models and interfaces. We also agreed that payroll calculation should stay in `PayrollProcessor` and `PayrollUtils`, not in SQL queries or Swing handlers.      |
| Where Our Approaches Differed | Where did individual approaches vary? Were there different interpretations of the Design Log?                          | The main difference was emphasis: Colin focused on overall architecture and wiring, Charlize focused on employee/attendance data consistency, Angelica focused on payroll computation boundaries, and Chelsie focused on access/documentation. These were complementary rather than conflicting. |
| Agreed Group Direction        | What approach will the group adopt moving forward? Which changes will be integrated into the shared codebase, and why? | The group agreed to adopt a dual-backend repository approach: keep CSV repositories as fallback/seed support while adding JDBC repositories for the main MySQL storage. `ApplicationContext` will decide which repository implementation to use so existing service behavior remains stable.     |
| Coordination Notes            | Did you need (or decide) to meet earlier than planned? Are there open questions to raise during the mentor sync?       | We agreed to meet before the Week 6-7 migration work to divide repository responsibilities and confirm the SQL schema. The main mentor-sync question is how much CSV backward compatibility should be kept while JDBC becomes the default storage mode.                                          |


