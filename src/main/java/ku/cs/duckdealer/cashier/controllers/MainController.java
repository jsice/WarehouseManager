package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.duckdealer.models.Stock;
import ku.cs.duckdealer.models.StockedProduct;
import ku.cs.duckdealer.services.ProductService;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private String title = "Cashier Manager";

    private Stock stock;
    private ProductService productService;

    private MainPaneController mainPaneCtrl;
    private cashierItemListController cashierListCtrl;
    private selectedItemsController selectedItemsCtrl;


    public MainController(Stage stage) throws IOException {
        this.stock = new Stock();
        this.productService = new ProductService();
        this.loadStock();

        this.stage = stage;
        this.loadPane();
        this.cashierListCtrl.showAllProducts();
    }

    public void start() {
        Pane mainPane = this.mainPaneCtrl.getMainPane();
        this.stage.setTitle(this.title);
        this.stage.setScene(new Scene(mainPane));
        this.stage.show();
    }

    private void loadStock() {
        for (StockedProduct sp: productService.getProducts()) {
            this.stock.newProduct(sp);
        }
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

    public Stock getStock() {
        return stock;
    }

    public cashierItemListController getCashierListCtrl(){
        return cashierListCtrl;
    }

    public selectedItemsController getSelectedItemsCtrl(){
        return selectedItemsCtrl;
    }
}
