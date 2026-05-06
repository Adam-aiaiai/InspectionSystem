package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a shortest path query.
 */
public class PathResult {
    private final String source;
    private final String destination;
    private final List<String> path;
    private final double totalCost;

    public PathResult(String source, String destination, List<String> path, double totalCost) {
        this.source = source;
        this.destination = destination;
        this.path = path == null ? new ArrayList<>() : new ArrayList<>(path);
        this.totalCost = totalCost;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getPath() {
        return new ArrayList<>(path);
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getPathAsString() {
        if (path == null || path.isEmpty()) {
            return "No path found";
        }
        return String.join(" -> ", path);
    }

    @Override
    public String toString() {
        return "Path from " + source + " to " + destination + ":\n" +
               "  Path: " + getPathAsString() + "\n" +
               "  Total Cost: " + totalCost;
    }
}
