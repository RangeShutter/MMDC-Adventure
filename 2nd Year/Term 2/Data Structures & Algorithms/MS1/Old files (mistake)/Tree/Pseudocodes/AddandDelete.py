# Add and Delete in Trees - BST insert and delete by brand (MotorPH)


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


def find_min(node):
    """Find node with minimum key in subtree (leftmost)."""
    while node and node.left:
        node = node.left
    return node


def delete(node, brand):
    """Delete node with given brand from BST. Returns (new) root of subtree."""
    if node is None:
        return node
    if brand < node.data["brand"]:
        node.left = delete(node.left, brand)
    elif brand > node.data["brand"]:
        node.right = delete(node.right, brand)
    else:
        # Found node to delete
        if node.left is None:
            return node.right
        if node.right is None:
            return node.left
        # Two children: replace with inorder successor (min of right subtree)
        min_node = find_min(node.right)
        node.data = min_node.data
        node.right = delete(node.right, min_node.data["brand"])
    return node


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

    # Add stock (insert)
    root = add_stock(root, "Yamaha", "NMAX 155", 5)
    root = add_stock(root, "Honda", "Click 125", 10)
    root = add_stock(root, "Suzuki", "Burgman Street", 7)
    root = add_stock(root, "Kawasaki", "Ninja 125", 4)

    print("Tree after adds:")
    display(root)

    # Delete by brand
    root = delete(root, "Yamaha")
    print("\nAfter delete 'Yamaha':")
    display(root)

    root = delete(root, "Kawasaki")
    print("\nAfter delete 'Kawasaki':")
    display(root)


if __name__ == "__main__":
    main()
