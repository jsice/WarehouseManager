package ku.cs.duckdealer.models;

import java.util.*;

public class Sales {

    private int sales_id;
    private Calendar date;
    private Map<String, SalesItem> items;

    public Sales() {
        this.date = new GregorianCalendar();
        this.items = new HashMap<>();
    }

    public Sales(int id, Calendar date) {
        this.sales_id = id;
        this.date = date;
        this.items = new HashMap<>();
    }

    public void addItem(Product prd, int qty) {
        if (items.containsKey(prd.getID())) {
            SalesItem item = items.get(prd.getID());
            item.setQuantity(item.getQuantity() + qty);
        } else {
            SalesItem item = new SalesItem(prd, qty);
            items.put(prd.getID(), item);
        }

    }

    public double getTotal() {
        double sum = 0;
        for (SalesItem item: items.values()) {
            sum += item.getSubTotal();
        }
        return sum;
    }

    public int getQuantity() {
        int quantity = 0;
        for (SalesItem item: items.values()) {
            quantity += item.getQuantity();
        }
        return quantity;
    }

    public void setID(int sales_id) {
        this.sales_id = sales_id;
    }

    public SalesItem[] getItems() {
        return items.values().toArray(new SalesItem[items.size()]);
    }

    public int getID() {
        return sales_id;
    }

    public Calendar getDate() {
        return date;
    }
}