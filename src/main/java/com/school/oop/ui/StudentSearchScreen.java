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

    public StudentSearchScreen() {
        buildScreen();
        this.scene = new Scene(this, 900, 600);
    }

    private void buildScreen() {
        Label titleLabel = new Label("Campus Empty Classroom Finder");
        Button loginButton = new Button("Lecturer Login");
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox topRight = new HBox(loginButton);
        topRight.setAlignment(Pos.CENTER_RIGHT);
        topRight.setPrefWidth(180);
        topBar.getChildren().addAll(titleLabel, topRight);
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);
        setTop(topBar);

        GridPane filterGrid = new GridPane();
        filterGrid.setHgap(10);
        filterGrid.setVgap(10);
        filterGrid.setPadding(new Insets(10));

        Label dayLabel = new Label("Day");
        ComboBox<String> dayCombo = new ComboBox<String>();
        dayCombo.setPromptText("Select day");
        dayCombo.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

        Label timeLabel = new Label("Time");
        TextField timeField = new TextField();
        timeField.setPromptText("e.g. 14:30");

        Label buildingLabel = new Label("Building");
        ComboBox<String> buildingCombo = new ComboBox<String>();
        buildingCombo.setPromptText("Any");
        buildingCombo.getItems().addAll("Any", "Main Building", "Science Block", "Engineering Block");

        Label capacityLabel = new Label("Min Capacity");
        TextField capacityField = new TextField();
        capacityField.setPromptText("20");

        Label roomTypeLabel = new Label("Room Type");
        ComboBox<String> roomTypeCombo = new ComboBox<String>();
        roomTypeCombo.setPromptText("Any");
        roomTypeCombo.getItems().addAll("Any", "Lecture", "Lab", "Lecture Theatre");

        Button freeNowButton = new Button("Free Now");
        Button searchButton = new Button("Search");

        filterGrid.add(dayLabel, 0, 0);
        filterGrid.add(dayCombo, 1, 0);
        filterGrid.add(timeLabel, 2, 0);
        filterGrid.add(timeField, 3, 0);
        filterGrid.add(buildingLabel, 4, 0);
        filterGrid.add(buildingCombo, 5, 0);
        filterGrid.add(capacityLabel, 0, 1);
        filterGrid.add(capacityField, 1, 1);
        filterGrid.add(roomTypeLabel, 2, 1);
        filterGrid.add(roomTypeCombo, 3, 1);
        filterGrid.add(freeNowButton, 4, 1);
        filterGrid.add(searchButton, 5, 1);

        VBox centerBox = new VBox();
        centerBox.setPadding(new Insets(10));
        centerBox.setSpacing(10);
        centerBox.getChildren().add(filterGrid);

        TableView<Object> tableView = new TableView<Object>();
        TableColumn<Object, String> roomColumn = new TableColumn<Object, String>("Room");
        TableColumn<Object, String> buildingColumn = new TableColumn<Object, String>("Building");
        TableColumn<Object, String> capacityColumn = new TableColumn<Object, String>("Capacity");
        TableColumn<Object, String> typeColumn = new TableColumn<Object, String>("Type");
        TableColumn<Object, String> freeUntilColumn = new TableColumn<Object, String>("Free Until");

        tableView.getColumns().addAll(roomColumn, buildingColumn, capacityColumn, typeColumn, freeUntilColumn);
        tableView.setPlaceholder(new Label("No rooms available yet"));
        centerBox.getChildren().add(tableView);
        setCenter(centerBox);

        Label statusLabel = new Label("Waiting for search");
        statusLabel.setPadding(new Insets(10));
        setBottom(statusLabel);
    }

    public Scene getScene() {
        return scene;
    }
}
