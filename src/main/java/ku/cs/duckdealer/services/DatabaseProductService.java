package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.StockedProduct;

import java.sql.*;
import java.util.ArrayList;

public abstract class DatabaseProductService implements IProductService {
    Connection conn;
    String url;

    public DatabaseProductService(String url) {
        this.url = url;
        try {
            connect();
            createDatabase();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    abstract void connect() throws ClassNotFoundException, SQLException;

    abstract String getCreateStocksTableQuery();

    abstract String getCreateSalesTableQuery();

    void createDatabase() {
        try {
            connect();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "", null);
            boolean stocksTableExist = false;
            boolean salesTableExist = false;
            while (rs.next()) {
                if ("stocks".equals(rs.getString(3).toLowerCase())) stocksTableExist = true;
                if ("sales".equals(rs.getString(3).toLowerCase())) salesTableExist = true;
            }
            if (!stocksTableExist) {
                String query = getCreateStocksTableQuery();
                Statement statement = conn.createStatement();
                statement.execute(query);
            }
            if (!salesTableExist) {
                String query = getCreateSalesTableQuery();
                Statement statement = conn.createStatement();
                statement.execute(query);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void close() throws SQLException {
        if (conn != null)
            conn.close();
    }

    public ArrayList<StockedProduct> getProducts(){
        ArrayList<StockedProduct> products = new ArrayList<>();
        try {
            connect();
            String sql = "select * from Stocks";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
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
        return products;
    }

    public void updateProduct(StockedProduct p) {
        try {
            connect();
            String id = p.getProduct().getID();
            String name = p.getProduct().getName();
            double price = p.getProduct().getPrice();
            int qty = p.getQuantity();

            String sql = String.format("update Stocks set name = '%s', price = %s, quantity = %s where id = '%s'", name, price, qty, id);
            Statement statement = conn.createStatement();
            statement.execute(sql);

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

    public void addProduct(StockedProduct p){
        try {
            connect();
            String sql = "insert into Stocks(id, name, price, quantity) values (?, ?, ?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, p.getProduct().getID());
            statement.setString(2, p.getProduct().getName());
            statement.setDouble(3, p.getProduct().getPrice());
            statement.setInt(4, p.getQuantity());

            statement.executeUpdate();
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
