#!/usr/bin/env python3
"""Regenerate combined M2 Workbench scripts (13 and 15)."""

from pathlib import Path

REPORTS = Path(__file__).resolve().parent / "reports"


def read(name: str) -> str:
    return (REPORTS / name).read_text(encoding="utf-8").strip()


def payslip_view_block() -> str:
    text = read("employee_payslip_report.sql")
    start = text.index("USE payrollsystem_db;")
    end = text.index("-- =============================================================================\n-- VERIFICATION: Single employee payslip")
    return text[start:end].strip()


def summary_view_block() -> str:
    text = read("employees_payroll_summary_report.sql")
    start = text.index("DROP VIEW IF EXISTS vw_EmployeePayrollSummaryReport;")
    end = text.index("-- =============================================================================\n-- VERIFICATION: Full summary")
    return text[start:end].strip()


def procedures_block() -> str:
    text = read("14_m2_report_procedures.sql")
    start = text.index("USE payrollsystem_db;")
    end = text.index("-- =============================================================================\n-- VERIFICATION: Procedure calls")
    return text[start:end].strip()


def verification_tail() -> str:
    return """
SELECT 'M2 deploy complete. Test with:' AS NextStep;
SELECT COUNT(*) AS PayslipRows FROM vw_EmployeePayslipReport;
SELECT COUNT(*) AS SummaryRows FROM vw_EmployeePayrollSummaryReport;
CALL sp_GetEmployeePayslip(10013);
""".strip()


def write_13() -> None:
    header = """-- =============================================================================
-- MotorPH M2 - Run BOTH report views + stored procedures (MySQL Workbench)
--
-- Use this file if employees_payroll_summary_report.sql fails with:
--   Error 1146: vw_employeepayslipreport doesn't exist
--
-- Prerequisites: MS1 scripts 01-05, then 11, then 12
-- Regenerate after edits: python build_m2_reports.py
-- =============================================================================
"""
    out = REPORTS / "13_m2_run_all_reports.sql"
    body = "\n\n".join(
        [
            header.strip(),
            payslip_view_block(),
            summary_view_block(),
            procedures_block(),
            verification_tail(),
        ]
    )
    out.write_text(body + "\n", encoding="utf-8")
    print(f"Wrote {out.name} ({out.stat().st_size // 1024} KB)")


def write_15() -> None:
    header = """-- =============================================================================
-- MotorPH M2 - Full deploy: tax table + pay period seed + views + procedures
--
-- Prerequisites: MS1 scripts 01-05 only (this file includes 11 and 12)
-- Regenerate after edits: python build_m2_reports.py
-- =============================================================================
"""
    out = REPORTS / "15_m2_full_deploy.sql"
    body = "\n\n".join(
        [
            header.strip(),
            read("11_schema_semi_monthly_tax.sql"),
            read("12_seed_payslip_pay_period.sql"),
            payslip_view_block(),
            summary_view_block(),
            procedures_block(),
            verification_tail(),
        ]
    )
    out.write_text(body + "\n", encoding="utf-8")
    print(f"Wrote {out.name} ({out.stat().st_size // 1024} KB)")


def main() -> None:
    write_13()
    write_15()


if __name__ == "__main__":
    main()
