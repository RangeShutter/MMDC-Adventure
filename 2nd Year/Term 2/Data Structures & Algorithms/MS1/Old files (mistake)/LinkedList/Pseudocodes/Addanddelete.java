package LinkedList.Pseudocodes;

/**
 * Add and Delete in Linked List - Inventory (MotorPH)
 */
public class Addanddelete {

    static class Node {
        InventoryItem data;
        Node next;

        Node(String brand, String model, int quantity) {
            this.data = new InventoryItem(brand, model, quantity);
            this.next = null;
        }
    }

    record InventoryItem(String brand, String model, int quantity) {}

    /** Add node at beginning. Returns new head. */
    static Node addAtHead(Node head, Node newNode) {
        newNode.next = head;
        return newNode;
    }

    record DeleteResult(Node head, boolean deleted) {}

    /** Delete first node with matching brand. Returns new head and whether a node was deleted. */
    static DeleteResult deleteByBrand(Node head, String targetBrand) {
        if (head == null) {
            return new DeleteResult(null, false);
        }
        if (head.data.brand().equals(targetBrand)) {
            return new DeleteResult(head.next, true);
        }
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current.data.brand().equals(targetBrand)) {
                previous.next = current.next;
                return new DeleteResult(head, true);
            }
            previous = current;
            current = current.next;
        }
        return new DeleteResult(head, false);
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

        // Add nodes at beginning
        head = addAtHead(head, new Node("Suzuki", "Burgman Street", 7));
        head = addAtHead(head, new Node("Yamaha", "NMAX 155", 5));
        head = addAtHead(head, new Node("Honda", "Click 125", 10));

        System.out.println("Linked list after adds (at head):");
        display(head);

        // Delete node by brand
        DeleteResult r = deleteByBrand(head, "Yamaha");
        head = r.head();
        System.out.println("\nDelete 'Yamaha': " + (r.deleted() ? "deleted" : "not found"));
        System.out.println("Linked list after delete:");
        display(head);

        // Delete another (not present)
        r = deleteByBrand(head, "Kawasaki");
        head = r.head();
        System.out.println("\nDelete 'Kawasaki': " + (r.deleted() ? "deleted" : "not found"));
        System.out.println("Linked list after delete:");
        display(head);
    }
}
