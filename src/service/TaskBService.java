package service;

import graph.Dijkstra;
import graph.LocationGraphBuilder;
import graph.StringGraph;
import model.CandidateLocation;
import model.PathCaseResult;
import model.PathResult;
import model.SortingResult;
import model.TaskBResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Coordinates Task B graph construction and shortest-path cases.
 */
public class TaskBService {
    private final String dataPath;
    private final LocationGraphBuilder graphBuilder;

    public TaskBService(String dataPath) {
        this.dataPath = dataPath;
        this.graphBuilder = new LocationGraphBuilder();
    }

    public TaskBResult evaluateCases(Map<String, SortingResult> sortingResults) throws IOException {
        StringGraph graph = graphBuilder.buildFromCsv(dataPath + "paths.csv");

        List<CandidateLocation> topA = sortingResults.get("Dataset A").getTopLocations();
        List<CandidateLocation> topB = sortingResults.get("Dataset B").getTopLocations();
        List<CandidateLocation> topC = sortingResults.get("Dataset C").getTopLocations();

        String a1 = topA.get(0).getLocationId();
        String a10 = topA.get(9).getLocationId();
        String b1 = topB.get(0).getLocationId();
        String b5 = topB.get(4).getLocationId();
        String c1 = topC.get(0).getLocationId();
        String c5 = topC.get(4).getLocationId();

        Map<String, String> selectedTargets = new LinkedHashMap<>();
        selectedTargets.put("A1", a1);
        selectedTargets.put("A10", a10);
        selectedTargets.put("B1", b1);
        selectedTargets.put("B5", b5);
        selectedTargets.put("C1", c1);
        selectedTargets.put("C5", c5);

        List<PathCaseResult> cases = new ArrayList<>();
        PathResult case1 = new Dijkstra(graph).findShortestPath(a1, a1);
        cases.add(new PathCaseResult("Case 1", a1, a1, new ArrayList<>(), case1));

        PathResult case2 = new Dijkstra(graph).findShortestPath(a1, a10);
        cases.add(new PathCaseResult("Case 2", a1, a10, new ArrayList<>(), case2));

        PathResult case3 = Dijkstra.findMultiHopPath(graph, a1, b1, b5);
        cases.add(new PathCaseResult("Case 3", a1, b1, Arrays.asList(b5), case3));

        PathResult case4 = Dijkstra.findMultiHopPath(graph, a1, c1, b5, c5);
        cases.add(new PathCaseResult("Case 4", a1, c1, Arrays.asList(b5, c5), case4));

        return new TaskBResult(graph.getVertexCount(), graph.getEdgeCount(), selectedTargets, cases);
    }
}
