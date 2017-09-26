package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class MainPaneController {

    private FlowPane mainPane;
    @FXML
    private Menu loginStatus;
    @FXML
    private FlowPane leftPane, rightPane;

    @FXML
    public void login() {

    }

    @FXML
    public void logout() {

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
