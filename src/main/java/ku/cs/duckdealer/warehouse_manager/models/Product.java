package ku.cs.duckdealer.warehouse_manager.models;

public class Product {
    private int number;
    private String name;
    private double price;
    private int amount;

    public Product(int number, String name, double price, int amount) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
