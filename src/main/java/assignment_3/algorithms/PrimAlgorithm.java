package assignment_3.algorithms;

import assignment_3.model.Edge;
import assignment_3.model.Graph;
import assignment_3.model.Vertex;
import java.util.*;

public class PrimAlgorithm {

    public AlgorithmResult run(Graph graph) {
        long start = System.currentTimeMillis();
        long operations = 0;

        Set<Vertex> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        List<Edge> mstEdges = new ArrayList<>();
        double totalCost = 0.0;

        // Start from any vertex (first one)
        Vertex startVertex = graph.getVertices().iterator().next();
        visited.add(startVertex);
        pq.addAll(graph.getEdgesFrom(startVertex));
        operations += graph.getEdgesFrom(startVertex).size();

        while (!pq.isEmpty() && visited.size() < graph.getVertices().size()) {
            Edge edge = pq.poll();
            operations++;
            Vertex next = !visited.contains(edge.getFrom()) ? edge.getFrom() : edge.getTo();

            if (visited.contains(next)) continue;

            visited.add(next);
            mstEdges.add(edge);
            totalCost += edge.getWeight();

            for (Edge neighbor : graph.getEdgesFrom(next)) {
                Vertex other = neighbor.getTo().equals(next) ? neighbor.getFrom() : neighbor.getTo();
                if (!visited.contains(other)) {
                    pq.add(neighbor);
                    operations++;
                }
            }
        }

        long time = System.currentTimeMillis() - start;
        return new AlgorithmResult(totalCost, time, operations,
                graph.getVertices().size(), graph.getEdges().size(), mstEdges);
    }
}
