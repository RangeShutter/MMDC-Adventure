# Sort and Insert in Trees - BST insert (ordered by brand) (MotorPH)


class TreeNode:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.left = None
        self.right = None


def insert(node, item):
    """Insert item into BST. Returns (new) root. item is a dict with brand, model, quantity."""
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


def inorder(node, result):
    """Inorder traversal: left -> root -> right (sorted by brand)."""
    if node is None:
        return
    inorder(node.left, result)
    result.append(node.data)
    inorder(node.right, result)


def display(root):
    """Display tree (inorder = sorted by brand)."""
    result = []
    inorder(root, result)
    if not result:
        print("  (tree empty)")
        return
    for item in result:
        print(f"  - {item['brand']} {item['model']}: {item['quantity']} units")


def main():
    root = None

    # Insert in any order; tree stays sorted by brand
    root = add_stock(root, "Yamaha", "NMAX 155", 5)
    root = add_stock(root, "Honda", "Click 125", 10)
    root = add_stock(root, "Suzuki", "Burgman Street", 7)
    root = add_stock(root, "Kawasaki", "Ninja 125", 4)

    print("Tree after sort-and-insert (inorder by brand):")
    display(root)


if __name__ == "__main__":
    main()
