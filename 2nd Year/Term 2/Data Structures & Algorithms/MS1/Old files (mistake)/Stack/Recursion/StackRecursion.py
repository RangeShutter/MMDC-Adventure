# Recursion in Stack - Recursively pop until empty (MotorPH)


def push(stack, action, brand, quantity):
    """Push inventory action onto stack."""
    stack.append({"action": action, "brand": brand, "quantity": quantity})


def recursive_pop(stack):
    """Recursively pop stack until empty (base case: empty stack)."""
    if not stack:
        return
    item = stack.pop()
    print(f"  Popped: [{item['action']}] {item['brand']}: {item['quantity']} units")
    recursive_pop(stack)


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

    print("Stack before recursive pop:")
    display(stack)

    # Recursively pop until empty
    print("\nRecursive pop (emptying stack):")
    recursive_pop(stack)

    print("\nStack after recursive pop:")
    display(stack)


if __name__ == "__main__":
    main()
#