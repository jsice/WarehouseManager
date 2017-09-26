package ku.cs.duckdealer.warehouse_manager.models;

import java.util.ArrayList;

public class Stock {

    private ArrayList<StockedProduct> stockedProductList;
    private int quantity;

    public Stock() {
        this.stockedProductList = new ArrayList<StockedProduct>();
    }

    public void addQuantity(String id, int qty) {
        for (StockedProduct prd: this.stockedProductList) {
            if (prd.getProduct().getID().equals(id)) {
                if (prd.getQuantity() + qty >= 0) {
                    this.quantity++;
                    prd.setQuantity(prd.getQuantity() + qty);
                }
                break;
            }
        }
    }

    public StockedProduct getProduct(String id) {
        StockedProduct StockedProduct = null;
        for (StockedProduct prd: this.stockedProductList) {
            if (prd.getProduct().getID().equals(id)) {
                StockedProduct = prd;
                break;
            }
        }
        return StockedProduct;
    }

    public ArrayList<StockedProduct> getAllProducts() {
        return stockedProductList;
    }

    public int getQuantity() {
        return quantity;
    }
}
