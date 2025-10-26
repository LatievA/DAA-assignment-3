package assignment_3.io;

import assignment_3.model.Graph;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphLoader {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<NamedGraph> loadGraphs(String filePath) throws IOException {
        JsonNode root = mapper.readTree(new File(filePath));
        JsonNode graphsNode = root.get("graphs");
        List<NamedGraph> graphs = new ArrayList<>();

        for (JsonNode g : graphsNode) {
            String name = g.get("name").asText();
            Graph graph = new Graph();

            // Add vertices
            for (JsonNode vertexNode : g.get("vertices")) {
                graph.addVertex(vertexNode.asText());
            }

            // Add edges
            for (JsonNode edgeNode : g.get("edges")) {
                String from = edgeNode.get("from").asText();
                String to = edgeNode.get("to").asText();
                double weight = edgeNode.get("weight").asDouble();
                graph.addEdge(from, to, weight);
            }

            graphs.add(new NamedGraph(name, graph));
        }
        return graphs;
    }

    // Helper record to store name + Graph together
    public static class NamedGraph {
        private final String name;
        private final Graph graph;

        public NamedGraph(String name, Graph graph) {
            this.name = name;
            this.graph = graph;
        }

        public String getName() {
            return name;
        }

        public Graph getGraph() {
            return graph;
        }
    }
}
