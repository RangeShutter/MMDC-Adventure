package LinkedList.Pseudocodes;

/**
 * Search in Linked List - Linear search by brand (MotorPH)
 */
public class Search {

    static class Node {
        InventoryItem data;
        Node next;

        Node(String brand, String model, int quantity) {
            this.data = new InventoryItem(brand, model, quantity);
            this.next = null;
        }
    }

    record InventoryItem(String brand, String model, int quantity) {}

    /** Search linked list by brand. Returns node data if found, else null. */
    static InventoryItem search(Node head, String targetBrand) {
        Node current = head;
        while (current != null) {
            if (current.data.brand().equals(targetBrand)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
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
        // Build linked list: Honda -> Yamaha -> Suzuki
        Node head = new Node("Honda", "Click 125", 10);
        head.next = new Node("Yamaha", "NMAX 155", 5);
        head.next.next = new Node("Suzuki", "Burgman Street", 7);

        System.out.println("Linked list:");
        display(head);

        // Search by brand
        System.out.println("\nSearch for 'Yamaha':");
        InventoryItem result = search(head, "Yamaha");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nSearch for 'Honda':");
        result = search(head, "Honda");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nSearch for 'Kawasaki':");
        result = search(head, "Kawasaki");
        System.out.println("  " + (result != null ? result : "Not found"));
    }
}
