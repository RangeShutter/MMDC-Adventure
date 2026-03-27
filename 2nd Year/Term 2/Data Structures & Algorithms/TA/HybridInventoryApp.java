import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hybrid inventory system:
 * - Singly linked list stores all records (next pointer).
 * - Hash table provides brand -> bucket lookup (bucketNext pointer).
 * - Merge sort orders the linked list by brand for display (uses next pointers).
 */
public class HybridInventoryApp {
    private static final int DEFAULT_TABLE_SIZE = 17;
    private static final String DEFAULT_CSV = "inventory_sample.csv";

    private final SinglyLinkedList list;
    private final HashTable hashTable;

    public HybridInventoryApp(int tableSize) {
        this.list = new SinglyLinkedList();
        this.hashTable = new HashTable(tableSize);
    }

    private void insert(String brand, String model, int quantity) {
        InventoryNode node = new InventoryNode(brand, model, quantity);
        list.append(node);
        hashTable.put(node);
    }

    private InventoryNode deleteByBrand(String brand) {
        InventoryNode removed = list.removeFirstByBrand(brand);
        if (removed == null) return null;
        hashTable.removeNode(removed);
        return removed;
    }

    private InventoryNode searchByBrand(String brand) {
        return hashTable.get(brand);
    }

    private void loadFromCsv(String csvPath) throws IOException {
        // Reset current structures for a clean re-load.
        list.clear();
        hashTable.clear();

        int loaded = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header row if it exists.
                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().startsWith("brand")) continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String brand = parts[0].trim();
                String model = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());

                insert(brand, model, quantity);
                loaded++;
            }
        }

        System.out.println("Loaded " + loaded + " records from CSV.");
    }

    private void displaySortedByBrand() {
        list.sortByBrandMergeSort();
        list.printAll();
    }

    private void runCli() {
        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;

            System.out.println("MotorPH Hybrid Inventory System (LinkedList + HashTable + MergeSort)");
            System.out.println("Default CSV: " + DEFAULT_CSV);

            while (running) {
                System.out.println();
                System.out.println("1. Load CSV");
                System.out.println("2. Add item");
                System.out.println("3. Delete by brand");
                System.out.println("4. Search by brand");
                System.out.println("5. Display sorted by brand");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                String choiceStr = sc.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(choiceStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option. Enter a number from 1 to 6.");
                    continue;
                }

                switch (choice) {
                    case 1: {
                        System.out.print("CSV path (press Enter for default): ");
                        String path = sc.nextLine().trim();
                        if (path.isEmpty()) path = DEFAULT_CSV;

                        try {
                            loadFromCsv(path);
                        } catch (IOException e) {
                            System.out.println("Failed to load CSV: " + e.getMessage());
                        }
                        break;
                    }

                    case 2: {
                        System.out.print("Brand: ");
                        String brand = sc.nextLine().trim();
                        System.out.print("Model: ");
                        String model = sc.nextLine().trim();
                        System.out.print("Quantity: ");
                        int quantity;
                        try {
                            quantity = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Quantity must be an integer.");
                            break;
                        }

                        insert(brand, model, quantity);
                        System.out.println("Added: " + brand + " (" + model + "), qty=" + quantity);
                        break;
                    }

                    case 3: {
                        System.out.print("Brand to delete: ");
                        String brand = sc.nextLine().trim();

                        InventoryNode removed = deleteByBrand(brand);
                        if (removed == null) {
                            System.out.println("No record found for brand: " + brand);
                        } else {
                            System.out.println("Deleted: " + removed.brand + " (" + removed.model + "), qty=" + removed.quantity);
                        }
                        break;
                    }

                    case 4: {
                        System.out.print("Brand to search: ");
                        String brand = sc.nextLine().trim();

                        InventoryNode found = searchByBrand(brand);
                        if (found == null) {
                            System.out.println("No record found for brand: " + brand);
                        } else {
                            System.out.println("Found: " + found.brand + " (" + found.model + "), qty=" + found.quantity);
                        }
                        break;
                    }

                    case 5: {
                        if (list.size() == 0) {
                            System.out.println("Inventory is empty. Load a CSV or add items first.");
                        } else {
                            System.out.println("Sorted inventory (by brand):");
                            displaySortedByBrand();
                        }
                        break;
                    }

                    case 6:
                        running = false;
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option. Enter a number from 1 to 6.");
                }
            }
        }
    }

    public static void main(String[] args) {
        HybridInventoryApp app = new HybridInventoryApp(DEFAULT_TABLE_SIZE);
        app.runCli();
    }

    // ----------------------------
    // Data structures
    // ----------------------------

    static class InventoryNode {
        final String brand;
        final String model;
        final int quantity;

        // Global linked list pointer (used by sorting and full traversal)
        InventoryNode next;

        // Hash bucket chaining pointer
        InventoryNode bucketNext;

        InventoryNode(String brand, String model, int quantity) {
            this.brand = brand;
            this.model = model;
            this.quantity = quantity;
        }
    }

    static class SinglyLinkedList {
        private InventoryNode head;
        private int size;

        int size() {
            return size;
        }

        void clear() {
            head = null;
            size = 0;
        }

        void append(InventoryNode node) {
            node.next = null; // Ensure we don't keep old links
            if (head == null) {
                head = node;
            } else {
                InventoryNode current = head;
                while (current.next != null) current = current.next;
                current.next = node;
            }
            size++;
        }

        InventoryNode removeFirstByBrand(String brand) {
            if (head == null) return null;

            if (head.brand.equalsIgnoreCase(brand)) {
                InventoryNode removed = head;
                head = head.next;
                removed.next = null;
                size--;
                return removed;
            }

            InventoryNode current = head;
            while (current.next != null && !current.next.brand.equalsIgnoreCase(brand)) {
                current = current.next;
            }

            if (current.next == null) return null;

            InventoryNode removed = current.next;
            current.next = removed.next;
            removed.next = null;
            size--;
            return removed;
        }

        void printAll() {
            InventoryNode current = head;
            System.out.println("Brand\tModel\t\tQuantity");
            while (current != null) {
                System.out.println(current.brand + "\t" + current.model + "\t\t" + current.quantity);
                current = current.next;
            }
        }

        void sortByBrandMergeSort() {
            head = mergeSort(head);
        }

        private InventoryNode mergeSort(InventoryNode start) {
            if (start == null || start.next == null) return start;

            InventoryNode middle = getMiddle(start);
            InventoryNode secondHalf = middle.next;
            middle.next = null; // Split list into two halves

            InventoryNode left = mergeSort(start);
            InventoryNode right = mergeSort(secondHalf);

            return merge(left, right);
        }

        private InventoryNode getMiddle(InventoryNode start) {
            InventoryNode slow = start;
            InventoryNode fast = start.next;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }

        private InventoryNode merge(InventoryNode left, InventoryNode right) {
            if (left == null) return right;
            if (right == null) return left;

            if (left.brand.compareToIgnoreCase(right.brand) <= 0) {
                InventoryNode result = left;
                result.next = merge(left.next, right);
                return result;
            } else {
                InventoryNode result = right;
                result.next = merge(left, right.next);
                return result;
            }
        }
    }

    static class HashTable {
        private final InventoryNode[] buckets;

        HashTable(int tableSize) {
            if (tableSize <= 0) tableSize = 17;
            this.buckets = new InventoryNode[tableSize];
        }

        void clear() {
            for (int i = 0; i < buckets.length; i++) buckets[i] = null;
        }

        private int indexForBrand(String brand) {
            int h = brand == null ? 0 : brand.hashCode();
            if (h == Integer.MIN_VALUE) h = 0; // Avoid overflow on abs(Integer.MIN_VALUE)
            h = Math.abs(h);
            return h % buckets.length;
        }

        void put(InventoryNode node) {
            int idx = indexForBrand(node.brand);
            node.bucketNext = buckets[idx]; // Chaining via bucketNext
            buckets[idx] = node;
        }

        InventoryNode get(String brand) {
            int idx = indexForBrand(brand);
            InventoryNode current = buckets[idx];
            while (current != null) {
                if (current.brand.equalsIgnoreCase(brand)) return current;
                current = current.bucketNext;
            }
            return null;
        }

        /**
         * Removes the exact node instance from its bucket chain.
         * This avoids ambiguity if duplicates exist for the same brand.
         */
        boolean removeNode(InventoryNode node) {
            int idx = indexForBrand(node.brand);
            InventoryNode current = buckets[idx];
            InventoryNode prev = null;

            while (current != null) {
                if (current == node) {
                    if (prev == null) buckets[idx] = current.bucketNext;
                    else prev.bucketNext = current.bucketNext;
                    node.bucketNext = null;
                    return true;
                }
                prev = current;
                current = current.bucketNext;
            }

            return false;
        }
    }
}

