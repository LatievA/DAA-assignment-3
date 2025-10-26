# 🧮 Minimum Spanning Tree Algorithms Analysis Report

**Date:** 2025-10-26

---

## 1. Summary of Input Data and Algorithm Results

The experiment analyzed several graph datasets of increasing size to compare the performance of **Prim’s** and **Kruskal’s** algorithms in constructing Minimum Spanning Trees (MSTs). The table below summarizes total MST cost, execution time, and operation count.

| Dataset              | Graph Name         | Prim Total Cost | Kruskal Total Cost | Prim Time (ms) | Kruskal Time (ms) | Prim Operations | Kruskal Operations | Equal Total Cost |
|----------------------|-------------------|-----------------|--------------------|----------------|--------------------|-----------------|--------------------|------------------|
| input_small.json     | SmallGraph1       | 10.00           | 10.00              | 4              | 1                  | 9               | 9                  | ✅ true |
| input_small.json     | SmallGraph2       | 11.00           | 11.00              | 0              | 0                  | 11              | 11                 | ✅ true |
| input_small.json     | SmallGraph3       | 15.00           | 15.00              | 0              | 0                  | 13              | 13                 | ✅ true |
| input_medium.json    | MediumGraph1      | 31.00           | 31.00              | 0              | 0                  | 23              | 23                 | ✅ true |
| input_medium.json    | MediumGraph2      | 36.00           | 36.00              | 0              | 0                  | 27              | 27                 | ✅ true |
| input_large.json     | LargeGraph1       | 65.00           | 65.00              | 0              | 0                  | 60              | 60                 | ✅ true |
| input_large.json     | LargeGraph2       | 100.00          | 100.00             | 1              | 1                  | 59              | 61                 | ✅ true |
| input_extra_large.json | ExtraLargeGraph1 | 6631.00         | 6631.00            | 8              | 8                  | 8931            | 8935               | ✅ true |

### Observations
- Both algorithms produced identical MST costs, confirming **correctness** of implementation.
- **Execution time** increases proportionally with graph size, with noticeable difference only for very large graphs.
- **Operation counts** scale sharply with graph size, reflecting algorithmic complexity (Prim: O(V²), Kruskal: O(E log E)).

---

## 2. Theoretical vs. Practical Comparison
### In Theory
According to theory Neither Prim's nor Kruskal's algorithm is universally faster.
The choice should depend on the graph's density. Prim's algorithm is more efficient
for dense graphs (where the number of edges is close to (V^{2})) because its (O(V^{2}))
implementation is faster than Kruskal's in this scenario. Kruskal's algorithm is more 
efficient for sparse graphs (where the number of edges is closer to (V)) because its 
time complexity of (O(Elog V)) is better when (E) is significantly smaller than (V^{2}).


| Aspect | **Prim’s Algorithm** | **Kruskal’s Algorithm** |
|--------|----------------------|--------------------------|
| **Approach** | Greedy; grows MST vertex by vertex using minimum edge to a new vertex | Greedy; sorts edges and adds them if they don't form a cycle |
| **Complexity (Dense Graphs)** | O(V²) (simple), O(E + V log V) (with heap) | O(E log E) due to edge sorting |
| **Complexity (Sparse Graphs)** | O(E + V log V) with adjacency list | O(E log E) |
| **Memory Usage** | Uses adjacency matrix or priority queue | Requires sorting edges and union-find structure |
| **Preferred Graph Type** | Dense (many edges) | Sparse (few edges) |
| **Implementation Complexity** | Slightly simpler for adjacency matrix graphs | Requires Disjoint Set (Union-Find) data structure |
| **Empirical Result (This Test)** | Slightly fewer operations on smaller graphs | Comparable or slightly more operations overall |

### In Practice
In my practice the results of both Prim's and Kruskal's algorithms were nearly identical due to very small graphs
even my extra large graph with more than 350 vertices was too small to find difference in speed between algorithms in terms of
miliseconds. To find the difference in ms I should've made even greater graphs with different density which was not told in assignment
so I choose not to do it because of limited time.

---

## 3. Conclusions

How it was told before Prim's algorithm is more efficient for dense graphs, Kruskal's algorithm is more efficient for sparse graphs.
I hadn't results proving it due to small input graphs for today's powerful CPUs. However, I tend to believe academic community that says
Prim's for dense graphs, Kruskal's for sparse graps.

---

## 4. References

- Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
- GeeksforGeeks: [Prim’s Algorithm](https://www.geeksforgeeks.org/prims-algorithm-using-priority_queue-stl/) and [Kruskal’s Algorithm](https://www.geeksforgeeks.org/kruskals-algorithm-simple-implementation-for-adjacency-matrix/).
