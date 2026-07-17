# HM3: Payroll System Expansion — Design Planning

This document plans how GEAR.HR payroll should grow beyond what we have today. It is design-level work: we describe what to add, who it serves, and why we chose certain directions before we build them. The goal is to justify design choices for course discussion (Homework 3) and to give a clear map for later implementation (Homework 4). Our earlier baseline reflection lives in [HM1: Pre-Connection Planning](HM1.md); HM3 builds on that picture and focuses specifically on payroll expansion.

---

## Current Payroll Scope

GEAR.HR already handles payroll for a real-sized workforce: the employee directory lists about thirty-eight people, and pay settings exist for most of them, though some individuals can appear in the directory before anyone has entered their pay details—a gap payroll staff must catch manually.

Pay is understood on a monthly rhythm. For a selected month, the program combines each person’s stored pay settings (base salary, hourly rate, and standard allowances) with hours drawn from attendance records. Gross pay comes from the hourly rate multiplied by hours worked in that month. From there the system applies Philippine statutory deductions—SSS, PhilHealth, Pag-IBIG, and withholding tax—along with allowances such as rice subsidy, phone allowance, and clothing allowance, to arrive at take-home pay. The rules for those deductions and taxes are already built into the program; staff do not re-enter formula logic each pay cycle.

What different users can do today is shaped by role. Regular employees open their own profile and see a personal pay breakdown for a month. Payroll staff and IT or admin roles can change pay settings for others. HR can look at payroll information for oversight but cannot edit pay figures—a deliberate separation of duties carried forward from our HM1 notes. Payroll staff can also view a readable text summary for one employee at a time. What the system does not yet offer is a payroll-office view of the whole company for one closed month: no single “run payroll for everyone” step, no stored history of a completed company-wide run, and no formal record of who changed pay data or when.

The sensitive information in this area is exactly what you would expect in a real payroll office: salaries and hourly rates, deduction and tax amounts, allowance figures, hours worked, and the resulting net pay. Protecting that information is one reason expansion must rest on a stronger data foundation than scattered files on disk.

The motivating gap is operational, not mathematical. For one person, on one screen, the numbers can be right. For a payroll department trying to close a month, serve leadership, and answer “what did we pay and deduct company-wide?”, today’s tools are thin: one-by-one viewing, no batch closure, no audit trail, and no pay period that everyone agrees is “done.”

---

## Expansion Vision

Payroll expansion for GEAR.HR means strengthening the payroll office inside the same desktop program—not replacing HR features or rebuilding the product from scratch. We want three complementary capabilities.

First, **reports**. Payroll should be able to produce a company-wide pay run for a chosen month: a payroll register listing each employee’s gross, deductions, allowances, and net, plus summary views such as company totals and deduction summaries useful when discussing tax and statutory compliance. Employees would still receive personal pay views, but those views would align with an official run the payroll team generated for that period.

Second, **audit**. When pay settings change or a pay period is finalized, the system should record who acted, when, and what kind of action occurred (for example, creating or updating pay settings, or closing a period). A first version does not need to log every single field change; a clear minimum is a change log tied to the signed-in user so silent edits are discouraged and questions can be answered later.

Third, **workflow**. Payroll should revolve around named **pay periods** (such as a month and year) with a simple status path—for example, draft while numbers are being checked, reviewed when HR or leadership has looked, and finalized when payroll locks the period. Only payroll staff (and IT or admin where appropriate) should finalize; HR may review but not release pay. Finalizing should mean the period’s results are stored as snapshots so later rate changes do not rewrite history.

Together, reports, audit, and workflow turn GEAR.HR from a helpful calculator with screens into something closer to how a small or mid-sized company actually runs monthly payroll.

---

## Phased Design Plan

We order work in phases so design choices stay realistic. The database is **not** connected yet; Phase 1 is part of this expansion plan, not something we assume is already finished.

**Phase 1 — Data foundation.** Move employee, attendance, leave, payroll, and login data from separate saved files into an organized database. We plan to connect the Java desktop application to that database using **JDBC**, the standard approach taught in the course for database access. One reliable store is required before company-wide pay runs, period history, and audit entries can be kept safely. File storage remains acceptable for learning the current app, but it is a poor fit for multi-row pay history and change logs at scale.

**Phase 2 — Pay periods and batch computation.** Introduce the pay period as a proper record in the design—not only “pick a month from a list” on a screen, but a stored period with a status. For a given period, payroll runs computation for all employees (or a defined active set) in one operation. The run should surface exceptions: missing attendance hours, missing pay setup, or other blockers so the team fixes them before closing the month.

**Phase 3 — Reports.** Build on the batch run to offer printable or exportable outputs: a payroll register, summary totals for the company (and optionally by department if that information is available in employee records), and a deduction summary that supports conversations about SSS, PhilHealth, Pag-IBIG, and tax. This moves reporting beyond what fits on a single table view.

**Phase 4 — Audit and controls.** Add the change log on pay settings and tie finalize actions to the logged-in user. An optional approval step before a period is locked can be kept simple—payroll finalizes after review—so the course scope stays manageable.

Phases may overlap during implementation, but **database first** is non-negotiable in the design: without it, audit history and consistent multi-employee pay runs remain fragile.

```mermaid
flowchart LR
  draft[Draft period]
  reviewed[Reviewed]
  finalized[Finalized]
  draft --> reviewed
  reviewed --> finalized
```

---

## Design Choices and Justification

**Role boundaries.** We keep the HM1 split: payroll staff own pay edits and period finalization; HR keeps view and review without edit rights; employees keep read-only personal pay slips. Expansion adds power to payroll and reporting for oversight roles; it does not blur who may change a salary figure. That protects the company from accidental or unauthorized pay changes and mirrors real HR departments.

**Pay period records versus ad hoc month selection.** Storing a period with month, year, and status gives every report and audit entry the same reference point—“January 2026, finalized on date X by user Y.” A dropdown alone does not create a closed book. When leadership asks for January payroll, everyone means the same finalized snapshot, not a recalculation that might change if someone edits rates next week.

**Batch run versus one-by-one screens.** Running all employees for a period reduces the risk of forgetting someone, speeds month-end, and makes reconciliation with attendance practical: payroll sees the exception list in one pass. One-by-one screens remain useful for spot checks; the batch run is the official office process.

**Audit trail scope (realistic first version).** We plan to log user, timestamp, affected employee, and action type (such as create or update pay settings, finalize period). Field-by-field diffs can wait if time is tight; the design still states the intent so implementation does not skip accountability entirely.

**JDBC for database connection.** JDBC fits our Java desktop application and the course’s integration path. Conceptually, it lets the program send structured requests to the database and commit related saves together—for example, when finalizing a period, storing all employee results and updating period status in one coordinated step. We describe that behavior in planning terms here; the homework implementation will follow in a later phase.

**Out of scope for this design pass.** We are not planning cloud hosting, a mobile app, or multi-country tax engines. Monthly Philippine statutory treatment stays as the program already models it. Keeping scope bounded makes the expansion credible for a coursework timeline.

---

## Information the Expanded System Must Hold

Beyond today’s per-employee pay settings, the expanded design needs a few new kinds of information, described in everyday terms.

A **pay period** record holds the period label (such as month and year), its status (draft, reviewed, finalized), and who finalized it and when. A **payroll run result** for each employee in that period stores the gross, each deduction, allowances, net pay, and the hours used—frozen at run time so historical reports stay true even if someone’s hourly rate changes later. **Audit entries** capture pay-related changes and important actions like finalization. Each run result should link back to the attendance hours used for that period so payroll can explain discrepancies without guessing.

Employee directory, attendance, leave, accounts, and existing pay settings still matter; they feed the batch run. The expansion adds the layer that turns individual calculations into an official monthly record the organization can keep.

---

## Who Does What After Expansion

After expansion, regular employees still sign in, view their attendance and leave, and open personal pay for a finalized period—now aligned with what payroll officially ran. HR still manages people and leave and can review payroll reports and period status but cannot edit pay or finalize. Payroll staff create and maintain pay settings, open and drive pay periods, run batch computation, resolve exceptions, produce reports, and finalize periods. IT or admin retain broader access for support and data correction where policy allows, consistent with today’s broader permissions, but finalization remains a payroll responsibility unless the organization explicitly delegates it.

This division keeps HM1’s trust model while giving payroll the tools to own month-end.

---

## Design Readiness Checklist

### A. Understanding the Expanded System

We should be able to explain the expanded flow to a classmate without opening the code: attendance hours feed a named pay period; payroll runs the batch for all staff; exceptions are fixed; reports and summaries are produced; the period is reviewed and finalized; employees then see personal pay that matches the official run. Reports, audit, and workflow are not three separate products—they are three faces of one monthly payroll process.

The types of data still include people, time at work, leave, and login accounts, but payroll gains period records, run snapshots, and audit history. Each type exists because someone in the organization must answer a real question: What did we pay? What did we deduct? Who changed this rate? Was this month closed?

### B. Gaps, Limitations, and Assumptions

We assume monthly pay remains the primary rhythm and that statutory rules stay aligned with the Philippine deductions already in the program. We assume a workforce on the order of thirty-eight employees—large enough to need batch and reports, small enough that a desktop tool remains reasonable. We assume the database connection via JDBC succeeds in a later homework phase; HM3 does not treat that work as done.

Gaps we still need to resolve in design or implementation include export format (PDF versus spreadsheet), whether review requires one approval step or two, and how much department breakdown reporting needs if employee records do not group people cleanly. We also assume HR will not gain edit rights on pay in this expansion; if policy changed, the role model would need revisiting.

### C. Clarifying Post-Expansion Responsibilities

After expansion, the system’s payroll responsibilities are clearer: maintain accurate pay settings, run and finalize periods, produce compliance-friendly summaries, and preserve history and audit evidence. Non-payroll modules (leave, directory, tickets) continue as today, fed by the same database once Phase 1 is complete.

Open questions are normal at design stage: exact export formats, the precise labels for period statuses, and whether IT may finalize in emergencies. Those can be decided during implementation without invalidating the phased plan.

### D. Readiness for Future Work

This plan leaves us ready to implement: Phase 1 JDBC database connection, then periods and batch runs, then reports, then audit. We have justified why each phase exists, who uses the results, and what information must be stored. A reader of HM3 and HM1 together could understand why file storage was a bottleneck, why expansion needs a database, and why batch periods and reports follow—not leap straight to screens without a data foundation.

---

## How This Design Plan Helps

HM1 captured where GEAR.HR stands today. HM3 states where payroll should go and why: stronger office operations through reports, audit, and workflow, built on a JDBC-connected database that we plan but have not yet deployed. When we implement or defend a choice in class, we can point to period snapshots instead of live recalculation, to batch runs instead of thirty-eight separate clicks, and to audit entries instead of untracked edits. This document is the rationale layer; the build log and code changes belong to later homework. The course arc moves from baseline understanding, through connection and expansion design, to implementation and refinement—and HM3 is the bridge that makes payroll expansion deliberate rather than accidental.
