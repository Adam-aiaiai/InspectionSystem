package sorting;

import model.CandidateLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Merge Sort implemented with recursive divide-and-conquer.
 */
public class MergeSorter implements Sorter {
    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public void sort(List<CandidateLocation> candidates) {
        List<CandidateLocation> sorted = mergeSort(candidates);
        candidates.clear();
        candidates.addAll(sorted);
    }

    private List<CandidateLocation> mergeSort(List<CandidateLocation> candidates) {
        if (candidates.size() <= 1) {
            return new ArrayList<>(candidates);
        }

        int mid = candidates.size() / 2;
        List<CandidateLocation> left = mergeSort(new ArrayList<>(candidates.subList(0, mid)));
        List<CandidateLocation> right = mergeSort(new ArrayList<>(candidates.subList(mid, candidates.size())));

        return merge(left, right);
    }

    private List<CandidateLocation> merge(List<CandidateLocation> left, List<CandidateLocation> right) {
        List<CandidateLocation> merged = new ArrayList<>(left.size() + right.size());
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (CandidateComparator.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                merged.add(left.get(leftIndex));
                leftIndex++;
            } else {
                merged.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex));
            rightIndex++;
        }

        return merged;
    }
}
