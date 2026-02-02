package Stack.Pseudocodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Search in Stack - Search by brand without destroying the stack (MotorPH)
 */
public class Search {

    record StackItem(String action, String brand, int quantity) {}

    /** Push inventory action onto stack. */
    static void push(List<StackItem> stack, String action, String brand, int quantity) {
        stack.add(new StackItem(action, brand, quantity));
    }

    /** Search stack for item with target brand. Uses temp stack to preserve original stack. Returns item if found, else null. */
    static StackItem search(List<StackItem> stack, String targetBrand) {
        List<StackItem> tempStack = new ArrayList<>();
        StackItem foundItem = null;

        while (!stack.isEmpty()) {
            StackItem item = stack.remove(stack.size() - 1);
            if (item.brand().equals(targetBrand)) {
                foundItem = item;
            }
            tempStack.add(item);
        }

        // Restore stack
        while (!tempStack.isEmpty()) {
            stack.add(tempStack.remove(tempStack.size() - 1));
        }

        return foundItem;
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
        push(stack, "add", "Honda", 10);

        System.out.println("Stack:");
        display(stack);

        // Search for brand (stack unchanged after search)
        System.out.println("\nSearch for 'Honda':");
        StackItem result = search(stack, "Honda");
        System.out.println("  Found: " + result);

        System.out.println("\nSearch for 'Kawasaki':");
        result = search(stack, "Kawasaki");
        System.out.println("  Found: " + result);

        System.out.println("\nStack after searches (unchanged):");
        display(stack);
    }
}
