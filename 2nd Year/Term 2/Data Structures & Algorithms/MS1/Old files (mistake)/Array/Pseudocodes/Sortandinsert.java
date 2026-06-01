package Array.Pseudocodes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sort and Insert in Array - Inventory (MotorPH)
 */
public class Sortandinsert {

    record InventoryItem(String brand, String model, int quantity) {}

    /** Insert new inventory item (append to array). */
    static void insertItem(List<InventoryItem> inventory, String brand, String model, int quantity) {
        inventory.add(new InventoryItem(brand, model, quantity));
    }

    /** Sort inventory by brand. */
    static void sortByBrand(List<InventoryItem> inventory) {
        inventory.sort(Comparator.comparing(InventoryItem::brand));
    }

    /** Display inventory list. */
    static void display(List<InventoryItem> inventory) {
        if (inventory.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }
        for (InventoryItem item : inventory) {
            System.out.println("  - " + item.brand() + " " + item.model() + ": " + item.quantity() + " units");
        }
    }

    public static void main(String[] args) {
        // Start with empty array (ArrayList)
        List<InventoryItem> inventory = new ArrayList<>();

        // Insert new inventory items
        insertItem(inventory, "Yamaha", "NMAX 155", 5);
        insertItem(inventory, "Honda", "PCX", 5);
        insertItem(inventory, "Suzuki", "Burgman Street", 7);

        System.out.println("After inserts (unsorted):");
        display(inventory);

        // Sort inventory by brand
        sortByBrand(inventory);
        System.out.println("\nAfter sort by brand:");
        display(inventory);

        // Insert one more, then sort again
        insertItem(inventory, "Kawasaki", "Ninja 125", 4);
        System.out.println("\nAfter one more insert (unsorted):");
        display(inventory);
        sortByBrand(inventory);
        System.out.println("After sort by brand:");
        display(inventory);
    }
}
