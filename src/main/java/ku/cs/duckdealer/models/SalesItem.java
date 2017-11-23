package ku.cs.duckdealer.models;

public class SalesItem {
    private String product_id;
    private double price;
    private int quantity;

    SalesItem(Product prd, int qty) {
        this.product_id = prd.getID();
        this.price = prd.getPrice();
        this.quantity = qty;
    }

    double getSubTotal() {
        return this.price * this.quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getID() {
        return product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
