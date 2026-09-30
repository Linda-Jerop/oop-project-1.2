package com.school.oop;

import com.school.oop.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    // This method opens the first screen of the app.
    @Override
    public void start(Stage stage) {
        SceneManager manager = new SceneManager(stage);
        manager.showStudentSearch();
    }

    // This method starts the JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}