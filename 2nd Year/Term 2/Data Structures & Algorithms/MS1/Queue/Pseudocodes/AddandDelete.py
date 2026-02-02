# Add & Delete in Queue - Enqueue (add) and Dequeue (delete) inventory process (MotorPH)


def enqueue(queue, brand, quantity):
    """Enqueue inventory process (add to back of queue)."""
    queue.append({"brand": brand, "quantity": quantity})


def dequeue(queue):
    """Dequeue inventory process (remove from front). Returns item or None if empty."""
    if not queue:
        return None
    return queue.pop(0)


def display(queue):
    """Display queue (front to back)."""
    if not queue:
        print("  (queue empty)")
        return
    for i, item in enumerate(queue):
        print(f"  {i + 1}. {item['brand']}: {item['quantity']} units")


def main():
    # Queue for inventory process (FIFO)
    queue = []

    # Enqueue inventory process (add)
    enqueue(queue, "Honda", 5)
    enqueue(queue, "Yamaha", 4)
    enqueue(queue, "Suzuki", 7)

    print("Queue after enqueues:")
    display(queue)

    # Dequeue inventory process (delete front)
    process = dequeue(queue)
    print(f"\nDequeued: {process}")
    print("Queue after dequeue:")
    display(queue)

    # Dequeue one more
    process = dequeue(queue)
    print(f"\nDequeued: {process}")
    print("Queue after dequeue:")
    display(queue)


if __name__ == "__main__":
    main()
#
