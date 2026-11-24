module com.school.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // App must be visible to JavaFX so it can start the application.
    exports com.school.oop;

    // The controllers are created by the FXML loader, and the tables read the model getters by name.
    // "opens" gives JavaFX that permission.
    opens com.school.oop.ui to javafx.fxml;
    opens com.school.oop.model to javafx.base;
}
