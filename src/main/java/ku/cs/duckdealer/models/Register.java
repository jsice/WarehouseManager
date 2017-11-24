package ku.cs.duckdealer.models;

import java.util.ArrayList;

public class Register {

    private ArrayList<Sales> allSales;
    private Sales currentSales;
    private Stock stock;

    public Register(Stock stock) {
        this.allSales = new ArrayList<>();
        this.stock = stock;
        this.currentSales = new Sales();
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
        this.currentSales = new Sales();
    }

    public Sales getCurrentSales() {
        return currentSales;
    }
}