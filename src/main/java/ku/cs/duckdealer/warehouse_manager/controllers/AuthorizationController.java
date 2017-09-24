package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthorizationController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private boolean isLoggedIn;

    public AuthorizationController() {
        isLoggedIn = false;
    }

    public void login() {

        if ("Owner".equals(usernameField.getText()) && "1234".equals(passwordField.getText())) {
            isLoggedIn = true;
        } else if ("Stock".equals(usernameField.getText()) && "12345".equals(passwordField.getText())) {
            isLoggedIn = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Login Error");
            alert.setContentText("Username or Password wrong");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Login Successful");
        alert.setContentText("Welcome");
        alert.showAndWait();
        back();
    }

    public void back() {
        usernameField.getScene().getWindow().hide();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
