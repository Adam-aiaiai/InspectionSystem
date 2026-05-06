package sorting;

import model.BenchmarkResult;
import model.CandidateLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Runs repeated timing measurements for sorting algorithms.
 */
public class PerformanceTester {
    private final List<Sorter> sorters;
    private final int runs;

    public PerformanceTester(List<Sorter> sorters, int runs) {
        this.sorters = new ArrayList<>(sorters);
        this.runs = runs;
    }

    public BenchmarkResult benchmark(String datasetName, List<CandidateLocation> originalData) {
        Map<String, Long> averageTimes = new LinkedHashMap<>();

        for (Sorter sorter : sorters) {
            long totalTime = 0L;
            for (int run = 0; run < runs; run++) {
                List<CandidateLocation> copy = new ArrayList<>(originalData);
                long start = System.nanoTime();
                sorter.sort(copy);
                long end = System.nanoTime();

                if (!CandidateComparator.isSorted(copy)) {
                    throw new IllegalStateException(sorter.getName() + " produced an invalid order for " + datasetName);
                }
                totalTime += end - start;
            }
            averageTimes.put(sorter.getName(), totalTime / runs);
        }

        return new BenchmarkResult(datasetName, averageTimes);
    }
}
