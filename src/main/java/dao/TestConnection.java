package dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import static dao.Credentials.*;
import static dao.DatabaseUtility.closeConnection;

import oracle.jdbc.OracleConnection;

public class TestConnection {
  
  public static String testConnection() throws Exception {
    OracleConnection conn = null;
    Statement stmt = null;
    ResultSet rset = null;
    try {
      Class.forName("oracle.jdbc.OracleDriver");
      conn = (OracleConnection) DriverManager.getConnection(url, user, password);

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
