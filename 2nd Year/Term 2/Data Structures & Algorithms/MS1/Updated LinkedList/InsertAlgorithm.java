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