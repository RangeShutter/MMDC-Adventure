package Array.Pseudocodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Add and Delete in Array - Inventory (MotorPH)
 */
public class Addanddelete {

    record InventoryItem(String brand, String model, int quantity) {}

    /** Add inventory item (append to array). */
    static void addItem(List<InventoryItem> inventory, String brand, String model, int quantity) {
        inventory.add(new InventoryItem(brand, model, quantity));
    }

    /** Delete all inventory items with the given brand. */
    static void deleteByBrand(List<InventoryItem> inventory, String brand) {
        inventory.removeIf(item -> item.brand().equals(brand));
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

        // Add inventory items
        addItem(inventory, "Honda", "Click 125", 10);
        addItem(inventory, "Yamaha", "Mio", 8);
        addItem(inventory, "Suzuki", "Burgman Street", 7);
        addItem(inventory, "Yamaha", "NMAX 155", 5);

        System.out.println("After adds:");
        display(inventory);

        // Delete inventory item by brand (removes all items with that brand)
        deleteByBrand(inventory, "Yamaha");
        System.out.println("\nAfter delete by brand 'Yamaha':");
        display(inventory);

        // Add one more
        addItem(inventory, "Kawasaki", "Ninja 125", 4);
        System.out.println("\nAfter add Kawasaki:");
        display(inventory);
    }
}
