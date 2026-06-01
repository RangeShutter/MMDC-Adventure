package Queue.Samples;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Queue for Inventory Processing (FIFO - process in order received).
 */
public class QueueSample {

    record InventoryItem(String brand, String model, int quantity) {}

    static class InventoryQueue {
        private final Deque<InventoryItem> queue = new ArrayDeque<>();

        void enqueue(String brand, String model, int quantity) {
            queue.addLast(new InventoryItem(brand, model, quantity));
        }

        InventoryItem dequeue() {
            if (queue.isEmpty()) {
                return null;
            }
            return queue.removeFirst();
        }

        InventoryItem peek() {
            if (queue.isEmpty()) {
                return null;
            }
            return queue.peekFirst();
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }

        void display() {
            if (queue.isEmpty()) {
                System.out.println("  (queue empty)");
                return;
            }
            int i = 1;
            for (InventoryItem item : queue) {
                System.out.println("  " + i++ + ". " + item.brand() + " " + item.model()
                        + ": " + item.quantity() + " units");
            }
        }
    }

    public static void main(String[] args) {
        InventoryQueue inventoryQueue = new InventoryQueue();

        System.out.println("Enqueueing inventory updates...");
        inventoryQueue.enqueue("Suzuki", "Raider 150", 3);
        inventoryQueue.enqueue("Yamaha", "NMAX 155", 5);
        inventoryQueue.enqueue("Honda", "Click 125", 10);
        System.out.println("Queue (front to back):");
        inventoryQueue.display();

        System.out.println("\nPeek at front (next to process):");
        System.out.println("  " + inventoryQueue.peek());

        System.out.println("\nDequeue (process first item)...");
        InventoryItem processed = inventoryQueue.dequeue();
        System.out.println("  Processed: " + processed);
        System.out.println("Queue after dequeue:");
        inventoryQueue.display();

        System.out.println("\nDequeue next item...");
        processed = inventoryQueue.dequeue();
        System.out.println("  Processed: " + processed);
        System.out.println("Queue after dequeue:");
        inventoryQueue.display();
    }
}
