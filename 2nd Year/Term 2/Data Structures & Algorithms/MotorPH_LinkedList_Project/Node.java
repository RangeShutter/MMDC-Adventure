
public class Node {
    String brand;
    String model;
    int quantity;
    Node next;

    public Node(String brand, String model, int quantity) {
        this.brand = brand;
        this.model = model;
        this.quantity = quantity;
        this.next = null;
    }
}
