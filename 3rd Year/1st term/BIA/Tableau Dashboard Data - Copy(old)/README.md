# FinMark Tableau Executive Dashboard

This folder contains cleaned, derived CSV files for one executive Tableau dashboard based on the corrected KPI definitions in `BI Analytics Exploration Report .md`.

## Files

- `executive_kpi_cards.csv` — five current KPI cards, partial-period checks, forecast endpoints, definitions, and warnings.
- `executive_kpi_timeseries.csv` — historical actuals, partial-period observations, forecasts, and 95% prediction bounds in long format.
- `executive_recommendations.csv` — executive findings, risks, and actions by KPI.
- `generate_tableau_csvs.py` — reproducibly generates the three CSV files from the source files in `Data`.

## Recommended dashboard

Use a fixed desktop dashboard of approximately **1300 × 900 pixels**.

### 1. Five KPI cards

**Chart type:** Big-number KPI cards  
**Data source:** `executive_kpi_cards.csv`

Place the five cards across the top in `display_order`.

| Tableau role | Field |
| --- | --- |
| Filter | `kpi_code` — one KPI per card |
| Main text | `completed_value` |
| Heading | `kpi_name` |
| Supporting text | `completed_period`, `direction` |
| Tooltip | `partial_period`, `partial_value`, `forecast_end_period`, `forecast_end_value`, `definition`, `data_warning` |

Formatting:

- CRR, CGR, and CER: percentage with one decimal place.
- ARPC: currency with no decimals.
- CSAT: number with two decimals followed by `/ 5`.
- Label completed results as the headline. Do not substitute the partial-period result.
- Use a neutral color for `Broadly stable`, a caution color for declining/flattening risks, and clearly mark uncertain forecasts.

### 2. Retention and engagement trend

**Chart type:** Multi-series quarterly line chart  
**Purpose:** Compare repeat activity (CRR) with current-quarter participation (CER), which share the same percentage scale.  
**Data source:** `executive_kpi_timeseries.csv`

| Tableau role | Field |
| --- | --- |
| Filter | `kpi_code` = `CRR` or `CER` |
| Columns | Continuous `period_start` |
| Rows | `value` |
| Color | `kpi_code` |
| Detail | `series_type` |
| Tooltip | `period`, `series_type`, `value`, `numerator`, `denominator`, prediction bounds |

Use a 0–100% fixed axis. Display:

- `Completed Actual` as solid lines.
- `Forecast` as dashed or lighter lines.
- `Partial Actual` as a distinct shape at Q2 2025.
- Optional prediction-band area using `forecast_lower_95` and `forecast_upper_95`.

Do not join the CRR and CER values into one calculated KPI. They answer different questions.

### 3. Average Revenue per Client trend

**Chart type:** Quarterly line chart with forecast  
**Purpose:** Show whether revenue generated per active client is stable or changing.  
**Data source:** `executive_kpi_timeseries.csv`

Filter `kpi_code` to `ARPC`. Put continuous `period_start` on Columns and `value` on Rows. Format the axis and tooltip as currency. Use `series_type` to distinguish completed, partial, and forecast values. If prediction bounds are shown, label them as uncertainty rather than targets.

ARPC needs its own chart because its currency scale cannot be compared directly with percentage KPIs.

### 4. Client Growth Rate trend

**Chart type:** Quarterly line chart with forecast  
**Purpose:** Show the deceleration in quarter-over-quarter client-base growth.  
**Data source:** `executive_kpi_timeseries.csv`

Filter `kpi_code` to `CGR`. Put continuous `period_start` on Columns and `value` on Rows. Format as a percentage and distinguish `series_type`.

The forecast is constrained at 0%. Interpret it as a **flattening-risk signal**, not a literal guarantee of zero new clients. Do not add a 40% quarterly reference line: FinMark's 40% target is cumulative over five years, while this chart shows quarter-over-quarter growth.

### 5. Customer Satisfaction trend

**Chart type:** Monthly line chart with forecast  
**Purpose:** Show cleaned monthly satisfaction and the high uncertainty caused by sparse feedback.  
**Data source:** `executive_kpi_timeseries.csv`

Filter `kpi_code` to `CSAT`. Put continuous `period_start` on Columns and `value` on Rows. Fix the axis from **1 to 5**. Show:

- January–April 2025 as completed actuals.
- May 2025 as a partial actual point.
- May–August 2025 as forecasts.
- `forecast_lower_95` and `forecast_upper_95` in the tooltip or as a shaded band.

The wide 1–5 prediction range must remain visible in the tooltip or chart note so the forecast is not presented as precise.

### 6. Executive action table

**Chart type:** Text table  
**Data source:** `executive_recommendations.csv`

| Tableau role | Field |
| --- | --- |
| Rows | `display_order`, `kpi_code` |
| Text | `latest_finding`, `principal_risk`, `recommended_action` |

Hide `display_order` after sorting. Place this table across the bottom of the dashboard and enable text wrapping.

## Suggested layout

1. Title and data-as-of note.
2. Five KPI cards in one horizontal row.
3. CRR/CER chart on the upper left; ARPC chart on the upper right.
4. CGR chart on the lower left; CSAT chart on the lower right.
5. Executive action table across the bottom.

Recommended dashboard filters:

- KPI selector for optional focus mode.
- `series_type` only if users need to hide forecasts.
- Date range for historical exploration.

Do not add service, industry, region, or engagement-tier filters to this executive dashboard: the derived KPI rows are already aggregated and those dimensions are not present in these CSVs.

## Time-series field dictionary

| Field | Meaning |
| --- | --- |
| `kpi_code` | CRR, ARPC, CGR, CER, or CSAT |
| `period` | Display label such as `2025-Q1` or `2025-04` |
| `period_start` | Tableau-compatible date for chronological axes |
| `frequency` | `Quarterly` or `Monthly` |
| `series_type` | `Completed Actual`, `Partial Actual`, or `Forecast` |
| `value` | KPI result in native units; percentage metrics are stored as 0–100 values |
| `numerator` | Actual-period numerator used in the KPI formula |
| `denominator` | Actual-period denominator used in the KPI formula |
| `forecast_lower_95` | Approximate lower 95% prediction bound |
| `forecast_upper_95` | Approximate upper 95% prediction bound |
| `is_partial` | `Yes` only for the observed incomplete period |

## Calculation rules

- **CRR:** clients active in both the current and prior quarter ÷ clients active in the prior quarter × 100.
- **ARPC:** quarterly billed amount ÷ unique active clients in the quarter.
- **CGR:** net increase in cumulative signed clients ÷ prior-quarter cumulative signed clients × 100.
- **CER:** signed clients with an engagement in the quarter ÷ clients signed by quarter-end × 100.
- **CSAT:** valid rating total ÷ valid rating count after retaining one row per `feedback_id`.
- Forecasts use least-squares trends fitted to the latest six completed quarters or four completed CSAT months. Percentage values are constrained to 0–100%, and CSAT is constrained to 1–5.

## Verified headline values

| KPI | Latest completed result | Partial-period check |
| --- | ---: | ---: |
| CRR | 51.0% in Q1 2025 | 49.0% in Q2 2025 |
| ARPC | $69,523 in Q1 2025 | $63,387 in Q2 2025 |
| CGR | 11.0% in Q1 2025 | 9.9% in Q2 2025 |
| CER | 49.5% in Q1 2025 | 52.0% in Q2 2025 |
| CSAT | 4.14 in April 2025 | 3.60 in May 2025 |

## Data limitations

- 696 engagements predate their clients' recorded signup dates.
- 754 engagements and 74.1% of billed revenue have service IDs absent from the service master.
- 232 quantity values use the text format `2 units`.
- Feedback has duplicate IDs, missing ratings, and only 29 cleaned valid ratings with usable dates.
- Q2 2025 and May 2025 are partial periods and must not replace the latest completed results.

Older figures in `BI Insight Summary #1.md` used inconsistent definitions. `BI Insight Summary #2.md` currently contains unresolved merge-conflict text. The generated CSV package therefore uses the raw source files and the corrected definitions in the exploration report.

## Refreshing the CSV files

From the BIA folder, run:

```powershell
py -3 ".\Tableau Dashboard Data\generate_tableau_csvs.py"
```

Refresh the corresponding Tableau data sources after regeneration.
