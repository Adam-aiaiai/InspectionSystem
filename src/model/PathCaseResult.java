package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Result for one required Task B shortest-path case.
 */
public class PathCaseResult {
    private final String caseName;
    private final String start;
    private final String destination;
    private final List<String> waypoints;
    private final PathResult pathResult;

    public PathCaseResult(String caseName, String start, String destination, List<String> waypoints, PathResult pathResult) {
        this.caseName = caseName;
        this.start = start;
        this.destination = destination;
        this.waypoints = new ArrayList<>(waypoints);
        this.pathResult = pathResult;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getWaypoints() {
        return new ArrayList<>(waypoints);
    }

    public String getWaypointText() {
        return waypoints.isEmpty() ? "None" : String.join(" -> ", waypoints);
    }

    public PathResult getPathResult() {
        return pathResult;
    }
}
