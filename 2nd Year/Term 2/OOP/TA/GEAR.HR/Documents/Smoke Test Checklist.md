
|     |                                                                           |     |     |        |                                                                                                   |
| --- | ------------------------------------------------------------------------- | --- | --- | ------ | ------------------------------------------------------------------------------------------------- |
|     | MO-IT110 Object-Oriented Programming                                      |     |     |        |                                                                                                   |
|     | Smoke Test Checklist                                                      |     |     |        |                                                                                                   |
|     |                                                                           |     |     |        |                                                                                                   |
|     | Complete immediately after finishing Task 3: GUI Integration              |     |     |        |                                                                                                   |
|     | Smoke Test Item                                                           |     |     | Yes/No | Notes                                                                                             |
|     | GUI opens without errors                                                  |     |     | Yes    | App starts; Main, SplashScreen, ApplicationContext load.                                          |
|     | Splash screen displays and transitions to login                           |     |     | Yes    | SplashScreen 2s then callback to User.showLoginScreen.                                            |
|     | Login accepts valid credentials and opens main dashboard                  |     |     | Yes    | AuthenticationService + Main.showMainScreen.                                                      |
|     | Invalid credentials show an error and do not open dashboard               |     |     | Yes    | User shows JOptionPane on auth failure.                                                           |
|     | Role-based sidebar shows correct menu items (e.g. HR vs Normal)           |     |     | Yes    | Main builds sidebar from RoleGroup (HR/PAYROLL/IT_ADMIN/NORMAL).                                  |
|     | Compute Salary button triggers processing and updates display             |     |     | Yes    | EmployeeProfile calls PayrollProcessor.processPayroll + PayrollReport.format.                     |
|     | Employee (and subclass/role) object instantiates successfully after login |     |     | Yes    | AuthenticationService returns Employee; role/email from credentials.                              |
|     | Payroll processor runs without crashing for a selected employee and month |     |     | Yes    | PayrollProcessor + PayrollUtils; exceptions caught and shown.                                     |
|     | Attendance record can be saved and appears in the table after refresh     |     |     | Yes    | AttendanceScreen → AttendanceService.addRecord → AttendanceRepository.save.                       |
|     | Leave request submits successfully and appears in the list                |     |     | Yes    | LeaveManagementScreen → LeaveService.addLeaveRequest (with overlap check).                        |
|     | GUI displays payroll output (breakdown and totals) after computation      |     |     | Yes    | Re-ran Compute Salary; breakdown and totals now appear in the salary tab.                         |
|     | Missing or malformed CSV file is handled without crashing                 |     |     | Yes    | Removed payroll_records.csv temporarily; app started and showed empty payroll list without crash. |
|     | Incorrect date or time format is rejected with a clear message            |     |     | Yes    | AttendanceScreen/LeaveManagementScreen validation + JOptionPane.                                  |
|     | Payroll receipt downloads to a chosen file path                           |     |     | Yes    | EmployeeProfile writes PayrollReport to .txt; IOException shown in dialog.                        |
|     | HR can record attendance; Payroll user sees view-only (no record form)    |     |     | Yes    | AttendanceScreen role checks (HR/IT vs PAYROLL).                                                  |
|     | Logout closes main window and returns to login screen                     |     |     | Yes    | Main logout disposes frame and calls User.showLoginScreen.                                        |
|     | Notes from the Smoke Test                                                 |     |     |        |                                                                                                   |
|     | Anything unusual? Crashes? Missing behaviors?                             |     |     |        |                                                                                                   |
|     |                                                                           |     |     |        |                                                                                                   |
|     |                                                                           |     |     |        |                                                                                                   |
|     |                                                                           |     |     |        |                                                                                                   |
|     |                                                                           |     |     |        |                                                                                                   |
|     |                                                                           |     |     |        |                                                                                                   |


