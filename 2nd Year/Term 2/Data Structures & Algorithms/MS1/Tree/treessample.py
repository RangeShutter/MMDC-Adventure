# Simple Binary Search Tree for Inventory (ordered by brand)


class TreeNode:
    def __init__(self, brand, model, quantity):
        self.data = {
            "brand": brand,
            "model": model,
            "quantity": quantity
        }
        self.left = None
        self.right = None


class InventoryTree:
    def __init__(self):
        self.root = None

    # Insert: add item into BST (ordered by brand)
    def add_stock(self, brand, model, quantity):
        self.root = self._insert(self.root, brand, model, quantity)

    def _insert(self, node, brand, model, quantity):
        if node is None:
            return TreeNode(brand, model, quantity)
        if brand < node.data["brand"]:
            node.left = self._insert(node.left, brand, model, quantity)
        else:
            node.right = self._insert(node.right, brand, model, quantity)
        return node

    # Search: find item by brand (BST search)
    def search_by_brand(self, brand):
        return self._search(self.root, brand)

    def _search(self, node, brand):
        if node is None:
            return None
        if brand == node.data["brand"]:
            return node.data
        if brand < node.data["brand"]:
            return self._search(node.left, brand)
        return self._search(node.right, brand)

    # Inorder traversal: left -> root -> right (shows items sorted by brand)
    def _inorder(self, node, result):
        if node is None:
            return
        self._inorder(node.left, result)
        result.append(node.data)
        self._inorder(node.right, result)

    def display(self):
        result = []
        self._inorder(self.root, result)
        if not result:
            print("  (tree empty)")
            return
        for item in result:
            print(f"  - {item['brand']} {item['model']}: {item['quantity']} units")


def main():
    # Simple Binary Search Tree for Inventory (MotorPH)
    inventory_tree = InventoryTree()

    # Insert items (BST orders by brand)
    print("Inserting stock...")
    inventory_tree.add_stock("Yamaha", "NMAX 155", 5)
    inventory_tree.add_stock("Honda", "Click 125", 10)
    inventory_tree.add_stock("Suzuki", "Burgman Street", 7)
    inventory_tree.add_stock("Kawasaki", "Ninja 125", 4)
    print("Inventory (inorder by brand):")
    inventory_tree.display()

    # Search by brand
    print("\nSearch for 'Honda':")
    result = inventory_tree.search_by_brand("Honda")
    print(f"  {result}")
    print("\nSearch for 'Kawasaki':")
    result = inventory_tree.search_by_brand("Kawasaki")
    print(f"  {result}")
    print("\nSearch for 'Ducati':")
    result = inventory_tree.search_by_brand("Ducati")
    print(f"  {result if result else 'Not found'}")


if __name__ == "__main__":
    main()
