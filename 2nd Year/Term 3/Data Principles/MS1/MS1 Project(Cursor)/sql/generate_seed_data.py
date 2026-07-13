#!/usr/bin/env python3
"""Generate 04_seed_employees.sql from MotorPH_Employee Details.md"""

import re
from pathlib import Path
from datetime import datetime

DOC = Path(__file__).resolve().parent.parent / "document" / "MotorPH_Employee Details.md"
OUT = Path(__file__).resolve().parent / "04_seed_employees.sql"


def sql_escape(s: str) -> str:
    return s.replace("\\", "\\\\").replace("'", "''")


def parse_money(val: str) -> float:
    return float(val.replace(",", "").strip())


def parse_date(md: str) -> str:
    m, d, y = md.strip().split("/")
    return f"{y}-{int(m):02d}-{int(d):02d}"


def dept_id(position: str) -> int:
    p = position.strip()
    if any(
        k in p
        for k in (
            "Chief Executive Officer",
            "Chief Operating Officer",
            "Chief Finance Officer",
            "Chief Marketing Officer",
        )
    ):
        return 1
    if "IT Operations" in p:
        return 2
    if p.startswith("HR") or "HR " in p:
        return 3
    if "Payroll" in p or "Accounting" in p:
        return 4
    if "Sales & Marketing" in p or p == "Sales & Marketing":
        return 6
    if "Supply Chain" in p:
        return 7
    if "Customer Service" in p:
        return 8
    if "Account" in p:
        return 5
    return 5


def status_id(status: str) -> int:
    return 1 if status.strip() == "Regular" else 2


def fix_pagibig(val: str, emp_id: int) -> str:
    val = val.strip()
    if "E+" in val.upper() or "e+" in val:
        return "697764000000" if emp_id == 10032 else val.replace(".", "").split("E")[0]
    return val.replace(",", "")


def parse_employees():
    lines = DOC.read_text(encoding="utf-8").splitlines()
    rows = []
    for line in lines[2:]:
        if not line.startswith("|") or line.startswith("|--") or "Employee #" in line:
            continue
        parts = [c.strip() for c in line.split("|")[1:-1]]
        if len(parts) < 18 or not parts[0].isdigit():
            continue
        rows.append(
            {
                "id": int(parts[0]),
                "last": parts[1],
                "first": parts[2],
                "dob": parse_date(parts[3]),
                "address": parts[4],
                "phone": parts[5],
                "sss": parts[6],
                "phil": parts[7],
                "tin": parts[8],
                "pagibig": fix_pagibig(parts[9], int(parts[0])),
                "status": status_id(parts[10]),
                "position": parts[11],
                "salary": parse_money(parts[13]),
                "rice": parse_money(parts[14]),
                "phone_allow": parse_money(parts[15]),
                "clothing": parse_money(parts[16]),
            }
        )
    return rows


def main():
    employees = parse_employees()
    assert len(employees) == 34, f"Expected 34 employees, got {len(employees)}"

    lines = [
        "-- MotorPH Payroll System - Employee and related seed data",
        "-- Generated from document/MotorPH_Employee Details.md",
        f"-- Generated: {datetime.now().isoformat(timespec='seconds')}",
        "",
        "USE payrollsystem_db;",
        "",
        "-- Employees",
        "INSERT INTO Employee (EmployeeID, FirstName, LastName, DateOfBirth, Address, ContactNumber, Position, DepartmentID, StatusID) VALUES",
    ]

    emp_vals = []
    for e in employees:
        emp_vals.append(
            f"({e['id']}, '{sql_escape(e['first'])}', '{sql_escape(e['last'])}', '{e['dob']}', NULL, "
            f"'{sql_escape(e['phone'])}', '{sql_escape(e['position'])}', {dept_id(e['position'])}, {e['status']})"
        )
    lines.append(",\n".join(emp_vals) + ";")
    lines.append("")

  # Department managers
    managers = [
        (1, 10001),
        (2, 10005),
        (3, 10006),
        (4, 10010),
        (5, 10015),
        (6, 10004),
        (7, 10033),
        (8, 10034),
    ]
    lines.append("-- Assign department managers")
    for dept, mgr in managers:
        lines.append(f"UPDATE Department SET ManagerID = {mgr} WHERE DepartmentID = {dept};")
    lines.append("")

    lines.append("-- Employee addresses (full address in StreetName)")
    lines.append(
        "INSERT INTO EmployeeAddress (EmployeeID, StreetName, Barangay, City, Province, ZIPCode) VALUES"
    )
    addr_vals = []
    for e in employees:
        addr_vals.append(
            f"({e['id']}, '{sql_escape(e['address'])}', NULL, NULL, NULL, NULL)"
        )
    lines.append(",\n".join(addr_vals) + ";")
    lines.append("")

    lines.append("INSERT INTO GovernmentID (EmployeeID, SSSNumber, PhilHealthNumber, TINNumber, PagIBIGNumber) VALUES")
    gov_vals = []
    for e in employees:
        gov_vals.append(
            f"({e['id']}, '{sql_escape(e['sss'])}', '{sql_escape(e['phil'])}', "
            f"'{sql_escape(e['tin'])}', '{sql_escape(e['pagibig'])}')"
        )
    lines.append(",\n".join(gov_vals) + ";")
    lines.append("")

    lines.append(
        "INSERT INTO Salary (EmployeeID, BaseSalary, PayFrequency, EffectiveFrom, EffectiveTo) VALUES"
    )
    sal_vals = [f"({e['id']}, {e['salary']:.2f}, 'Monthly', '2024-01-01', NULL)" for e in employees]
    lines.append(",\n".join(sal_vals) + ";")
    lines.append("")

    lines.append("INSERT INTO Benefit (EmployeeID, BenefitType, Amount) VALUES")
    ben_vals = []
    for e in employees:
        for btype, amt in [
            ("Rice Subsidy", e["rice"]),
            ("Phone Allowance", e["phone_allow"]),
            ("Clothing Allowance", e["clothing"]),
        ]:
            if amt > 0:
                ben_vals.append(f"({e['id']}, '{btype}', {amt:.2f})")
    lines.append(",\n".join(ben_vals) + ";")
    lines.append("")

    OUT.write_text("\n".join(lines), encoding="utf-8")
    print(f"Wrote {OUT} ({len(employees)} employees, {len(ben_vals)} benefits)")


if __name__ == "__main__":
    main()
