package com.school.oop;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    // This method runs when the app starts. It opens the window with the login screen.
    @Override
    public void start(Stage stage) throws IOException {
        // Load the login screen from its FXML file.
        Parent loginScreen = FXMLLoader.load(getClass().getResource("ui/Login.fxml"));

        // The Scene is the content of the window. We add the Strathmore colours (the CSS file) once here.
        Scene scene = new Scene(loginScreen, 1200, 780);
        scene.getStylesheets().add(getClass().getResource("ui/strathmore.css").toExternalForm());

        stage.setTitle("Strathmore University - Room Finder");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    // This method starts the JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}
