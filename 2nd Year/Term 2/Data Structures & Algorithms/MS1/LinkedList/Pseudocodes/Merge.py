# Merge in Linked List - Merge two linked lists (MotorPH)


class Node:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.next = None


def merge_lists(head1, head2):
    """Merge list2 onto the end of list1. Returns head of merged list (list1)."""
    if not head1:
        return head2
    if not head2:
        return head1

    list1_tail = head1
    while list1_tail.next:
        list1_tail = list1_tail.next
    list1_tail.next = head2
    return head1


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
    # List 1: Honda -> Yamaha
    list1 = Node("Honda", "Click 125", 10)
    list1.next = Node("Yamaha", "NMAX 155", 5)

    # List 2: Suzuki -> Kawasaki
    list2 = Node("Suzuki", "Burgman Street", 7)
    list2.next = Node("Kawasaki", "Ninja 125", 4)

    print("List 1:")
    display(list1)
    print("\nList 2:")
    display(list2)

    # Merge list2 onto end of list1
    merged = merge_lists(list1, list2)
    print("\nMerged list (list1 + list2):")
    display(merged)


if __name__ == "__main__":
    main()
