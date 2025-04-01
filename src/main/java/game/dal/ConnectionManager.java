package game.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Use ConnectionManager to connect to your database instance.
 *
 * ConnectionManager uses the MySQL Connector/J driver to connect to
 * your local MySQL instance.
 *
 * In our example, we will create a DAO (data access object) java
 * class to interact with each MySQL table. The DAO java classes will
 * use ConnectionManager to open and close connections.
 */
public class ConnectionManager {

  /**
   * Private default constructor to prevent instantiation.
   */
  private ConnectionManager() { }

  // User to connect to your database instance. By default, this is "root".
  private static final String USER = "root";
  // Password for the user.
  private static final String PASSWORD = "password"; // Replace with your actual password
  // URI to your database server. If running on the same machine, then
  // this is "localhost".
  private static final String HOSTNAME = "localhost";
  // Port to your database server. By default, this is 3306.
  private static final int PORT = 3306;
  // Name of the MySQL schema that contains your tables.
  private static final String SCHEMA = "CS5200Project";
  // Default timezone for MySQL server.
  private static final String TIMEZONE = "UTC";

  /** Get the connection to the database instance. */
  public static Connection getConnection() throws SQLException {
    Connection connection = null;
    try {
      Properties connectionProperties = new Properties();
      connectionProperties.put("user", USER);
      connectionProperties.put("password", PASSWORD);
      connectionProperties.put("serverTimezone", TIMEZONE);
      
      // Ensure the JDBC driver is loaded by retrieving the runtime
      // Class descriptor.
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new SQLException(e);
      }
      connection = DriverManager.getConnection(
        String.format(
          "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true",
          HOSTNAME,
          PORT,
          SCHEMA
        ),
        connectionProperties
      );
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    return connection;
  }

  /**
   * Open and return a connection to the database server that is not
   * associated with a particular schema. Used for creating/dropping schemas.
   */
  public static Connection getSchemalessConnection() throws SQLException {
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", USER);
    connectionProperties.put("password", PASSWORD);
    connectionProperties.put("serverTimezone", TIMEZONE);
    
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new SQLException(e);
    }
    
    return DriverManager.getConnection(
      String.format(
        "jdbc:mysql://%s:%d?useSSL=false&allowPublicKeyRetrieval=true",
        HOSTNAME,
        PORT
      ),
      connectionProperties
    );
  }
} 