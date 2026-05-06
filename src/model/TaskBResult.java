package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Full Task B output, including graph statistics and required cases.
 */
public class TaskBResult {
    private final int vertexCount;
    private final int edgeCount;
    private final Map<String, String> selectedTargets;
    private final List<PathCaseResult> caseResults;

    public TaskBResult(int vertexCount, int edgeCount, Map<String, String> selectedTargets,
                       List<PathCaseResult> caseResults) {
        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;
        this.selectedTargets = new LinkedHashMap<>(selectedTargets);
        this.caseResults = new ArrayList<>(caseResults);
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public Map<String, String> getSelectedTargets() {
        return new LinkedHashMap<>(selectedTargets);
    }

    public List<PathCaseResult> getCaseResults() {
        return new ArrayList<>(caseResults);
    }
}
