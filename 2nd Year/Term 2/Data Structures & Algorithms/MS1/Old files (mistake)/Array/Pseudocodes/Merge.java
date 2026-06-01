package Array.Pseudocodes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Merge in Arrays - Merge two inventory arrays (MotorPH)
 */
public class Merge {

    record InventoryItem(String brand, String model, int quantity) {}

    /** Merge two inventory arrays (concatenate). */
    static List<InventoryItem> mergeArrays(List<InventoryItem> arr1, List<InventoryItem> arr2) {
        List<InventoryItem> merged = new ArrayList<>(arr1);
        merged.addAll(arr2);
        return merged;
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
        // Two branch inventory arrays
        List<InventoryItem> branch1 = new ArrayList<>();
        branch1.add(new InventoryItem("Honda", "Click 125", 10));
        branch1.add(new InventoryItem("Honda", "PCX", 5));

        List<InventoryItem> branch2 = new ArrayList<>();
        branch2.add(new InventoryItem("Suzuki", "Burgman Street", 7));
        branch2.add(new InventoryItem("Yamaha", "NMAX 155", 5));

        System.out.println("Branch 1:");
        display(branch1);
        System.out.println("\nBranch 2:");
        display(branch2);

        // Merge two inventory arrays
        List<InventoryItem> mergedInventory = mergeArrays(branch1, branch2);
        System.out.println("\nMerged inventory (branch1 + branch2):");
        display(mergedInventory);

        // Optional: sort merged by brand for clearer view
        mergedInventory.sort(Comparator.comparing(InventoryItem::brand));
        System.out.println("\nMerged inventory (sorted by brand):");
        display(mergedInventory);
    }
}
