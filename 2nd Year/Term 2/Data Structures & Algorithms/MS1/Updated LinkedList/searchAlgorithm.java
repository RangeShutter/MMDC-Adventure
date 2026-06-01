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