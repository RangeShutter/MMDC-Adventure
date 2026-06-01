# Merge in Arrays - Merge two inventory arrays (MotorPH)


def merge_arrays(arr1, arr2):
    """Merge two inventory arrays (concatenate)."""
    return arr1 + arr2


def display(inventory):
    """Display inventory list."""
    if not inventory:
        print("  (empty)")
        return
    for item in inventory:
        b = item.get("brand", "")
        m = item.get("model", "")
        q = item.get("quantity", "")
        print(f"  - {b} {m}: {q} units")


def main():
    # Two branch inventory arrays
    branch1 = [
        {"brand": "Honda", "model": "Click 125", "quantity": 10},
        {"brand": "Honda", "model": "PCX", "quantity": 5},
    ]
    branch2 = [
        {"brand": "Suzuki", "model": "Burgman Street", "quantity": 7},
        {"brand": "Yamaha", "model": "NMAX 155", "quantity": 5},
    ]

    print("Branch 1:")
    display(branch1)
    print("\nBranch 2:")
    display(branch2)

    # Merge two inventory arrays
    merged_inventory = merge_arrays(branch1, branch2)
    print("\nMerged inventory (branch1 + branch2):")
    display(merged_inventory)

    # Optional: sort merged by brand for clearer view
    merged_inventory.sort(key=lambda x: x["brand"])
    print("\nMerged inventory (sorted by brand):")
    display(merged_inventory)


if __name__ == "__main__":
    main()
