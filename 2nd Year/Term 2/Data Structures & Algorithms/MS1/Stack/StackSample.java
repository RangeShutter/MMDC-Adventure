package Stack;
import java.util.ArrayList;
import java.util.List;

/**
 * Stack Operations for Inventory Actions (LIFO - e.g. undo last action).
 */
public class StackSample {

    record ActionItem(String action, String brand, String model, int quantity) {}

    static class InventoryActionStack {
        private final List<ActionItem> stack = new ArrayList<>();

        void push(String action, String brand, String model, int quantity) {
            stack.add(new ActionItem(action, brand, model, quantity));
        }

        ActionItem pop() {
            if (stack.isEmpty()) {
                return null;
            }
            return stack.remove(stack.size() - 1);
        }

        ActionItem peek() {
            if (stack.isEmpty()) {
                return null;
            }
            return stack.get(stack.size() - 1);
        }

        boolean isEmpty() {
            return stack.isEmpty();
        }

        void display() {
            if (stack.isEmpty()) {
                System.out.println("  (stack empty)");
                return;
            }
            for (int i = stack.size() - 1; i >= 0; i--) {
                ActionItem item = stack.get(i);
                System.out.println("  " + (stack.size() - i) + ". [" + item.action() + "] "
                        + item.brand() + " " + item.model() + ": " + item.quantity() + " units");
            }
        }
    }

    public static void main(String[] args) {
        InventoryActionStack inventoryStack = new InventoryActionStack();

        System.out.println("Pushing inventory actions onto stack...");
        inventoryStack.push("add", "Yamaha", "NMAX 155", 5);
        inventoryStack.push("add", "Honda", "Click 125", 10);
        inventoryStack.push("add", "Suzuki", "Burgman Street", 7);
        System.out.println("Stack (top to bottom):");
        inventoryStack.display();

        System.out.println("\nPeek at top action:");
        System.out.println("  " + inventoryStack.peek());

        System.out.println("\nPop last action (undo)...");
        ActionItem lastAction = inventoryStack.pop();
        System.out.println("  Undid: " + lastAction);
        System.out.println("Stack after pop:");
        inventoryStack.display();

        System.out.println("\nPop another action...");
        lastAction = inventoryStack.pop();
        System.out.println("  Undid: " + lastAction);
        System.out.println("Stack after pop:");
        inventoryStack.display();
    }
}
