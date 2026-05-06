package sorting;

import model.CandidateLocation;

import java.util.List;

/**
 * Bubble Sort using adjacent comparisons and an early-stop flag.
 */
public class BubbleSorter implements Sorter {
    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public void sort(List<CandidateLocation> candidates) {
        boolean needNextPass = true;

        for (int pass = 1; pass < candidates.size() && needNextPass; pass++) {
            needNextPass = false;
            for (int i = 0; i < candidates.size() - pass; i++) {
                if (CandidateComparator.compare(candidates.get(i), candidates.get(i + 1)) > 0) {
                    swap(candidates, i, i + 1);
                    needNextPass = true;
                }
            }
        }
    }

    private void swap(List<CandidateLocation> candidates, int i, int j) {
        CandidateLocation temp = candidates.get(i);
        candidates.set(i, candidates.get(j));
        candidates.set(j, temp);
    }
}
