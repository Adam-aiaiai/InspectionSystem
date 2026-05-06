package model;

/**
 * Represents a candidate location with location ID and priority score.
 */
public class CandidateLocation {
    private final String locationId;
    private final int priorityScore;

    public CandidateLocation(String locationId, int priorityScore) {
        this.locationId = locationId;
        this.priorityScore = priorityScore;
    }

    public String getLocationId() {
        return locationId;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    @Override
    public String toString() {
        return locationId + " (Score: " + priorityScore + ")";
    }
}
