package Tree.Recursion;

/**
 * Recursion in Trees - Recursive inorder traversal (MotorPH)
 */
public class TreesRecursion {

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

    /** Recursive inorder traversal: left -> root -> right (sorted by brand). */
    static void inorderTraversal(TreeNode node) {
        if (node != null) {
            inorderTraversal(node.left);
            InventoryItem d = node.data;
            System.out.println("  - " + d.brand() + " " + d.model() + ": " + d.quantity() + " units");
            inorderTraversal(node.right);
        }
    }

    public static void main(String[] args) {
        TreeNode root = null;

        // Build BST
        root = addStock(root, "Yamaha", "NMAX 155", 5);
        root = addStock(root, "Honda", "Click 125", 10);
        root = addStock(root, "Suzuki", "Burgman Street", 7);
        root = addStock(root, "Kawasaki", "Ninja 125", 4);

        System.out.println("Tree (recursive inorder traversal):");
        inorderTraversal(root);
    }
}
