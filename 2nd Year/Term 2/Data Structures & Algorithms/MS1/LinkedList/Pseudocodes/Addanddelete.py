# Add and Delete in Linked List - Inventory (MotorPH)


class Node:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.next = None


def add_at_head(head, new_node):
    """Add node at beginning."""
    new_node.next = head
    return new_node


def delete_by_brand(head, target_brand):
    """Delete first node with matching brand. Returns (new_head, deleted)."""
    if head is None:
        return None, False
    if head.data["brand"] == target_brand:
        return head.next, True
    current = head
    previous = None
    while current:
        if current.data["brand"] == target_brand:
            previous.next = current.next
            return head, True
        previous = current
        current = current.next
    return head, False


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

    # Add nodes at beginning
    head = add_at_head(head, Node("Suzuki", "Burgman Street", 7))
    head = add_at_head(head, Node("Yamaha", "NMAX 155", 5))
    head = add_at_head(head, Node("Honda", "Click 125", 10))

    print("Linked list after adds (at head):")
    display(head)

    # Delete node by brand
    head, deleted = delete_by_brand(head, "Yamaha")
    print(f"\nDelete 'Yamaha': {'deleted' if deleted else 'not found'}")
    print("Linked list after delete:")
    display(head)

    # Delete another
    head, deleted = delete_by_brand(head, "Kawasaki")
    print(f"\nDelete 'Kawasaki': {'deleted' if deleted else 'not found'}")
    print("Linked list after delete:")
    display(head)


if __name__ == "__main__":
    main()
