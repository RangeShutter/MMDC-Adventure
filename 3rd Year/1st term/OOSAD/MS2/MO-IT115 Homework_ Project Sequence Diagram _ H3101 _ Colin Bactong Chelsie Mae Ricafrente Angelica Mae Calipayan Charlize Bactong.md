# Project Sequence Diagram

| MO-IT115 Object-Oriented System Analysis & Design | |
| :--- | :--- |
| **Milestone 2 Team Leader** | Colin Bactong |
| **Members** | Chelsie Mae Ricafrente; Angelica Mae Calipayan; Charlize Bactong |
| **Program and Year Level** | BSIT Marketing Technology & Software Development, 3rd Year |

## 1. Project Context

StudySync is a web-based groupmate-matching platform for MMDC students. It addresses the difficulty of finding classmates who take the same subject and have compatible availability, employment schedules, and working preferences.

The sequence diagram models **UC-05 Find Potential Groupmates**, the core behavior that turns profile and enrollment data into ranked recommendations. This use case is important because it replaces an informal manual search with a repeatable rule-based process and supplies the candidates from which requests and project groups originate.

## 2. Selected Use Case Overview

| Item | Description |
| --- | --- |
| Use Case ID | UC-05 |
| Use Case Name | Find Potential Groupmates |
| Primary Actor | Student |
| Business Goal | Identify eligible same-subject students and rank them using predefined compatibility criteria. |
| Expected Outcome | The student receives a ranked list of candidates whose scores meet the configured minimum threshold, or a clear empty state when no candidate qualifies. |

## 3. Participant Identification

| Participant | Type | Responsibility |
| --- | --- | --- |
| Student | Actor | Selects the subject and requests recommendations. |
| `StudentWebUI` | Boundary | Captures the action and displays results or errors. |
| `APIGateway` | Control/Component | Validates the request path and coordinates application services. |
| `AuthenticationService` | Control/Component | Validates the session and returns the current student ID. |
| `MatchingEngine` | Control/Component | Orchestrates candidate filtering, scoring, ranking, and recommendation creation. |
| `SubjectEnrollmentService` | Control/Component | Finds same-subject students marked as looking for a group. |
| `ProfileService` | Control/Component | Supplies collaboration profiles and availability. |
| `NotificationService` | Control/Component | Creates the `MATCH_FOUND` notification. |
| `DataAccessLayer` | Entity/Persistence Component | Reads and stores enrollments, profiles, recommendations, and notifications. |

## 4. Sequence Diagram

![StudySync Sequence Diagram](StudySync_Sequence_Diagram.png)

*Figure 1. UC-05 Find Potential Groupmates. Editable source: [StudySync Milestone 2 — Figma](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK).*

## 5. Interaction Analysis

The interaction begins when the Student selects a subject and chooses **Find Groupmates**. `StudentWebUI` sends the request to `APIGateway`, which first asks `AuthenticationService` to validate the session. An invalid session follows the first `alt` branch and returns an authentication error without running the matching process.

For a valid session, `MatchingEngine` asks `SubjectEnrollmentService` for students in the selected subject who are still looking for a group. The service queries `DataAccessLayer` and excludes the requesting student. If no eligible candidates exist, the second `alt` branch returns an empty result with guidance.

When candidates exist, the `loop` fragment processes each candidate. `ProfileService` retrieves profile and availability data, and `MatchingEngine` calculates compatibility using major, availability, employment/work schedule, and working style. The `opt` fragment creates a recommendation only when the score is at or above `minimumScore`. Qualifying recommendations are ranked and stored. `NotificationService` records a `MATCH_FOUND` notification, and the ranked list returns through the gateway to the UI.

The sequence uses solid arrows for calls and dashed arrows for return messages. Activations identify which participant currently performs work.

## 6. Sequence Flow Explanation

| Step | Interaction Description |
| --- | --- |
| 1 | Student selects a subject and requests potential groupmates. |
| 2 | `StudentWebUI` calls `APIGateway.requestRecommendations(subjectId)`. |
| 3–4 | `APIGateway` validates the session through `AuthenticationService` and receives the student ID. |
| 5a | If the session is invalid, the UI receives an authentication error and the flow stops. |
| 5b | If valid, `APIGateway` calls `MatchingEngine.generateRecommendations(studentId, subjectId)`. |
| 6–9 | `MatchingEngine` obtains eligible same-subject candidate IDs through `SubjectEnrollmentService` and `DataAccessLayer`. |
| 10a | If the candidate set is empty, the gateway receives an empty result and guidance. |
| 10b–13 | For each candidate, `ProfileService` retrieves profile and availability information. |
| 14 | `MatchingEngine` calculates a compatibility score. |
| 15 | If the score meets `minimumScore`, a `MatchRecommendation` is created. |
| 16–17 | Ranked recommendations are persisted. |
| 18–19 | `NotificationService` stores a `MATCH_FOUND` notification. |
| 20–22 | Recommendations return through `APIGateway` and `StudentWebUI` displays the ranked list. |

## 7. Design Decisions

The participants were selected from the approved component model so that the behavioral diagram remains consistent with Milestone 1. `StudentWebUI` is modeled as a boundary, the gateway and services as controls, and `DataAccessLayer` as the persistence-facing participant. The StudySync Database is not shown separately because all storage is intentionally mediated by `DataAccessLayer`; including both would repeat every persistence message without improving the use-case explanation.

Authentication appears first because UC-05 requires a signed-in student. Candidate filtering occurs before profile retrieval to avoid loading data for students who are in another subject or are not looking for a group. Compatibility calculation remains inside `MatchingEngine`, matching the `MatchingService` responsibility in the class diagram.

The diagram intentionally abstracts individual database queries and the exact weighting formula. Those details belong to implementation design. It assumes that profile and enrollment information is current, matching is rule-based rather than AI-driven, and a configurable threshold limits low-quality recommendations.

The main modeling challenge was representing error handling without obscuring the success flow. Separate `alt` fragments clarify invalid-session and no-candidate outcomes, while `loop` and `opt` show repeated candidate scoring and conditional recommendation creation.

## 8. Behavioral Analysis Findings

The diagram confirms that matching depends on three critical conditions: a valid session, same-subject eligibility, and complete collaboration-profile data. `MatchingEngine` is the behavioral center of UC-05 and is also the most coupled component because it coordinates enrollment, profile, persistence, and notification services.

The analysis exposed requirements that must remain explicit during implementation:

1. The requesting student must be excluded from their own results.
2. Only students marked `lookingForGroup = true` for the selected subject are eligible.
3. Recommendations below `minimumScore` must not be displayed.
4. Empty results need actionable guidance rather than a generic error.
5. Ranked output and stored recommendation records must remain consistent.

The sequence directly informed the low-fidelity prototype: the My Subjects screen supplies the selected subject, the Potential Groupmates screen presents ranked recommendations, and the empty/error states define alternative UI feedback.

## 9. References

| Resource Name | Reference |
| --- | --- |
| Team Project Proposal | [Group Proposal Document](MS1%20Files/Group%20Proposal%20Document%20.md) |
| High-Level Structural Analysis | [High-Level Project Structural Analysis](MS1%20Files/High-Level%20Project%20Structural%20Analysis%20of%20StudySync.md) |
| Use Case Diagram Worksheet | [Project Use Case Diagram](MO-IT115%20Homework_%20Project%20Use%20Case%20Diagram%20_%20H3101%20_%20Colin%20Bactong%20Chelsie%20Mae%20Ricafrente%20Angelica%20Mae%20Calipayan%20Charlize%20Bactong.md) |
| Project Class Diagram | [Project Class Diagram](MS1%20Files/Project%20Class%20Diagram.md) |
| Project Component Diagram | [Project Component Diagram](MS1%20Files/Project%20Component%20Diagram%20.md) |
| Figma File | [StudySync Milestone 2](https://www.figma.com/design/xcfawiquqex4sFrLCXJdlK) |
