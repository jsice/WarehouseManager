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
        currentSales.removeItem(item.getID(), qty);
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