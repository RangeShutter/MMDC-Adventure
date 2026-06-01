package Queue.Pseudocodes;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Add & Delete in Queue - Enqueue (add) and Dequeue (delete) inventory process (MotorPH)
 */
public class AddandDelete {

    record QueueItem(String brand, int quantity) {}

    /** Enqueue inventory process (add to back of queue). */
    static void enqueue(Deque<QueueItem> queue, String brand, int quantity) {
        queue.addLast(new QueueItem(brand, quantity));
    }

    /** Dequeue inventory process (remove from front). Returns item or null if empty. */
    static QueueItem dequeue(Deque<QueueItem> queue) {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.removeFirst();
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
        // Queue for inventory process (FIFO - ArrayDeque: addLast = enqueue, removeFirst = dequeue)
        Deque<QueueItem> queue = new ArrayDeque<>();

        // Enqueue inventory process (add)
        enqueue(queue, "Honda", 5);
        enqueue(queue, "Yamaha", 4);
        enqueue(queue, "Suzuki", 7);

        System.out.println("Queue after enqueues:");
        display(queue);

        // Dequeue inventory process (delete front)
        QueueItem process = dequeue(queue);
        System.out.println("\nDequeued: " + process);
        System.out.println("Queue after dequeue:");
        display(queue);

        // Dequeue one more
        process = dequeue(queue);
        System.out.println("\nDequeued: " + process);
        System.out.println("Queue after dequeue:");
        display(queue);
    }
}
