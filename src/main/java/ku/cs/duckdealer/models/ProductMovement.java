package ku.cs.duckdealer.models;

import java.util.Calendar;

public class ProductMovement {

    private Product product;
    private Calendar date;
    private boolean isExit;
    private int quantity;
    private String reason;

    public ProductMovement(Product product, Calendar date, boolean isExit, int quantity, String reason) {
        this.product = product;
        this.date = date;
        this.isExit = isExit;
        this.quantity = quantity;
        this.reason = reason;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isExit() {
        return isExit;
    }

    public Product getProduct() {
        return product;
    }

    public String getReason() {
        return reason;
    }

    public Calendar getDate() {
        return date;
    }
}
