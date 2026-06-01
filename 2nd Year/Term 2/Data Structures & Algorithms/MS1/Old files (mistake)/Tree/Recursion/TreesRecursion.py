# Recursion in Trees - Recursive inorder traversal (MotorPH)


class TreeNode:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.left = None
        self.right = None


def insert(node, item):
    """Insert item into BST. Returns (new) root."""
    if node is None:
        return TreeNode(item["brand"], item["model"], item["quantity"])
    if item["brand"] < node.data["brand"]:
        node.left = insert(node.left, item)
    else:
        node.right = insert(node.right, item)
    return node


def add_stock(root, brand, model, quantity):
    """Add stock: insert into BST by brand."""
    return insert(root, {"brand": brand, "model": model, "quantity": quantity})


def inorder_traversal(node):
    """Recursive inorder traversal: left -> root -> right (sorted by brand)."""
    if node:
        inorder_traversal(node.left)
        d = node.data
        print(f"  - {d['brand']} {d['model']}: {d['quantity']} units")
        inorder_traversal(node.right)


def main():
    root = None

    # Build BST
    root = add_stock(root, "Yamaha", "NMAX 155", 5)
    root = add_stock(root, "Honda", "Click 125", 10)
    root = add_stock(root, "Suzuki", "Burgman Street", 7)
    root = add_stock(root, "Kawasaki", "Ninja 125", 4)

    print("Tree (recursive inorder traversal):")
    inorder_traversal(root)


if __name__ == "__main__":
    main()
