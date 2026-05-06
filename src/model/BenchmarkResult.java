package model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Average runtime measurements for one dataset.
 */
public class BenchmarkResult {
    private final String datasetName;
    private final Map<String, Long> averageRuntimeNanos;

    public BenchmarkResult(String datasetName, Map<String, Long> averageRuntimeNanos) {
        this.datasetName = datasetName;
        this.averageRuntimeNanos = new LinkedHashMap<>(averageRuntimeNanos);
    }

    public String getDatasetName() {
        return datasetName;
    }

    public long getAverageRuntimeNanos(String algorithmName) {
        Long runtime = averageRuntimeNanos.get(algorithmName);
        return runtime == null ? -1L : runtime;
    }

    public Map<String, Long> getAverageRuntimeNanos() {
        return new LinkedHashMap<>(averageRuntimeNanos);
    }
}
