package com.school.oop.ui;

import javafx.stage.Stage;

public class SceneManager {
    private Stage stage;
    private StudentSearchScreen studentSearchScreen;

    // This constructor stores the main window.
    public SceneManager(Stage stage) {
        this.stage = stage;
        this.stage.setMinWidth(900);
        this.stage.setMinHeight(600);
    }

    // This method creates the first screen and shows it.
    public void showStudentSearch() {
        if (studentSearchScreen == null) {
            studentSearchScreen = new StudentSearchScreen();
        }
        stage.setTitle("Campus Empty Classroom Finder");
        stage.setScene(studentSearchScreen.createScene());
        stage.show();
    }
}
