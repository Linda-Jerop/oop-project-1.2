package com.school.oop.ui;

import com.school.oop.logic.AuthService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;

// Imports for reading filters and populating the results table.
import com.school.oop.logic.RoomFinder;
import com.school.oop.logic.RoomResult;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StudentSearchScreen extends BorderPane {
    private Scene scene;

// References to the filters, finder, and table so the Search button can use them.
private final RoomFinder roomFinder = new RoomFinder();
private final TableView<RoomResult> resultsTable = new TableView<>();
private final ComboBox<String> dayCombo = new ComboBox<>();
private final TextField timeField = new TextField();
private final TextField capacityField = new TextField();
private final ComboBox<String> roomTypeCombo = new ComboBox<>();
private final ComboBox<String> buildingCombo = new ComboBox<>();

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
        Button loginButton = new Button("Stratizen Login");

        loginButton.setOnAction(event -> showLoginDialog());

        HBox bar = new HBox();
        bar.setPadding(new Insets(15));
        bar.setSpacing(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.getChildren().addAll(title, loginButton);
        return bar;
    }
        
    private void showLoginDialog() {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Stratizen Login");
    dialog.setHeaderText("Enter your Stratizen username and password");
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    TextField username = new TextField();
    username.setPromptText("Username");

    PasswordField password = new PasswordField();
    password.setPromptText("Password");

    GridPane form = new GridPane();
    form.setHgap(10);
    form.setVgap(10);
    form.add(new Label("Username:"), 0, 0);
    form.add(username, 1, 0);
    form.add(new Label("Password:"), 0, 1);
    form.add(password, 1, 1);
    dialog.getDialogPane().setContent(form);

    var loginButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
    loginButton.setDisable(true);

    Runnable updateButton = () -> loginButton.setDisable(username.getText().isBlank());
    username.textProperty().addListener((obs, oldValue, newValue) -> updateButton.run());
    password.textProperty().addListener((obs, oldValue, newValue) -> updateButton.run());

    dialog.showAndWait().ifPresent(result -> {
        if (result == ButtonType.OK) {
            AuthService authService = new AuthService();
            Alert.AlertType type = authService.login(username.getText().trim(), password.getText())
                    ? Alert.AlertType.INFORMATION
                    : Alert.AlertType.ERROR;
            String message = type == Alert.AlertType.INFORMATION
                    ? "Login successful."
                    : "Invalid username or password.";

            Alert alert = new Alert(type, message);
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    });
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
        grid.add(createRoomTypeLabel(), 0, 0);
        grid.add(createRoomTypeCombo(), 1, 0);
        grid.add(createCapacityLabel(), 0, 1);
        grid.add(createCapacityField(), 1, 1);
    }

    // This method adds the right side filter controls; and connects each button to a room search.
    private void addRightFilters(GridPane grid) {
        grid.add(createBuildingLabel(), 2, 0);
        grid.add(createBuildingCombo(), 3, 0);

        Button searchButton = createButton("Search Rooms");
        searchButton.setOnAction(event -> searchRooms());
        grid.add(searchButton, 3, 1);
    }

    // Validate inventory filters and show matching rooms without claiming schedule availability.
    private void searchRooms() {
        int minimumCapacity;
        try {
            minimumCapacity = capacityField.getText().isBlank()
                    ? 1
                    : Integer.parseInt(capacityField.getText().trim());
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Enter a whole-number capacity.").showAndWait();
            return;
        }

        String roomType = roomTypeCombo.getValue();
        int maximumCapacity = "Lab".equals(roomType) ? 42 : 63;
        if (minimumCapacity < 1 || minimumCapacity > maximumCapacity) {
            new Alert(Alert.AlertType.WARNING,
                    "Capacity must be between 1 and " + maximumCapacity
                            + " for the selected room type.").showAndWait();
            return;
        }

        String building = buildingCombo.getValue();
        if (building == null || "Any building".equals(building)) {
            building = "";
        }
        if ("Either".equals(roomType)) {
            roomType = "";
        }

        resultsTable.setItems(FXCollections.observableArrayList(
                roomFinder.searchRooms(building, minimumCapacity, roomType)));
    }

    // This method creates the label for the building field.
    private Label createBuildingLabel() {
        return new Label("Building");
    }

    // This method creates the building dropdown.
    private ComboBox<String> createBuildingCombo() {
        buildingCombo.getItems().addAll(
                "Any building", "Oval Building", "STMB", "STC", "SBS", "MSB",
                "Central Building", "Library");
        buildingCombo.setValue("Any building");
        return buildingCombo;
    }

    // This method creates the label for the capacity field.
    private Label createCapacityLabel() {
        return new Label("Min Capacity");
    }

    // This method creates the capacity input box and return the capacity filter used by the search handler.
    private TextField createCapacityField() {
        capacityField.setPromptText("1 to 63");
        capacityField.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().matches("\\d*") ? change : null));
        return capacityField;
    }

    // This method creates the label for the room type.
    private Label createRoomTypeLabel() {
        return new Label("Room Type");
    }

    // This method creates the room type dropdown and return the room-type filter used by the search handler.
    private ComboBox<String> createRoomTypeCombo() {
        roomTypeCombo.getItems().addAll("Lab", "Classroom", "Either");
        roomTypeCombo.setValue("Either");
        roomTypeCombo.valueProperty().addListener((observable, oldValue, newValue) ->
                capacityField.setPromptText("1 to " + ("Lab".equals(newValue) ? 42 : 63)));
        return roomTypeCombo;
    }

    // This method creates a simple button.
    private Button createButton(String text) {
        Button button = new Button(text);
        return button;
    }

    // Define how each room result supplies the value shown in the table.
    private TableView<RoomResult> createTable() {
        TableColumn<RoomResult, String> roomColumn = new TableColumn<>("Room");
        roomColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getRoom().getRoomId()));

        TableColumn<RoomResult, String> buildingColumn = new TableColumn<>("Building");
        buildingColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getRoom().getBuilding()));

        TableColumn<RoomResult, String> capacityColumn = new TableColumn<>("Max Capacity");
        capacityColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        Integer.toString(cell.getValue().getRoom().getCapacity())));

        TableColumn<RoomResult, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getRoom().getType()));

        TableColumn<RoomResult, String> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getAvailability()));

        resultsTable.getColumns().addAll(
                roomColumn, buildingColumn, capacityColumn, typeColumn, availabilityColumn);
        resultsTable.setPlaceholder(new Label("No rooms match these filters"));
        return resultsTable;
    }

    // This method creates the status label at the bottom.
    private Label createStatusLabel() {
        Label label = new Label(
                "Room inventory is local sample data; live availability is not connected yet.");
        label.setPadding(new Insets(10));
        return label;
    }

    // This method returns the whole scene for the screen.
    public Scene createScene() {
        return scene;
    }
}
