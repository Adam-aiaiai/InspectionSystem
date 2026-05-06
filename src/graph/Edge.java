package graph;

/**
 * Represents an edge in a graph.
 * Used as the base class for weighted edges.
 */
public class Edge {
    public int u; // Starting vertex index
    public int v; // Ending vertex index

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return u == edge.u && v == edge.v;
    }

    @Override
    public String toString() {
        return "(" + u + ", " + v + ")";
    }
}
