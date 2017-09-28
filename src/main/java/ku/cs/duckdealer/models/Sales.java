package ku.cs.duckdealer.models;

import java.util.ArrayList;

public class Sales {

    private class SalesItem {
        private Product product;
        private int quantity;

        private SalesItem(Product prd, int qty) {
            this.product = prd;
            this.quantity = qty;
        }

        private double getSubTotal() {
            return this.product.getPrice() * this.quantity;
        }

    }

    private int quantity;
    private ArrayList<SalesItem> items;

    public Sales() {
        this.items = new ArrayList<>();
    }

    public void addItem(Product prd, int qty) {
        SalesItem item = new SalesItem(prd, qty);
        items.add(item);
    }

    public double getTotal() {
        double sum = 0;
        for (SalesItem item: items) {
            sum += item.getSubTotal();
        }
        return sum;
    }

    public int getQuantity() {
        return quantity;
    }
}
