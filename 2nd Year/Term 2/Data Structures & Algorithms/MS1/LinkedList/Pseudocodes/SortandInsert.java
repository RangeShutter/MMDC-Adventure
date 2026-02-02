package LinkedList.Pseudocodes;

/**
 * Sort and Insert in Linked List - Insert in sorted order by brand (MotorPH)
 */
public class SortandInsert {

    static class Node {
        InventoryItem data;
        Node next;

        Node(String brand, String model, int quantity) {
            this.data = new InventoryItem(brand, model, quantity);
            this.next = null;
        }
    }

    record InventoryItem(String brand, String model, int quantity) {}

    /** Insert new_node into list so it stays sorted by brand. */
    static Node insertSorted(Node head, Node newNode) {
        if (head == null || newNode.data.brand().compareTo(head.data.brand()) < 0) {
            newNode.next = head;
            return newNode;
        }

        Node current = head;
        while (current.next != null && newNode.data.brand().compareTo(current.next.data.brand()) > 0) {
            current = current.next;
        }

        newNode.next = current.next;
        current.next = newNode;
        return head;
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
        Node head = null;

        // Insert nodes in random order; list stays sorted by brand
        head = insertSorted(head, new Node("Yamaha", "NMAX 155", 5));
        head = insertSorted(head, new Node("Honda", "Click 125", 10));
        head = insertSorted(head, new Node("Suzuki", "Burgman Street", 7));
        head = insertSorted(head, new Node("Kawasaki", "Ninja 125", 4));

        System.out.println("Linked list after sort-and-insert (by brand):");
        display(head);
    }
}
