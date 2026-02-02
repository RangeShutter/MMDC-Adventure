# Search in Queue - Search by brand without destroying the queue (MotorPH)


def enqueue(queue, brand, quantity):
    """Enqueue inventory process (add to back)."""
    queue.append({"brand": brand, "quantity": quantity})


def search(queue, target_brand):
    """Search queue for item with target brand. Uses temp queue to preserve original. Returns item if found, else None."""
    temp_queue = []
    found_item = None

    while queue:
        item = queue.pop(0)
        if item["brand"] == target_brand:
            found_item = item
        temp_queue.append(item)

    # Restore queue
    for item in temp_queue:
        queue.append(item)

    return found_item


def display(queue):
    """Display queue (front to back)."""
    if not queue:
        print("  (queue empty)")
        return
    for i, item in enumerate(queue):
        print(f"  {i + 1}. {item['brand']}: {item['quantity']} units")


def main():
    # Queue for inventory process
    queue = []

    # Enqueue inventory process
    enqueue(queue, "Honda", 5)
    enqueue(queue, "Yamaha", 4)
    enqueue(queue, "Suzuki", 7)
    enqueue(queue, "Yamaha", 3)

    print("Queue:")
    display(queue)

    # Search for brand (queue unchanged after search)
    print("\nSearch for 'Yamaha':")
    result = search(queue, "Yamaha")
    print(f"  Found: {result}")

    print("\nSearch for 'Kawasaki':")
    result = search(queue, "Kawasaki")
    print(f"  Found: {result}")

    print("\nQueue after searches (unchanged):")
    display(queue)


if __name__ == "__main__":
    main()
#