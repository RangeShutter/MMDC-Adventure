# Hash in Array - Simple hash table using array (MotorPH inventory)

SIZE = 10


def hash_function(key):
    """Map key to array index (0 to SIZE-1)."""
    return len(key) % SIZE


def create_table():
    """Create empty hash table (array of SIZE slots)."""
    return [None] * SIZE


def put(hash_table, key, value):
    """Insert key-value into hash table (linear probing for collisions)."""
    index = hash_function(key)
    start = index
    while True:
        if hash_table[index] is None or hash_table[index][0] == key:
            hash_table[index] = (key, value)
            return
        index = (index + 1) % SIZE
        if index == start:
            raise RuntimeError("Hash table full")


def get(hash_table, key):
    """Look up value by key; returns None if not found."""
    index = hash_function(key)
    start = index
    while hash_table[index] is not None:
        k, v = hash_table[index]
        if k == key:
            return v
        index = (index + 1) % SIZE
        if index == start:
            break
    return None


def display(hash_table):
    """Display all non-empty slots in the hash table."""
    for i, slot in enumerate(hash_table):
        if slot is not None:
            key, value = slot
            print(f"  [{i}] {key} -> {value}")


def main():
    # Simple hash table using array
    hash_table = create_table()

    # Insert inventory by brand (key)
    put(hash_table, "Honda", {"brand": "Honda", "model": "Click 125", "quantity": 5})
    put(hash_table, "Yamaha", {"brand": "Yamaha", "model": "NMAX 155", "quantity": 3})
    put(hash_table, "Suzuki", {"brand": "Suzuki", "model": "Burgman Street", "quantity": 7})

    print("Hash table (array) after inserts:")
    display(hash_table)

    # Look up by key
    print("\nGet 'Honda':", get(hash_table, "Honda"))
    print("Get 'Yamaha':", get(hash_table, "Yamaha"))
    print("Get 'Kawasaki':", get(hash_table, "Kawasaki"))


if __name__ == "__main__":
    main()
