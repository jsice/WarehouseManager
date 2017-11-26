package ku.cs.duckdealer.models;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Register {

    private ArrayList<Sales> allSales;
    private Sales currentSales;
    private Stock stock;
    private double vat;
    private String storeName;

    public Register(Stock stock, double vat, String name) {
        this.storeName = name;
        this.allSales = new ArrayList<>();
        this.stock = stock;
        this.currentSales = new Sales();
        this.vat = vat;
    }

    public void makeNewSales() {
        currentSales = new Sales();
    }

    public void enterItem(String id, int qty) {
        if (currentSales != null) {
            StockedProduct sprd = this.stock.getProduct(id);
            if (sprd.getQuantity() >= qty) {
                currentSales.addItem(sprd.getProduct(), qty);
            }
        }
    }

    public void endSales() {
        this.allSales.add(currentSales);
        makeNewSales();
    }

    public void removeCurrentSalesItem(SalesItem item, int qty) {
        StockedProduct sprd = this.stock.getProduct(item.getID());
        sprd.setQuantity(sprd.getQuantity() + item.getQuantity());
        currentSales.removeItem(sprd.getProduct(), qty);
    }

    public void removeCurrentSalesItem(SalesItem item) {
        removeCurrentSalesItem(item, item.getQuantity());
    }

    public void removeAllCurrentSalesItem() {
        for (SalesItem item: currentSales.getItems()) {
            removeCurrentSalesItem(item);
        }
    }

    public double getTotalFromCurrentSales() {
        if (currentSales == null) return 0;
        return currentSales.getTotal();
    }

    public double getBeforeVatFromCurrentSales() {
        if (currentSales == null) return 0;
        return Math.round((currentSales.getTotal() * 100 / (100 + vat)) * 100.0) / 100.0;
    }

    public double getVatFromCurrentSales() {
        if (currentSales == null) return 0;
        return Math.round((getTotalFromCurrentSales() - getBeforeVatFromCurrentSales()) * 100.0) / 100.0;
    }

    public Sales getCurrentSales() {
        return currentSales;
    }

    public Node getReceiptFromCurrentSales() {
        if (currentSales == null) return null;

        GridPane receipt = new GridPane();
        receipt.setHgap(10);
        receipt.setVgap(10);
        receipt.setPadding(new Insets(25, 25, 25, 25));
        Label storeNameLabel = new Label(this.storeName);
        receipt.add(storeNameLabel,0, 0, 3, 1);
        Calendar date = currentSales.getDate();
        Label dateLabel = new Label(String.format("date: %02d/%02d/%d   %02d:%02d", date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH), date.get(Calendar.YEAR), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE)));
        receipt.add(dateLabel, 0, 1, 3, 1);
        Label orderIDLabel = new Label("Order ID: " + currentSales.getID());
        receipt.add(orderIDLabel, 0, 2, 3, 1);
        Label itemsLabel = new Label("Items");
        itemsLabel.setAlignment(Pos.CENTER);
        receipt.add(itemsLabel, 0, 4, 2, 1);
        Label priceLabel = new Label("price");
        priceLabel.setAlignment(Pos.CENTER);
        receipt.add(priceLabel, 2, 4);
        receipt.add(new Label("------------------------------"), 0, 5, 3, 1);
        int row = 6;
        int quantity = currentSales.getQuantity();
        int padding = String.valueOf(quantity).length();
        for (SalesItem item: currentSales.getItems()) {
            Label itemInfoLabel = new Label(String.format("%" + padding + "d %s", item.getQuantity(), item.getName()));
            Label itemPriceLabel = new Label(String.format("%.2f", item.getPrice()));
            itemPriceLabel.setAlignment(Pos.CENTER_RIGHT);
            receipt.add(itemInfoLabel, 0, row, 2, 1);
            receipt.add(itemPriceLabel, 2, row);
            row++;
        }
        receipt.add(new Label("------------------------------"), 0, row++, 3, 1);
        receipt.add(new Label(String.format("%"+padding+"d items", quantity)), 0, row++, 3, 1);
        receipt.add(new Label("Total"), 0, row, 2, 1);
        receipt.add(new Label(String.format("%.2f", getBeforeVatFromCurrentSales())), 2, row++);
        receipt.add(new Label("VAT " + vat + "%"), 0, row, 2, 1);
        receipt.add(new Label(String.format("%.2f", getVatFromCurrentSales())), 2, row++);
        Label grandTotal = new Label("Grand Total");
        Font oldFont = grandTotal.getFont();
        grandTotal.setFont(Font.font(oldFont.getFamily(), FontWeight.BOLD, oldFont.getSize() + 2));
        receipt.add(grandTotal, 0, row, 2, 1);
        receipt.add(new Label(String.format("%.2f", getTotalFromCurrentSales())), 2, row++);
        receipt.add(new Label("------------------------------"), 0, row++, 3, 1);
        receipt.add(new Label("Received"), 0, row, 2, 1);
        receipt.add(new Label(String.format("%.2f", getBeforeVatFromCurrentSales())), 2, row++);
        receipt.add(new Label("Change"), 0, row, 2, 1);
        receipt.add(new Label(String.format("%.2f", getBeforeVatFromCurrentSales())), 2, row++);

        return receipt;
    }

    public Stock getStock() {
        return stock;
    }

    public double getVat() {
        return vat;
    }

    public String getStoreName() {
        return storeName;
    }
}