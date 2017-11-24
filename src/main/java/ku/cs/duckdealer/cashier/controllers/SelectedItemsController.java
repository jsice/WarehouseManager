package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    @FXML
    private Button btnClearAll, btnCheckOut;
    @FXML
    private Label beforeVatLabel, vatLabel, netTotalLabel;

    private Pane mainPane;
    private ArrayList<Label> labels = new ArrayList<>();
    private MainController mainCtrl;
    private Register register;

    @FXML
    private void initialize() {
        btnCheckOut.setDisable(true);
        btnClearAll.setDisable(true);
    }

    private void clearItems() {
        itemsList.getChildren().clear();
        itemsList.setPrefHeight(330);
        this.itemsList.setGridLinesVisible(false);
        this.itemsList.setGridLinesVisible(true);
    }

    public void showItems(){
        clearItems();
        if (register.getCurrentSales().getItems().length > 0) {
            btnCheckOut.setDisable(false);
            btnClearAll.setDisable(false);
        } else {
            btnCheckOut.setDisable(true);
            btnClearAll.setDisable(true);
        }
        int row = 0;
        for (SalesItem item :register.getCurrentSales().getItems()) {
            if (row >= 10) {
                this.itemsList.setPrefHeight(this.itemsList.getPrefHeight() + 33);
                this.itemsList.addRow(row);
            }
            Label productName = new Label("   " + item.getName());
            productName.setPrefWidth(139);
            productName.setPrefHeight(33);
            productName.setAlignment(Pos.CENTER_LEFT);
            Label productQuantity = new Label(item.getQuantity() + "   ");
            productQuantity.setPrefWidth(80);
            productQuantity.setPrefHeight(33);
            productQuantity.setAlignment(Pos.CENTER_RIGHT);
            Label productPrice = new Label(String.format("%.2f   ", item.getPrice() * item.getQuantity()));
            productPrice.setPrefWidth(145);
            productPrice.setPrefHeight(33);
            productPrice.setAlignment(Pos.CENTER_RIGHT);
            labels.add(productName);
            labels.add(productQuantity);
            labels.add(productPrice);
            itemsList.add(productName, 0, row);
            itemsList.add(productQuantity, 1, row);
            itemsList.add(productPrice, 2, row);
            row++;
        }

        double netTotal = this.register.getTotalFromCurrentSales();
        double vat = this.register.getVatFromCurrentSales();
        double beforeVat = this.register.getBeforeVatFromCurrentSales();
        this.netTotalLabel.setText(String.format("%.2f", netTotal));
        this.vatLabel.setText(String.format("%.2f", vat));
        this.beforeVatLabel.setText(String.format("%.2f", beforeVat));
    }

    @FXML
    private void clearAll() {
        this.register.removeAllCurrentSalesItem();
        showItems();
        this.mainCtrl.showFilteredProducts();
    }

    @FXML
    private void checkOut() {
        System.out.println("checkOut");
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