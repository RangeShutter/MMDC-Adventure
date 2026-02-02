package LinkedList.Samples;
/**
 * Add, Delete, Search Inventory using Linked List.
 */
public class LinkedListSample {

    record InventoryItem(String brand, String model, int quantity) {}

    static class Node {
        InventoryItem data;
        Node next;

        Node(String brand, String model, int quantity) {
            this.data = new InventoryItem(brand, model, quantity);
            this.next = null;
        }
    }

    static class InventoryLinkedList {
        Node head;

        void addStock(String brand, String model, int quantity) {
            Node newNode = new Node(brand, model, quantity);
            newNode.next = head;
            head = newNode;
        }

        Object searchByBrand(String brand) {
            Node current = head;
            while (current != null) {
                if (current.data.brand().equals(brand)) {
                    return current.data;
                }
                current = current.next;
            }
            return "Item not found";
        }

        String deleteStock(String brand) {
            Node current = head;
            Node previous = null;
            while (current != null) {
                if (current.data.brand().equals(brand)) {
                    if (previous != null) {
                        previous.next = current.next;
                    } else {
                        head = current.next;
                    }
                    return "Item deleted";
                }
                previous = current;
                current = current.next;
            }
            return "Item not found";
        }

        void display() {
            Node current = head;
            if (current == null) {
                System.out.println("  (empty)");
                return;
            }
            while (current != null) {
                InventoryItem d = current.data;
                System.out.println("  - " + d.brand() + " " + d.model() + ": " + d.quantity() + " units");
                current = current.next;
            }
        }
    }

    public static void main(String[] args) {
        InventoryLinkedList inventory = new InventoryLinkedList();

        System.out.println("Adding stock...");
        inventory.addStock("Honda", "Click 125", 10);
        inventory.addStock("Yamaha", "NMAX 155", 5);
        inventory.addStock("Suzuki", "Burgman Street", 7);
        System.out.println("Inventory:");
        inventory.display();

        System.out.println("\nSearch for 'Yamaha':");
        System.out.println("  " + inventory.searchByBrand("Yamaha"));
        System.out.println("\nSearch for 'Kawasaki':");
        System.out.println("  " + inventory.searchByBrand("Kawasaki"));

        System.out.println("\nDeleting 'Yamaha'...");
        System.out.println("  " + inventory.deleteStock("Yamaha"));
        System.out.println("Inventory after delete:");
        inventory.display();
    }
}
