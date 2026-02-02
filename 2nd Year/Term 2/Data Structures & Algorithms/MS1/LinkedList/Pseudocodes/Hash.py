# Hash in Linked List - Hash table with chaining (each bucket is a linked list) (MotorPH)

SIZE = 10


class Node:
    def __init__(self, key, brand, model, quantity):
        self.key = key
        self.data = {"brand": brand, "model": model, "quantity": quantity}
        self.next = None


def hash_function(key):
    """Map key to bucket index (0 to SIZE-1)."""
    return len(key) % SIZE


def create_table():
    """Create empty hash table (array of SIZE bucket heads)."""
    return [None] * SIZE


def put(hash_table, key, brand, model, quantity):
    """Insert key-value: add new node at head of chain at bucket."""
    index = hash_function(key)
    new_node = Node(key, brand, model, quantity)
    new_node.next = hash_table[index]
    hash_table[index] = new_node


def get(hash_table, key):
    """Look up value by key; returns data dict or None if not found."""
    index = hash_function(key)
    current = hash_table[index]
    while current:
        if current.key == key:
            return current.data
        current = current.next
    return None


def display(hash_table):
    """Display all buckets and their chains."""
    for i in range(SIZE):
        if hash_table[i] is None:
            continue
        current = hash_table[i]
        while current:
            d = current.data
            print(f"  [{i}] {current.key} -> {d['brand']} {d['model']}: {d['quantity']} units")
            current = current.next


def main():
    # Hash table: array of linked list heads
    hash_table = create_table()

    # Insert (chaining at each bucket)
    put(hash_table, "Honda", "Honda", "Click 125", 5)
    put(hash_table, "Yamaha", "Yamaha", "NMAX 155", 3)
    put(hash_table, "Suzuki", "Suzuki", "Burgman Street", 7)
    put(hash_table, "Kawasaki", "Kawasaki", "Ninja 125", 4)

    print("Hash table (array of linked lists) after inserts:")
    display(hash_table)

    # Look up by key
    print("\nGet 'Honda':", get(hash_table, "Honda"))
    print("Get 'Yamaha':", get(hash_table, "Yamaha"))
    print("Get 'Ducati':", get(hash_table, "Ducati"))


if __name__ == "__main__":
    main()
