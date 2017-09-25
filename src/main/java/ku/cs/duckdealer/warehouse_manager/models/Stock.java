package ku.cs.duckdealer.warehouse_manager.models;

import java.util.ArrayList;

public class Stock {

    private ArrayList<Product> productList;

    public Stock() {
        this.productList = new ArrayList<Product>();
    }

    public void addQuantity(String id, int qty) {
        for (Product prd: this.productList) {
            if (prd.getID().equals(id)) {
                if (prd.getQuantity() + qty >= 0) {
                    prd.setQuantity(prd.getQuantity() + qty);
                }
                break;
            }
        }
    }

    public Product getProduct(String id) {
        Product Product = null;
        for (Product prd: this.productList) {
            if (prd.getID().equals(id)) {
                Product = prd;
                break;
            }
        }
        return Product;
    }

}
