package Array.Pseudocodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Search in Array - Inventory (MotorPH)
 */
public class Search {

    record InventoryItem(String brand, String model, int quantity) {}

    /** Search inventory array by brand. Returns item if found, else null. */
    static InventoryItem searchInventory(List<InventoryItem> inventory, String brand) {
        for (InventoryItem item : inventory) {
            if (item.brand().equals(brand)) {
                return item;
            }
        }
        return null;
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
        // Inventory array (ArrayList)
        List<InventoryItem> inventory = new ArrayList<>();
        inventory.add(new InventoryItem("Honda", "Click 125", 10));
        inventory.add(new InventoryItem("Yamaha", "NMAX 155", 5));
        inventory.add(new InventoryItem("Suzuki", "Burgman Street", 7));

        System.out.println("Inventory:");
        display(inventory);

        // Search by brand
        System.out.println("\nSearch for 'Yamaha':");
        InventoryItem result = searchInventory(inventory, "Yamaha");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nSearch for 'Honda':");
        result = searchInventory(inventory, "Honda");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nSearch for 'Kawasaki':");
        result = searchInventory(inventory, "Kawasaki");
        System.out.println("  " + (result != null ? result : "Not found"));
    }
}
