

**_Bactong, Charlize_**

**_Calipayan, Angelica_**

_Bachelor of Science in Information Technology_

_Term SY 2025-2026_

# **1\. Introduction**

MotorPH continues to experience growth in its motorcycle inventory operations, resulting in an increasing volume of inventory records that must be efficiently managed. In the initial system design (Linked List Design), a Singly Linked List was implemented as the core data structure to support dynamic insertion, deletion, and sequential traversal of inventory records. This solution provided structural simplicity and flexibility suitable for managing flat, non-hierarchical data.

However, as inventory size increases, performance considerations become more critical. While the linked list effectively supports dynamic updates, its reliance on sequential traversal results in linear time complexity for search operations and quadratic time complexity for basic sorting algorithms. These limitations may affect system responsiveness as the dataset grows.

To address these concerns, this milestone introduces an enhanced hybrid data structure model that combines both linear and non-linear structures. The proposed system retains the Singly Linked List for primary storage and dynamic record management while integrating a Hash Table for optimized search operations and the Merge Sort algorithm for improved sorting efficiency.

By combining linear and non-linear data structures, the refined design aims to improve computational performance, scalability, and practical applicability while maintaining structured data management. This enhancement reflects a more efficient and performance-aware approach to inventory system design.

# **2\. MotorPH Inventory Management Requirements**

MotorPH's inventory management system must support essential operational tasks performed in daily business activities. These include:

- Adding newly arrived inventory items
- Deleting outdated, incorrect, or sold-out records
- Searching for specific motorcycles based on attributes such as brand
- Displaying inventory records in an organized and sorted format

The inventory dataset consists of flat records, where each motorcycle entry exists independently and does not follow a hierarchical structure. Therefore, the system must manage sequential records efficiently without imposing parent-child relationships.

In addition to supporting these core operations, the system must accommodate continuous inventory growth. As the number of records increases, the data structure must maintain performance efficiency during frequent insertions, deletions, searches, and sorting operations. A design that relies solely on sequential traversal may become less efficient as the dataset expands.

The system must therefore ensure:

- Dynamic memory allocation to allow flexible growth
- Efficient search mechanisms to support frequent queries
- Scalable sorting capability for reporting and display
- Data consistency during record modifications

Given these requirements, the refined solution must not only preserve structural simplicity and flexibility but also enhance computational performance. The objective is to support MotorPH's operational needs while ensuring the system remains responsive and scalable as inventory volume increases.

# **3\. Identified Limitations of the Initial Design (Linked List Design)**

The initial implementation of MotorPH's inventory system utilized a Singly Linked List as the primary data structure. While this approach provided flexibility, dynamic memory allocation, and simplified insertion and deletion operations, performance limitations become more apparent as the number of inventory records increases.

One major limitation of the linked list structure is its reliance on sequential traversal for search operations. To locate a specific inventory record by brand, the system must iterate from the head node through each subsequent node until a match is found or the end of the list is reached. This results in a time complexity of O(n) in the worst case. As inventory size grows, this linear search approach may lead to slower retrieval times.

Another limitation is the use of basic comparison-based sorting algorithms such as Bubble Sort or Insertion Sort. These algorithms operate with a time complexity of O(n²), which becomes inefficient for larger datasets. Although sorting is primarily used for display and reporting purposes, improved performance can significantly enhance system responsiveness and scalability.

Additionally, the Linked List design lacks an indexing mechanism. All operations requiring record retrieval depend solely on list traversal, which limits the system's ability to perform optimized lookups. In real-world inventory systems, fast search functionality is critical for operational efficiency, particularly when handling frequent queries.

While the linked list remains effective for dynamic insertion and deletion of records, its limitations in search optimization and sorting efficiency justify the introduction of additional data structures and algorithms. These enhancements aim to improve computational performance without compromising structural flexibility.

# **4\. Proposed Enhanced Hybrid Architecture**

To improve the efficiency and scalability of MotorPH's inventory management system, a hybrid data structure model is proposed. This enhanced architecture combines both linear and non-linear data structures to address the performance limitations identified in the initial implementation.

The introduction of a non-linear data structure, specifically a hash table, was considered to address the performance limitations of the linked list design. While the linked list efficiently supports dynamic insertion and deletion, it relies on sequential traversal for searching, resulting in linear time complexity. In contrast, a hash table provides direct access to records through key-based indexing, significantly reducing search time. This improvement is particularly important for inventory systems where frequent lookup operations are required. Therefore, the integration of a non-linear structure enhances system efficiency without compromising the flexibility of the primary storage mechanism.

The refined system consists of three primary components:

- A Singly Linked List for dynamic record storage
- A Hash Table for optimized search operations
- The Merge Sort algorithm for efficient inventory sorting

Each component serves a distinct role within the system while working together to improve overall performance.

## 4.1 Singly Linked List (Primary Storage Structure)

The Singly Linked List remains the primary data structure for storing inventory records. It continues to support:

- Dynamic memory allocation
- Efficient insertion and deletion through pointer reassignment
- Sequential traversal of records

The linked list preserves the flexibility and structural simplicity established in MS1. All inventory records are stored as nodes containing the motorcycle brand, model, quantity, and a reference to the next node.

This structure ensures that inventory data remains organized and dynamically expandable without predefined size constraints.

## 4.2 Hash Table (Search Optimization Layer)

To address the inefficiency of linear search operations, a Hash Table is introduced as a non-linear indexing structure. The hash table enables faster retrieval of inventory records by mapping a specific key such as the motorcycle brand to a computed index using a hash function.

Key Characteristics:

- The motorcycle brand is used as the hashing key.
- A hash function converts the brand into an integer index.
- Each index represents a bucket in the hash table.

Collision handling is managed using chaining, where each bucket contains a linked list of nodes that share the same hash index.

By using hashing, the system achieves an average search time complexity of O(1), significantly improving performance compared to the O(n) traversal required in a standard linked list.

The hash table functions as an indexing layer and does not replace the linked list. Instead, it provides direct access to nodes already stored in the primary structure.

## 4.3 Merge Sort (Efficient Sorting Mechanism)

To improve sorting efficiency, the Merge Sort algorithm is integrated into the system. Unlike basic comparison-based algorithms with quadratic time complexity, Merge Sort operates in O(n log n) time complexity.

Merge Sort is particularly suitable for linked list structures because:

- It does not require random access indexing.
- It operates efficiently through recursive division of nodes.
- It can merge sorted sublists by rearranging node references.

The algorithm divides the linked list into smaller sublists, recursively sorts each half, and merges them in sorted order based on the chosen attribute (e.g., brand).

This significantly improves sorting performance, especially as the inventory dataset grows.

## 4.4 Interaction Between Components

The hybrid architecture ensures coordinated interaction between structures:

Insert Operation:

- A new node is created and added to the linked list.
- The same node is indexed into the hash table using the hashing function.

Delete Operation:

- The node is removed from the linked list.
- The corresponding entry is removed from the hash table bucket.

Search Operation:

- The system directly performs lookup through the hash table for fast retrieval.

Sorting Operation:

- Merge Sort is applied to the linked list when ordered output is required.

This coordinated design preserves the flexibility of linear storage while leveraging non-linear indexing for performance optimization.

# **5\. Data Structure Design (Detailed Technical Specification)**

The enhanced MotorPH inventory system integrates both linear and non-linear data structures to improve performance while maintaining structural flexibility. This section provides a detailed breakdown of each component and their internal design.

## 5.1 Linked List Structure (Primary Storage Layer)

The Singly Linked List continues to serve as the main storage mechanism for inventory records.

**_Node Structure:_**

**Each node contains:**

- - brand (String)
    - model (String)
    - quantity (Integer)
    - next (Reference to next node)

**Structural Characteristics:**

- - The first node is referenced by the head pointer.
    - Nodes are dynamically allocated during insertion.
    - Traversal begins from the head and proceeds sequentially.
    - No hierarchical relationship exists between nodes.
    - The list grows proportionally as inventory increases.

**Purpose**:

- - Maintains complete inventory dataset.
    - Handles insertion and deletion efficiently.
    - Serves as the base structure upon which sorting operates.

The linked list remains essential because it preserves dynamic storage behavior and flexible record management.

## 5.2 Hash Table Structure (Indexing Layer)

To optimize search operations, a Hash Table is introduced as a secondary indexing structure.

**Hash Key:**

- The **brand** attribute is used as the key.
- This attribute is appropriate because searches are commonly performed by brand.

**Hash Function:**

A simple hash function may be defined as:

- index = abs(hashCode(brand)) % tableSize

**Where:**

- hashCode(brand) converts the string into an integer.
- tableSize defines the number of buckets.
- The modulus operator ensures index bounds.

**Table Structure:**

- The hash table consists of an array of buckets.
- Each bucket contains a linked list (chaining).
- Chaining handles collisions when multiple brands map to the same index.

**Collision Handling (Chaining):**

If two brands produce the same hash index:

- They are stored in the same bucket.
- The bucket itself contains a linked list of nodes.
- Search within the bucket is then limited to that smaller list.

**Purpose**:

- Provides average search complexity of O(1).
- Reduces dependency on full list traversal.
- Acts as a fast access layer without replacing primary storage.

This design ensures search operations become significantly faster compared to MS1.

## 5.3 Merge Sort Design for Linked List

To improve sorting performance, Merge Sort is applied directly to the linked list.

**Why Merge Sort is Suitable:**

- Linked lists do not support random indexing.
- Merge Sort does not require array-based indexing.
- It works efficiently by dividing the list into halves.
- Node references are rearranged instead of shifting elements.

**High-Level Merge Sort Process:**

- Divide the linked list into two halves using slow and fast pointers.
- Recursively apply Merge Sort to each half.
- Merge the two sorted halves into a single sorted list.
- Update head reference to the merged list.

**Sorting Key:**

- Sorting is performed based on the brand attribute.
- Lexicographical string comparison is used.

**Time Complexity:**

- Divide phase: O(log n) levels
- Merge phase: O(n) per level
- Overall complexity: O(n log n)

This significantly improves sorting efficiency compared to O(n²) algorithms used previously.

## 5.4 Integrated Data Flow Architecture

The enhanced system operates through coordinated interactions:

**Insert Flow:**

- Create a new node.
- Append to the linked list.
- Compute hash index.
- Insert into the appropriate hash bucket.

**Delete Flow:**

- Remove nodes from linked list.
- Compute hash index.
- Remove nodes from the bucket chain.

**Search Flow:**

- Compute hash index.
- Traverse only the corresponding bucket.
- Return node if found.

**Sort Flow:**

- Apply Merge Sort on the linked list.
- Update head reference after merge.

**This layered architecture ensures:**

- Dynamic storage (Linked List)
- Fast lookup (Hash Table)
- Efficient sorting (Merge Sort)

The combination of structures produces a more scalable and performance-optimized system.

# **6\. Algorithm Implementation**

This section presents the pseudocode representation of the enhanced hybrid inventory system. The algorithms demonstrate how the linked list integrates with the hash table for optimized searching and how Merge Sort improves sorting efficiency.

## 6.1 Inventory Initialization Algorithm (CSV-Based Loading)

The inventory system must first be initialized by loading data from an external source, such as a CSV file. Each row in the CSV file represents a motorcycle inventory record containing attributes such as brand, model, and quantity. During initialization, the system reads each record and inserts it into both the linked list and hash table using the hybrid insert algorithm.

Pseudocode:

FUNCTION initializeInventory(file)

    OPEN file

    WHILE NOT end of file DO
        READ brand, model, quantity
        CALL insert(brand, model, quantity)
    END WHILE

    CLOSE file

END FUNCTION

## 6.2 Hybrid Insert Algorithm (Linked List + Hash Table)

When a new inventory record is added, it must be inserted into both the linked list and the hash table.

Pseudocode:

FUNCTION insert(brand, model, quantity)

    CREATE newNode

    IF head IS null THEN
        head ← newNode
    ELSE
        current ← head
        WHILE current.next IS NOT null DO
            current ← current.next
        END WHILE
        current.next ← newNode
    END IF

    index ← hash(brand)
    INSERT newNode INTO hashTable[index]

END FUNCTION

This ensures both storage and indexing are updated simultaneously.

## 6.3 Hybrid Delete Algorithm (Linked List + Hash Table)

Deletion requires removing the record from both structures to maintain consistency.

Pseudocode:  

FUNCTION delete(brand)

    IF head IS null THEN RETURN

    IF head.brand = brand THEN
        head ← head.next
    ELSE
        current ← head
        WHILE current.next IS NOT null AND current.next.brand ≠ brand DO
            current ← current.next
        END WHILE

        IF current.next IS NOT null THEN
            current.next ← current.next.next
        END IF
    END IF

    index ← hash(brand)
    REMOVE node FROM hashTable[index]

END FUNCTION

This prevents orphaned references in the hash index.

## 6.4 Optimized Search Algorithm (Hash-Based Search)

Instead of traversing the entire linked list, the system performs direct hash lookup.

Pseudocode:

FUNCTION search(brand)

    index ← hash(brand)
    current ← hashTable[index]

    WHILE current IS NOT null DO
        IF current.brand = brand THEN
            RETURN current
        END IF
        current ← current.next
    END WHILE

    RETURN null

END FUNCTION

This reduces average search complexity from O(n) to O(1).

## 6.5 Merge Sort Algorithm for Linked List

Merge Sort is applied when sorted output is required.

**_Step 1:_** _Merge Sort Main Function_

FUNCTION mergeSort(head)

    IF head IS null OR head.next IS null THEN
        RETURN head
    END IF

    middle ← getMiddle(head)
    next ← middle.next
    middle.next ← null

    left ← mergeSort(head)
    right ← mergeSort(next)

    RETURN merge(left, right)

END FUNCTION

**_Step 2:_** _Find Middle of Linked List_

FUNCTION getMiddle(head)

    slow ← head
    fast ← head.next

    WHILE fast IS NOT null AND fast.next IS NOT null DO
        slow ← slow.next
        fast ← fast.next.next
    END WHILE

    RETURN slow

END FUNCTION

**_Step 3:_** _Merge Two Sorted Lists_

FUNCTION merge(left, right)

    IF left IS null THEN RETURN right
    IF right IS null THEN RETURN left

    IF left.brand ≤ right.brand THEN
        result ← left
        result.next ← merge(left.next, right)
    ELSE
        result ← right
        result.next ← merge(left, right.next)
    END IF

    RETURN result

END FUNCTION

# **7\. Performance Comparison and Analysis (Linked-list design vs Hybrid Model)**

The enhanced hybrid architecture significantly improves the computational efficiency of MotorPH's inventory management system. This section compares the performance characteristics of the initial linked list-only design with the optimized hybrid model.

## 7.1 Time Complexity Comparison

The following table summarizes the time complexity of major operations:

| Operation | Linked List Only | Hybrid Model |
| --------- | ---------------- | ------------ |
| Insert    | O(n)             | O(n)         |
| ---       | ---              | ---          |
| Delete    | O(n)             | O(n)         |
| ---       | ---              | ---          |
| Search    | O(n)             | O(1) average |
| ---       | ---              | ---          |
| Sort      | O(n²)            | O(n log n)   |
| ---       | ---              | ---          |

## 7.2 Insert Operation Analysis

In both the Linked List Design and Hybride model, insertion into the linked list requires traversal to the end of the list, resulting in O(n) time complexity.

In the Hybrid Model, an additional step inserts the node into the hash table. Hash insertion operates in O(1) average time. Therefore, the overall insert complexity remains O(n) due to list traversal.

The enhancement does not significantly change insertion complexity but prepares the structure for faster future lookups.

## 7.3 Search Operation Analysis

In the Linked List Design, searching for an inventory record required full traversal of the linked list in the worst case, resulting in O(n) time complexity.

In the Hybrid Model, search operations utilize the hash table:

- The brand is converted into a hash index.
- The system directly accesses the corresponding bucket.
- Only a small chained list (if collision occurs) is traversed.

The average time complexity becomes O(1), assuming uniform hash distribution. In rare worst-case scenarios (extreme collisions), complexity may degrade to O(n), but proper hash function design minimizes this risk.

This represents the most significant performance improvement in the refined system.

**7.4 Sorting Operation Analysis**

In the Linked List Design, sorting was performed using basic comparison-based algorithms such as Bubble Sort or Insertion Sort, both of which operate in O(n²) time complexity.

In the Hybrid Model, Merge Sort replaces these algorithms. Merge Sort operates in:

- O(n log n) time complexity
- Consistent performance regardless of data order

Because Merge Sort divides the list recursively and merges sorted halves efficiently, it significantly reduces sorting time as the inventory grows.

This improvement enhances reporting efficiency and scalability.

## 7.5 Space Complexity Considerations

Linked List Design:

- Space Complexity: O(n)
- Each node stores data and one pointer.

Hybrid Model:

- Space Complexity: O(n + m)

Where:

- n = number of inventory records
- m = size of hash table

The hash table introduces additional memory overhead due to bucket storage. However, this trade-off is justified by improved search efficiency.

Merge Sort also requires additional recursive stack space proportional to O(log n) due to divide-and-conquer recursion.

## 7.6 Scalability Evaluation

As the inventory dataset grows:

- The Linked List Design performance degrades linearly for search and quadratically for sorting.
- The Hybrid Model maintains near-constant search time and logarithmic sorting efficiency.

This makes the hybrid system more suitable for handling larger inventory datasets and frequent search queries.

The introduction of a hash-based indexing mechanism significantly enhances system responsiveness in real-world operational scenarios.

# **8\. Feasibility and Practical Implications of the Hybrid Model**

The enhanced hybrid architecture combining a Singly Linked List, Hash Table, and Merge Sort algorithm provides measurable performance improvements. However, the introduction of additional structures also brings implementation and maintenance considerations that must be evaluated.

## 8.1 Implementation Complexity

Compared to the initial linked list-only design, the hybrid model introduces:

- A secondary indexing structure (Hash Table)
- A hash function for key mapping
- Collision handling mechanisms (chaining)
- A recursive sorting algorithm (Merge Sort)

This increases the structural complexity of the system. Developers must ensure synchronization between the linked list and hash table during insertion and deletion operations to prevent inconsistencies.

Despite this added complexity, the algorithms involved remain manageable and follow structured, well-defined procedures. The implementation is feasible using standard programming constructs and does not require advanced external libraries.

## 8.2 Memory Overhead

The introduction of a hash table increases memory consumption because:

- An additional array structure is required.
- Each bucket may contain its own linked chain.
- Merge Sort requires recursive stack space during execution.

While the Linked List Design required memory proportional only to the number of inventory nodes, The Hybrid Model requires memory for both the linked list and the hash table buckets.

However, this additional memory usage is justified by the substantial improvement in search performance. In modern computing environments, the trade-off between memory and speed is often acceptable when performance gains are significant.

## 8.3 Data Consistency Management

One important consideration in the hybrid system is maintaining consistency between structures.

When performing:

- Insert operations → The node must be added to both structures.
- Delete operations → The node must be removed from both structures.
- Updates → Changes must reflect in both linked list and hash index.

Proper coordination ensures that no stale references remain in the hash table and that the linked list remains structurally intact.

This dual-structure synchronization is manageable but requires careful implementation.

## 8.4 Practical Efficiency and Real-World Applicability

In real-world inventory systems, frequent search operations are common. Employees may repeatedly query inventory by brand or model. The introduction of hashing significantly improves response time for such queries.

Similarly, sorting large datasets for reporting purposes benefits from Merge Sort's predictable and efficient performance.

The hybrid design reflects how modern systems often combine multiple data structures to balance:

- Flexibility
- Speed
- Memory usage
- Maintainability

This layered approach mirrors real-world software engineering practices where indexing mechanisms are used alongside primary storage structures.

## 8.5 Overall Feasibility Assessment

The enhanced hybrid model is technically feasible and operationally practical. While it introduces additional structural components, the performance improvements particularly in search and sorting justify the design refinement.

The system remains scalable, adaptable, and capable of supporting increasing inventory volume without significant degradation in performance.

# **9\. Conclusion and Recommendation**

The refinement of MotorPH's inventory management system demonstrates the practical benefits of integrating both linear and non-linear data structures. While the initial implementation utilizing a Singly Linked List provided flexibility and dynamic data management, performance limitations in search and sorting operations became evident as inventory size increases.

The enhanced hybrid model introduced in this milestone addresses these limitations by combining three complementary components:

- A Singly Linked List for dynamic storage and record management
- A Hash Table for optimized search performance
- The Merge Sort algorithm for efficient sorting

This combination significantly improves system efficiency. Search operations are reduced from linear time complexity to average constant time, and sorting operations are improved from quadratic complexity to logarithmic-linear complexity. These improvements enhance scalability and responsiveness, particularly in environments where frequent inventory queries and reporting are required.

Although the hybrid model introduces additional structural complexity and memory overhead, these trade-offs are justified by measurable performance gains. The coordinated interaction between structures ensures data consistency while maintaining flexibility in record management.

The enhanced design reflects a more performance-aware and practical system architecture. By leveraging both linear and non-linear data structures, the refined solution aligns with real-world software engineering practices, where multiple data handling mechanisms are integrated to balance efficiency, scalability, and maintainability.

It is therefore recommended that MotorPH adopt the hybrid data structure model to support continued growth in inventory operations while maintaining efficient and reliable system performance.

#

#

# **10\. References**

W3Schools. (n.d.). _Java LinkedList_. W3Schools. <https://www.w3schools.com/java/java_linkedlist.asp>

GeeksforGeeks. (n.d.). _Linked List Data Structure_. GeeksforGeeks. <https://www.geeksforgeeks.org/dsa/linked-list-data-structure/>

Programiz. (n.d.). _Linked List in Data Structures_. Programiz. <https://www.programiz.com/dsa/linked-list>

Komilo, Y. (2022, September 16). _A comprehensive guide to LinkedList in Java_. _Medium_. <https://medium.com/@YodgorbekKomilo/a-comprehensive-guide-to-linkedlist-in-java-a64a4584a3dd>

GeeksforGeeks. (n.d.). Hashing in data structure. GeeksforGeeks. <https://www.geeksforgeeks.org/hashing-data-structure/>

W3Schools. (n.d.). Merge sort. W3Schools. <https://www.w3schools.com/dsa/dsa_algo_mergesort.php>

Programiz. (n.d.). Linked list. Programiz. <https://www.programiz.com/dsa/linked-list>

GeeksforGeeks. (n.d.). Analysis of algorithms | Time complexity. GeeksforGeeks. <https://www.geeksforgeeks.org/analysis-of-algorithms-time-and-space-complexity/>