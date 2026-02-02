package LinkedList.Recursion;

/**
 * Recursion in Linked List - Recursive search by brand (MotorPH)
 */
public class LinkedListRecursion {

    static class Node {
        InventoryItem data;
        Node next;

        Node(String brand, String model, int quantity) {
            this.data = new InventoryItem(brand, model, quantity);
            this.next = null;
        }
    }

    record InventoryItem(String brand, String model, int quantity) {}

    /** Search linked list by brand using recursion. */
    static InventoryItem recursiveSearch(Node node, String brand) {
        if (node == null) {
            return null;
        }
        if (node.data.brand().equals(brand)) {
            return node.data;
        }
        return recursiveSearch(node.next, brand);
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

        // Recursive search by brand
        System.out.println("\nRecursive search for 'Yamaha':");
        InventoryItem result = recursiveSearch(head, "Yamaha");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nRecursive search for 'Honda':");
        result = recursiveSearch(head, "Honda");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nRecursive search for 'Kawasaki':");
        result = recursiveSearch(head, "Kawasaki");
        System.out.println("  " + (result != null ? result : "Not found"));
    }
}
