package Tree.Pseudocodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Add and Delete in Trees - BST insert and delete by brand (MotorPH)
 */
public class AddandDelete {

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

    /** Find node with minimum key in subtree (leftmost). */
    static TreeNode findMin(TreeNode node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    /** Delete node with given brand from BST. Returns (new) root of subtree. */
    static TreeNode delete(TreeNode node, String brand) {
        if (node == null) {
            return node;
        }
        if (brand.compareTo(node.data.brand()) < 0) {
            node.left = delete(node.left, brand);
        } else if (brand.compareTo(node.data.brand()) > 0) {
            node.right = delete(node.right, brand);
        } else {
            // Found node to delete
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Two children: replace with inorder successor (min of right subtree)
            TreeNode minNode = findMin(node.right);
            node.data = minNode.data;
            node.right = delete(node.right, minNode.data.brand());
        }
        return node;
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

        // Add stock (insert)
        root = addStock(root, "Yamaha", "NMAX 155", 5);
        root = addStock(root, "Honda", "Click 125", 10);
        root = addStock(root, "Suzuki", "Burgman Street", 7);
        root = addStock(root, "Kawasaki", "Ninja 125", 4);

        System.out.println("Tree after adds:");
        display(root);

        // Delete by brand
        root = delete(root, "Yamaha");
        System.out.println("\nAfter delete 'Yamaha':");
        display(root);

        root = delete(root, "Kawasaki");
        System.out.println("\nAfter delete 'Kawasaki':");
        display(root);
    }
}
