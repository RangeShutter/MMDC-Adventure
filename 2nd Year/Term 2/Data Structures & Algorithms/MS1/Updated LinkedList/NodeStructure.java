class Node {
    String brand;
    String model;
    int quantity;
    Node next;

    Node(String brand, String model, int quantity) {
        this.brand = brand;
        this.model = model;
        this.quantity = quantity;
        this.next = null;
    }
}