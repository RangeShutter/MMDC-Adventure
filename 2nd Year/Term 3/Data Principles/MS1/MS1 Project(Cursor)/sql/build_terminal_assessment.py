#!/usr/bin/env python3
"""Build Terminal Assessment/payrollsystem_db_final.sql (MS1 + M2 views/procedures)."""

from pathlib import Path

SQL_DIR = Path(__file__).resolve().parent
REPORTS = SQL_DIR / "reports"
OUT_DIR = SQL_DIR.parent / "Terminal Assessment"
OUTPUT = OUT_DIR / "payrollsystem_db_final.sql"


def read(path: Path) -> str:
    return path.read_text(encoding="utf-8").strip()


def payslip_view_block() -> str:
    text = read(REPORTS / "employee_payslip_report.sql")
    start = text.index("USE payrollsystem_db;")
    end = text.index("-- =============================================================================\n-- VERIFICATION: Official template check")
    return text[start:end].strip()


def summary_view_block() -> str:
    text = read(REPORTS / "employees_payroll_summary_report.sql")
    start = text.index("DROP VIEW IF EXISTS vw_EmployeePayrollOverallTotals;")
    end = text.index("-- =============================================================================\n-- VERIFICATION: Official template monthly detail summary")
    return text[start:end].strip()


def procedures_block() -> str:
    text = read(REPORTS / "14_m2_report_procedures.sql")
    start = text.index("USE payrollsystem_db;")
    end = text.index("SELECT '=== sp_GetEmployeePayslip(10013)")
    return text[start:end].strip()


def main() -> None:
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    parts = [
        "-- =============================================================================",
        "-- MotorPH Payroll System - FINALIZED database script (Terminal Assessment)",
        "-- Database: payrollsystem_db | MySQL 8.0+",
        "-- Includes: MS1 schema/seed + M2 semi-monthly tax, pay periods, report views,",
        "--           and stored procedures (definitions only).",
        "-- Test cases: run 16_terminal_assessment_test_cases.sql separately.",
        "-- Regenerate: python sql/build_terminal_assessment.py",
        "-- =============================================================================",
        "",
    ]

    ms1 = [
        "01_create_database.sql",
        "02_schema.sql",
        "03_seed_lookup.sql",
        "04_seed_employees.sql",
        "05_seed_statutory.sql",
    ]
    for name in ms1:
        parts.append(f"-- >>> BEGIN {name}")
        parts.append(read(SQL_DIR / name))
        parts.append(f"-- <<< END {name}")
        parts.append("")

    m2_files = [
        ("11_schema_semi_monthly_tax.sql", REPORTS / "11_schema_semi_monthly_tax.sql"),
        ("12_seed_payslip_pay_period.sql", REPORTS / "12_seed_payslip_pay_period.sql"),
    ]
    for name, path in m2_files:
        parts.append(f"-- >>> BEGIN reports/{name}")
        parts.append(read(path))
        parts.append(f"-- <<< END reports/{name}")
        parts.append("")

    parts.append("-- >>> BEGIN employee_payslip_report.sql (VIEW only)")
    parts.append(payslip_view_block())
    parts.append("-- <<< END employee_payslip_report.sql")
    parts.append("")

    parts.append("-- >>> BEGIN employees_payroll_summary_report.sql (VIEWS only)")
    # Ensure USE is present before DROP VIEW block
    parts.append("USE payrollsystem_db;")
    parts.append("")
    parts.append(summary_view_block())
    parts.append("-- <<< END employees_payroll_summary_report.sql")
    parts.append("")

    parts.append("-- >>> BEGIN 14_m2_report_procedures.sql (PROCEDURES only)")
    parts.append(procedures_block())
    parts.append("-- <<< END 14_m2_report_procedures.sql")
    parts.append("")

    parts.append("SELECT 'payrollsystem_db_final.sql deploy complete' AS Status;")
    parts.append("SELECT COUNT(*) AS EmployeeCount FROM Employee;")
    parts.append(
        "SELECT COUNT(*) AS PayslipViewRows FROM vw_EmployeePayslipReport;"
    )
    parts.append(
        "SELECT COUNT(*) AS SummaryViewRows FROM vw_EmployeePayrollSummaryReport;"
    )

    OUTPUT.write_text("\n\n".join(parts) + "\n", encoding="utf-8")
    print(f"Wrote {OUTPUT} ({OUTPUT.stat().st_size // 1024} KB)")


if __name__ == "__main__":
    main()
