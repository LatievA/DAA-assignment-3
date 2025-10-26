package assignment_3.model;

import java.util.*;

public class Graph {
    private final Map<Vertex, List<Edge>> adjacencyList;
    private final List<Edge> allEdges;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.allEdges = new ArrayList<>();
    }

    public void addVertex(String id) {
        adjacencyList.putIfAbsent(new Vertex(id), new ArrayList<>());
    }

    public void addEdge(String fromId, String toId, double weight) {
        Vertex from = getOrCreateVertex(fromId);
        Vertex to = getOrCreateVertex(toId);
        Edge edge = new Edge(from, to, weight);

        // Add to adjacency list (undirected)
        adjacencyList.get(from).add(edge);
        adjacencyList.get(to).add(new Edge(to, from, weight));

        allEdges.add(edge);
    }

    private Vertex getOrCreateVertex(String id) {
        return adjacencyList.keySet()
                .stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElseGet(() -> {
                    Vertex v = new Vertex(id);
                    adjacencyList.put(v, new ArrayList<>());
                    return v;
                });
    }

    public Set<Vertex> getVertices() {
        return adjacencyList.keySet();
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(allEdges);
    }

    public List<Edge> getEdgesFrom(Vertex vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        return allEdges.size();
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph:\n");
        for (Vertex v : adjacencyList.keySet()) {
            sb.append(v).append(" -> ").append(adjacencyList.get(v)).append("\n");
        }
        return sb.toString();
    }
}