package assignment_3.model;

// Implements Comparable<Edge> for sorting used in Kruskal's algorithm
public class Edge implements Comparable<Edge> {
    private final Vertex from;
    private final Vertex to;
    private final double weight;

    public Edge(Vertex from, Vertex to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("%s -- %s (%.2f)", from, to, weight);
    }
}
