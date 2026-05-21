#!/usr/bin/env python3
"""Generate CRC Cards and Method Dictionary markdown from Java sources."""
import re
from pathlib import Path

SRC = Path(__file__).resolve().parent.parent / "src"
OUT = Path(__file__).resolve().parent.parent / "docs" / "CRC-and-Method-Dictionary.md"

PACKAGE_ORDER = ["model", "repository", "service", "ui", "util"]

EXTRA_CRC = [
    ("ApplicationContext.ItTicketRepositoryFallback",
     "Anonymous no-op IItTicketRepository used when ItTicketRepository cannot be loaded."),
]

JAVA_KEYWORDS = {
    "if", "else", "for", "while", "do", "switch", "case", "try", "catch", "finally",
    "return", "throw", "new", "super", "this", "break", "continue", "default",
    "synchronized", "import", "package", "class", "interface", "enum", "extends",
    "implements", "instanceof", "void", "int", "long", "double", "float", "boolean",
    "char", "byte", "short", "null", "true", "false",
}

def clean_javadoc(block: str) -> str:
    if not block:
        return ""
    lines = []
    for line in block.split("\n"):
        line = line.strip()
        if line.startswith("/**") or line.startswith("*/"):
            continue
        if line.startswith("*"):
            line = line[1:].strip()
        if line.startswith("@"):
            break
        if line:
            lines.append(line)
    if not lines:
        return ""
    text = " ".join(lines)
    text = re.sub(r"\[[\w\s]+\]\s*", "", text)
    text = re.sub(r"\{@link\s+([^}]+)\}", r"\1", text)
    return text.strip()

def find_javadoc_before(lines, idx):
    if idx <= 0:
        return ""
    j = idx - 1
    while j >= 0 and lines[j].strip() == "":
        j -= 1
    if j < 0 or "*/" not in lines[j]:
        return ""
    end = j
    while j >= 0 and "/**" not in lines[j]:
        j -= 1
    if j < 0:
        return ""
    return clean_javadoc("\n".join(lines[j : end + 1]))

def split_top_level_types(content: str, outer_name: str = ""):
    lines = content.split("\n")
    results = []
    i = 0
    while i < len(lines):
        m = re.search(
            r"^\s*(?:(public|private|protected)\s+)?(?:(static)\s+)?(?:(final)\s+)?(?:(abstract)\s+)?(class|interface|enum)\s+(\w+)",
            lines[i],
        )
        if not m:
            i += 1
            continue
        name = m.group(6)
        kind = m.group(5)
        jdoc = find_javadoc_before(lines, i)
        k = i
        while k < len(lines) and "{" not in lines[k]:
            k += 1
        if k >= len(lines):
            i += 1
            continue
        depth = 0
        start = k
        type_indent = len(lines[i]) - len(lines[i].lstrip())
        for ki in range(k, len(lines)):
            for ch in lines[ki]:
                if ch == "{":
                    depth += 1
                elif ch == "}":
                    depth -= 1
                    if depth == 0:
                        body = "\n".join(lines[start + 1 : ki])
                        is_nested = type_indent > 0
                        results.append((name, body, jdoc, kind, is_nested))
                        # Extract nested types declared inside this body
                        nested_outer = f"{outer_name}.{name}" if outer_name else name
                        for nname, nbody, njdoc, nkind, _ in split_top_level_types(
                            body, nested_outer
                        ):
                            results.append((nname, nbody, njdoc, nkind, True))
                        i = ki
                        break
            else:
                continue
            break
        i += 1
    return results

def detect_member_indent(body: str) -> int:
    for line in body.split("\n"):
        if re.match(r"^\s+(?:@\w+.*)?(?:public|private|protected)\s", line):
            return len(line) - len(line.lstrip())
        if re.match(r"^\s+(?:@\w+.*)?(?:public|private|protected)\s*\w+\s*\(", line):
            return len(line) - len(line.lstrip())
    return 4

def normalize_params(p: str) -> str:
    p = " ".join(p.split())
    return "none" if not p else p

def infer_purpose(name: str, ret: str) -> str:
    if name.startswith("get"):
        field = re.sub(r"([A-Z])", r" \1", name[3:]).strip().lower()
        return f"Returns {field}."
    if name.startswith("set"):
        field = re.sub(r"([A-Z])", r" \1", name[3:]).strip().lower()
        return f"Sets {field}."
    if name.startswith("is") or name.startswith("has"):
        return f"Returns whether {name[2:] if name.startswith('is') else name[3:]}."
    if ret == "void":
        return f"Performs {name}."
    return f"Returns result of {name}."

METHOD_RE = re.compile(
    r"^(?P<modifiers>(?:(?:public|private|protected)\s+)?(?:(?:static)\s+)?(?:(?:final|synchronized|abstract)\s+)*)"
    r"(?P<ret>[\w.<>,\[\]?&\s]+?)\s+"
    r"(?P<name>\w+)\s*"
    r"\((?P<params>[^)]*)\)"
    r"\s*(?:throws\s+[\w.<>,\s]+)?\s*[{;]"
)

CTOR_RE = re.compile(
    r"^(?P<modifiers>(?:(?:public|private|protected)\s+)?(?:(?:static)\s+)?)"
    r"(?P<name>\w+)\s*"
    r"\((?P<params>[^)]*)\)"
    r"\s*(?:throws\s+[\w.<>,\s]+)?\s*[{;]"
)

def parse_methods(body: str, class_name: str):
    methods = []
    seen = set()
    lines = body.split("\n")
    member_indent = detect_member_indent(body)
    pending_javadoc = ""
    i = 0
    while i < len(lines):
        line = lines[i]
        indent = len(line) - len(line.lstrip()) if line.strip() else -1

        if line.strip().startswith("/**"):
            doc = [line]
            if "*/" not in line:
                i += 1
                while i < len(lines) and "*/" not in lines[i]:
                    doc.append(lines[i])
                    i += 1
                if i < len(lines):
                    doc.append(lines[i])
            pending_javadoc = clean_javadoc("\n".join(doc))
            i += 1
            continue

        if indent == member_indent:
            sig = line.strip()
            j = i
            while j < len(lines) and "{" not in lines[j] and ";" not in lines[j]:
                if j > i:
                    sig += " " + lines[j].strip()
                j += 1
            if j < len(lines):
                sig += " " + lines[j].strip()

            sig_no_anno = re.sub(r"^(@\w+(?:\([^)]*\))?\s*)+", "", sig).strip()

            cm = CTOR_RE.match(sig_no_anno)
            if cm and cm.group("name") == class_name:
                vis = (cm.group("modifiers") or "").strip() or "package"
                params = normalize_params(cm.group("params"))
                purpose = pending_javadoc or f"Constructs a new {class_name} instance."
                key = (class_name, class_name, params)
                if key not in seen:
                    seen.add(key)
                    methods.append((class_name, class_name, purpose, vis, params, "—"))
                pending_javadoc = ""
                i = j + 1
                continue
            else:
                mm = METHOD_RE.match(sig_no_anno)
                if mm:
                    name = mm.group("name")
                    if name not in JAVA_KEYWORDS:
                        vis = (mm.group("modifiers") or "").strip() or "package"
                        ret = mm.group("ret").strip()
                        params = normalize_params(mm.group("params"))
                        purpose = pending_javadoc or infer_purpose(name, ret)
                        key = (class_name, name, params)
                        if key not in seen:
                            seen.add(key)
                            methods.append((class_name, name, purpose, vis, params, ret))
                        pending_javadoc = ""
                        i = j + 1
                        continue
                else:
                    pending_javadoc = ""
        elif indent > member_indent or (indent >= 0 and indent < member_indent):
            pending_javadoc = ""

        i += 1

    return methods

def class_responsibility(jdoc: str, name: str, kind: str, pkg: str) -> str:
    if jdoc:
        return jdoc
    if kind == "interface" and name.startswith("I"):
        return f"Contract for {name[1:]} operations."
    return f"{kind.capitalize()} {name} in the {pkg} layer."

def process_file(path: Path, pkg: str):
    content = path.read_text(encoding="utf-8")
    types = split_top_level_types(content, path.stem)
    entries = []
    seen_classes = set()
    for name, body, jdoc, kind, is_nested in types:
        display = f"{path.stem}.{name}" if is_nested else name
        if display in seen_classes:
            continue
        seen_classes.add(display)
        methods = parse_methods(body, name)
        entries.append({
            "package": pkg,
            "class": display,
            "responsibility": class_responsibility(jdoc, name, kind, pkg),
            "methods": methods,
        })
    return entries

def collect_all():
    by_pkg = {p: [] for p in PACKAGE_ORDER}
    for pkg in PACKAGE_ORDER:
        pkg_dir = SRC / pkg
        if not pkg_dir.exists():
            continue
        for path in sorted(pkg_dir.glob("*.java")):
            by_pkg[pkg].extend(process_file(path, pkg))
    for pkg in PACKAGE_ORDER:
        by_pkg[pkg].sort(key=lambda x: x["class"].lower())
    return by_pkg

def md_escape(s: str) -> str:
    return s.replace("|", "\\|").replace("\n", " ")

def build_markdown(by_pkg):
    lines = [
        "# GEAR.HR — CRC Cards and Method Dictionary",
        "",
        "GEAR.HR is a desktop HR system built in **Java Swing** for managing employees, attendance, leave requests, and payroll. Data is persisted to CSV files under `csv/`. The application entry point is `ui.Main`.",
        "",
        "## Table 1 — CRC Cards",
        "",
        "| Class Name | Responsibility |",
        "|------------|----------------|",
    ]
    for pkg in PACKAGE_ORDER:
        for e in by_pkg[pkg]:
            lines.append(f"| `{e['class']}` | {md_escape(e['responsibility'])} |")
    for cls, resp in EXTRA_CRC:
        lines.append(f"| `{cls}` | {md_escape(resp)} |")

    lines.extend([
        "",
        "## Table 2 — Method Dictionary",
        "",
        "| Class | Method Name | Purpose | Visibility | Parameters | Return type |",
        "|-------|-------------|---------|------------|------------|-------------|",
    ])

    for pkg in PACKAGE_ORDER:
        for e in by_pkg[pkg]:
            c = e["class"]
            for _, mname, purpose, vis, params, ret in e["methods"]:
                lines.append(
                    f"| `{c}` | `{mname}` | {md_escape(purpose)} | {vis} | {md_escape(params)} | `{ret}` |"
                )

    lines.append(
        "| `ApplicationContext.ItTicketRepositoryFallback` | `load` | Implements IItTicketRepository.load (no-op empty list). | public | none | `List<ItTicket>` |"
    )
    lines.append(
        "| `ApplicationContext.ItTicketRepositoryFallback` | `save` | Implements IItTicketRepository.save (no-op). | public | `List<ItTicket> items` | `void` |"
    )

    return "\n".join(lines) + "\n"

def main():
    by_pkg = collect_all()
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(build_markdown(by_pkg), encoding="utf-8")
    total_classes = sum(len(by_pkg[p]) for p in PACKAGE_ORDER) + 1
    total_methods = sum(len(e["methods"]) for p in PACKAGE_ORDER for e in by_pkg[p]) + 2
    print(f"Wrote {OUT}")
    print(f"Classes: {total_classes}, Methods: {total_methods}")
    for pkg in PACKAGE_ORDER:
        for e in by_pkg[pkg]:
            print(f"  {e['class']}: {len(e['methods'])}")

if __name__ == "__main__":
    main()
