package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.Sales;
import ku.cs.duckdealer.models.SalesItem;

import java.sql.*;
import java.util.*;

public class DatabaseSalesService extends DatabaseDataService<Sales> {

    public DatabaseSalesService(String url, DatabaseConnector connector) {
        super(url, connector);
    }

    @Override
    List<String> getCreateTableQueries() {
        List<String> queries = new ArrayList<>();
        queries.add("CREATE TABLE sales (sales_id integer primary key /*!40101 AUTO_INCREMENT */, date text not null )");
        queries.add("CREATE TABLE sales_detail ( `sales_id` int NOT NULL, `product_id` varchar(8) NOT NULL, `quantity` int ( 11 ) NOT NULL, `price` double NOT NULL )");
        return queries;
    }

    @Override
    public ArrayList<Sales> getAll() {
        ArrayList<Sales> salesList = new ArrayList<>();
        HashMap<Integer, Sales> salesMap = new HashMap<>();
        try {
            connect();
            String sql = "select * from (sales natural join (sales_detail natural join (select product_id, name from product) as alias1 ))";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int sales_id = result.getInt(1);
                String[] dateStr = result.getString(2).split("-");
                Calendar date = new GregorianCalendar(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]) - 1, Integer.parseInt(dateStr[2]), Integer.parseInt(dateStr[3]), Integer.parseInt(dateStr[4]));
                String productID = result.getString(3);
                int qty = result.getInt(4);
                double price = result.getDouble(5);
                String productName = result.getString(6);
                Product product = new Product(productID, productName, price);
                Sales sales;
                if (salesMap.containsKey(sales_id)){
                    sales = salesMap.get(sales_id);
                } else {
                    sales = new Sales(sales_id, date);
                    salesList.add(sales);
                    salesMap.put(sales_id, sales);
                }
                sales.addItem(product, qty);
            }
            close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesList;
    }

    @Override
    public void update(Sales data) {
        //you can't update sales. funk you.
    }

    @Override
    public void add(Sales data) {
        try {
            connect();
            Calendar date = data.getDate();
            String query = String.format("insert into sales (date) values ('%s')", String.format("%d-%d-%d-%d-%d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE)));
            Statement statement = conn.createStatement();
            statement.execute(query);
            query = "SELECT max(sales_id) FROM sales";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int sales_id = resultSet.getInt(1);
            data.setID(sales_id);
            SalesItem[] items = data.getItems();
            for (SalesItem item : items) {
                String product_id = item.getID();
                double price = item.getPrice();
                int qty = item.getQuantity();
                query = String.format("insert into sales_detail values (%d, '%s', %d, %.2f)", sales_id, product_id, qty, price);
                statement.execute(query);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
