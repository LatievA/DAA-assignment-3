package assignment_3.algorithms;

import assignment_3.model.Edge;
import java.util.List;

public class AlgorithmResult {
    private double totalCost;
    private long executionTimeMs;
    private long operationCount;
    private int vertexCount;
    private int edgeCount;
    private List<Edge> edges;

    public AlgorithmResult(double totalCost, long executionTimeMs, long operationCount,
                           int vertexCount, int edgeCount, List<Edge> edges) {
        this.totalCost = totalCost;
        this.executionTimeMs = executionTimeMs;
        this.operationCount = operationCount;
        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;
        this.edges = edges;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public long getOperationCount() {
        return operationCount;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
