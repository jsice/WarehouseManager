package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.Stock;
import ku.cs.duckdealer.models.StockedProduct;

import java.sql.*;
import java.util.ArrayList;

public class MySQLProductService extends DatabaseProductService{

//    private String host = "10.2.21.32";
//    private String port = "3306";
//    private String dbName = "WarehouseDB";
//    private String url = "//" + host + ":" + port + "/" + dbName;

    public MySQLProductService(String host, String port, String dbName) {
        super("//" + host + ":" + port + "/" + dbName);
    }

    void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql:" + url, "root", "");
    }

    @Override
    String getCreateStocksTableQuery() {
        return "CREATE TABLE stocks (id varchar(8) NOT NULL, name text NOT NULL, price double NOT NULL, quantity int(11) NOT NULL, PRIMARY KEY (id))";
    }

    @Override
    String getCreateSalesTableQuery() {
        return "CREATE TABLE sales ( date date NOT NULL, item text NOT NULL, quantity int(11) NOT NULL, price double NOT NULL)";
    }
}