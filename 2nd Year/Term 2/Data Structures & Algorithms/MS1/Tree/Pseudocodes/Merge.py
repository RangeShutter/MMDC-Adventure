# Merge in Trees - Merge source BST into target BST (inorder: left, insert, right) (MotorPH)


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


def merge_trees(source, target):
    """Merge source tree into target (inorder traverse source, insert each node into target). Returns updated target root."""
    if source is None:
        return target
    target = merge_trees(source.left, target)
    target = insert(target, source.data)
    target = merge_trees(source.right, target)
    return target


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
    # Tree 1: Honda, Yamaha
    tree1 = None
    tree1 = add_stock(tree1, "Yamaha", "NMAX 155", 5)
    tree1 = add_stock(tree1, "Honda", "Click 125", 10)

    # Tree 2: Suzuki, Kawasaki
    tree2 = None
    tree2 = add_stock(tree2, "Suzuki", "Burgman Street", 7)
    tree2 = add_stock(tree2, "Kawasaki", "Ninja 125", 4)

    print("Tree 1:")
    display(tree1)
    print("\nTree 2:")
    display(tree2)

    # Merge tree1 into tree2 (tree2 becomes the combined tree)
    merged = merge_trees(tree1, tree2)
    print("\nMerged tree (tree1 merged into tree2):")
    display(merged)


if __name__ == "__main__":
    main()

#