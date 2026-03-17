# Data Structures Project 2 — Performance Comparison

A comparative benchmarking study of **four data structures** for managing large integer datasets, measuring **insertion time**, **search time**, and **search depth** across datasets from 20 to 3,000,000 keys.

> Course project — Technical University of Crete

---

## Data Structures Implemented

### AVL Tree
Self-balancing binary search tree that maintains $O(\log n)$ operations via rotations (LL, LR, RR, RL). Tracks node height explicitly and rebalances after every insertion/deletion.

### Binary Search Tree (BST)
Unbalanced BST used as a **baseline** for comparison. Simple insertion with no rebalancing — degrades toward $O(n)$ on skewed inputs.

### B-Tree
Multi-way balanced search tree optimized for block-based storage. Tested with two configurations:
- **BTree100** — minimum degree $T = 50$ (up to 99 keys per node)
- **BTree600** — minimum degree $T = 300$ (up to 599 keys per node)

All leaves stay at the same depth; nodes split proactively when full.

### Linear Hashing
Dynamic hash table that splits and merges buckets to maintain a target load factor (0.7–0.8). Tested with two bucket sizes:
- **LinearHashing40** — 40 keys per bucket, 500 initial pages
- **LinearHashing1000** — 1000 keys per bucket, 500 initial pages

Overflow chains handle collisions; achieves near-$O(1)$ search.

---

## Performance Results

Average **search levels** (nodes visited) at scale:

| Keys (N) | AVL | BST | BTree100 | BTree600 | LH40 | LH1000 |
|----------:|:---:|:---:|:--------:|:--------:|:----:|:------:|
| 1,000 | 10.6 | 35.0 | 2.0 | 2.0 | 1.0 | 1.0 |
| 100,000 | 17.5 | 93.6 | 3.0 | 2.0 | 1.7 | 1.0 |
| 3,000,000 | 22.9 | 99.6 | 4.0 | 3.0 | 1.7 | 1.7 |

> Linear Hashing provides the shallowest lookups, B-Trees stay within 3–4 levels, AVL scales logarithmically, and BST degrades significantly.

---

## Project Structure

```
src/src/org/tuc/
├── avl/                  # AVL Tree (AVLTree, Node, AVLTmain)
├── bst/                  # Binary Search Tree (BSTree, Node, BSTmain)
├── btree/                # B-Tree (BTree, Node, BtreeMain)
├── linearhashing/        # Linear Hashing (LinearHashing, HashBucket, LinearMain)
├── counter/              # MultiCounter — tracks search depth
├── interfaces/           # SearchInsert — common interface for all structures
├── Measurements.java     # Main benchmarking harness
└── TestDataCollector.java # Results formatting & CSV export
```

All structures implement a common `SearchInsert` interface:
```java
interface SearchInsert {
    void insert(int key);
    boolean searchKey(int key);
}
```

---

## How to Run

### Compile
```bash
cd src/src
javac org/tuc/**/*.java org/tuc/*.java
```

### Interactive demos
```bash
java org.tuc.avl.AVLTmain              # AVL Tree — menu-driven (insert/delete/search/traversals)
java org.tuc.bst.BSTmain               # BST — menu-driven
java org.tuc.btree.BtreeMain           # B-Tree demo
java org.tuc.linearhashing.LinearMain  # Linear Hashing demo
```

### Full benchmark
```bash
java org.tuc.Measurements
```
Reads binary datasets (`numbers-{N}.bin`) and outputs three CSV files:
| File | Content |
|------|---------|
| `TimeOfInsert.csv` | Mean insertion time (ns) per structure |
| `TimeOfSearch.csv` | Mean search time (ns) per structure |
| `LevelsOfSearch.csv` | Mean search depth per structure |

---

## Built With

- Java (no external dependencies)
