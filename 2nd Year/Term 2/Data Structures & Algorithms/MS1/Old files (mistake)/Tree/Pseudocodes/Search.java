package Tree.Pseudocodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Search in Trees - BST search by brand (MotorPH)
 */
public class Search {

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

    /** Insert item into BST. Returns (new) root. */
    static TreeNode insert(TreeNode node, InventoryItem item) {
        if (node == null) {
            return new TreeNode(item.brand(), item.model(), item.quantity());
        }
        if (item.brand().compareTo(node.data.brand()) < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }
        return node;
    }

    /** Add stock: insert into BST by brand. */
    static TreeNode addStock(TreeNode root, String brand, String model, int quantity) {
        return insert(root, new InventoryItem(brand, model, quantity));
    }

    /** Search BST by brand. Returns node data if found, else null. */
    static InventoryItem search(TreeNode node, String brand) {
        if (node == null) {
            return null;
        }
        if (node.data.brand().equals(brand)) {
            return node.data;
        }
        if (brand.compareTo(node.data.brand()) < 0) {
            return search(node.left, brand);
        }
        return search(node.right, brand);
    }

    /** Inorder traversal: left -> root -> right (sorted by brand). */
    static void inorder(TreeNode node, List<InventoryItem> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }

    /** Display tree (inorder = sorted by brand). */
    static void display(TreeNode root) {
        List<InventoryItem> result = new ArrayList<>();
        inorder(root, result);
        if (result.isEmpty()) {
            System.out.println("  (tree empty)");
            return;
        }
        for (InventoryItem item : result) {
            System.out.println("  - " + item.brand() + " " + item.model() + ": " + item.quantity() + " units");
        }
    }

    public static void main(String[] args) {
        TreeNode root = null;

        // Build BST
        root = addStock(root, "Yamaha", "NMAX 155", 5);
        root = addStock(root, "Honda", "Click 125", 10);
        root = addStock(root, "Suzuki", "Burgman Street", 7);
        root = addStock(root, "Kawasaki", "Ninja 125", 4);

        System.out.println("Tree (inorder by brand):");
        display(root);

        // Search by brand
        System.out.println("\nSearch for 'Honda':");
        InventoryItem result = search(root, "Honda");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nSearch for 'Kawasaki':");
        result = search(root, "Kawasaki");
        System.out.println("  " + (result != null ? result : "Not found"));

        System.out.println("\nSearch for 'Ducati':");
        result = search(root, "Ducati");
        System.out.println("  " + (result != null ? result : "Not found"));
    }
}
