  
**Intellectual Property Notice**

This template is an exclusive property of **Mapua-Malayan Digital College** and is protected under **Republic Act No. 8293**, also known as the Intellectual Property Code of the Philippines (IP Code). It is provided solely for educational purposes within this course. Students may use this template to complete their tasks, but may not **modify**, **distribute**, **sell**, **upload**, or **claim ownership** of the template. Such actions constitute copyright infringement under **Sections 172**, **177**, and **216** of the IP Code and may result in legal consequences. Unauthorized use beyond this course may result in legal or academic consequences.

Additionally, students must comply with the **Mapua-Malayan Digital College Student Handbook**, particularly with the following provisions:

* **Offenses Related to MMDC IT:**  
  * **Section 6.2** – Unauthorized copying of files  
  * **Section 6.8** – Extraction of protected, copyrighted, and/or confidential information by electronic means using MMDC IT infrastructure  
* **Offenses Related to MMDC Admin, IT, and Operations:**  
  * **Section 4.5** – Unauthorized collection or extraction of money, checks, or other instruments of monetary equivalent in connection with matters about MMDC

Violations of these policies may result in **disciplinary actions ranging from suspension to dismissa**l, according to the Student Handbook.

For permissions or inquiries, please contact MMDC-ISD at [isd@mmdc.mcl.edu.ph](mailto:isd@mmdc.mcl.edu.ph). 

| MO-IT154 Business Intelligence and Analytics |  |
| :---- | :---- |
| **BI Insight Summary \#3** |  |
| **Team Leader** | *Jennelyn Portea* |
| **Team Members** | *Marsha Aurelio*  |
|  | *Colin Bactong*  |

1. **Executive Summary**  
   This BI Insight Summary forecasts five customer and business KPIs for FinMark Corporation using the available client, engagement, and feedback records. A least-squares trend was fitted to the latest completed periods, while the incomplete Q2 2025 and May 2025 observations were shown separately and excluded from model fitting. The results indicate declining risks for retention and engagement, broadly stable but slightly decreasing revenue per active client, slowing client growth, and a possible improvement in satisfaction that remains highly uncertain because of the short feedback history. These projections support FinMark's priorities of client growth, predictive retention, and improved customer lifetime value.

2. **Forecasted KPI Outcomes**

   **Forecast Method and Limitations:** Quarterly forecasts cover Q2 2025 through Q1 2026 for CRR, ARPC, CGR, and CER. The monthly CSAT forecast covers May through August 2025. Each chart uses the latest six completed quarterly observations, or the four available completed months for CSAT, and includes an approximate 95% prediction band. Percentage forecasts are constrained to 0–100%, while CSAT is constrained to 1–5. The projections are statistical estimates generated from the CSV data rather than Tableau exports. Data-quality limitations identified in BI Insight Summary #2 remain relevant: some engagement dates precede recorded signup dates, 754 engagements have unmatched service IDs, 232 quantity entries use a non-standard format, and feedback contains duplicate IDs and missing ratings.

   **KPI #1: Customer Retention Rate (CRR)**
   * **KPI Description:** Customer Retention Rate measures the percentage of clients active in one quarter who are also active in the next quarter. This operational definition avoids treating newly acquired clients as retained clients.
   * **Forecast Insights:** Completed-quarter CRR was 51.0% in Q1 2025. The trend model projects a gradual decrease from 45.1% in Q2 2025 to 40.6% in Q1 2026. The partial Q2 2025 observation is 49.0%, which is above the model estimate but still slightly below Q1.
   * **Scenario Implications:** If the decline continues, FinMark may have fewer repeat-engagement clients, reducing relationship stability and future revenue opportunities. The wide prediction band means the direction should be monitored rather than treated as certain.
   * **Visual Evidence:**

     ![Customer Retention Rate historical trend and forecast](Visual%20Evidence/bi3_crr_forecast.png)

     *Figure 1. Quarterly CRR forecast. Q2 2025 is partial and excluded from model fitting. Source: FinMark_Engagements.csv.*
   * **Prescriptive Action:** Monitor clients who were active in the previous quarter but have not returned. Trigger targeted follow-ups, personalized offers, and re-engagement campaigns before they remain inactive for a full quarter.

   **KPI #2: Average Revenue per Client (ARPC)**
   * **KPI Description:** Average Revenue per Client measures total billed amount divided by the number of unique active clients in a period.
   * **Forecast Insights:** ARPC was $69,523 in Q1 2025. The model projects a nearly stable but slightly declining path, from approximately $67,987 in Q2 2025 to $67,074 in Q1 2026. The partial Q2 actual is lower at $63,387.
   * **Scenario Implications:** Stable ARPC would preserve client value even if acquisition slows, while the lower partial-quarter result indicates a risk that should be checked when Q2 closes. Unmatched service IDs limit service-level revenue attribution, but they do not prevent headline ARPC calculation because billed amounts and customer IDs are present.
   * **Visual Evidence:**

     ![Average Revenue per Client historical trend and forecast](Visual%20Evidence/bi3_arpc_forecast.png)

     *Figure 2. Quarterly ARPC forecast. Q2 2025 is partial and excluded from model fitting. Source: FinMark_Engagements.csv.*
   * **Prescriptive Action:** Develop cross-selling and service-bundling offers for active clients, review accounts below the forecast range, and complete the service reference list before making service-level resource decisions.

   **KPI #3: Client Growth Rate (CGR)**
   * **KPI Description:** Client Growth Rate measures the quarter-over-quarter percentage change in cumulative unique clients based on standardized signup dates.
   * **Forecast Insights:** Growth slowed from 100.0% in Q3 2023, when the recorded base was small, to 11.0% in Q1 2025. The linear model reaches the 0% lower bound across the four forecast quarters, signaling that the historical rate of deceleration is not sustainable. The partial Q2 2025 result is still positive at 9.9%, so the zero-growth model result should be interpreted as a warning of flattening rather than a literal prediction.
   * **Scenario Implications:** A sustained slowdown would make future expansion harder even though the historical client base has already exceeded FinMark's 40% strategic growth target. Continuing positive partial-quarter growth provides time to strengthen acquisition before the trend flattens.
   * **Visual Evidence:**

     ![Client Growth Rate historical trend and forecast](Visual%20Evidence/bi3_cgr_forecast.png)

     *Figure 3. Quarterly CGR forecast. The model is constrained at 0%; Q2 2025 partial growth is 9.9%. Source: finmark_clients.csv.*
   * **Prescriptive Action:** Strengthen referral and segment-focused acquisition campaigns, set a realistic quarterly net-client target, and compare completed-quarter performance against the 40% long-term goal rather than relying on the early high-growth percentage.

   **KPI #4: Customer Engagement Rate (CER)**
   * **KPI Description:** Customer Engagement Rate measures the percentage of clients signed by the end of a quarter who recorded at least one engagement during that quarter.
   * **Forecast Insights:** CER was 49.5% in Q1 2025. The model projects a gradual decrease from 48.6% in Q2 2025 to 45.4% in Q1 2026, while the partial Q2 result is currently stronger at 52.0%.
   * **Scenario Implications:** A completed-quarter result near or above the partial value would outperform the model. A decline toward 45% would mean more than half of eligible clients are not engaging in a typical quarter, increasing retention risk. The historical series should be interpreted cautiously because some engagements predate recorded signup dates.
   * **Visual Evidence:**

     ![Customer Engagement Rate historical trend and forecast](Visual%20Evidence/bi3_cer_forecast.png)

     *Figure 4. Quarterly CER forecast. Q2 2025 is partial and excluded from model fitting. Source: finmark_clients.csv and FinMark_Engagements.csv.*
   * **Prescriptive Action:** Create engagement alerts for clients with no quarterly activity, provide personalized service recommendations, and standardize engagement records so changes can be monitored reliably.

   **KPI #5: Customer Satisfaction Score (CSAT)**
   * **KPI Description:** Customer Satisfaction Score is the average valid rating submitted after a service interaction, measured on a 1–5 scale.
   * **Forecast Insights:** Cleaned monthly CSAT increased from 2.50 in February to 4.14 in April 2025. The model projects 4.26 in May and reaches the 5.00 upper bound by August. However, the partial May result is 3.60, below the model estimate, and the prediction band spans almost the full rating scale.
   * **Scenario Implications:** The direction suggests possible improvement, but the forecast is not sufficiently precise for target-setting. Only 29 cleaned ratings had usable submission dates for the monthly trend, so additional feedback could materially change the result.
   * **Visual Evidence:**

     ![Customer Satisfaction Score historical trend and forecast](Visual%20Evidence/bi3_csat_forecast.png)

     *Figure 5. Monthly CSAT forecast based on 29 dated, deduplicated ratings. May 2025 is partial and excluded from model fitting. Source: FinMark_client_feedback.csv.*
   * **Prescriptive Action:** Increase feedback response volume, prevent duplicate and incomplete submissions, review low-rating comments promptly, and recalculate the forecast after each completed month.

3. **Summary of Strategic Recommendations**

| KPI | Forecast Insight | Business Impact | Recommended Action |
| ----- | ----- | ----- | ----- |
| Customer Retention Rate (CRR) | Model decreases from 45.1% to 40.6%; partial Q2 is 49.0% | Fewer repeat clients may weaken recurring revenue and customer lifetime value | Trigger quarterly inactivity alerts and targeted re-engagement |
| Average Revenue per Client (ARPC) | Broadly stable with a slight decline from $67,987 to $67,074; partial Q2 is $63,387 | Client value may hold, but the partial result requires review | Bundle services, cross-sell, and investigate below-range accounts |
| Client Growth Rate (CGR) | Model reaches the 0% lower bound; partial Q2 growth remains 9.9% | Acquisition could flatten after earlier rapid expansion | Set quarterly net-client targets and strengthen referrals and segment campaigns |
| Customer Engagement Rate (CER) | Model decreases from 48.6% to 45.4%; partial Q2 is 52.0% | More inactive clients could increase future churn risk | Identify clients with no quarterly engagement and personalize outreach |
| Customer Satisfaction Score (CSAT) | Upward model reaches 5.00, but partial May is 3.60 and uncertainty is very high | Sparse feedback can produce misleading service-quality conclusions | Collect more complete feedback and recalculate monthly |

