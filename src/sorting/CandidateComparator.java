package sorting;

import model.CandidateLocation;

import java.util.List;

/**
 * Central ranking rule for inspection candidates.
 */
public final class CandidateComparator {
    private CandidateComparator() {
    }

    public static int compare(CandidateLocation first, CandidateLocation second) {
        int scoreCompare = Integer.compare(second.getPriorityScore(), first.getPriorityScore());
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return first.getLocationId().compareTo(second.getLocationId());
    }

    public static boolean isSorted(List<CandidateLocation> candidates) {
        for (int i = 0; i < candidates.size() - 1; i++) {
            if (compare(candidates.get(i), candidates.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}
