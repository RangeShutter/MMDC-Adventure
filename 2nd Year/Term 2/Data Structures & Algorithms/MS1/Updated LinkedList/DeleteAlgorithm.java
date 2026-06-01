public void delete(String brand) {
    if (head == null) return;

    if (head.brand.equals(brand)) {
        head = head.next;
        return;
    }

    Node current = head;
    while (current.next != null && 
           !current.next.brand.equals(brand)) {
        current = current.next;
    }

    if (current.next != null) {
        current.next = current.next.next;
    }
}