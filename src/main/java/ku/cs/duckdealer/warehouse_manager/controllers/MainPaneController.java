package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class MainPaneController {

    private MainController mainCtrl;
    private FlowPane mainPane;
    @FXML
    private Menu loginStatus;
    @FXML
    private FlowPane leftPane, rightPane;
    @FXML
    private AnchorPane reportPane;
    @FXML
    private GridPane mainPaneInner;

    public void switchPanel(ActionEvent event) {
        int status = mainCtrl.getMainPanelStatus();
        if (status == 1 && event.getSource().toString().contains("toReport")) {
            mainCtrl.switchPanel(2);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/warehouse/reportInWarehouse.fxml"));
            try {
                reportPane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mainPane.getChildren().remove(1);
            this.mainPane.getChildren().add(1, reportPane);

        }else if (status == 2 && event.getSource().toString().contains("toMain")){
            mainCtrl.switchPanel(1);
            this.mainPane.getChildren().remove(1);
            this.mainPane.getChildren().add(mainPaneInner);
        }

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
}
