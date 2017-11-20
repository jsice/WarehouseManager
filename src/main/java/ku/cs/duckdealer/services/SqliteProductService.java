package ku.cs.duckdealer.services;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteProductService extends DatabaseProductService {

    public SqliteProductService(String dbFilePath) {
        super(dbFilePath);
    }

    @Override
    void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + this.url);
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
