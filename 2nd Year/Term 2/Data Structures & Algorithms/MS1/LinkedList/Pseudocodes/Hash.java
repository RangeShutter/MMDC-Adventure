package LinkedList.Pseudocodes;

/**
 * Hash in Linked List - Hash table with chaining (each bucket is a linked list) (MotorPH)
 */
public class Hash {

    static final int SIZE = 10;

    static class Node {
        String key;
        InventoryItem data;
        Node next;

        Node(String key, String brand, String model, int quantity) {
            this.key = key;
            this.data = new InventoryItem(brand, model, quantity);
            this.next = null;
        }
    }

    record InventoryItem(String brand, String model, int quantity) {}

    /** Map key to bucket index (0 to SIZE-1). */
    static int hashFunction(String key) {
        return key.length() % SIZE;
    }

    /** Create empty hash table (array of SIZE bucket heads). */
    static Node[] createTable() {
        return new Node[SIZE];
    }

    /** Insert key-value: add new node at head of chain at bucket. */
    static void put(Node[] table, String key, String brand, String model, int quantity) {
        int index = hashFunction(key);
        Node newNode = new Node(key, brand, model, quantity);
        newNode.next = table[index];
        table[index] = newNode;
    }

    /** Look up value by key; returns data or null if not found. */
    static InventoryItem get(Node[] table, String key) {
        int index = hashFunction(key);
        Node current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /** Display all buckets and their chains. */
    static void display(Node[] table) {
        for (int i = 0; i < SIZE; i++) {
            if (table[i] == null) continue;
            Node current = table[i];
            while (current != null) {
                InventoryItem d = current.data;
                System.out.println("  [" + i + "] " + current.key + " -> " + d.brand() + " " + d.model() + ": " + d.quantity() + " units");
                current = current.next;
            }
        }
    }

    public static void main(String[] args) {
        // Hash table: array of linked list heads
        Node[] hashTable = createTable();

        // Insert (chaining at each bucket)
        put(hashTable, "Honda", "Honda", "Click 125", 5);
        put(hashTable, "Yamaha", "Yamaha", "NMAX 155", 3);
        put(hashTable, "Suzuki", "Suzuki", "Burgman Street", 7);
        put(hashTable, "Kawasaki", "Kawasaki", "Ninja 125", 4);

        System.out.println("Hash table (array of linked lists) after inserts:");
        display(hashTable);

        // Look up by key
        System.out.println("\nGet 'Honda': " + get(hashTable, "Honda"));
        System.out.println("Get 'Yamaha': " + get(hashTable, "Yamaha"));
        System.out.println("Get 'Ducati': " + get(hashTable, "Ducati"));
    }
}
