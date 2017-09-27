package ku.cs.duckdealer.warehouse_manager.models;

import com.sun.javafx.binding.StringFormatter;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price) {
        this.id = String.format("%08d", (int) (Math.random()*100000000));
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

    public void setName(String name) { this.name = name; }

    public void setPrice(double price) { this.price = price; }
}
