package project;

import graph.Dijkstra;
import graph.StringGraph;
import model.CandidateLocation;
import model.PathResult;
import model.WeightedEdge;
import util.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application for Urban Infrastructure Inspection System.
 * Implements sorting algorithms evaluation and graph shortest path queries.
 */
public class Application {

    private static final String DATA_PATH = "data/";
    private static final int TOP_N = 10;
    private static final int RUNS = 3;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Urban Infrastructure Inspection System");
        System.out.println("  CPT204 Algorithm Evaluation");
        System.out.println("=================================================\n");

        try {
            runSortingEvaluation();
            runGraphEvaluation();
        } catch (IOException e) {
            System.err.println("Error reading data files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runSortingEvaluation() throws IOException {
        System.out.println("=".repeat(60));
        System.out.println("TASK A: Sorting Algorithm Evaluation");
        System.out.println("=".repeat(60));

        List<List<CandidateLocation>> datasets = new ArrayList<>(3);
        datasets.add(CSVReader.readCandidates(DATA_PATH + "candidates_A.csv"));
        datasets.add(CSVReader.readCandidates(DATA_PATH + "candidates_B.csv"));
        datasets.add(CSVReader.readCandidates(DATA_PATH + "candidates_C.csv"));

        String[] datasetNames = {"Dataset A", "Dataset B", "Dataset C"};

        System.out.println("\nSorting Rule:");
        System.out.println("  - Sort by priority_score in DESCENDING order");
        System.out.println("  - If priority_score is equal, sort by location_id in ASCENDING order");
        System.out.println("\nRunning each algorithm " + RUNS + " times and averaging results.\n");

        System.out.println("-".repeat(95));
        System.out.printf("| %-12s | %-18s | %-18s | %-18s |%n", 
            "Dataset", "Bubble Sort", "Quick Sort", "Merge Sort");
        System.out.println("-".repeat(95));

        for (int i = 0; i < 3; i++) {
            List<CandidateLocation> originalData = datasets.get(i);
            System.out.printf("| %-12s |", datasetNames[i]);

            long bubbleTime = measureSortingTime(originalData, "bubble");
            System.out.printf(" %14d ns |", bubbleTime);

            long quickTime = measureSortingTime(originalData, "quick");
            System.out.printf(" %14d ns |", quickTime);

            long mergeTime = measureSortingTime(originalData, "merge");
            System.out.printf(" %14d ns |%n", mergeTime);
        }
        System.out.println("-".repeat(95));

        System.out.println("\n" + "=".repeat(60));
        System.out.println("TOP 10 SELECTED LOCATIONS FROM EACH DATASET");
        System.out.println("=".repeat(60));

        List<List<CandidateLocation>> top10 = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            List<CandidateLocation> sorted = mergeSort(new ArrayList<>(datasets.get(i)));
            top10.add(new ArrayList<>(sorted.subList(0, Math.min(TOP_N, sorted.size()))));
            System.out.println("\n" + datasetNames[i] + " Top 10:");
            for (int j = 0; j < top10.get(i).size(); j++) {
                System.out.printf("  %2d. %s (Score: %d)%n", 
                    j + 1, top10.get(i).get(j).getLocationId(), top10.get(i).get(j).getPriorityScore());
            }
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("ANALYSIS SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("\n[Analysis Questions]");
        System.out.println("1. How does initial order affect performance?");
        System.out.println("   - Bubble Sort: O(n) best case when nearly sorted, O(n²) worst case");
        System.out.println("   - Quick Sort: O(n log n) average, but O(n²) worst if pivot is extreme");
        System.out.println("   - Merge Sort: O(n log n) guaranteed regardless of initial order");
        System.out.println("\n2. Which algorithm performs best?");
        System.out.println("   - Merge Sort typically shows consistent O(n log n) performance");
        System.out.println("   - Quick Sort often faster in practice due to cache efficiency");
        System.out.println("   - Bubble Sort is slowest for all datasets");
        System.out.println("\n3. Most consistent algorithm?");
        System.out.println("   - Merge Sort is most consistent (guaranteed O(n log n))");
        System.out.println("\n4. Best choice for final system?");
        System.out.println("   - Recommend Merge Sort for stability, or Quick Sort for speed");
        System.out.println("\n5. For significantly larger datasets?");
        System.out.println("   - Quick Sort or Merge Sort (both O(n log n))");
        System.out.println("\n6. Runtime vs Memory trade-off?");
        System.out.println("   - Quick Sort: O(log n) space, fast runtime");
        System.out.println("   - Merge Sort: O(n) space, guaranteed performance");
    }

    private static long measureSortingTime(List<CandidateLocation> data, String algorithm) {
        long totalTime = 0;

        for (int run = 0; run < RUNS; run++) {
            List<CandidateLocation> copy = new ArrayList<>(data);
            long start = System.nanoTime();

            switch (algorithm) {
                case "bubble":
                    bubbleSort(copy);
                    break;
                case "quick":
                    quickSort(copy, 0, copy.size() - 1);
                    break;
                case "merge":
                    mergeSort(copy);
                    break;
            }

            long end = System.nanoTime();
            totalTime += (end - start);
        }

        return totalTime / RUNS;
    }

    private static void bubbleSort(List<CandidateLocation> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (compareCandidates(list.get(j), list.get(j + 1)) > 0) {
                    CandidateLocation temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    private static void quickSort(List<CandidateLocation> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private static int partition(List<CandidateLocation> list, int low, int high) {
        CandidateLocation pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compareCandidates(list.get(j), pivot) <= 0) {
                i++;
                CandidateLocation temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        CandidateLocation temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }

    private static List<CandidateLocation> mergeSort(List<CandidateLocation> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<CandidateLocation> left = new ArrayList<>(list.subList(0, mid));
        List<CandidateLocation> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private static List<CandidateLocation> merge(List<CandidateLocation> left, List<CandidateLocation> right) {
        List<CandidateLocation> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (compareCandidates(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }

        return result;
    }

    private static int compareCandidates(CandidateLocation a, CandidateLocation b) {
        int scoreCompare = Integer.compare(b.getPriorityScore(), a.getPriorityScore());
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return a.getLocationId().compareTo(b.getLocationId());
    }

    private static void runGraphEvaluation() throws IOException {
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("TASK B: Graph Algorithm Evaluation");
        System.out.println("=".repeat(60));

        List<WeightedEdge> edges = CSVReader.readEdges(DATA_PATH + "paths.csv");
        StringGraph graph = StringGraph.buildFromEdges(edges);

        System.out.println("\nGraph Statistics:");
        System.out.println("  - Vertices: " + graph.getVertexCount());
        System.out.println("  - Edges: " + graph.getEdgeCount());

        List<List<CandidateLocation>> datasets = new ArrayList<>(3);
        datasets.add(CSVReader.readCandidates(DATA_PATH + "candidates_A.csv"));
        datasets.add(CSVReader.readCandidates(DATA_PATH + "candidates_B.csv"));
        datasets.add(CSVReader.readCandidates(DATA_PATH + "candidates_C.csv"));

        List<List<CandidateLocation>> top10 = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            List<CandidateLocation> sorted = mergeSort(new ArrayList<>(datasets.get(i)));
            top10.add(new ArrayList<>(sorted.subList(0, Math.min(TOP_N, sorted.size()))));
        }

        String A1 = top10.get(0).get(0).getLocationId();
        String A10 = top10.get(0).get(9).getLocationId();
        String B1 = top10.get(1).get(0).getLocationId();
        String B5 = top10.get(1).get(4).getLocationId();
        String C1 = top10.get(2).get(0).getLocationId();
        String C5 = top10.get(2).get(4).getLocationId();

        System.out.println("\nSelected Inspection Targets:");
        System.out.println("  Dataset A: A1=" + A1 + ", A10=" + A10);
        System.out.println("  Dataset B: B1=" + B1 + ", B5=" + B5);
        System.out.println("  Dataset C: C1=" + C1 + ", C5=" + C5);

        System.out.println("\n" + "-".repeat(70));
        System.out.println("SHORTEST PATH QUERY RESULTS");
        System.out.println("-".repeat(70));

        System.out.println("\n[CASE 1] From " + A1 + " to itself (self-loop)");
        Dijkstra dijkstra1 = new Dijkstra(graph);
        PathResult result1 = dijkstra1.findShortestPath(A1, A1);
        printPathResult(result1);

        System.out.println("\n[CASE 2] From " + A1 + " to " + A10);
        Dijkstra dijkstra2 = new Dijkstra(graph);
        PathResult result2 = dijkstra2.findShortestPath(A1, A10);
        printPathResult(result2);

        System.out.println("\n[CASE 3] From " + A1 + " via " + B5 + " to " + B1);
        PathResult result3 = Dijkstra.findMultiHopPath(graph, A1, B1, B5);
        System.out.println("  Start: " + result3.getSource());
        System.out.println("  End: " + result3.getDestination());
        System.out.println("  Via: " + B5);
        System.out.println("  Path: " + result3.getPathAsString());
        System.out.println("  Total Cost: " + result3.getTotalCost());

        System.out.println("\n[CASE 4] From " + A1 + " via " + B5 + " and " + C5 + " to " + C1);
        PathResult result4 = Dijkstra.findMultiHopPath(graph, A1, C1, B5, C5);
        System.out.println("  Start: " + result4.getSource());
        System.out.println("  End: " + result4.getDestination());
        System.out.println("  Via: " + B5 + " -> " + C5);
        System.out.println("  Path: " + result4.getPathAsString());
        System.out.println("  Total Cost: " + result4.getTotalCost());

        System.out.println("\n" + "-".repeat(70));
        System.out.println("GRAPH ALGORITHM ANALYSIS");
        System.out.println("-".repeat(70));
        System.out.println("\n1. Algorithm Used: Dijkstra's Shortest Path Algorithm");
        System.out.println("   - Suitable for weighted graphs with non-negative weights");
        System.out.println("   - Finds optimal single-source shortest paths");
        System.out.println("\n2. Implementation Details:");
        System.out.println("   - Time Complexity: O((V + E) log V) using binary heap");
        System.out.println("   - Space Complexity: O(V) for distance/predecessor maps");
        System.out.println("\n3. Are all optimal individual paths globally optimal?");
        System.out.println("   - NOT necessarily. The inspection planning problem is");
        System.out.println("     more complex - it requires solving the Traveling");
        System.out.println("     Salesman Problem (TSP) or Vehicle Routing Problem");
        System.out.println("   - Individual shortest paths don't guarantee optimal tour");
        System.out.println("\n4. Alternative algorithms for unweighted graphs:");
        System.out.println("   - BFS: O(V + E) time, finds unweighted shortest paths");
        System.out.println("   - More memory efficient for sparse graphs");
        System.out.println("\n5. For larger graphs or coordinate-based routing:");
        System.out.println("   - A* algorithm with heuristics");
        System.out.println("   - Floyd-Warshall for all-pairs shortest paths");
        System.out.println("   - Contraction Hierarchies for real-world road networks");
    }

    private static void printPathResult(PathResult result) {
        System.out.println("  Start: " + result.getSource());
        System.out.println("  End: " + result.getDestination());
        System.out.println("  Path: " + result.getPathAsString());
        System.out.println("  Total Cost: " + result.getTotalCost());
    }
}
