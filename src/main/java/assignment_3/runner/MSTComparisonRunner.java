package assignment_3.runner;

import assignment_3.algorithms.AlgorithmResult;
import assignment_3.algorithms.KruskalAlgorithm;
import assignment_3.algorithms.PrimAlgorithm;
import assignment_3.io.GraphLoader;
import assignment_3.io.GraphLoader.NamedGraph;
import assignment_3.model.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Runs Prim's and Kruskal's algorithms on multiple input datasets,
 * writes both JSON (detailed) and CSV (summary) results.
 */
public class MSTComparisonRunner {

    private final PrimAlgorithm prim = new PrimAlgorithm();
    private final KruskalAlgorithm kruskal = new KruskalAlgorithm();
    private final ObjectMapper mapper = new ObjectMapper();

    public void runAll() throws Exception {
        String[] inputFiles = {
                "src/main/resources/input_small.json",
                "src/main/resources/input_medium.json",
                "src/main/resources/input_large.json",
                "src/main/resources/input_extra_large.json"
        };

        ArrayNode allResults = mapper.createArrayNode();

        // --- Prepare CSV writer ---
        File csvFile = new File("src/main/resources/output.csv");
        try (FileWriter csvWriter = new FileWriter(csvFile)) {
            csvWriter.write("Dataset,Graph Name,Prim Total Cost,Kruskal Total Cost,"
                    + "Prim Time (ms),Kruskal Time (ms),"
                    + "Prim Operations,Kruskal Operations,Equal Total Cost\n");

            for (String path : inputFiles) {
                GraphLoader loader = new GraphLoader();
                List<NamedGraph> graphs = loader.loadGraphs(path);

                for (NamedGraph g : graphs) {
                    ObjectNode resultNode = runComparison(g.getName(), g.getGraph());
                    resultNode.put("dataset", new File(path).getName());
                    allResults.add(resultNode);

                    // Extract values for CSV
                    ObjectNode primNode = (ObjectNode) resultNode.get("prim");
                    ObjectNode kruskalNode = (ObjectNode) resultNode.get("kruskal");

                    String dataset = new File(path).getName();
                    String graphName = resultNode.get("graph_name").asText();
                    double primCost = primNode.get("total_cost").asDouble();
                    double kruskalCost = kruskalNode.get("total_cost").asDouble();
                    long primTime = primNode.get("execution_time_ms").asLong();
                    long kruskalTime = kruskalNode.get("execution_time_ms").asLong();
                    long primOps = primNode.get("operations").asLong();
                    long kruskalOps = kruskalNode.get("operations").asLong();
                    boolean equalCost = resultNode.get("equal_total_cost").asBoolean();

                    csvWriter.write(String.format("%s,%s,%.2f,%.2f,%d,%d,%d,%d,%b\n",
                            dataset, graphName, primCost, kruskalCost,
                            primTime, kruskalTime, primOps, kruskalOps, equalCost));
                }
            }
        }

        // --- Write JSON output ---
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("src/main/resources/output.json"), allResults);

        System.out.println("Results saved:");
        System.out.println("    JSON: src/main/resources/output.json");
        System.out.println("    CSV : src/main/resources/output.csv");
    }

    private ObjectNode runComparison(String graphName, Graph graph) {
        ObjectNode node = mapper.createObjectNode();

        AlgorithmResult primResult = prim.run(graph);
        AlgorithmResult kruskalResult = kruskal.run(graph);

        node.put("graph_name", graphName);
        node.put("vertex_count", primResult.getVertexCount());
        node.put("edge_count", primResult.getEdgeCount());

        // --- Prim results ---
        ObjectNode primNode = createAlgorithmNode("Prim", primResult);
        node.set("prim", primNode);

        // --- Kruskal results ---
        ObjectNode kruskalNode = createAlgorithmNode("Kruskal", kruskalResult);
        node.set("kruskal", kruskalNode);

        // --- Comparison summary ---
        node.put("equal_total_cost",
                Math.abs(primResult.getTotalCost() - kruskalResult.getTotalCost()) < 1e-6);

        return node;
    }

    /** Builds a JSON node for a single algorithm result */
    private ObjectNode createAlgorithmNode(String algoName, AlgorithmResult result) {
        ObjectNode node = mapper.createObjectNode();
        node.put("algorithm", algoName);
        node.put("total_cost", result.getTotalCost());
        node.put("execution_time_ms", result.getExecutionTimeMs());
        node.put("operations", result.getOperationCount());
        node.put("vertex_count", result.getVertexCount());
        node.put("edge_count", result.getEdgeCount());

        // --- Add MST edges array ---
        ArrayNode edgesArray = mapper.createArrayNode();
        result.getEdges().forEach(e -> {
            ObjectNode edgeNode = mapper.createObjectNode();
            edgeNode.put("from", e.getFrom().getId());
            edgeNode.put("to", e.getTo().getId());
            edgeNode.put("weight", e.getWeight());
            edgesArray.add(edgeNode);
        });
        node.set("mst_edges", edgesArray);

        return node;
    }

    public static void main(String[] args) throws Exception {
        new MSTComparisonRunner().runAll();
    }
}
