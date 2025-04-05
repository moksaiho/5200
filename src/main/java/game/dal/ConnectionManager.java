package game.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
	  // User to connect to your database instance. By default, this is "root2".
	  private static final String USER = "root";
	  // Password for the user.
	  private static final String PASSWORD = "muxihe123.";
	  // URI to your database server. If running on the same machine, then
	  // this is "localhost".
	  private static final String HOSTNAME = "localhost";
	  // Port to your database server. By default, this is 3307.
	  private static final int PORT = 3306;
	  // Name of the MySQL schema that contains your tables.
	  private static final String SCHEMA = "CS5200Project";
	  // Default timezone for MySQL server.
	  private static final String TIMEZONE = "UTC";

  public static Connection getConnection() throws SQLException {
    Connection connection = null;
    try {
      Properties props = new Properties();
      props.put("user", USER);
      props.put("password", PASSWORD);
      props.put("serverTimezone", TIMEZONE);
      Class.forName("com.mysql.cj.jdbc.Driver");
      String jdbcUrl = String.format(
          "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true",
          HOSTNAME, PORT, SCHEMA
      );
      connection = DriverManager.getConnection(jdbcUrl, props);
      return connection;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new SQLException("MySQL JDBC Driver not found", e);
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
  }
  public static Connection getSchemalessConnection() throws SQLException {
	  Connection connection = null;
	  try {
	    Properties props = new Properties();
	    props.put("user", USER);
	    props.put("password", PASSWORD);
	    props.put("serverTimezone", TIMEZONE);
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    String jdbcUrl = String.format(
	        "jdbc:mysql://%s:%d?useSSL=false&allowPublicKeyRetrieval=true",
	        HOSTNAME, PORT
	    );
	    connection = DriverManager.getConnection(jdbcUrl, props);
	    return connection;
	  } catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    throw new SQLException("MySQL JDBC Driver not found", e);
	  } catch (SQLException e) {
	    e.printStackTrace();
	    throw e;
	  }
	}
}
