package Tree.Pseudocodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Merge in Trees - Merge source BST into target BST (inorder: left, insert, right) (MotorPH)
 */
public class Merge {

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

    /** Merge source tree into target (inorder traverse source, insert each node into target). Returns updated target root. */
    static TreeNode mergeTrees(TreeNode source, TreeNode target) {
        if (source == null) {
            return target;
        }
        target = mergeTrees(source.left, target);
        target = insert(target, source.data);
        target = mergeTrees(source.right, target);
        return target;
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
        // Tree 1: Honda, Yamaha
        TreeNode tree1 = null;
        tree1 = addStock(tree1, "Yamaha", "NMAX 155", 5);
        tree1 = addStock(tree1, "Honda", "Click 125", 10);

        // Tree 2: Suzuki, Kawasaki
        TreeNode tree2 = null;
        tree2 = addStock(tree2, "Suzuki", "Burgman Street", 7);
        tree2 = addStock(tree2, "Kawasaki", "Ninja 125", 4);

        System.out.println("Tree 1:");
        display(tree1);
        System.out.println("\nTree 2:");
        display(tree2);

        // Merge tree1 into tree2 (tree2 becomes the combined tree)
        TreeNode merged = mergeTrees(tree1, tree2);
        System.out.println("\nMerged tree (tree1 merged into tree2):");
        display(merged);
    }
}
