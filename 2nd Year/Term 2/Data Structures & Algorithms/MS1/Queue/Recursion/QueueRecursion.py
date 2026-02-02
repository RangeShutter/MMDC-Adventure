# Recursion in Queue - Recursively dequeue until empty (MotorPH)


def enqueue(queue, brand, quantity):
    """Enqueue inventory process (add to back)."""
    queue.append({"brand": brand, "quantity": quantity})


def recursive_dequeue(queue):
    """Recursively dequeue queue until empty (base case: empty queue)."""
    if not queue:
        return
    item = queue.pop(0)
    print(f"  Dequeued: {item['brand']}: {item['quantity']} units")
    recursive_dequeue(queue)


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

    print("Queue before recursive dequeue:")
    display(queue)

    # Recursively dequeue until empty
    print("\nRecursive dequeue (emptying queue):")
    recursive_dequeue(queue)

    print("\nQueue after recursive dequeue:")
    display(queue)


if __name__ == "__main__":
    main()
#