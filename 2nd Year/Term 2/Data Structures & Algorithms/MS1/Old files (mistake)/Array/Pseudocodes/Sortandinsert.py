# Sort and Insert in Array - Inventory (MotorPH)

def insert_item(inventory, brand, model, quantity):
    """Insert new inventory item (append to array)."""
    inventory.append({"brand": brand, "model": model, "quantity": quantity})


def sort_by_brand(inventory):
    """Sort inventory by brand."""
    inventory.sort(key=lambda x: x["brand"])


def display(inventory):
    """Display inventory list."""
    if not inventory:
        print("  (empty)")
        return
    for item in inventory:
        print(f"  - {item['brand']} {item['model']}: {item['quantity']} units")


def main():
    # Start with empty array
    inventory = []

    # Insert new inventory items
    insert_item(inventory, "Yamaha", "NMAX 155", 5)
    insert_item(inventory, "Honda", "PCX", 5)
    insert_item(inventory, "Suzuki", "Burgman Street", 7)

    print("After inserts (unsorted):")
    display(inventory)

    # Sort inventory by brand
    sort_by_brand(inventory)
    print("\nAfter sort by brand:")
    display(inventory)

    # Insert one more, then sort again
    insert_item(inventory, "Kawasaki", "Ninja 125", 4)
    print("\nAfter one more insert (unsorted):")
    display(inventory)
    sort_by_brand(inventory)
    print("After sort by brand:")
    display(inventory)


if __name__ == "__main__":
    main()
