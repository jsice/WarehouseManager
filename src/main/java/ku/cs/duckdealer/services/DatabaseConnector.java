package ku.cs.duckdealer.services;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {

    Connection connectTo(String url) throws ClassNotFoundException, SQLException;

}
