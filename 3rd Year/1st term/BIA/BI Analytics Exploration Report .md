# BI Analytics    Exploration Report

Prepared and Presented by:

***Jennelyn Portea***  
***Marsha Aurelio***  
***Colin Bactong***  
*Bachelor of Science in Information Technology*  
*1st  term 2026-2027*

# **Intellectual Property Notice**

This template is an exclusive property of **Mapua-Malayan Digital College** and is protected under **Republic Act No. 8293**, also known as the *Intellectual Property Code of the Philippines* (IP Code). It is provided solely for educational purposes within this course. Students may use this template to complete their tasks, but may not **modify, distribute, sell, upload,** or **claim ownership** of the template. Such actions constitute copyright infringement under **Sections 172, 177, and 216** of the IP Code and may result in legal consequences. Unauthorized use beyond this course may result in legal or academic consequences.

Additionally, students must comply with the **Mapua-Malayan Digital College Student Handbook**, particularly with the following provisions:

- **Offenses Related to MMDC IT**:  
  - **Section 6.2** – Unauthorized copying of files  
  - **Section 6.8** – Extraction of protected, copyrighted, and/or confidential information by electronic means using MMDC IT infrastructure
- **Offenses Related to MMDC Admin, IT, and Operations**:  
  - **Section 4.5** – Unauthorized collection or extraction of money, checks, or other instruments of monetary equivalent in connection with matters about MMDC

Violations of these policies may result in **disciplinary actions ranging from suspension to dismissal**, according to the Student Handbook.

For permissions or inquiries, please contact MMDC-ISD at [isd@mmdc.mcl.edu.ph](mailto:isd@mmdc.mcl.edu.ph). 

# **TABLE OF CONTENTS**

**Section**  

1. **[Introduction](#introduction)**
  [Overview of Selected KPIs](#overview-of-selected-kpis)  
   [Objective of the Report](#objective-of-the-report)
2. **[Descriptive Analytics Summary](#descriptive-analytics-summary)**
  [Key Findings](#key-findings)  
   [Visual Evidence](#visual-evidence)
3. **[Diagnostic Analytics Summary](#diagnostic-analytics-summary)**
  [Key Findings](#key-findings-1)  
   [Visual Evidence](#visual-evidence-1)
4. **[Predictive Analytics Summary](#predictive-analytics-summary)**
  [Forecasted Insights](#forecasted-insights)  
   [Reliability](#reliability)
5. **[Prescriptive Analytics Summary](#prescriptive-analytics-summary)**
  [Data-Driven Recommendations](#data-driven-recommendations)
6. **[Consolidated Recommendations](#consolidated-recommendations)**
7. # **Introduction** {#introduction}
  FinMark Corporation supports small and medium-sized enterprises through financial analysis, marketing analytics, business intelligence, and consulting services. This report evaluates five selected key performance indicators (KPIs): Customer Retention Rate (CRR), Average Revenue per Client (ARPC), Client Growth Rate (CGR), Customer Engagement Rate (CER), and Customer Satisfaction Score (CSAT). Together, these measures show whether FinMark is retaining active relationships, generating value from its client base, expanding sustainably, maintaining customer participation, and delivering satisfactory service.
   The analysis covers all seven available datasets. Client, engagement, and feedback records provide the primary KPI evidence. Service, advertising, internal campaign, and regional sales/traffic records provide supporting context. Because the source data contain missing values, duplicates, inconsistent formats, incomplete reference records, and temporal conflicts, the findings distinguish verified patterns from results that require cautious interpretation.
  1. ## **Overview of Selected KPIs** {#overview-of-selected-kpis}
    - **Customer Retention Rate (CRR):** The percentage of clients active in one quarter who are also active in the next quarter. It provides an operational indicator of repeat participation and potential churn.
    - **Average Revenue per Client (ARPC):** Total billed amount divided by the number of unique active clients in a period. It shows the financial value produced by active client relationships.
    - **Client Growth Rate (CGR):** The quarter-over-quarter percentage change in cumulative unique clients based on standardized signup dates. It supports FinMark's five-year goal of increasing its SME client base by 40%.
    - **Customer Engagement Rate (CER):** The percentage of clients signed by quarter-end who recorded at least one engagement during that quarter. It identifies whether the client base is actively using FinMark's services.
    - **Customer Satisfaction Score (CSAT):** The average valid 1–5 rating after duplicate feedback IDs and missing ratings are removed. It monitors customer experience and service quality.
  2. ## **Objective of the Report** {#objective-of-the-report}
    This report combines descriptive, diagnostic, predictive, and prescriptive analytics. Descriptive analysis identifies what is happening across the five KPIs and major client segments. Diagnostic analysis examines why some results are unreliable or underperforming. Predictive analysis estimates possible future KPI movement, while prescriptive analysis converts the combined evidence into practical actions aligned with FinMark's growth, retention, and customer-lifetime-value priorities.
8. # **Descriptive Analytics Summary** {#descriptive-analytics-summary}
  The descriptive analysis uses corrected operational formulas that can be reproduced from the CSV files. These calculations differ from some values in BI Insight Summary #1, which used inconsistent denominators and client counts. The latest completed-period results are 51.0% CRR, $69,523 ARPC, 11.0% CGR, 49.5% CER, and 4.14 CSAT. <mark>The dashboard visuals present the completed-period KPI trends, client segments, and supporting business context.</mark>
  1. ## **Key Findings** {#key-findings}
    - **Customer performance is mixed.** CRR and CER are volatile and finished Q1 2025 near 50%, indicating that approximately half of the relevant client population was retained or engaged under the operational definitions. ARPC remained within an approximately $61,000–$79,000 quarterly range and was $69,523 in Q1 2025.
    - **Growth is positive but decelerating.** CGR declined from 100.0% in Q3 2023, when the recorded client base was small, to 11.0% in Q1 2025. The partial Q2 2025 result remains positive at 9.9%. The declining percentage is partly a normal base effect, but continued slowing should be monitored.
    - **Satisfaction improved across the completed months but remains data-limited.** Clean monthly CSAT fell to 2.50 in February 2025 and increased to 4.14 in April. Only 38 deduplicated records contain valid ratings, and only 29 also contain usable dates for monthly analysis.
    - **Client composition is diversified.** Education is the largest industry with 26 clients. Standard is the largest engagement tier with 38 clients. Twenty-three clients have no recorded region, limiting geographic comparisons.
    - **Whole-period client value varies by segment.** Technology records the highest revenue per client at approximately $596,000, followed by Manufacturing at $573,000. Visayas leads the recorded regions at approximately $573,000 per client. Basic-tier clients have the highest whole-period revenue per client at approximately $551,000, showing that tier labels alone do not determine value.
    - **Supporting marketing data show different efficiency signals.** LinkedIn has the largest recorded ad spend at approximately $696,000, while Facebook has the highest calculated click-through rate at 5.73%. Mindanao records the highest regional sales and foot traffic, but missing values, duplicates, and outliers prevent causal conclusions.
  2. ## **Visual Evidence** {#visual-evidence}
    ![FinMark corrected KPI overview dashboard](Exploration%20Report%20Visuals/01_kpi_overview_dashboard.png)
     *Figure 1. Corrected historical KPI overview. The figure summarizes the five selected KPIs using completed-period values and reproducible calculations.*
     ![FinMark customer segment exploration dashboard](Exploration%20Report%20Visuals/02_customer_segment_dashboard.png)
     *Figure 2. Customer segment exploration. Client counts and whole-period revenue per client are compared by industry, engagement tier, and region.*
     ![FinMark marketing and regional context dashboard](Exploration%20Report%20Visuals/03_marketing_regional_dashboard.png)
     *Figure 3. Marketing and regional context. These supporting datasets help describe the broader environment but were not used to calculate or forecast the five selected KPIs.*
9. # **Diagnostic Analytics Summary** {#diagnostic-analytics-summary}
  Diagnostic analysis shows that several apparent KPI patterns are partly influenced by data-integrity problems rather than business performance alone. The largest issues concern incomplete service references, dates that violate the expected customer lifecycle, inconsistent transaction quantities, and incomplete or duplicated feedback. These problems explain why some earlier descriptive results cannot be compared directly with the corrected KPI calculations.
  1. ## **Key Findings** {#key-findings-1}
    - **Incomplete service reference data prevents reliable service-level analysis.** The service master contains only PROD001–PROD005, while engagements use PROD001–PROD020. Consequently, 754 of 1,020 engagements (73.9%) and $37.76 million of $50.97 million billed revenue (74.1%) cannot be attributed to a service name or category. Headline ARPC can still be calculated from billed amount and customer ID, but service-level pricing and resource decisions are unreliable.
    - **Customer lifecycle dates are internally inconsistent.** There are 696 engagement records dated before the associated client's recorded signup date. This affects historical eligibility, tenure, and engagement analysis and helps explain conflicts between BI Insight Summary #1 and the corrected CER/CGR calculations.
    - **Quantity values are not standardized.** Of 1,020 engagement records, 232 store the value as `2 units` instead of a numeric value. This does not affect binary CER, which only checks for an engagement, but it compromises service-volume and usage-depth analysis.
    - **Feedback cleaning materially changes CSAT.** The feedback file contains 60 rows but only 50 unique feedback IDs. Forty-six raw rows have ratings, while only 38 deduplicated records have valid ratings. The average changes from 3.54 raw to 3.45 after cleaning.
    - **Supporting data contain additional gaps.** Twenty-three clients lack a region, 20 advertisements lack a platform, and 12 regional records lack a month. The regional forecast file also contains duplicates and large sales outliers. These limitations prevent fair comparisons unless records are validated.
  2. ## **Visual Evidence** {#visual-evidence-1}
    ![FinMark data-quality and root-cause diagnostic dashboard](Exploration%20Report%20Visuals/04_data_quality_diagnostics.png)
     *Figure 4. Data-quality and root-cause dashboard. The figure quantifies the service-reference gap, pre-signup engagements, inconsistent quantities, feedback-cleaning impact, and missing client regions.*
10. # **Predictive Analytics Summary** {#predictive-analytics-summary}
  Predictive analysis uses least-squares trend models fitted to the latest six completed quarters for CRR, ARPC, CGR, and CER and the four available completed months for CSAT. Q2 2025 and May 2025 observations are partial and are displayed as checks rather than included in model fitting. Percentage projections are constrained to 0–100%, and CSAT is constrained to 1–5.
  1. ### **Forecasted Insights** {#forecasted-insights}
    - **CRR:** The model decreases from 45.1% in Q2 2025 to 40.6% in Q1 2026. Partial Q2 retention is 49.0%, above the model estimate but below the 51.0% Q1 result. This is a warning that repeat activity may weaken.
    - **ARPC:** The model is broadly stable, moving from approximately $67,987 to $67,074. Partial Q2 ARPC is lower at $63,387, so the completed quarter should be reviewed before concluding that client value is stable.
    - **CGR:** The linear model reaches the 0% lower bound because early percentage growth decelerated sharply. Partial Q2 growth is still 9.9%, so the result should be interpreted as a flattening-risk signal rather than a literal prediction of no new clients.
    - **CER:** The model decreases from 48.6% to 45.4%. Partial Q2 engagement is stronger at 52.0%, suggesting that current performance may outperform the trend if maintained through quarter-end.
    - **CSAT:** The model rises from 4.26 in May to the 5.00 upper bound by August, but partial May CSAT is only 3.60. The almost full-scale prediction interval and small sample make this the least reliable forecast.
  2. ### **Reliability** {#reliability}
    The forecasts are directional estimates, not guarantees. Wide prediction ranges, partial periods, 696 pre-signup engagements, sparse dated feedback, and inconsistent historical definitions reduce confidence. <mark>The forecast dashboard summarizes historical KPI values, projected trends, partial-period checks, and the forecast boundary.</mark> FinMark should update the models after each completed period and should not use CSAT or the constrained CGR forecast as precise targets.
     ![FinMark five-KPI forecast summary dashboard](Exploration%20Report%20Visuals/05_forecast_summary_dashboard.png)
     *Figure 5. Five-KPI forecast summary. Solid lines represent completed-period history, dashed lines represent model projections, and the orange divider marks the forecast boundary. Partial-period results appear in each panel subtitle.*
11. # **Prescriptive Analytics Summary** {#prescriptive-analytics-summary}
  The combined evidence indicates that FinMark should strengthen retention and engagement while correcting source-data weaknesses. Actions should be implemented as measurable operating controls and reviewed after each completed quarter. This approach supports the five-year goals of expanding the SME client base by 40%, improving customer lifetime value, and establishing a data-driven culture.
  1. ## **Data-Driven Recommendations** {#data-driven-recommendations}
    - **Create quarterly retention and engagement alerts.** Identify clients active in the prior quarter who have no current-quarter engagement. Assign account owners to contact these clients before quarter-end and track reactivation as a CRR/CER outcome.
    - **Protect and expand client value.** Flag accounts with ARPC below the completed-quarter range, then test service bundles, cross-selling, and personalized recommendations. Complete the PROD006–PROD020 service master before attributing revenue or changing service-level pricing.
    - **Maintain acquisition momentum with realistic targets.** Replace percentage-only growth goals with quarterly net-client targets by industry and region. Prioritize referral and segment campaigns while monitoring operational capacity.
    - **Improve feedback volume and quality.** Require a rating and timestamp, prevent duplicate feedback IDs, categorize comments, and review low ratings within a defined service-level period. Recalculate CSAT monthly only after the period closes.
    - **Establish data-governance controls.** Validate signup and engagement chronology, store quantity as a numeric field, standardize platform names, enforce reference-table relationships, and publish data-quality exception counts with every dashboard refresh.
12. # **Consolidated Recommendations** {#consolidated-recommendations}
  The following table links each KPI to its corrected finding, principal cause or forecast risk, and recommended response.


| KPI                                | Key Insight                                                      | Root Cause or Forecast Risk                                                                           | Recommended Action                                                                         |
| ---------------------------------- | ---------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| Customer Retention Rate (CRR)      | Q1 2025 retention is 51.0%; model declines to 40.6%              | Approximately half of active clients do not return in the following quarter; prediction range is wide | Trigger quarterly inactivity alerts and targeted re-engagement                             |
| Average Revenue per Client (ARPC)  | Q1 2025 ARPC is $69,523; forecast is broadly stable near $67,000 | Partial Q2 is lower, and 74.1% of revenue cannot be attributed to a named service                     | Bundle and cross-sell services; complete the service master before service-level decisions |
| Client Growth Rate (CGR)           | Growth slowed to 11.0%; partial Q2 remains positive at 9.9%      | Percentage growth is decelerating as the client base increases; linear model reaches its 0% floor     | Set quarterly net-client targets and strengthen referral and segment campaigns             |
| Customer Engagement Rate (CER)     | Q1 2025 engagement is 49.5%; model declines to 45.4%             | Many eligible clients are inactive, while pre-signup engagements weaken historical comparisons        | Create engagement alerts and enforce chronological validation                              |
| Customer Satisfaction Score (CSAT) | April CSAT is 4.14, but partial May is 3.60                      | Only 29 cleaned ratings have usable dates; duplicates and missing ratings increase uncertainty        | Improve feedback collection, resolve low-rating issues, and recalculate monthly            |


