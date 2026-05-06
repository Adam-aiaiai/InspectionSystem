package visualization;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CandidateLocation;
import model.PathCaseResult;
import model.SortingResult;
import model.TaskBResult;
import service.TaskAService;
import service.TaskBService;
import sorting.Sorter;
import util.CSVReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Optional JavaFX visualizer. The console entry point remains project.Main.
 */
public class InspectionSystemFXApp extends Application {
    private static final String DATA_PATH = "data/";
    private static final int TOP_N = 10;
    private static final int RUNS = 3;

    private Map<String, SortingResult> sortingResults;
    private TaskBResult taskBResult;
    private List<Sorter> sorters;

    private final Canvas sortingCanvas = new Canvas(620, 360);
    private final Canvas graphCanvas = new Canvas(680, 420);
    private final TextArea sortingStatus = new TextArea();
    private final TextArea graphStatus = new TextArea();
    private Timeline timeline;

    @Override
    public void start(Stage stage) throws Exception {
        TaskAService taskAService = new TaskAService(DATA_PATH, TOP_N, RUNS);
        sortingResults = taskAService.evaluateDatasets();
        sorters = taskAService.getSorters();
        taskBResult = new TaskBService(DATA_PATH).evaluateCases(sortingResults);

        TabPane tabs = new TabPane();
        tabs.getTabs().add(new Tab("Task A - Sorting Visualizer", createSortingTab()));
        tabs.getTabs().add(new Tab("Task B - Shortest Path Visualizer", createGraphTab()));
        tabs.getTabs().forEach(tab -> tab.setClosable(false));

        Scene scene = new Scene(tabs, 1180, 740);
        stage.setTitle("Urban Infrastructure Inspection System - Optional JavaFX Visualizer");
        stage.setScene(scene);
        stage.show();

        drawInitialSortingBars("Dataset A");
        drawPath(taskBResult.getCaseResults().get(0));
    }

    private BorderPane createSortingTab() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(14));

        ComboBox<String> datasetBox = new ComboBox<>();
        datasetBox.getItems().addAll("Dataset A", "Dataset B", "Dataset C");
        datasetBox.setValue("Dataset A");

        ComboBox<String> algorithmBox = new ComboBox<>();
        for (Sorter sorter : sorters) {
            algorithmBox.getItems().add(sorter.getName());
        }
        algorithmBox.setValue(sorters.get(0).getName());

        Slider speedSlider = new Slider(1, 10, 5);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button nextButton = new Button("Next Step");
        Button resetButton = new Button("Reset");
        Button benchmarkButton = new Button("Run Benchmark");

        VBox controls = new VBox(10,
                new Label("Dataset"), datasetBox,
                new Label("Algorithm"), algorithmBox,
                new Label("Speed"), speedSlider,
                new HBox(8, startButton, pauseButton),
                new HBox(8, nextButton, resetButton),
                benchmarkButton);
        controls.setPadding(new Insets(0, 14, 0, 0));
        controls.setPrefWidth(220);

        TableView<CandidateLocation> topTable = createTopTable();
        TableView<SortingResult> benchmarkTable = createBenchmarkTable();
        VBox rightPanel = new VBox(12, new Label("Top 10 Locations"), topTable,
                new Label("Runtime Comparison"), benchmarkTable);
        rightPanel.setPrefWidth(330);

        sortingStatus.setEditable(false);
        sortingStatus.setPrefRowCount(5);
        sortingStatus.setText("Ranking rule: priority_score descending, then location_id ascending.\n"
                + "Visualizer displays a 30-item sample; Top 10 and benchmark use the full dataset.");

        VBox center = new VBox(10, sortingCanvas, sortingStatus);
        VBox.setVgrow(sortingStatus, Priority.ALWAYS);

        datasetBox.setOnAction(event -> {
            updateTopTable(topTable, datasetBox.getValue());
            drawInitialSortingBars(datasetBox.getValue());
        });
        startButton.setOnAction(event -> runSortingVisualization(datasetBox.getValue(), algorithmBox.getValue(), speedSlider.getValue()));
        pauseButton.setOnAction(event -> {
            if (timeline != null) {
                timeline.pause();
            }
            sortingStatus.appendText("\nAnimation paused.");
        });
        nextButton.setOnAction(event -> runSortingVisualization(datasetBox.getValue(), algorithmBox.getValue(), 10));
        resetButton.setOnAction(event -> drawInitialSortingBars(datasetBox.getValue()));
        benchmarkButton.setOnAction(event -> benchmarkTable.getItems().setAll(sortingResults.values()));

        updateTopTable(topTable, "Dataset A");
        benchmarkTable.getItems().setAll(sortingResults.values());

        root.setLeft(controls);
        root.setCenter(center);
        root.setRight(rightPanel);
        return root;
    }

    private BorderPane createGraphTab() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(14));

        ComboBox<String> caseBox = new ComboBox<>();
        for (PathCaseResult caseResult : taskBResult.getCaseResults()) {
            caseBox.getItems().add(caseResult.getCaseName());
        }
        caseBox.setValue("Case 1");

        Button runButton = new Button("Run Dijkstra");
        Button animateButton = new Button("Animate Path");
        Button resetButton = new Button("Reset");

        VBox controls = new VBox(10,
                new Label("Case"), caseBox,
                runButton, animateButton, resetButton);
        controls.setPadding(new Insets(0, 14, 0, 0));
        controls.setPrefWidth(220);

        TableView<PathCaseResult> pathTable = createPathTable();
        pathTable.getItems().setAll(taskBResult.getCaseResults());
        VBox rightPanel = new VBox(12, new Label("Path Result Table"), pathTable);
        rightPanel.setPrefWidth(380);

        graphStatus.setEditable(false);
        graphStatus.setPrefRowCount(7);
        VBox center = new VBox(10, graphCanvas, graphStatus);
        VBox.setVgrow(graphStatus, Priority.ALWAYS);

        runButton.setOnAction(event -> drawPath(findCase(caseBox.getValue())));
        animateButton.setOnAction(event -> animatePath(findCase(caseBox.getValue())));
        resetButton.setOnAction(event -> drawPath(taskBResult.getCaseResults().get(0)));

        root.setLeft(controls);
        root.setCenter(center);
        root.setRight(rightPanel);
        return root;
    }

    private TableView<CandidateLocation> createTopTable() {
        TableView<CandidateLocation> table = new TableView<>();
        table.setPrefHeight(250);

        TableColumn<CandidateLocation, String> idColumn = new TableColumn<>("Location");
        idColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLocationId()));
        idColumn.setPrefWidth(110);

        TableColumn<CandidateLocation, String> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getPriorityScore())));
        scoreColumn.setPrefWidth(90);

        table.getColumns().add(idColumn);
        table.getColumns().add(scoreColumn);
        return table;
    }

    private TableView<SortingResult> createBenchmarkTable() {
        TableView<SortingResult> table = new TableView<>();
        table.setPrefHeight(210);

        TableColumn<SortingResult, String> datasetColumn = new TableColumn<>("Dataset");
        datasetColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDatasetName()));
        datasetColumn.setPrefWidth(90);
        table.getColumns().add(datasetColumn);

        for (Sorter sorter : sorters) {
            TableColumn<SortingResult, String> column = new TableColumn<>(sorter.getName());
            column.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                    String.valueOf(data.getValue().getBenchmarkResult().getAverageRuntimeNanos(sorter.getName()))));
            column.setPrefWidth(90);
            table.getColumns().add(column);
        }

        return table;
    }

    private TableView<PathCaseResult> createPathTable() {
        TableView<PathCaseResult> table = new TableView<>();

        TableColumn<PathCaseResult, String> caseColumn = new TableColumn<>("Case");
        caseColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCaseName()));
        caseColumn.setPrefWidth(70);

        TableColumn<PathCaseResult, String> routeColumn = new TableColumn<>("Route");
        routeColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getStart() + " -> " + data.getValue().getDestination()));
        routeColumn.setPrefWidth(120);

        TableColumn<PathCaseResult, String> waypointColumn = new TableColumn<>("Waypoints");
        waypointColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getWaypointText()));
        waypointColumn.setPrefWidth(120);

        TableColumn<PathCaseResult, String> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                String.valueOf(data.getValue().getPathResult().getTotalCost())));
        costColumn.setPrefWidth(70);

        table.getColumns().add(caseColumn);
        table.getColumns().add(routeColumn);
        table.getColumns().add(waypointColumn);
        table.getColumns().add(costColumn);
        return table;
    }

    private void updateTopTable(TableView<CandidateLocation> table, String datasetName) {
        table.getItems().setAll(sortingResults.get(datasetName).getTopLocations());
    }

    private void drawInitialSortingBars(String datasetName) {
        List<CandidateLocation> sample = sampleLocations(datasetName);
        drawBars(sample, -1, -1);
        sortingStatus.setText("Loaded " + datasetName + ". Showing the first 30 source rows as sample bars.");
    }

    private void runSortingVisualization(String datasetName, String algorithmName, double speed) {
        List<CandidateLocation> sample = sampleLocations(datasetName);
        Sorter sorter = findSorter(algorithmName);
        sorter.sort(sample);
        drawBars(sample, 0, Math.min(1, sample.size() - 1));
        sortingStatus.setText(algorithmName + " completed on the displayed sample.\n"
                + "Full benchmark and Top 10 are computed through TaskAService using all 1000 rows.\n"
                + "Speed setting: " + String.format("%.1f", speed));
    }

    private List<CandidateLocation> sampleLocations(String datasetName) {
        try {
            List<CandidateLocation> candidates = CSVReader.readCandidates(DATA_PATH + fileNameForDataset(datasetName));
            return new ArrayList<>(candidates.subList(0, Math.min(30, candidates.size())));
        } catch (Exception e) {
            return sortingResults.get(datasetName).getTopLocations();
        }
    }

    private String fileNameForDataset(String datasetName) {
        if ("Dataset A".equals(datasetName)) {
            return "candidates_A.csv";
        }
        if ("Dataset B".equals(datasetName)) {
            return "candidates_B.csv";
        }
        return "candidates_C.csv";
    }

    private Sorter findSorter(String algorithmName) {
        for (Sorter sorter : sorters) {
            if (sorter.getName().equals(algorithmName)) {
                return sorter;
            }
        }
        return sorters.get(0);
    }

    private void drawBars(List<CandidateLocation> locations, int highlightA, int highlightB) {
        GraphicsContext gc = sortingCanvas.getGraphicsContext2D();
        gc.setFill(Color.web("#f7f8fb"));
        gc.fillRect(0, 0, sortingCanvas.getWidth(), sortingCanvas.getHeight());

        double width = sortingCanvas.getWidth() / Math.max(1, locations.size());
        int maxScore = 1;
        for (CandidateLocation location : locations) {
            maxScore = Math.max(maxScore, location.getPriorityScore());
        }

        for (int i = 0; i < locations.size(); i++) {
            double barHeight = (locations.get(i).getPriorityScore() / (double) maxScore) * 300;
            if (i == highlightA || i == highlightB) {
                gc.setFill(Color.web("#e94f37"));
            } else {
                gc.setFill(Color.web("#2f80ed"));
            }
            gc.fillRoundRect(i * width + 4, 330 - barHeight, width - 8, barHeight, 4, 4);
            gc.setFill(Color.web("#1f2933"));
            gc.fillText(locations.get(i).getLocationId(), i * width + 2, 350);
        }
    }

    private PathCaseResult findCase(String caseName) {
        for (PathCaseResult caseResult : taskBResult.getCaseResults()) {
            if (caseResult.getCaseName().equals(caseName)) {
                return caseResult;
            }
        }
        return taskBResult.getCaseResults().get(0);
    }

    private void animatePath(PathCaseResult caseResult) {
        drawPath(caseResult);
        graphStatus.appendText("\nAnimation note: path nodes are highlighted in order from start to destination.");
    }

    private void drawPath(PathCaseResult caseResult) {
        List<String> path = caseResult.getPathResult().getPath();
        GraphicsContext gc = graphCanvas.getGraphicsContext2D();
        gc.setFill(Color.web("#f7f8fb"));
        gc.fillRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());

        if (path.isEmpty()) {
            graphStatus.setText("No path found.");
            return;
        }

        double startX = 60;
        double endX = graphCanvas.getWidth() - 60;
        double y = graphCanvas.getHeight() / 2;
        double gap = path.size() == 1 ? 0 : (endX - startX) / (path.size() - 1);

        gc.setStroke(Color.web("#8b95a1"));
        gc.setLineWidth(3);
        for (int i = 0; i < path.size() - 1; i++) {
            double x1 = startX + i * gap;
            double x2 = startX + (i + 1) * gap;
            gc.strokeLine(x1, y, x2, y);
        }

        for (int i = 0; i < path.size(); i++) {
            double x = startX + i * gap;
            String node = path.get(i);
            if (node.equals(caseResult.getStart())) {
                gc.setFill(Color.web("#1f7a4d"));
            } else if (node.equals(caseResult.getDestination())) {
                gc.setFill(Color.web("#b83232"));
            } else if (caseResult.getWaypoints().contains(node)) {
                gc.setFill(Color.web("#8e44ad"));
            } else {
                gc.setFill(Color.web("#2f80ed"));
            }
            gc.fillOval(x - 13, y - 13, 26, 26);
            gc.setFill(Color.web("#1f2933"));
            gc.fillText(node, x - 18, y + 34);
        }

        graphStatus.setText(caseResult.getCaseName() + "\n"
                + "Start: " + caseResult.getStart() + "\n"
                + "Destination: " + caseResult.getDestination() + "\n"
                + "Waypoints: " + caseResult.getWaypointText() + "\n"
                + "Path: " + caseResult.getPathResult().getPathAsString() + "\n"
                + "Total cost: " + caseResult.getPathResult().getTotalCost() + "\n"
                + "Layout: abstract network path, not a real city map.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
