package Stack.Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Recursion in Stack - Recursively pop until empty (MotorPH)
 */
public class StackRecursion {

    record StackItem(String action, String brand, int quantity) {}

    /** Push inventory action onto stack. */
    static void push(List<StackItem> stack, String action, String brand, int quantity) {
        stack.add(new StackItem(action, brand, quantity));
    }

    /** Recursively pop stack until empty (base case: empty stack). */
    static void recursivePop(List<StackItem> stack) {
        if (stack.isEmpty()) {
            return;
        }
        StackItem item = stack.remove(stack.size() - 1);
        System.out.println("  Popped: [" + item.action() + "] " + item.brand() + ": " + item.quantity() + " units");
        recursivePop(stack);
    }

    /** Display stack (top to bottom). */
    static void display(List<StackItem> stack) {
        if (stack.isEmpty()) {
            System.out.println("  (stack empty)");
            return;
        }
        for (int i = stack.size() - 1; i >= 0; i--) {
            StackItem item = stack.get(i);
            System.out.println("  " + (stack.size() - i) + ". [" + item.action() + "] " + item.brand() + ": " + item.quantity() + " units");
        }
    }

    public static void main(String[] args) {
        // Stack for inventory actions (ArrayList: add at end = push, remove from end = pop)
        List<StackItem> stack = new ArrayList<>();

        // Push inventory actions
        push(stack, "add", "Honda", 5);
        push(stack, "add", "Yamaha", 3);
        push(stack, "add", "Suzuki", 7);

        System.out.println("Stack before recursive pop:");
        display(stack);

        // Recursively pop until empty
        System.out.println("\nRecursive pop (emptying stack):");
        recursivePop(stack);

        System.out.println("\nStack after recursive pop:");
        display(stack);
    }
}
