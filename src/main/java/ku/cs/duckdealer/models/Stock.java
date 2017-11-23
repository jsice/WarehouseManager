package ku.cs.duckdealer.models;

import java.util.ArrayList;

public class Stock {

    private ArrayList<StockedProduct> stockedProductList;
    private int quantity;

    public Stock() {
        this.stockedProductList = new ArrayList<StockedProduct>();
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

    public void newProduct(StockedProduct stockedProduct) {
        this.stockedProductList.add(stockedProduct);
    }

    public ArrayList<StockedProduct> getAllProducts() {
        return stockedProductList;
    }

    public int getQuantity() {
        return quantity;
    }
}