package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

import java.io.IOException;

public class MainPaneController {

    private MainController mainCtrl;
    private FlowPane mainPane;
    @FXML
    private Menu loginStatus;
    @FXML
    private FlowPane leftPane, rightPane, rootPane;
    @FXML
    private AnchorPane reportPane;
    @FXML
    private GridPane mainPaneInner;

    public void switchPanel(ActionEvent event) {
        mainCtrl.switchPanel(event);

    }

    public void swapPane(Pane pane){
        rootPane.getChildren().remove(1);
        rootPane.getChildren().add(pane);
    }

    @FXML
    public void login() {
        this.mainCtrl.login();
    }

    @FXML
    public void logout() {
        this.mainCtrl.logout();
    }

    public void exitProgram() {
        System.exit(0);
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

    public Menu getLoginStatus() {
        return loginStatus;
    }

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public GridPane getMainPaneInner() {
        return mainPaneInner;
    }
}
