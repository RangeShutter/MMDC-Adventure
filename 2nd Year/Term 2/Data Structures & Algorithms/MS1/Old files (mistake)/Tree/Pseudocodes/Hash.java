package Tree.Pseudocodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Hash in Trees - Hash table mapping brand (key) to tree node (MotorPH)
 */
public class Hash {

    static class TreeNode {
        InventoryItem data;
        TreeNode left;
        TreeNode right;

        TreeNode(String brand, String model, int quantity) {
            this.data = new InventoryItem(brand, model, quantity);
            this.left = null;
            this.right = null;
        }
    }

    record InventoryItem(String brand, String model, int quantity) {}

    /** Add brand -> node to hash table if brand not already present. */
    static void addToHashTree(Map<String, TreeNode> hashTable, String brand, TreeNode node) {
        if (!hashTable.containsKey(brand)) {
            hashTable.put(brand, node);
        }
    }

    /** Look up node by brand. Returns node or null. */
    static TreeNode getFromHashTree(Map<String, TreeNode> hashTable, String brand) {
        return hashTable.get(brand);
    }

    /** Display hash table (brand -> node data). */
    static void display(Map<String, TreeNode> hashTable) {
        if (hashTable.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }
        for (Map.Entry<String, TreeNode> e : hashTable.entrySet()) {
            InventoryItem d = e.getValue().data;
            System.out.println("  " + e.getKey() + " -> " + d.brand() + " " + d.model() + ": " + d.quantity() + " units");
        }
    }

    public static void main(String[] args) {
        // Hash table: brand (key) -> TreeNode
        Map<String, TreeNode> hashTable = new HashMap<>();

        // Create nodes and add to hash tree
        addToHashTree(hashTable, "Honda", new TreeNode("Honda", "Click 125", 10));
        addToHashTree(hashTable, "Yamaha", new TreeNode("Yamaha", "NMAX 155", 5));
        addToHashTree(hashTable, "Suzuki", new TreeNode("Suzuki", "Burgman Street", 7));

        System.out.println("Hash table (brand -> node):");
        display(hashTable);

        // Look up by brand
        System.out.println("\nGet 'Honda':");
        TreeNode node = getFromHashTree(hashTable, "Honda");
        System.out.println("  " + (node != null ? node.data : "Not found"));

        System.out.println("\nGet 'Kawasaki':");
        node = getFromHashTree(hashTable, "Kawasaki");
        System.out.println("  " + (node != null ? node.data : "Not found"));
    }
}
