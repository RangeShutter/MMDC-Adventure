class Node:
    def __init__(self, brand, model, quantity):
        self.data = {
            "brand": brand,
            "model": model,
            "quantity": quantity
        }
        self.next = None

class InventoryLinkedList:
    def __init__(self):
        self.head = None

    # Add new stock
    def add_stock(self, brand, model, quantity):
        new_node = Node(brand, model, quantity)
        new_node.next = self.head
        self.head = new_node

    # Search by brand
    def search_by_brand(self, brand):
        current = self.head
        while current:
            if current.data["brand"] == brand:
                return current.data
            current = current.next
        return "Item not found"

    # Delete stock by brand
    def delete_stock(self, brand):
        current = self.head
        previous = None
        while current:
            if current.data["brand"] == brand:
                if previous:
                    previous.next = current.next
                else:
                    self.head = current.next
                return "Item deleted"
            previous = current
            current = current.next
        return "Item not found"

    # Display inventory (for demo)
    def display(self):
        current = self.head
        if not current:
            print("  (empty)")
            return
        while current:
            print(f"  - {current.data['brand']} {current.data['model']}: {current.data['quantity']} units")
            current = current.next


def main():
    # Inventory using linked list for MotorPH
    inventory = InventoryLinkedList()

    # Add new stock
    print("Adding stock...")
    inventory.add_stock("Honda", "Click 125", 10)
    inventory.add_stock("Yamaha", "NMAX 155", 5)
    inventory.add_stock("Suzuki", "Burgman Street", 7)
    print("Inventory:")
    inventory.display()

    # Search by brand
    print("\nSearch for 'Yamaha':")
    result = inventory.search_by_brand("Yamaha")
    print(f"  {result}")
    print("\nSearch for 'Kawasaki':")
    result = inventory.search_by_brand("Kawasaki")
    print(f"  {result}")

    # Delete stock by brand
    print("\nDeleting 'Yamaha'...")
    msg = inventory.delete_stock("Yamaha")
    print(f"  {msg}")
    print("Inventory after delete:")
    inventory.display()


if __name__ == "__main__":
    main()
