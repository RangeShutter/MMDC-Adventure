# Search in Linked List - Linear search by brand (MotorPH)


class Node:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.next = None


def search(head, target_brand):
    """Search linked list by brand. Returns node data if found, else None."""
    current = head
    while current:
        if current.data["brand"] == target_brand:
            return current.data
        current = current.next
    return None


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

    # Search by brand
    print("\nSearch for 'Yamaha':")
    result = search(head, "Yamaha")
    print(f"  {result if result else 'Not found'}")

    print("\nSearch for 'Honda':")
    result = search(head, "Honda")
    print(f"  {result if result else 'Not found'}")

    print("\nSearch for 'Kawasaki':")
    result = search(head, "Kawasaki")
    print(f"  {result if result else 'Not found'}")


if __name__ == "__main__":
    main()
