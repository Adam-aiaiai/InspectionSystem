package graph;

import util.CSVReader;

import java.io.IOException;
import java.util.List;

/**
 * Builds the location graph from the project path CSV file.
 */
public class LocationGraphBuilder {
    public StringGraph buildFromCsv(String filePath) throws IOException {
        List<WeightedEdge> edges = CSVReader.readEdges(filePath);
        return StringGraph.buildFromEdges(edges);
    }
}
