package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.duckdealer.warehouse_manager.models.StockedProduct;
import ku.cs.duckdealer.warehouse_manager.models.Stock;

import java.io.IOException;

public class MainController {

    private Stage stage;
    private String title = "Warehouse Manger";

    private MainPaneController mainPaneCtrl;
    private StockListController stockListCtrl;
    private ProductDetailController productDetailCtrl;

    private Stock stock;

    public MainController(Stage stage) throws IOException {
        this.stock = new Stock();

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
        FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        FlowPane mainPane = mainPaneLoader.load();
        this.mainPaneCtrl = mainPaneLoader.getController();
        this.mainPaneCtrl.setMainPane(mainPane);
        this.mainPaneCtrl.setMainCtrl(this);

        FXMLLoader stockListPaneLoader = new FXMLLoader(getClass().getResource("/stockList.fxml"));
        BorderPane stockListPane = stockListPaneLoader.load();
        this.stockListCtrl = stockListPaneLoader.getController();
        this.stockListCtrl.setMainPane(stockListPane);
        this.stockListCtrl.setMainCtrl(this);

        FXMLLoader productDetailPaneLoader = new FXMLLoader(getClass().getResource("/productDetail.fxml"));
        BorderPane productDetailPane = productDetailPaneLoader.load();
        this.productDetailCtrl = productDetailPaneLoader.getController();
        this.productDetailCtrl.setMainPane(productDetailPane);
        this.productDetailCtrl.setMainCtrl(this);

        this.mainPaneCtrl.getLeftPane().getChildren().add(this.stockListCtrl.getMainPane());
        this.mainPaneCtrl.getRightPane().getChildren().add(this.productDetailCtrl.getMainPane());
    }

    public void login() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorizationPopUp.fxml"));
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene((Parent) loader.load()));
            AuthorizationController authController = loader.getController();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (AuthenticationService.LOGGED_IN_AS_STOCK) this.mainPaneCtrl.getLoginStatus().setText("You are logged in as Warehouse");
        if (AuthenticationService.LOGGED_IN_AS_OWNER) this.mainPaneCtrl.getLoginStatus().setText("You are logged in as Owner");

    }
    public void logout(){
        AuthenticationService.logout();
        this.mainPaneCtrl.getLoginStatus().setText("You are not logged in...");
    }

    public void showProductDetail(StockedProduct stockedProduct) {
        this.productDetailCtrl.setup(stockedProduct);
    }

    public Stock getStock() {
        return stock;
    }
}
