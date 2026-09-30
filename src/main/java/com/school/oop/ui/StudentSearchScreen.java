package com.school.oop.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StudentSearchScreen extends BorderPane {
    private Scene scene;

    // This constructor creates the screen and sets its size.
    public StudentSearchScreen() {
        buildScreen();
        this.scene = new Scene(this, 900, 600);
    }

    // This method builds the full layout of the student search page.
    private void buildScreen() {
        setTop(createTopBar());
        setCenter(createCenterArea());
        setBottom(createStatusLabel());
    }

    // This method creates the bar with the title and the login button.
    private HBox createTopBar() {
        Label title = new Label("Campus Empty Classroom Finder");
        Button loginButton = new Button("Lecturer Login");
        HBox bar = new HBox();
        bar.setPadding(new Insets(15));
        bar.setSpacing(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.getChildren().addAll(title, loginButton);
        return bar;
    }

    // This method creates the center section with filters and a table.
    private VBox createCenterArea() {
        VBox area = new VBox();
        area.setPadding(new Insets(10));
        area.setSpacing(10);
        area.getChildren().add(createFilterGrid());
        area.getChildren().add(createTable());
        return area;
    }

    // This method creates the grid with the search filters.
    private GridPane createFilterGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        addLeftFilters(grid);
        addRightFilters(grid);
        return grid;
    }

    // This method adds the left side filter controls.
    private void addLeftFilters(GridPane grid) {
        grid.add(createDayLabel(), 0, 0);
        grid.add(createDayCombo(), 1, 0);
        grid.add(createTimeLabel(), 2, 0);
        grid.add(createTimeField(), 3, 0);
        grid.add(createCapacityLabel(), 0, 1);
        grid.add(createCapacityField(), 1, 1);
        grid.add(createRoomTypeLabel(), 2, 1);
        grid.add(createRoomTypeCombo(), 3, 1);
    }

    // This method adds the right side filter controls.
    private void addRightFilters(GridPane grid) {
        grid.add(createBuildingLabel(), 4, 0);
        grid.add(createBuildingCombo(), 5, 0);
        grid.add(createButton("Free Now"), 4, 1);
        grid.add(createButton("Search"), 5, 1);
    }

    // This method creates the label for the day field.
    private Label createDayLabel() {
        return new Label("Day");
    }

    // This method creates the day dropdown.
    private ComboBox<String> createDayCombo() {
        ComboBox<String> combo = new ComboBox<String>();
        combo.setPromptText("Select day");
        combo.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        return combo;
    }

    // This method creates the label for the time field.
    private Label createTimeLabel() {
        return new Label("Time");
    }

    // This method creates the time input box.
    private TextField createTimeField() {
        TextField field = new TextField();
        field.setPromptText("e.g. 14:30");
        return field;
    }

    // This method creates the label for the building field.
    private Label createBuildingLabel() {
        return new Label("Building");
    }

    // This method creates the building dropdown.
    private ComboBox<String> createBuildingCombo() {
        ComboBox<String> combo = new ComboBox<String>();
        combo.setPromptText("Any");
        combo.getItems().addAll("Any", "Main Building", "Science Block", "Engineering Block");
        return combo;
    }

    // This method creates the label for the capacity field.
    private Label createCapacityLabel() {
        return new Label("Min Capacity");
    }

    // This method creates the capacity input box.
    private TextField createCapacityField() {
        TextField field = new TextField();
        field.setPromptText("20");
        return field;
    }

    // This method creates the label for the room type.
    private Label createRoomTypeLabel() {
        return new Label("Room Type");
    }

    // This method creates the room type dropdown.
    private ComboBox<String> createRoomTypeCombo() {
        ComboBox<String> combo = new ComboBox<String>();
        combo.setPromptText("Any");
        combo.getItems().addAll("Any", "Lecture", "Lab", "Lecture Theatre");
        return combo;
    }

    // This method creates a simple button.
    private Button createButton(String text) {
        Button button = new Button(text);
        return button;
    }

    // This method creates the table showing rooms.
    private TableView<Object> createTable() {
        TableView<Object> table = new TableView<Object>();
        table.getColumns().add(new TableColumn<Object, String>("Room"));
        table.getColumns().add(new TableColumn<Object, String>("Building"));
        table.getColumns().add(new TableColumn<Object, String>("Capacity"));
        table.getColumns().add(new TableColumn<Object, String>("Type"));
        table.getColumns().add(new TableColumn<Object, String>("Free Until"));
        table.setPlaceholder(new Label("No rooms available yet"));
        return table;
    }

    // This method creates the status label at the bottom.
    private Label createStatusLabel() {
        Label label = new Label("Waiting for search");
        label.setPadding(new Insets(10));
        return label;
    }

    // This method returns the whole scene for the screen.
    public Scene createScene() {
        return scene;
    }
}
