package assignment_3.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

public class ExtraLargeGraphGenerator {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // --- SETTINGS ---
        int vertexCount = 350;   // total vertices
        int edgeCount = 8000;    // total edges (dense but manageable)
        Random rand = new Random(42); // seed for reproducibility

        // --- Vertices ---
        List<String> vertices = new ArrayList<>();
        for (int i = 1; i <= vertexCount; i++) {
            vertices.add("V" + i);
        }

        // --- Edges ---
        List<Map<String, Object>> edges = new ArrayList<>();

        // Ensure connectivity (simple chain)
        for (int i = 1; i < vertexCount; i++) {
            Map<String, Object> edge = new HashMap<>();
            edge.put("from", "V" + i);
            edge.put("to", "V" + (i + 1));
            edge.put("weight", rand.nextInt(100) + 1);
            edges.add(edge);
        }

        // Add random edges for density
        while (edges.size() < edgeCount) {
            String from = "V" + (rand.nextInt(vertexCount) + 1);
            String to = "V" + (rand.nextInt(vertexCount) + 1);
            if (from.equals(to)) continue;

            Map<String, Object> edge = new HashMap<>();
            edge.put("from", from);
            edge.put("to", to);
            edge.put("weight", rand.nextInt(1000) + 1);
            edges.add(edge);
        }

        // --- Build the graph structure ---
        Map<String, Object> graph = new LinkedHashMap<>();
        graph.put("name", "ExtraLargeGraph1");
        graph.put("vertices", vertices);
        graph.put("edges", edges);

        // --- Wrap inside "graphs" key ---
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("graphs", Collections.singletonList(graph));

        // --- Save to file ---
        File outputFile = new File("src/main/resources/input_extra_large.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);

        System.out.println("✅ Created " + outputFile.getPath() + " with "
                + vertexCount + " vertices and " + edges.size() + " edges.");
    }
}
