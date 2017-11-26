package ku.cs.duckdealer.cashier.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.Register;
import ku.cs.duckdealer.models.SalesItem;
import ku.cs.duckdealer.models.StockedProduct;
import ku.cs.duckdealer.services.PrintService;

import java.util.*;

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
            Button btnRemove = new Button("X");
            btnRemove.setOnMouseClicked(event -> {
                TextInputDialog dialog = new TextInputDialog("1");
                dialog.setTitle("Remove Item: " + item.getName());
                dialog.setHeaderText("Current amount is " + item.getQuantity() + ".");
                dialog.setContentText("How many items do you want to remove? ");
                dialog.setResizable(false);
                dialog.initOwner(this.beforeVatLabel.getScene().getWindow());
                Optional<String> result = dialog.showAndWait();
                String value = result.get();
                int amount = Integer.parseInt(value);
                if (amount >= item.getQuantity()) {
                    register.getCurrentSales().removeItem(item.getID(), amount);
                } else {
                    register.getCurrentSales().removeItem(item.getID(), amount);
                }
                showItems();
            });
            btnRemove.setPrefWidth(34);
            btnRemove.setPrefHeight(33);
            labels.add(productName);
            labels.add(productQuantity);
            labels.add(productPrice);
            itemsList.add(productName, 0, row);
            itemsList.add(productQuantity, 1, row);
            itemsList.add(productPrice, 2, row);
            itemsList.add(btnRemove, 3, row);
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
        if (money == netTotal) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment");
            alert.setContentText("No change, thanks.");
            alert.initOwner(this.beforeVatLabel.getScene().getWindow());
            alert.showAndWait();
        } else if (money > netTotal) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment");
            alert.setContentText("Here your change: $" + (money-netTotal) +", thanks.");
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
        this.register.getCurrentSales().setDate(new GregorianCalendar());
        mainCtrl.getSalesService().add(this.register.getCurrentSales());
        for (SalesItem item: this.register.getCurrentSales().getItems()) {
            mainCtrl.getProductService().update(this.register.getStock().getProduct(item.getID()));
        }
        Node receipt = this.createReceipt(money);
        Stage stage = new Stage();
        Scene scene = new Scene((Parent) receipt);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(this.beforeVatLabel.getScene().getWindow());
        stage.show();
        stage.hide();
        double receiptWidth = ((GridPane) receipt).getWidth();
        double receiptHeight = ((GridPane) receipt).getHeight();
        double width = 200;
        double height = receiptHeight/receiptWidth*width;
        mainCtrl.getPrintService().print(receipt, width, height);
        this.register.endSales();
        this.showItems();
    }

    private Node createReceipt(double payment) {
        String separator = "---------------------------------------------------";
        GridPane receipt = new GridPane();
        receipt.getColumnConstraints().add(new ColumnConstraints(100));
        receipt.getColumnConstraints().add(new ColumnConstraints(100));
        receipt.getColumnConstraints().add(new ColumnConstraints(100));
        receipt.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        receipt.setAlignment(Pos.CENTER);
        receipt.setHgap(5);
        receipt.setVgap(5);
        receipt.setPadding(new Insets(10, 10, 10, 10));
        Label storeNameLabel = new Label(this.register.getStoreName());
        storeNameLabel.setPrefWidth(300);
        storeNameLabel.setAlignment(Pos.CENTER);
        receipt.add(storeNameLabel,0, 0, 3, 1);
        Calendar date = this.register.getCurrentSales().getDate();
        Label dateLabel = new Label(String.format("Date: %02d/%02d/%d   %02d:%02d", date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH), date.get(Calendar.YEAR), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE)));
        receipt.add(dateLabel, 0, 1, 3, 1);
        Label orderIDLabel = new Label("Order ID: " + this.register.getCurrentSales().getID());
        receipt.add(orderIDLabel, 0, 2, 3, 1);
        Label itemsLabel = new Label("Items");
        itemsLabel.setPrefWidth(200);
        itemsLabel.setAlignment(Pos.CENTER);
        receipt.add(itemsLabel, 0, 4, 2, 1);
        Label priceLabel = new Label("Price");
        priceLabel.setPrefWidth(100);
        priceLabel.setAlignment(Pos.CENTER);
        receipt.add(priceLabel, 2, 4);
        receipt.add(new Label(separator), 0, 5, 3, 1);
        int row = 6;
        int quantity = this.register.getCurrentSales().getQuantity();
        int padding = String.valueOf(quantity).length();
        for (SalesItem item: this.register.getCurrentSales().getItems()) {
            Label itemInfoLabel = new Label(String.format("%" + padding + "d %s", item.getQuantity(), item.getName()));
            Label itemPriceLabel = new Label(String.format("%.2f", item.getSubTotal()));
            itemPriceLabel.setAlignment(Pos.CENTER_RIGHT);
            itemPriceLabel.setPrefWidth(100);
            receipt.add(itemInfoLabel, 0, row, 2, 1);
            receipt.add(itemPriceLabel, 2, row);
            row++;
        }
        receipt.add(new Label(separator), 0, row++, 3, 1);
        receipt.add(new Label(String.format("%"+padding+"d items", quantity)), 0, row++, 3, 1);
        receipt.add(new Label("Total"), 0, row, 2, 1);
        Label totalLabel = new Label(String.format("%.2f", this.register.getBeforeVatFromCurrentSales()));
        totalLabel.setPrefWidth(100);
        totalLabel.setAlignment(Pos.CENTER_RIGHT);
        receipt.add(totalLabel, 2, row++);
        receipt.add(new Label("VAT " + this.register.getVat() + "%"), 0, row, 2, 1);
        Label vatLabel = new Label(String.format("%.2f", this.register.getVatFromCurrentSales()));
        vatLabel.setAlignment(Pos.CENTER_RIGHT);
        vatLabel.setPrefWidth(100);
        receipt.add(vatLabel, 2, row++);
        Label grandTotal = new Label("Grand Total");
        Font oldFont = grandTotal.getFont();
        Font newFont = Font.font(oldFont.getFamily(), FontWeight.BOLD, oldFont.getSize() + 2);
        grandTotal.setFont(newFont);
        receipt.add(grandTotal, 0, row, 2, 1);
        Label grandTotalLabel = new Label(String.format("%.2f", this.register.getTotalFromCurrentSales()));
        grandTotalLabel.setPrefWidth(100);
        grandTotalLabel.setAlignment(Pos.CENTER_RIGHT);
        grandTotalLabel.setFont(newFont);
        receipt.add(grandTotalLabel, 2, row++);
        receipt.add(new Label(separator), 0, row++, 3, 1);
        receipt.add(new Label("Received"), 0, row, 2, 1);
        Label receivedLabel = new Label(String.format("%.2f", payment));
        receivedLabel.setPrefWidth(100);
        receivedLabel.setAlignment(Pos.CENTER_RIGHT);
        receipt.add(receivedLabel, 2, row++);
        receipt.add(new Label("Change"), 0, row, 2, 1);
        Label changeLabel = new Label(String.format("%.2f", payment - this.register.getTotalFromCurrentSales()));
        changeLabel.setPrefWidth(100);
        changeLabel.setAlignment(Pos.CENTER_RIGHT);
        receipt.add(changeLabel, 2, row++);
        receipt.add(new Label(separator), 0, row++, 3, 1);
        Label thankyouLabel = new Label("Thank you.");
        thankyouLabel.setPrefWidth(300);
        thankyouLabel.setAlignment(Pos.CENTER);
        receipt.add(thankyouLabel, 0, row++, 3, 1);
        Label pleaseLabel = new Label("Please come back again.");
        pleaseLabel.setPrefWidth(300);
        pleaseLabel.setAlignment(Pos.CENTER);
        receipt.add(pleaseLabel, 0, row, 3, 1);
        return receipt;
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