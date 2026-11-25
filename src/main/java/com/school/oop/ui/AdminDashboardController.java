package com.school.oop.ui;

import com.school.oop.UserSession;
import com.school.oop.dao.RoomDAO;
import com.school.oop.dao.TimetableDAO;
import com.school.oop.model.TimetableEntry;
import com.school.oop.model.TimetableOptions;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

// AdminDashboardController belongs to AdminDashboard.fxml.
// The admin fills in a form to add a timetable entry to MySQL, and sees the entries in a table.
// A filter lets the admin see the entries of one class group only. Only the admin can see timetables.
public class AdminDashboardController {

    // The first choice of the filter. It means "do not filter, show every class group".
    private static final String ALL_GROUPS = "All class groups";

    // ----- Things from AdminDashboard.fxml. Each name matches an fx:id in the FXML file. -----
    @FXML
    private Label roleLabel;
    @FXML
    private TextField classGroupField;
    @FXML
    private TextField courseNameField;
    @FXML
    private TextField lecturerNameField;
    @FXML
    private ComboBox<String> dayCombo;
    @FXML
    private ComboBox<String> startTimeCombo;
    @FXML
    private ComboBox<String> endTimeCombo;
    @FXML
    private ComboBox<String> roomCombo;
    @FXML
    private ComboBox<String> filterCombo;
    @FXML
    private TableView<TimetableEntry> timetableTable;
    @FXML
    private Label statusLabel;

    // The two DAO objects are how this screen talks to MySQL.
    private TimetableDAO timetableDAO = new TimetableDAO();
    private RoomDAO roomDAO = new RoomDAO();

    // JavaFX calls this method by itself, once, after AdminDashboard.fxml has been loaded.
    @FXML
    public void initialize() {
        roleLabel.setText("Signed in as: " + UserSession.currentRole + " (ID " + UserSession.currentUserId + ")");
        for (String day : TimetableOptions.DAYS) {
            dayCombo.getItems().add(day);
        }
        for (String time : TimetableOptions.TIMES) {
            startTimeCombo.getItems().add(time);
            endTimeCombo.getItems().add(time);
        }
        try {
            // The room dropdown is filled from MySQL, so the admin can only choose rooms that exist.
            ArrayList<String> roomNames = roomDAO.getRoomNames();
            for (String roomName : roomNames) {
                roomCombo.getItems().add(roomName);
            }
        } catch (SQLException e) {
            showDatabaseError(e);
        }
        fillFilterChoices(ALL_GROUPS);
        loadEntries();
    }

    // This method fills the filter dropdown with "All class groups" and every class group found in MySQL.
    // Then it selects groupToSelect. If that group does not exist (any more), it selects "All class groups".
    // Note: changing the dropdown also runs handleFilterChanged, which reloads the table.
    private void fillFilterChoices(String groupToSelect) {
        try {
            ArrayList<String> groups = timetableDAO.getClassGroups();
            filterCombo.getItems().clear();
            filterCombo.getItems().add(ALL_GROUPS);
            for (String group : groups) {
                filterCombo.getItems().add(group);
            }
            if (filterCombo.getItems().contains(groupToSelect)) {
                filterCombo.setValue(groupToSelect);
            } else {
                filterCombo.setValue(ALL_GROUPS);
            }
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method runs when a class group is chosen in the filter (onAction in AdminDashboard.fxml).
    @FXML
    private void handleFilterChanged() {
        loadEntries();
    }

    // This method reads the timetable entries from MySQL and shows them in the table.
    // It shows only the class group chosen in the filter, or every entry if the filter says "All class groups".
    private void loadEntries() {
        try {
            String chosenGroup = filterCombo.getValue();
            if (chosenGroup == null || chosenGroup.equals(ALL_GROUPS)) {
                ArrayList<TimetableEntry> entries = timetableDAO.getAllEntries();
                timetableTable.getItems().setAll(entries);
                showMessage("Showing all timetable entries (" + entries.size() + " found).", false);
            } else {
                ArrayList<TimetableEntry> entries = timetableDAO.getEntriesForGroup(chosenGroup);
                timetableTable.getItems().setAll(entries);
                showMessage("Showing the entries of " + chosenGroup + " (" + entries.size() + " found).", false);
            }
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method reads the form and checks it. If everything is fine, it gives back a TimetableEntry with the given id
    // (we use 0 for a new entry). If something is wrong, it shows the reason in red and gives back null.
    // The clash check ignores the entry with this id, so an entry never clashes with itself when we update it.
    private TimetableEntry readForm(int id) {
        String classGroup = classGroupField.getText().trim();
        String courseName = courseNameField.getText().trim();
        String lecturerName = lecturerNameField.getText().trim();
        String day = dayCombo.getValue();
        String startTime = startTimeCombo.getValue();
        String endTime = endTimeCombo.getValue();
        String room = roomCombo.getValue();

        // Check 1: every box must be filled in. A ComboBox with nothing chosen gives null.
        if (classGroup.isEmpty() || courseName.isEmpty() || lecturerName.isEmpty()
                || day == null || startTime == null || endTime == null || room == null) {
            showMessage("Please fill in every box first.", true);
            return null;
        }
        // Check 2: the class must end after it starts. Times like "08:15" can be compared as text.
        if (endTime.compareTo(startTime) <= 0) {
            showMessage("The end time must be after the start time.", true);
            return null;
        }
        try {
            // Check 3: the room must be free at that time, so two classes never share a room.
            if (!timetableDAO.isRoomFree(room, day, startTime, endTime, id)) {
                showMessage(room + " is already in use on " + day + " between " + startTime + " and " + endTime + ".", true);
                return null;
            }
        } catch (SQLException e) {
            showDatabaseError(e);
            return null;
        }
        return new TimetableEntry(id, classGroup, courseName, lecturerName, day, startTime, endTime, room);
    }

    // CREATE: runs when "Add Entry" is clicked. It checks the form, then INSERTs a new row.
    @FXML
    private void handleAddEntry() {
        TimetableEntry newEntry = readForm(0);
        if (newEntry == null) {
            return;
        }
        try {
            timetableDAO.addEntry(newEntry);
            clearForm();
            // The new class group (if it is new) joins the filter and is selected, so the new entry is visible.
            fillFilterChoices(newEntry.getClassGroup());
            loadEntries();
            showMessage("Added " + newEntry.getCourseName() + " (" + newEntry.getClassGroup() + ") in "
                    + newEntry.getRoom() + " on " + newEntry.getDayOfWeek() + ".", false);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method runs when a row of the table is clicked (or chosen with the arrow keys).
    // It copies the row into the form, so the admin can change it and click "Update Entry".
    @FXML
    private void handleRowSelected() {
        TimetableEntry selected = timetableTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        classGroupField.setText(selected.getClassGroup());
        courseNameField.setText(selected.getCourseName());
        lecturerNameField.setText(selected.getLecturerName());
        dayCombo.setValue(selected.getDayOfWeek());
        startTimeCombo.setValue(selected.getStartTime());
        endTimeCombo.setValue(selected.getEndTime());
        roomCombo.setValue(selected.getRoom());
        showMessage("Editing " + selected.getCourseName() + ". Change the boxes above, then click Update Entry.", false);
    }

    // UPDATE: runs when "Update Entry" is clicked. It checks the form, then UPDATEs the row that is selected in the table.
    @FXML
    private void handleUpdateEntry() {
        TimetableEntry selected = timetableTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Click a row in the table first, change the boxes, then click Update Entry.", true);
            return;
        }
        TimetableEntry changedEntry = readForm(selected.getId());
        if (changedEntry == null) {
            return;
        }
        try {
            int changedRows = timetableDAO.updateEntry(changedEntry);
            // The class group may have been changed too, so we refresh the filter and follow the entry.
            fillFilterChoices(changedEntry.getClassGroup());
            loadEntries();
            if (changedRows == 0) {
                showMessage("That entry does not exist any more, so nothing was changed.", true);
                return;
            }
            clearForm();
            showMessage("Updated " + changedEntry.getCourseName() + " (" + changedEntry.getClassGroup() + ").", false);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // DELETE: runs when "Delete Selected" is clicked. It removes the row chosen in the table.
    @FXML
    private void handleDeleteEntry() {
        TimetableEntry selected = timetableTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Click a row in the table first.", true);
            return;
        }
        try {
            timetableDAO.deleteEntry(selected.getId());
            clearForm();
            // If that was the last entry of its class group, the group leaves the filter and "All class groups" is selected.
            fillFilterChoices(filterCombo.getValue());
            loadEntries();
            showMessage("Deleted " + selected.getCourseName() + " (" + selected.getClassGroup() + ").", false);
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // This method runs when "Clear Form" is clicked.
    @FXML
    private void handleClearForm() {
        clearForm();
        timetableTable.getSelectionModel().clearSelection();
    }

    // This method empties every box of the form.
    private void clearForm() {
        classGroupField.clear();
        courseNameField.clear();
        lecturerNameField.clear();
        dayCombo.setValue(null);
        startTimeCombo.setValue(null);
        endTimeCombo.setValue(null);
        roomCombo.setValue(null);
    }

    // This method runs when Logout is clicked. It forgets the user and goes back to the login screen.
    @FXML
    private void handleLogout() {
        UserSession.clear();
        SceneSwitcher.switchTo(statusLabel, "Login.fxml");
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
