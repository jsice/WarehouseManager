package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private String title = "Warehouse Manger";

    private MainPaneController mainPaneCtrl;
    private StockListController stockListCtrl;

    public MainController(Stage stage) throws IOException {
        this.stage = stage;
        this.loadPane();
    }

    public void start() {
        Pane mainPane = this.mainPaneCtrl.getMainPane();
        int w = (int) mainPane.getWidth();
        int h = (int) mainPane.getHeight();
        System.out.println(w);
        System.out.println(h);
        this.stage.setTitle(this.title);
        this.stage.setScene(new Scene(mainPane, 1000, 600));
        this.stage.show();
    }

    private void loadPane() throws IOException {
        FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        GridPane mainPane = mainPaneLoader.load();
        this.mainPaneCtrl = mainPaneLoader.getController();
        this.mainPaneCtrl.setMainPane(mainPane);

        FXMLLoader stockListPaneLoader = new FXMLLoader(getClass().getResource("/stockList.fxml"));
        BorderPane stockListPane = stockListPaneLoader.load();
        this.stockListCtrl = stockListPaneLoader.getController();
        this.stockListCtrl.setMainPane(stockListPane);

        this.mainPaneCtrl.getLeftPane().getChildren().add(this.stockListCtrl.getMainPane());

    }



}
