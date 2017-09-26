package ku.cs.duckdealer.warehouse_manager.models;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price) {
        this.id = String.valueOf((int) (Math.random()*10000000));
        this.name = name;
        this.price = price;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
