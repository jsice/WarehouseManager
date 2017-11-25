package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.StockedProduct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseProductService extends DatabaseDataService<StockedProduct> {

    public DatabaseProductService(String url, DatabaseConnector connector) {
        super(url, connector);
    }

    @Override
    List<String> getCreateTableQueries() {
        List<String> queries = new ArrayList<>();
        queries.add("CREATE TABLE product (product_id varchar(8) NOT NULL, name text NOT NULL, price double NOT NULL, quantity int(11) NOT NULL, PRIMARY KEY (product_id))");
        return queries;
    }

    @Override
    public ArrayList<StockedProduct> getAll() {
        ArrayList<StockedProduct> products = new ArrayList<>();
        try {
            connect();
            String query = "select * from product";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String id = result.getString(1);
                String name = result.getString(2);
                double price = result.getDouble(3);
                int quantity = result.getInt(4);

                Product p = new Product(id, name, price);
                StockedProduct sp = new StockedProduct(p);
                sp.setQuantity(quantity);

                products.add(sp);

            }

            close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  products;
    }

    @Override
    public void update(StockedProduct data) {
        try {
            connect();
            String id = data.getProduct().getID();
            String name = data.getProduct().getName();
            double price = data.getProduct().getPrice();
            int qty = data.getQuantity();

            String query = String.format("update product set name = '%s', price = %s, quantity = %s where product_id = '%s'", name, price, qty, id);
            Statement statement = conn.createStatement();
            statement.execute(query);

            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void add(StockedProduct data) {
        try {
            connect();
            String query = String.format("insert into product (product_id, name, price, quantity) values (\"%s\", \"%s\", %.2f, %d)", data.getProduct().getID(), data.getProduct().getName(), data.getProduct().getPrice(), data.getQuantity());
            Statement statement = conn.createStatement();
            statement.execute(query);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
