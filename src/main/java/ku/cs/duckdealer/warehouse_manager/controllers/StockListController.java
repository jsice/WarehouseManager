package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

    public void createNewProduct(){
        this.mainCtrl.login();
    }

    @FXML
    private void showProducts() {
        String text = searchTextfield.getText();
        this.stockedProducts.removeAll(this.stockedProducts);
        for (StockedProduct p: this.mainCtrl.getStock().getAllProducts()) {
            if (p.getProduct().getID().contains(text)) {
                stockedProducts.add(p);
            }
        }
        int row = 0;
        for (StockedProduct p: this.stockedProducts) {
            Label id = new Label(p.getProduct().getID()+"");
            Label name = new Label(p.getProduct().getName());
            Label price = new Label(p.getProduct().getPrice()+"");
            Label amount = new Label(p.getQuantity()+"");
            this.labels.add(id);
            this.labels.add(name);
            this.labels.add(price);
            this.labels.add(amount);
            this.innerTableGrid.add(id, 0, row);
            this.innerTableGrid.add(name, 1, row);
            this.innerTableGrid.add(price, 2, row);
            this.innerTableGrid.add(amount, 3, row);
            row++;
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
