# Search in Stack - Search by brand without destroying the stack (MotorPH)


def push(stack, action, brand, quantity):
    """Push inventory action onto stack."""
    stack.append({"action": action, "brand": brand, "quantity": quantity})


def search(stack, target_brand):
    """Search stack for item with target brand. Uses temp stack to preserve original stack. Returns item if found, else None."""
    temp_stack = []
    found_item = None

    while stack:
        item = stack.pop()
        if item["brand"] == target_brand:
            found_item = item
        temp_stack.append(item)

    # Restore stack
    while temp_stack:
        stack.append(temp_stack.pop())

    return found_item


def display(stack):
    """Display stack (top to bottom)."""
    if not stack:
        print("  (stack empty)")
        return
    for i, item in enumerate(reversed(stack)):
        print(f"  {i + 1}. [{item['action']}] {item['brand']}: {item['quantity']} units")


def main():
    # Stack for inventory actions
    stack = []

    # Push inventory actions
    push(stack, "add", "Honda", 5)
    push(stack, "add", "Yamaha", 3)
    push(stack, "add", "Suzuki", 7)
    push(stack, "add", "Honda", 10)

    print("Stack:")
    display(stack)

    # Search for brand (stack unchanged after search)
    print("\nSearch for 'Honda':")
    result = search(stack, "Honda")
    print(f"  Found: {result}")

    print("\nSearch for 'Kawasaki':")
    result = search(stack, "Kawasaki")
    print(f"  Found: {result}")

    print("\nStack after searches (unchanged):")
    display(stack)


if __name__ == "__main__":
    main()
#
