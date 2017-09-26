package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.warehouse_manager.models.Product;
import java.util.ArrayList;

public class StockListController {

    private Pane mainPane;
    private ArrayList<Product> products;
    private MainController mainCtrl;

    @FXML
    private GridPane innerTableGrid;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField searchTextfield;

    public void createNewProduct(){
        if (AuthenticationService.NOT_LOGGED_IN){
            this.mainCtrl.login();
            if (!AuthenticationService.NOT_LOGGED_IN){
                mainCtrl.createProduct();

            }
        }

    }

    @FXML
    private void showProducts() {

    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
}
