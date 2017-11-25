package ku.cs.duckdealer.models;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Register {

    private ArrayList<Sales> allSales;
    private Sales currentSales;
    private Stock stock;
    private double vat;

    public Register(Stock stock, double vat) {
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

        GridPane node = new GridPane();

        return node;
    }

    public Stock getStock() {
        return stock;
    }
}