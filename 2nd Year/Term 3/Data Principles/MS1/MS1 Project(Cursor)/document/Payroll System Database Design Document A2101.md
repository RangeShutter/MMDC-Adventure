# 

# 

# 

# Payroll System Database Design Document 

Prepared and Presented by:

***Charlize Bactong***   
***Colin Bactong | Robert Cery Amisola***   
***Angelica Calipayan | Chelsie Mae Ricafrente***  
*Bachelor of Science in Information Technology*  
*Term 2 A.Y. 2025-2026*

# **Intellectual Property Notice**

This template is an exclusive property of **Mapua-Malayan Digital College** and is protected under **Republic Act No. 8293**, also known as the *Intellectual Property Code of the Philippines* (IP Code). It is provided solely for educational purposes within this course. Students may use this template to complete their tasks, but may not **modify, distribute, sell, upload,** or **claim ownership** of the template itself. Such actions constitute copyright infringement under **Sections 172, 177, and 216** of the IP Code and may result in legal consequences. Unauthorized use beyond this course may result in legal or academic consequences.

Additionally, students must comply with the **Mapua-Malayan Digital College Student Handbook**, particularly with the following provisions:

* **Offenses Related to MMDC IT**:  
  * **Section 6.2** – Unauthorized copying of files  
  * **Section 6.8** – Extraction of protected, copyrighted, and/or confidential information by electronic means using MMDC IT infrastructure  
* **Offenses Related to MMDC Admin, IT, and Operations**:  
  * **Section 4.5** – Unauthorized collection or extraction of money, checks, or other instruments of monetary equivalent in connection with matters pertaining to MMDC

Violations of these policies may result in **disciplinary actions ranging from suspension to dismissal**, in accordance with the Student Handbook.

For permissions or inquiries, please contact MMDC-ISD at [isd@mmdc.mcl.edu.ph](mailto:isd@mmdc.mcl.edu.ph). 

# 

# **TABLE OF CONTENTS**

**Section**  
     1 		[**Introduction**](#introduction)  
     		[Purpose of the Document](#purpose-of-the-document)  
     		[Project Overview](#project-overview)  
     		[Scope of the Database](#scope-of-the-database)  
		[Definition of Terms](#definition-of-terms)  
       2 		[**Database Requirements**](#database-requirements)  
      		[Data Flow Diagram](#heading=h.f3xlz96ajyaa)  
      		[Data Volume](#data-volume)  
      		[Data Types](#data-types)  
      		[Data Constraints and Rules](#data-constraints-and-rules)  
     3 		[**Data Dictionary**](#data-dictionary)  
     4		[**Data Modeling**](#data-modeling)  
[Conceptual Diagram](#conceptual-diagram)  
[Schema Diagram](#schema-diagram)  
     5 		[**Normalization**](#normalization)  
[Explanation of the Normalization Process](#heading=h.s1b8y0t6m9wl)  
[Description of the Normalized Data Model](#heading=h.jz8zphhkidc5)  
[Key Decisions](#key-decisions)  
[Considerations and Limitations](#considerations-and-limitations)  
     6		[**Database Schema and Data Integrity**](#database-schema-and-data-integrity)  
[Primary Keys](#heading=h.n4yjzb3clvn2)  
[Foreign Keys](#heading=h.obc4q5k0lsue)  
[Constraints](#heading=h.x11yvjy2qex8)  
     7		[**Data Migration Plan**](#data-migration-plan)  
[Steps for Data Migration](#data-migration-plan)  
[Data Transformation and Cleaning](#data-transformation-and-cleaning)  
     8		[Test Scripts and validation](#test-scripts-and-validation)  
     9 		[**Security Access and Control**](#security-access-and-control)  
[User Roles](#user-roles-the-motorph-payroll-system-database-implements-role-based-access-control-\(rbac\)-to-manage-system-access.-each-user-is-assigned-one-or-more-roles-that-define-their-permitted-actions-within-the-system.)  
[Encryption](#9.2-encryption)  
   10 		[**User Manual**](#user-manual)  
[**Reference**](#heading=h.qvv8dw9us9q7)

1. # **Introduction**  {#introduction}

   The MotorPH Payroll System Database is developed to support the company’s payroll operations by storing and managing employee, salary, attendance, and payroll information in a centralized system. The database serves as the foundation of the payroll system, ensuring that all payroll-related data is organized, accurate, and easily accessible to authorized users.

   

   The system is designed to support MotorPH’s human resource and finance operations by enabling efficient salary computation, payroll generation, and employee record management in accordance with the company’s Business Requirement Specification Document and business profile.

   

   ## **Purpose of the Document** {#purpose-of-the-document}

     
   The database is necessary to ensure accurate, secure, and efficient payroll processing for MotorPH employees. It supports the project’s objectives by:

* Providing a centralized storage for employee and payroll data  
* Supporting payroll computation based on attendance, salary, and deductions  
* Reducing manual errors in salary processing  
* Ensuring data consistency, integrity, and security  
* Allowing authorized departments to access payroll information based on their roles


  Overall, the database helps MotorPH manage payroll operations more effectively while maintaining compliance with company policies and payroll standards.


  ## **Project Overview** {#project-overview}


  The MotorPH Payroll System Database is a structured and normalized relational database designed to support secure, scalable, and accurate payroll data management. The system implements Third Normal Form (3NF) to eliminate redundancy and maintain data integrity across core modules including employee management, attendance tracking, payroll records, leave management, and role-based access control (RBAC). The database design incorporates primary keys (PK), foreign keys (FK), unique constraints, and check constraints to enforce referential integrity and maintain consistency of business rules across related tables.


  The database serves as the centralized repository for payroll-related information, providing secure data storage and controlled system access through RBAC. Associative entities such as UserRole and RolePermission enable flexible assignment of roles and permissions, while lookup tables such as LeaveType and EmploymentStatus ensure domain consistency by restricting categorical values to predefined options. These structures help maintain reliable relationships between entities and prevent invalid or inconsistent data entries.


  **Tools and Technologies Used**


  The development of the MotorPH Payroll System Database utilized the following tools:


  • MySQL – for relational database management  

  • MySQL Workbench – for database design and schema visualization  

  • Draw.io / Lucidchart – for ERD and Data Flow Diagram creation  

   


  These tools enabled efficient database modeling, implementation, testing, and documentation throughout the project lifecycle.

  **Database Management System**

  **Selected DBMS: MySQL**  
  The MotorPH Payroll System Database will be implemented using MySQL, a widely used open- source relational database management system (RDBMS). MySQL was selected because it provides reliable performance, strong support for relational database structures, and efficient management of structured data. Since the MotorPH Payroll System relied on the relational tables such as employee, payroll, attendance and department MySQL provides the necessary features to maintain relationships and enforce data integrity through primary keys, foreign keys, and constraints. 

  MySQL also supports indexing, transactions, and scalability, making it suitable for systems that manage growing datasets such as payroll records, attendance logs, and employee information. In addition, MySQL integrates easily with many application frameworks and web technologies, which makes it a practical choice for developing enterprise systems such as payroll management platforms. 

  **Rationale for Choosing MySQL**  
  MySQL was selected as the DBMS for the MotorPH Payroll System for the following reasons: 

  *1\.  Reliability and Stability*   
  MySQL is widely used in enterprise applications and is known for its stability and reliability when managing large amounts of transactional data.

  *2\. Data Integrity Support*  
  MySQL enforces referential integrity through constraints such as primary keys, foreign keys, unique constraints, and check constraints. These mechanisms help maintain accurate payroll records and prevent invalid data entries. 

  *3\. Scalability*   
  The MotorPH database is expected to grow as employee records, attendance logs, and payroll data accumulate. MySQL supports scalable architecture capable of handling increasing data volumes efficiency.

  *4\. Cost Efficiency*   
  As an open-source DBMS, MySQL reduces operational costs while still offering robust database management features.

  *5\. Strong Community and Documentation* 

  MySQL has extensive community support and documentation, which helps developers troubleshoot issues and maintain the database system effectively. 


  MySQL versions prior to 8.0.16 did not enforce CHECK constraints. The MotorPH database assumes a modern MySQL version where CHECK constraints are supported and enforced.

  ## **Scope of the Database** {#scope-of-the-database}


  The scope of the database includes employee master data management, attendance recording, payroll storage, leave management, role-based access control, and the preservation of historical records for auditing and reporting purposes. However, the database does not perform payroll computation logic or financial transaction processing. Detailed breakdown of deductions (e.g., SSS, tax, and other statutory contributions) is handled at the application layer and is not stored in the database. The system stores only GrossPay and NetPay to maintain a simplified and normalized structure. The design also supports projected annual data growth of approximately 30 percent while maintaining performance and structural stability through a normalized schema, constraint-based validation, and scalable relational architecture.

  

  ## **Definition of Terms** {#definition-of-terms}


| Term | Definition |
| ----- | ----- |
| **ERD** | Entity Relationship Diagram, a visual representation of database entities and relationships |
| **PK** | Primary Key, a unique identifier for each record in a table |
| **FK** | Foreign Key, a field that links one table to another |
| **Payroll** | Process of calculating and recording employee salaries |
| **Attendance** | Record of employee working hours used for payroll |
| **BSRD** | Business Requirement Specification Document |
| **SSS** | Social Security System |
| **PhilHealth** | Philippine Health Insurance Corporation |
| **Pag-IBIG** | Home Development Mutual Fund |
| **Gross Pay** | Total earnings before deductions |
| **Net Pay** | Earnings after deductions |
| **Database** | Structured collection of data stored electronically |

2. # **Database Requirements** {#database-requirements}

     
   **Data Entities**

     
* **Department**

  The Department entity stores information relating to the departments within MotorPH. This helps organize employees based on their assigned department and supports reporting by department. Each department is managed by one employee. A department can have multiple employees assigned to it.	

  Key Attributes: DepartmentID (primary key), DepartmentName, ManagerID (foreign key referencing Employee)

* **Employee**

  This entity stores personal and employee-related information of MotorPH employees. Each employee belongs to one department and acts as the parent entity for salary, attendance, payroll, and leave records.

  Key Attributes: EmployeeID (primary key), FirstName, LastName, DateOfBirth, Address, ContactNumber, Position, DepartmentID (Foreign Key)

* **Salary**

  This stores employees’ salary history for employees. An employee may have multiple salary records over time to support promotions, salary adjustments, and historical payroll accuracy.

  Key Attributes: SalaryID (primary key), EmployeeID (foreign key). BasicSalary, PayFrequency, EffectiveFrom, EffectiveTo

* **Attendance**

  The Attendance entity records daily time-in and time-out of employees. Attendance stores only raw time data. Computed values such as total hours are derived during payroll processing to avoid redundancy.

  Key Attributes: AttendanceID (primary key), EmployeeID (foreign key), Date, TimeIn, TimeOut

* **Overtime**

  Tracks overtime hours associated with attendance records. Overtime is separated from attendance to allow flexible computation and proper normalization.

  Key Attributes: OvertimeID (Primary Key), AttendanceID (Foreign Key), Hours

* **Leave**

  Stores employee leave applications and records. Leave records are used during payroll computation to adjust payable days and deductions.

  Key Attributes: LeaveID (Primary Key), EmployeeID (Foreign Key), LeaveType, StartDate, EndDate

* **Payroll**

  This stores payroll computation results for each employee per pay period. Each payroll record corresponds to one employee and one pay period. Payroll records are unique per employee per pay period.

  Key Attributes: PayrollID (primary key), EmployeeID (foreign key), PayPeriodStart, PayPeriodEnd, GrossPay, NetPay

* **Payslip**

  Stores payslip issuance information. Each payroll record generates exactly one payslip. The payslip serves as a presentation layer and does not duplicate payroll totals.

  Key Attributes: PayslipID (primary key), PayrollID (foreign key), IssueDate

* **UserAccount**

  The UserAccount entity stores system login credentials for system access. User accounts may be linked to employees or exist independently for system administrators. Each employee may have at most one user account.

  Key Attributes: UserID (primary key), EmployeeID (foreign key), Username, PasswordHash

* **Permission**

  The Permission entity defines specific system access rights that control allowed operations within the payroll system. This enables granular and auditable role-based access control at the database level.

  Key Attributes: PermissionID (Primary Key), PermissionName, Description

* **RolePermission**

  The RolePermission entity is an associative table linking roles to permissions. It allows roles to be assigned multiple permissions and ensures scalable access management.

  Key Attributes: RolePermissionID (Primary Key), RoleID (Foreign Key), PermissionID (Foreign Key)

* **EmploymentStatus**

  The EmploymentStatus entity stores predefined employment states to enforce data consistency and normalization. Each employee record references a valid employment status (Active, Resigned, Terminated, etc.)

  Key Attributes: StatusID (Primary Key), StatusName

* **LeaveType**

  The LeaveType entity stores standardized leave categories to ensure consistent classification of employee leave records. Each leave record references a valid leave type (Sick leave, vacation leave, etc.)

  Key Attributes: Leav

  ## **DFD to Database Schema Mapping**

  To ensure consistency between system processes and database design, each major process in the Data Flow Diagram is directly mapped to specific database tables, entities, and relational structures within the MotorPH Payroll System Database.

  ## **Process 4.2 – Retrieve Attendance and Overtime Records**

  This process retrieves employee attendance and overtime information required for payroll computation. The process corresponds to the Attendance and Overtime entities, which are directly linked to the Employee entity through foreign key relationships.

  Relationships:

* Employee.EmployeeID → Attendance.EmployeeID  
* Attendance.AttendanceID → Overtime.AttendanceID

  The Attendance table stores employee time-in and time-out records, while the Overtime table stores additional work hours beyond regular schedules. These entities support accurate payroll computation and attendance monitoring.

  The process may also retrieve employee address and government-related information when validating employee records during payroll generation.

  Additional Relationships:

* Employee.EmployeeID → EmployeeAddress.EmployeeID  
* Employee.EmployeeID → GovernmentID.EmployeeID

  The EmployeeAddress entity stores employee residential information, while the GovernmentID entity stores statutory identifiers such as SSS, PhilHealth, TIN, and Pag-IBIG numbers.

  ## **Process 4.3 – Calculate Gross Pay**

  This process integrates multiple entities to compute employee gross salary and compensation details.

  The process utilizes the following entities:

* Employee – employee information  
* Salary – base salary information  
* Attendance – worked hours  
* Overtime – overtime hours and rates  
* Benefit – employee benefits and allowances  
* Deduction – payroll deductions  
  Relationships:  
* Salary.EmployeeID → Employee.EmployeeID  
* Attendance.EmployeeID → Employee.EmployeeID  
* Overtime.AttendanceID → Attendance.AttendanceID  
* Benefit.EmployeeID → Employee.EmployeeID  
  During payroll processing, the system retrieves salary information, attendance records, overtime data, and employee benefits to calculate the employee’s gross pay.  
  The computed payroll result is stored in:  
* Payroll.EmployeeID → Employee.EmployeeID  
  After payroll generation, deduction records associated with the payroll transaction are stored through:  
* Deduction.PayrollID → Payroll.PayrollID  
  The Deduction entity stores payroll deductions such as tax, government contributions, and other salary adjustments linked to each payroll record.

  ## **Process 4.4 – Generate Payroll Record**

  This process corresponds to the Payroll entity, which stores generated payroll transactions for employees during a specific pay period.  
  Relationship:  
* Payroll.EmployeeID → Employee.EmployeeID  
  The Payroll table stores salary results including GrossPay and NetPay. Payroll records are associated with valid employees through foreign key constraints, ensuring relational consistency and reliable payroll history tracking.  
  The payroll generation process may also retrieve employee government identifiers from the GovernmentID entity to support payroll documentation and statutory contribution references.  
  Additional Relationship:  
* GovernmentID.EmployeeID → Employee.EmployeeID  
  This integration supports compliance with payroll-related government requirements such as SSS, PhilHealth, TIN, and Pag-IBIG references.

  ## **Process 4.5 – Generate Payslip**

  This process corresponds to the Payslip entity, which stores official payroll documents issued to employees.  
  Relationship:  
* Payslip.PayrollID → Payroll.PayrollID  
  Each payroll transaction generates a corresponding payslip that contains the employee’s computed salary information for a specific payroll period.  
  The generated payslip may also include information retrieved from related entities such as:  
* Employee  
* EmployeeAddress  
* GovernmentID  
* Benefit  
* Deduction  
  These entities provide complete employee details, address information, statutory identification numbers, employee benefits, and payroll deductions required for comprehensive payroll documentation.  
  Additional Relationships:  
* Employee.EmployeeID → EmployeeAddress.EmployeeID  
* Employee.EmployeeID → GovernmentID.EmployeeID  
* Benefit.EmployeeID → Employee.EmployeeID  
* Deduction.PayrollID → Payroll.PayrollID  
  Overall, the Level 2 Data Flow Diagram demonstrates how data flows between subprocesses, entities, data stores, and external users to support attendance tracking, overtime processing, leave approval, payroll generation, benefit management, deduction processing, employee record management, and secure payroll documentation within the MotorPH Payroll System Database.

  ## **Data Volume** {#data-volume}

    
  The Data Volume section of the database design document provides an estimate of the amount of data that will be stored, processed, and maintained by the MotorPH Payroll System database. Understanding the expected data volume helps designers determine appropriate storage requirements, performance considerations, and scalability planning for the database system. By estimating the amount of data that will flow through the system, the database can be structured to support both current operational needs and future growth.  
  To estimate the data volume, several types of data must be quantified. These include employee records, attendance entries, payroll records, salary history, leave requests, and user account data. For example, the organization may initially store several hundred employee records, while attendance data may generate multiple records per employee each month due to daily time-in and time-out tracking. Payroll records will also accumulate over time as each employee receives payroll entries for every pay period. These datasets collectively contribute to the overall data volume handled by the system.  
  The database is expected to experience steady growth over time as the organization expands and more employee records and payroll transactions are generated. Based on projected operational growth, the system may experience an annual data growth rate of approximately 20–30 percent, primarily due to increasing attendance logs, payroll histories, and leave records. This growth trend requires the database structure to support scalable storage and efficient data retrieval.  
  Increasing data volume may impact both storage requirements and database performance. As more records are added, query operations such as payroll reporting, attendance analysis, and employee record retrieval may require additional processing time. To maintain system performance, the database design incorporates normalized tables, indexing strategies, and efficient relational structures that reduce redundancy and improve query efficiency.  
  To manage the expansion of data over time, several database management procedures and tools will be utilized. These include database indexing to improve query performance, periodic database maintenance and optimization, data archiving strategies for older payroll records, and monitoring tools provided by the database management system (MySQL). These mechanisms ensure that the MotorPH Payroll System database remains reliable, scalable, and capable of supporting long-term operational requirements.

##  	**Data Types** {#data-types}

The MotorPH Payroll System Database utilizes appropriate data types to ensure efficient data storage, accuracy, consistency, and scalability across all entities within the system. Each attribute is assigned a suitable data type based on the nature of the data it stores and the operational requirements of the payroll system.

Numeric identifiers such as EmployeeID, DepartmentID, PayrollID, SalaryID, BenefitID, GovernmentID, and AddressID use the INT data type. These attributes serve as primary keys and foreign keys that uniquely identify records and establish relationships between tables. Using INT supports efficient indexing, faster query processing, and optimized relational mapping across the database.

Text-based attributes such as employee names, department names, usernames, positions, benefit types, and government identification numbers are stored using the VARCHAR data type with appropriate length limitations. Examples include FirstName VARCHAR(100), DepartmentName VARCHAR(100), and SSSNumber VARCHAR(20). This data type provides flexibility for storing variable-length text while minimizing unnecessary storage allocation.

The newly added EmployeeAddress entity uses VARCHAR data types for address-related information such as StreetName, Barangay, City, Province, and ZIPCode. Separating address details into a dedicated entity improves normalization and supports future scalability for address management.

Date-related attributes such as DateOfBirth, PayPeriodStart, PayPeriodEnd, EffectiveFrom, EffectiveTo, StartDate, and EndDate utilize the DATE data type to accurately represent calendar-based values required for payroll scheduling, leave management, and salary tracking.

Time-sensitive attributes such as TimeIn, TimeOut, and ApprovalDate use the TIMESTAMP data type to capture precise date and time information. This is essential for attendance monitoring, overtime tracking, approval workflows, and auditing purposes.

Financial values such as BaseSalary, GrossPay, NetPay, Benefit Amounts, and Overtime Hours are stored using the DECIMAL(p,s) data type, specifically DECIMAL(12,2), to maintain monetary precision and prevent rounding errors. This ensures accurate payroll computation and reliable financial reporting.

The GovernmentID entity stores employee government-related identifiers such as SSSNumber, PhilHealthNumber, TINNumber, and PagIBIGNumber using VARCHAR data types because these identifiers may contain leading zeros and alphanumeric formatting.

By selecting appropriate data types for each entity and attribute, the MotorPH Payroll System Database ensures efficient storage allocation, improved query performance, accurate data representation, and long-term scalability for payroll operations.

##  	**Data Constraints and Rules**  {#data-constraints-and-rules}

To maintain data integrity, reliability, consistency, and security, the MotorPH Payroll System Database implements a comprehensive set of constraints and business rules across all entities. These mechanisms ensure that only valid and consistent data can be stored within the database.

Primary Key Constraints are implemented in every entity to uniquely identify each record. Examples include EmployeeID in Employee, PayrollID in Payroll, SalaryID in Salary, BenefitID in Benefit, GovernmentID in GovernmentID, and AddressID in EmployeeAddress. These constraints prevent duplicate records and ensure entity uniqueness throughout the system.

Foreign Key Constraints establish and enforce relationships between related entities. For example, EmployeeID in the Salary, Payroll, Attendance, Benefit, GovernmentID, and EmployeeAddress tables references Employee(EmployeeID). These relationships ensure referential integrity by preventing orphan records and ensuring that dependent records always correspond to valid employees.

The Department table contains ManagerID as a foreign key referencing Employee(EmployeeID), ensuring that only existing employees can be assigned as department managers. Similarly, Leave records reference LeaveType and ApprovalStatus, while Overtime records reference Attendance and ApprovalStatus.

Unique Constraints are applied to fields that require non-duplicate values. Examples include:

* UNIQUE(ContactNumber) in the Employee table  
* UNIQUE(Username) in the UserAccount table  
* UNIQUE(SSSNumber)  
* UNIQUE(TINNumber)  
* UNIQUE(PhilHealthNumber)  
* UNIQUE(PagIBIGNumber)  
  These constraints prevent duplicate employee registrations and duplicate government identifiers.  
  Composite Unique Constraints are implemented in the Payroll table using:  
  (EmployeeID, PayPeriodStart, PayPeriodEnd)  
  This ensures that employees cannot have duplicate payroll records for the same payroll period.  
  Check Constraints enforce valid data ranges and logical conditions. Examples include:  
* CHECK(BaseSalary \>= 0\)  
* CHECK(Amount \>= 0\)  
* CHECK(GrossPay \>= 0\)  
* CHECK(NetPay \>= 0\)  
* CHECK(TimeOut \>= TimeIn)  
* CHECK(EndDate \>= StartDate)  
  These constraints prevent invalid salary amounts, incorrect attendance times, and illogical leave schedules from being stored in the database.  
  NOT NULL Constraints are applied to mandatory attributes such as employee names, department names, usernames, and positions to ensure that essential business information is always provided.  
  Business rules are also implemented to support organizational policies and system logic. Each employee may only have one user account, one government identification profile, and one primary address record. Lookup entities such as LeaveType, EmploymentStatus, and ApprovalStatus restrict values to predefined categories, ensuring domain consistency and preventing invalid classifications.  
  The database also follows Third Normal Form (3NF), which reduces redundancy by separating employee information, salary records, benefits, government IDs, and address details into dedicated entities. This normalization approach improves scalability, simplifies maintenance, and minimizes update anomalies.  
  Overall, these constraints and business rules ensure that the MotorPH Payroll System Database maintains high data quality, secure relational consistency, efficient storage management, and reliable payroll operations.

  The Deduction entity is linked to the Payroll table through PayrollID as a foreign key. This relationship ensures that deduction records cannot exist without a valid payroll transaction. Deduction amounts use the DECIMAL(12,2) data type with CHECK(Amount \>= 0\) constraints to prevent negative deduction values from being stored.  
  The Payroll entity enforces CHECK(GrossPay \>= 0\) and CHECK(NetPay \>= 0\) constraints to ensure that payroll computations always contain valid financial values. In addition, composite unique constraints on (EmployeeID, PayPeriodStart, PayPeriodEnd) prevent duplicate payroll records for the same employee and payroll period.  
  The Attendance entity applies CHECK(TimeOut \>= TimeIn) constraints to ensure logical attendance entries. Attendance records are connected to Employee through EmployeeID, guaranteeing that attendance logs are always associated with valid employees.  
  The Overtime entity references Attendance through AttendanceID and ApprovalStatus through ApprovalStatusID. This ensures that overtime requests are tied to valid attendance records and follow predefined approval workflows. Overtime Hours and Rate attributes use DECIMAL data types with non-negative validation constraints to maintain accurate overtime compensation values.  
  The Leave entity references Employee, LeaveType, and ApprovalStatus using foreign keys. CHECK(EndDate \>= StartDate) constraints ensure that leave requests maintain valid date ranges. The ApprovedBy attribute references Employee(EmployeeID), ensuring that only valid employees can act as approvers for leave requests.  
  The LeaveType and ApprovalStatus entities function as lookup tables that restrict categorical values to predefined options. This prevents inconsistent leave classifications and invalid approval states from being entered into the database.  
  The Role-Based Access Control (RBAC) module implements additional integrity constraints to maintain secure system access. The UserAccount table enforces UNIQUE(Username) constraints to prevent duplicate login credentials. PasswordHash is defined as NOT NULL to ensure that all accounts contain secure authentication data.  
  The UserRole associative entity resolves the many-to-many relationship between UserAccount and Role. Composite unique constraints such as UNIQUE(UserID, RoleID) prevent duplicate role assignments for the same user.  
  Similarly, the RolePermission associative entity resolves the many-to-many relationship between Role and Permission. Composite unique constraints such as UNIQUE(RoleID, PermissionID) prevent duplicate permission assignments within the RBAC structure.  
  The database also enforces business rules stating that:  
* Each employee may only have one user account  
* Payroll records must belong to valid employees  
* Overtime and leave requests must follow approval workflows  
* Government identification numbers must remain unique  
* Salary, benefit, deduction, and overtime values cannot be negative  
  These additional constraints and business rules improve security, maintain relational consistency, prevent invalid transactions, and ensure reliable payroll system operations throughout the MotorPH Payroll System Database.


3. # **Data Dictionary** {#data-dictionary}

   This section provides detailed information about the data elements, tables, and columns that make up the database.

   1. ## **Employee Table**

      ##        The Employee table stores the core information about each employee working in MotorPH. It contains personal and employment details such as the employee’s name, date of birth, contact information, job position, department assignment, and employment status. This table serves as the central entity of the payroll system since most other tables reference employee records.

      ## The Employee table is linked to several other tables through foreign key relationships, including Payroll, Attendance, Leave, Salary, and UserAccount. These relationships ensure that employee-related transactions such as payroll processing, attendance tracking, and leave management are always associated with a valid employee record. Referential integrity ensures that records in dependent tables cannot exist without a corresponding employee entry.

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| EmployeeID | INT | Unique identifier of each employee |
| FirstName | VARCHAR(100) | Employee’s first name |
| LastName | VARCHAR(100) | Employee’s last name |
| DateOfBirth | DATE | Employee birth date |
| Address | VARCHAR(255) | Residential address |
| ContactNumber | VARCHAR(20) | Employee contact number |
| Position | VARCHAR(100) | Employee job title |
| DepartmentID | INT | References employee’s department |
| StatusID | INT | References employment status |

      2. **Constraints**

| Primary Key | EmployeeID |
| :---- | :---- |
| **Foreign Key** | DepartmentID → Department(DepartmentID); StatusID → EmploymentStatus(StatusID) |
| **Unique Constraint** | ContactNumber (optional) |
| **Check Constraint** | DateOfBirth ≤ CURRENT\_DATE |

   2. ## **EmploymentStatus**

      ## 

      ## The EmploymentStatus table is a lookup table that defines the possible employment classifications within MotorPH. It stores predefined values such as Active, Resigned, or Terminated to ensure that employee status information remains consistent across the database.

      The Employee table references the EmploymentStatus table through the StatusID foreign key. This relationship ensures that employees can only be assigned valid employment status values defined in the system. Using a lookup table also prevents inconsistent data entry and simplifies future updates if new employment status categories are added.

      

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| StatusID | INT | Unique identifier of employment status |
| StatusName | VARCHAR(50) | Name of employment status |

      2. **Constraints**

| Primary Key | StatusID |
| :---- | :---- |
| **Foreign Key** | None |
| **Unique Constraint** | StatusName |
| **Check Constraint** | None |

   3. ## **Department**

        
      The Department table stores information about the different departments within the MotorPH organization. Each record represents a specific department and contains attributes such as the department name and description.

      Employees are assigned to departments through the DepartmentID foreign key in the Employee table. This structure allows the system to organize employees according to the company’s organizational structure. It also enables department-based reporting, payroll summaries, and workforce management analytics.

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| DepartmentID | INT | Unique identifier of each department |
| DepartmentName | VARCHAR(100) | Name of the department |
| ManagerID | INT | References Employee who manages the department |

      

      2. **Constraints**

| Primary Key | DepartmentID |
| :---- | :---- |
| **Foreign Key** | ManagerID → Employee(EmployeeID) |
| **Unique Constraint** | DepartmentName |
| **Check Constraint** | None |

   4. ## **Benefit**

      The Benefit entity is used to store employee compensation benefits and allowances provided by the company in addition to the employee’s base salary. It manages benefit-related information such as incentives, bonuses, phone allowances, meal allowances, benefits (PhilHealth) and (SSS), and other employee compensation packages. 

      The Benefit table is linked to the Employee entity through EmployeeID, ensuring that each benefit record corresponds to a valid employee. This entity supports payroll processing by allowing employee benefits to be included during salary computation and payroll generation.

      1. **Columns**

      

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| BenefitID | INT | Unique identifier of each benefit |
| EmployeeID | INT | Foreign Key; references Employee(EmployeeID) to associate benefits with employees  |
| BenefitType | VARCHAR(100) | Stores the type or category of employee benefit  |
| Amount | DECIMAL | Stores the monetary value of the employee benefit  |

      

      2. **Constraints**

| Primary Key | BenefitID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) |
| **Unique Constraint** | None |
| **Check Constraint** | Amount ≥ 0  |

   5. ## **EmployeeAddress**        The EmployeeAddress entity is used to store the residential address information of employees separately from the Employee table. It manages detailed location data such as street name, barangay, city, province, and ZIP code to maintain organized and normalized employee records.         This entity is linked to the Employee table through EmployeeID, ensuring that each address record belongs to a valid employee. Separating address information into its own entity improves data organization, reduces redundancy, and supports future scalability for employee address management within the payroll system.

      ## 

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| AddressID | INT | Primary Key; uniquely identifies each employee address record  |
| EmployeeID | INT | Foreign Key; references the Employee table |
| StreetName | VARCHAR(100) | Stores the street name and house/building details of the employee  |
| Barangay | VARCHAR(100) | Stores the barangay information of the employee address  |
| City | VARCHAR(100) | Stores the city or municipality of the employee  |
| Province | VARCHAR(100) | Stores the province of the employee  |
| ZIPCode | VARCHAR(10) | Stores the postal or ZIP code of the employee address  |

      

      2. **Constraints**

| Primary Key | AddressID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) |
| **Unique Constraint** | EmployeeID  |
| **Check Constraint** | None  |

   6. ## **GovernmentID**        The GovernmentID entity is used to store employee government-issued identification numbers required for payroll processing, statutory compliance, and employee verification. It manages important government records such as SSS, PhilHealth, TIN, and Pag-IBIG numbers separately from the Employee table to improve data organization and security.         This entity is linked to the Employee table through EmployeeID, ensuring that each government identification record corresponds to a valid employee. Separating government-related information into its own entity supports normalization, reduces redundancy, and simplifies the management of payroll-related statutory records within the MotorPH Payroll System.  

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| GovernmentID | INT | Primary Key; uniquely identifies each government identification record Unique identifier of each department |
| EmployeeID | INT | Foreign key; references the Employee record |
| SSSNumber | VARCHAR(20) | Stores the employee’s Social Security System (SSS) number  |
| PhilhealthNumber | VARCHAR(20) | Stores the employee’s PhilHealth identification number  |
| TINNumber | VARCHAR(20) | Stores the employee’s PhilHealth identification number  |
| PagIBIGNumber | VARCHAR(20) | Stores the employee’s Pag-IBIG membership number |

      

      2. **Constraints**

| Primary Key | GovernmentID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) |
| **Unique Constraint** | EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber  |
| **Check Constraint** | None |

      

   7. ## **Leave**

        
      The Leave table references LeaveType through the LeaveTypeID foreign key to categorize each leave record. This ensures that leave applications follow predefined categories and prevents inconsistent data entry. It also enables the system to generate leave reports based on specific leave types.

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| LeaveID | INT | Unique identifier of leave record |
| EmployeeID | INT | References Employee |
| LeaveTypeID | INT | References leave category |
| StartDate | DATE | Leave start date |
| EndDate | DATE | Leave end date |
| ApprovedBy | INT | References the Employee table for the employee who approves the leave |
| ApprovalStatusID | INT | References the ApprovalStatus table  |
| ApprovalDate | TIMESTAMP | Date and time when the leave is approved |

      

      2. **Constraints**

| Primary Key | LeaveID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) LeaveTypeID → LeaveType(LeaveTypeID) ApprovedBy → Employee(EmployeeID) ApprovalStatusID → ApprovalStatus(ApprovalStatusID) |
| **Unique Constraint** | None |
| **Check Constraint** | EndDate ≥ StartDate |

         

   8. ## **LeaveType**

        
      The LeaveType table is a lookup table that stores the different categories of leave available to employees within the MotorPH organization. Examples of these categories include Sick Leave, Vacation Leave, and Emergency Leave. Each leave type is identified by a unique identifier and a descriptive name.  
        
      The Leave table references the LeaveType table through the LeaveTypeID foreign key. This relationship ensures that all leave records follow predefined leave categories, preventing inconsistent or invalid leave classifications. Using a lookup table also improves data integrity and allows the organization to update or expand leave categories without modifying existing leave records.

      ## 

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| LeaveTypeID | INT | Unique identifier of leave type |
| LeaveTypeName | VARCHAR(100) | Name of the leave category |
| Description | VARCHAR(255) | Leave type description |

      

      2. **Constraints**

| Primary Key | LeaveTypeID |
| :---- | :---- |
| **Foreign Key** | None |
| **Unique Constraint** | LeaveTypeName |
| **Check Constraint** | None |

   9. ## **Attendance**

      ##        The Attendance table records daily employee attendance information, including the date, time-in, and time-out values. This table captures employee work hours that will later be used for payroll calculations and attendance monitoring.

      ## The Attendance table references the Employee table through the EmployeeID foreign key. This ensures that attendance records are linked to valid employees. Attendance data is also used to determine overtime hours and calculate payroll earnings, making it a critical component of the payroll processing workflow.

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| AttendanceID | INT | Unique identifier of attendance record |
| EmployeeID | INT | References employee record |
| Date | DATE | Date of attendance |
| TimeIn | TIMESTAMP | Employee time-in |
| TimeOut | TIMESTAMP | Employee time-out |

      

      2. **Constraints**

| Primary Key | AttendanceID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) |
| **Unique Constraint** | None |
| **Check Constraint** | TimeOut ≥ TimeIn |

   10. ## **Overtime**

         
       The Overtime table stores records of additional hours worked by employees beyond their regular work schedule. It includes the number of overtime hours and the overtime pay rate applied for those hours.  
       The Overtime table references the Attendance table through the AttendanceID foreign key. This ensures that overtime entries correspond to a specific attendance record. By linking overtime data to attendance records, the system can accurately calculate additional earnings during payroll processing.  
       1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| OvertimeID | INT | Unique identifier of overtime record |
| AttendanceID | INT | References attendance record |
| Hours | DECIMAL(5,2) | Number of overtime hours |
| Rate | DECIMAL(10,2) | Overtime rate per hour |
| ApprovedBy | INT | References the Employee table for the employee who approves the ovetime |
| ApprovalStatusID | INT | References the ApprovalStatus table  |
| ApprovalDate | TIMESTAMP | Date and time when the overtime is approved |

       

       2. **Constraints**

| Primary Key | OvertimeID |
| :---- | :---- |
| **Foreign Key** | AttendanceID → Attendance(AttendanceID) ApprovedBy → Employee(EmployeeID) ApprovalStatusID → ApprovalStatus(ApprovalStatusID) |
| **Unique Constraint** | None |
| **Check Constraint** | Hours ≥ 0; Rate ≥ 0 |

   11. ## **ApprovalStatus**

         
       The ApprovalStatus table is a lookup table that defines the possible approval states for leave and overtime requests. It stores predefined status values such as Pending, Approved, and Rejected, which are referenced by other tables to maintain consistent approval classifications throughout the system. Supervisors or authorized personnel are responsible for approving leave and overtime requests. This is implemented through the ApprovedBy attribute, which references the Employee entity, and is governed by role-based access control (RBAC).  
         
       By using a lookup table instead of storing text values directly in transactional tables, the database ensures domain control and prevents inconsistent or invalid approval entries. This design improves data integrity while supporting the system’s approval workflow for employee leave and overtime records.  
         
       1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| **ApprovalStatusID** | INT | Unique identifier for approval status |
| StatusName | VARCHAR(50) | Name of the approval status (Pending, Approved, Rejected) |

       

       2. **Constraints**

| Primary Key | ApprovalStatusID |
| :---- | :---- |
| **Foreign Key** | None |
| **Unique Constraint** | StatusName |
| **Check Constraint** | None |

          

   12. ## **Salary**

         
       The Salary table stores base salary information for employees, including the employee’s base salary amount and the salary payment frequency. This table defines the fundamental compensation structure for each employee.  
       The Salary table references the Employee table through the EmployeeID foreign key. This ensures that salary information is always associated with a valid employee record. Salary data is used during payroll processing to compute gross pay before deductions and allowances are applied.  
       1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| SalaryID | INT | Unique identifier of salary record |
| EmployeeID | INT | References employee record |
| BaseSalary | DECIMAL(12,2) | Employee’s base salary |
| PayFrequency | VARCHAR(50) | Salary payment frequency |
| EffectiveFrom | DATE | Date when salary becomes active |
| EffectiveTo | DATE | Date when the salary ends |

       

       2. **Constraints**

| Primary Key | SalaryID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) ON DELETE RESTRICT ON UPDATE CASCADE |
| **Unique Constraint** | None |
| **Check Constraint** | BaseSalary ≥ 0 |

   13. ## **Payroll**

       The Payroll table stores payroll records generated for employees during specific pay periods. Each record contains key financial information such as gross pay and net pay, allowing the system to maintain an accurate history of employee compensation over time.  
       This table is linked to the Employee table through a foreign key, ensuring that payroll records are associated with valid employees. The table supports financial auditing and reporting by preserving payroll history and enforcing referential integrity between employee records and payroll transactions. Payroll records are also associated with attendance data indirectly through EmployeeID and the defined pay period (PayPeriodStart and PayPeriodEnd). Attendance records within this period are used for payroll computation.  
       **Regulatory and Legal Compliance**  
       The Payroll entity is designed to align with Philippine labor laws and statutory requirements to ensure accurate and compliant salary computation.  
       The system considers mandatory government contributions including:  
* Social Security System (SSS) – Republic Act No. 11199    
* PhilHealth – Republic Act No. 11223    
* Pag-IBIG Fund – Republic Act No. 9679  

  Although contribution computation is handled at the application layer, the database structure ensures that payroll outputs (gross pay, deductions, and net pay) are stored accurately. Attendance records also support compliance with the Labor Code of the Philippines by ensuring proper wage and overtime computation. This integration ensures that the system reflects real-world payroll regulations and business practices.

  1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| PayrollID | INT | Unique Identifier for payroll record |
| EmployeeID | INT | References the employee receiving payroll |
| PayPeriodStart | DATE | Start of pay period |
| PayPeriodEnd | DATE | End of pay period |
| GrossPay | DECIMAL(12,2) | Total earnings before deductions |
| NetPay | DECIMAL(12,2) | Final pay after deductions |

     2. **Constraints**

| Primary Key | PayrollID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) ON DELETE RESTRICT, ON UPDATE CASCADE |
| **Unique Constraint** | (EmployeeID, PayPeriodStart, PayPeriodEnd) |
| **Check Constraint** | GrossPay ≥ 0, NetPay ≥ 0 |

  14. ## **Payslip**

      The Payslip table stores the payslip generated for each payroll record issued to employees. It serves as the official documentation of a completed payroll transaction, including the date the payslip was issued.  
      Each payslip is linked to a corresponding payroll record through a foreign key relationship, ensuring that every payroll entry produces a single official payslip. This relationship maintains traceability between payroll computations and employee pay documentation.  
      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| PayslipID | INT | Unique Identifier for each generated payslip |
| PayrollID | INT | References the payroll record used to generate the payslip  |
| IssueDate | DATE | Date when the payslip was issued to the employee |

      2. **Constraints**

| Primary Key | PayslipID |
| :---- | :---- |
| **Foreign Key** | PayrollID → Payroll(PayrollID) ON DELETE RESTRICT, ON UPDATE CASCADE |
| **Unique Constraint** | PayrollID |
| **Check Constraint** | None |

  15. ## **Deduction**

      The Deduction table stores payroll deductions applied to a specific payroll record. These deductions may include statutory deductions such as SSS, PhilHealth, Pag-IBIG contributions, withholding tax, or other authorized deductions that reduce an employee’s gross pay.  
        
      The Deduction table references the Payroll table through the PayrollID foreign key. This relationship ensures that all deductions are associated with a valid payroll record and provides greater transparency by allowing the system to track the individual components that contribute to the calculation of an employee’s net pay.  
        
      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| DeductionID | INT | Unique Identifier of deduction record |
| PayrollID | INT | References the payroll record |
| DeductionType | VARCHAR(100) | Type of deduction (e.g., Tax, SSS, PhilHealth, Pag-IBIG) |
| Amount | DECIMAL | Deduction amount applied to payroll  |

      2. **Constraints**


| Primary Key | DeductionID |
| :---- | :---- |
| **Foreign Key** | PayrollID → Payroll(PayrollID) ON DELETE RESTRICT, ON UPDATE CASCADE |
| **Unique Constraint** | PayrollID, DeductionType  |
| **Check Constraint** | Amount \>= 0  |

  16. ## **UserAccount Table**

        
      The UserAccount table stores login credentials for system users who access the MotorPH payroll system. It includes information such as username and the associated employee account.  
      Each user account is linked to an employee record through the EmployeeID foreign key. This ensures that system access is granted only to valid employees. The UserAccount table also supports the implementation of role-based access control by linking user accounts to system roles through the UserRole table.  
      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| UserID | INT | Unique identifier of system user |
| EmployeeID | INT | References employee record |
| Username | VARCHAR(100) | Login username |
| PasswordHash | VARCHAR(255) | Hashed password used for authentication |

      

      2. **Constraints**

| Primary Key | UserID |
| :---- | :---- |
| **Foreign Key** | EmployeeID → Employee(EmployeeID) |
| **Unique Constraint** | Username (NOT NULL, UNIQUE) EmployeeID |
| **Check Constraint** | Business Rule: One employee may have only one UserAccount |

  17. ## **Role**

      ##        The Role table defines the different roles available within the system, such as HR staff, finance personnel, administrators, or employees. Each role represents a specific set of responsibilities within the payroll system.

      ## Roles are assigned to users through the UserRole associative table. This allows a user to have one or more roles depending on their responsibilities. The Role table works together with the Permission table to implement role-based access control within the system.

      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| RoleID | INT | Unique identifier of role |
| RoleName | VARCHAR(100) | Name of the role |

      

      2. **Constraints**

| Primary Key | RoleID |
| :---- | :---- |
| **Foreign Key** | None |
| **Unique Constraint** | RoleName |
| **Check Constraint** | None |

  18. ## **Permission**

        
      The Permission table stores the different access rights that can be granted within the system. These permissions define specific actions that users can perform, such as viewing payroll records or managing employee information.  
      Permissions are assigned to roles through the RolePermission associative table. This structure allows the system to define what actions each role can perform without assigning permissions directly to individual users. This improves system security and simplifies permission management.  
        
      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| PermissionID | INT | Unique identifier of permission |
| PermissionName | VARCHAR(100) | Name of the permission |
| Description | VARCHAR(255) | Permission description |

      

      2. **Constraints**

| Primary Key | PermissionID |
| :---- | :---- |
| **Foreign Key** | None |
| **Unique Constraint** | PermissionName |
| **Check Constraint** | None |

  19. ## **UserRole**

        
      The UserRole table is an associative table that connects users to their assigned roles. It resolves the many-to-many relationship between the UserAccount and Role tables.  
      Each record in the UserRole table links a specific user account to a specific role. This allows users to have multiple roles within the system while ensuring that role assignments reference valid user accounts and roles. This table is essential for implementing role-based access control.  
      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| UserRoleID | INT | Unique identifier of user-role mapping |
| UserID | INT | References user account |
| RoleID | INT | References role |

      

      2. **Constraints**

| Primary Key | UserRoleID |
| :---- | :---- |
| **Foreign Key** | UserID → UserAccount(UserID); RoleID → Role(RoleID) |
| **Unique Constraint** | (UserID, RoleID) |
| **Check Constraint** | None |

  20. ## **RolePermission**

        
      The RolePermission table is an associative table that connects system roles to the permissions assigned to them. It defines which permissions are available for each role in the system.  
      Each record links a role to a specific permission, allowing the system to control what actions each role can perform. This design supports flexible access control and ensures that permissions are consistently applied across all users assigned to a role.  
      1. **Columns**

| Column Name | Data Type | Descriptor |
| :---- | :---- | :---- |
| RolePermissionID | INT | Unique identifier of role-permission mapping |
| RoleID | INT | References role |
| PermissionID | INT | References permission |

      

      2. **Constraints**

| Primary Key | RolePermissionID |
| :---- | :---- |
| **Foreign Key** | RoleID → Role(RoleID); PermissionID → Permission(PermissionID) |
| **Unique Constraint** | (RoleID, PermissionID) |
| **Check Constraint** | None |


4. # **Data Modeling** {#data-modeling}

   

   ## **Conceptual Diagram** {#conceptual-diagram}

     
   *Figure 4.1.a. MotorPH Payroll Database Entity Relationship Diagram*  
   The Entity Relationship Diagram (ERD) represents the conceptual design of the MotorPH Payroll System database. It was the first diagram developed during the database design process and served as the foundation for the later logical and physical database models. The ERD identifies the major entities involved in the payroll system, such as Employee, Department, Attendance, Payroll, Leave, and UserAccount, along with their key relationships.  
   The purpose of the ERD is to illustrate how data entities interact within the system without focusing on implementation details such as data types or constraints. Relationships between entities define how information is connected, for example employees belonging to departments, employees generating attendance records, and payroll records being associated with employees. This conceptual model provided a clear representation of the system’s core data structure and business processes.  
   As the system design evolved, additional requirements and improvements were identified. These refinements resulted in structural changes that were incorporated into the later schema diagram. Examples include the introduction of lookup tables such as LeaveType and ApprovalStatus to enforce data consistency, as well as the addition of approver-related attributes (ApprovedBy) in the Leave and Overtime entities to support approval workflows. Approval workflows are represented through relationships between Employee (as approver), Leave, Overtime, and the ApprovalStatus lookup table. These updates ensured that the database design better reflects real organizational processes and supports normalized data management.

   ## **Schema Diagram** {#schema-diagram}

     
   *Figure 4.2.a. MotorPH Payroll Database Schema Diagram*  
   ![][image1]  
   *View the full resolution image here: [MotorPH Payroll Database Schema Diagram - revised.jpg](https://drive.google.com/file/d/1_WmNdSNLxoxG7OsIbvNMfWSJCizKrHig/view?usp=sharing)*  
   The schema diagram represents the logical and physical structure of the MotorPH Payroll System database after the conceptual ERD was refined and normalized. Unlike the ERD, the schema diagram specifies the actual database tables, their attributes, data types, primary keys, foreign keys, and constraints. This diagram provides a more detailed representation of how the database will be implemented within the chosen relational database management system.  
   The schema design follows Third Normal Form (3NF) principles to reduce redundancy and maintain data integrity. Core entities such as Employee, Department, Salary, Attendance, Payroll, and Payslip are structured as individual tables with clearly defined relationships using primary and foreign keys. Additional modules were also incorporated, including role-based access control (RBAC) tables such as UserAccount, Role, Permission, UserRole, and RolePermission to manage user access within the system.  
   Several refinements were introduced during the transition from the initial ERD to the schema diagram. Lookup tables such as LeaveType and ApprovalStatus were added to standardize categorical data and enforce domain consistency. The Leave and Overtime tables were also extended with approval-related attributes, including ApprovedBy and ApprovalStatusID, allowing supervisors or managers to authorize requests within the system. These improvements ensure that the final schema accurately represents real payroll operations while maintaining a normalized and scalable database structure.  
   

5. # **Normalization** {#normalization}

   

   **Explanation of the Normalization Process**  
     
   Database normalization is a systematic design process used to organize data within a relational database in order to minimize redundancy, eliminate data anomalies, and improve overall data integrity. The MotorPH Payroll System Database applies normalization principles up to Third Normal Form (3NF) to ensure that payroll-related information is stored efficiently, consistently, and securely across multiple interconnected entities.  
     
   The normalization process helps ensure that each piece of information is stored only once in its most appropriate location. This reduces duplicate data, improves storage efficiency, simplifies maintenance, and prevents inconsistencies during database operations such as INSERT, UPDATE, and DELETE transactions. By separating information into logical entities such as Employee, Department, Salary, Attendance, Payroll, Leave, Benefit, GovernmentID, and EmployeeAddress, the system maintains accurate relationships while supporting scalability and long-term database performance.  
     
   **First Normal Form (1NF)**  
     
   The database satisfies First Normal Form (1NF) because all tables contain atomic values and no repeating groups. Each attribute stores only a single value, and every table contains a uniquely identifiable primary key.  
     
   For example:  
     
   \* Employee information is separated into distinct attributes such as FirstName, LastName, ContactNumber, and Position.  
   \* Attendance records store one Date, TimeIn, and TimeOut value per row.  
   \* Government identifiers such as SSSNumber and TINNumber are stored individually within the GovernmentID entity.  
     
   This structure ensures organized and standardized data storage throughout the database.  
     
   **Second Normal Form (2NF)**  
     
   The database satisfies Second Normal Form (2NF) because all non-key attributes fully depend on their respective primary keys. Partial dependencies are eliminated by separating related information into dedicated entities.  
     
   For example:  
     
   \* Salary information is stored in the Salary table rather than directly within the Employee table.  
   \* Employee benefits are stored separately in the Benefit entity.  
   \* Employee addresses are isolated in the EmployeeAddress table.  
   \* Government-related records are maintained within the GovernmentID entity.  
     
   This separation prevents repeated salary, address, and government information from being duplicated across employee records. It also improves flexibility by allowing multiple salary histories and benefits to be managed independently.  
     
   **Third Normal Form (3NF)**  
     
   The database achieves Third Normal Form (3NF) by removing transitive dependencies and ensuring that non-key attributes depend only on the primary key and not on other non-key attributes.  
     
   To accomplish this, the database uses dedicated lookup and associative entities such as:  
     
   \* EmploymentStatus  
   \* LeaveType  
   \* ApprovalStatus  
   \* Role  
   \* Permission  
   \* UserRole  
   \* RolePermission  
     
   For example:  
     
   \* Leave records reference LeaveTypeID instead of repeatedly storing leave category names.  
   \* Overtime and Leave requests reference ApprovalStatusID instead of repeatedly storing approval text values such as “Approved” or “Rejected.”  
   \* User roles and permissions are separated into associative entities to properly support many-to-many relationships within the RBAC module.  
     
   This structure eliminates redundant text values, improves consistency, and simplifies updates across the system.  
     
   **Benefits of Normalization in the MotorPH Payroll System**  
     
   Applying normalization within the MotorPH Payroll System Database provides several important advantages:  
     
   \* Reduces data redundancy by storing information only once  
   \* Prevents update, insertion, and deletion anomalies  
   \* Improves data consistency and integrity  
   \* Simplifies database maintenance and future modifications  
   \* Enhances query performance and storage efficiency  
   \* Supports scalable payroll and employee management operations  
   \* Improves security through modular entity separation  
     
   The updated database design further strengthens normalization by introducing dedicated entities such as Benefit, GovernmentID, EmployeeAddress, and Deduction. These additions improve modularity, separate sensitive employee information, and support more efficient payroll data management.  
     
   Overall, the normalization process ensures that the MotorPH Payroll System Database maintains a well-structured, scalable, and reliable relational architecture capable of supporting secure and accurate payroll operations.  
     
   **Application of Normalization to the MotorPH Database Design**  
     
   The normalization principles discussed above were applied directly in transforming the initial ERD into the final schema diagram.  
     
   **From ERD to Schema Refinement**  
     
   During the initial ERD design, several attributes were identified that could lead to redundancy if stored in a single table. These were refined into separate entities during schema development.  
     
   For example:  
* Salary details were separated from the Employee entity into a dedicated Salary table to prevent duplication when salary changes over time    
* Payroll data was separated into its own table to store transactional records per pay period  


  **Implementation of Lookup Tables**


  To eliminate transitive dependencies and enforce consistency, several lookup tables were introduced:


* EmploymentStatus – to standardize employee status values    
* LeaveType – to categorize leave records    
* ApprovalStatus – to manage approval workflows  


  This ensures that categorical data is not repeatedly stored in transactional tables.


  **Normalization of Relationships**


* Relationships identified in the ERD were refined using foreign keys:  
* Employee → Attendance (via EmployeeID)    
* Employee → Payroll (via EmployeeID)    
* Attendance → Overtime (via AttendanceID)    
* Payroll → Payslip (via PayrollID)  


  Additionally, many-to-many relationships were resolved using associative tables:


* UserRole (User ↔ Role)    
* RolePermission (Role ↔ Permission)  


  **Resulting Improvements**


  These normalization steps transformed the conceptual design into a structured relational schema that eliminates redundancy, ensures data consistency, supports scalability, and enables efficient querying through well-defined relationships. This demonstrates that normalization was actively applied in the design process rather than only defined conceptually.


  This structured transformation from ERD to schema demonstrates a clear application of database design principles aligned with real-world system requirements.


  ## **Key Decisions**  {#key-decisions}

  First, the Employee table was designed as the central entity, since most other tables reference employee information. Tables such as Payroll, Attendance, Leave, and Salary all link to Employee through foreign key relationships.

  Second, lookup tables were introduced for attributes that contain predefined categories. Tables such as LeaveType, EmploymentStatus, and ApprovalStatus allow the system to enforce standardized values and prevent inconsistent entries.

  Third, associative tables such as UserRole and RolePermission were implemented to support role-based access control (RBAC). These tables resolve many-to-many relationships between users, roles, and permissions while maintaining a scalable security model.

  Finally, historical data tracking was considered in the design. Tables such as Salary and Payroll allow multiple records per employee over time, ensuring that payroll history is preserved for auditing and reporting purposes.

  ## **Considerations and Limitations** {#considerations-and-limitations}

  While normalization improves data organization and integrity, it also introduces certain design considerations. Highly normalized databases may require more joins when retrieving data from multiple tables, which can impact query performance if not properly optimized.

  To address this, the MotorPH Payroll System database uses indexing and efficient foreign key relationships to maintain acceptable performance levels. In addition, the design separates transactional data from lookup data, which helps reduce redundancy while preserving query efficiency.

  Another limitation is that the database design focuses primarily on data storage and integrity, while payroll computations and financial calculations are handled by the application layer. This separation ensures that the database remains focused on maintaining reliable data relationships rather than performing complex business logic. 

  ## 

6. # **Database Schema and Data Integrity** {#database-schema-and-data-integrity}

| Table Name | Primary Key | Foreign Keys | Description |
| ----- | ----- | ----- | ----- |
| Employee | EmployeeID | StatusID → EmploymentStatus(StatusID), DepartmentID → Department(DepartmentID) | Stores core employee information |
| Department | DepartmentID | ManagerID → Employee(EmployeeID) | Stores department information and assigned manager |
| EmploymentStatus | StatusID | None | Stores predefined employment statuses |
| EmployeeAddress | AddressID | EmployeeID → Employee(EmployeeID) | Stores employee address information |
| GovernmentID | GovernmentID | EmployeeID → Employee(EmployeeID) | Stores employee government identification numbers |
| Salary | SalaryID | EmployeeID → Employee(EmployeeID) | Stores employee salary history and compensation |
| Benefit | BenefitID | EmployeeID → Employee(EmployeeID) | Stores employee benefits and allowances |
| Attendance | AttendanceID | EmployeeID → Employee(EmployeeID) | Stores employee attendance records |
| Overtime | OvertimeID | AttendanceID → Attendance(AttendanceID), ApprovalStatusID → ApprovalStatus(ApprovalStatusID), ApprovedBy → Employee(EmployeeID) | Stores overtime requests and approvals |
| Leave | LeaveID | EmployeeID → Employee(EmployeeID), LeaveTypeID → LeaveType(LeaveTypeID), ApprovalStatusID → ApprovalStatus(ApprovalStatusID), ApprovedBy → Employee(EmployeeID) | Stores employee leave requests |
| LeaveType | LeaveTypeID | None | Stores predefined leave categories |
| ApprovalStatus | ApprovalStatusID | None | Stores predefined approval statuses |
| Payroll | PayrollID | EmployeeID → Employee(EmployeeID) | Stores payroll transaction records |
| Deduction | DeductionID | PayrollID → Payroll(PayrollID) | Stores payroll deductions |
| Payslip | PayslipID | PayrollID → Payroll(PayrollID) | Stores generated employee payslips |
| UserAccount | UserID | EmployeeID → Employee(EmployeeID) | Stores system login credentials |
| Role | RoleID | None | Stores user roles for RBAC |
| Permission | PermissionID | None | Stores system permissions |
| UserRole | UserRoleID | UserID → UserAccount(UserID), RoleID → Role(RoleID) | Resolves many-to-many relationship between users and roles |
| RolePermission | RolePermissionID | RoleID → Role(RoleID), PermissionID → Permission(PermissionID) | Resolves many-to-many relationship between roles and permissions |

# 

| Table | Constraint Type | Constraint / Rule | Purpose |
| ----- | ----- | ----- | ----- |
| Employee | Primary Key | PRIMARY KEY(EmployeeID) | Uniquely identifies employees |
| Employee | Unique Constraint | UNIQUE(ContactNumber) | Prevents duplicate contact numbers |
| Employee | Foreign Key | DepartmentID → Department(DepartmentID) | Ensures valid department assignment |
| Employee | Foreign Key | StatusID → EmploymentStatus(StatusID) | Ensures valid employment status |
| Department | Foreign Key | ManagerID → Employee(EmployeeID) | Ensures valid department manager |
| Salary | Foreign Key | EmployeeID → Employee(EmployeeID) | Links salary records to employees |
| Salary | Check Constraint | CHECK(BaseSalary \>= 0\) | Prevents negative salary values |
| Benefit | Foreign Key | EmployeeID → Employee(EmployeeID) | Links benefits to employees |
| Benefit | Check Constraint | CHECK(Amount \>= 0\) | Prevents negative benefit values |
| GovernmentID | Foreign Key | EmployeeID → Employee(EmployeeID) | Associates government IDs with employees |
| GovernmentID | Unique Constraint | UNIQUE(SSSNumber) | Prevents duplicate SSS numbers |
| GovernmentID | Unique Constraint | UNIQUE(TINNumber) | Prevents duplicate TIN numbers |
| GovernmentID | Unique Constraint | UNIQUE(PhilHealthNumber) | Prevents duplicate PhilHealth numbers |
| GovernmentID | Unique Constraint | UNIQUE(PagIBIGNumber) | Prevents duplicate Pag-IBIG numbers |
| EmployeeAddress | Foreign Key | EmployeeID → Employee(EmployeeID) | Associates address records with employees |
| Attendance | Foreign Key | EmployeeID → Employee(EmployeeID) | Associates attendance with employees |
| Attendance | Check Constraint | CHECK(TimeOut \>= TimeIn) | Ensures valid attendance times |
| Overtime | Foreign Key | AttendanceID → Attendance(AttendanceID) | Associates overtime with attendance |
| Overtime | Foreign Key | ApprovalStatusID → ApprovalStatus(ApprovalStatusID) | Ensures valid overtime approval status |
| Overtime | Foreign Key | ApprovedBy → Employee(EmployeeID) | Ensures valid approver |
| Overtime | Check Constraint | CHECK(Hours \>= 0\) | Prevents negative overtime hours |
| Overtime | Check Constraint | CHECK(Rate \>= 0\) | Prevents negative overtime rates |
| Leave | Foreign Key | EmployeeID → Employee(EmployeeID) | Associates leave records with employees |
| Leave | Foreign Key | LeaveTypeID → LeaveType(LeaveTypeID) | Ensures valid leave category |
| Leave | Foreign Key | ApprovalStatusID → ApprovalStatus(ApprovalStatusID) | Ensures valid approval status |
| Leave | Foreign Key | ApprovedBy → Employee(EmployeeID) | Ensures valid approver |
| Leave | Check Constraint | CHECK(EndDate \>= StartDate) | Prevents invalid leave dates |
| Payroll | Foreign Key | EmployeeID → Employee(EmployeeID) | Associates payroll with employees |
| Payroll | Unique Constraint | UNIQUE(EmployeeID, PayPeriodStart, PayPeriodEnd) | Prevents duplicate payroll periods |
| Payroll | Check Constraint | CHECK(GrossPay \>= 0\) | Prevents negative gross pay |
| Payroll | Check Constraint | CHECK(NetPay \>= 0\) | Prevents negative net pay |
| Deduction | Foreign Key | PayrollID → Payroll(PayrollID) | Associates deductions with payroll |
| Deduction | Check Constraint | CHECK(Amount \>= 0\) | Prevents negative deduction values |
| Payslip | Foreign Key | PayrollID → Payroll(PayrollID) | Associates payslips with payroll records |
| UserAccount | Unique Constraint | UNIQUE(Username) | Prevents duplicate usernames |
| UserAccount | Not Null Constraint | PasswordHash NOT NULL | Ensures account security |
| UserRole | Foreign Key | UserID → UserAccount(UserID) | Associates users with roles |
| UserRole | Foreign Key | RoleID → Role(RoleID) | Associates role assignments |
| UserRole | Unique Constraint | UNIQUE(UserID, RoleID) | Prevents duplicate role assignments |
| RolePermission | Foreign Key | RoleID → Role(RoleID) | Associates permissions with roles |
| RolePermission | Foreign Key | PermissionID → Permission(PermissionID) | Associates role permissions |
| RolePermission | Unique Constraint | UNIQUE(RoleID, PermissionID) | Prevents duplicate permission assignments |

   # **Business Rules**

* Each employee may only have one user account.  
* Payroll records must belong to valid employees.  
* Overtime and leave requests must follow approval workflows.  
* Government identification numbers must remain unique.  
* Salary, benefit, overtime, and deduction values cannot be negative.  
* Attendance records must contain valid time entries.  
* Leave requests must contain valid start and end dates.  
* User roles and permissions must not contain duplicate assignments.


7. # **Data Migration Plan**  {#data-migration-plan}

   1. ## **Steps for Data Migration** 

      Data migration involves transferring existing employee and payroll-related data from legacy systems or spreadsheets into the MotorPH Payroll System database.  
        
      The migration process follows several steps. First, existing data sources such as spreadsheets, HR records, and attendance logs are collected and analyzed. Second, the data is mapped to the corresponding database tables to ensure compatibility with the new schema structure.  
        
      Next, the data is imported into staging tables where it is validated and checked for errors. After verification, the cleaned data is inserted into the production database tables using structured SQL scripts. Finally, the migrated data is verified through validation checks to ensure completeness and accuracy.

   # 

   2. ## **Data Transformation and Cleaning** {#data-transformation-and-cleaning}

   # 

      Before migration, the collected data must be transformed and cleaned to ensure compatibility with the new database structure.

      

      Data cleaning involves removing duplicate records, correcting inconsistent values, and ensuring that all required fields are complete. For example, employee records must have valid DepartmentID and StatusID values that correspond to existing lookup tables.

      

      Data transformation may include converting date formats, standardizing text fields, and separating combined values into individual attributes. These transformations ensure that the migrated data conforms to the database schema and maintains data integrity.

8. # **Test Scripts and Validation** {#test-scripts-and-validation}

# 

   The database was tested to ensure data integrity and correct enforcement of constraints.

   

   Test Case 1: Insert Employee with Missing Data  

   Expected Result: Rejected due to NOT NULL constraint  

   Test Case 2: Duplicate Username  

   Expected Result: Rejected due to UNIQUE constraint  

   Test Case 3: Invalid Attendance Time (TimeOut \< TimeIn) 

   Expected Result: Rejected due to CHECK constraint  

   Test Case 4: Duplicate Payroll Record  

   Expected Result: Rejected due to UNIQUE(EmployeeID, PayPeriod)  

   Test Case 5: Invalid Foreign Key Entry  

   Expected Result: Rejected due to referential integrity


   These test cases confirm that the system enforces business rules and maintains data

   consistency.

9. # **Security Access and Control**  {#security-access-and-control}

   1. ## **User Roles**	        The MotorPH Payroll System database implements Role-Based Access Control (RBAC) to manage system access. Each user is assigned one or more roles that define their permitted actions within the system.  {#user-roles-the-motorph-payroll-system-database-implements-role-based-access-control-(rbac)-to-manage-system-access.-each-user-is-assigned-one-or-more-roles-that-define-their-permitted-actions-within-the-system.}

| User Role | Permissions | User Role |
| ----- | ----- | ----- |
| HR Administrator | Manage employee records, departments, and employment status | HR Administrator |
| Finance Officer | Access payroll records, salary information, and financial reports | Finance Officer |
| Supervisor | Approve leave and overtime requests | Supervisor |
| Employee | View personal attendance records, leave requests, and payslips | Employee |
| System Administrator | Manage user accounts, roles, and system permissions | System Administrator |

   ##        **9.2      Encryption** {#9.2-encryption}

      

      To ensure the security and confidentiality of sensitive payroll data, the MotorPH Payroll System Database implements encryption mechanisms at both the data storage and data transmission levels.

      

      Sensitive information such as user credentials is protected using one-way hashing algorithms. Specifically, passwords are not stored in plain text but are securely stored in the UserAccount.PasswordHash field using strong hashing techniques such as bcrypt or SHA-256 with salting. This ensures that even if unauthorized access to the database occurs, actual passwords cannot be retrieved.

      

      In addition, data in transit is secured using SSL/TLS encryption protocols, which protect data exchanged between the application and the database server. This prevents interception or unauthorized access during communication.

      

      For highly sensitive personal data (e.g., contact information), column-level encryption may be applied to ensure additional protection. Access to encrypted data is restricted based on user roles through Role-Based Access Control (RBAC), ensuring that only authorized users can view or process sensitive information.

      

      These encryption strategies enhance overall system security, protect confidential employee data, and support compliance with data protection standards and cybersecurity best practices.

10. # **User Manual** {#user-manual}

    The MotorPH Payroll System provides a user-friendly interface for managing payroll operations.

    ![][image2]

    

    ## **Key Features:**

    

* Employee Management – Add, update, and delete employee records    
* Attendance Tracking – Record time-in and time-out    
* Payroll Processing – Generate payroll records    
* Leave Management – Submit and approve leave requests    
* Role-Based Access – Access depends on assigned roles  


  ## **Step by Step Usage Flow:**    	

1. STEP 1: LOGIN

   → Enter Username & Password

2. STEP 2: ACCESS DASHBOARD

   → Select desired module

3. STEP 3: PERFORM ACTION

   → Input or update data

4. STEP 4: SAVE DATA

   → Click "Save" or "Submit"

5. STEP 5: VIEW OUTPUT

→ Check reports, payroll, or records

## 

## **Security Behavior:**  

✔ Only authorized users can log in    
✔ Access is limited based on role    
✔ Sensitive data is protected    
✔ All actions follow system permissions 

The system ensures that only authorized users can perform specific actions based on their assigned roles.

[image1]: <data:image/png;base64,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>

[image2]: <data:image/png;base64,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>