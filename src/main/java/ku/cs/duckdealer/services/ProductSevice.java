package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.Stock;
import ku.cs.duckdealer.models.StockedProduct;

import java.sql.*;

public class ProductSevice {
    private String dbUrl;
    private Connection conn;
    private String host = "10.2.21.181";
    private String port = "3306";
    private String dbName = "WarehouseDB";
    private String url = "//" + host + ":" + port + "/" + dbName;

    public ProductSevice() {
        dbUrl = "com.mysql.";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql:" + url, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Stock getStock() throws SQLException {
        Stock stock = new Stock();
        String sql = "select * from Stocks";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);
        return stock;

    }

    public void addProduct(StockedProduct p){
        String sql = "insert into Stocks(id, name, price, quantity) values (?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, p.getProduct().getID());
            statement.setString(2, p.getProduct().getName());
            statement.setDouble(3, p.getProduct().getPrice());
            statement.setInt(4, p.getQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
