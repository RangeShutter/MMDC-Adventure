# Recursion in Linked List - Recursive search by brand (MotorPH)


class Node:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.next = None


def recursive_search(node, brand):
    """Search linked list by brand using recursion."""
    if node is None:
        return None
    if node.data["brand"] == brand:
        return node.data
    return recursive_search(node.next, brand)


def display(head):
    """Display linked list from head."""
    if head is None:
        print("  (empty)")
        return
    current = head
    while current:
        d = current.data
        print(f"  - {d['brand']} {d['model']}: {d['quantity']} units")
        current = current.next


def main():
    # Build linked list: Honda -> Yamaha -> Suzuki
    head = Node("Honda", "Click 125", 10)
    head.next = Node("Yamaha", "NMAX 155", 5)
    head.next.next = Node("Suzuki", "Burgman Street", 7)

    print("Linked list:")
    display(head)

    # Recursive search by brand
    print("\nRecursive search for 'Yamaha':")
    result = recursive_search(head, "Yamaha")
    print(f"  {result if result else 'Not found'}")

    print("\nRecursive search for 'Honda':")
    result = recursive_search(head, "Honda")
    print(f"  {result if result else 'Not found'}")

    print("\nRecursive search for 'Kawasaki':")
    result = recursive_search(head, "Kawasaki")
    print(f"  {result if result else 'Not found'}")


if __name__ == "__main__":
    main()
