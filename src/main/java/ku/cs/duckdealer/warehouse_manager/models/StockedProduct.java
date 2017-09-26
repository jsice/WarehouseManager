package ku.cs.duckdealer.warehouse_manager.models;

public class StockedProduct {
    private Product product;
    private int quantity;

    public StockedProduct(String name, double price) {
        this.product = new Product(name, price);
    }

    public StockedProduct(Product p) {
        this.product = p;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }
}
