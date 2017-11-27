package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.ProductMovement;
import ku.cs.duckdealer.models.StockedProduct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseProductMovementService extends DatabaseDataService<ProductMovement> {

    public DatabaseProductMovementService(String url, DatabaseConnector connector) {
        super(url, connector);
    }

    @Override
    List<String> getCreateTableQueries() {
        List<String> queries = new ArrayList<>();
        queries.add("CREATE TABLE product_movements (id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */, date text NOT NULL, product_id varchar(8) NOT NULL, exit INTEGER NOT NULL, quantity int(11) NOT NULL, reason text NOT NULL)");
        return queries;
    }

    @Override
    public ArrayList<ProductMovement> getAll() {
        ArrayList<ProductMovement> productMovements = new ArrayList<>();
        try {
            connect();
            String query = "SELECT * FROM product_movements natural join (select product_id, name, price from product) as product";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String[] dateStr = result.getString(2).split("-");
                Calendar date = new GregorianCalendar(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]) - 1, Integer.parseInt(dateStr[2]), Integer.parseInt(dateStr[3]), Integer.parseInt(dateStr[4]));
                String productID = result.getString(3);
                boolean isExit = result.getInt(4) == 1;
                int quantity = result.getInt(5);
                String reason = result.getString(6);
                String name = result.getString(7);
                double price = result.getDouble(8);

                Product product = new Product(productID, name, price);

                ProductMovement productMovement = new ProductMovement(product, date, isExit, quantity, reason);

                productMovements.add(productMovement);

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
        return  productMovements;
    }

    @Override
    public void update(ProductMovement data) {
        // you can't update this table
    }

    @Override
    public void add(ProductMovement data) {
        try {
            connect();
            String date = String.format("%d-%d-%d-%d-%d", data.getDate().get(Calendar.YEAR), data.getDate().get(Calendar.MONTH) + 1, data.getDate().get(Calendar.DAY_OF_MONTH), data.getDate().get(Calendar.HOUR_OF_DAY), data.getDate().get(Calendar.MINUTE));
            String productID = data.getProduct().getID();
            int isExit = data.isExit() ? 1 : 0;
            int quantity = data.getQuantity();
            String reason = data.getReason();
            String query = String.format("insert into product_movements (date, product_id, exit, quantity, reason) values (\"%s\", \"%s\", %d, %d, \"%s\")", date, productID, isExit, quantity, reason);
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
