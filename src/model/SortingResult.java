package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Task A result for one candidate dataset.
 */
public class SortingResult {
    private final String datasetName;
    private final List<CandidateLocation> topLocations;
    private final BenchmarkResult benchmarkResult;

    public SortingResult(String datasetName, List<CandidateLocation> topLocations, BenchmarkResult benchmarkResult) {
        this.datasetName = datasetName;
        this.topLocations = new ArrayList<>(topLocations);
        this.benchmarkResult = benchmarkResult;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public List<CandidateLocation> getTopLocations() {
        return new ArrayList<>(topLocations);
    }

    public BenchmarkResult getBenchmarkResult() {
        return benchmarkResult;
    }
}
