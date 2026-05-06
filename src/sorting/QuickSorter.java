package sorting;

import model.CandidateLocation;

import java.util.List;

/**
 * Quick Sort using the first element of each range as the pivot.
 */
public class QuickSorter implements Sorter {
    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public void sort(List<CandidateLocation> candidates) {
        quickSort(candidates, 0, candidates.size() - 1);
    }

    private void quickSort(List<CandidateLocation> candidates, int first, int last) {
        if (first < last) {
            int pivotIndex = partition(candidates, first, last);
            quickSort(candidates, first, pivotIndex - 1);
            quickSort(candidates, pivotIndex + 1, last);
        }
    }

    private int partition(List<CandidateLocation> candidates, int first, int last) {
        CandidateLocation pivot = candidates.get(first);
        int low = first + 1;
        int high = last;

        while (high > low) {
            while (low <= high && CandidateComparator.compare(candidates.get(low), pivot) <= 0) {
                low++;
            }
            while (low <= high && CandidateComparator.compare(candidates.get(high), pivot) > 0) {
                high--;
            }
            if (high > low) {
                swap(candidates, low, high);
            }
        }

        while (high > first && CandidateComparator.compare(candidates.get(high), pivot) >= 0) {
            high--;
        }

        if (CandidateComparator.compare(pivot, candidates.get(high)) > 0) {
            candidates.set(first, candidates.get(high));
            candidates.set(high, pivot);
            return high;
        }

        return first;
    }

    private void swap(List<CandidateLocation> candidates, int i, int j) {
        CandidateLocation temp = candidates.get(i);
        candidates.set(i, candidates.get(j));
        candidates.set(j, temp);
    }
}
