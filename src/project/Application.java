package project;

import model.BenchmarkResult;
import model.CandidateLocation;
import model.PathCaseResult;
import model.PathResult;
import model.SortingResult;
import model.TaskBResult;
import service.TaskAService;
import service.TaskBService;
import sorting.Sorter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Console application for the Urban Infrastructure Inspection System.
 * Core algorithms are delegated to service, sorting, and graph classes.
 */
public class Application {

    private static final String DATA_PATH = "data/";
    private static final int TOP_N = 10;
    private static final int RUNS = 3;

    public static void main(String[] args) {
        new Application().run();
    }

    private void run() {
        System.out.println("=================================================");
        System.out.println("  Urban Infrastructure Inspection System");
        System.out.println("  CPT204 Algorithm Evaluation");
        System.out.println("=================================================\n");

        try {
            TaskAService taskAService = new TaskAService(DATA_PATH, TOP_N, RUNS);
            Map<String, SortingResult> sortingResults = taskAService.evaluateDatasets();
            printSortingEvaluation(taskAService.getSorters(), sortingResults);

            TaskBService taskBService = new TaskBService(DATA_PATH);
            TaskBResult taskBResult = taskBService.evaluateCases(sortingResults);
            printGraphEvaluation(taskBResult);
        } catch (IOException e) {
            System.err.println("Error reading data files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printSortingEvaluation(List<Sorter> sorters, Map<String, SortingResult> sortingResults) {
        System.out.println("=".repeat(60));
        System.out.println("TASK A: Sorting Algorithm Evaluation");
        System.out.println("=".repeat(60));

        System.out.println("\nSorting Rule:");
        System.out.println("  - Sort by priority_score in DESCENDING order");
        System.out.println("  - If priority_score is equal, sort by location_id in ASCENDING order");
        System.out.println("\nRunning each algorithm " + RUNS + " times and averaging results.\n");

        System.out.println("-".repeat(95));
        System.out.printf("| %-12s | %-18s | %-18s | %-18s |%n",
                "Dataset", sorters.get(0).getName(), sorters.get(1).getName(), sorters.get(2).getName());
        System.out.println("-".repeat(95));

        for (SortingResult sortingResult : sortingResults.values()) {
            BenchmarkResult benchmark = sortingResult.getBenchmarkResult();
            System.out.printf("| %-12s |", sortingResult.getDatasetName());
            for (Sorter sorter : sorters) {
                System.out.printf(" %14d ns |", benchmark.getAverageRuntimeNanos(sorter.getName()));
            }
            System.out.println();
        }
        System.out.println("-".repeat(95));

        System.out.println("\n" + "=".repeat(60));
        System.out.println("TOP 10 SELECTED LOCATIONS FROM EACH DATASET");
        System.out.println("=".repeat(60));

        for (SortingResult sortingResult : sortingResults.values()) {
            System.out.println("\n" + sortingResult.getDatasetName() + " Top 10:");
            List<CandidateLocation> topLocations = sortingResult.getTopLocations();
            for (int i = 0; i < topLocations.size(); i++) {
                CandidateLocation location = topLocations.get(i);
                System.out.printf("  %2d. %s (Score: %d)%n",
                        i + 1, location.getLocationId(), location.getPriorityScore());
            }
        }

        printSortingAnalysis();
    }

    private void printSortingAnalysis() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ANALYSIS SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("\n[Analysis Questions]");
        System.out.println("1. How does initial order affect performance?");
        System.out.println("   - Bubble Sort: early stop gives O(n) best case when already sorted, O(n^2) worst case");
        System.out.println("   - Quick Sort: first-pivot partition is close to the teacher example, but can be O(n^2)");
        System.out.println("   - Merge Sort: O(n log n) regardless of initial order");
        System.out.println("\n2. Which algorithm performs best?");
        System.out.println("   - Use the runtime table above; this depends on the dataset order and JVM run");
        System.out.println("   - Bubble Sort is simple but usually slow for 1000 items");
        System.out.println("\n3. Most consistent algorithm?");
        System.out.println("   - Merge Sort is the most consistent because it always divides and merges predictably");
        System.out.println("\n4. Best choice for final system?");
        System.out.println("   - Choose Merge Sort for stable performance, or Quick Sort if average speed and low memory are preferred");
        System.out.println("\n5. For significantly larger datasets?");
        System.out.println("   - Merge Sort or Quick Sort scales better than Bubble Sort");
        System.out.println("\n6. Runtime vs Memory trade-off?");
        System.out.println("   - Quick Sort uses little extra memory, but first-pivot worst case must be discussed");
        System.out.println("   - Merge Sort uses O(n) extra memory, but its runtime is stable");
    }

    private void printGraphEvaluation(TaskBResult taskBResult) {
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("TASK B: Graph Algorithm Evaluation");
        System.out.println("=".repeat(60));

        System.out.println("\nGraph Statistics:");
        System.out.println("  - Vertices: " + taskBResult.getVertexCount());
        System.out.println("  - Edges: " + taskBResult.getEdgeCount());

        Map<String, String> selectedTargets = taskBResult.getSelectedTargets();
        System.out.println("\nSelected Inspection Targets:");
        System.out.println("  Dataset A: A1=" + selectedTargets.get("A1") + ", A10=" + selectedTargets.get("A10"));
        System.out.println("  Dataset B: B1=" + selectedTargets.get("B1") + ", B5=" + selectedTargets.get("B5"));
        System.out.println("  Dataset C: C1=" + selectedTargets.get("C1") + ", C5=" + selectedTargets.get("C5"));

        System.out.println("\n" + "-".repeat(70));
        System.out.println("SHORTEST PATH QUERY RESULTS");
        System.out.println("-".repeat(70));

        for (PathCaseResult caseResult : taskBResult.getCaseResults()) {
            printPathCase(caseResult);
        }

        printGraphAnalysis();
    }

    private void printPathCase(PathCaseResult caseResult) {
        PathResult result = caseResult.getPathResult();
        System.out.println();
        if ("Case 1".equals(caseResult.getCaseName())) {
            System.out.println("[CASE 1] From " + caseResult.getStart() + " to itself (self-loop)");
        } else if ("Case 2".equals(caseResult.getCaseName())) {
            System.out.println("[CASE 2] From " + caseResult.getStart() + " to " + caseResult.getDestination());
        } else {
            System.out.println("[" + caseResult.getCaseName().toUpperCase() + "] From " + caseResult.getStart()
                    + " via " + caseResult.getWaypointText() + " to " + caseResult.getDestination());
        }
        System.out.println("  Start: " + result.getSource());
        System.out.println("  End: " + result.getDestination());
        if (!caseResult.getWaypoints().isEmpty()) {
            System.out.println("  Via: " + caseResult.getWaypointText());
        }
        System.out.println("  Path: " + result.getPathAsString());
        System.out.println("  Total Cost: " + result.getTotalCost());
    }

    private void printGraphAnalysis() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("GRAPH ALGORITHM ANALYSIS");
        System.out.println("-".repeat(70));
        System.out.println("\n1. Algorithm Used: Dijkstra's Shortest Path Algorithm");
        System.out.println("   - Suitable for weighted graphs with non-negative weights");
        System.out.println("   - The project graph uses String location IDs directly, such as L0001");
        System.out.println("\n2. Implementation Details:");
        System.out.println("   - Time Complexity: O((V + E) log V) using PriorityQueue");
        System.out.println("   - Space Complexity: O(V + E) for adjacency lists plus distance/predecessor maps");
        System.out.println("\n3. Are all optimal individual paths globally optimal?");
        System.out.println("   - No. Each query is a shortest path or a chain of shortest path segments");
        System.out.println("   - It does not solve a full TSP or vehicle routing problem for all 30 targets");
        System.out.println("\n4. Alternative algorithms for unweighted graphs:");
        System.out.println("   - BFS can find shortest paths by number of edges in O(V + E)");
        System.out.println("\n5. For larger graphs or coordinate-based routing:");
        System.out.println("   - A* can use coordinates and a heuristic");
        System.out.println("   - Hierarchical road-network methods can reduce search space");
    }
}
