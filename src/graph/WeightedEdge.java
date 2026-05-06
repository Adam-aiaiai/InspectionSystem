package graph;

/**
 * Represents a weighted edge in a graph.
 */
public class WeightedEdge extends Edge implements Comparable<WeightedEdge> {
    public double weight;

    private String fromStr;
    private String toStr;

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

    public double getWeight() {
        return weight;
    }

    public String getFromStr() {
        return fromStr;
    }

    public String getToStr() {
        return toStr;
    }

    @Override
    public int compareTo(WeightedEdge edge) {
        return Double.compare(weight, edge.weight);
    }

    @Override
    public String toString() {
        if (fromStr != null && toStr != null) {
            return fromStr + " <-> " + toStr + " (weight: " + weight + ")";
        }
        return "(" + u + ", " + v + ", " + weight + ")";
    }
}
