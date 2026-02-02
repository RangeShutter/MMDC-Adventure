package Queue.Pseudocodes;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Search in Queue - Search by brand without destroying the queue (MotorPH)
 */
public class Search {

    record QueueItem(String brand, int quantity) {}

    /** Enqueue inventory process (add to back). */
    static void enqueue(Deque<QueueItem> queue, String brand, int quantity) {
        queue.addLast(new QueueItem(brand, quantity));
    }

    /** Search queue for item with target brand. Uses temp queue to preserve original. Returns item if found, else null. */
    static QueueItem search(Deque<QueueItem> queue, String targetBrand) {
        Deque<QueueItem> tempQueue = new ArrayDeque<>();
        QueueItem foundItem = null;

        while (!queue.isEmpty()) {
            QueueItem item = queue.removeFirst();
            if (item.brand().equals(targetBrand)) {
                foundItem = item;
            }
            tempQueue.addLast(item);
        }

        // Restore queue
        while (!tempQueue.isEmpty()) {
            queue.addLast(tempQueue.removeFirst());
        }

        return foundItem;
    }

    /** Display queue (front to back). */
    static void display(Deque<QueueItem> queue) {
        if (queue.isEmpty()) {
            System.out.println("  (queue empty)");
            return;
        }
        int i = 1;
        for (QueueItem item : queue) {
            System.out.println("  " + i++ + ". " + item.brand() + ": " + item.quantity() + " units");
        }
    }

    public static void main(String[] args) {
        // Queue for inventory process (FIFO)
        Deque<QueueItem> queue = new ArrayDeque<>();

        // Enqueue inventory process
        enqueue(queue, "Honda", 5);
        enqueue(queue, "Yamaha", 4);
        enqueue(queue, "Suzuki", 7);
        enqueue(queue, "Yamaha", 3);

        System.out.println("Queue:");
        display(queue);

        // Search for brand (queue unchanged after search)
        System.out.println("\nSearch for 'Yamaha':");
        QueueItem result = search(queue, "Yamaha");
        System.out.println("  Found: " + result);

        System.out.println("\nSearch for 'Kawasaki':");
        result = search(queue, "Kawasaki");
        System.out.println("  Found: " + result);

        System.out.println("\nQueue after searches (unchanged):");
        display(queue);
    }
}
