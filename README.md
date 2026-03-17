# Data Structures Project 2

Implementation and performance comparison of:
- **AVL Tree**
- **B-Tree**
- **Linear Hashing**

## Structure
- `src/` — Java source files
  - `avl/` — AVL Tree implementation
  - `btree/` — B-Tree implementation
  - `linearhashing/` — Linear Hashing implementation
  - `bst/` — Binary Search Tree (baseline)
  - `Measurements.java` / `TestDataCollector.java` — Performance benchmarks

## How to Compile & Run
```bash
cd src/src
javac org/tuc/**/*.java org/tuc/*.java
java org.tuc.avl.AVLTmain        # Run AVL Tree
java org.tuc.btree.BtreeMain     # Run B-Tree
java org.tuc.linearhashing.LinearMain  # Run Linear Hashing
```
