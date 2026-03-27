# HybridInventory (MotorPH) - Linked List + Hash Table + Merge Sort

This program implements the concept described in `Document.md`:
- A **Singly Linked List** stores all inventory records (dynamic insert/delete + merge sort by `brand`)
- A **Hash Table** indexes records by **`brand`** using **chaining** for collisions
- **Merge Sort** is applied to the linked list when you choose “Display sorted by brand”

## Sample CSV

`inventory_sample.csv` uses this format:

`brand,model,quantity`

Example:
`Honda,WaveRunner,120`

The loader skips the header row if it starts with `brand`.

## How to run

Open a terminal in the `TA` folder and run:

1. Compile:
   - `javac HybridInventoryApp.java`
2. Run:
   - `java HybridInventoryApp`

The app’s default CSV path is `inventory_sample.csv` (you can also enter a different CSV path from the menu).

## Menu options

1. Load CSV
2. Add item
3. Delete by brand
4. Search by brand (uses the hash table)
5. Display sorted by brand (runs merge sort on the linked list)
6. Exit

