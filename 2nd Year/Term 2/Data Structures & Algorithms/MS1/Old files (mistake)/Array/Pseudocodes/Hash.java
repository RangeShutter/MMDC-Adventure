package Array.Pseudocodes;

/**
 * Hash in Array - Simple hash table using array (MotorPH inventory)
 */
public class Hash {

    static final int SIZE = 10;

    record InventoryItem(String brand, String model, int quantity) {}

    record Entry(String key, InventoryItem value) {}

    /** Map key to array index (0 to SIZE-1). */
    static int hashFunction(String key) {
        return key.length() % SIZE;
    }

    /** Create empty hash table (array of SIZE slots). */
    static Entry[] createTable() {
        return new Entry[SIZE];
    }

    /** Insert key-value into hash table (linear probing for collisions). */
    static void put(Entry[] table, String key, InventoryItem value) {
        int index = hashFunction(key);
        int start = index;
        do {
            if (table[index] == null || table[index].key().equals(key)) {
                table[index] = new Entry(key, value);
                return;
            }
            index = (index + 1) % SIZE;
        } while (index != start);
        throw new RuntimeException("Hash table full");
    }

    /** Look up value by key; returns null if not found. */
    static InventoryItem get(Entry[] table, String key) {
        int index = hashFunction(key);
        int start = index;
        do {
            if (table[index] == null) break;
            if (table[index].key().equals(key)) {
                return table[index].value();
            }
            index = (index + 1) % SIZE;
        } while (index != start);
        return null;
    }

    /** Display all non-empty slots in the hash table. */
    static void display(Entry[] table) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.println("  [" + i + "] " + table[i].key() + " -> " + table[i].value());
            }
        }
    }

    public static void main(String[] args) {
        // Simple hash table using array
        Entry[] hashTable = createTable();

        // Insert inventory by brand (key)
        put(hashTable, "Honda", new InventoryItem("Honda", "Click 125", 5));
        put(hashTable, "Yamaha", new InventoryItem("Yamaha", "NMAX 155", 3));
        put(hashTable, "Suzuki", new InventoryItem("Suzuki", "Burgman Street", 7));

        System.out.println("Hash table (array) after inserts:");
        display(hashTable);

        // Look up by key
        System.out.println("\nGet 'Honda': " + get(hashTable, "Honda"));
        System.out.println("Get 'Yamaha': " + get(hashTable, "Yamaha"));
        System.out.println("Get 'Kawasaki': " + get(hashTable, "Kawasaki"));
    }
}
