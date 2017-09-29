package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    private ArrayList<StockedProduct> stockedProducts;
    private ArrayList<ArrayList> tempArray ;
    private Stock stock ;
    private Label selectedID, selectedName, selectedPrice, selectedAmount;
    private BackgroundFill selectedBackgroundFill;
    private String searchText;

    @FXML
    private TextField searchTextfield;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private GridPane innerTableGrid ;
    @FXML
    private Label idLabel, nameLabel, priceLabel, qtyLabel;



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
        selectedBackgroundFill = new BackgroundFill(Color.CORAL, CornerRadii.EMPTY, Insets.EMPTY);
        searchText = "" ;
        stock = new Stock() ;

        filterComboBox.getItems().addAll("ID", "Name");
        stockedProducts = stock.getAllProducts();
        stock.newProduct(new StockedProduct("Apple",20));
        stockedProducts.get(0).setQuantity(10);
        stock.newProduct(new StockedProduct("Bag",100));
        stockedProducts.get(1).setQuantity(5);
        stock.newProduct(new StockedProduct("Pepsi",15));
        stockedProducts.get(2).setQuantity(20);
        stock.newProduct(new StockedProduct("Hamburger",25));
        stockedProducts.get(3).setQuantity(3);
        showAllStockedProduct() ;
    }

    @FXML
    private void showProducts() {} //show products from search


    private void showAllStockedProduct(){
        for (int i = 0 ; i < stockedProducts.size() ; i++) {
            idLabel = new Label(stockedProducts.get(i).getProduct().getID()) ;
            nameLabel = new Label(stockedProducts.get(i).getProduct().getName()) ;
            priceLabel = new Label(stockedProducts.get(i).getProduct().getPrice() + "");
            qtyLabel = new Label(stockedProducts.get(i).getQuantity() + "");

            innerTableGrid.add( idLabel, 0, i);
            innerTableGrid.add( nameLabel, 1, i);
            innerTableGrid.add( priceLabel, 2, i);
            innerTableGrid.add( qtyLabel, 3, i);


        }
    }







}