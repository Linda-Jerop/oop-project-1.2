package com.school.oop.ui;

import com.school.oop.UserSession;
import com.school.oop.dao.RoomDAO;
import com.school.oop.dao.TimetableDAO;
import com.school.oop.model.Room;
import com.school.oop.model.TimetableEntry;
import com.school.oop.model.TimetableOptions;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// StratizenController belongs to Stratizen.fxml. This screen is for lecturers, class reps and students.
// It has a "Find a Room" tab (search rooms, and book one if you are allowed) and, for lecturers and
// class reps only, a "My Bookings" tab. There is NO timetable tab: only the admin can see timetables.
public class StratizenController {

    // ----- Things from Stratizen.fxml. Each name matches an fx:id in the FXML file. -----
    @FXML
    private Label roleLabel;
    @FXML
    private ComboBox<String> dayCombo;
    @FXML
    private TextField timeField;
    @FXML
    private ComboBox<String> buildingCombo;
    @FXML
    private TextField capacityField;
    @FXML
    private ComboBox<String> roomTypeCombo;
    @FXML
    private TableView<Room> roomTable;
    @FXML
    private TableColumn<Room, String> statusColumn;
    @FXML
    private TableColumn<Room, String> bookingColumn;
    @FXML
    private VBox bookingPanel;
    @FXML
    private Label selectedRoomLabel;
    @FXML
    private ComboBox<String> endTimeCombo;
    @FXML
    private TextField purposeField;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab myBookingsTab;
    @FXML
    private TableView<TimetableEntry> bookingsTable;
    @FXML
    private ComboBox<String> editDayCombo;
    @FXML
    private ComboBox<String> editStartCombo;
    @FXML
    private ComboBox<String> editEndCombo;
    @FXML
    private TextField editPurposeField;
    @FXML
    private Label statusLabel;

    // The two DAO objects are how this screen talks to MySQL.
    private RoomDAO roomDAO = new RoomDAO();
    private TimetableDAO timetableDAO = new TimetableDAO();

    // We remember the search that is shown in the table, so a booking uses the same day and start time.
    private String searchedDay;
    private String searchedTime;
    private String searchedBuilding;
    private int searchedCapacity;
    private String searchedRoomType;

    // The vacant room the user clicked in the table (null when nothing is selected).
    private Room selectedRoom = null;

    // JavaFX calls this method by itself, once, after Stratizen.fxml has been loaded.
    @FXML
    public void initialize() {
        roleLabel.setText("Signed in as: " + UserSession.currentRole + " (ID " + UserSession.currentUserId + ")");

        // Use our own cells so "Vacant" is red and "Occupied" is green.
        statusColumn.setCellFactory(new StatusCellFactory());
        bookingColumn.setCellFactory(new StatusCellFactory());

        // A student cannot book: hide the Booking column and the booking form, and remove the My Bookings tab.
        if (!UserSession.canBookRooms()) {
            bookingColumn.setVisible(false);
            bookingPanel.setVisible(false);
            bookingPanel.setManaged(false);
            tabPane.getTabs().remove(myBookingsTab);
        }

        fillDropdowns();
        loadMyBookings();
        handleSearch();
    }

    // This method puts the choices into the dropdown boxes and sets the starting values.
    private void fillDropdowns() {
        for (String day : TimetableOptions.DAYS) {
            dayCombo.getItems().add(day);
        }
        dayCombo.setValue(todayName());
        timeField.setText("10:15");

        buildingCombo.getItems().add("Any");
        try {
            ArrayList<String> buildings = roomDAO.getBuildings();
            for (String building : buildings) {
                buildingCombo.getItems().add(building);
            }
        } catch (SQLException e) {
            showDatabaseError(e);
        }
        buildingCombo.setValue("Any");

        roomTypeCombo.getItems().add("Any");
        roomTypeCombo.getItems().add("Lab");
        roomTypeCombo.getItems().add("Classroom");
        roomTypeCombo.setValue("Any");

        for (String time : TimetableOptions.TIMES) {
            endTimeCombo.getItems().add(time);
            editStartCombo.getItems().add(time);
            editEndCombo.getItems().add(time);
        }
        for (String day : TimetableOptions.DAYS) {
            editDayCombo.getItems().add(day);
        }
    }

    // This method reads the bookings of the logged in user from MySQL and shows them in the My Bookings tab.
    // Students cannot book, so they have nothing to load.
    private void loadMyBookings() {
        if (!UserSession.canBookRooms()) {
            return;
        }
        try {
            ArrayList<TimetableEntry> bookings = timetableDAO.getBookingsForUser(UserSession.currentUserName);
            bookingsTable.getItems().setAll(bookings);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method runs when a booking is clicked (or chosen with the arrow keys) in the My Bookings table.
    // It copies the booking into the form, so it can be changed and saved with "Update Booking".
    @FXML
    private void handleBookingSelected() {
        TimetableEntry selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            return;
        }
        editDayCombo.setValue(selectedBooking.getDayOfWeek());
        editStartCombo.setValue(selectedBooking.getStartTime());
        editEndCombo.setValue(selectedBooking.getEndTime());
        editPurposeField.setText(selectedBooking.getCourseName());
    }

    // UPDATE: runs when "Update Booking" is clicked. Only the person who made the booking can change it.
    @FXML
    private void handleUpdateBooking() {
        TimetableEntry selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showMessage("Click one of your bookings in the table first.", true);
            return;
        }
        String day = editDayCombo.getValue();
        String startTime = editStartCombo.getValue();
        String endTime = editEndCombo.getValue();
        String purpose = editPurposeField.getText().trim();
        if (day == null || startTime == null || endTime == null || purpose.isEmpty()) {
            showMessage("Please fill in the day, the start time, the end time and the event or purpose.", true);
            return;
        }
        if (endTime.compareTo(startTime) <= 0) {
            showMessage("The end time must be after the start time.", true);
            return;
        }
        try {
            // The booking must not clash with a class or another booking. It does not clash with itself.
            if (!timetableDAO.isRoomFree(selectedBooking.getRoom(), day, startTime, endTime, selectedBooking.getId())) {
                showMessage(selectedBooking.getRoom() + " is already in use on " + day + " between " + startTime
                        + " and " + endTime + ".", true);
                return;
            }
            int changedRows = timetableDAO.updateBooking(selectedBooking.getId(), UserSession.currentUserName,
                    day, startTime, endTime, purpose);
            if (changedRows == 0) {
                showMessage("That booking could not be changed because it is not yours.", true);
                return;
            }
            clearBookingForm();
            loadMyBookings();
            loadRoomTable();
            showMessage("Updated your booking of " + selectedBooking.getRoom() + ": " + day + ", "
                    + startTime + " - " + endTime + ", " + purpose + ".", false);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method empties the boxes of the "Change the selected booking" form.
    private void clearBookingForm() {
        editDayCombo.setValue(null);
        editStartCombo.setValue(null);
        editEndCombo.setValue(null);
        editPurposeField.clear();
    }

    // DELETE: runs when "Delete Booking" is clicked. Only the person who made the booking can delete it.
    @FXML
    private void handleDeleteBooking() {
        TimetableEntry selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showMessage("Click one of your bookings in the table first.", true);
            return;
        }
        try {
            // The DAO deletes the row only if it is a booking made by this user. It gives back 0 if it is not.
            int deletedRows = timetableDAO.deleteBooking(selectedBooking.getId(), UserSession.currentUserName);
            if (deletedRows == 0) {
                showMessage("That booking could not be deleted because it is not yours.", true);
                return;
            }
            loadMyBookings();
            loadRoomTable();
            clearBookingForm();
            showMessage("Deleted your booking of " + selectedBooking.getRoom() + " on " + selectedBooking.getDayOfWeek()
                    + " (" + selectedBooking.getTimeSlot() + "). The room is vacant again.", false);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method runs when the Search button is clicked. It checks the inputs, then searches.
    @FXML
    private void handleSearch() {
        String time = timeField.getText().trim();
        try {
            // LocalTime.parse fails if the text is not a time like 10:15.
            LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            showMessage("Please type the time like 10:15 (hours:minutes, 24 hour clock).", true);
            return;
        }

        int minCapacity = 0;
        String capacityText = capacityField.getText().trim();
        if (!capacityText.isEmpty()) {
            try {
                minCapacity = Integer.parseInt(capacityText);
            } catch (NumberFormatException e) {
                showMessage("Minimum capacity must be a whole number, for example 40.", true);
                return;
            }
        }

        // The inputs are fine, so remember this search and show the results.
        searchedDay = dayCombo.getValue();
        searchedTime = time;
        searchedBuilding = buildingCombo.getValue();
        searchedCapacity = minCapacity;
        searchedRoomType = roomTypeCombo.getValue();

        selectedRoom = null;
        selectedRoomLabel.setText("Click a vacant room in the table to select it.");
        loadRoomTable();
        showSearchSummary();
    }

    // This method runs when "Use current day and time" is clicked.
    @FXML
    private void handleUseNow() {
        dayCombo.setValue(todayName());
        // LocalTime.now() looks like 23:51:17.521, so we keep only the first 5 characters.
        timeField.setText(LocalTime.now().toString().substring(0, 5));
        handleSearch();
    }

    // This method asks MySQL for the rooms of the last search and shows them in the table.
    private void loadRoomTable() {
        try {
            ArrayList<Room> rooms = roomDAO.searchRooms(searchedDay, searchedTime, searchedBuilding,
                    searchedCapacity, searchedRoomType);
            roomTable.getItems().setAll(rooms);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method counts the vacant and occupied rooms and shows the result at the bottom.
    private void showSearchSummary() {
        int vacant = 0;
        int occupied = 0;
        for (Room room : roomTable.getItems()) {
            if (room.getStatus().equals("Vacant")) {
                vacant++;
            } else {
                occupied++;
            }
        }
        showMessage("On " + searchedDay + " at " + searchedTime + ": " + vacant + " vacant and "
                + occupied + " occupied rooms.", false);
    }

    // This method runs when the user clicks the room table. A lecturer or class rep selects a room to book.
    @FXML
    private void handleRoomClicked() {
        Room clickedRoom = roomTable.getSelectionModel().getSelectedItem();
        if (clickedRoom == null || !UserSession.canBookRooms()) {
            return;
        }
        if (clickedRoom.getStatus().equals("Occupied")) {
            selectedRoom = null;
            selectedRoomLabel.setText(clickedRoom.getRoomName() + " is occupied. Please select a vacant room.");
        } else {
            selectedRoom = clickedRoom;
            selectedRoomLabel.setText("Booking " + clickedRoom.getRoomName() + " (" + clickedRoom.getBuilding()
                    + ") on " + searchedDay + " from " + searchedTime);
        }
    }

    // This method runs when "Book Selected Room" is clicked. A booking is saved as a new row in the timetable table.
    @FXML
    private void handleBookRoom() {
        if (selectedRoom == null) {
            showMessage("First click a vacant room in the table.", true);
            return;
        }
        String endTime = endTimeCombo.getValue();
        String purpose = purposeField.getText().trim();
        if (endTime == null || purpose.isEmpty()) {
            showMessage("Please choose an end time and type the event or purpose.", true);
            return;
        }
        // Times like "10:15" and "11:15" can be compared as text, because they are always written the same way.
        if (endTime.compareTo(searchedTime) <= 0) {
            showMessage("The end time must be after the start time (" + searchedTime + ").", true);
            return;
        }

        try {
            if (!timetableDAO.isRoomFree(selectedRoom.getRoomName(), searchedDay, searchedTime, endTime)) {
                showMessage(selectedRoom.getRoomName() + " is already in use between " + searchedTime
                        + " and " + endTime + ". Choose an earlier end time.", true);
                return;
            }
            TimetableEntry booking = new TimetableEntry(0, "Booking", purpose, UserSession.currentUserName,
                    searchedDay, searchedTime, endTime, selectedRoom.getRoomName());
            timetableDAO.addEntry(booking);

            String bookedRoomName = selectedRoom.getRoomName();
            selectedRoom = null;
            selectedRoomLabel.setText("Click a vacant room in the table to select it.");
            purposeField.clear();
            loadRoomTable();
            loadMyBookings();
            showMessage("Booked " + bookedRoomName + " on " + searchedDay + " from " + searchedTime
                    + " to " + endTime + " for: " + purpose, false);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method runs when Logout is clicked. It forgets the user and goes back to the login screen.
    @FXML
    private void handleLogout() {
        UserSession.clear();
        SceneSwitcher.switchTo(roleLabel, "Login.fxml");
    }

    // This method gives today's day as text, for example "Tuesday".
    private String todayName() {
        return LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    // This method shows a message at the bottom of the screen. Errors are shown in red.
    private void showMessage(String text, boolean isError) {
        statusLabel.setText(text);
        statusLabel.getStyleClass().remove("status-error");
        if (isError) {
            statusLabel.getStyleClass().add("status-error");
        }
    }

    // This method shows a simple message when MySQL cannot be used. The technical reason goes to the console.
    private void showDatabaseError(SQLException e) {
        System.out.println("Database error: " + e.getMessage());
        showMessage("Could not use MySQL. Check that it is running and that the password in DatabaseConnection.java is right.", true);
    }
}
