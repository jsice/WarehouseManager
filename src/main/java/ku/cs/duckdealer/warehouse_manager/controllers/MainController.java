package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.duckdealer.models.Stock;
import ku.cs.duckdealer.models.StockedProduct;
import ku.cs.duckdealer.services.DatabaseProductService;
import ku.cs.duckdealer.services.DatabaseSalesService;
import ku.cs.duckdealer.services.MySQLConnector;
import ku.cs.duckdealer.services.SQLiteConnector;


import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    private int mainPanelStatus;

    private Stage stage;
    private String title = "Warehouse Manager";

    private MainPaneController mainPaneCtrl;
    private AuthenticationService authenticationService;
    private StockListController stockListCtrl;
    private ProductDetailController productDetailCtrl;
    private ReportController reportCtrl;
    private Stock stock;

    private AnchorPane reportPane;

    private DatabaseProductService productService;
    private DatabaseSalesService salesService;

    public MainController(Stage stage) throws IOException, SQLException {
        this.stage = stage;
        this.productService = new DatabaseProductService("//10.2.57.175:3306/warehousedb", new MySQLConnector());
        this.salesService = new DatabaseSalesService("//127.0.0.1:3306/warehousedb", new MySQLConnector());
//        this.productService = new DatabaseProductService("test_db.db", new SQLiteConnector());
//        this.salesService = new DatabaseSalesService("test_db.db", new SQLiteConnector());
        this.stock = new Stock();
        this.authenticationService = new AuthenticationService();

        this.loadStock();
        this.loadPane();
        this.stockListCtrl.showAllProducts();

        this.mainPanelStatus = 1;

    }

    private void loadStock() {
        for (StockedProduct sp : productService.getAll()) {
            this.stock.newProduct(sp);
        }
    }

    public void start() {
        Pane mainPane = this.mainPaneCtrl.getMainPane();
        int w = (int) mainPane.getWidth();
        int h = (int) mainPane.getHeight();
        this.stage.setTitle(this.title);
        this.stage.setOnCloseRequest(e -> System.exit(0));
        this.stage.setScene(new Scene(mainPane));
        this.stage.show();
    }

    private void loadPane() throws IOException {
        FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("/fxml/warehouse/main.fxml"));
        FlowPane mainPane = mainPaneLoader.load();
        this.mainPaneCtrl = mainPaneLoader.getController();
        this.mainPaneCtrl.setMainPane(mainPane);
        this.mainPaneCtrl.setMainCtrl(this);

        FXMLLoader stockListPaneLoader = new FXMLLoader(getClass().getResource("/fxml/warehouse/stockList.fxml"));
        BorderPane stockListPane = stockListPaneLoader.load();
        this.stockListCtrl = stockListPaneLoader.getController();
        this.stockListCtrl.setMainPane(stockListPane);
        this.stockListCtrl.setMainCtrl(this);

        FXMLLoader productDetailPaneLoader = new FXMLLoader(getClass().getResource("/fxml/warehouse/productDetail.fxml"));
        BorderPane productDetailPane = productDetailPaneLoader.load();
        this.productDetailCtrl = productDetailPaneLoader.getController();
        this.productDetailCtrl.setMainPane(productDetailPane);
        this.productDetailCtrl.setMainCtrl(this);

        FXMLLoader reportPaneLoader = new FXMLLoader(getClass().getResource("/fxml/warehouse/reportInWarehouse.fxml"));
        this.reportPane = reportPaneLoader.load();
        this.reportCtrl = reportPaneLoader.getController();
        this.reportCtrl.setMainPane(reportPane);
        this.reportCtrl.setMainCtrl(this);

        this.mainPaneCtrl.getLeftPane().getChildren().add(this.stockListCtrl.getMainPane());
        this.mainPaneCtrl.getRightPane().getChildren().add(this.productDetailCtrl.getMainPane());
    }

    public void login() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/warehouse/authorizationPopUp.fxml"));
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene((Parent) loader.load()));
            AuthorizationController authCtrl = loader.getController();
            authCtrl.setAuthenticationService(this.authenticationService);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (AuthenticationService.LOGGED_IN_AS_STOCK)
            this.mainPaneCtrl.getLoginStatus().setText("You are logged in as Warehouse");
        if (AuthenticationService.LOGGED_IN_AS_OWNER)
            this.mainPaneCtrl.getLoginStatus().setText("You are logged in as Owner");

    }

    public void logout() {
        authenticationService.logout();
        this.mainPaneCtrl.getLoginStatus().setText("You are not logged in...");
    }

    public void switchPanel(ActionEvent event) {
        if (this.mainPanelStatus == 1 && event.getSource().toString().contains("toReport")) {
            mainPanelStatus = 2;
            mainPaneCtrl.swapPane(reportPane);

        } else if (this.mainPanelStatus == 2 && event.getSource().toString().contains("toMain")) {
            mainPanelStatus = 1;
            mainPaneCtrl.swapPane(mainPaneCtrl.getMainPaneInner());
        }

    }

    public void showProductDetail(StockedProduct stockedProduct) {
        this.stockListCtrl.setSelectedProduct(stockedProduct);
        this.productDetailCtrl.setup(stockedProduct);
    }

    public void showAllProducts() {
        this.stockListCtrl.showAllProducts();
    }

    public void showFilteredProducts() {
        this.stockListCtrl.showFilteredProducts();
    }

    public void createProduct() {
        productDetailCtrl.toggleCreateMode();
    }

    public Stock getStock() {
        return stock;
    }

    public DatabaseProductService getProductService() {
        return productService;
    }

    public int getMainPanelStatus() {
        return mainPanelStatus;
    }

    public void setMainPanelStatus(int mainPanelStatus) {
        this.mainPanelStatus = mainPanelStatus;
    }

    public DatabaseSalesService getSalesService() {
        return salesService;
    }
}
