# FinMark Tableau Executive Dashboard

This folder contains cleaned, derived CSV files for one executive Tableau dashboard based on the corrected KPI definitions in `BI Analytics Exploration Report .md`.

## Files

- `executive_kpi_cards.csv` — five current KPI cards, partial-period checks, forecast endpoints, supporting text, definitions, and warnings.
- `executive_kpi_timeseries.csv` — historical actuals, partial-period observations, forecasts, 95% prediction bounds, and KPI-specific `axis_title` values.
- `client_base_timeseries.csv` — quarterly cumulative signed clients, net new clients, and CGR for a dual-axis growth chart.
- `executive_decision_center.csv` — ranked management actions with affected clients, timelines, KPI targets, and expected impact.
- `executive_recommendations.csv` — earlier findings table, retained as backup. Use the Decision Center on the dashboard instead.
- `generate_tableau_csvs.py` — regenerates the derived CSV files from the source files in `Data`.
- `TABLEAU_BUILD_GUIDE.md` — original build steps plus Section 16 revision instructions.
- `tableau_executive_dashboard_mockup.svg` — original dashboard sample.
- `tableau_executive_dashboard_revised_mockup.svg` — revised dashboard sample with CGR counts, required forecast encodings, specific axes, and the Decision Center.

## Recommended dashboard

Use a fixed desktop dashboard of approximately **1600 × 1100 pixels**.

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
| Tooltip | `partial_period`, `partial_value`, `forecast_end_period`, `forecast_end_value`, `supporting_text`, `definition`, `data_warning` |

Formatting:

- CRR, CGR, and CER: percentage with one decimal place.
- ARPC: currency with no decimals.
- CSAT: number with two decimals followed by `/ 5`.
- Label completed results as the headline. Do not substitute the partial-period result.
- Show `supporting_text` under the CGR headline (`+9 net new clients; cumulative base 91`) so a slower rate is not read as a shrinking client base.
- Use a caution color for declining retention/engagement, a neutral color for stable ARPC, and clearly mark uncertain CSAT forecasts.

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
- `Partial Actual` as a distinct outlined circle at Q2 2025 or May 2025.
- A visible 95% prediction band using `forecast_lower_95` and `forecast_upper_95`. This band is required, especially on CSAT.

Do not join the CRR and CER values into one calculated KPI. They answer different questions.

### 3. Average Revenue per Client trend

**Chart type:** Quarterly line chart with forecast  
**Purpose:** Show whether revenue generated per active client is stable or changing.  
**Data source:** `executive_kpi_timeseries.csv`

Filter `kpi_code` to `ARPC`. Put continuous `period_start` on Columns and `value` on Rows. Format the axis and tooltip as currency. Use `series_type` to distinguish completed, partial, and forecast values. If prediction bounds are shown, label them as uncertainty rather than targets.

ARPC needs its own chart because its currency scale cannot be compared directly with percentage KPIs.

### 4. Client Growth Rate and client count

**Chart type:** Dual-axis quarterly chart  
**Purpose:** Show that CGR % can fall while the signed-client count is still rising.  
**Data sources:** `executive_kpi_timeseries.csv` (CGR %) and `client_base_timeseries.csv` (cumulative signed clients)

Left axis: CGR % with completed, partial, and forecast encodings.  
Right axis: cumulative signed clients. Do not synchronize the axes.

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

The wide 1–5 prediction range must remain visible as a shaded band so the forecast is not presented as precise.

### 6. Decision Center

**Chart type:** Ranked text table  
**Data source:** `executive_decision_center.csv`

| Tableau role | Field |
| --- | --- |
| Rows | `priority`, `kpi_code` |
| Text | `business_issue`, `specific_action`, `timeline`, `kpi_target`, `expected_impact` |
| Tooltip | `clients_or_records_affected`, `success_measure` |

P1 is retention: keep completed Q2 CRR at least 51.0% or about 5 extra Q1-active clients fail to return. Place this table across the bottom of the dashboard and enable text wrapping.

## Suggested layout

1. Title and data-as-of note.
2. Five KPI cards in one horizontal row.
3. CRR/CER chart on the upper left; ARPC chart on the upper right.
4. CGR % plus signed-client count on the lower left; CSAT with a visible prediction band on the lower right.
5. Decision Center across the bottom.

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
| `axis_title` | Exact Y-axis label to use instead of Tableau defaults such as `AVG(Value)` |

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
