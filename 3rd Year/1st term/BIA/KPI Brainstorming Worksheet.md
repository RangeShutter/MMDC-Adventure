# 

# **Intellectual Property Notice**

# This template is an exclusive property of **Mapua-Malayan Digital College** and is protected under **Republic Act No. 8293**, also known as the Intellectual Property Code of the Philippines (IP Code). It is provided solely for educational purposes within this course. Students may use this template to complete their tasks, but may not **modify**, **distribute**, **sell**, **upload**, or **claim ownership** of the template itself. Such actions constitute copyright infringement under **Sections 172**, **177**, and **216** of the IP Code and may result in legal consequences. Unauthorized use beyond this course may result in legal or academic consequences.

# 

# Additionally, students must comply with the **Mapua-Malayan Digital College Student Handbook**, particularly with the following provisions:

* # **Offenses Related to MMDC IT:**

  * # **Section 6.2** – Unauthorized copying of files

  * # **Section 6.8** – Extraction of protected, copyrighted, and/or confidential information by electronic means using MMDC IT infrastructure

* # **Offenses Related to MMDC Admin, IT, and Operations:**

  * # **Section 4.5** – Unauthorized collection or extraction of money, checks, or other instruments of monetary equivalent in connection with matters pertaining to MMDC

# Violations of these policies may result in **disciplinary actions ranging from suspension to dismissal**, in accordance with the Student Handbook.

# For permissions or inquiries, please contact MMDC-ISD at [isd@mmdc.mcl.edu.ph](mailto:isd@mmdc.mcl.edu.ph). 

| MO-IT154 Business Intelligence and Analytics |  |
| :---- | :---- |
| **KPI Brainstorming Worksheet** |  |
| **Team Leader** | Jennelyn Portea |
| **Team Members** | Marsha Aurelio |
|  | Noreen Cendaña / Colin Bactong  |

# 

# 

1. **Initial Dataset Review**  
   *Review each dataset you loaded into Tableau. Fill in the table below with summary details.*  
   

| *Dataset Name* | *Number of Records* | *Number of Columns* | *Notable Issues (e.g., missing values, duplicates)* | *Notes on Relevant Variable* |
| ----- | ----- | ----- | ----- | ----- |
| finmark\_clients.csv  | 100 | 8 | There are missing values in contact\_email (13), region (23), and gender (13). Gender values are inconsistently formatted, including Male, male, M, Female, female, and F. Signup dates also use inconsistent formats, such as 04/03/2023 and 2023-09-22. Company names have inconsistent capitalization. No duplicate customer\_id values were identified. The missing region values may limit regional segmentation, while inconsistent gender and date formats require standardization before analysis.  | customer\_id can uniquely identify clients. gender, region, and industry can be used for customer segmentation. signup\_date can measure client tenure and growth over time. engagement\_tier can identify Basic, Standard, and Premium clients and help analyze customer value/engagement.  |
| finmark\_services.csv  | 5  | 4  | No missing values are visible in the provided records. Service categories are repeated, which is expected because multiple services belong to the same category.  However, a data-integrity issue exists when this dataset is compared with finmark\_engagements.csv: the services table contains only PROD001–PROD005, while the engagements dataset references PROD001–PROD020. Therefore, PROD006–PROD020 cannot currently be matched to service names, categories, or prices.   | service\_id uniquely identifies each service. service\_name identifies the specific offering. service\_category groups services into Financial Analysis, Marketing Analytics, and Consulting Services. price is useful for revenue, pricing, and service-value analysis.  |
| finmark\_engagements.csv  | 1020 | 8 | The quantity field has inconsistent formatting, with some values stored as numbers and others containing text such as "2 units." The engagement\_date field is consistently formatted in the provided data. No duplicate engagement\_id values were identified. A major data-integrity issue is that the dataset contains service IDs from PROD001 through PROD020, while finmark\_services.csv contains only PROD001 through PROD005. As a result, 754 engagement records cannot currently be matched to a service name, category, or price. | engagement\_id uniquely identifies each engagement. customer\_id links engagements to individual clients, while service\_id links them to the services provided. quantity measures the number of units/services involved, and billed\_amount is useful for revenue and customer-value analysis. engagement\_date can be used to analyze engagement and revenue trends over time. recorded\_by identifies the team responsible for recording the engagement.  |
| finmark\_internal\_campaigns.csv  | 48 | 8 | Missing campaign IDs / non-sequential records Potentially inconsistent campaign duration  | The campaign\_id values are not continuous (e.g., CAM1001 → CAM1006 → CAM1009). This suggests many campaign records are missing from this dataset. Several campaigns run for multiple years, e.g. CAM1015 (2022-09-23 to 2025-10-29), CAM1018 (2022-11-02 to 2025-11-07), and CAM1019 (2022-01-12 to 2025-04-27). This may be worth checking if campaigns are expected to have shorter durations.  |
| finmark\_client\_feedback.csv  | 60 | 5 | There are **missing values** in rating and submitted\_at. There are **10 duplicate feedback records**, with several feedback\_id values appearing more than once (e.g., FBFM1000, FBFM1015, FBFM1036, FBFM1040,FBFM1024 FBFM1041, FBFM1014, FBFM1042, FBFM1008, and FBFM1018). Some comments contain **encoding issues**, such as ñice and dashbÃ³ard. The submitted\_at values also have slightly inconsistent time formatting.  | feedback\_id should uniquely identify each feedback record. customer\_id links feedback to individual clients and can be used to analyze customer satisfaction. rating is useful for measuring satisfaction and identifying positive or negative feedback. comment provides qualitative customer feedback and can be analyzed for common issues or themes. submitted\_at can be used to analyze feedback trends over time.  |
| finmark\_forecast\_adjusted.csv  | 130 | 6 | There are missing values in month, sales, ad\_spend, and foot\_traffic. The month field has inconsistent date formats, such as 2023-06 and 06/2023, and some records have blank month values. There are duplicate records, including repeated rows for 2022-01 North Luzon, 2022-01 Mindanao, 2022-03 Mindanao, 2022-09 Mindanao, 2023-06 Mindanao, 2024-01 South Luzon, 2024-05 Mindanao, and 2023-11 Visayas. There are also unusually high sales values, such as 412,913, 385,004, 424,791, and 479,264. These should be treated as potential outliers and validated before being used for forecasting or trend analysis. The dataset also contains duplicate records that should be removed or investigated before analysis. | month is the primary time variable for monthly trend analysis. region groups forecast data by geographic area. sales is the main performance metric for revenue analysis. ad\_spend can be used to evaluate marketing investment, while foot\_traffic measures customer activity. client\_name identifies the client and is consistently recorded as FinMark.  |
| finmark\_ads\_adjusted.csv  | 158 | 10 |  There are missing values in **platform, clicks, ctr, and spend**. Platform values are also inconsistently formatted, with values such as **Instagram, INSTAGRAM, Facebook, and FACEBOOK**. There are **duplicate ad records**, including repeated ad IDs such as **AD5007, AD5009, AD5107, AD5003, AD5114, AD5085, AD5103, and AD5083**. Some records are also missing from the sequential row/index numbering.  | **ad\_id** can uniquely identify individual advertisements. **campaign\_id** links each ad to a campaign, while **platform** identifies the advertising channel. **impressions, clicks, and ctr** can be used to measure ad performance and engagement. **spend** is useful for analyzing advertising costs and efficiency. **ad\_date** can be used to analyze advertising performance over time. **client\_name** identifies the client associated with the ads.  |

   

   

   

   

   

2. **Variable Identification by Business Area**  
   Identify which variables from your datasets might help analyze each business area below. Explain briefly why they matter.  
   

| a. Customer Profiles and Segments |  |
| :---- | :---- |
| **Variables** | *customer\_id, company\_name, contact\_email, gender, region, industry, engagement\_tier* |
| **Possible Insights** | *The variables can be used to group customers into different segments. These variables can help FinMark understand the characteristics and engagement levels of its own customers. Thus, the variables used can support the development of better services and strategies that can match the needs and preferences of each customer group. The “customer\_id” variable can be used to connect customer information with records like feedback  and engagement. FinMark can utilize these variables to compare customer activity and feedback across different customer segments. FinMark can utilize these comparisons to easily understand customer behavior and experiences. Analyzing such variables like “industry”, “region”, and “engagement\_tier” can help the business identify which customer groups are more active or valuable. FinMark can take advantage of these insights in order to determine which specific segments should require more attention or engagement. Thus, these specific variables can be used to improve FinMark’s customer retention and strengthen its relationships with different customer groups.* |
| **b. Product Sales and Trends** |  |
| **Variables** | *service\_id, service\_name, service\_category, price, engagement\_id, quantity, billed\_amount, engagement\_date, month, region, sales, ad\_spend, foot\_traffic* |
| **Possible Insights** | *Variables can be used to identify which services are most likely to be purchased or used. With these variables present in the dataset, FinMark will be able to determine which services have the highest demands amongst customers. The business can then use this information to plan its own services and resources. Variables can also be utilized in order to determine which services generate the most revenue. These variables can be used to help FinMark to compare the financial performance of its different services. With this information, FinMark can further support better decisions and plan on its pricing and service offerings. Additionally, variables like “engagement\_date”, “month”, and “region” can track changes in sales over time and differing locations. FinMark can identify seasonal trends, high-performing regions, and periods of high or low demand. The business can utilize these patterns to better plan its sales activities and services.* |
| **c. Marketing Campaigns and Effectiveness** |  |
| **Variables** | *campaign\_id, campaign\_name, channel, start\_date, end\_date, budget, client, feedback\_id, rating, comment, month, region, sales, ad\_spend, foot\_traffic, ad\_id, platform, impressions, clicks, ctr, spend, ad\_date* |
| **Possible Insights** | *The variables can measure the performance of differing marketing campaigns. These can help FinMark determine which campaigns attract and engage the most potential customers. The results under these variables can be used to improve future campaigns and marketing strategies. Variables also can compare the performance and costs of differing advertising platforms. With these variables, FinMark can determine which platforms would provide the business better results for the amount spent. Thus, the business can allocate its marketing budget towards more effective platforms. Variables like “sales”, “ad\_spend”, “rating”, and “comment” can provide additional information about the possible effects and reception of marketing activities. Comparing these variables can help FinMark determine whether marketing efforts are associated with changes in its sales and customer feedback. The insights under these variables can help the business improve its marketing strategies and overall campaign effectiveness.* |
| **d. Transaction Timing and Patterns** |  |
| **Variables** | *signup\_date, engagement\_date, start\_date, end\_date, submitted\_at, month* |
| **Possible Insights** | *The chosen variables can track when customers sign up, interact with the business, and submit feedback. FinMark can identify periods when customer activity is particularly high or low. This can support Finmark’s planning on its own customer services and business operations. Additionally, variables can also be used to identify monthly and seasonal patterns in customer activity and sales. These patterns can help FinMark determine when demand is likely to increase or decrease. Thus, the business can use this information to prepare its resources and services accordingly. Variables can also be utilized to track when campaigns and advertisements are active. Comparing these dates with customer activity and sales can help FinMark identify whether certain periods are more effective for marketing activities. Thus, this can help the business choose proper time to run campaigns and advertisements/* |

   

3. **KPI Brainstorming Table**  
   *List proposed KPIs. Each row should contain one KPI and explain what it measures, why it’s useful to FinMark, and which variables support it.*  
   

| KPI Name | What It Measures | Why It’s Important to FinMark | Supporting Variables |
| ----- | ----- | ----- | ----- |
| *Customer satisfaction score* | *The average rating given by customers in feedback submissions.* | *Help FinMark monitor customer satisfaction, improve service quality, and support customer retention strategies.* | *Rating, feedback\_id, customer\_id, submitted\_at* |
| *Customer Engagement Rate* | *The frequency of customers interactions with FinMark services over a given offering.* | *Helps identify active and inactive clients, supporting retention efforts and personalized engagement strategies.* | *Customer\_id, engagement\_id, engagement\_date, engagement\_tier* |
| *Revenue per Service*  | *Total revenue generated by each service offering.* | *Helps determine which services contribute the most revenue and where resources should be allocated.* | *service \_id, billed\_amount, quantity, engagement\_tier*  |
| *Advertising ROI* | *The return generated relative to advertising spending.* | *Allows FinMark to evaluate marketing effectiveness and optimize budget allocation,*   | *campaign\_id , budget, spend, clicks, ctr, impression.* |
| *Client Growth Rate* | *The increase in new customer over time,* | *Support FinMark strategic goal ints SME client base by 40%* | *Customer\_id, signup,date,  region, industry.* |

4. **KPI Reflection and Feasibility Notes**  
   *Review the KPIs listed in Section III. Choose 2–3 KPIs that you believe are the most measurable, relevant, and actionable for FinMark. Use the table below to explain why you believe each KPI is a strong candidate and identify any concerns regarding its feasibility or data support.*  
   

| Selected KPI Name | Why This KPI is a Strong Candidate | Concerns or Limitations (if any) |
| ----- | ----- | ----- |
| *Customer Satisfaction score*  | *Directly measures customer experience and supports improving retention and service quality. The required data is readily available in the feedback dataset.* | *Missing ratings and duplicate feedback records may affect accuracy* |
| *Revenue per Service*  | *Easy to calculate and highly relevant because it identifies the most profitable services and supports business growth decisions.* | *Quantity values contains inconsistent formats such as “2 units”, requiring data cleaning* |
| *Advertising ROI* | *Helps evaluate campaign effectiveness and optimize masketing spending, supporting data-driven decision-making.* | *Missing values in spend click, CTR, and duplicated ad records may impact calculations.* |

