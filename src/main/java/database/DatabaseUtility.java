package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtility {
  
  public static void closeConnection(Connection conn, Statement stmt, ResultSet rset)
      throws SQLException {
    if (rset != null) {
      rset.close();
    }
    if (stmt != null) {
      stmt.close();
    }
    if (conn != null) {
      conn.close();
    }
  }
  
}
