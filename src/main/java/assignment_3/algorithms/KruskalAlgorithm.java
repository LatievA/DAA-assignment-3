package assignment_3.algorithms;

import assignment_3.model.Edge;
import assignment_3.model.Graph;
import assignment_3.model.Vertex;
import java.util.*;

public class KruskalAlgorithm {

    public AlgorithmResult run(Graph graph) {
        long start = System.currentTimeMillis();
        long operations = 0;

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(Comparator.comparingDouble(Edge::getWeight));
        operations += edges.size();

        UnionFind uf = new UnionFind(graph.getVertices());
        List<Edge> mstEdges = new ArrayList<>();
        double totalCost = 0.0;

        for (Edge e : edges) {
            Vertex u = e.getFrom();
            Vertex v = e.getTo();

            if (uf.find(u) != uf.find(v)) {
                uf.union(u, v);
                mstEdges.add(e);
                totalCost += e.getWeight();
            }
            operations++;
            if (mstEdges.size() == graph.getVertices().size() - 1) break;
        }

        long time = System.currentTimeMillis() - start;
        return new AlgorithmResult(totalCost, time, operations,
                graph.getVertices().size(), graph.getEdges().size(), mstEdges);
    }

    // --- Internal Union-Find class ---
    private static class UnionFind {
        private final Map<Vertex, Vertex> parent = new HashMap<>();

        public UnionFind(Collection<Vertex> vertices) {
            for (Vertex v : vertices) parent.put(v, v);
        }

        public Vertex find(Vertex v) {
            if (parent.get(v) != v)
                parent.put(v, find(parent.get(v))); // Path compression
            return parent.get(v);
        }

        public void union(Vertex a, Vertex b) {
            Vertex rootA = find(a);
            Vertex rootB = find(b);
            if (!rootA.equals(rootB))
                parent.put(rootA, rootB);
        }
    }
}