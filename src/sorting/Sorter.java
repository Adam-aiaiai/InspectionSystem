package sorting;

import model.CandidateLocation;

import java.util.List;

/**
 * Common abstraction for all candidate sorting algorithms.
 */
public interface Sorter {
    String getName();

    void sort(List<CandidateLocation> candidates);
}
