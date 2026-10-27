# Project Use Case Diagram

| MO-IT115 Object-Oriented System Analysis & Design | |
| :--- | :--- |
| **Milestone 2 Team Leader** | Colin Bactong |
| **Members** | Chelsie Mae Ricafrente; Angelica Mae Calipayan; Charlize Bactong |
| **Program and Year Level** | BSIT Marketing Technology & Software Development, 3rd Year |

## 1. Project Context

**StudySync: MMDC Student Groupmate Matching System** is a web-based platform that addresses the difficulty MMDC students experience when finding compatible groupmates for subject-based academic projects. Because MMDC operates fully online, students have limited opportunities to meet classmates outside synchronous sessions. Differences in IT major, availability, employment schedule, and preferred working style make informal group formation slow and unreliable.

The primary users are MMDC students forming project groups. Administrators are secondary users who maintain accounts and subjects and review reported users. StudySync supports registration and authentication, collaboration profiles, subject enrollment, compatibility-based recommendations, groupmate requests, project-group formation, notifications, reporting, and basic administration. It does not replace Coursera, MyCamu, Google Meet, or Google Workspace.

The Use Case Diagram represents StudySync's behavior at a high level by showing which external actors initiate each function, the system boundary, and mandatory or optional relationships among functions.

## 2. Actor Identification

| Actor | Description | Relationship to the System |
| --- | --- | --- |
| User | General actor representing an authenticated account holder. | Generalizes the shared login and logout behavior inherited by Student and Administrator. |
| Student | MMDC student looking for compatible project groupmates. | Registers, manages a collaboration profile and subjects, finds candidates, manages requests, forms groups, views notifications, and reports users. |
| Administrator | Authorized system maintainer. | Manages student accounts and subjects and reviews reported users. |

StudySync has no external-system actor because it does not integrate with official MMDC enrollment systems, Coursera, MyCamu, Google Meet, or Google Workspace.

## 3. Use Case Identification

| Use Case | Primary Actor | Purpose |
| --- | --- | --- |
| UC-01 Register Account | Student | Create a StudySync student account. |
| UC-02 Log In / Log Out | User | Establish or end an authenticated session. |
| UC-03 Manage Collaboration Profile | Student | Maintain matching information such as major, employment, availability, and working style. |
| UC-04 Manage Subject Enrollment | Student | Record subjects and whether the student is looking for a group. |
| UC-05 Find Potential Groupmates | Student | Generate ranked compatible candidates for a selected subject. |
| UC-06 Send Groupmate Request | Student | Ask a recommended candidate to collaborate. |
| UC-07 Respond to Groupmate Request | Student | Accept or decline a pending request. |
| UC-08 Form Project Group | Student | Establish a subject project group after agreement. |
| UC-09 View Notifications | Student | Read matching, request, and group updates. |
| UC-10 Report User | Student | Report inappropriate behavior or profile content. |
| UC-11 Manage Student Accounts | Administrator | View and update student account status. |
| UC-12 Manage Subjects | Administrator | Add or update subjects available in StudySync. |
| UC-13 Review Reported Users | Administrator | Investigate and resolve submitted reports. |

Supporting use cases are **Set Availability**, **Provide Work Schedule**, **Calculate Compatibility**, **View Candidate Profile**, **Send Notification**, and **Update Account Status**.

## 4. Use Case Diagram

![StudySync Use Case Diagram](StudySync_Use_Case_Diagram.png)

*Figure 1. StudySync Use Case Diagram. Editable source: [StudySync Milestone 2 — Figma](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK).*

## 5. Use Case Diagram Analysis

The Student actor participates in the complete group-formation flow: registration, profile and subject setup, matching, requests, agreement, group formation, notifications, and reporting. The Administrator participates only in account, subject, and report-management functions. Both specialize the general User actor and share UC-02.

The critical path is **UC-03 → UC-04 → UC-05 → UC-06 → UC-07 → UC-08**. Without accurate profile and enrollment data, the system cannot generate useful recommendations. UC-03 includes Set Availability because availability is a standard matching input; Provide Work Schedule extends UC-03 only when the student is employed. UC-05 includes Calculate Compatibility. View Candidate Profile and Send Groupmate Request extend the matching flow because they occur only after candidates are shown. UC-08 extends UC-07 under the condition that a request is accepted. UC-06, UC-07, and UC-08 include Send Notification. Update Account Status extends UC-13 only when a report resolution requires an account action.

These relationships keep the scope focused on forming project groups while representing mandatory shared behavior separately from conditional behavior.

## 6. Use Case Narratives

### 6.1 UC-01 — Register Account

Creates a new StudySync student account.

| Item | Definition |
| --- | --- |
| Use Case ID | UC-01 |
| Use Case Name | Register Account |
| Business Goal | Give an MMDC student secure access to StudySync. |
| Actor | Student |
| Description | The student submits identity and credential information; the system validates and creates an active account. |
| Preconditions | 1. Student has an MMDC email address. 2. No account already uses that email. |
| Postconditions | An ACTIVE student account is stored and can be authenticated. |
| Expected Outcome | Registration succeeds and the student can log in. |
| Exceptions | Duplicate or invalid email, weak/mismatched password, or storage failure; the system rejects the submission and explains the error. |
| Includes | None |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Opens registration. | Displays the registration form. |
| 2 | Enters name, MMDC email, password, and confirmation. | Validates required fields and formats. |
| 3 | Submits the form. | Checks email uniqueness, hashes the password, creates the account, and confirms success. |

**Alternative course:** If validation fails, the system highlights the invalid fields and keeps valid entries for correction.

### 6.2 UC-03 — Manage Collaboration Profile

| Item | Definition |
| --- | --- |
| Use Case ID | UC-03 |
| Use Case Name | Manage Collaboration Profile |
| Business Goal | Capture reliable information for compatibility matching. |
| Actor | Student |
| Description | The student creates or updates major, employment status, schedule, availability, and working style. |
| Preconditions | 1. Student is authenticated. |
| Postconditions | A valid collaboration profile and availability entries are stored. |
| Expected Outcome | Later recommendations use the latest profile. |
| Exceptions | Missing/overlapping time ranges or invalid work schedule; the system requests correction. |
| Includes | Set Availability |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Opens Collaboration Profile. | Displays current values. |
| 2 | Selects major, employment status, working style, and availability. | Validates each selection. |
| 3 | If working, enters a typical work schedule. | Shows the conditional schedule field and validates it. |
| 4 | Saves the profile. | Stores the profile and confirms the update. |

**Alternative course:** A full-time student omits the work schedule because Provide Work Schedule does not apply.

### 6.3 UC-04 — Manage Subject Enrollment

| Item | Definition |
| --- | --- |
| Use Case ID | UC-04 |
| Use Case Name | Manage Subject Enrollment |
| Business Goal | Identify the subjects for which the student may be matched. |
| Actor | Student |
| Preconditions | 1. Student is authenticated. 2. Subjects exist in the catalog. |
| Postconditions | Subject enrollment and looking-for-group status are stored. |
| Expected Outcome | The student becomes eligible for matching in selected subjects. |
| Exceptions | Duplicate enrollment or unavailable subject; the system prevents invalid changes. |
| Includes | None |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Opens My Subjects. | Displays enrolled and available subjects. |
| 2 | Adds or removes a subject. | Validates and updates enrollment. |
| 3 | Toggles Looking for Group. | Stores the subject-specific flag. |

**Alternative course:** Turning the flag off excludes the student from new recommendations for that subject.

### 6.4 UC-05 — Find Potential Groupmates

| Item | Definition |
| --- | --- |
| Use Case ID | UC-05 |
| Use Case Name | Find Potential Groupmates |
| Business Goal | Recommend compatible students for the same subject. |
| Actor | Student |
| Preconditions | 1. Student is authenticated. 2. Profile is complete. 3. Student is looking for a group in the selected subject. |
| Postconditions | Ranked recommendations are displayed and may be stored. |
| Expected Outcome | Eligible candidates meeting the minimum score appear in descending compatibility order. |
| Exceptions | No eligible candidates, incomplete profile, or service failure; the system displays guidance and no false matches. |
| Includes | Calculate Compatibility |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Selects a subject and chooses Find Groupmates. | Validates the session and eligibility. |
| 2 | Waits for results. | Retrieves same-subject students marked looking for a group. |
| 3 | Reviews ranked candidates. | Compares profile criteria, keeps qualifying scores, and displays the list. |

**Alternative course:** If no candidate qualifies, the system displays an empty state and suggests checking profile and availability information.

### 6.5 UC-06 — Send Groupmate Request

| Item | Definition |
| --- | --- |
| Use Case ID | UC-06 |
| Use Case Name | Send Groupmate Request |
| Business Goal | Let a student propose collaboration to a suitable candidate. |
| Actor | Student |
| Preconditions | 1. Student is authenticated. 2. Candidate is eligible for the same subject. 3. No equivalent pending request exists. |
| Postconditions | A PENDING request is stored and the receiver is notified. |
| Expected Outcome | The candidate can accept or decline the request. |
| Exceptions | Duplicate request, ineligible candidate, or full group; the system rejects the action. |
| Includes | Send Notification |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Opens a candidate profile. | Displays relevant collaboration information. |
| 2 | Selects Send Request. | Validates eligibility and duplicate status. |
| 3 | Confirms. | Creates the request and notifies the receiver. |

**Alternative course:** The student cancels before confirmation and no request is stored.

### 6.6 UC-07 — Respond to Groupmate Request

| Item | Definition |
| --- | --- |
| Use Case ID | UC-07 |
| Use Case Name | Respond to Groupmate Request |
| Business Goal | Record whether the receiver agrees to collaborate. |
| Actor | Student |
| Preconditions | 1. Student is authenticated. 2. A PENDING request is addressed to the student. |
| Postconditions | Request status becomes ACCEPTED or DECLINED; the sender is notified. |
| Expected Outcome | Both students receive a consistent request status. |
| Exceptions | Request already resolved, cancelled, or acceptance exceeds group capacity. |
| Includes | Send Notification |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Opens Requests Inbox. | Displays pending requests. |
| 2 | Reviews a request. | Shows sender, subject, and status. |
| 3 | Selects Accept or Decline. | Updates status and notifies the sender. |

**Alternative course:** If capacity would be exceeded, acceptance is blocked and the request remains pending until valid action is possible.

### 6.7 UC-08 — Form Project Group

| Item | Definition |
| --- | --- |
| Use Case ID | UC-08 |
| Use Case Name | Form Project Group |
| Business Goal | Establish a subject project team from agreed students. |
| Actor | Student |
| Preconditions | 1. Student is authenticated. 2. A relevant request is ACCEPTED. 3. Both students are enrolled in the subject. |
| Postconditions | A ProjectGroup and active memberships exist. |
| Expected Outcome | Members can view the group roster and basic group information. |
| Exceptions | Group capacity reached, duplicate subject membership, or invalid enrollment. |
| Includes | Send Notification |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Opens the accepted request. | Offers group creation or joining. |
| 2 | Confirms group details. | Validates enrollment and capacity. |
| 3 | Creates/forms the group. | Stores group and memberships and notifies members. |

**Alternative course:** If the requester already owns a forming group for the subject, the accepted student is added to that group instead.

### 6.8 UC-10 — Report User

| Item | Definition |
| --- | --- |
| Use Case ID | UC-10 |
| Use Case Name | Report User |
| Business Goal | Allow inappropriate activity to be reviewed safely. |
| Actor | Student |
| Preconditions | 1. Reporter is authenticated. 2. Reported user exists. |
| Postconditions | A PENDING UserReport is stored for administrators. |
| Expected Outcome | The report appears in the moderation queue. |
| Exceptions | Empty reason, duplicate report, or self-report; the system rejects invalid submission. |
| Includes | None |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Selects Report User from a profile. | Displays report form and privacy notice. |
| 2 | Selects a reason and provides details. | Validates required information. |
| 3 | Submits. | Creates the report and confirms receipt. |

**Alternative course:** The student cancels, and no report is created.

### 6.9 UC-13 — Review Reported Users

| Item | Definition |
| --- | --- |
| Use Case ID | UC-13 |
| Use Case Name | Review Reported Users |
| Business Goal | Resolve reports consistently and protect users. |
| Actor | Administrator |
| Preconditions | 1. Administrator is authenticated and authorized. 2. A report exists. |
| Postconditions | Report becomes RESOLVED or DISMISSED; a resolution is recorded. |
| Expected Outcome | Valid reports receive an auditable administrative decision. |
| Exceptions | Insufficient evidence, concurrent update, or unauthorized access. |
| Includes | None |

| Step | Actor Action | System Response |
| --- | --- | --- |
| 1 | Opens the report queue. | Displays pending and under-review reports. |
| 2 | Selects a report. | Shows reporter, reported user, reason, and history. |
| 3 | Records a resolution. | Stores the decision and closes or dismisses the report. |
| 4 | If required, selects an account action. | Updates the account status and records the action. |

**Alternative course:** If evidence is insufficient, the administrator dismisses the report without changing the account.

## References

- [Group Proposal Document](MS1%20Files/Group%20Proposal%20Document%20.md)
- [High-Level Project Structural Analysis](MS1%20Files/High-Level%20Project%20Structural%20Analysis%20of%20StudySync.md)
- [Project Class Diagram](MS1%20Files/Project%20Class%20Diagram.md)
- [Project Component Diagram](MS1%20Files/Project%20Component%20Diagram%20.md)
- [StudySync Milestone 2 — Figma](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK)
