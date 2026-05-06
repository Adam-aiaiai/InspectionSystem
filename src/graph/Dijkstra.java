package graph;

import model.PathResult;
import model.WeightedEdge;

import java.util.*;

/**
 * Implementation of Dijkstra's shortest path algorithm.
 * Returns the shortest path from a source vertex to all other vertices.
 * Works with StringGraph for location ID-based vertices.
 */
public class Dijkstra {
    private final StringGraph graph;
    private final Map<String, Double> distances;
    private final Map<String, String> predecessors;

    public Dijkstra(StringGraph graph) {
        this.graph = graph;
        this.distances = new HashMap<>();
        this.predecessors = new HashMap<>();
    }

    public PathResult findShortestPath(String source, String destination) {
        if (!graph.hasVertex(source) || !graph.hasVertex(destination)) {
            return new PathResult(source, destination, Collections.emptyList(), Double.MAX_VALUE);
        }

        if (source.equals(destination)) {
            return new PathResult(source, destination, Collections.singletonList(source), 0.0);
        }

        dijkstra(source);
        return buildPath(source, destination);
    }

    private void dijkstra(String source) {
        distances.clear();
        predecessors.clear();

        Set<String> vertices = graph.getVertices();
        for (String vertex : vertices) {
            distances.put(vertex, Double.MAX_VALUE);
            predecessors.put(vertex, null);
        }
        distances.put(source, 0.0);

        PriorityQueue<String> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(distances::get)
        );
        priorityQueue.add(source);

        Set<String> settled = new HashSet<>();

        while (!priorityQueue.isEmpty()) {
            String current = priorityQueue.poll();

            if (settled.contains(current)) {
                continue;
            }
            settled.add(current);

            List<WeightedEdge> neighbors = graph.getNeighbors(current);
            for (WeightedEdge edge : neighbors) {
                String neighbor = edge.getToStr();
                double newDist = distances.get(current) + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    priorityQueue.add(neighbor);
                }
            }
        }
    }

    private PathResult buildPath(String source, String destination) {
        List<String> path = new LinkedList<>();
        double cost = distances.getOrDefault(destination, Double.MAX_VALUE);

        if (cost == Double.MAX_VALUE) {
            return new PathResult(source, destination, Collections.emptyList(), Double.MAX_VALUE);
        }

        String current = destination;
        while (current != null) {
            path.add(0, current);
            current = predecessors.get(current);
        }

        if (path.isEmpty() || !path.get(0).equals(source)) {
            return new PathResult(source, destination, Collections.emptyList(), Double.MAX_VALUE);
        }

        return new PathResult(source, destination, path, cost);
    }

    public double getDistance(String vertex) {
        return distances.getOrDefault(vertex, Double.MAX_VALUE);
    }

    public Map<String, Double> getAllDistances() {
        return new HashMap<>(distances);
    }

    public static PathResult findMultiHopPath(StringGraph graph, String start, String end, String... intermediates) {
        if (intermediates == null || intermediates.length == 0) {
            Dijkstra dijkstra = new Dijkstra(graph);
            return dijkstra.findShortestPath(start, end);
        }

        List<String> fullPath = new ArrayList<>();
        double totalCost = 0.0;

        String currentStart = start;
        for (String intermediate : intermediates) {
            Dijkstra dijkstra = new Dijkstra(graph);
            PathResult segment = dijkstra.findShortestPath(currentStart, intermediate);
            
            if (segment.getPath().isEmpty()) {
                return new PathResult(start, end, Collections.emptyList(), Double.MAX_VALUE);
            }

            if (fullPath.isEmpty()) {
                fullPath.addAll(segment.getPath());
            } else {
                List<String> segmentPath = segment.getPath();
                for (int i = 1; i < segmentPath.size(); i++) {
                    fullPath.add(segmentPath.get(i));
                }
            }
            totalCost += segment.getTotalCost();
            currentStart = intermediate;
        }

        Dijkstra finalDijkstra = new Dijkstra(graph);
        PathResult finalSegment = finalDijkstra.findShortestPath(currentStart, end);
        
        if (finalSegment.getPath().isEmpty()) {
            return new PathResult(start, end, Collections.emptyList(), Double.MAX_VALUE);
        }

        List<String> finalPath = finalSegment.getPath();
        for (int i = 1; i < finalPath.size(); i++) {
            fullPath.add(finalPath.get(i));
        }
        totalCost += finalSegment.getTotalCost();

        return new PathResult(start, end, fullPath, totalCost);
    }
}
