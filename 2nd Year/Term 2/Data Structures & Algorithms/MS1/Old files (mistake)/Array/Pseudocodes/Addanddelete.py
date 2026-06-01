# Add and Delete in Array - Inventory (MotorPH)


def add_item(inventory, brand, model, quantity):
    """Add inventory item (append to array)."""
    inventory.append({"brand": brand, "model": model, "quantity": quantity})


def delete_by_brand(inventory, brand):
    """Delete all inventory items with the given brand."""
    inventory[:] = [item for item in inventory if item["brand"] != brand]


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

    # Add inventory items
    add_item(inventory, "Honda", "Click 125", 10)
    add_item(inventory, "Yamaha", "Mio", 8)
    add_item(inventory, "Suzuki", "Burgman Street", 7)
    add_item(inventory, "Yamaha", "NMAX 155", 5)

    print("After adds:")
    display(inventory)

    # Delete inventory item by brand (removes all items with that brand)
    delete_by_brand(inventory, "Yamaha")
    print("\nAfter delete by brand 'Yamaha':")
    display(inventory)

    # Add one more
    add_item(inventory, "Kawasaki", "Ninja 125", 4)
    print("\nAfter add Kawasaki:")
    display(inventory)


if __name__ == "__main__":
    main()
