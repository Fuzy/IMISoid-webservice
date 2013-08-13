package database.connection;

import static database.connection.Credentials.password;
import static database.connection.Credentials.url;
import static database.connection.Credentials.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Creates new connections to database.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class ConnectionManager {
  private static Logger log = Logger.getLogger("imisoid");
  
  public ConnectionManager() {
  }

  static {
    try {
      Class.forName("oracle.jdbc.OracleDriver");
      log.info("oracle.jdbc.OracleDriver loaded");
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

  }
  
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }
}
