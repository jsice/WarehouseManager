package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private String title = "Cashier Manager";

    private MainPaneController mainPaneCtrl;
    private cashierItemListController cashierListCtrl;
    private selectedItemsController selectedItemsCtrl;


    public MainController(Stage stage) throws IOException {
        this.stage = stage;
        this.loadPane();
    }

    public void start() {
        Pane mainPane = this.mainPaneCtrl.getMainPane();
        int w = (int) mainPane.getWidth();
        int h = (int) mainPane.getHeight();
        this.stage.setTitle(this.title);
        this.stage.setScene(new Scene(mainPane));
        this.stage.show();
    }


    private void loadPane() throws IOException {
        FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("/main2.fxml"));

        FlowPane mainPane = mainPaneLoader.load();
        this.mainPaneCtrl = mainPaneLoader.getController();
        this.mainPaneCtrl.setMainPane(mainPane);
        this.mainPaneCtrl.setMainCtrl(this);

        FXMLLoader cashierListPaneLoader = new FXMLLoader(getClass().getResource("/cashierItemList.fxml"));
        BorderPane cashierListPane = cashierListPaneLoader.load();
        this.cashierListCtrl = cashierListPaneLoader.getController();
        this.cashierListCtrl.setMainPane(cashierListPane);
        this.cashierListCtrl.setMainCtrl(this);

        FXMLLoader selectedItemsLoader = new FXMLLoader(getClass().getResource("/selectedItems.fxml"));
        GridPane selectedItemsPane = selectedItemsLoader.load();
        this.selectedItemsCtrl = selectedItemsLoader.getController();
        this.selectedItemsCtrl.setMainPane(selectedItemsPane);
        this.selectedItemsCtrl.setMainCtrl(this);

        this.mainPaneCtrl.getLeftPane().getChildren().add(this.cashierListCtrl.getMainPane());
        this.mainPaneCtrl.getRightPane().getChildren().add(this.selectedItemsCtrl.getMainPane());
    }

}
