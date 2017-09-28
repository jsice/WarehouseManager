package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.Stock;

import java.sql.*;

public class ProductSevice {
    private String dbUrl;
    private Connection conn;
    private String host = "10.2.21.181";
    private String port = "3306";
    private String dbName = "testsql";
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

    public void getStock() throws SQLException {
        Stock stock = new Stock();
        String sql = "select * from temptable";
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            System.out.println(result.getInt(1));
            System.out.println(result.getString(2));
        }
//        return stock;
    }
}
