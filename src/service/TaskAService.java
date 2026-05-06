package service;

import model.BenchmarkResult;
import model.CandidateLocation;
import model.SortingResult;
import sorting.BubbleSorter;
import sorting.MergeSorter;
import sorting.PerformanceTester;
import sorting.QuickSorter;
import sorting.Sorter;
import util.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Coordinates Task A candidate sorting and benchmarking.
 */
public class TaskAService {
    private final String dataPath;
    private final int topCount;
    private final List<Sorter> sorters;
    private final PerformanceTester performanceTester;

    public TaskAService(String dataPath, int topCount, int runs) {
        this.dataPath = dataPath;
        this.topCount = topCount;
        this.sorters = Arrays.asList(new BubbleSorter(), new QuickSorter(), new MergeSorter());
        this.performanceTester = new PerformanceTester(sorters, runs);
    }

    public Map<String, SortingResult> evaluateDatasets() throws IOException {
        Map<String, SortingResult> results = new LinkedHashMap<>();
        results.put("Dataset A", evaluateDataset("Dataset A", "candidates_A.csv"));
        results.put("Dataset B", evaluateDataset("Dataset B", "candidates_B.csv"));
        results.put("Dataset C", evaluateDataset("Dataset C", "candidates_C.csv"));
        return results;
    }

    public List<Sorter> getSorters() {
        return new ArrayList<>(sorters);
    }

    private SortingResult evaluateDataset(String datasetName, String fileName) throws IOException {
        List<CandidateLocation> candidates = CSVReader.readCandidates(dataPath + fileName);
        BenchmarkResult benchmarkResult = performanceTester.benchmark(datasetName, candidates);
        List<CandidateLocation> topLocations = selectTopLocations(candidates);
        return new SortingResult(datasetName, topLocations, benchmarkResult);
    }

    private List<CandidateLocation> selectTopLocations(List<CandidateLocation> candidates) {
        List<CandidateLocation> sorted = new ArrayList<>(candidates);
        new MergeSorter().sort(sorted);
        return new ArrayList<>(sorted.subList(0, Math.min(topCount, sorted.size())));
    }
}
