from collections import deque

# Queue for inventory processing (FIFO - process in order received)


class InventoryQueue:
    def __init__(self):
        self.queue = deque()

    # Enqueue: add an inventory update to the back of the queue
    def enqueue(self, brand, model, quantity):
        self.queue.append({
            "brand": brand,
            "model": model,
            "quantity": quantity
        })

    # Dequeue: remove and return the front item (process next in line)
    def dequeue(self):
        if self.is_empty():
            return None
        return self.queue.popleft()

    # Peek: view the front item without removing it
    def peek(self):
        if self.is_empty():
            return None
        return self.queue[0]

    def is_empty(self):
        return len(self.queue) == 0

    def display(self):
        if self.is_empty():
            print("  (queue empty)")
            return
        for i, item in enumerate(self.queue):
            print(f"  {i + 1}. {item['brand']} {item['model']}: {item['quantity']} units")


def main():
    # Queue for inventory processing (MotorPH)
    inventory_queue = InventoryQueue()

    # Enqueue inventory updates
    print("Enqueueing inventory updates...")
    inventory_queue.enqueue("Suzuki", "Raider 150", 3)
    inventory_queue.enqueue("Yamaha", "NMAX 155", 5)
    inventory_queue.enqueue("Honda", "Click 125", 10)
    print("Queue (front to back):")
    inventory_queue.display()

    # Peek at front
    print("\nPeek at front (next to process):")
    front = inventory_queue.peek()
    print(f"  {front}")

    # Dequeue: process first item
    print("\nDequeue (process first item)...")
    processed_item = inventory_queue.dequeue()
    print(f"  Processed: {processed_item}")
    print("Queue after dequeue:")
    inventory_queue.display()

    # Dequeue one more
    print("\nDequeue next item...")
    processed_item = inventory_queue.dequeue()
    print(f"  Processed: {processed_item}")
    print("Queue after dequeue:")
    inventory_queue.display()


if __name__ == "__main__":
    main()
