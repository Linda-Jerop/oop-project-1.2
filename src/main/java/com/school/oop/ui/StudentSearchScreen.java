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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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

    Runnable updateButton = () ->
            loginButton.setDisable(username.getText().isBlank()
                    || password.getText().isBlank());
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
        grid.add(createDayLabel(), 0, 0);
        grid.add(createDayCombo(), 1, 0);
        grid.add(createTimeLabel(), 2, 0);
        grid.add(createTimeField(), 3, 0);
        grid.add(createCapacityLabel(), 0, 1);
        grid.add(createCapacityField(), 1, 1);
        grid.add(createRoomTypeLabel(), 2, 1);
        grid.add(createRoomTypeCombo(), 3, 1);
    }

    // This method adds the right side filter controls; and connects each button to a room search.
private void addRightFilters(GridPane grid) {
    grid.add(createBuildingLabel(), 4, 0);
    grid.add(createBuildingCombo(), 5, 0);

    Button freeNowButton = createButton("Free Now");
    freeNowButton.setOnAction(event ->
            resultsTable.setItems(FXCollections.observableArrayList(
                    roomFinder.findFreeRoomsNow())));
    grid.add(freeNowButton, 4, 1);

    Button searchButton = createButton("Search");
    searchButton.setOnAction(event -> searchRooms());
    grid.add(searchButton, 5, 1);
}

// Validate the filters, search for rooms, and display the results.
private void searchRooms() {
    if (dayCombo.getValue() == null || timeField.getText().isBlank()) {
        new Alert(Alert.AlertType.WARNING, "Select a day and enter a time.").showAndWait();
        return;
    }

    try {
        DayOfWeek day = DayOfWeek.valueOf(dayCombo.getValue().toUpperCase(Locale.ROOT));
        LocalTime time = LocalTime.parse(timeField.getText().trim());
        int minimumCapacity = capacityField.getText().isBlank()
                ? 0
                : Integer.parseInt(capacityField.getText().trim());

        String building = buildingCombo.getValue();
        if (building == null || "Any".equals(building)) {
            building = "";
        }

        String roomType = roomTypeCombo.getValue();
        if (roomType == null || "Any".equals(roomType)) {
            roomType = "";
        }

        resultsTable.setItems(FXCollections.observableArrayList(
                roomFinder.findFreeRooms(day, time, building, minimumCapacity, roomType)));
    } catch (DateTimeParseException | NumberFormatException ex) {
        new Alert(Alert.AlertType.ERROR,
                "Enter time as HH:mm and capacity as a whole number.").showAndWait();
    }
}

    // This method creates the label for the day field.
    private Label createDayLabel() {
        return new Label("Day");
    }

// Configure and return the day filter used by the search handler.
private ComboBox<String> createDayCombo() {
    dayCombo.setPromptText("Select day");
    dayCombo.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
    return dayCombo;
}

    // This method creates the label for the time field.
    private Label createTimeLabel() {
        return new Label("Time");
    }

    // This method creates the label for the time field and return the time filter used by the search handler.
private TextField createTimeField() {
    timeField.setPromptText("e.g. 14:30");
    return timeField;
}

    // This method creates the label for the building field.
    private Label createBuildingLabel() {
        return new Label("Building");
    }

    // This method creates the building dropdown.
    private ComboBox<String> createBuildingCombo() {
    buildingCombo.setPromptText("Any");
    buildingCombo.getItems().addAll("Any", "Main Building", "Science Block", "Engineering Block");
    return buildingCombo;
}

    // This method creates the label for the capacity field.
    private Label createCapacityLabel() {
        return new Label("Min Capacity");
    }

    // This method creates the capacity input box and return the capacity filter used by the search handler.
    private TextField createCapacityField() {
    capacityField.setPromptText("20");
    return capacityField;
}

    // This method creates the label for the room type.
    private Label createRoomTypeLabel() {
        return new Label("Room Type");
    }

    // This method creates the room type dropdown and return the room-type filter used by the search handler.
private ComboBox<String> createRoomTypeCombo() {
    roomTypeCombo.setPromptText("Any");
    roomTypeCombo.getItems().addAll("Any", "Lecture", "Lab", "Lecture Theatre");
    return roomTypeCombo;
}

    // This method creates a simple button.
    private Button createButton(String text) {
        Button button = new Button(text);
        return button;
    }

    // This method creates the table showing rooms.
    // Defines how each result supplies the value shown in each table column.
private TableView<RoomResult> createTable() {
    TableColumn<RoomResult, String> roomColumn = new TableColumn<>("Room");
    roomColumn.setCellValueFactory(cell ->
            new ReadOnlyStringWrapper(cell.getValue().getRoom().getRoomId()));

    TableColumn<RoomResult, String> buildingColumn = new TableColumn<>("Building");
    buildingColumn.setCellValueFactory(cell ->
            new ReadOnlyStringWrapper(cell.getValue().getRoom().getBuilding()));

    TableColumn<RoomResult, String> capacityColumn = new TableColumn<>("Capacity");
    capacityColumn.setCellValueFactory(cell ->
            new ReadOnlyStringWrapper(
                    Integer.toString(cell.getValue().getRoom().getCapacity())));

    TableColumn<RoomResult, String> typeColumn = new TableColumn<>("Type");
    typeColumn.setCellValueFactory(cell ->
            new ReadOnlyStringWrapper(cell.getValue().getRoom().getType()));

    TableColumn<RoomResult, String> freeUntilColumn = new TableColumn<>("Free Until");
    freeUntilColumn.setCellValueFactory(cell ->
            new ReadOnlyStringWrapper(cell.getValue().getFreeUntil()));

    resultsTable.getColumns().addAll(
            roomColumn, buildingColumn, capacityColumn, typeColumn, freeUntilColumn);
    resultsTable.setPlaceholder(new Label("No rooms available yet"));
    return resultsTable;
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
