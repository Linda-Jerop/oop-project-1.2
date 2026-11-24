package com.school.oop.ui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

// SceneSwitcher changes the screen shown in the window, for example from Login to AdminDashboard.
public class SceneSwitcher {

    // This method loads an FXML file and shows it in the window.
    // anyNodeOnScreen can be any control that is on the current screen. We use it to find the window.
    public static void switchTo(Node anyNodeOnScreen, String fxmlFileName) {
        try {
            Parent newScreen = FXMLLoader.load(SceneSwitcher.class.getResource(fxmlFileName));
            Stage window = (Stage) anyNodeOnScreen.getScene().getWindow();
            window.getScene().setRoot(newScreen);
        } catch (IOException e) {
            System.out.println("Could not open " + fxmlFileName + ": " + e.getMessage());
        }
    }
}
