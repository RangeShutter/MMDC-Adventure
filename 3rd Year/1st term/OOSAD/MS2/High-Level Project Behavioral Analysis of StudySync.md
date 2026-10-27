# High-Level Project Behavioral Analysis of StudySync

Prepared and Presented by:

**Colin Bactong**  
**Chelsie Mae Ricafrente**  
**Angelica Mae Calipayan**  
**Charlize Bactong**  
*Bachelor of Science in Information Technology*  
*1st Term A.Y. 2026–2027*

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Project Overview](#2-project-overview)
3. [Use Case Analysis](#3-use-case-analysis)
4. [Sequence Analysis](#4-sequence-analysis)
5. [Low-Fidelity Prototype](#5-low-fidelity-prototype)
6. [Consistency and Usability Evaluation](#6-consistency-and-usability-evaluation)
7. [Behavioral Findings](#7-behavioral-findings)
8. [Recommendations](#8-recommendations)
9. [References](#9-references)

## 1. Executive Summary

Mapúa Malayan Digital College students complete projects in a fully online environment and may have limited opportunities to meet classmates outside synchronous sessions. Forming a group can require manually asking several students whether they take the same subject, still need a group, share compatible schedules, and prefer a similar way of working. The difficulty is greater for working students whose employment schedules restrict their availability.

**StudySync: MMDC Student Groupmate Matching System** addresses this problem through a structured, web-based group-formation process. Students maintain collaboration profiles, select subjects, indicate when they need a group, receive rule-based compatibility recommendations, exchange groupmate requests, form project groups, and receive relevant notifications. Administrators maintain accounts and subjects and resolve reports. StudySync remains separate from Coursera, MyCamu, Google Meet, and Google Workspace.

Milestone 1 established the system's static structure through the class and component models. This Milestone 2 draft analyzes dynamic behavior through three connected artifacts:

1. A Use Case Diagram defining the system boundary, actors, and major functions.
2. A Sequence Diagram detailing UC-05 Find Potential Groupmates.
3. A clickable low-fidelity prototype translating the models into user screens and navigation.

The analysis confirms that the core flow is internally consistent: profile and subject data feed matching; recommendations lead to requests; accepted requests lead to project groups; and notifications communicate state changes. It also exposes implementation requirements involving eligibility, group capacity, empty results, account moderation, and data quality.

## 2. Project Overview

### 2.1 Problem Statement

MMDC students may know only a limited number of classmates taking a particular subject. Potential groupmates can differ in IT major, available days and times, employment commitments, and preferred collaboration style. Informal group formation is slow and often prioritizes immediate availability over compatibility. This can delay project work and create coordination problems after a group is formed.

### 2.2 Project Objective

StudySync aims to provide a centralized platform for finding compatible groupmates for subject-based projects. The behavioral objective is to support the complete process:

**Account registration → Collaboration profile → Subject selection → Potential groupmates → Groupmate request → Acceptance or decline → Project group formation → Notifications**

### 2.3 Scope

The system includes:

- Student registration, login, logout, and profile management.
- Subject enrollment and looking-for-group status.
- Rule-based groupmate recommendations.
- Request sending, acceptance, decline, cancellation, and status tracking.
- Project-group creation and membership display.
- In-app notifications.
- User reporting and basic administrative management.

The system excludes learning materials, project task management, file sharing, synchronous meetings, grading, official MMDC enrollment integration, and advanced AI recommendations.

### 2.4 Behavioral Artifacts

| Artifact | Behavioral Question Answered |
| --- | --- |
| Use Case Diagram and Narratives | Who interacts with StudySync, what goals do they pursue, and what normal or alternative outcomes occur? |
| Sequence Diagram | In what order do system components collaborate to complete Find Potential Groupmates? |
| Low-Fidelity Prototype | What screens, controls, information, and navigation paths let users perform those behaviors? |

## 3. Use Case Analysis

### 3.1 Actors

**Student** is the primary actor. The student registers, maintains matching data, selects subjects, finds and evaluates candidates, exchanges requests, forms groups, views notifications, and reports users.

**Administrator** is the secondary actor. The administrator manages accounts and subjects and reviews reports.

**User** is a general actor capturing behavior shared by Student and Administrator, primarily authentication.

No external platform is modeled as an actor because StudySync does not integrate with other MMDC systems.

### 3.2 Use Case Diagram

![StudySync Use Case Diagram](StudySync_Use_Case_Diagram.png)

*Figure 1. StudySync Use Case Diagram. Editable source: [StudySync Milestone 2 — Figma](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK).*

### 3.3 Major Use Cases

| ID | Use Case | Behavioral Contribution |
| --- | --- | --- |
| UC-01 | Register Account | Establishes the student's system identity. |
| UC-02 | Log In / Log Out | Controls authenticated access. |
| UC-03 | Manage Collaboration Profile | Captures the criteria used by matching. |
| UC-04 | Manage Subject Enrollment | Defines subject eligibility and looking-for-group state. |
| UC-05 | Find Potential Groupmates | Filters, scores, and ranks eligible candidates. |
| UC-06 | Send Groupmate Request | Initiates agreement with a candidate. |
| UC-07 | Respond to Groupmate Request | Records acceptance or decline. |
| UC-08 | Form Project Group | Creates the agreed group and memberships. |
| UC-09 | View Notifications | Communicates matching, request, and group changes. |
| UC-10 | Report User | Starts the moderation process. |
| UC-11 | Manage Student Accounts | Supports account maintenance and enforcement. |
| UC-12 | Manage Subjects | Maintains the subject catalog. |
| UC-13 | Review Reported Users | Resolves reports and may update account status. |

### 3.4 Relationship Analysis

The diagram separates mandatory shared behavior from conditional behavior:

- UC-03 **includes** Set Availability.
- Provide Work Schedule **extends** UC-03 when employment status is `WORKING_STUDENT`.
- UC-05 **includes** Calculate Compatibility.
- View Candidate Profile and UC-06 **extend** the discovery flow after results exist.
- UC-08 **extends** UC-07 when the request is accepted.
- UC-06, UC-07, and UC-08 **include** Send Notification.
- Update Account Status **extends** UC-13 when a report resolution requires an account action.

The most important behavioral chain is UC-03 → UC-04 → UC-05 → UC-06 → UC-07 → UC-08. Each step has a clear precondition and produces state required by the next step.

### 3.5 Narrative Findings

The detailed narratives in the Use Case Diagram worksheet define normal and alternative courses for nine major use cases. They identify key behavioral rules:

- Duplicate accounts and duplicate pending requests are rejected.
- Only same-subject students marked as looking for a group are eligible.
- A declined request does not create a group.
- Group-capacity validation occurs before accepting or adding a member.
- Report resolution and account action are separate decisions.
- Invalid actions preserve consistent system state and return useful feedback.

## 4. Sequence Analysis

### 4.1 Selected Use Case

UC-05 Find Potential Groupmates was selected because it represents StudySync's central value and the deepest service dependency chain. It depends on authentication, subject enrollment, profile data, matching, persistence, and notifications.

### 4.2 Sequence Diagram

![StudySync Sequence Diagram](StudySync_Sequence_Diagram.png)

*Figure 2. Sequence Diagram for UC-05 Find Potential Groupmates. Editable source: [StudySync Milestone 2 — Figma](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK).*

### 4.3 Interaction Flow

1. The Student selects a subject and chooses Find Groupmates.
2. `StudentWebUI` sends the subject to `APIGateway`.
3. The gateway validates the session through `AuthenticationService`.
4. For a valid session, `MatchingEngine` requests eligible candidate IDs from `SubjectEnrollmentService`.
5. `SubjectEnrollmentService` queries enrollment information through `DataAccessLayer`.
6. If no eligible candidate exists, an empty result with guidance returns.
7. Otherwise, a loop retrieves each candidate's profile and availability through `ProfileService`.
8. `MatchingEngine` calculates compatibility.
9. An `opt` fragment creates a recommendation only when the score meets `minimumScore`.
10. Recommendations are ranked and persisted.
11. `NotificationService` records a matching notification.
12. The ranked list returns to the UI.

### 4.4 Alternative and Conditional Behavior

The first `alt` fragment handles invalid sessions. The second handles an empty eligible-candidate set. The `loop` fragment expresses repeated scoring. The `opt` fragment enforces the minimum-score rule. These fragments prevent normal-flow assumptions from hiding important edge cases.

### 4.5 Alignment with Milestone 1

Every sequence participant corresponds to a Milestone 1 component. Messages reflect the responsibilities and operations of `MatchingService`, `SubjectEnrollment`, `CollaborationProfile`, and related classes. Persistence occurs only through `DataAccessLayer`, maintaining the architecture's rule that services do not access the database directly.

## 5. Low-Fidelity Prototype

### 5.1 Prototype Approach

The prototype uses grayscale desktop wireframes at 1440-pixel width. It emphasizes information organization, navigation, controls, and behavioral feedback instead of visual polish. Its clickable flows are stored on the **Low-Fidelity Prototype** page of the [StudySync Milestone 2 Figma file](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK).

Figma contains two prototype starting points:

- **Student Flow**, beginning at Login.
- **Admin Flow**, beginning at Admin Dashboard.

Twelve clickable hotspots connect the primary student actions and administrative navigation.

### 5.2 Student Screens

| Screen | Supported Behavior | Main Elements |
| --- | --- | --- |
| Login | UC-02 | MMDC email, password, login action, registration link. |
| Register | UC-01 | Name, email, password, confirmation, create-account action. |
| Profile Setup | UC-03 | Major, employment status, work schedule, availability, working style. |
| My Subjects | UC-04 | Subject list and looking-for-group toggles. |
| Potential Groupmates | UC-05 | Ranked candidate cards, compatibility percentages, profile actions. |
| Candidate Profile | UC-05/06/10 | Profile details, compatibility explanation, Send Request, Report User. |
| Requests Inbox | UC-07 | Pending request, Accept, Decline, and group-capacity alternative state. |
| Project Group | UC-08 | Subject, status, capacity, roles, and member roster. |
| Notifications | UC-09 | Request, group, and matching updates. |

### 5.3 Administrator Screens

| Screen | Supported Behavior | Main Elements |
| --- | --- | --- |
| Admin Dashboard | Administrative overview | Account, subject, and pending-report summaries. |
| Manage Accounts | UC-11 | Search, status display, and account detail actions. |
| Manage Subjects | UC-12 | Subject list and add/edit actions. |
| Review Reports | UC-13 | Report status, reason, review action, and resolution guidance. |

### 5.4 Navigation Paths

The principal student path is:

**Register → Login → Profile Setup → My Subjects → Potential Groupmates → Candidate Profile → Requests Inbox → Project Group → Notifications**

The administrative path is:

**Admin Dashboard → Manage Accounts / Manage Subjects / Review Reports**

A traceability flow map in Figma groups these navigation paths by use case ID.

### 5.5 Information and Decision Points

The prototype makes the following decisions visible:

- Employment status controls whether work-schedule information is needed.
- Looking-for-group toggles determine matching eligibility by subject.
- Compatibility percentages help students interpret ranking.
- Accept and Decline are explicit request-state transitions.
- A capacity warning prevents an invalid acceptance.
- Report statuses distinguish pending, under-review, and resolved work.

## 6. Consistency and Usability Evaluation

### 6.1 Prototype Against Use Cases

All thirteen major use cases have a screen or visible action. UC-01 through UC-09 form a complete student flow. UC-10 appears on Candidate Profile. UC-11 through UC-13 are represented by dedicated administrator screens.

Supporting use cases are also visible: availability and work schedule appear on Profile Setup; compatibility appears in ranked results; candidate details appear on Candidate Profile; notifications have a dedicated screen; account-status consequences are acknowledged in the report and account screens.

### 6.2 Prototype Against Use Case Narratives

Normal cases are represented by the primary clickable path. Important alternatives are present where they materially affect screen design:

- Login and registration validation have field-level locations for messages.
- No-match behavior is defined in the UC-05 narrative and sequence flow.
- Requests expose both Accept and Decline.
- Requests Inbox displays a group-capacity warning.
- Reporting and moderation support dismissal or account action.

The prototype does not simulate every error interactively because it is low fidelity, but the screens reserve clear locations for feedback.

### 6.3 Prototype Against Sequence Diagram

The My Subjects screen supplies `subjectId`, and the Find Groupmates action initiates the sequence. Potential Groupmates displays the returned ranked `MatchRecommendation[0..*]` values. Candidate cards expose the score produced by `calculateCompatibility`. The notification screen represents the `MATCH_FOUND` output.

The sequence diagram's invalid-session and no-candidate branches imply login/error and empty-result states. These requirements are documented for implementation even where the low-fidelity prototype shows the main success state.

### 6.4 Workflow Completeness

The student can move from having no account to having a visible project group. The administrator can reach each maintenance function from a single dashboard. Page labels include use case IDs so reviewers can trace each screen back to the behavioral model.

### 6.5 Usability Review

The interface uses consistent top navigation, side navigation, card structure, labels, and primary/secondary actions. Related inputs are grouped. Compatibility is shown as both a percentage and explanatory profile information. Destructive or negative actions use secondary styling rather than competing with the main path.

The principal usability risk is information accuracy rather than navigation complexity. Students must understand that recommendations depend on self-reported availability and preferences. Production design should therefore explain why fields matter and make updates easy.

## 7. Behavioral Findings

### 7.1 Confirmed Strengths

- The behavioral and structural models use the same actors, components, classes, and state values.
- The complete project objective can be demonstrated without integration with external MMDC systems.
- Alternative outcomes are identifiable and can be presented without complicating the main flow.
- The separation of matching, requests, groups, and notifications gives each state transition a clear owner.

### 7.2 Missing or Refined Requirements

The analysis identifies requirements that should be finalized before implementation:

1. Define the exact compatibility formula and criterion weights.
2. Define how partial schedule overlap is scored.
3. Define maximum group size per subject.
4. Decide whether a student may join more than one group for the same subject.
5. Define whether accepted requests automatically create a group or first join an existing forming group.
6. Define recommendation expiry and refresh behavior.
7. Define report-resolution reasons and the permissions required for account suspension or deactivation.
8. Define how users are informed when their profile is incomplete or no candidates qualify.

### 7.3 Risk Analysis

Matching quality depends on accurate self-reported data. In-app-only notifications may delay responses when students do not open StudySync. Locally maintained subject data may drift from actual enrollment. `MatchingEngine` is highly coupled and requires careful testing because changes to profile or enrollment rules affect its output.

## 8. Recommendations

1. Specify and test the compatibility formula with representative student scenarios.
2. Add a profile-completeness indicator before enabling matching.
3. Model `RequestStatus`, `GroupStatus`, and `ReportStatus` transitions as state diagrams.
4. Add prototype variants for no matches, invalid session, duplicate request, and report confirmation.
5. Conduct a short usability test with MMDC students, including working students.
6. Verify terminology with the intended users, particularly “working style,” “availability,” and “looking for group.”
7. Keep accessibility in the production design through labeled controls, keyboard support, responsive layouts, and non-color status cues.
8. Preserve the current scope boundary so implementation remains feasible within the academic term.

## 9. References

1. [Group Proposal Document](MS1%20Files/Group%20Proposal%20Document%20.md)
2. [High-Level Project Structural Analysis of StudySync](MS1%20Files/High-Level%20Project%20Structural%20Analysis%20of%20StudySync.md)
3. [Project Class Diagram](MS1%20Files/Project%20Class%20Diagram.md)
4. [Project Component Diagram](MS1%20Files/Project%20Component%20Diagram%20.md)
5. [Project Use Case Diagram Worksheet](MO-IT115%20Homework_%20Project%20Use%20Case%20Diagram%20_%20H3101%20_%20Colin%20Bactong%20Chelsie%20Mae%20Ricafrente%20Angelica%20Mae%20Calipayan%20Charlize%20Bactong.md)
6. [Project Sequence Diagram Worksheet](MO-IT115%20Homework_%20Project%20Sequence%20Diagram%20_%20H3101%20_%20Colin%20Bactong%20Chelsie%20Mae%20Ricafrente%20Angelica%20Mae%20Calipayan%20Charlize%20Bactong.md)
7. [StudySync Milestone 2 — Figma](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK)
