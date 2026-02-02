# Search in Trees - BST search by brand (MotorPH)


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


def search(node, brand):
    """Search BST by brand. Returns node data if found, else None."""
    if node is None:
        return None
    if node.data["brand"] == brand:
        return node.data
    if brand < node.data["brand"]:
        return search(node.left, brand)
    return search(node.right, brand)


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

    # Build BST
    root = add_stock(root, "Yamaha", "NMAX 155", 5)
    root = add_stock(root, "Honda", "Click 125", 10)
    root = add_stock(root, "Suzuki", "Burgman Street", 7)
    root = add_stock(root, "Kawasaki", "Ninja 125", 4)

    print("Tree (inorder by brand):")
    display(root)

    # Search by brand
    print("\nSearch for 'Honda':")
    result = search(root, "Honda")
    print(f"  {result if result else 'Not found'}")

    print("\nSearch for 'Kawasaki':")
    result = search(root, "Kawasaki")
    print(f"  {result if result else 'Not found'}")

    print("\nSearch for 'Ducati':")
    result = search(root, "Ducati")
    print(f"  {result if result else 'Not found'}")


if __name__ == "__main__":
    main()
