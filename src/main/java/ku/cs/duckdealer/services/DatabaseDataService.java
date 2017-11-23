package ku.cs.duckdealer.services;

import java.sql.*;

public abstract class DatabaseDataService<T> implements IDataService<T> {
    Connection conn;
    String url;
    DatabaseConnector connector;

    public DatabaseDataService(String url, DatabaseConnector connector) {
        this.connector = connector;
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

    void connect() throws ClassNotFoundException, SQLException {
        conn = this.connector.connectTo(this.url);
    }

    abstract String getTableName();

    abstract String getCreateTableQuery();

    void createDatabase() {
        try {
            connect();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, getTableName(), null);
            boolean tableExist = false;
            while (rs.next()) {
                if (getTableName().equals(rs.getString(3).toLowerCase())) tableExist = true;
            }
            if (!tableExist) {
                String query = getCreateTableQuery();
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
}
