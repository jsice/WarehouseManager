package ku.cs.duckdealer.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    abstract List<String> getCreateTableQueries();

    void createDatabase() {
        try {
            connect();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "", null);
            List<String> createTableQueries = getCreateTableQueries();
            boolean[] tableExist = new boolean[createTableQueries.size()];
            while (rs.next()) {
                int i = 0;
                for (String query: createTableQueries) {
                    String tableName = query.split(" ")[2];
                    if (tableName.equals(rs.getString(3).toLowerCase())) tableExist[i] = true;
                    i++;
                }

            }
            for (int i = 0; i < tableExist.length; i++) {
                if (!tableExist[i]) {
                    String query = createTableQueries.get(i);
                    Statement statement = conn.createStatement();
                    statement.execute(query);
                }
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
