
public class Main {
    public static void main(String[] args) {

        InventoryList inventory = new InventoryList();

        inventory.insert("Honda", "Click", 10);
        inventory.insert("Yamaha", "Mio", 5);
        inventory.insert("Suzuki", "Raider", 3);

        System.out.println("=== Current Inventory ===");
        inventory.display();

        System.out.println("\nSearching for Yamaha...");
        Node found = inventory.search("Yamaha");
        if (found != null) {
            System.out.println("Found: " + found.brand + " " + found.model);
        } else {
            System.out.println("Not found.");
        }

        System.out.println("\nDeleting Honda...");
        inventory.delete("Honda");

        System.out.println("\n=== Updated Inventory ===");
        inventory.display();
    }
}
