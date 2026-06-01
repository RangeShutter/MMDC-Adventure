# Search in Array - Inventory (MotorPH)


def search_inventory(inventory, brand):
    """Search inventory array by brand. Returns item if found, else None."""
    for item in inventory:
        if item["brand"] == brand:
            return item
    return None


def display(inventory):
    """Display inventory list."""
    if not inventory:
        print("  (empty)")
        return
    for item in inventory:
        print(f"  - {item['brand']} {item['model']}: {item['quantity']} units")


def main():
    # Inventory array
    inventory = [
        {"brand": "Honda", "model": "Click 125", "quantity": 10},
        {"brand": "Yamaha", "model": "NMAX 155", "quantity": 5},
        {"brand": "Suzuki", "model": "Burgman Street", "quantity": 7},
    ]

    print("Inventory:")
    display(inventory)

    # Search by brand
    print("\nSearch for 'Yamaha':")
    result = search_inventory(inventory, "Yamaha")
    print(f"  {result if result else 'Not found'}")

    print("\nSearch for 'Honda':")
    result = search_inventory(inventory, "Honda")
    print(f"  {result if result else 'Not found'}")

    print("\nSearch for 'Kawasaki':")
    result = search_inventory(inventory, "Kawasaki")
    print(f"  {result if result else 'Not found'}")


if __name__ == "__main__":
    main()
