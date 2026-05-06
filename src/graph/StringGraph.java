package graph;

import java.util.*;

/**
 * Represents an undirected weighted graph using adjacency list representation.
 * Uses String vertices for location IDs (e.g., "L0001").
 */
public class StringGraph {
    private final Map<String, List<WeightedEdge>> adjacencyList;
    private final Set<String> vertices;

    public StringGraph() {
        this.adjacencyList = new HashMap<>();
        this.vertices = new HashSet<>();
    }

    public void addVertex(String vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
            vertices.add(vertex);
        }
    }

    public void addEdge(String from, String to, double weight) {
        addVertex(from);
        addVertex(to);
        
        WeightedEdge edge1 = new WeightedEdge(from, to, weight);
        WeightedEdge edge2 = new WeightedEdge(to, from, weight);
        
        adjacencyList.get(from).add(edge1);
        adjacencyList.get(to).add(edge2);
    }

    public void addUndirectedEdge(String from, String to, double weight) {
        addEdge(from, to, weight);
    }

    public List<WeightedEdge> getNeighbors(String vertex) {
        List<WeightedEdge> neighbors = adjacencyList.get(vertex);
        if (neighbors == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(neighbors);
    }

    public Set<String> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    public boolean hasVertex(String vertex) {
        return vertices.contains(vertex);
    }

    public boolean hasEdge(String from, String to) {
        if (!adjacencyList.containsKey(from)) {
            return false;
        }
        return adjacencyList.get(from).stream()
                .anyMatch(e -> e.getToStr() != null && e.getToStr().equals(to));
    }

    public double getEdgeWeight(String from, String to) {
        if (!adjacencyList.containsKey(from)) {
            return Double.MAX_VALUE;
        }
        return adjacencyList.get(from).stream()
                .filter(e -> e.getToStr() != null && e.getToStr().equals(to))
                .map(WeightedEdge::getWeight)
                .findFirst()
                .orElse(Double.MAX_VALUE);
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return adjacencyList.values().stream()
                .mapToInt(List::size)
                .sum() / 2;
    }

    public void printGraph() {
        System.out.println("Graph Structure:");
        System.out.println("Vertices: " + vertices.size());
        System.out.println("Edges: " + getEdgeCount());
        System.out.println("\nAdjacency List:");
        for (String vertex : vertices) {
            System.out.print(vertex + " -> ");
            List<WeightedEdge> neighbors = getNeighbors(vertex);
            System.out.println(neighbors.stream()
                    .map(e -> e.getToStr() + "(" + e.getWeight() + ")")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("none"));
        }
    }

    public static StringGraph buildFromEdges(List<WeightedEdge> edges) {
        StringGraph graph = new StringGraph();
        for (WeightedEdge edge : edges) {
            graph.addUndirectedEdge(edge.getFromStr(), edge.getToStr(), edge.getWeight());
        }
        return graph;
    }
}
