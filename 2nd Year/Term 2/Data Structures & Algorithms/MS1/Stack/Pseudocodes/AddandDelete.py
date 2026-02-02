# Add & Delete in Stack - Push (add) and Pop (delete) inventory actions (MotorPH)


def push(stack, action, brand, quantity):
    """Push inventory action onto stack (add to top)."""
    stack.append({"action": action, "brand": brand, "quantity": quantity})


def pop(stack):
    """Pop inventory action from stack (delete/remove top). Returns item or None if empty."""
    if not stack:
        return None
    return stack.pop()


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

    # Push inventory actions (add)
    push(stack, "add", "Honda", 5)
    push(stack, "add", "Yamaha", 3)
    push(stack, "add", "Suzuki", 7)

    print("Stack after pushes:")
    display(stack)

    # Pop inventory action (delete top)
    last_action = pop(stack)
    print(f"\nPopped: {last_action}")
    print("Stack after pop:")
    display(stack)

    # Pop one more
    last_action = pop(stack)
    print(f"\nPopped: {last_action}")
    print("Stack after pop:")
    display(stack)


if __name__ == "__main__":
    main()

