package project;

import model.CandidateLocation;
import model.PathCaseResult;
import model.SortingResult;
import model.TaskBResult;
import service.TaskAService;
import service.TaskBService;
import sorting.CandidateComparator;
import sorting.Sorter;
import util.CSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Small validation runner for checking algorithm correctness after changes.
 */
public class ValidationRunner {
    private static final String DATA_PATH = "data/";
    private static final int TOP_N = 10;
    private static final int RUNS = 3;

    public static void main(String[] args) throws IOException {
        TaskAService taskAService = new TaskAService(DATA_PATH, TOP_N, RUNS);
        validateSortingAlgorithms(taskAService);

        Map<String, SortingResult> sortingResults = taskAService.evaluateDatasets();
        validateTaskB(sortingResults);

        System.out.println("Validation completed successfully.");
    }

    private static void validateSortingAlgorithms(TaskAService taskAService) throws IOException {
        validateDatasetSorting("Dataset A", "candidates_A.csv", taskAService.getSorters());
        validateDatasetSorting("Dataset B", "candidates_B.csv", taskAService.getSorters());
        validateDatasetSorting("Dataset C", "candidates_C.csv", taskAService.getSorters());
    }

    private static void validateDatasetSorting(String datasetName, String fileName, List<Sorter> sorters) throws IOException {
        List<CandidateLocation> original = CSVReader.readCandidates(DATA_PATH + fileName);
        List<CandidateLocation> reference = null;

        for (Sorter sorter : sorters) {
            List<CandidateLocation> copy = new ArrayList<>(original);
            sorter.sort(copy);

            if (!CandidateComparator.isSorted(copy)) {
                throw new IllegalStateException(sorter.getName() + " did not sort " + datasetName + " correctly.");
            }

            if (reference == null) {
                reference = copy;
            } else if (!sameOrder(reference, copy)) {
                throw new IllegalStateException(sorter.getName() + " does not match the other algorithms for " + datasetName);
            }
        }

        System.out.println(datasetName + " sorting validation passed.");
    }

    private static boolean sameOrder(List<CandidateLocation> first, List<CandidateLocation> second) {
        if (first.size() != second.size()) {
            return false;
        }
        for (int i = 0; i < first.size(); i++) {
            CandidateLocation a = first.get(i);
            CandidateLocation b = second.get(i);
            if (!a.getLocationId().equals(b.getLocationId()) || a.getPriorityScore() != b.getPriorityScore()) {
                return false;
            }
        }
        return true;
    }

    private static void validateTaskB(Map<String, SortingResult> sortingResults) throws IOException {
        TaskBResult result = new TaskBService(DATA_PATH).evaluateCases(sortingResults);
        for (PathCaseResult caseResult : result.getCaseResults()) {
            if (caseResult.getPathResult().getPath().isEmpty()) {
                throw new IllegalStateException(caseResult.getCaseName() + " did not produce a path.");
            }
            if (caseResult.getPathResult().getTotalCost() == Double.MAX_VALUE) {
                throw new IllegalStateException(caseResult.getCaseName() + " produced an infinite cost.");
            }
            System.out.println(caseResult.getCaseName() + " path validation passed. Cost: "
                    + caseResult.getPathResult().getTotalCost());
        }
    }
}
