package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.CandidateLocation;
import model.WeightedEdge;

/**
 * Utility class for reading CSV files.
 */
public class CSVReader {

    public static List<CandidateLocation> readCandidates(String filePath) throws IOException {
        List<CandidateLocation> candidates = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null && line.startsWith("location_id")) {
                line = br.readLine();
            }
            
            while (line != null && !line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String locationId = parts[0].trim();
                    int priorityScore = Integer.parseInt(parts[1].trim());
                    candidates.add(new CandidateLocation(locationId, priorityScore));
                }
                line = br.readLine();
            }
        }
        
        return candidates;
    }

    public static List<WeightedEdge> readEdges(String filePath) throws IOException {
        List<WeightedEdge> edges = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null && line.startsWith("from_location")) {
                line = br.readLine();
            }
            
            while (line != null && !line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String from = parts[0].trim();
                    String to = parts[1].trim();
                    double weight = Double.parseDouble(parts[2].trim());
                    edges.add(new WeightedEdge(from, to, weight));
                }
                line = br.readLine();
            }
        }
        
        return edges;
    }

    public static List<String[]> readLines(String filePath) throws IOException {
        List<String[]> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null && line.startsWith("location_id")) {
                line = br.readLine();
            }
            
            while (line != null && !line.trim().isEmpty()) {
                String[] parts = line.split(",");
                lines.add(parts);
                line = br.readLine();
            }
        }
        
        return lines;
    }
}
