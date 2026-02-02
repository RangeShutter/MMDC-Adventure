# Recursion in Array - Recursive search in inventory (MotorPH)


def recursive_search(inventory, brand, index=0):
    """Search inventory array by brand using recursion."""
    if index >= len(inventory):
        return None
    if inventory[index]["brand"] == brand:
        return inventory[index]
    return recursive_search(inventory, brand, index + 1)


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

    # Recursive search by brand
    print("\nRecursive search for 'Yamaha':")
    result = recursive_search(inventory, "Yamaha")
    print(f"  {result if result else 'Not found'}")

    print("\nRecursive search for 'Honda':")
    result = recursive_search(inventory, "Honda")
    print(f"  {result if result else 'Not found'}")

    print("\nRecursive search for 'Kawasaki':")
    result = recursive_search(inventory, "Kawasaki")
    print(f"  {result if result else 'Not found'}")


if __name__ == "__main__":
    main()
