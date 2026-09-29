package com.school.oop;

import com.school.oop.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.showStudentSearch();
    }

    public static void main(String[] args) {
        launch(args);
    }
}