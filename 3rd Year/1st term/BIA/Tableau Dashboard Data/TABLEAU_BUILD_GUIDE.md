# Step-by-Step Tableau Dashboard Build Guide

This guide explains how to recreate the FinMark executive dashboard shown in `tableau_executive_dashboard_mockup.svg`.

The instructions work with Tableau Desktop or Tableau Public. Menu names may differ slightly by version.

## 1. Files you will use

Use these three CSV files as separate Tableau data sources:

1. `executive_kpi_cards.csv` — the five KPI cards.
2. `executive_kpi_timeseries.csv` — all historical, partial, and forecast trend charts.
3. `executive_recommendations.csv` — the action table at the bottom.

Do not join the three CSV files. They have different levels of detail, and joining them can duplicate rows and change the displayed values.

## 2. Connect the CSV files to Tableau

### First data source

1. Open Tableau.
2. Under **Connect**, select **Text File**.
3. Open `executive_kpi_timeseries.csv`.
4. Rename the Tableau data source to **KPI Time Series**.

### Additional data sources

1. Select **Data > New Data Source**.
2. Select **Text File**.
3. Open `executive_kpi_cards.csv`.
4. Rename it to **KPI Cards**.
5. Repeat the process for `executive_recommendations.csv`.
6. Rename that source to **Executive Recommendations**.

You can switch between sources using the data-source name above Tableau's Data pane.

## 3. Check the field types

### KPI Time Series

Open the **KPI Time Series** data source and confirm:

- `period_start` — Date
- `value` — Number (decimal)
- `numerator` — Number (decimal)
- `denominator` — Number (decimal)
- `forecast_lower_95` — Number (decimal)
- `forecast_upper_95` — Number (decimal)
- `kpi_code`, `kpi_name`, `period`, `frequency`, `series_type`, and `is_partial` — String

If `period_start` is shown as text:

1. Click its data-type icon.
2. Select **Date**.

### KPI Cards

Confirm:

- `display_order` — Number (whole)
- `completed_value`, `partial_value`, and `forecast_end_value` — Number (decimal)
- All remaining fields — String

### Executive Recommendations

Confirm:

- `display_order` — Number (whole)
- All remaining fields — String

## 4. Create time-series calculated fields

Switch to the **KPI Time Series** source. Create these calculated fields through **Analysis > Create Calculated Field**.

### Completed Actual Value

```text
IF [series_type] = "Completed Actual" THEN [value] END
```

### Forecast Value

```text
IF [series_type] = "Forecast" THEN [value] END
```

### Partial Actual Value

```text
IF [series_type] = "Partial Actual" THEN [value] END
```

### Series Label

```text
[kpi_code] + " - " + [series_type]
```

These fields keep completed, forecast, and partial observations visually separate.

## 5. Build the five KPI cards

Use the **KPI Cards** data source. The easiest method is to create one worksheet per KPI.

### Create the CRR card

1. Create a new worksheet and name it **Card - CRR**.
2. Drag `kpi_code` to **Filters** and select `CRR`.
3. Set the Marks type to **Text**.
4. Drag `completed_value` to **Text**.
5. Drag `kpi_name`, `completed_period`, `direction`, `partial_value`, `partial_period`, `forecast_end_value`, and `data_warning` to **Detail**.
6. Click **Text > Edit Label**.
7. Arrange the label approximately as follows:

```text
CUSTOMER RETENTION
<completed_value>%
<completed_period> completed
<direction>
```

8. Make the value large and bold.
9. Put the partial value, forecast endpoint, definition, and warning in the tooltip.
10. Format the completed value as a number with one decimal place.

### Duplicate the other cards

Duplicate the CRR sheet four times, then change the filter and formatting:

- **Card - ARPC:** filter `ARPC`; format as currency with no decimals.
- **Card - CGR:** filter `CGR`; format as a percentage-style number with one decimal.
- **Card - CER:** filter `CER`; format with one decimal.
- **Card - CSAT:** filter `CSAT`; format with two decimals and add `/ 5` in the label.

Important: CRR, CGR, and CER are stored as values from 0 to 100, such as `51.0204`. Do not apply Tableau's standard Percentage format because it would display `5,102.04%`. Use a regular number and add `%` to the label.

For all cards:

- Remove row and column dividers.
- Hide worksheet titles if the title is already included in the text mark.
- Use **Fit > Entire View**.
- Keep the latest completed result as the large headline.
- Show partial results only in the tooltip or smaller supporting text.

## 6. Build the CRR and CER trend chart

Use the **KPI Time Series** source.

1. Create a worksheet named **Trend - CRR and CER**.
2. Drag `kpi_code` to **Filters** and select `CRR` and `CER`.
3. Drag `period_start` to **Columns**.
4. Click the field menu and choose a continuous date at the **Quarter** level.
5. Drag `value` to **Rows**.
6. Change the aggregation to **Average**.
7. Set Marks to **Line**.
8. Drag `Series Label` to **Color**.
9. Drag `period`, `series_type`, `numerator`, `denominator`, `forecast_lower_95`, and `forecast_upper_95` to **Tooltip**.
10. Edit the Y-axis and set a fixed range from `0` to `100`.

Recommended colors:

- CRR completed — blue.
- CRR forecast — lighter blue.
- CER completed — purple.
- CER forecast — lighter purple.

If your Tableau version supports line patterns, make forecast lines dashed. Otherwise, use lighter forecast colors.

### Add partial-period points

For a simple version, leave the partial Q2 values in the tooltip and KPI cards.

For a closer match to the mockup:

1. Replace `value` on Rows with `Completed Actual Value`.
2. Drag `Forecast Value` beside it on Rows.
3. Right-click the second axis and select **Dual Axis**.
4. Select **Synchronize Axis**.
5. Add `Partial Actual Value` as a third marks layer if your Tableau version supports layered marks.
6. Set partial marks to circles with an orange outline.

The simple version is acceptable if layered marks become difficult.

## 7. Build the ARPC trend chart

1. Duplicate **Trend - CRR and CER**.
2. Rename it **Trend - ARPC**.
3. Change the `kpi_code` filter to `ARPC`.
4. Format the Y-axis as currency with display units in thousands.
5. Use green for completed actuals and a lighter or dashed green for forecasts.
6. Keep the partial Q2 2025 value visually distinct or include it in the tooltip.

Suggested title: **Average Revenue per Client**

Do not place ARPC on the same axis as percentage KPIs.

## 8. Build the CGR trend chart

1. Duplicate the ARPC trend sheet.
2. Rename it **Trend - CGR**.
3. Change the `kpi_code` filter to `CGR`.
4. Change the Y-axis to a regular number.
5. Add `%` to labels or tooltips.
6. Use orange for completed actuals and a lighter or dashed orange for forecasts.
7. Keep the lower end of the axis at `0`.

Suggested title: **Client Growth Rate**

Do not add a 40% reference line. The 40% target is cumulative over five years, while this chart shows quarter-over-quarter growth.

Add this chart note:

> The forecast reaching 0% is a flattening-risk signal, not a guarantee of zero new clients.

## 9. Build the CSAT trend chart

1. Duplicate the CGR trend sheet.
2. Rename it **Trend - CSAT**.
3. Change the `kpi_code` filter to `CSAT`.
4. Change `period_start` on Columns from Quarter to continuous **Month**.
5. Fix the Y-axis from `1` to `5`.
6. Use pink for completed actuals and a lighter or dashed pink for forecasts.
7. Display May 2025 as the partial-period point.
8. Put `forecast_lower_95` and `forecast_upper_95` in the tooltip.

Suggested title: **Customer Satisfaction Score**

Add this chart note:

> Forecast confidence is low because only 29 cleaned valid ratings have usable dates.

### Optional prediction band

To display the forecast interval:

1. Place `forecast_lower_95` and `forecast_upper_95` in the view.
2. Use a dual-axis area chart or reference band between the two measures.
3. Give the band a pale transparent color.
4. Keep the forecast line visible above it.

If this makes the chart difficult to read, keep the bounds in the tooltip instead.

## 10. Build the executive action table

Use the **Executive Recommendations** data source.

1. Create a worksheet named **Executive Action Table**.
2. Drag `display_order` to **Rows**.
3. Drag `kpi_code` to **Rows** after `display_order`.
4. Drag `latest_finding` to **Rows**.
5. Drag `principal_risk` to **Rows**.
6. Drag `recommended_action` to **Rows**.
7. Sort ascending by `display_order`.
8. Hide the `display_order` header.
9. Rename the visible headers:
   - KPI
   - Latest Finding
   - Principal Risk
   - Recommended Action
10. Increase row height and enable text wrapping where available.
11. Remove unnecessary grid lines but retain subtle row separators.

If Tableau truncates the text, increase the worksheet width in the final dashboard.

## 11. Assemble the dashboard

1. Click **New Dashboard**.
2. Set **Size** to **Fixed Size**.
3. Enter approximately `1300 × 900`.
4. Use **Tiled** objects and layout containers.

### Recommended layout

1. Add a horizontal text title:
   - **FinMark Executive KPI Dashboard**
   - Subtitle: `Latest completed quarter: Q1 2025 | CSAT through April 2025`
2. Add a horizontal container below the title.
3. Place the five KPI cards inside the container.
4. Add another horizontal container.
5. Place **Trend - CRR and CER** on the left.
6. Place **Trend - ARPC** on the right.
7. Add another horizontal container.
8. Place **Trend - CGR** on the left.
9. Place **Trend - CSAT** on the right.
10. Place **Executive Action Table** across the bottom.

Use the mockup for visual reference:

`tableau_executive_dashboard_mockup.svg`

## 12. Recommended dashboard formatting

- Background: very light gray.
- Worksheet panels: white.
- Card and panel padding: approximately 10–16 pixels.
- Titles: dark blue-gray.
- Use consistent KPI colors across cards and charts.
- Remove unnecessary borders, legends, and grid lines.
- Keep forecast colors lighter than actual colors.
- Distinguish partial values with an outlined circle or orange accent.
- Use no more than one decimal place for percentage KPIs.
- Use thousands separators for currency.

Suggested colors:

- CRR — blue
- ARPC — green
- CGR — orange
- CER — purple
- CSAT — pink

## 13. Configure tooltips

Each trend tooltip should show:

```text
KPI: <kpi_name>
Period: <period>
Series: <series_type>
Value: <value>
Numerator: <numerator>
Denominator: <denominator>
95% forecast range: <forecast_lower_95> to <forecast_upper_95>
```

For actual rows, the forecast fields will be blank. For forecast rows, numerator and denominator will be blank.

## 14. Validate the finished dashboard

Before submission, confirm these completed-period values:

- CRR — `51.0%`, Q1 2025
- ARPC — `$69,523`, Q1 2025
- CGR — `11.0%`, Q1 2025
- CER — `49.5%`, Q1 2025
- CSAT — `4.14 / 5`, April 2025

Confirm these partial-period checks:

- CRR — `49.0%`, Q2 2025
- ARPC — `$63,387`, Q2 2025
- CGR — `9.9%`, Q2 2025
- CER — `52.0%`, Q2 2025
- CSAT — `3.60 / 5`, May 2025

If Tableau shows values 100 times too large, change the percentage KPI format from **Percentage** to a regular number and add `%` manually.

## 15. Save or publish

### Tableau Desktop

Save as a packaged workbook:

```text
FinMark_Executive_Dashboard.twbx
```

A packaged workbook includes the CSV extracts and is easier to move to another computer.

### Tableau Public

1. Select **File > Save to Tableau Public As**.
2. Sign in.
3. Use a clear title such as **FinMark Executive KPI Dashboard**.
4. Review the published version to confirm that tooltips, labels, and text wrapping still display correctly.

Do not publish confidential information if the course data are not approved for public sharing.

## 16. Revise an existing workbook (feedback fixes)

Use this section if the dashboard from Sections 1–15 already exists. These steps add the missing client-count context, required forecast encodings, KPI-specific axis titles, and a Decision Center.

The visual target is `tableau_executive_dashboard_revised_mockup.svg`. Keep the original mockup as the before state.

### 16.1 Connect the two new CSV files

Do **not** join these files to the existing KPI sources. They have different grain.

1. Select **Data > New Data Source > Text File**.
2. Open `client_base_timeseries.csv`.
3. Rename the source to **Client Base**.
4. Confirm field types:
   - `period_start` — Date
   - `cumulative_signed_clients`, `net_new_clients`, `prior_cumulative_clients`, `cgr_percent` — Number
   - remaining fields — String
5. Add another data source for `executive_decision_center.csv`.
6. Rename it to **Decision Center**.
7. Confirm `priority` is a whole number and all other fields are strings.

`executive_kpi_timeseries.csv` now includes `axis_title`. After refresh, confirm that field is a String. `executive_kpi_cards.csv` now includes `supporting_text`.

### 16.2 Fix the CGR chart so a falling rate is not read as a shrinking base

Q1 2025 CGR is **11.0%**, but the client base still grew by **+9 clients** (82 to 91). Partial Q2 is **9.9%** with another **+9 clients** (91 to 100).

1. Open **Trend - CGR**.
2. Keep `kpi_code` = `CGR` from **KPI Time Series** on the left axis.
3. Build the required three-series encoding from Section 16.3 on the CGR % axis.
4. Drag a new worksheet named **Trend - Client Count**, or add **Client Base** as a dual axis on the same sheet:
   - Columns: continuous `period_start` at Quarter
   - Rows: `cumulative_signed_clients` as a line
   - Color: a distinct count color, such as dark gray
   - Filter out quarters where `cumulative_signed_clients` = 0 if the early empty base clutters the view (signed clients begin in 2023-Q2)
5. If using one worksheet, place CGR % on the left axis and cumulative signed clients on the right axis. Select **Dual Axis**, then **Synchronize** only if the scales are comparable — they are not, so **do not synchronize**.
6. Edit axis titles:
   - Left: `Client Growth Rate (%)`
   - Right: `Cumulative Signed Clients (Count)`
7. Add this caption on the worksheet or dashboard:

> Declining percentage growth does not mean the number of clients is declining. Q1 added +9 clients (base 91). Partial Q2 also added +9 clients (base 100).

8. On **Card - CGR**, add `supporting_text` under the headline so the card shows `11.0%` plus `+9 net new clients; cumulative base 91`.

### 16.3 Require actual vs partial vs forecast on every trend chart

The earlier “simple version” is no longer acceptable. Every forecast chart must use three encodings.

Create or keep these calculated fields on **KPI Time Series**:

```text
Completed Actual Value
IF [series_type] = "Completed Actual" THEN [value] END

Partial Actual Value
IF [series_type] = "Partial Actual" THEN [value] END

Forecast Value
IF [series_type] = "Forecast" THEN [value] END

Forecast Lower
IF [series_type] = "Forecast" THEN [forecast_lower_95] END

Forecast Upper
IF [series_type] = "Forecast" THEN [forecast_upper_95] END
```

On **Trend - CRR and CER**, **Trend - ARPC**, **Trend - CGR**, and **Trend - CSAT**:

1. Place `Completed Actual Value` on Rows as a **solid line**.
2. Place `Forecast Value` on a dual axis as a **dashed line** (or a lighter color if dashes are unavailable).
3. Synchronize the dual axis.
4. Add `Partial Actual Value` as circles with a white fill and orange outline. Do not connect partial points into the completed or forecast line.
5. Color:
   - Completed actual — KPI color, solid
   - Forecast — same hue, dashed or 50% lighter
   - Partial actual — orange outline
6. Show a visible legend: **Completed Actual**, **Partial Actual**, **Forecast**.
7. Add the 95% prediction band:
   - Place `Forecast Lower` and `Forecast Upper` on the view
   - Use a dual-axis area, measure names/values area, or reference band between the two fields
   - Pale transparent fill
   - Keep the forecast line on top of the band
8. For **CSAT**, the band spans almost 1–5. That width must remain visible. Add the note:

> CSAT forecast uncertainty is very high because only 29 cleaned ratings have usable dates. Do not manage to the 5.00 upper bound.

### 16.4 Replace generic axis labels

Tableau often titles the Y-axis `AVG(Value)` or `Value`. Replace it on every chart.

1. Right-click the Y-axis > **Edit Axis**.
2. Under **Axis Title**, replace the default with the matching `axis_title` value:

| Worksheet | Axis title |
| --- | --- |
| Trend - CRR and CER | `Retention / Engagement (%)` |
| Trend - ARPC | `Average Revenue per Client (USD)` |
| Trend - CGR (left) | `Client Growth Rate (%)` |
| Trend - CGR (right) | `Cumulative Signed Clients (Count)` |
| Trend - CSAT | `Customer Satisfaction Score (1-5)` |

3. Keep percentage KPIs formatted as numbers with one decimal plus a `%` suffix. Do not use Tableau Percent format on 0–100 stored values.
4. Format ARPC as currency with thousands separators.
5. Fix CSAT from 1 to 5.

### 16.5 Replace the action table with a Decision Center

1. Hide or remove **Executive Action Table** from the dashboard. Keep `executive_recommendations.csv` in the folder if needed for backup, but do not display it.
2. Create a worksheet named **Decision Center** from the **Decision Center** source.
3. Drag `priority` to **Rows**.
4. Then add, in order:
   - `kpi_code`
   - `business_issue`
   - `specific_action`
   - `timeline`
   - `kpi_target`
   - `expected_impact`
5. Put `clients_or_records_affected` and `success_measure` in the tooltip or as extra wrapped columns if space allows.
6. Sort ascending by `priority`.
7. Hide the `priority` header after sorting, or show it as **P1–P5**.
8. Rename headers:

| Field | Header |
| --- | --- |
| kpi_code | KPI |
| business_issue | Priority issue |
| specific_action | Action |
| timeline | When |
| kpi_target | KPI target |
| expected_impact | Expected impact |

9. Enable text wrapping and increase row height. P1 is retention and should read as the top management action.

### 16.6 Rebuild the dashboard layout

1. Change dashboard size to about **1600 × 1100**, Fixed Size.
2. Keep the title and five KPI cards.
3. Keep the 2×2 chart grid.
4. Replace the bottom recommendations table with **Decision Center** across the full width.
5. Add a dashboard legend near the charts: solid = completed actual, outlined circle = partial period, dashed = forecast, shaded band = 95% prediction interval.
6. Fit objects to the container and wrap Decision Center text.

### 16.7 Validate the revised dashboard

Confirm these completed-period headlines:

- CRR 51.0% in Q1 2025, supporting text `25 of 49 prior-quarter active clients returned`
- ARPC $69,523 in Q1 2025
- CGR 11.0% in Q1 2025, supporting text `+9 net new clients; cumulative base 91`
- CER 49.5% in Q1 2025
- CSAT 4.14 / 5 in April 2025

Confirm Decision Center P1:

- About **5 extra clients** are at risk if Q1’s 51 active clients retain at 40.6% instead of 51%
- Target: completed Q2 CRR **at least 51.0%**
- Timeline: outreach before Q2 2025 close

If CGR still looks like a decline-only story, the dual-axis client count or card supporting text is missing.
