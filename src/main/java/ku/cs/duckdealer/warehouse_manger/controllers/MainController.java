package ku.cs.duckdealer.warehouse_manger.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private String title = "Warehouse Manger";

    private MainPaneController mainPaneCtrl;

    public MainController(Stage stage) throws IOException {
        this.stage = stage;
        this.loadPane();
    }

    private void loadPane() throws IOException {
        FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        GridPane mainPane = mainPaneLoader.load();
        this.mainPaneCtrl = mainPaneLoader.getController();
        this.mainPaneCtrl.setMainPane(mainPane);
    }

    public void start() {
        Pane mainPane = this.mainPaneCtrl.getMainPane();
        Parent root = mainPane;
        int w = (int) mainPane.getWidth();
        int h = (int) mainPane.getHeight();
        this.stage.setTitle(this.title);
        this.stage.setScene(new Scene(root, w, h));
        this.stage.show();
    }

}
