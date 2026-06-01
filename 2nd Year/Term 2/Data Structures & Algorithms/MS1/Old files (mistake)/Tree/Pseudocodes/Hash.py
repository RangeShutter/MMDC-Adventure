# Hash in Trees - Hash table mapping brand (key) to tree node (MotorPH)


class TreeNode:
    def __init__(self, brand, model, quantity):
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.left = None
        self.right = None


def add_to_hash_tree(hash_table, brand, node):
    """Add brand -> node to hash table if brand not already present."""
    if brand not in hash_table:
        hash_table[brand] = node


def get_from_hash_tree(hash_table, brand):
    """Look up node by brand. Returns node or None."""
    return hash_table.get(brand)


def display(hash_table):
    """Display hash table (brand -> node data)."""
    if not hash_table:
        print("  (empty)")
        return
    for brand, node in hash_table.items():
        d = node.data
        print(f"  {brand} -> {d['brand']} {d['model']}: {d['quantity']} units")


def main():
    # Hash table: brand (key) -> TreeNode
    hash_table = {}

    # Create nodes and add to hash tree
    add_to_hash_tree(hash_table, "Honda", TreeNode("Honda", "Click 125", 10))
    add_to_hash_tree(hash_table, "Yamaha", TreeNode("Yamaha", "NMAX 155", 5))
    add_to_hash_tree(hash_table, "Suzuki", TreeNode("Suzuki", "Burgman Street", 7))

    print("Hash table (brand -> node):")
    display(hash_table)

    # Look up by brand
    print("\nGet 'Honda':")
    node = get_from_hash_tree(hash_table, "Honda")
    print(f"  {node.data if node else 'Not found'}")

    print("\nGet 'Kawasaki':")
    node = get_from_hash_tree(hash_table, "Kawasaki")
    print(f"  {node.data if node else 'Not found'}")


if __name__ == "__main__":
    main()
