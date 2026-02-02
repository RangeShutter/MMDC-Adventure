# Sort and Insert in Linked List - Insert in sorted order by brand (MotorPH)


class Node:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.next = None


def insert_sorted(head, new_node):
    """Insert new_node into list so it stays sorted by brand."""
    if head is None or new_node.data["brand"] < head.data["brand"]:
        new_node.next = head
        return new_node

    current = head
    while current.next and current.next.data["brand"] < new_node.data["brand"]:
        current = current.next

    new_node.next = current.next
    current.next = new_node
    return head


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
    head = None

    # Insert nodes in random order; list stays sorted by brand
    head = insert_sorted(head, Node("Yamaha", "NMAX 155", 5))
    head = insert_sorted(head, Node("Honda", "Click 125", 10))
    head = insert_sorted(head, Node("Suzuki", "Burgman Street", 7))
    head = insert_sorted(head, Node("Kawasaki", "Ninja 125", 4))

    print("Linked list after sort-and-insert (by brand):")
    display(head)


if __name__ == "__main__":
    main()
