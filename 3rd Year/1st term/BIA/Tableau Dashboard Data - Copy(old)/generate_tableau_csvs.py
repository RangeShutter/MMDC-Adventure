"""Generate Tableau-ready CSVs for the FinMark executive KPI dashboard.

Uses only the Python standard library. Run from any directory:
    python "Tableau Dashboard Data/generate_tableau_csvs.py"
"""

from __future__ import annotations

import csv
import math
from collections import defaultdict
from datetime import datetime
from pathlib import Path


ROOT = Path(__file__).resolve().parent.parent
DATA_DIR = ROOT / "Data"
OUTPUT_DIR = Path(__file__).resolve().parent

LATEST_COMPLETED_QUARTER = "2025-Q1"
PARTIAL_QUARTER = "2025-Q2"
LATEST_COMPLETED_MONTH = "2025-04"
PARTIAL_MONTH = "2025-05"

KPI_NAMES = {
    "CRR": "Customer Retention Rate",
    "ARPC": "Average Revenue per Client",
    "CGR": "Client Growth Rate",
    "CER": "Customer Engagement Rate",
    "CSAT": "Customer Satisfaction Score",
}


def read_csv(name: str) -> list[dict[str, str]]:
    with (DATA_DIR / name).open(newline="", encoding="utf-8-sig") as handle:
        return list(csv.DictReader(handle))


def parse_date(value: str) -> datetime | None:
    value = (value or "").strip()
    for date_format in ("%Y-%m-%d", "%m/%d/%Y", "%Y-%m-%d %H:%M:%S"):
        try:
            return datetime.strptime(value, date_format)
        except ValueError:
            continue
    return None


def quarter_label(value: datetime) -> str:
    return f"{value.year}-Q{(value.month - 1) // 3 + 1}"


def quarter_start(label: str) -> str:
    year = int(label[:4])
    quarter = int(label[-1])
    return f"{year:04d}-{(quarter - 1) * 3 + 1:02d}-01"


def next_quarter(label: str) -> str:
    year = int(label[:4])
    quarter = int(label[-1])
    if quarter == 4:
        return f"{year + 1}-Q1"
    return f"{year}-Q{quarter + 1}"


def next_month(label: str) -> str:
    value = datetime.strptime(label, "%Y-%m")
    if value.month == 12:
        return f"{value.year + 1}-01"
    return f"{value.year}-{value.month + 1:02d}"


def quarter_end_exclusive(label: str) -> datetime:
    year = int(label[:4])
    quarter = int(label[-1])
    if quarter == 4:
        return datetime(year + 1, 1, 1)
    return datetime(year, quarter * 3 + 1, 1)


def clamp(value: float, lower: float | None, upper: float | None) -> float:
    if lower is not None:
        value = max(lower, value)
    if upper is not None:
        value = min(upper, value)
    return value


def linear_forecast(
    values: list[float],
    periods: int,
    lower_limit: float | None = None,
    upper_limit: float | None = None,
) -> list[tuple[float, float, float]]:
    """Return forecast, lower PI, and upper PI using a least-squares trend."""
    n = len(values)
    x_values = list(range(n))
    x_mean = sum(x_values) / n
    y_mean = sum(values) / n
    sxx = sum((x - x_mean) ** 2 for x in x_values)
    slope = sum((x - x_mean) * (y - y_mean) for x, y in zip(x_values, values)) / sxx
    intercept = y_mean - slope * x_mean

    residuals = [y - (intercept + slope * x) for x, y in zip(x_values, values)]
    residual_standard_error = math.sqrt(sum(r * r for r in residuals) / (n - 2))
    # 95% Student-t critical values for the sample sizes used here.
    t_critical = {2: 4.303, 4: 2.776}[n - 2]

    results = []
    for offset in range(periods):
        x_value = n + offset
        raw_prediction = intercept + slope * x_value
        prediction_error = (
            t_critical
            * residual_standard_error
            * math.sqrt(1 + (1 / n) + ((x_value - x_mean) ** 2 / sxx))
        )
        prediction = clamp(raw_prediction, lower_limit, upper_limit)
        lower = clamp(raw_prediction - prediction_error, lower_limit, upper_limit)
        upper = clamp(raw_prediction + prediction_error, lower_limit, upper_limit)
        results.append((prediction, lower, upper))
    return results


def series_row(
    kpi_code: str,
    period: str,
    frequency: str,
    series_type: str,
    value: float,
    numerator: float | int | None = None,
    denominator: float | int | None = None,
    lower_bound: float | None = None,
    upper_bound: float | None = None,
) -> dict[str, object]:
    period_start = quarter_start(period) if frequency == "Quarterly" else f"{period}-01"
    return {
        "kpi_code": kpi_code,
        "kpi_name": KPI_NAMES[kpi_code],
        "period": period,
        "period_start": period_start,
        "frequency": frequency,
        "series_type": series_type,
        "value": round(value, 4),
        "numerator": "" if numerator is None else round(numerator, 4),
        "denominator": "" if denominator is None else round(denominator, 4),
        "forecast_lower_95": "" if lower_bound is None else round(lower_bound, 4),
        "forecast_upper_95": "" if upper_bound is None else round(upper_bound, 4),
        "is_partial": "Yes" if series_type == "Partial Actual" else "No",
    }


def calculate_quarterly_series(
    clients: list[dict[str, str]],
    engagements: list[dict[str, str]],
) -> tuple[list[dict[str, object]], dict[str, dict[str, float]]]:
    signup_dates = {
        row["customer_id"]: parse_date(row["signup_date"])
        for row in clients
    }
    by_quarter: dict[str, list[dict[str, str]]] = defaultdict(list)
    for row in engagements:
        engagement_date = parse_date(row["engagement_date"])
        if engagement_date is None:
            continue
        by_quarter[quarter_label(engagement_date)].append(row)

    quarters = sorted(by_quarter)
    active_clients = {
        period: {row["customer_id"] for row in rows}
        for period, rows in by_quarter.items()
    }
    metrics: dict[str, dict[str, float]] = defaultdict(dict)
    components: dict[tuple[str, str], tuple[float, float]] = {}
    previous_cumulative: int | None = None

    for index, period in enumerate(quarters):
        rows = by_quarter[period]
        active = active_clients[period]
        revenue = sum(float(row["billed_amount"]) for row in rows)
        metrics["ARPC"][period] = revenue / len(active)
        components[("ARPC", period)] = (revenue, len(active))

        if index > 0:
            previous_active = active_clients[quarters[index - 1]]
            retained = len(active & previous_active)
            metrics["CRR"][period] = retained / len(previous_active) * 100
            components[("CRR", period)] = (retained, len(previous_active))

        eligible = {
            customer_id
            for customer_id, signup_date in signup_dates.items()
            if signup_date is not None and signup_date < quarter_end_exclusive(period)
        }
        cumulative = len(eligible)
        if cumulative:
            engaged_eligible = len(active & eligible)
            metrics["CER"][period] = engaged_eligible / cumulative * 100
            components[("CER", period)] = (engaged_eligible, cumulative)

        if previous_cumulative not in (None, 0):
            net_new = cumulative - previous_cumulative
            metrics["CGR"][period] = net_new / previous_cumulative * 100
            components[("CGR", period)] = (net_new, previous_cumulative)
        previous_cumulative = cumulative

    rows: list[dict[str, object]] = []
    for kpi_code in ("CRR", "ARPC", "CGR", "CER"):
        for period, value in sorted(metrics[kpi_code].items()):
            if period > PARTIAL_QUARTER:
                continue
            series_type = "Partial Actual" if period == PARTIAL_QUARTER else "Completed Actual"
            numerator, denominator = components[(kpi_code, period)]
            rows.append(
                series_row(
                    kpi_code,
                    period,
                    "Quarterly",
                    series_type,
                    value,
                    numerator,
                    denominator,
                )
            )

        completed_values = [
            value
            for period, value in sorted(metrics[kpi_code].items())
            if period <= LATEST_COMPLETED_QUARTER
        ][-6:]
        limits = (0.0, 100.0) if kpi_code in {"CRR", "CGR", "CER"} else (0.0, None)
        forecasts = linear_forecast(completed_values, 4, *limits)
        forecast_period = PARTIAL_QUARTER
        for prediction, lower, upper in forecasts:
            rows.append(
                series_row(
                    kpi_code,
                    forecast_period,
                    "Quarterly",
                    "Forecast",
                    prediction,
                    lower_bound=lower,
                    upper_bound=upper,
                )
            )
            forecast_period = next_quarter(forecast_period)

    return rows, metrics


def calculate_csat_series(
    feedback: list[dict[str, str]],
) -> tuple[list[dict[str, object]], dict[str, float]]:
    unique_feedback: dict[str, dict[str, str]] = {}
    for row in feedback:
        unique_feedback.setdefault(row["feedback_id"], row)

    by_month: dict[str, list[float]] = defaultdict(list)
    for row in unique_feedback.values():
        submitted_at = parse_date(row["submitted_at"])
        rating = (row["rating"] or "").strip()
        if submitted_at is not None and rating:
            by_month[submitted_at.strftime("%Y-%m")].append(float(rating))

    monthly_values = {
        period: sum(values) / len(values)
        for period, values in sorted(by_month.items())
    }
    rows = []
    for period, values in sorted(by_month.items()):
        if period > PARTIAL_MONTH:
            continue
        series_type = "Partial Actual" if period == PARTIAL_MONTH else "Completed Actual"
        rows.append(
            series_row(
                "CSAT",
                period,
                "Monthly",
                series_type,
                sum(values) / len(values),
                sum(values),
                len(values),
            )
        )

    completed_values = [
        value
        for period, value in sorted(monthly_values.items())
        if period <= LATEST_COMPLETED_MONTH
    ][-4:]
    forecasts = linear_forecast(completed_values, 4, 1.0, 5.0)
    forecast_period = PARTIAL_MONTH
    for prediction, lower, upper in forecasts:
        rows.append(
            series_row(
                "CSAT",
                forecast_period,
                "Monthly",
                "Forecast",
                prediction,
                lower_bound=lower,
                upper_bound=upper,
            )
        )
        forecast_period = next_month(forecast_period)

    return rows, monthly_values


def write_csv(path: Path, rows: list[dict[str, object]], fieldnames: list[str]) -> None:
    with path.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.DictWriter(handle, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)


def main() -> None:
    clients = read_csv("finmark_clients.csv")
    engagements = read_csv("FinMark_Engagements.csv")
    feedback = read_csv("FinMark_client_feedback.csv")

    quarterly_rows, quarterly_metrics = calculate_quarterly_series(clients, engagements)
    csat_rows, csat_metrics = calculate_csat_series(feedback)
    timeseries_rows = sorted(
        quarterly_rows + csat_rows,
        key=lambda row: (
            list(KPI_NAMES).index(str(row["kpi_code"])),
            str(row["period_start"]),
            {"Completed Actual": 0, "Partial Actual": 1, "Forecast": 2}[str(row["series_type"])],
        ),
    )

    time_fields = [
        "kpi_code",
        "kpi_name",
        "period",
        "period_start",
        "frequency",
        "series_type",
        "value",
        "numerator",
        "denominator",
        "forecast_lower_95",
        "forecast_upper_95",
        "is_partial",
    ]
    write_csv(OUTPUT_DIR / "executive_kpi_timeseries.csv", timeseries_rows, time_fields)

    forecast_end = {
        code: next(
            row
            for row in reversed(timeseries_rows)
            if row["kpi_code"] == code and row["series_type"] == "Forecast"
        )
        for code in KPI_NAMES
    }
    card_specs = [
        ("CRR", 1, LATEST_COMPLETED_QUARTER, quarterly_metrics["CRR"][LATEST_COMPLETED_QUARTER],
         PARTIAL_QUARTER, quarterly_metrics["CRR"][PARTIAL_QUARTER], "Percent", "Declining risk",
         "Active clients returning from the prior quarter divided by prior-quarter active clients.",
         "696 engagements predate recorded signup dates, weakening lifecycle interpretation."),
        ("ARPC", 2, LATEST_COMPLETED_QUARTER, quarterly_metrics["ARPC"][LATEST_COMPLETED_QUARTER],
         PARTIAL_QUARTER, quarterly_metrics["ARPC"][PARTIAL_QUARTER], "Currency", "Broadly stable",
         "Quarterly billed amount divided by unique active clients.",
         "74.1% of billed revenue cannot be assigned to a named service, although headline ARPC remains calculable."),
        ("CGR", 3, LATEST_COMPLETED_QUARTER, quarterly_metrics["CGR"][LATEST_COMPLETED_QUARTER],
         PARTIAL_QUARTER, quarterly_metrics["CGR"][PARTIAL_QUARTER], "Percent", "Flattening risk",
         "Quarter-over-quarter change in cumulative clients divided by the prior cumulative client count.",
         "The 40% strategic target is cumulative over five years, not a quarterly CGR target."),
        ("CER", 4, LATEST_COMPLETED_QUARTER, quarterly_metrics["CER"][LATEST_COMPLETED_QUARTER],
         PARTIAL_QUARTER, quarterly_metrics["CER"][PARTIAL_QUARTER], "Percent", "Declining risk",
         "Clients signed by quarter-end who engaged during the quarter divided by all clients signed by quarter-end.",
         "Pre-signup engagements make earlier historical comparisons less reliable."),
        ("CSAT", 5, LATEST_COMPLETED_MONTH, csat_metrics[LATEST_COMPLETED_MONTH],
         PARTIAL_MONTH, csat_metrics[PARTIAL_MONTH], "Score out of 5", "Improving but uncertain",
         "Average valid rating after duplicate feedback IDs and missing ratings are removed.",
         "Only 29 deduplicated valid ratings have usable dates; the forecast is highly uncertain."),
    ]
    card_rows = []
    for code, order, completed_period, completed_value, partial_period, partial_value, unit, direction, definition, warning in card_specs:
        endpoint = forecast_end[code]
        card_rows.append({
            "display_order": order,
            "kpi_code": code,
            "kpi_name": KPI_NAMES[code],
            "completed_period": completed_period,
            "completed_value": round(completed_value, 4),
            "partial_period": partial_period,
            "partial_value": round(partial_value, 4),
            "forecast_end_period": endpoint["period"],
            "forecast_end_value": endpoint["value"],
            "unit": unit,
            "direction": direction,
            "definition": definition,
            "data_warning": warning,
        })
    card_fields = list(card_rows[0])
    write_csv(OUTPUT_DIR / "executive_kpi_cards.csv", card_rows, card_fields)

    recommendation_rows = [
        {"display_order": 1, "kpi_code": "CRR", "latest_finding": "Q1 2025 retention is 51.0%; the model declines to 40.6% by Q1 2026.", "principal_risk": "Approximately half of active clients do not return in the following quarter.", "recommended_action": "Trigger quarterly inactivity alerts and targeted re-engagement."},
        {"display_order": 2, "kpi_code": "ARPC", "latest_finding": "Q1 2025 ARPC is $69,523; the forecast remains near $67,000.", "principal_risk": "Partial Q2 is lower, and incomplete service references prevent reliable service-level attribution.", "recommended_action": "Bundle and cross-sell services; complete the service master before service-level decisions."},
        {"display_order": 3, "kpi_code": "CGR", "latest_finding": "Quarterly growth slowed to 11.0%; partial Q2 remains positive at 9.9%.", "principal_risk": "Percentage growth is decelerating as the client base increases.", "recommended_action": "Set quarterly net-client targets and strengthen referral and segment campaigns."},
        {"display_order": 4, "kpi_code": "CER", "latest_finding": "Q1 2025 engagement is 49.5%; the model declines to 45.4% by Q1 2026.", "principal_risk": "Many eligible clients are inactive, and pre-signup engagements weaken comparisons.", "recommended_action": "Create engagement alerts and enforce chronological validation."},
        {"display_order": 5, "kpi_code": "CSAT", "latest_finding": "April CSAT is 4.14, while partial May is 3.60.", "principal_risk": "Sparse dated feedback makes the upward forecast highly uncertain.", "recommended_action": "Improve feedback collection, review low ratings, and recalculate monthly."},
    ]
    recommendation_fields = list(recommendation_rows[0])
    write_csv(
        OUTPUT_DIR / "executive_recommendations.csv",
        recommendation_rows,
        recommendation_fields,
    )

    print(f"Generated {len(card_rows)} KPI card rows")
    print(f"Generated {len(timeseries_rows)} time-series rows")
    print(f"Generated {len(recommendation_rows)} recommendation rows")


if __name__ == "__main__":
    main()
