package com.school.oop.ui;

import com.school.oop.model.Room;
import javafx.scene.control.TableCell;

// A StatusCell is one cell of the Status or Booking column.
// It changes the colour of the text: "Vacant" is red, "Occupied" is green and "Book" is blue.
// The colours themselves are written in strathmore.css.
public class StatusCell extends TableCell<Room, String> {

    // JavaFX calls this method every time the cell has to show a value.
    @Override
    protected void updateItem(String text, boolean empty) {
        super.updateItem(text, empty);

        // JavaFX reuses cells, so first remove any colour the cell had before.
        getStyleClass().removeAll("vacant", "occupied", "book");

        if (empty || text == null) {
            setText(null);
            return;
        }
        setText(text);

        if (text.equals("Vacant")) {
            getStyleClass().add("vacant");
        } else if (text.equals("Occupied")) {
            getStyleClass().add("occupied");
        } else if (text.equals("Book")) {
            getStyleClass().add("book");
        }
    }
}
