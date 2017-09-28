package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.models.Stock;
import ku.cs.duckdealer.models.StockedProduct;
//import ku.cs.duckdealer.cashier.models.StockedProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class cashierItemListController {

    private Pane mainPane;
    private ArrayList<Label> labels;
    private MainController mainCtrl;

    @FXML
    private TextField searchTextfield;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private GridPane innerTableGrid ;

    private ArrayList<StockedProduct> stockedProducts;
    private ArrayList<ArrayList> tempArray ;
    private Stock stock ;


    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }


    @FXML
    private void initialize() {

        filterComboBox.getItems().addAll("ID", "Name");
        stockedProducts = stock.getAllProducts();
        stock.newProduct(new StockedProduct("Apple",20.0));



    }

    @FXML
    private void showProducts() {}

    private void showAllStockedProduct(){


    }





}