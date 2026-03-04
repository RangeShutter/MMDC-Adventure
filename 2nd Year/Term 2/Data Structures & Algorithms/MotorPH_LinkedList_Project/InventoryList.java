
public class InventoryList {

    Node head;

    public InventoryList() {
        head = null;
    }

    // Insert at End
    public void insert(String brand, String model, int quantity) {
        Node newNode = new Node(brand, model, quantity);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    // Delete by Brand
    public void delete(String brand) {
        if (head == null) return;

        if (head.brand.equals(brand)) {
            head = head.next;
            return;
        }

        Node current = head;

        while (current.next != null && !current.next.brand.equals(brand)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
        }
    }

    // Search by Brand
    public Node search(String brand) {
        Node current = head;

        while (current != null) {
            if (current.brand.equals(brand)) {
                return current;
            }
            current = current.next;
        }

        return null;
    }

    // Display Inventory
    public void display() {
        Node current = head;

        while (current != null) {
            System.out.println("Brand: " + current.brand +
                               ", Model: " + current.model +
                               ", Quantity: " + current.quantity);
            current = current.next;
        }
    }
}
