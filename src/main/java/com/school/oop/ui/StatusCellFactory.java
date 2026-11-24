package com.school.oop.ui;

import com.school.oop.model.Room;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

// A column needs a "cell factory": a small object that makes a new cell whenever the table needs one.
// This one makes StatusCell objects.
public class StatusCellFactory implements Callback<TableColumn<Room, String>, TableCell<Room, String>> {

    // The table calls this method to get a new cell.
    @Override
    public TableCell<Room, String> call(TableColumn<Room, String> column) {
        return new StatusCell();
    }
}
