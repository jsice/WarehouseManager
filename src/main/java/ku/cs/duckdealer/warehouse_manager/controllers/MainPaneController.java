package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPaneController {

    private FlowPane mainPane;
    @FXML
    private Menu loginStatus;
    @FXML
    private FlowPane leftPane, rightPane;

    @FXML
    public void login() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorizationPopUp.fxml"));
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene((Parent) loader.load()));
            AuthorizationController authController = loader.getController();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (AuthenticationService.LOGGED_IN_AS_STOCK) this.loginStatus.setText("You are logged in as Warehouse");
        if (AuthenticationService.LOGGED_IN_AS_OWNER) this.loginStatus.setText("You are logged in as Owner");
        System.out.println(AuthenticationService.LOGGED_IN_AS_OWNER);
    }

    @FXML
    public void logout() {
        AuthenticationService.logout();
        this.loginStatus.setText("You are not logged in...");
    }

    public FlowPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(FlowPane mainPane) {
        this.mainPane = mainPane;
    }

    public FlowPane getLeftPane() {
        return leftPane;
    }

    public FlowPane getRightPane() {
        return rightPane;
    }

}
