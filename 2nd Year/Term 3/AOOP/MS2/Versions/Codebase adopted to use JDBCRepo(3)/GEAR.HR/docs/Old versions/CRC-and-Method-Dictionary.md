# GEAR.HR — CRC Cards and Method Dictionary

GEAR.HR is a desktop HR system built in **Java Swing** for managing employees, attendance, leave requests, and payroll. Data is persisted to CSV files under `csv/`. The application entry point is `ui.Main`.

## Table 1 — CRC Cards

| Class Name | Responsibility |
|------------|----------------|
| `AbstractEntity` | Abstract base class that defines shared characteristics of domain entities that have an identifier. Subclasses (Employee, AttendanceRecord, LeaveRequest, PayrollResult) extend this class and provide their own validation logic. Implements Identifiable and Validatable. |
| `AttendanceRecord` | Domain model for a single attendance entry (OOP redesign - GEAR.HR). Child class extends AbstractEntity. Validates time-out after time-in and non-negative hours worked. |
| `Employee` | Domain model representing an employee (OOP redesign - GEAR.HR). Child class extends AbstractEntity. Encapsulates personal, work, and payroll-related attributes. Validation in setters per OOP.txt: non-negative rates, non-empty password, valid roles. Format validation delegated to EmployeeValidationUtil for consistency at persistence boundaries. |
| `Identifiable` | Defines entities that have a unique identifier for persistence and lookup. Implemented by AbstractEntity and its subclasses (Employee, AttendanceRecord, LeaveRequest, PayrollResult). |
| `ItTicket` | Domain model for IT support tickets. Child class extends AbstractEntity. |
| `LeaveRequest` | Domain model for a leave request (OOP redesign - GEAR.HR). Child class extends AbstractEntity. Validates start date precedes end date and status is in allowed set. |
| `PayrollData` | Domain model for payroll data per employee (OOP redesign - GEAR.HR). Implements Validatable only (no in-object id; does not extend AbstractEntity). Holds base salary, deductions, and allowances; used by PayrollProcessor. Amounts are stored at creation time (computed by PayrollUtils in service layer). |
| `PayrollResult` | DTO for payroll computation result (OOP redesign - GEAR.HR). Child class extends AbstractEntity. Carries computed breakdown for PayrollReport to format. |
| `UserCredential` | Domain model for user login credentials. Child class extends AbstractEntity. |
| `Validatable` | Defines entities that can validate their own state. Implemented by AbstractEntity subclasses and by PayrollData. |
| `AbstractCsvListRepository` | Abstract base for CSV repositories that persist a list of entities. Subclasses provide file path, header, and line parsing/serialization; this class implements the common load/save file I/O. Concrete repos extend and implement the abstract hooks. |
| `AttendanceRepository` | CSV persistence for attendance records. Load/save only; no business logic. Implements IAttendanceRepository. Extends AbstractCsvListRepository for shared CSV load/save; provides AttendanceRecord-specific parse/serialize. Can be used as IAttendanceRepository by callers. |
| `EmployeeRepository` | CSV persistence for employees (employees.csv). Load/save only; no business logic. Implements IEmployeeRepository. Extends AbstractCsvListRepository for shared CSV load/save; provides Employee-specific parse/serialize. Can be used as IEmployeeRepository by callers. |
| `IAttendanceRepository` | Contract for loading and saving attendance records. Implementations (e.g. CSV) can be swapped for testing or different storage. |
| `IEmployeeRepository` | Contract for loading and saving employee data. Implementations (e.g. CSV) can be swapped for testing or different storage. |
| `IItTicketRepository` | Contract for loading and saving IT support tickets. Implementations (e.g. CSV) can be swapped for testing or different storage. |
| `ILeaveRequestRepository` | Contract for loading and saving leave requests. Implementations (e.g. CSV) can be swapped for testing or different storage. |
| `IPayrollRepository` | Contract for loading and saving payroll data (employeeId -> PayrollData). Implementations (e.g. CSV) can be swapped for testing or different storage. |
| `ItTicketRepository` | CSV persistence for IT support tickets. Load/save only; no business logic. Implements IItTicketRepository. Extends AbstractCsvListRepository for shared CSV load/save; provides ItTicket-specific parse/serialize. Can be used as IItTicketRepository by callers. |
| `IUserCredentialRepository` | Contract for loading and saving user credentials. Implementations (e.g. CSV) can be swapped for testing or different storage. |
| `LeaveRequestRepository` | CSV persistence for leave requests. Load/save only; no business logic. Implements ILeaveRequestRepository. Extends AbstractCsvListRepository for shared CSV load/save; provides LeaveRequest-specific parse/serialize. Can be used as ILeaveRequestRepository by callers. |
| `PayrollRepository` | CSV persistence for payroll data (employeeId -> PayrollData). Load/save only. Expects map values to already include withholding tax when saving. Implements IPayrollRepository. Can be used as IPayrollRepository by callers. |
| `UserCredentialRepository` | CSV persistence for user credentials. Load/save only; no business logic. Implements IUserCredentialRepository. Extends AbstractCsvListRepository for shared CSV load/save; provides UserCredential-specific parse/serialize. Can be used as IUserCredentialRepository by callers. |
| `ApplicationContext` | Composition root: creates repositories and services; passed into UI (DI). Stores and exposes interface types so callers can depend on abstractions. |
| `AttendanceService` | Manages attendance records; persistence delegated to repository (OOP redesign - GEAR.HR). Implements IAttendanceService. Can be used as IAttendanceService by callers; depends on IAttendanceRepository. |
| `AuthContext` | Immutable authentication context value object. |
| `AuthenticationService` | Concrete authentication implementation used via IAuthenticationService. Loads and validates credentials independent of UI. |
| `EmployeeService` | Manages employee data; persistence delegated to repository (OOP redesign - GEAR.HR). Implements IEmployeeService. Can be used as IEmployeeService by callers; depends on IEmployeeRepository. |
| `IAttendanceService` | Contract for attendance record management: load and CRUD. Implementations can be swapped for testing or different backends. |
| `IAuthenticationService` | Authentication contract used by callers. |
| `IEmployeeService` | Contract for employee management: load/save and CRUD. Implementations can be swapped for testing or different backends. |
| `IItTicketService` | Contract for IT ticket use cases: reload, list, create, update status, delete. |
| `ILeaveService` | Contract for leave request management: load and CRUD. Implementations can be swapped for testing or different backends. |
| `ItTicketService` | Service implementation for IT ticket use cases. Maintains in-memory ticket list and persists via repository abstraction. |
| `IUserCredentialService` | Contract for credential administration: reload, query, and update accounts. |
| `LeaveService` | Manages leave requests; persistence delegated to repository (OOP redesign - GEAR.HR). Implements ILeaveService. Can be used as ILeaveService by callers; depends on ILeaveRequestRepository. |
| `PayrollProcessor` | Computes employee compensation; persistence delegated to repository (OOP redesign - GEAR.HR). Depends on IPayrollRepository so implementations can be swapped. |
| `PayrollReport` | Formats payroll computation results for display (OOP redesign - GEAR.HR). Separates presentation text from payroll computation (PayrollProcessor). |
| `RoleGroup` | Role-based access: classifies user role string into HR, Payroll, IT/Admin, or Normal Employee group. Used by Main and screens to show the correct homepage and enforce access restrictions. Enum + factory #fromRole(String) classify roles without string subclassing. |
| `UserCredentialService` | Service implementation for credential administration. Owns loaded credential collection and persistence lifecycle. |
| `AttendanceScreen` | Attendance UI screen; services injected (DI). Named AttendanceScreen to avoid confusion with domain/service. Implements ModuleScreen. Extends BaseModuleScreen for shared colors and helpers. INSTANCE can be used as ModuleScreen by Main. |
| `BaseModuleScreen` | Abstract base for module screens; provides shared colors and layout helpers. Subclasses use protected colors and createFrame/createHeaderPanel/createFooterPanel to avoid duplication. Content is built by each subclass. |
| `EmployeeProfile` | EmployeeProfile UI; employee and payroll data delegated to services (OOP redesign). Implements ModuleScreen. Extends BaseModuleScreen for shared colors and helpers. INSTANCE can be used as ModuleScreen by Main. |
| `InputFilters` | UI-layer input restrictions for employee forms. Keeps the domain/persistence layer responsible for final validation via EmployeeValidationUtil. Inner RegexOnlyDocumentFilter extends DocumentFilter to restrict input. Non-instantiable facade; applies filters via static methods. |
| `InputFilters.RegexOnlyDocumentFilter` | Private DocumentFilter that allows only text matching a configured regex pattern when editing Swing text fields. |
| `LeaveManagementScreen` | Leave management UI; view, create, and update leave request status. Services injected (DI). Implements ModuleScreen. Extends BaseModuleScreen for shared colors and helpers. INSTANCE can be used as ModuleScreen by Main. |
| `Main` | Main class for the Employee Management System. Central dashboard witnih navigation to modules. Opens module screens via ModuleScreen interface so any screen can be shown uniformly. |
| `ModuleScreen` | Contract for module screens that can be shown with a single polymorphic call. Enables Main to open any screen without knowing the concrete class. |
| `SplashScreen` | SplashScreen provides an animated loading screen before the main application loads. Bootstrap UI only; no domain or service dependencies. |
| `User` | User class handles login UI; authentication delegated to AuthenticationService (OOP redesign). Delegates authentication to service.IAuthenticationService via ApplicationContext. |
| `UserCredentialManagementScreen` | Admin UI for credentials and IT tickets; services from ApplicationContext (DI). Implements ModuleScreen. Extends BaseModuleScreen for shared colors and helpers. INSTANCE can be used as ModuleScreen by Main. |
| `CredentialValidationUtil` | Utility class; validates credentials via static helpers and private constructor. |
| `EmployeeValidationUtil` | Format validation for employee profile fields (Employee Profile Validation Plan). Each method returns null if valid, or an error message describing the parameter and required format. Utility class with private constructor; validation rules encapsulated as patterns. |
| `PayrollUtils` | Utility class: private constructor prevents instantiation; static payroll calculation helpers. |
| `TableColumnSortUtil` | Installs header-click sorting on a JTable with at most one sort column at a time. Numeric columns compare parsed numbers; other columns compare case-insensitive strings. Unparseable numeric cells sort last (treated as positive infinity when ascending). Hides TableRowSorter setup behind a single install method. Non-instantiable utility type. |
| `ApplicationContext.ItTicketRepositoryFallback` | Anonymous no-op IItTicketRepository used when ItTicketRepository cannot be loaded. |

## Table 2 — Method Dictionary

| Class | Method Name | Purpose | Visibility | Parameters | Return type |
|-------|-------------|---------|------------|------------|-------------|
| `AbstractEntity` | `AbstractEntity` | Protected constructor for subclasses; stores the entity id (null-safe). | protected | String id | `—` |
| `AbstractEntity` | `getId` | Returns id. | public | none | `String` |
| `AbstractEntity` | `isValid` | Subclasses must implement validation (Validatable.isValid). | public abstract | none | `boolean` |
| `AttendanceRecord` | `AttendanceRecord` | Constructs a new AttendanceRecord instance. | public | String employeeId, String date, String status, String timeIn, String timeOut | `—` |
| `AttendanceRecord` | `getEmployeeId` | Returns employee id. | public | none | `String` |
| `AttendanceRecord` | `getDate` | Returns date. | public | none | `String` |
| `AttendanceRecord` | `getStatus` | Returns status. | public | none | `String` |
| `AttendanceRecord` | `getTimeIn` | Returns time in. | public | none | `String` |
| `AttendanceRecord` | `getTimeOut` | Returns time out. | public | none | `String` |
| `AttendanceRecord` | `getHoursWorked` | Hours worked as "H:MM" or "N/A" if invalid. | public | none | `String` |
| `AttendanceRecord` | `getMinutesWorked` | Total minutes worked; negative if time-out is before time-in. | public | none | `int` |
| `AttendanceRecord` | `isValid` | Returns whether Valid. | public | none | `boolean` |
| `AttendanceRecord` | `getHoursWorked` | Returns hours worked. | public | boolean asMinutes | `String` |
| `Employee` | `Employee` | Constructs a new Employee instance. | public | String employeeNumber, String lastName, String firstName, String sssNumber, String philHealthNumber, String tin, String pagIbigNumber, String email, String position, String status, String address, String phone | `—` |
| `Employee` | `getEmployeeNumber` | Returns employee number. | public | none | `String` |
| `Employee` | `getLastName` | Returns last name. | public | none | `String` |
| `Employee` | `setLastName` | Sets last name. | public | String lastName | `void` |
| `Employee` | `getFirstName` | Returns first name. | public | none | `String` |
| `Employee` | `setFirstName` | Sets first name. | public | String firstName | `void` |
| `Employee` | `getSssNumber` | Returns sss number. | public | none | `String` |
| `Employee` | `setSssNumber` | Sets sss number. | public | String sssNumber | `void` |
| `Employee` | `getPhilHealthNumber` | Returns phil health number. | public | none | `String` |
| `Employee` | `setPhilHealthNumber` | Sets phil health number. | public | String philHealthNumber | `void` |
| `Employee` | `getTin` | Returns tin. | public | none | `String` |
| `Employee` | `setTin` | Sets tin. | public | String tin | `void` |
| `Employee` | `getPagIbigNumber` | Returns pag ibig number. | public | none | `String` |
| `Employee` | `setPagIbigNumber` | Sets pag ibig number. | public | String pagIbigNumber | `void` |
| `Employee` | `getEmail` | Returns email. | public | none | `String` |
| `Employee` | `setEmail` | Sets email. | public | String email | `void` |
| `Employee` | `getPosition` | Returns position. | public | none | `String` |
| `Employee` | `setPosition` | Sets position. | public | String position | `void` |
| `Employee` | `getStatus` | Returns status. | public | none | `String` |
| `Employee` | `setStatus` | Sets status. | public | String status | `void` |
| `Employee` | `getAddress` | Returns address. | public | none | `String` |
| `Employee` | `setAddress` | Sets address. | public | String address | `void` |
| `Employee` | `getPhone` | Returns phone. | public | none | `String` |
| `Employee` | `setPhone` | Sets phone. | public | String phone | `void` |
| `Employee` | `getHourlyRate` | Returns hourly rate. | public | none | `double` |
| `Employee` | `setHourlyRate` | Sets hourly rate. | public | double hourlyRate | `void` |
| `Employee` | `isValid` | Returns whether Valid. | public | none | `boolean` |
| `Employee` | `getValidationError` | Returns the first validation error message, or null if this employee is valid. Uses EmployeeValidationUtil for format checks so validation is consistent at persistence boundaries. | public | none | `String` |
| `Employee` | `getDisplayName` | Returns display name. | public | none | `String` |
| `Employee` | `getDisplayName` | Returns display name. | public | boolean includeId | `String` |
| `Identifiable` | `getId` | Returns the unique identifier of this entity. | package | none | `String` |
| `ItTicket` | `ItTicket` | Constructs a new ItTicket instance. | public | String ticketId, String userIdRequestor, String typeOfRequest, String status | `—` |
| `ItTicket` | `getTicketId` | Returns ticket id. | public | none | `String` |
| `ItTicket` | `getUserIdRequestor` | Returns user id requestor. | public | none | `String` |
| `ItTicket` | `setUserIdRequestor` | Sets user id requestor. | public | String userIdRequestor | `void` |
| `ItTicket` | `getTypeOfRequest` | Returns type of request. | public | none | `String` |
| `ItTicket` | `setTypeOfRequest` | Sets type of request. | public | String typeOfRequest | `void` |
| `ItTicket` | `getStatus` | Returns status. | public | none | `String` |
| `ItTicket` | `setStatus` | Sets status. | public | String status | `void` |
| `ItTicket` | `isValid` | Returns whether Valid. | public | none | `boolean` |
| `LeaveRequest` | `LeaveRequest` | Constructs a new LeaveRequest instance. | public | String employeeId, LocalDate startDate, LocalDate endDate, String reason, String status | `—` |
| `LeaveRequest` | `getEmployeeId` | Returns employee id. | public | none | `String` |
| `LeaveRequest` | `setEmployeeId` | Prevents identity drift from AbstractEntity#getId(). Setter accepts same value for compatibility and ignores conflicting values. | public | String employeeId | `void` |
| `LeaveRequest` | `getStartDate` | Returns start date. | public | none | `LocalDate` |
| `LeaveRequest` | `setStartDate` | Sets start date. | public | LocalDate startDate | `void` |
| `LeaveRequest` | `getEndDate` | Returns end date. | public | none | `LocalDate` |
| `LeaveRequest` | `setEndDate` | Sets end date. | public | LocalDate endDate | `void` |
| `LeaveRequest` | `getReason` | Returns reason. | public | none | `String` |
| `LeaveRequest` | `setReason` | Sets reason. | public | String reason | `void` |
| `LeaveRequest` | `getStatus` | Returns status. | public | none | `String` |
| `LeaveRequest` | `setStatus` | Sets status. | public | String status | `void` |
| `LeaveRequest` | `setStatus` | Sets status. | public | String status, boolean skipValidation | `void` |
| `LeaveRequest` | `isValid` | Returns whether Valid. | public | none | `boolean` |
| `LeaveRequest` | `isValidDateRange` | Returns whether ValidDateRange. | public | none | `boolean` |
| `LeaveRequest` | `overlapsWith` | Returns true if this request's date range overlaps the other's. Two ranges [s1,e1] and [s2,e2] overlap iff !(e1.isBefore(s2) \|\| e2.isBefore(s1)). | public | LeaveRequest other | `boolean` |
| `LeaveRequest` | `parseDate` | Returns result of parseDate. | public static | String dateStr | `LocalDate` |
| `PayrollData` | `PayrollData` | Constructs a new PayrollData instance. | public | double baseSalary, double hourlyRate, double sssAmount, double philHealthAmount, double pagIbigAmount, float withholdingTax, float riceSubsidy, float phoneAllowance, float clothingAllowance | `—` |
| `PayrollData` | `getBaseSalary` | Returns base salary. | public | none | `double` |
| `PayrollData` | `getHourlyRate` | Returns hourly rate. | public | none | `double` |
| `PayrollData` | `getSssAmount` | Returns sss amount. | public | none | `double` |
| `PayrollData` | `getPhilHealthAmount` | Returns phil health amount. | public | none | `double` |
| `PayrollData` | `getPagIbigAmount` | Returns pag ibig amount. | public | none | `double` |
| `PayrollData` | `getWithholdingTax` | Returns withholding tax. | public | none | `float` |
| `PayrollData` | `getRiceSubsidy` | Returns rice subsidy. | public | none | `float` |
| `PayrollData` | `getPhoneAllowance` | Returns phone allowance. | public | none | `float` |
| `PayrollData` | `getClothingAllowance` | Returns clothing allowance. | public | none | `float` |
| `PayrollData` | `getSSSDeduction` | Returns s s s deduction. | public | none | `double` |
| `PayrollData` | `getPhilHealthDeduction` | Returns phil health deduction. | public | none | `double` |
| `PayrollData` | `getPagIbigDeduction` | Returns pag ibig deduction. | public | none | `double` |
| `PayrollData` | `getTaxDeduction` | Returns tax deduction. | public | none | `double` |
| `PayrollData` | `calculateTotalDeductions` | Returns result of calculateTotalDeductions. | public | none | `double` |
| `PayrollData` | `calculateTotalAllowances` | Returns result of calculateTotalAllowances. | public | none | `double` |
| `PayrollData` | `calculateNetSalary` | Estimated net for reference hours only; base salary is informational and does not drive this calculation. | public | none | `double` |
| `PayrollData` | `calculateGrossPay` | Returns result of calculateGrossPay. | public | double workedHours | `double` |
| `PayrollData` | `calculateReferenceMonthlyGross` | Returns result of calculateReferenceMonthlyGross. | public | none | `double` |
| `PayrollData` | `calculateNetSalaryForHours` | Returns result of calculateNetSalaryForHours. | public | double workedHours | `double` |
| `PayrollData` | `calculateNetSalary` | Returns result of calculateNetSalary. | public | double additionalDeduction | `double` |
| `PayrollData` | `isValid` | Returns whether Valid. | public | none | `boolean` |
| `PayrollResult` | `PayrollResult` | Constructs a new PayrollResult instance. | public | String employeeId, String employeeName, String position, String month, double hourlyRate, double workedHours, double grossPay, double baseSalary, double sssDeduction, double philHealthDeduction, double pagIbigDeduction, double taxDeduction, double totalDeductions, double riceSubsidy, double phoneAllowance, double clothingAllowance, double totalAllowances, double netSalary | `—` |
| `PayrollResult` | `getEmployeeId` | Returns employee id. | public | none | `String` |
| `PayrollResult` | `getEmployeeName` | Returns employee name. | public | none | `String` |
| `PayrollResult` | `getPosition` | Returns position. | public | none | `String` |
| `PayrollResult` | `getMonth` | Returns month. | public | none | `String` |
| `PayrollResult` | `getHourlyRate` | Returns hourly rate. | public | none | `double` |
| `PayrollResult` | `getWorkedHours` | Returns worked hours. | public | none | `double` |
| `PayrollResult` | `getGrossPay` | Returns gross pay. | public | none | `double` |
| `PayrollResult` | `getBaseSalary` | Returns base salary. | public | none | `double` |
| `PayrollResult` | `getSssDeduction` | Returns sss deduction. | public | none | `double` |
| `PayrollResult` | `getPhilHealthDeduction` | Returns phil health deduction. | public | none | `double` |
| `PayrollResult` | `getPagIbigDeduction` | Returns pag ibig deduction. | public | none | `double` |
| `PayrollResult` | `getTaxDeduction` | Returns tax deduction. | public | none | `double` |
| `PayrollResult` | `getTotalDeductions` | Returns total deductions. | public | none | `double` |
| `PayrollResult` | `getRiceSubsidy` | Returns rice subsidy. | public | none | `double` |
| `PayrollResult` | `getPhoneAllowance` | Returns phone allowance. | public | none | `double` |
| `PayrollResult` | `getClothingAllowance` | Returns clothing allowance. | public | none | `double` |
| `PayrollResult` | `getTotalAllowances` | Returns total allowances. | public | none | `double` |
| `PayrollResult` | `getNetSalary` | Returns net salary. | public | none | `double` |
| `PayrollResult` | `isValid` | Returns whether Valid. | public | none | `boolean` |
| `PayrollResult` | `getFormattedAmount` | Returns formatted amount. | public | String field | `String` |
| `PayrollResult` | `getFormattedAmount` | Returns formatted amount. | public | String field, String format | `String` |
| `UserCredential` | `UserCredential` | Constructs a new UserCredential instance. | public | String userId, String password, String role, String email | `—` |
| `UserCredential` | `getUserId` | Returns user id. | public | none | `String` |
| `UserCredential` | `getPassword` | Returns password. | public | none | `String` |
| `UserCredential` | `setPassword` | Sets password. | public | String password | `void` |
| `UserCredential` | `getRole` | Returns role. | public | none | `String` |
| `UserCredential` | `setRole` | Sets role. | public | String role | `void` |
| `UserCredential` | `getEmail` | Returns email. | public | none | `String` |
| `UserCredential` | `setEmail` | Sets email. | public | String email | `void` |
| `UserCredential` | `isValid` | Returns whether Valid. | public | none | `boolean` |
| `UserCredential` | `getValidationError` | Returns validation error. | public | none | `String` |
| `Validatable` | `isValid` | Returns true if this entity's state is valid according to its business rules. | package | none | `boolean` |
| `AbstractCsvListRepository` | `getFilePath` | Subclass returns the CSV file path (e.g. "csv/employees.csv"). | protected abstract | none | `String` |
| `AbstractCsvListRepository` | `getHeader` | Subclass returns the header line (e.g. "EmployeeNumber,LastName,..."). | protected abstract | none | `String` |
| `AbstractCsvListRepository` | `parseLine` | Subclass parses one CSV line (already split) into an entity; returns null to skip. | protected abstract | String[] parts | `T` |
| `AbstractCsvListRepository` | `toCsvRow` | Subclass converts one entity to CSV column values for writing. | protected abstract | T item | `String[]` |
| `AbstractCsvListRepository` | `splitLine` | Splits a raw line into parts; default is comma. Override for quoted CSV. | protected | String line | `String[]` |
| `AbstractCsvListRepository` | `load` | Template method: reads file, skips header, parses each line via parseLine. Not final so subclasses can override to implement interface and delegate to super. | public | none | `List<T>` |
| `AbstractCsvListRepository` | `save` | Template method: writes header then each entity via toCsvRow. Not final so subclasses can override to implement interface and delegate to super. | public | List<T> items | `void` |
| `AttendanceRepository` | `getFilePath` | Returns file path. | protected | none | `String` |
| `AttendanceRepository` | `getHeader` | Returns header. | protected | none | `String` |
| `AttendanceRepository` | `parseLine` | Returns result of parseLine. | protected | String[] parts | `AttendanceRecord` |
| `AttendanceRepository` | `toCsvRow` | Returns result of toCsvRow. | protected | AttendanceRecord r | `String[]` |
| `AttendanceRepository` | `load` | Returns result of load. | public | none | `List<AttendanceRecord>` |
| `AttendanceRepository` | `save` | Performs save. | public | List<AttendanceRecord> records | `void` |
| `EmployeeRepository` | `splitLine` | Returns result of splitLine. | protected | String line | `String[]` |
| `EmployeeRepository` | `getFilePath` | Returns file path. | protected | none | `String` |
| `EmployeeRepository` | `getHeader` | Returns header. | protected | none | `String` |
| `EmployeeRepository` | `parseLine` | Returns result of parseLine. | protected | String[] parts | `Employee` |
| `EmployeeRepository` | `toCsvRow` | Returns result of toCsvRow. | protected | Employee emp | `String[]` |
| `EmployeeRepository` | `load` | Returns result of load. | public | none | `List<Employee>` |
| `EmployeeRepository` | `save` | Performs save. | public | List<Employee> employees | `void` |
| `IAttendanceRepository` | `load` | Loads all attendance records from storage. | package | none | `List<AttendanceRecord>` |
| `IAttendanceRepository` | `save` | Saves the given list of attendance records to storage. | package | List<AttendanceRecord> records | `void` |
| `IEmployeeRepository` | `load` | Loads all employees from storage. | package | none | `List<Employee>` |
| `IEmployeeRepository` | `save` | Saves the given list of employees to storage. | package | List<Employee> employees | `void` |
| `IItTicketRepository` | `load` | Returns result of load. | package | none | `List<ItTicket>` |
| `IItTicketRepository` | `save` | Performs save. | package | List<ItTicket> tickets | `void` |
| `ILeaveRequestRepository` | `load` | Loads all leave requests from storage. | package | none | `List<LeaveRequest>` |
| `ILeaveRequestRepository` | `save` | Saves the given list of leave requests to storage. | package | List<LeaveRequest> requests | `void` |
| `IPayrollRepository` | `load` | Loads all payroll data from storage. | package | none | `Map<String, PayrollData>` |
| `IPayrollRepository` | `save` | Saves the given payroll data map to storage. | package | Map<String, PayrollData> data | `void` |
| `ItTicketRepository` | `ItTicketRepository` | Constructs a new ItTicketRepository instance. | public | none | `—` |
| `ItTicketRepository` | `getFilePath` | Returns file path. | protected | none | `String` |
| `ItTicketRepository` | `getHeader` | Returns header. | protected | none | `String` |
| `ItTicketRepository` | `parseLine` | Returns result of parseLine. | protected | String[] parts | `ItTicket` |
| `ItTicketRepository` | `toCsvRow` | Returns result of toCsvRow. | protected | ItTicket item | `String[]` |
| `ItTicketRepository` | `load` | Returns result of load. | public | none | `List<ItTicket>` |
| `ItTicketRepository` | `save` | Performs save. | public | List<ItTicket> tickets | `void` |
| `ItTicketRepository` | `ensureFileWithHeader` | Performs ensureFileWithHeader. | private | none | `void` |
| `IUserCredentialRepository` | `load` | Returns result of load. | package | none | `List<UserCredential>` |
| `IUserCredentialRepository` | `save` | Performs save. | package | List<UserCredential> credentials | `void` |
| `LeaveRequestRepository` | `getFilePath` | Returns file path. | protected | none | `String` |
| `LeaveRequestRepository` | `getHeader` | Returns header. | protected | none | `String` |
| `LeaveRequestRepository` | `parseLine` | Returns result of parseLine. | protected | String[] parts | `LeaveRequest` |
| `LeaveRequestRepository` | `toCsvRow` | Returns result of toCsvRow. | protected | LeaveRequest lr | `String[]` |
| `LeaveRequestRepository` | `load` | Returns result of load. | public | none | `List<LeaveRequest>` |
| `LeaveRequestRepository` | `save` | Performs save. | public | List<LeaveRequest> requests | `void` |
| `PayrollRepository` | `load` | Returns result of load. | public | none | `Map<String, PayrollData>` |
| `PayrollRepository` | `save` | Performs save. | public | Map<String, PayrollData> data | `void` |
| `UserCredentialRepository` | `getFilePath` | Returns file path. | protected | none | `String` |
| `UserCredentialRepository` | `getHeader` | Returns header. | protected | none | `String` |
| `UserCredentialRepository` | `parseLine` | Returns result of parseLine. | protected | String[] parts | `UserCredential` |
| `UserCredentialRepository` | `toCsvRow` | Returns result of toCsvRow. | protected | UserCredential item | `String[]` |
| `UserCredentialRepository` | `load` | Returns result of load. | public | none | `List<UserCredential>` |
| `UserCredentialRepository` | `save` | Performs save. | public | List<UserCredential> credentials | `void` |
| `ApplicationContext` | `getAuthenticationService` | Returns authentication service. | public | none | `IAuthenticationService` |
| `ApplicationContext` | `getEmployeeService` | Returns employee service. | public | none | `IEmployeeService` |
| `ApplicationContext` | `getAttendanceService` | Returns attendance service. | public | none | `IAttendanceService` |
| `ApplicationContext` | `getPayrollProcessor` | Returns payroll processor. | public | none | `PayrollProcessor` |
| `ApplicationContext` | `getLeaveService` | Returns leave service. | public | none | `ILeaveService` |
| `ApplicationContext` | `getUserCredentialService` | Returns user credential service. | public | none | `IUserCredentialService` |
| `ApplicationContext` | `getItTicketService` | Returns it ticket service. | public | none | `IItTicketService` |
| `ApplicationContext` | `createItTicketRepository` | Resolves IT ticket repository implementation without hard compile dependency. Fallback is an anonymous IItTicketRepository (empty load, no-op save). | private static | none | `IItTicketRepository` |
| `AttendanceService` | `AttendanceService` | Constructs a new AttendanceService instance. | public | IAttendanceRepository repository | `—` |
| `AttendanceService` | `loadAttendanceRecordsFromCSV` | Performs loadAttendanceRecordsFromCSV. | public | none | `void` |
| `AttendanceService` | `save` | Performs save. | private | none | `void` |
| `AttendanceService` | `getAllRecords` | Returns all records. | public | none | `List<AttendanceRecord>` |
| `AttendanceService` | `hasRecord` | Returns whether Record. | public | String employeeId, String date | `boolean` |
| `AttendanceService` | `addRecord` | Performs addRecord. | public | AttendanceRecord record | `void` |
| `AttendanceService` | `removeRecord` | Performs removeRecord. | public | String employeeId, String date | `void` |
| `AttendanceService` | `removeAttendanceRecords` | Performs removeAttendanceRecords. | public | String employeeId | `void` |
| `AttendanceService` | `clearAll` | Performs clearAll. | public | none | `void` |
| `AttendanceService` | `getWorkedHoursForMonth` | Returns worked hours for month. | public | String employeeId, String month | `double` |
| `AuthContext` | `AuthContext` | Constructor normalizes null values. | public | String role, String email | `—` |
| `AuthContext` | `getRole` | Returns authenticated role. | public | none | `String` |
| `AuthContext` | `getEmail` | Returns authenticated email. | public | none | `String` |
| `AuthenticationService` | `AuthenticationService` | Constructs a new AuthenticationService instance. | public | none | `—` |
| `AuthenticationService` | `loadUserCredentials` | Loads credentials from CSV. Tries CREDENTIALS_CSV then CREDENTIALS_CSV_ALT. Implements IAuthenticationService.loadUserCredentials. | public | none | `void` |
| `AuthenticationService` | `authenticate` | Returns result of authenticate. | public | String userId, String password | `Employee` |
| `AuthenticationService` | `getAuthContext` | Returns auth context. | public | String userId | `AuthContext` |
| `AuthenticationService` | `getRoleAndEmail` | Backward-compatible accessor for legacy callers. Prefer getAuthContext(userId) for new usage. | public | String userId | `String[]` |
| `AuthenticationService` | `hasUser` | Returns whether User. | public | String userId | `boolean` |
| `EmployeeService` | `EmployeeService` | Constructs a new EmployeeService instance. | public | IEmployeeRepository repository, IUserCredentialService userCredentialService | `—` |
| `EmployeeService` | `loadEmployeesFromCSV` | Performs loadEmployeesFromCSV. | public | none | `void` |
| `EmployeeService` | `saveEmployeesToCSV` | Performs saveEmployeesToCSV. | public | none | `void` |
| `EmployeeService` | `getAllEmployees` | Returns all employees. | public | none | `List<Employee>` |
| `EmployeeService` | `findEmployeeById` | Returns result of findEmployeeById. | public | String empNumber | `Employee` |
| `EmployeeService` | `findEmployeeBySss` | Returns result of findEmployeeBySss. | public | String sss | `Employee` |
| `EmployeeService` | `findEmployeeByPhilHealth` | Returns result of findEmployeeByPhilHealth. | public | String philHealth | `Employee` |
| `EmployeeService` | `findEmployeeByTin` | Returns result of findEmployeeByTin. | public | String tin | `Employee` |
| `EmployeeService` | `findEmployeeByPagIbig` | Returns result of findEmployeeByPagIbig. | public | String pagIbig | `Employee` |
| `EmployeeService` | `findEmployeeByEmail` | Returns result of findEmployeeByEmail. | public | String email | `Employee` |
| `EmployeeService` | `findEmployeeByPhone` | Returns result of findEmployeeByPhone. | public | String phone | `Employee` |
| `EmployeeService` | `addEmployee` | Returns result of addEmployee. | public | Employee emp | `String` |
| `EmployeeService` | `updateEmployee` | Returns result of updateEmployee. | public | Employee emp | `String` |
| `EmployeeService` | `deleteEmployee` | Performs deleteEmployee. | public | String empNumber | `void` |
| `IAttendanceService` | `loadAttendanceRecordsFromCSV` | Performs loadAttendanceRecordsFromCSV. | package | none | `void` |
| `IAttendanceService` | `getAllRecords` | Returns all records. | package | none | `List<AttendanceRecord>` |
| `IAttendanceService` | `hasRecord` | Returns whether Record. | package | String employeeId, String date | `boolean` |
| `IAttendanceService` | `addRecord` | Performs addRecord. | package | AttendanceRecord record | `void` |
| `IAttendanceService` | `removeRecord` | Performs removeRecord. | package | String employeeId, String date | `void` |
| `IAttendanceService` | `removeAttendanceRecords` | Performs removeAttendanceRecords. | package | String employeeId | `void` |
| `IAttendanceService` | `clearAll` | Performs clearAll. | package | none | `void` |
| `IAttendanceService` | `getWorkedHoursForMonth` | Returns worked hours for month. | package | String employeeId, String month | `double` |
| `IAuthenticationService` | `loadUserCredentials` | Reload credential storage into memory. | package | none | `void` |
| `IAuthenticationService` | `authenticate` | Authenticate a user; returns employee identity when valid. | package | String userId, String password | `Employee` |
| `IAuthenticationService` | `getAuthContext` | Returns role/email context for an existing user. | package | String userId | `AuthContext` |
| `IAuthenticationService` | `hasUser` | Returns true when a user id exists in credentials. | package | String userId | `boolean` |
| `IEmployeeService` | `loadEmployeesFromCSV` | Performs loadEmployeesFromCSV. | package | none | `void` |
| `IEmployeeService` | `saveEmployeesToCSV` | Performs saveEmployeesToCSV. | package | none | `void` |
| `IEmployeeService` | `getAllEmployees` | Returns all employees. | package | none | `List<Employee>` |
| `IEmployeeService` | `findEmployeeById` | Returns result of findEmployeeById. | package | String empNumber | `Employee` |
| `IEmployeeService` | `findEmployeeBySss` | Returns result of findEmployeeBySss. | package | String sss | `Employee` |
| `IEmployeeService` | `findEmployeeByPhilHealth` | Returns result of findEmployeeByPhilHealth. | package | String philHealth | `Employee` |
| `IEmployeeService` | `findEmployeeByTin` | Returns result of findEmployeeByTin. | package | String tin | `Employee` |
| `IEmployeeService` | `findEmployeeByPagIbig` | Returns result of findEmployeeByPagIbig. | package | String pagIbig | `Employee` |
| `IEmployeeService` | `findEmployeeByEmail` | Returns result of findEmployeeByEmail. | package | String email | `Employee` |
| `IEmployeeService` | `findEmployeeByPhone` | Returns result of findEmployeeByPhone. | package | String phone | `Employee` |
| `IEmployeeService` | `addEmployee` | Returns result of addEmployee. | package | Employee emp | `String` |
| `IEmployeeService` | `updateEmployee` | Returns result of updateEmployee. | package | Employee emp | `String` |
| `IEmployeeService` | `deleteEmployee` | Performs deleteEmployee. | package | String empNumber | `void` |
| `IItTicketService` | `reload` | Performs reload. | package | none | `void` |
| `IItTicketService` | `getAllTickets` | Returns all tickets. | package | none | `List<ItTicket>` |
| `IItTicketService` | `createTicket` | Returns result of createTicket. | package | String userIdRequestor, String typeOfRequest | `String` |
| `IItTicketService` | `updateTicketStatus` | Returns result of updateTicketStatus. | package | String ticketId, String newStatus | `boolean` |
| `IItTicketService` | `deleteTicket` | Returns result of deleteTicket. | package | String ticketId | `boolean` |
| `ILeaveService` | `loadLeaveRequestsFromCSV` | Performs loadLeaveRequestsFromCSV. | package | none | `void` |
| `ILeaveService` | `getAllLeaveRequests` | Returns all leave requests. | package | none | `List<LeaveRequest>` |
| `ILeaveService` | `getLeaveRequestsByEmployee` | Returns leave requests by employee. | package | String employeeId | `List<LeaveRequest>` |
| `ILeaveService` | `addLeaveRequest` | Performs addLeaveRequest. | package | LeaveRequest request | `void` |
| `ILeaveService` | `updateLeaveRequestStatus` | Performs updateLeaveRequestStatus. | package | String employeeId, LocalDate startDate, String newStatus | `void` |
| `ILeaveService` | `hasOverlappingLeaveRequest` | Returns whether OverlappingLeaveRequest. | package | String employeeId, LocalDate start, LocalDate end | `boolean` |
| `ILeaveService` | `deleteLeaveRequest` | Performs deleteLeaveRequest. | package | String employeeId, LocalDate startDate | `void` |
| `ItTicketService` | `ItTicketService` | Constructs a new ItTicketService instance. | public | IItTicketRepository repository, IAuthenticationService authenticationService | `—` |
| `ItTicketService` | `reload` | Performs reload. | public | none | `void` |
| `ItTicketService` | `getAllTickets` | Returns all tickets. | public | none | `List<ItTicket>` |
| `ItTicketService` | `createTicket` | Returns result of createTicket. | public | String userIdRequestor, String typeOfRequest | `String` |
| `ItTicketService` | `updateTicketStatus` | Returns result of updateTicketStatus. | public | String ticketId, String newStatus | `boolean` |
| `ItTicketService` | `deleteTicket` | Returns result of deleteTicket. | public | String ticketId | `boolean` |
| `ItTicketService` | `generateNextTicketId` | Returns result of generateNextTicketId. | private | none | `String` |
| `IUserCredentialService` | `reload` | Performs reload. | package | none | `void` |
| `IUserCredentialService` | `getAllCredentials` | Returns all credentials. | package | none | `List<UserCredential>` |
| `IUserCredentialService` | `getDistinctRoles` | Returns distinct roles. | package | none | `Set<String>` |
| `IUserCredentialService` | `findByUserId` | Returns result of findByUserId. | package | String userId | `UserCredential` |
| `IUserCredentialService` | `updateCredential` | Returns result of updateCredential. | package | String userId, String password, String role, String email | `String` |
| `IUserCredentialService` | `upsertCredentialForNewEmployee` | Returns result of upsertCredentialForNewEmployee. | package | String userId, String email, String role | `String` |
| `IUserCredentialService` | `patchCredentialFromEmployee` | Returns result of patchCredentialFromEmployee. | package | String userId, String email, String role | `String` |
| `IUserCredentialService` | `deleteCredentialByUserId` | Performs deleteCredentialByUserId. | package | String userId | `void` |
| `LeaveService` | `LeaveService` | Constructs a new LeaveService instance. | public | ILeaveRequestRepository repository | `—` |
| `LeaveService` | `loadLeaveRequestsFromCSV` | Performs loadLeaveRequestsFromCSV. | public | none | `void` |
| `LeaveService` | `save` | Performs save. | private | none | `void` |
| `LeaveService` | `getAllLeaveRequests` | Returns all leave requests. | public | none | `List<LeaveRequest>` |
| `LeaveService` | `getLeaveRequestsByEmployee` | Returns leave requests by employee. | public | String employeeId | `List<LeaveRequest>` |
| `LeaveService` | `addLeaveRequest` | Performs addLeaveRequest. | public | LeaveRequest request | `void` |
| `LeaveService` | `updateLeaveRequestStatus` | Performs updateLeaveRequestStatus. | public | String employeeId, LocalDate startDate, String newStatus | `void` |
| `LeaveService` | `hasOverlappingLeaveRequest` | Returns whether OverlappingLeaveRequest. | public | String employeeId, LocalDate start, LocalDate end | `boolean` |
| `LeaveService` | `deleteLeaveRequest` | Performs deleteLeaveRequest. | public | String employeeId, LocalDate startDate | `void` |
| `PayrollProcessor` | `PayrollProcessor` | Injects repository and attendance service dependencies. Parameters are interface types (IPayrollRepository, IAttendanceService). | public | IPayrollRepository repository, IAttendanceService attendanceService | `—` |
| `PayrollProcessor` | `loadPayrollDataFromCSV` | Performs loadPayrollDataFromCSV. | public | none | `void` |
| `PayrollProcessor` | `savePayrollDataToCSV` | Performs savePayrollDataToCSV. | public | none | `void` |
| `PayrollProcessor` | `getDefaultPayrollData` | Returns default payroll data. | private static | none | `PayrollData` |
| `PayrollProcessor` | `hasPayrollRecord` | Returns whether PayrollRecord. | public | String employeeId | `boolean` |
| `PayrollProcessor` | `getPayrollData` | Returns payroll data. | public | String employeeId | `PayrollData` |
| `PayrollProcessor` | `updatePayrollData` | Performs updatePayrollData. | public | String employeeId, PayrollData data | `void` |
| `PayrollProcessor` | `removePayrollData` | Performs removePayrollData. | public | String employeeId | `void` |
| `PayrollProcessor` | `processPayroll` | Returns result of processPayroll. | public | Employee employee, String month | `PayrollResult` |
| `PayrollReport` | `format` | Returns result of format. | public static | PayrollResult r | `String` |
| `RoleGroup` | `fromRole` | Maps the role string (from user_credentials.csv) to the corresponding group. All roles not in HR, Payroll, or IT/Admin sets are NORMAL. Hides role-set details behind a single entry point. | public static | String role | `RoleGroup` |
| `UserCredentialService` | `UserCredentialService` | Constructs a new UserCredentialService instance. | public | IUserCredentialRepository repository, IAuthenticationService authenticationService | `—` |
| `UserCredentialService` | `reload` | Performs reload. | public | none | `void` |
| `UserCredentialService` | `getAllCredentials` | Returns all credentials. | public | none | `List<UserCredential>` |
| `UserCredentialService` | `getDistinctRoles` | Returns distinct roles. | public | none | `Set<String>` |
| `UserCredentialService` | `findByUserId` | Returns result of findByUserId. | public | String userId | `UserCredential` |
| `UserCredentialService` | `updateCredential` | Returns result of updateCredential. | public | String userId, String password, String role, String email | `String` |
| `UserCredentialService` | `upsertCredentialForNewEmployee` | Returns result of upsertCredentialForNewEmployee. | public | String userId, String email, String role | `String` |
| `UserCredentialService` | `patchCredentialFromEmployee` | Returns result of patchCredentialFromEmployee. | public | String userId, String email, String role | `String` |
| `UserCredentialService` | `deleteCredentialByUserId` | Performs deleteCredentialByUserId. | public | String userId | `void` |
| `UserCredentialService` | `validateUserId` | Returns result of validateUserId. | private | String userId | `String` |
| `UserCredentialService` | `persistAndReloadAuth` | Performs persistAndReloadAuth. | private | none | `void` |
| `AttendanceScreen` | `show` | Performs show. | public | JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `void` |
| `AttendanceScreen` | `showAttendanceScreen` | Performs showAttendanceScreen. | public static | JFrame parentFrame, String userId, String role, RoleGroup group, IAttendanceService attSvc, IEmployeeService empSvc | `void` |
| `AttendanceScreen` | `createMainPanel` | Returns result of createMainPanel. | private static | none | `JPanel` |
| `AttendanceScreen` | `createHeaderPanel` | Returns result of createHeaderPanel. | private static | none | `JPanel` |
| `AttendanceScreen` | `createHeaderTitleLabel` | Returns result of createHeaderTitleLabel. | private static | none | `JLabel` |
| `AttendanceScreen` | `createHeaderSubtitleLabel` | Returns result of createHeaderSubtitleLabel. | private static | none | `JLabel` |
| `AttendanceScreen` | `createContentPanel` | Returns result of createContentPanel. | private static | JFrame attendanceFrame, String userId, String role | `JPanel` |
| `AttendanceScreen` | `createInputPanel` | Returns result of createInputPanel. | private static | JFrame attendanceFrame | `JPanel` |
| `AttendanceScreen` | `createInputPanelTitleLabel` | Returns result of createInputPanelTitleLabel. | private static | none | `JLabel` |
| `AttendanceScreen` | `createFormPanel` | Returns result of createFormPanel. | private static | JFrame attendanceFrame | `JPanel` |
| `AttendanceScreen` | `createEmployeeComboBox` | Returns result of createEmployeeComboBox. | private static | none | `JComboBox<String>` |
| `AttendanceScreen` | `createDateField` | Returns result of createDateField. | private static | none | `JTextField` |
| `AttendanceScreen` | `createStatusComboBox` | Returns result of createStatusComboBox. | private static | none | `JComboBox<String>` |
| `AttendanceScreen` | `createTimeField` | Returns result of createTimeField. | private static | String defaultValue | `JTextField` |
| `AttendanceScreen` | `addFormField` | Performs addFormField. | private static | JPanel formPanel, String labelText, JComponent component, GridBagConstraints constraints, int row, int col | `void` |
| `AttendanceScreen` | `createButtonPanel` | Returns result of createButtonPanel. | private static | JFrame attendanceFrame | `JPanel` |
| `AttendanceScreen` | `createTablePanel` | Returns result of createTablePanel. | private static | none | `JPanel` |
| `AttendanceScreen` | `createTableTitleLabel` | Returns result of createTableTitleLabel. | private static | none | `JLabel` |
| `AttendanceScreen` | `createAttendanceTable` | Performs createAttendanceTable. | private static | none | `void` |
| `AttendanceScreen` | `styleAttendanceTable` | Performs styleAttendanceTable. | private static | none | `void` |
| `AttendanceScreen` | `createStyledButton` | Returns result of createStyledButton. | private static | String text, Color backgroundColor | `JButton` |
| `AttendanceScreen` | `createFooterPanel` | Returns result of createFooterPanel. | private static | none | `JPanel` |
| `AttendanceScreen` | `getEmployeeOptions` | Returns employee options. | private static | none | `List<String>` |
| `AttendanceScreen` | `updateAttendanceTable` | Performs updateAttendanceTable. | private static | none | `void` |
| `AttendanceScreen` | `isValidDateFormat` | Returns true if date is in yyyy-MM-dd format and parseable. | private static | String dateStr | `boolean` |
| `AttendanceScreen` | `parseTimeToMinutes` | Parses time string (HH:mm) to total minutes since midnight, or -1 if invalid. | private static | String timeStr | `int` |
| `AttendanceScreen` | `handleRecordAttendance` | Performs handleRecordAttendance. | private static | JFrame attendanceFrame | `void` |
| `AttendanceScreen` | `handleClearAllRecords` | Performs handleClearAllRecords. | private static | JFrame attendanceFrame | `void` |
| `AttendanceScreen` | `handleDeleteAttendanceRecord` | Performs handleDeleteAttendanceRecord. | private static | JFrame attendanceFrame | `void` |
| `AttendanceScreen` | `handleRefreshData` | Performs handleRefreshData. | private static | JFrame attendanceFrame | `void` |
| `AttendanceScreen` | `removeAttendanceRecords` | Performs removeAttendanceRecords. | public static | String employeeId | `void` |
| `BaseModuleScreen` | `createFrame` | Creates a standard module JFrame (icon, size, dispose on close). | protected static | JFrame parent, String title, int width, int height | `JFrame` |
| `BaseModuleScreen` | `createHeaderPanel` | Creates a standard header panel with title and subtitle. | protected static | String title, String subtitle | `JPanel` |
| `BaseModuleScreen` | `createFooterPanel` | Creates a simple footer panel with the given text. Subclasses may override in their own class for custom footers (e.g. gradient). | protected static | String footerText | `JPanel` |
| `EmployeeProfile` | `show` | Performs show. | public | JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `void` |
| `EmployeeProfile` | `showProfileScreen` | Performs showProfileScreen. | public static | JFrame parentFrame, String userId, String role, RoleGroup group, IEmployeeService es, IAttendanceService as, PayrollProcessor pp | `void` |
| `EmployeeProfile` | `createProfileFrame` | Creates the main profile frame. | private static | JFrame parentFrame | `JFrame` |
| `EmployeeProfile` | `createMainPanel` | Creates the main panel for the profile screen. | private static | none | `JPanel` |
| `EmployeeProfile` | `createHeaderPanel` | Creates the header panel for the profile screen. | private static | none | `JPanel` |
| `EmployeeProfile` | `createHeaderTitleLabel` | Creates the header title label. | private static | none | `JLabel` |
| `EmployeeProfile` | `createHeaderSubtitleLabel` | Creates the header subtitle label. | private static | none | `JLabel` |
| `EmployeeProfile` | `createContentPanel` | Builds main content for this module (screen-specific; not overridden from base). | private static | JFrame profileFrame, String userId, String role | `JPanel` |
| `EmployeeProfile` | `createManagementTabsPanel` | Returns result of createManagementTabsPanel. | private static | JFrame profileFrame, String userId, String role | `JTabbedPane` |
| `EmployeeProfile` | `createPersonalTabsPanel` | Returns result of createPersonalTabsPanel. | private static | Employee self | `JTabbedPane` |
| `EmployeeProfile` | `createTablePanel` | Creates the table panel for displaying employees. | private static | none | `JPanel` |
| `EmployeeProfile` | `createEmployeePayrollDataTabPanel` | Returns result of createEmployeePayrollDataTabPanel. | private static | JFrame parentFrame | `JPanel` |
| `EmployeeProfile` | `createPersonalPayrollPanel` | Returns result of createPersonalPayrollPanel. | private static | Employee self | `JPanel` |
| `EmployeeProfile` | `createTableTitleLabel` | Creates the table title label. | private static | none | `JLabel` |
| `EmployeeProfile` | `createEmployeeTable` | Creates the employee table and sets up the table model. | private static | none | `void` |
| `EmployeeProfile` | `styleEmployeeTable` | Styles the employee table for a modern appearance. | private static | none | `void` |
| `EmployeeProfile` | `styleTable` | Performs styleTable. | private static | JTable table | `void` |
| `EmployeeProfile` | `updateEmployeeTable` | Updates the employee table with the current list of employees. | private static | none | `void` |
| `EmployeeProfile` | `createPayrollDataTable` | Performs createPayrollDataTable. | private static | none | `void` |
| `EmployeeProfile` | `getPayrollTableSelectedMonth` | Returns payroll table selected month. | private static | none | `String` |
| `EmployeeProfile` | `updatePayrollDataTable` | Performs updatePayrollDataTable. | private static | none | `void` |
| `EmployeeProfile` | `handleViewEmployeeFromPayrollTab` | Performs handleViewEmployeeFromPayrollTab. | private static | JFrame parentFrame | `void` |
| `EmployeeProfile` | `handleEditPayrollFromPayrollTab` | Performs handleEditPayrollFromPayrollTab. | private static | JFrame parentFrame | `void` |
| `EmployeeProfile` | `createPersonalPayrollTable` | Performs createPersonalPayrollTable. | private static | none | `void` |
| `EmployeeProfile` | `updatePersonalPayrollTable` | Performs updatePersonalPayrollTable. | private static | Employee employee | `void` |
| `EmployeeProfile` | `createButtonPanel` | Creates the button panel with action buttons for the profile screen. | private static | JFrame frame, String userId, String role | `JPanel` |
| `EmployeeProfile` | `createModernButton` | Creates a modern styled button with custom background color. | private static | String text, Color bg | `JButton` |
| `EmployeeProfile` | `showModernMessage` | Shows a modern styled message dialog. | private static | JFrame parent, String message, String title, int messageType | `void` |
| `EmployeeProfile` | `createFooterPanel` | Creates the footer panel for the profile screen. | private static | none | `JPanel` |
| `EmployeeProfile` | `deleteEmployee` | Deletes an employee and their payroll record. Updates the table and saves changes to CSV. | private static | String empNumber | `void` |
| `EmployeeProfile` | `showEmployeeDetails` | Performs showEmployeeDetails. | private static | JFrame parentFrame, Employee employee, boolean readOnly | `void` |
| `EmployeeProfile` | `createDetailsHeaderPanel` | Creates the header panel for the employee details screen. | private static | Employee employee | `JPanel` |
| `EmployeeProfile` | `createDetailsContentPanel` | Creates the content panel for the employee details screen. | private static | JFrame detailsFrame, Employee employee, boolean readOnly | `JPanel` |
| `EmployeeProfile` | `createPersonalInfoPanel` | Creates the personal info panel for the employee details screen. | private static | Employee employee | `JPanel` |
| `EmployeeProfile` | `createSalaryComputationPanel` | Creates the salary computation panel for the employee details screen. | private static | Employee employee | `JPanel` |
| `EmployeeProfile` | `addModernDetailField` | Adds a modern styled detail field to a panel. | private static | JPanel panel, String label, String value | `void` |
| `EmployeeProfile` | `showPayrollEditDialog` | Shows the payroll edit dialog for an employee. Allows editing of salary, deductions, and allowances. | private static | JFrame parentFrame, Employee employee | `void` |
| `EmployeeProfile` | `updateSummaryArea` | Performs updateSummaryArea. | private static | JTextArea summaryArea, PayrollData record | `void` |
| `EmployeeProfile` | `initializePayrollRecord` | Seeds payroll for an employee with a default base salary and zero hourly rate until payroll staff set rates. Deduction snapshots use reference gross from hourly only (PayrollUtils#REFERENCE_PAYROLL_HOURS). | private static | String employeeId, String position, double defaultBaseSalary | `void` |
| `EmployeeProfile` | `showUpdateEmployeeForm` | Shows the update employee form for editing employee details. | private static | JFrame parentFrame, Employee employee | `void` |
| `EmployeeProfile` | `showNewEmployeeForm` | Shows the new employee form for adding a new employee. | private static | JFrame parentFrame | `void` |
| `EmployeeProfile` | `addFormField` | Adds a form field (label and input) to a panel. | private static | JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row | `void` |
| `EmployeeProfile` | `addFormField` | Performs addFormField. | private static | JPanel panel, String label, JComboBox<String> comboBox, GridBagConstraints gbc, int row | `void` |
| `EmployeeProfile` | `findEmployeeById` | Returns result of findEmployeeById. | private static | String empNumber | `Employee` |
| `EmployeeProfile` | `getAllEmployees` | Returns all employees. | public static | none | `List<Employee>` |
| `InputFilters` | `InputFilters` | Constructs a new InputFilters instance. | private | none | `—` |
| `InputFilters` | `setDigitsOnly` | Sets digits only. | public static | JTextField field | `void` |
| `InputFilters` | `setDigitsAndHyphenOnly` | Sets digits and hyphen only. | public static | JTextField field | `void` |
| `InputFilters` | `setCharactersOnlyNoDigits` | Sets characters only no digits. | public static | JTextField field | `void` |
| `InputFilters` | `applyRegexFilter` | Performs applyRegexFilter. | private static | JTextField field, Pattern allowedPattern | `void` |
| `InputFilters.RegexOnlyDocumentFilter` | `RegexOnlyDocumentFilter` | Constructs a new RegexOnlyDocumentFilter instance. | private | Pattern allowedPattern | `—` |
| `InputFilters.RegexOnlyDocumentFilter` | `insertString` | Performs insertString. | public | FilterBypass fb, int offset, String string, AttributeSet attr | `void` |
| `InputFilters.RegexOnlyDocumentFilter` | `replace` | Performs replace. | public | FilterBypass fb, int offset, int length, String text, AttributeSet attrs | `void` |
| `LeaveManagementScreen` | `show` | Performs show. | public | JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `void` |
| `LeaveManagementScreen` | `showLeaveScreen` | Performs showLeaveScreen. | public static | JFrame parentFrame, String userId, String role, RoleGroup group, ILeaveService lsvc, IEmployeeService empSvc | `void` |
| `LeaveManagementScreen` | `createContentPanel` | Returns result of createContentPanel. | private static | JFrame frame | `JPanel` |
| `LeaveManagementScreen` | `createFormPanel` | Returns result of createFormPanel. | private static | JFrame frame | `JPanel` |
| `LeaveManagementScreen` | `addRow` | Performs addRow. | private static | JPanel p, GridBagConstraints c, int row, String labelText, JComponent comp | `void` |
| `LeaveManagementScreen` | `styleField` | Performs styleField. | private static | JTextField f | `void` |
| `LeaveManagementScreen` | `createEmployeeComboBox` | Returns result of createEmployeeComboBox. | private static | none | `JComboBox<String>` |
| `LeaveManagementScreen` | `createTablePanel` | Returns result of createTablePanel. | private static | JFrame frame | `JPanel` |
| `LeaveManagementScreen` | `refreshTable` | Performs refreshTable. | private static | none | `void` |
| `LeaveManagementScreen` | `handleSubmit` | Performs handleSubmit. | private static | JFrame frame | `void` |
| `LeaveManagementScreen` | `handleUpdateStatus` | Performs handleUpdateStatus. | private static | JFrame frame | `void` |
| `LeaveManagementScreen` | `handleDeleteLeaveRequest` | Performs handleDeleteLeaveRequest. | private static | JFrame frame | `void` |
| `LeaveManagementScreen` | `createStyledButton` | Returns result of createStyledButton. | private static | String text, Color bg | `JButton` |
| `Main` | `showMainScreen` | Performs showMainScreen. | public static | String userId, String role, String email, ApplicationContext ctx | `void` |
| `Main` | `createMainFrame` | Returns result of createMainFrame. | private static | none | `JFrame` |
| `Main` | `createMainPanel` | Returns result of createMainPanel. | private static | none | `JPanel` |
| `Main` | `handleLogout` | Performs handleLogout. | private static | JFrame mainFrame | `void` |
| `Main` | `createSidebarPanel` | Returns result of createSidebarPanel. | private static | JFrame mainFrame, String userId, String role, String email, RoleGroup group, ApplicationContext ctx | `JPanel` |
| `Main` | `createSidebarButton` | Returns result of createSidebarButton. | private static | String text, ActionListener action | `JButton` |
| `Main` | `createSidebarButton` | Returns result of createSidebarButton. | private static | String text, ActionListener action, int height | `JButton` |
| `Main` | `createCollapsibleSection` | Returns result of createCollapsibleSection. | private static | String title, boolean startExpanded, JPanel contentPanel | `JPanel` |
| `Main` | `createPersonalAccountCollapsible` | Returns result of createPersonalAccountCollapsible. | private static | JFrame mainFrame, String userId, String role, ApplicationContext ctx | `JPanel` |
| `Main` | `createHrDirectivesCollapsible` | Returns result of createHrDirectivesCollapsible. | private static | JFrame mainFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `JPanel` |
| `Main` | `createPayrollDirectivesCollapsible` | Returns result of createPayrollDirectivesCollapsible. | private static | JFrame mainFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `JPanel` |
| `Main` | `createItAdminDirectivesCollapsible` | Returns result of createItAdminDirectivesCollapsible. | private static | JFrame mainFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `JPanel` |
| `Main` | `createContentPanel` | Returns result of createContentPanel. | private static | JFrame mainFrame, String userId, String role, String email, ApplicationContext ctx | `JPanel` |
| `Main` | `createContentLogoLabel` | Returns result of createContentLogoLabel. | private static | none | `JLabel` |
| `Main` | `main` | Application entry point: builds ApplicationContext (composition root / DI) then shows login after splash. | public static | String[] args | `void` |
| `ModuleScreen` | `show` | Shows this module screen; implementations obtain services from ctx. | package | JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `void` |
| `SplashScreen` | `showSplash` | Performs showSplash. | public static | Runnable onFinish | `void` |
| `SplashScreen` | `createSplashWindow` | Returns result of createSplashWindow. | private static | none | `JWindow` |
| `SplashScreen` | `createMainPanel` | Returns result of createMainPanel. | private static | none | `JPanel` |
| `SplashScreen` | `createContentPanel` | Returns result of createContentPanel. | private static | none | `JPanel` |
| `SplashScreen` | `createLogoLabel` | Returns result of createLogoLabel. | private static | none | `JLabel` |
| `SplashScreen` | `createLoadingLabel` | Returns result of createLoadingLabel. | private static | none | `JLabel` |
| `SplashScreen` | `createCloseTimer` | Returns result of createCloseTimer. | private static | JWindow splashWindow, Runnable onFinish | `Timer` |
| `User` | `showLoginScreen` | Displays the main login screen. Requires ApplicationContext for authentication (DI). | public static | JFrame parentFrame, ApplicationContext ctx | `void` |
| `User` | `createLoginFrame` | Returns result of createLoginFrame. | private static | JFrame parentFrame | `JFrame` |
| `User` | `createMainPanel` | Returns result of createMainPanel. | private static | none | `JPanel` |
| `User` | `createLeftPanel` | Returns result of createLeftPanel. | private static | none | `JPanel` |
| `User` | `createLogoLabel` | Returns result of createLogoLabel. | private static | none | `JLabel` |
| `User` | `createHeaderLabel` | Returns result of createHeaderLabel. | private static | none | `JLabel` |
| `User` | `createFooterLabel` | Returns result of createFooterLabel. | private static | none | `JLabel` |
| `User` | `createRightPanel` | Returns result of createRightPanel. | private static | JFrame loginFrame, ApplicationContext ctx | `JPanel` |
| `User` | `createTitleLabel` | Returns result of createTitleLabel. | private static | none | `JLabel` |
| `User` | `createSubtitleLabel` | Returns result of createSubtitleLabel. | private static | none | `JLabel` |
| `User` | `createFieldLabel` | Returns result of createFieldLabel. | private static | String text | `JLabel` |
| `User` | `createTextField` | Returns result of createTextField. | private static | none | `JTextField` |
| `User` | `createPasswordField` | Returns result of createPasswordField. | private static | none | `JPasswordField` |
| `User` | `createLoginButton` | Returns result of createLoginButton. | private static | JFrame loginFrame, JTextField usernameField, JPasswordField passwordField, ApplicationContext ctx | `JButton` |
| `User` | `createForgetPasswordButton` | Returns result of createForgetPasswordButton. | private static | JFrame loginFrame, ApplicationContext ctx | `JButton` |
| `User` | `authenticateUser` | Performs authenticateUser. | private static | JFrame loginFrame, JTextField usernameField, JPasswordField passwordField, ApplicationContext ctx | `void` |
| `User` | `showSendTicketDialog` | Performs showSendTicketDialog. | private static | JFrame parentFrame, ApplicationContext ctx | `void` |
| `User` | `createStyledButton` | Returns result of createStyledButton. | private static | String text, Color backgroundColor, Color foregroundColor | `JButton` |
| `UserCredentialManagementScreen` | `show` | Performs show. | public | JFrame parentFrame, String userId, String role, RoleGroup group, ApplicationContext ctx | `void` |
| `UserCredentialManagementScreen` | `createContentPanel` | Returns result of createContentPanel. | private static | JFrame frame | `JTabbedPane` |
| `UserCredentialManagementScreen` | `createCredentialsTab` | Returns result of createCredentialsTab. | private static | JFrame frame | `JPanel` |
| `UserCredentialManagementScreen` | `createTicketsTab` | Returns result of createTicketsTab. | private static | JFrame frame | `JPanel` |
| `UserCredentialManagementScreen` | `createActionButton` | Returns result of createActionButton. | private static | String text | `JButton` |
| `UserCredentialManagementScreen` | `refreshCredentialsTable` | Performs refreshCredentialsTable. | private static | none | `void` |
| `UserCredentialManagementScreen` | `refreshTicketsTable` | Performs refreshTicketsTable. | private static | none | `void` |
| `UserCredentialManagementScreen` | `showUpdateCredentialDialog` | Performs showUpdateCredentialDialog. | private static | JFrame frame | `void` |
| `UserCredentialManagementScreen` | `showUpdateTicketStatusDialog` | Performs showUpdateTicketStatusDialog. | private static | JFrame frame | `void` |
| `UserCredentialManagementScreen` | `deleteSelectedTicket` | Performs deleteSelectedTicket. | private static | JFrame frame | `void` |
| `CredentialValidationUtil` | `CredentialValidationUtil` | Constructs a new CredentialValidationUtil instance. | private | none | `—` |
| `CredentialValidationUtil` | `validateEmail` | Returns result of validateEmail. | public static | String email | `String` |
| `CredentialValidationUtil` | `validateRole` | Returns result of validateRole. | public static | String role | `String` |
| `CredentialValidationUtil` | `validateUserCredential` | Returns result of validateUserCredential. | public static | UserCredential credential | `String` |
| `EmployeeValidationUtil` | `EmployeeValidationUtil` | Constructs a new EmployeeValidationUtil instance. | private | none | `—` |
| `EmployeeValidationUtil` | `validateEmployeeNumber` | Returns result of validateEmployeeNumber. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validateSss` | Returns result of validateSss. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validatePhilHealth` | Returns result of validatePhilHealth. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validateTin` | Returns result of validateTin. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validatePagIbig` | Returns result of validatePagIbig. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validateEmail` | Returns result of validateEmail. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validatePhone` | Returns result of validatePhone. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validateStatus` | Returns result of validateStatus. | public static | String value | `String` |
| `EmployeeValidationUtil` | `validateCharactersOnly` | Validates character-only fields: allows letters, spaces, and selected punctuation; blocks digits. Empty values are treated as valid to preserve existing behavior. | public static | String value, String fieldName | `String` |
| `EmployeeValidationUtil` | `validateEmployee` | Validates all required and format fields of an employee. Returns null if the employee is valid; otherwise the first validation error message. Used by the model layer (Employee.isValid) and for consistent validation at persistence boundaries. | public static | Employee emp | `String` |
| `PayrollUtils` | `PayrollUtils` | Constructs a new PayrollUtils instance. | private | none | `—` |
| `PayrollUtils` | `calculateSSSAmount` | Returns result of calculateSSSAmount. | public static | double baseSalary | `double` |
| `PayrollUtils` | `calculatePhilHealthAmount` | Returns result of calculatePhilHealthAmount. | public static | double baseSalary | `double` |
| `PayrollUtils` | `calculatePagIbigAmount` | Returns result of calculatePagIbigAmount. | public static | double baseSalary | `double` |
| `PayrollUtils` | `calculateWithholdingTax` | Returns result of calculateWithholdingTax. | public static | double baseSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance | `double` |
| `TableColumnSortUtil` | `TableColumnSortUtil` | Constructs a new TableColumnSortUtil instance. | private | none | `—` |
| `TableColumnSortUtil` | `install` | Attaches a TableRowSorter so only one column can be sorted; toggles asc/desc on header clicks. | public static | JTable table, TableModel model, Set<Integer> numericColumnIndices | `void` |
| `TableColumnSortUtil` | `parseNumeric` | Parses a cell for numeric sorting. Commas stripped. Invalid/blank maps to Double#POSITIVE_INFINITY so those rows sort last when ascending (and first when descending). | private static | Object o | `double` |
| `ApplicationContext.ItTicketRepositoryFallback` | `load` | Implements IItTicketRepository.load (no-op empty list). | public | none | `List<ItTicket>` |
| `ApplicationContext.ItTicketRepositoryFallback` | `save` | Implements IItTicketRepository.save (no-op). | public | `List<ItTicket> items` | `void` |
