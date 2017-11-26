package ku.cs.duckdealer.models;

public class SalesItem {
    private String product_id;
    private double price;
    private String name;
    private int quantity;

    SalesItem(Product prd, int qty) {
        this.product_id = prd.getID();
        this.name = prd.getName();
        this.price = prd.getPrice();
        this.quantity = qty;
    }

    public double getSubTotal() {
        return this.price * this.quantity;
    }

    public String getName() { return name; }

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
