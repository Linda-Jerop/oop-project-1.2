package com.school.oop.ui;

import com.school.oop.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// LoginController belongs to Login.fxml. This is a MOCK login.
// The Stratizen ID must be six digits. The password can be anything, it is never checked.
// The role buttons decide which screen opens.
public class LoginController {

    // These are the things from Login.fxml. Each name matches an fx:id in the FXML file.
    @FXML
    private VBox loginPane;
    @FXML
    private TextField stratizenIdField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    // Each of these methods runs when its button is clicked (onAction in Login.fxml).
    @FXML
    private void loginAsAdmin() {
        login("Admin", "AdminDashboard.fxml");
    }

    @FXML
    private void loginAsLecturer() {
        login("Lecturer", "Stratizen.fxml");
    }

    @FXML
    private void loginAsClassRep() {
        login("ClassRep", "Stratizen.fxml");
    }

    @FXML
    private void loginAsStudent() {
        login("Student", "Stratizen.fxml");
    }

    // This method checks the ID and the password. If they are fine, it saves who logged in
    // and opens the screen for that role. If not, it shows the reason in red and stays on this screen.
    private void login(String role, String screenFile) {
        String stratizenId = stratizenIdField.getText().trim();

        if (!isSixDigits(stratizenId)) {
            errorLabel.setText("The Stratizen ID must be exactly six digits, for example 123456.");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            errorLabel.setText("Please type your password.");
            return;
        }

        UserSession.currentRole = role;
        UserSession.currentUserId = stratizenId;
        UserSession.currentUserName = makeUserName(role, stratizenId);
        SceneSwitcher.switchTo(loginPane, screenFile);
    }

    // This method checks that the text is exactly six characters and that every one of them is a digit from 0 to 9.
    private boolean isSixDigits(String text) {
        if (text.length() != 6) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            if (letter < '0' || letter > '9') {
                return false;
            }
        }
        return true;
    }

    // This method gives the name that is saved with a booking, so we know who made it. It is made from the role
    // and the ID, for example "Lecturer 654321". Two different people therefore never share a name.
    private String makeUserName(String role, String stratizenId) {
        if (role.equals("ClassRep")) {
            return "Class Rep " + stratizenId;
        }
        return role + " " + stratizenId;
    }
}
