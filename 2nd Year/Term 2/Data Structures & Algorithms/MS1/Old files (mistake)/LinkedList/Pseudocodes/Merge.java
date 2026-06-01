package LinkedList.Pseudocodes;

/**
 * Merge in Linked List - Merge two linked lists (MotorPH)
 */
public class Merge {

    static class Node {
        InventoryItem data;
        Node next;

        Node(String brand, String model, int quantity) {
            this.data = new InventoryItem(brand, model, quantity);
            this.next = null;
        }
    }

    record InventoryItem(String brand, String model, int quantity) {}

    /** Merge list2 onto the end of list1. Returns head of merged list (list1). */
    static Node mergeLists(Node head1, Node head2) {
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }

        Node list1Tail = head1;
        while (list1Tail.next != null) {
            list1Tail = list1Tail.next;
        }
        list1Tail.next = head2;
        return head1;
    }

    /** Display linked list from head. */
    static void display(Node head) {
        if (head == null) {
            System.out.println("  (empty)");
            return;
        }
        Node current = head;
        while (current != null) {
            InventoryItem d = current.data;
            System.out.println("  - " + d.brand() + " " + d.model() + ": " + d.quantity() + " units");
            current = current.next;
        }
    }

    public static void main(String[] args) {
        // List 1: Honda -> Yamaha
        Node list1 = new Node("Honda", "Click 125", 10);
        list1.next = new Node("Yamaha", "NMAX 155", 5);

        // List 2: Suzuki -> Kawasaki
        Node list2 = new Node("Suzuki", "Burgman Street", 7);
        list2.next = new Node("Kawasaki", "Ninja 125", 4);

        System.out.println("List 1:");
        display(list1);
        System.out.println("\nList 2:");
        display(list2);

        // Merge list2 onto end of list1
        Node merged = mergeLists(list1, list2);
        System.out.println("\nMerged list (list1 + list2):");
        display(merged);
    }
}
