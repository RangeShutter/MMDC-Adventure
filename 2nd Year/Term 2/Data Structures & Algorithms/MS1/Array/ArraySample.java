package Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Add, Search, Sort Inventory using Array (ArrayList).
 */
public class ArraySample {

    record InventoryItem(String brand, String model, int quantity) {}

    static InventoryItem searchByBrand(List<InventoryItem> inventory, String brand) {
        for (InventoryItem item : inventory) {
            if (item.brand().equals(brand)) {
                return item;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // Simple inventory array for MotorPH
        List<InventoryItem> inventory = new ArrayList<>();

        // Add new stock
        inventory.add(new InventoryItem("Honda", "Click 125", 10));
        inventory.add(new InventoryItem("Yamaha", "NMAX 155", 5));

        // Show initial inventory
        System.out.println("Inventory:");
        for (InventoryItem item : inventory) {
            System.out.println("  - " + item.brand() + " " + item.model() + ": " + item.quantity() + " units");
        }

        // Search inventory by brand
        InventoryItem result = searchByBrand(inventory, "Honda");
        if (result != null) {
            System.out.println("\nFound: " + result);
        } else {
            System.out.println("\nItem not found");
        }

        // Sort inventory by brand
        inventory.sort(Comparator.comparing(InventoryItem::brand));
        System.out.println("\nSorted by brand:");
        for (InventoryItem item : inventory) {
            System.out.println("  - " + item.brand() + " " + item.model() + ": " + item.quantity() + " units");
        }
    }
}
