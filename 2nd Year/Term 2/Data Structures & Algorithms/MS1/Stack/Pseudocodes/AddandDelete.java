package Stack.Pseudocodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Add & Delete in Stack - Push (add) and Pop (delete) inventory actions (MotorPH)
 */
public class AddandDelete {

    record StackItem(String action, String brand, int quantity) {}

    /** Push inventory action onto stack (add to top). */
    static void push(List<StackItem> stack, String action, String brand, int quantity) {
        stack.add(new StackItem(action, brand, quantity));
    }

    /** Pop inventory action from stack (delete/remove top). Returns item or null if empty. */
    static StackItem pop(List<StackItem> stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.remove(stack.size() - 1);
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
        // Stack for inventory actions (ArrayList as stack: add at end = push, remove from end = pop)
        List<StackItem> stack = new ArrayList<>();

        // Push inventory actions (add)
        push(stack, "add", "Honda", 5);
        push(stack, "add", "Yamaha", 3);
        push(stack, "add", "Suzuki", 7);

        System.out.println("Stack after pushes:");
        display(stack);

        // Pop inventory action (delete top)
        StackItem lastAction = pop(stack);
        System.out.println("\nPopped: " + lastAction);
        System.out.println("Stack after pop:");
        display(stack);

        // Pop one more
        lastAction = pop(stack);
        System.out.println("\nPopped: " + lastAction);
        System.out.println("Stack after pop:");
        display(stack);
    }
}
