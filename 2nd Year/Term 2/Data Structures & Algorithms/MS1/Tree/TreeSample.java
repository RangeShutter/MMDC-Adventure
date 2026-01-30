package Tree;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Binary Search Tree for Inventory (ordered by brand).
 */
public class TreeSample {

    record InventoryItem(String brand, String model, int quantity) {}

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

    static class InventoryTree {
        TreeNode root;

        void addStock(String brand, String model, int quantity) {
            root = insert(root, brand, model, quantity);
        }

        private TreeNode insert(TreeNode node, String brand, String model, int quantity) {
            if (node == null) {
                return new TreeNode(brand, model, quantity);
            }
            if (brand.compareTo(node.data.brand()) < 0) {
                node.left = insert(node.left, brand, model, quantity);
            } else {
                node.right = insert(node.right, brand, model, quantity);
            }
            return node;
        }

        InventoryItem searchByBrand(String brand) {
            return search(root, brand);
        }

        private InventoryItem search(TreeNode node, String brand) {
            if (node == null) {
                return null;
            }
            if (brand.equals(node.data.brand())) {
                return node.data;
            }
            if (brand.compareTo(node.data.brand()) < 0) {
                return search(node.left, brand);
            }
            return search(node.right, brand);
        }

        void display() {
            List<InventoryItem> result = new ArrayList<>();
            inorder(root, result);
            if (result.isEmpty()) {
                System.out.println("  (tree empty)");
                return;
            }
            for (InventoryItem item : result) {
                System.out.println("  - " + item.brand() + " " + item.model()
                        + ": " + item.quantity() + " units");
            }
        }

        private void inorder(TreeNode node, List<InventoryItem> result) {
            if (node == null) return;
            inorder(node.left, result);
            result.add(node.data);
            inorder(node.right, result);
        }
    }

    public static void main(String[] args) {
        InventoryTree inventoryTree = new InventoryTree();

        System.out.println("Inserting stock...");
        inventoryTree.addStock("Yamaha", "NMAX 155", 5);
        inventoryTree.addStock("Honda", "Click 125", 10);
        inventoryTree.addStock("Suzuki", "Burgman Street", 7);
        inventoryTree.addStock("Kawasaki", "Ninja 125", 4);
        System.out.println("Inventory (inorder by brand):");
        inventoryTree.display();

        System.out.println("\nSearch for 'Honda':");
        System.out.println("  " + inventoryTree.searchByBrand("Honda"));
        System.out.println("\nSearch for 'Kawasaki':");
        System.out.println("  " + inventoryTree.searchByBrand("Kawasaki"));
        System.out.println("\nSearch for 'Ducati':");
        InventoryItem found = inventoryTree.searchByBrand("Ducati");
        System.out.println("  " + (found != null ? found : "Not found"));
    }
}
