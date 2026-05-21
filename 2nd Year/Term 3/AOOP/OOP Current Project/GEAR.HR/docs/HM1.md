# HM1: Pre-Connection Planning

This document captures our baseline understanding of GEAR.HR before we connect it to a database. It is a self-reflection exercise, not a technical specification. The notes here are meant to support later planning, group discussion, and clear reasons for design choices as the course moves into integration work.

---

## Step 1: Review Your Existing System

GEAR.HR is an existing piece of software—a desktop program that staff open on a computer—not a manual process or a spreadsheet workflow. It acts as a company HR and payroll helper: one place to look up people, track time at work, handle leave, work out pay, and sign in with different levels of access depending on the user’s job.

Today the program handles several everyday HR tasks. People sign in with a user account tied to their role. The employee directory holds who works for the company and basic profile information. Attendance records track when people were present, similar to clock-in data. Leave requests let staff ask for time off and let managers work with those requests. Payroll brings together base salary, allowances, government-related deductions, and tax so that take-home pay can be shown, using attendance hours where that applies. For IT and admin roles, there is also a simple way to log and manage support tickets alongside account and employee management.

The system works with sensitive information throughout. That includes names and contact details, job titles and roles, salary and allowance figures, deduction amounts, login credentials, and histories of attendance and leave. Anyone using the app should treat that data as confidential, which is one reason a more robust way of storing it matters later.

The program is organized into areas that match how people actually work. Every signed-in user gets a personal side: my attendance, my profile and pay, and my leave. Beyond that, what someone sees depends on their group. HR staff get tools to manage attendance, employee profiles, and leave for others. Payroll staff get payroll management plus read-only views of attendance and leave so pay can be checked against time worked. IT and admin get broader access—employee and payroll management, leave and attendance management, and user account handling—plus IT tickets. Regular employees stay on the personal side only; they can view their own pay but not change company-wide records.

All of this information is kept on the computer in plain saved files rather than in a central database. There are separate files for employees, attendance, leave, payroll, login accounts, and IT tickets. When the program starts, it loads those files for use in the program; when someone saves changes, it writes back to the same files. There is no shared database server yet—just files that the application owns and updates.

Day-to-day work mixes automated steps with human ones. Updating base pay or allowances, recording attendance, submitting or acting on leave, and maintaining login accounts are largely manual actions done through screens. Once pay inputs exist, the program applies built-in rules to work out contributions and withholding and to combine that with hours from attendance for a pay picture. Employees typically only view their own profile and pay. HR can look at payroll information for oversight but cannot edit pay figures; only payroll staff and IT or admin roles can change payroll data. That split reflects how responsibilities are divided in a real HR department.

---

## Step 2: Clarify Your Integration Goals

Our main objective in the next phase of the course is to store HR and payroll information in a database instead of relying on scattered files on disk. We are not trying to throw away what the program already does for users; we want the same kinds of tasks—sign-in, directories, attendance, leave, payroll, and admin tools—to continue, but with data living in one organized, protected place that fits standard connection and integration lessons.

The current setup has clear limits we intend to address. File-based storage works for a single person on one machine but does not scale well if more people need to use the system at once, because there is no built-in way to prevent one save from overwriting another. There is no single shared source of truth beyond “whatever was written last to the file.” Backups, audit trails, and answering “who changed this pay record and when?” are harder than they would be with proper storage. We have also seen practical gaps, such as employees appearing in the directory before anyone has entered their payroll row, which then depends on someone noticing and filling that in manually.

After integration, we expect the improved system to keep one reliable store for employees, attendance, leave, payroll, accounts, and tickets, with payroll remaining the most sensitive core. A database gives a foundation for more reliable and consistent data, safer handling of confidential pay information, and eventually richer reporting and history without rebuilding every screen from scratch. It also opens the door—later—to more than one user accessing data in a controlled way, as long as access rules stay aligned with the role groups we already use. For this homework, the focus is connection and organized storage; broader features like advanced analytics or multi-site deployment can follow once the way data is stored is sound.

We see this as evolution, not replacement. The experience for HR, payroll, IT, and employees can stay familiar; what changes is where information lives and how safely it can be shared, backed up, and extended as the course progresses.

---

## Self-Reflection Checklist

### A. Understanding Your Current System

The system revolves around people records, time records, leave records, pay records, login records, and optional IT ticket records. Each type answers a different question: who works here, were they present, did they request leave, what are they paid and deducted, who is allowed to sign in, and was IT asked for help. Together they support the full HR and payroll picture the company needs on a desktop.

Some steps are automated by design. After payroll inputs are in place, calculations for statutory deductions, tax, and net pay follow fixed rules inside the program. Menus and screens adapt automatically based on whether someone is HR, payroll, IT or admin, or a regular employee, so users are not overwhelmed with tools they should not use. Other steps remain manual: entering or correcting attendance, creating and updating employee profiles, editing pay fields where permitted, processing leave, and maintaining user accounts. During development and testing, roles are sometimes changed by editing account data directly, which is a manual workaround rather than a production-style admin workflow.

Understanding this split helps us know what must keep working after a database is connected—the calculations and role-based views are part of the product’s value, while data entry and approvals stay human-driven.

### B. Assessment of Limitations

The biggest pain points today cluster around how data is stored and used. The program assumes a single-user, single-computer mindset: one person runs the app, changes data, and saves files. If two people needed to work at the same time, the last save would win with no warning, which is risky for payroll and employee records. Reporting is mostly what you can see on screen in tables and profile views, not exportable or historical reports pulled from a searchable central store. Operationally, the app depends on the right data files being present in the project folder; if files are missing, some areas start empty, which is fine for a fresh start but confusing if data was expected to be there.

There is no automated test suite in the project, so confidence in changes comes from manual clicking through screens. That is acceptable for coursework but would be a limitation in a live payroll environment where a small mistake affects real pay. Some employees can exist in the directory without a matching payroll entry until someone adds it, which is easy to miss and can lead to incomplete pay views. HR’s read-only access to payroll edits is intentional for separation of duties, but it also means payroll staff must own data quality for pay fields.

These limitations do not mean the current system failed as a learning project; they define why moving to a database is the right next step rather than an optional polish.

### C. Clarifying Integration Goals

The new and improved system, for our course scope, should still be GEAR.HR in purpose—a desktop HR and payroll helper—but with all persistent information flowing through a database connection. Employees, attendance, leave, payroll, user accounts, and IT tickets should each have a clear place in that store, with relationships that match how the app already thinks about them (for example, pay tied to a person, attendance tied to a person and a period).

Primary responsibilities after integration stay the same: support sign-in and role-appropriate screens, maintain an accurate directory, track attendance and leave, compute and display pay correctly, and let authorized roles update sensitive fields. The difference is reliability and structure underneath—one organized data store instead of six separate files, ready for the connection work and design discussions the course will require.

We are not, in this reflection, committing to a specific database product or a full plan for moving existing data; those belong to upcoming modules. The goal stated here is directional: save information to a database instead of separate files while preserving behavior users already understand.

### D. Readiness for Future Work

This reflection leaves us better prepared for design and integration tasks ahead. We can describe what the application does for each role, what categories of data exist, which information is most sensitive, and why file storage is the main bottleneck before connection. We also have a shared vocabulary that is contextual—personal versus management areas, payroll versus HR duties, automated calculation versus manual entry—without needing to lean on implementation details in every conversation.

Open items are expected and appropriate at this stage: which database technology the course will use, how existing saved file data will be moved or recreated, and how connection errors or missing data will be shown to users. Those gaps do not block starting integration planning; they are the natural next questions once a baseline like this document exists.

---

## How This Reflection Helps

Writing this baseline clarifies where GEAR.HR stands today—a working desktop HR and payroll program with role-based access and file-based storage—and where we want it to go: the same user-facing capabilities backed by a database. That clarity supports group discussion, justifies why connection work matters now, and gives a plain-language reference when we compare design options in later weeks. When we argue for or against a design choice, we can point back to real limitations (single save, fragmented files, payroll sensitivity) and goals (one organized store, safer evolution) rather than only to technical preference.
