package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthorizationController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private AuthenticationService authenticationService;

    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (!authenticationService.login(username, password)) {
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

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
