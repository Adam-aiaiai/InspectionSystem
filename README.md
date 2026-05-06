# CPT204 Group Project: Urban Infrastructure Inspection System

## Project Overview

This is a Java OOP application for an Urban Infrastructure Inspection System that demonstrates:

- **Task A**: Sorting algorithm evaluation (Bubble Sort, Quick Sort, Merge Sort)
- **Task B**: Graph shortest path algorithm (Dijkstra's algorithm)
- **Task C**: OOP design principles (encapsulation, abstraction, polymorphism)
- **Task D**: Project reflection and collaboration documentation

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── project/
│   │       ├── graph/
│   │       │   ├── Dijkstra.java      # Dijkstra's shortest path algorithm
│   │       │   └── Graph.java         # Adjacency list based weighted graph
│   │       ├── model/
│   │       │   ├── CandidateLocation.java  # Inspection candidate model
│   │       │   ├── Edge.java           # Graph edge representation
│   │       │   └── PathResult.java     # Shortest path result
│   │       ├── project/
│   │       │   ├── Application.java    # Main application logic
│   │       │   └── Main.java           # Entry point
│   │       ├── sorting/
│   │       │   ├── BubbleSorter.java   # Bubble Sort implementation
│   │       │   ├── CandidateComparator.java
│   │       │   ├── MergeSorter.java    # Merge Sort implementation
│   │       │   ├── QuickSorter.java    # Quick Sort implementation
│   │       │   └── Sorter.java         # Sorting interface
│   │       └── util/
│   │           ├── CSVReader.java      # CSV file reader
│   │           └── PerformanceTester.java  # Runtime benchmarking
│   └── resources/
│       ├── candidates_A.csv
│       ├── candidates_B.csv
│       ├── candidates_C.csv
│       └── paths.csv                   # Weighted graph data
```

## Team Responsibilities

### Student A (Task A, Task C, Report Chapter 1)
- Sorting algorithms implementation
- CSV reader for candidate data
- Runtime performance testing
- Report Chapter 1

### Student B (Task B, Task D, Report Chapters 2 & 4)
- Graph data structure (adjacency list)
- Dijkstra's shortest path algorithm
- Four path computation cases
- Project reflection and EDI analysis

## How to Run

### Compile
```bash
cd src/main/java
javac -d ../../../out project/Main.java
```

### Run
```bash
cd src/main/java
java project.Main
```

Or use your IDE (IntelliJ IDEA, VS Code, Eclipse) to run `project.Main`.

## Sorting Ranking Rule

Candidates are sorted by:
1. **Priority score** (descending)
2. **Location ID** (ascending, if scores are equal)

## Task B Shortest Path Cases

| Case | Route | Description |
|------|-------|-------------|
| 1 | A1 to A1 | Same location (cost = 0) |
| 2 | A1 to A10 | Direct path between top picks |
| 3 | A1 via B5 to B1 | Path with one waypoint |
| 4 | A1 via B5, C5 to C1 | Path with two waypoints |

## Data Files

### Candidate CSVs
Each row contains:
- `location_id`: Unique location identifier
- `priority_score`: Inspection priority (higher = more urgent)

### paths.csv
Represents an undirected weighted graph:
- `from_location`: Source node
- `to_location`: Destination node
- `weight`: Edge cost/distance

## OOP Principles Demonstrated

- **Encapsulation**: Private fields with public getters in model classes
- **Abstraction**: `Sorter` interface hides implementation details
- **Polymorphism**: `BubbleSorter`, `QuickSorter`, `MergeSorter` implement `Sorter`
- **Inheritance**: Classes extend/implement abstractions as needed
