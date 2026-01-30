# Stack for temporary inventory actions (LIFO - e.g. undo last action)

class InventoryActionStack:
    def __init__(self):
        self.stack = []

    # Push: add a new inventory action to the top of the stack
    def push(self, action, brand, model, quantity):
        self.stack.append({
            "action": action,
            "brand": brand,
            "model": model,
            "quantity": quantity
        })

    # Pop: remove and return the last action (undo)
    def pop(self):
        if self.is_empty():
            return None
        return self.stack.pop()

    # Peek: view the top action without removing it
    def peek(self):
        if self.is_empty():
            return None
        return self.stack[-1]

    def is_empty(self):
        return len(self.stack) == 0

    def display(self):
        if self.is_empty():
            print("  (stack empty)")
            return
        for i, item in enumerate(reversed(self.stack)):
            print(f"  {i + 1}. [{item['action']}] {item['brand']} {item['model']}: {item['quantity']} units")


def main():
    # Stack for inventory actions (MotorPH)
    inventory_stack = InventoryActionStack()

    # Push new stock actions
    print("Pushing inventory actions onto stack...")
    inventory_stack.push("add", "Yamaha", "NMAX 155", 5)
    inventory_stack.push("add", "Honda", "Click 125", 10)
    inventory_stack.push("add", "Suzuki", "Burgman Street", 7)
    print("Stack (top to bottom):")
    inventory_stack.display()

    # Peek at top action
    print("\nPeek at top action:")
    top = inventory_stack.peek()
    print(f"  {top}")

    # Pop last action (undo)
    print("\nPop last action (undo)...")
    last_action = inventory_stack.pop()
    print(f"  Undid: {last_action}")
    print("Stack after pop:")
    inventory_stack.display()

    # Pop one more
    print("\nPop another action...")
    last_action = inventory_stack.pop()
    print(f"  Undid: {last_action}")
    print("Stack after pop:")
    inventory_stack.display()


if __name__ == "__main__":
    main()
