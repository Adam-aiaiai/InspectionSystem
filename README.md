# CPT204 Group Project: Urban Infrastructure Inspection System

## Overview

This is a plain IntelliJ IDEA Java project for the CPT204 Advanced Object-Oriented Programming group project.

It contains:

- Task A: Bubble Sort, Quick Sort, and Merge Sort for candidate locations.
- Task B: Dijkstra shortest paths on an undirected weighted graph.
- Task C: Java OOP design using model, sorting, graph, service, and project packages.
- Optional JavaFX visualizer for PPT/video demonstration.

The console entry point is still `project.Main`.

## Actual Project Structure

```text
src/
├── graph/
│   ├── Dijkstra.java
│   ├── Edge.java
│   ├── Graph.java
│   ├── LocationGraphBuilder.java
│   ├── StringGraph.java
│   ├── UnweightedGraph.java
│   ├── WeightedEdge.java
│   └── WeightedGraph.java
├── model/
│   ├── BenchmarkResult.java
│   ├── CandidateLocation.java
│   ├── PathCaseResult.java
│   ├── PathResult.java
│   ├── SortingResult.java
│   └── TaskBResult.java
├── project/
│   ├── Application.java
│   ├── Main.java
│   ├── ValidationRunner.java
│   └── Test*.java
├── service/
│   ├── TaskAService.java
│   └── TaskBService.java
├── sorting/
│   ├── BubbleSorter.java
│   ├── CandidateComparator.java
│   ├── MergeSorter.java
│   ├── PerformanceTester.java
│   ├── QuickSorter.java
│   └── Sorter.java
└── util/
    └── CSVReader.java

data/
├── candidates_A.csv
├── candidates_B.csv
├── candidates_C.csv
└── paths.csv

optional-visualization/
└── visualization/
    ├── InspectionSystemFXApp.java
    └── VisualizerStyle.css
```

`optional-visualization` is kept outside `src` so that the normal console compile command does not require JavaFX SDK.

## Console Compile and Run

From the project root:

```bash
javac -d out $(find src -name "*.java")
java -cp out project.Main
```

Validation runner:

```bash
java -cp out project.ValidationRunner
```

The validation runner checks:

- All three sorting algorithms produce sorted output.
- Bubble, Quick, and Merge produce the same ordering for each dataset.
- Task B produces finite, non-empty paths for the four required cases.

## Optional JavaFX Compile and Run

This visualizer is for demonstration only. It does not replace `project.Main`.

For this machine, JavaFX SDK is expected at:

```text
/Users/sunjiafei/javafx-sdk-21.0.2
```

Compile console code first:

```bash
javac -d out $(find src -name "*.java")
```

Compile JavaFX optional source:

```bash
javac --module-path /Users/sunjiafei/javafx-sdk-21.0.2/lib --add-modules javafx.controls -cp out -d out $(find optional-visualization -name "*.java")
```

Run JavaFX visualizer:

```bash
java --module-path /Users/sunjiafei/javafx-sdk-21.0.2/lib --add-modules javafx.controls -cp out visualization.InspectionSystemFXApp
```

IntelliJ IDEA setup:

1. Project Structure -> Libraries -> add `/Users/sunjiafei/javafx-sdk-21.0.2/lib`.
2. Create a run configuration for `visualization.InspectionSystemFXApp`.
3. Add VM options:

```text
--module-path /Users/sunjiafei/javafx-sdk-21.0.2/lib --add-modules javafx.controls
```

## Task A Sorting Rule

All sorting algorithms use the same rule in `sorting.CandidateComparator`:

1. Higher `priority_score` first.
2. If scores are equal, smaller `location_id` first.

The algorithms are implemented manually:

- `BubbleSorter`: adjacent comparisons with `needNextPass` early stop.
- `QuickSorter`: first-element pivot partition style.
- `MergeSorter`: recursive divide and merge.

The main program does not use `Collections.sort()`, `List.sort()`, or stream sorting for Task A.

## Task B Graph Rule

`paths.csv` is read as an undirected weighted graph.

The production Task B workflow uses:

- `StringGraph`: adjacency list keyed by location IDs such as `L0001`.
- `LocationGraphBuilder`: builds the graph from CSV.
- `Dijkstra`: shortest path using `PriorityQueue`.
- `TaskBService`: runs the four required cases.

`Graph`, `UnweightedGraph`, and `WeightedGraph` are kept because they match the teacher's Week 10 graph style and support the provided demo test classes. The Task B project workflow uses `StringGraph` because the CSV vertices are already meaningful string location IDs.

## OOP Notes for Report

- Encapsulation: model fields are private/final where practical; path and graph collection getters return defensive copies or unmodifiable views.
- Abstraction: `Sorter` hides the sorting algorithm behind one interface.
- Polymorphism: `TaskAService` and `PerformanceTester` work with `List<Sorter>` containing `BubbleSorter`, `QuickSorter`, and `MergeSorter`.
- Inheritance: `WeightedGraph` extends `UnweightedGraph`, and `WeightedEdge` extends `Edge`.
- Reuse: console output and optional JavaFX visualizer both call the same services instead of duplicating algorithms.

## Important Restrictions

- Do not use Swing.
- Do not use third-party sorting, graph, or visualization libraries.
- Do not hardcode Top 10 or shortest path results.
- Do not edit the CSV data files.
- Do not remove `project.Main`.
- Do not convert the project to Maven or Gradle unless the module leader requires it.
