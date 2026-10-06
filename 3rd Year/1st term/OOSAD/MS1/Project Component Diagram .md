

# **Intellectual Property Notice**

This template is an exclusive property of **Mapua-Malayan Digital College** and is protected under **Republic Act No. 8293**, also known as the *Intellectual Property Code of the Philippines* (IP Code). It is provided solely for educational purposes within this course. Students may use this template to complete their tasks but may not **modify, distribute, sell, upload,** or **claim ownership** of the template itself. Such actions constitute copyright infringement under **Sections 172, 177, and 216** of the IP Code and may result in legal consequences. Unauthorized use beyond this course may result in legal or academic consequences.

Additionally, students must comply with the **Mapua-Malayan Digital College Student Handbook**, particularly with the following provisions:

- **Offenses Related to MMDC IT**:  
  - **Section 6.2** – Unauthorized copying of files  
  - **Section 6.8** – Extraction of protected, copyrighted, and/or confidential information by electronic means using MMDC IT infrastructure
- **Offenses Related to MMDC Admin, IT, and Operations**:  
  - **Section 4.5** – Unauthorized collection or extraction of money, checks, or other instruments of monetary equivalent in connection with matters pertaining to MMDC

Violations of these policies may result in **disciplinary actions ranging from suspension to dismissal**, in accordance with the Student Handbook.

For permissions or inquiries, please contact MMDC-ISD at [isd@mmdc.mcl.edu.ph](mailto:isd@mmdc.mcl.edu.ph). 


| MO-IT115 Object-Oriented System Analysis & Design |     |
| ------------------------------------------------- | --- |
| Project Component Diagram                         |     |



| Milestone 1 Team Leader     | Colin Bactong                                                                     |
| --------------------------- | --------------------------------------------------------------------------------- |
| **Members:**                | Charlize Bactong                                                                  |
|                             | Angelica Mae Calipayan                                                            |
|                             | Chelsie Mae Ricafrente                                                            |
| **Program and Year Level:** | 3rd Year BSIT Major in Software Development, Marketing Technology & Cybersecurity |




## 1. Component Identification

**StudySync: MMDC Student Groupmate Matching System** is organized as a web-based application with a browser presentation layer, an application layer of cooperating service components, and a shared data layer. Each component groups the classes from the class diagram that share a single responsibility.


| Component Name             | Purpose/Responsibility                                                                                                                                                                                                                                                         |
| -------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `StudentWebUI`             | Browser-based interface used by students to register, manage their collaboration profile, select subjects, view potential groupmates, send or respond to requests, join project groups, and read notifications. Presents the behavior of the `Student` class.                  |
| `AdminWebUI`               | Browser-based interface used by administrators to manage student accounts, maintain subjects, and review reported users or inappropriate activity. Presents the behavior of the `Administrator` class.                                                                         |
| `APIGateway`               | Single entry point for all browser requests. Handles routing, session validation, and dispatch to the correct application service, so the user interfaces do not depend on individual services directly.                                                                       |
| `AuthenticationService`    | Handles registration, login, logout, session issuance, account status, and role-based access control. Implements the shared behavior of the `User` class and distinguishes the `Student` and `Administrator` roles.                                                            |
| `ProfileService`           | Creates and updates a student's collaboration profile, including IT major, employment status, work schedule, working-style preference, and available day and time ranges. Manages the `CollaborationProfile` and `Availability` classes.                                       |
| `SubjectEnrollmentService` | Maintains the list of MMDC subjects and records which students are enrolled in each subject and whether they are currently looking for a group. Manages the `Subject` and `SubjectEnrollment` classes.                                                                         |
| `MatchingEngine`           | Filters eligible students in a subject, compares their profile information against predefined compatibility criteria, calculates a compatibility score, and produces ranked recommendations. Implements the `MatchingService` class and creates `MatchRecommendation` records. |
| `RequestService`           | Manages groupmate requests between two students for a subject, including sending, accepting, declining, cancelling, and tracking request status. Manages the `GroupmateRequest` class.                                                                                         |
| `GroupService`             | Creates project groups after students agree to collaborate, adds and removes members, and provides group membership and basic group information. Manages the `ProjectGroup` and `GroupMembership` classes.                                                                     |
| `NotificationService`      | Generates and delivers in-app updates about recommendations, groupmate requests, request responses, group invitations, and group changes. Manages the `Notification` class.                                                                                                    |
| `ModerationService`        | Accepts reports submitted by students about other users or inappropriate activity, stores them for review, and records the administrator's resolution. Manages the `UserReport` class.                                                                                         |
| `DataAccessLayer`          | Provides a consistent persistence interface for all application services, translating domain objects into database operations and keeping query logic out of the service components.                                                                                           |
| `Database`                 | Stores all persistent system data, including accounts, collaboration profiles, availability, subjects, enrollments, recommendations, requests, groups, memberships, notifications, and reports.                                                                                |

These components are organized into six `«subsystem»` packages, each a collection of related components supporting one larger business function.

| Subsystem | Components | Business Function |
| ----- | ----- | ----- |
| Presentation | `StudentWebUI`, `AdminWebUI` | Browser-based user interaction |
| Gateway | `APIGateway` | Request routing and session validation |
| Account and Access | `AuthenticationService`, `ModerationService` | Account identity, access control, and moderation |
| Profile and Enrollment | `ProfileService`, `SubjectEnrollmentService` | Collaboration profiles and subject enrollment |
| Matching and Collaboration | `MatchingEngine`, `RequestService`, `GroupService` | Groupmate discovery, requests, and group formation |
| Notification | `NotificationService` | Delivery of in-app status updates |
| Data | `DataAccessLayer`, `Database` | Persistence of all system data |

Each component publishes its services through a named interface. The following interfaces define the contracts between components.

| Interface | Provided By | Required By |
| ----- | ----- | ----- |
| `IStudentRequests` | `APIGateway` | `StudentWebUI` |
| `IAdminRequests` | `APIGateway` | `AdminWebUI` |
| `IAuthentication` | `AuthenticationService` | `APIGateway`, `ModerationService` |
| `IProfileManagement` | `ProfileService` | `APIGateway`, `MatchingEngine` |
| `ISubjectEnrollment` | `SubjectEnrollmentService` | `APIGateway`, `MatchingEngine`, `GroupService` |
| `IMatching` | `MatchingEngine` | `APIGateway`, `RequestService` |
| `IGroupmateRequest` | `RequestService` | `APIGateway`, `GroupService` |
| `IGroupManagement` | `GroupService` | `APIGateway` |
| `INotification` | `NotificationService` | `APIGateway`, `MatchingEngine`, `RequestService`, `GroupService` |
| `IModeration` | `ModerationService` | `APIGateway` |
| `IDataAccess` | `DataAccessLayer` | All eight application services |




## 2. Component Diagram

![StudySync Component Diagram](StudySync_Component_Diagram.png)

*Figure: StudySync component diagram, drawn in standard UML component notation. Full-resolution versions are available as `StudySync_Component_Diagram.svg` and `StudySync_Component_Diagram.pdf`. The diagram is generated from the PlantUML source `StudySync_Component_Diagram.puml`.*




## 3. Component Interaction Analysis

The diagram is read through UML component notation. Each component is a modular, replaceable unit marked `«component»` and carrying the component icon. Related components are grouped into `«subsystem»` packages. Every connection between components passes through a named interface drawn ball-and-socket: the full circle, or ball, marks the interface a component **provides**, and the open half-circle, or socket, marks the interface a component **requires**. Where a socket meets a ball the two form an assembly connector, meaning the required service is satisfied by the provided one. The small squares on the boundary of `APIGateway` and `DataAccessLayer` are ports, the defined interaction points through which those components expose or access their interfaces. The dashed arrow to the database is a `«use»` dependency, used only where no interface contract applies.

All user interaction begins in the Presentation Subsystem. `StudentWebUI` and `AdminWebUI` run in the student's or administrator's web browser and require `IStudentRequests` and `IAdminRequests`, the two interfaces provided by `APIGateway` through its `studentPort` and `adminPort`. Neither interface communicates with an application service directly, so the presentation layer depends only on the gateway. This keeps the browser code independent of how the services are internally organized.

`APIGateway` requires `IAuthentication` and validates the session with `AuthenticationService` before forwarding any request that needs a signed-in user. It then dispatches the request to the responsible service through that service's provided interface, such as `IProfileManagement` for profile updates or `IMatching` for recommendation requests. `AuthenticationService` also supplies the user's role, which determines whether administrative operations in `ModerationService` and `SubjectEnrollmentService` are permitted.

The matching flow shows the most significant dependencies between services. `MatchingEngine` requires both `IProfileManagement` and `ISubjectEnrollment`, which is visible on the diagram as two sockets reaching out of the Matching and Collaboration Subsystem. When a student requests potential groupmates, it reads collaboration profile information from `ProfileService` and subject enrollment information from `SubjectEnrollmentService`. It uses the enrollment records to restrict candidates to students taking the same subject who are marked as looking for a group, then compares IT major, availability, employment schedule, and working-style preference to compute a compatibility score. The resulting recommendations are returned to the student through the gateway.

`RequestService` requires `IMatching` because a groupmate request usually originates from a displayed recommendation. `GroupService` requires `IGroupmateRequest` so that an accepted request can be used to establish a project group and create the corresponding membership records, and it also requires `ISubjectEnrollment` to verify that each member is enrolled in the group's subject.

`NotificationService` is a shared provider rather than a caller. It provides a single interface, `INotification`, which `MatchingEngine`, `RequestService`, `GroupService`, and `APIGateway` all require. Because those components depend on the notification contract rather than on one another for messaging, notification behavior can change without affecting the matching or group-formation logic.

On the administrative path, a student submits a report through `IModeration`, provided by `ModerationService`, which stores it for review. An administrator retrieves pending reports through `AdminWebUI` and records a resolution. When a resolution affects an account, `ModerationService` requires `IAuthentication` to update that account's status.

Every application service requires `IDataAccess`, the single interface provided by `DataAccessLayer`. This is the most widely required interface in the architecture, with eight consuming components. `DataAccessLayer` then reaches the database through its `dbPort` as a `«use»` dependency, so no service accesses storage directly. Together these interactions support the system's complete flow: account creation, profile and subject setup, groupmate discovery, requests, group formation, notifications, and administration.

## 4. Architectural Decisions

The components were selected by grouping the classes from the class diagram according to shared responsibility rather than by screen or page. Each application component owns a small, closely related set of classes: `ProfileService` owns `CollaborationProfile` and `Availability`, `SubjectEnrollmentService` owns `Subject` and `SubjectEnrollment`, `GroupService` owns `ProjectGroup` and `GroupMembership`, and so on. This keeps each component cohesive and makes the boundary between components match the boundaries already established in the class model.

The class diagram directly influenced the structure in several places. `MatchingService` was already modeled as a service class rather than a domain entity, so it became the separate `MatchingEngine` component. The association classes `SubjectEnrollment` and `GroupMembership` were kept inside the components that own their parent classes, because they exist only to connect a student to a subject or a group. The inheritance of `Student` and `Administrator` from the abstract `User` class is handled entirely within `AuthenticationService`, which is why role checking is centralized there instead of being repeated in each service.

Several components were deliberately combined or simplified. `MatchingEngine` also produces and stores `MatchRecommendation` records instead of a separate recommendation component, since a recommendation has no meaning apart from the matching process. `NotificationService` handles all notification types through one component rather than separate components per event, matching the class diagram's decision to use a general `type` field. A separate invitation component was not created, because group membership is established from an accepted groupmate request. Likewise, no external email or push-delivery component is included, since notifications are kept in-app and within the project's scope.

`APIGateway` and `DataAccessLayer` were added even though they have no matching class in the class diagram. The gateway gives the browser a single point of contact and a consistent place for session validation, while the data access layer keeps persistence logic out of the individual services. Both reflect the proposal's non-functional requirements for security and reliability.

The architecture assumes a three-layer web deployment accessible from common desktop and mobile browsers, as stated in the proposal's Compatibility requirement. It also assumes that all StudySync data is stored in a single system-owned database, because direct integration with official MMDC enrollment records is outside the project's scope, and that matching remains rule-based on predefined criteria rather than an AI recommendation engine.

The main challenge during the analysis was deciding how finely to divide the application layer. An earlier grouping placed matching, requests, and group formation in one large component, which hid the dependencies between them, while dividing every class into its own component produced too many small pieces for an academic-term project. The final set balances the two by keeping components aligned with the major functions defined in the proposal. A related challenge was avoiding scope creep toward features already provided by Coursera, MyCamu, Google Meet, and Google Workspace; those responsibilities were intentionally excluded from the architecture.

**Notation corrections applied after review.** An earlier version of this diagram was drawn as a layered flowchart, with components shown as plain boxes and interface names written as text labels on the connecting arrows. That is not UML component notation. The diagram has been redrawn so that components carry the `«component»` stereotype and icon, related components are enclosed in `«subsystem»` packages, and each interface is modelled as a first-class element connected ball-and-socket to the components that provide and require it. Ports were added to `APIGateway` and `DataAccessLayer` to mark their explicit interaction points. A legend on the diagram records the meaning of each symbol.

**Decisions arising from the notation change.** Making the interfaces explicit changed how the architecture is grouped. The three informal layers became six named subsystems, which the course material identifies as collections of related components supporting a larger business function. `AuthenticationService` and `ModerationService` were placed together in the Account and Access Subsystem because both operate on accounts, and `APIGateway` was given its own subsystem because it mediates between the browser and every other subsystem rather than belonging to any one of them. Drawing the sockets also made the real coupling visible in a way the flowchart did not: `IDataAccess` is required by eight components and `INotification` by four, while `MatchingEngine` is the only service that requires two other business interfaces, which confirms it as the most coupled component in the system.

## 5. Project Resources

| Resource Name         |     Reference     |
| --------------------- | :---------------: |
| Team Project Proposal | *\<paste link\>*  |
| Class Diagram         | *\<paste link\>*  |


