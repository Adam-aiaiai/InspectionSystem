package model;

/**
 * Represents a weighted edge in an undirected graph.
 * Extends Edge to add weight information.
 */
public class WeightedEdge extends graph.Edge implements Comparable<WeightedEdge> {
    public double weight; // The weight on edge (u, v)

    public WeightedEdge(int u, int v, double weight) {
        super(u, v);
        this.weight = weight;
    }

    public WeightedEdge(String from, String to, double weight) {
        super(-1, -1);
        this.weight = weight;
        this.fromStr = from;
        this.toStr = to;
    }

    private String fromStr;
    private String toStr;

    public double getWeight() {
        return weight;
    }

    public String getFromStr() {
        return fromStr;
    }

    public String getToStr() {
        return toStr;
    }

    public int compareTo(WeightedEdge edge) {
        if (weight > edge.weight) {
            return 1;
        } else if (weight == edge.weight) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "(" + u + ", " + v + ", " + weight + ")";
    }

    public String toStringString() {
        return fromStr + " <-> " + toStr + " (weight: " + weight + ")";
    }
}
