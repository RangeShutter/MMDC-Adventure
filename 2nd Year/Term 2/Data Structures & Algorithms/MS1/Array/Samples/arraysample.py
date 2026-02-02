def search_by_brand(inventory, brand):
    """Search inventory by brand. Returns the item or None if not found."""
    for item in inventory:
        if item["brand"] == brand:
            return item
    return None


def arraysample():
    # Simple inventory array for MotorPH
    inventory = []

    # Add new stock
    inventory.append({
        "brand": "Honda",
        "model": "Click 125",
        "quantity": 10
    })
    inventory.append({
        "brand": "Yamaha",
        "model": "NMAX 155",
        "quantity": 5
    })

    # Show initial inventory
    print("Inventory:")
    for item in inventory:
        print(f"  - {item['brand']} {item['model']}: {item['quantity']} units")

    # Search inventory by brand
    result = search_by_brand(inventory, "Honda")
    if result:
        print(f"\nFound: {result}")
    else:
        print("\nItem not found")

    # Sort inventory by brand
    inventory.sort(key=lambda x: x["brand"])
    print("\nSorted by brand:")
    for item in inventory:
        print(f"  - {item['brand']} {item['model']}: {item['quantity']} units")


if __name__ == "__main__":
    arraysample()