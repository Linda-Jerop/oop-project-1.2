module com.school.oop {
    requires transitive javafx.graphics;
    requires javafx.controls;
    exports com.school.oop;
    exports com.school.oop.model;
    exports com.school.oop.logic;
    exports com.school.oop.ui;
    opens com.school.oop.model to javafx.base;
}
