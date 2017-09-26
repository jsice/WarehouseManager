package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import ku.cs.duckdealer.warehouse_manager.models.Product;
import ku.cs.duckdealer.warehouse_manager.models.StockedProduct;

import java.util.ArrayList;

public class StockListController {

    private Pane mainPane;
    private ArrayList<StockedProduct> stockedProducts;
    private ArrayList<Label> labels;
    private MainController mainCtrl;

    @FXML
    private GridPane innerTableGrid;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField searchTextfield;

    @FXML
    private void initialize() {
        this.stockedProducts = new ArrayList<StockedProduct>();
        labels = new ArrayList<Label>();
    }

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
        if (searchTextfield.getText() == null) {
            stockedProducts = this.mainCtrl.getStock().getAllProducts();
        } else {
            String text = searchTextfield.getText();
            this.stockedProducts.removeAll(this.stockedProducts);
            for (StockedProduct p: this.mainCtrl.getStock().getAllProducts()) {
                if (p.getProduct().getID().contains(text)) {
                    stockedProducts.add(p);
                }
            }
        }

        innerTableGrid.getChildren().clear();
        this.innerTableGrid.setGridLinesVisible(false);
        this.innerTableGrid.setGridLinesVisible(true);

        int row = 0;
        for (StockedProduct p: this.stockedProducts) {
            Label id = new Label(p.getProduct().getID()+"");
            Label name = new Label(p.getProduct().getName());
            Label price = new Label(p.getProduct().getPrice()+"");
            Label amount = new Label(p.getQuantity()+"");
            id.setPrefHeight(38);
            id.setPrefWidth(99);
            name.setPrefHeight(38);
            name.setPrefWidth(100);
            price.setPrefHeight(38);
            price.setPrefWidth(100);
            amount.setPrefHeight(38);
            amount.setPrefWidth(99);
            this.labels.add(id);
            this.labels.add(name);
            this.labels.add(price);
            this.labels.add(amount);
            this.innerTableGrid.add(id, 0, row);
            this.innerTableGrid.add(name, 1, row);
            this.innerTableGrid.add(price, 2, row);
            this.innerTableGrid.add(amount, 3, row);
            row++;
            if (row >= 10) {
                this.innerTableGrid.setPrefHeight(this.innerTableGrid.getPrefHeight() + 40);
                this.innerTableGrid.addRow(row);
            }
        }

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
