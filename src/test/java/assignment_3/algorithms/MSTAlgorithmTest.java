package assignment_3.algorithms;

import assignment_3.model.Graph;
import assignment_3.model.Edge;
import assignment_3.model.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated tests for Prim's and Kruskal's algorithms.
 *
 * Covers:
 *  a) Correctness: total cost, edge count, acyclicity, connectivity, disconnected graphs
 *  b) Performance & consistency: non-negative timing, reproducibility, valid operation counts
 */
public class MSTAlgorithmTest {

    private PrimAlgorithm prim;
    private KruskalAlgorithm kruskal;

    @BeforeEach
    void setup() {
        prim = new PrimAlgorithm();
        kruskal = new KruskalAlgorithm();
    }

    // -------------------------------------------------------
    // Helper: build a simple connected graph
    // -------------------------------------------------------
    private Graph buildSimpleGraph() {
        Graph g = new Graph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B", 1);
        g.addEdge("B", "C", 2);
        g.addEdge("A", "C", 3);
        return g;
    }

    // -------------------------------------------------------
    // Helper: build a disconnected graph
    // -------------------------------------------------------
    private Graph buildDisconnectedGraph() {
        Graph g = new Graph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addEdge("A", "B", 1);
        // (C, D) disconnected part
        return g;
    }

    // -------------------------------------------------------
    // CORRECTNESS TESTS
    // -------------------------------------------------------

    @Test
    void testTotalCostIsIdentical() {
        Graph g = buildSimpleGraph();
        AlgorithmResult primResult = prim.run(g);
        AlgorithmResult kruskalResult = kruskal.run(g);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 1e-6,
                "Prim and Kruskal MST costs should be identical");
    }

    @Test
    void testEdgeCountEqualsVminus1() {
        Graph g = buildSimpleGraph();
        int V = g.getVertices().size();

        AlgorithmResult primResult = prim.run(g);
        AlgorithmResult kruskalResult = kruskal.run(g);

        assertEquals(V - 1, primResult.getEdges().size(), "Prim MST should have V-1 edges");
        assertEquals(V - 1, kruskalResult.getEdges().size(), "Kruskal MST should have V-1 edges");
    }

    @Test
    void testMSTIsAcyclic() {
        Graph g = buildSimpleGraph();
        AlgorithmResult primResult = prim.run(g);
        AlgorithmResult kruskalResult = kruskal.run(g);

        assertTrue(isAcyclic(primResult.getEdges()), "Prim MST must be acyclic");
        assertTrue(isAcyclic(kruskalResult.getEdges()), "Kruskal MST must be acyclic");
    }

    @Test
    void testMSTConnectsAllVertices() {
        Graph g = buildSimpleGraph();
        int vertexCount = g.getVertices().size();

        AlgorithmResult primResult = prim.run(g);
        AlgorithmResult kruskalResult = kruskal.run(g);

        assertTrue(isConnected(primResult.getEdges(), vertexCount),
                "Prim MST should connect all vertices");
        assertTrue(isConnected(kruskalResult.getEdges(), vertexCount),
                "Kruskal MST should connect all vertices");
    }

    @Test
    void testDisconnectedGraphHandledGracefully() {
        Graph g = buildDisconnectedGraph();
        AlgorithmResult primResult = prim.run(g);
        AlgorithmResult kruskalResult = kruskal.run(g);

        // Both should either produce partial MST or indicate failure gracefully
        assertTrue(primResult.getEdges().size() < g.getVertices().size() - 1,
                "Prim should not create full MST for disconnected graph");
        assertTrue(kruskalResult.getEdges().size() < g.getVertices().size() - 1,
                "Kruskal should not create full MST for disconnected graph");
    }

    // -------------------------------------------------------
    // PERFORMANCE & CONSISTENCY TESTS
    // -------------------------------------------------------

    @Test
    void testExecutionTimeIsNonNegative() {
        Graph g = buildSimpleGraph();
        AlgorithmResult primResult = prim.run(g);
        AlgorithmResult kruskalResult = kruskal.run(g);

        assertTrue(primResult.getExecutionTimeMs() >= 0, "Prim time must be non-negative");
        assertTrue(kruskalResult.getExecutionTimeMs() >= 0, "Kruskal time must be non-negative");
    }

    @Test
    void testOperationCountsAreNonNegative() {
        Graph g = buildSimpleGraph();
        AlgorithmResult primResult = prim.run(g);
        AlgorithmResult kruskalResult = kruskal.run(g);

        assertTrue(primResult.getOperationCount() >= 0, "Prim operation count >= 0");
        assertTrue(kruskalResult.getOperationCount() >= 0, "Kruskal operation count >= 0");
    }

    @Test
    void testResultsAreReproducible() {
        Graph g = buildSimpleGraph();
        AlgorithmResult firstRunPrim = prim.run(g);
        AlgorithmResult secondRunPrim = prim.run(g);

        AlgorithmResult firstRunKruskal = kruskal.run(g);
        AlgorithmResult secondRunKruskal = kruskal.run(g);

        assertEquals(firstRunPrim.getTotalCost(), secondRunPrim.getTotalCost(), 1e-6,
                "Prim should produce consistent total cost");
        assertEquals(firstRunKruskal.getTotalCost(), secondRunKruskal.getTotalCost(), 1e-6,
                "Kruskal should produce consistent total cost");
    }

    // -------------------------------------------------------
    // Utility Methods
    // -------------------------------------------------------

    private boolean isAcyclic(List<Edge> edges) {
        // Simple union-find check
        Set<Vertex> vertices = new HashSet<>();
        for (Edge e : edges) {
            vertices.add(e.getFrom());
            vertices.add(e.getTo());
        }
        DisjointSet ds = new DisjointSet(vertices);
        for (Edge e : edges) {
            if (!ds.union(e.getFrom(), e.getTo())) {
                return false; // cycle detected
            }
        }
        return true;
    }

    private boolean isConnected(List<Edge> edges, int totalVertices) {
        if (edges.isEmpty()) return totalVertices <= 1;
        Set<Vertex> vertices = new HashSet<>();
        for (Edge e : edges) {
            vertices.add(e.getFrom());
            vertices.add(e.getTo());
        }
        return vertices.size() == totalVertices;
    }

    // --- minimal Disjoint Set for testing ---
    private static class DisjointSet {
        private final java.util.Map<Vertex, Vertex> parent = new java.util.HashMap<>();

        DisjointSet(Set<Vertex> vertices) {
            for (Vertex v : vertices) parent.put(v, v);
        }

        Vertex find(Vertex v) {
            if (parent.get(v) == v) return v;
            Vertex root = find(parent.get(v));
            parent.put(v, root);
            return root;
        }

        boolean union(Vertex a, Vertex b) {
            Vertex rootA = find(a);
            Vertex rootB = find(b);
            if (rootA == rootB) return false; // cycle
            parent.put(rootA, rootB);
            return true;
        }
    }
}
