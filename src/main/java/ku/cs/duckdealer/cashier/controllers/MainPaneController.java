package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class MainPaneController {

    private MainController mainCtrl;
    private FlowPane mainPane;

    @FXML
    private GridPane leftPane, rightPane;


    public FlowPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(FlowPane mainPane) {
        this.mainPane = mainPane;
    }

    public GridPane getLeftPane() {
        return leftPane;
    }

    public GridPane getRightPane() {
        return rightPane;
    }


    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
}