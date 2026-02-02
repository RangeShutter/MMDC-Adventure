package Array.Recursions;

import java.util.ArrayList;
import java.util.List;

/**
 * Recursion in Array - Recursive search in inventory (MotorPH)
 */
public class ArrayRecursion {

    record InventoryItem(String brand, String model, int quantity) {}

    /** Search inventory array by brand using recursion. */
    static InventoryItem recursiveSearch(List<InventoryItem> inventory, String brand, int index) {
        if (index >= inventory.size()) {
            return null;
        }
        if (inventory.get(index).brand().equals(brand)) {
            return inventory.get(index);
        }
        return recursiveSearch(inventory, brand, index + 1);
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

        // Recursive search by brand
        System.out.println("\nRecursive search for 'Yamaha':");
        InventoryItem result = recursiveSearch(inventory, "Yamaha", 0);
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nRecursive search for 'Honda':");
        result = recursiveSearch(inventory, "Honda", 0);
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nRecursive search for 'Kawasaki':");
        result = recursiveSearch(inventory, "Kawasaki", 0);
        System.out.println("  " + (result != null ? result : "Not found"));
    }
}
