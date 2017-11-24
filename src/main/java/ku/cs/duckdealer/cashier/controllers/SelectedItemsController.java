package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.Register;
import ku.cs.duckdealer.models.SalesItem;
import ku.cs.duckdealer.models.StockedProduct;

import java.util.ArrayList;
import java.util.List;

public class SelectedItemsController {

    @FXML
    private GridPane itemsList;

    private Pane mainPane;
    private ArrayList<Label> labels = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private MainController mainCtrl;
    private Register register;

    public void addItem(Product p, int selectedAmount){
        mainCtrl.getRegister().enterItem(p.getID(), selectedAmount);

    }

    private void clearItems() {
        itemsList.getChildren().clear();
        itemsList.setPrefHeight(330);
        this.itemsList.setGridLinesVisible(false);
        this.itemsList.setGridLinesVisible(true);
    }

    public void showItems(){
        clearItems();
        int row = 0;
        for (SalesItem item :register.getCurrentSales().getItems()) {
            if (row >= 10) {
                this.itemsList.setPrefHeight(this.itemsList.getPrefHeight() + 33);
                this.itemsList.addRow(row);
            }
            Label productName = new Label(item.getName());
            productName.setPrefWidth(139);
            productName.setPrefHeight(33);
            productName.setAlignment(Pos.CENTER);
            Label productQuantity = new Label(item.getQuantity() + "");
            productQuantity.setPrefWidth(80);
            productQuantity.setPrefHeight(33);
            productQuantity.setAlignment(Pos.CENTER);
            Label productPrice = new Label(item.getPrice() * item.getQuantity() + "");
            productPrice.setPrefWidth(145);
            productPrice.setPrefHeight(33);
            productPrice.setAlignment(Pos.CENTER);
            labels.add(productName);
            labels.add(productQuantity);
            labels.add(productPrice);
            itemsList.add(productName, 0, row);
            itemsList.add(productQuantity, 1, row);
            itemsList.add(productPrice, 2, row);
            row++;
        }
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) { this.mainCtrl = mainCtrl; }

    public void setRegister(Register register) {
        this.register = register;
    }
}