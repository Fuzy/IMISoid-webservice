package database.connection;

import static database.Credentials.password;
import static database.Credentials.url;
import static database.Credentials.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
  private static Connection conn = null;
  
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
    if (conn != null && conn.isClosed() != true) {
      System.out.println("spojeni existuje");
      return conn;
    }
    System.out.println("nove spojeni");
    return DriverManager.getConnection(url, user, password);
  }// TODO connection factory
}
