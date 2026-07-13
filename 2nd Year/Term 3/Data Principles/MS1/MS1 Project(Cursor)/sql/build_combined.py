#!/usr/bin/env python3
"""Combine ordered SQL scripts into payrollsystem_db.sql"""

from pathlib import Path

SQL_DIR = Path(__file__).resolve().parent
OUTPUT = SQL_DIR / "payrollsystem_db.sql"

FILES = [
    "01_create_database.sql",
    "02_schema.sql",
    "03_seed_lookup.sql",
    "04_seed_employees.sql",
    "05_seed_statutory.sql",
    "06_validation_and_queries.sql",
]

parts = [
    "-- =============================================================================",
    "-- MotorPH Payroll System - Combined deployment script",
    "-- Database: payrollsystem_db | MySQL 8.0+",
    "-- Run: mysql -u root -p < payrollsystem_db.sql",
    "-- Constraint failure tests: run 07_constraint_tests.sql separately",
    "-- =============================================================================",
    "",
]

for name in FILES:
    path = SQL_DIR / name
    parts.append(f"-- >>> BEGIN {name}")
    parts.append(path.read_text(encoding="utf-8").strip())
    parts.append(f"-- <<< END {name}")
    parts.append("")

OUTPUT.write_text("\n\n".join(parts) + "\n", encoding="utf-8")
print(f"Wrote {OUTPUT} ({OUTPUT.stat().st_size // 1024} KB)")
