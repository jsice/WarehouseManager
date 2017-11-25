package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.Register;
import ku.cs.duckdealer.models.SalesItem;
import ku.cs.duckdealer.models.StockedProduct;
import ku.cs.duckdealer.services.PrintService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Payment");
        dialog.setHeaderText("Your items cost $" + this.netTotalLabel.getText() + ".");
        dialog.setContentText("Insert money: ");
        dialog.setResizable(false);
        dialog.initOwner(this.beforeVatLabel.getScene().getWindow());
        Optional<String> result = dialog.showAndWait();
        double netTotal = Double.parseDouble(this.netTotalLabel.getText());
        double money = Double.parseDouble(result.get());
        if (money > netTotal) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment");
            alert.setContentText("No change, thanks.");
            alert.initOwner(this.beforeVatLabel.getScene().getWindow());
            alert.showAndWait();
        } else if (money == netTotal) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment");
            alert.setContentText("Here your change: $" + (netTotal-money) +", thanks.");
            alert.initOwner(this.beforeVatLabel.getScene().getWindow());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Payment");
            alert.setHeaderText("Not enough money.");
            alert.setContentText("Please insert more money.");
            alert.initOwner(this.beforeVatLabel.getScene().getWindow());
            alert.showAndWait();
            return;
        }
//        Node receipt = this.register.getReceiptFromCurrentSales();
//        mainCtrl.getPrintService().print(receipt, receipt.getBoundsInParent().getWidth(), receipt.getBoundsInParent().getHeight());
        mainCtrl.getSalesService().add(this.register.getCurrentSales());
        for (SalesItem item: this.register.getCurrentSales().getItems()) {
            mainCtrl.getProductService().update(this.register.getStock().getProduct(item.getID()));
        }
        this.register.endSales();
        this.showItems();
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