package ku.cs.duckdealer.warehouse_manger.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class MainPaneController {

    private GridPane mainPane;

    @FXML
    private FlowPane leftPane,rightPane;

    public GridPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(GridPane mainPane) {
        this.mainPane = mainPane;
    }
}
