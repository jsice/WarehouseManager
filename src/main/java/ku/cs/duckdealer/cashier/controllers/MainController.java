package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ku.cs.duckdealer.models.Register;
import ku.cs.duckdealer.models.Stock;
import ku.cs.duckdealer.models.StockedProduct;
import ku.cs.duckdealer.services.*;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private String title = "Cashier Manager";

    private Register register;
    private Stock stock;
    private DatabaseProductService productService;
    private DatabaseSalesService salesService;

    private MainPaneController mainPaneCtrl;
    private CashierItemListController cashierListCtrl;
    private SelectedItemsController selectedItemsCtrl;
    private SelectItemPopUpController selectedItemPopUpCtrl;

    private Stage selectedItemPopUpStage;

    public MainController(Stage stage) throws IOException {
        String dbURL = "test_db.db";//"//10.2.21.181:3306/WarehouseDB"
        this.stock = new Stock();
        this.register = new Register(this.stock);
        this.productService = new DatabaseProductService(dbURL, new SQLiteConnector());
        this.salesService = new DatabaseSalesService(dbURL, new SQLiteConnector());
        this.loadStock();

        this.stage = stage;
        this.loadPane();
        this.cashierListCtrl.showAllProducts();
    }

    public void start() {
        Pane mainPane = this.mainPaneCtrl.getMainPane();
        this.stage.setTitle(this.title);
        this.stage.setOnCloseRequest(e -> System.exit(0));
        this.stage.setScene(new Scene(mainPane));
        this.stage.show();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        mainPane.getTransforms().add(new Scale(bounds.getWidth() / mainPane.getWidth(), bounds.getHeight() / mainPane.getHeight()));
        this.stage.setX(bounds.getMinX());
        this.stage.setY(bounds.getMinY());
        this.stage.setWidth(bounds.getWidth());
        this.stage.setHeight(bounds.getHeight());
        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.stage.setFullScreenExitHint("");
        this.stage.setFullScreen(true);
    }

    private void loadStock() {
        for (StockedProduct sp: productService.getAll()) {
            this.stock.newProduct(sp);
        }
    }

    private void loadPane() throws IOException {
        FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("/fxml/cashier/main.fxml"));

        FlowPane mainPane = mainPaneLoader.load();
        this.mainPaneCtrl = mainPaneLoader.getController();
        this.mainPaneCtrl.setMainPane(mainPane);
        this.mainPaneCtrl.setMainCtrl(this);

        FXMLLoader cashierListPaneLoader = new FXMLLoader(getClass().getResource("/fxml/cashier/cashierItemList.fxml"));
        BorderPane cashierListPane = cashierListPaneLoader.load();
        this.cashierListCtrl = cashierListPaneLoader.getController();
        this.cashierListCtrl.setMainPane(cashierListPane);
        this.cashierListCtrl.setMainCtrl(this);

        FXMLLoader selectedItemsLoader = new FXMLLoader(getClass().getResource("/fxml/cashier/selectedItems.fxml"));
        GridPane selectedItemsPane = selectedItemsLoader.load();
        this.selectedItemsCtrl = selectedItemsLoader.getController();
        this.selectedItemsCtrl.setMainPane(selectedItemsPane);
        this.selectedItemsCtrl.setMainCtrl(this);
        this.selectedItemsCtrl.setRegister(this.register);

        this.mainPaneCtrl.getLeftPane().getChildren().add(this.cashierListCtrl.getMainPane());
        this.mainPaneCtrl.getRightPane().getChildren().add(this.selectedItemsCtrl.getMainPane());

        FXMLLoader selectItemPopUpLoader = new FXMLLoader(getClass().getResource("/fxml/cashier/selectItemPopUp.fxml"));
        GridPane selectItemPopUpPane = selectItemPopUpLoader.load();
        this.selectedItemPopUpCtrl = selectItemPopUpLoader.getController();
        this.selectedItemPopUpCtrl.setMainPane(selectItemPopUpPane);

        selectedItemPopUpStage = new Stage();
        selectedItemPopUpStage.initModality(Modality.APPLICATION_MODAL);
        selectedItemPopUpStage.setScene(new Scene(this.selectedItemPopUpCtrl.getMainPane()));

    }

    public Stock getStock() {
        return stock;
    }

    public CashierItemListController getCashierListCtrl(){
        return cashierListCtrl;
    }

    public SelectedItemsController getSelectedItemsCtrl(){
        return selectedItemsCtrl;
    }

    public int enterItemAmount(StockedProduct selectedProduct) {
        this.selectedItemPopUpCtrl.setAllLabelText(selectedProduct);
        this.selectedItemPopUpStage.showAndWait();
        return this.selectedItemPopUpCtrl.getAmount();
    }

    public void reloadSalesItems() {
        this.selectedItemsCtrl.showItems();
    }

    public SelectItemPopUpController getSelectedItemPopUpCtrl() {
        return selectedItemPopUpCtrl;
    }

    public Register getRegister() {
        return register;
    }
}
