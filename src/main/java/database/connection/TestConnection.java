package database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static database.DatabaseUtility.closeConnection;
import static database.connection.Credentials.*;

public class TestConnection {
  
  public static String testConnection() throws Exception {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rset = null;
    try {
      Class.forName("oracle.jdbc.OracleDriver");
      conn = DriverManager.getConnection(url, user, password);

      stmt = conn.createStatement();
      rset = stmt.executeQuery("SELECT * FROM v$version WHERE banner LIKE 'Oracle%'");

      while (rset.next()) {
        return rset.getString(1); // Print col 1
      }
    } catch (Exception e) {
      throw e;
    }
    finally {
      closeConnection(conn, stmt, rset);
    }
    return null;
  }

}
