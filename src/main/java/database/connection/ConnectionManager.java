package database.connection;

import static database.connection.Credentials.password;
import static database.connection.Credentials.url;
import static database.connection.Credentials.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectionManager {
  private static Logger log = Logger.getLogger("imisoid");
  //private static Connection conn = null;
  
  public ConnectionManager() {
  }

  static {
    try {
      Class.forName("oracle.jdbc.OracleDriver");
    }
    catch (ClassNotFoundException e) {
      // TODO sysexit
      e.printStackTrace();
    }

  }
  
  public Connection getConnection() throws SQLException {
    /*if (conn != null && conn.isClosed() != true) {
      log.info("spojeni existuje");
      return conn;
    }*/
    //log.info("Vytvarim spojeni");
    return DriverManager.getConnection(url, user, password);
  }// TODO connection factory
}
