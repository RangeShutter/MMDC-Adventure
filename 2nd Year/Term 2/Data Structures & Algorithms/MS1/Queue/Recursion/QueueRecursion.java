package Queue.Recursion;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Recursion in Queue - Recursively dequeue until empty (MotorPH)
 */
public class QueueRecursion {

    record QueueItem(String brand, int quantity) {}

    /** Enqueue inventory process (add to back). */
    static void enqueue(Deque<QueueItem> queue, String brand, int quantity) {
        queue.addLast(new QueueItem(brand, quantity));
    }

    /** Recursively dequeue queue until empty (base case: empty queue). */
    static void recursiveDequeue(Deque<QueueItem> queue) {
        if (queue.isEmpty()) {
            return;
        }
        QueueItem item = queue.removeFirst();
        System.out.println("  Dequeued: " + item.brand() + ": " + item.quantity() + " units");
        recursiveDequeue(queue);
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

        // Enqueue inventory process
        enqueue(queue, "Honda", 5);
        enqueue(queue, "Yamaha", 4);
        enqueue(queue, "Suzuki", 7);

        System.out.println("Queue before recursive dequeue:");
        display(queue);

        // Recursively dequeue until empty
        System.out.println("\nRecursive dequeue (emptying queue):");
        recursiveDequeue(queue);

        System.out.println("\nQueue after recursive dequeue:");
        display(queue);
    }
}
