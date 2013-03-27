package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseUtility {
  private static Logger log = Logger.getLogger("imisoid");
  
  public static void closeConnection(Connection conn, Statement stmt, ResultSet rset)
      throws SQLException {
    if (rset != null) {
      rset.close();
    }
    if (stmt != null) {
      stmt.close();
    }
    if (conn != null) {
      log.info("---Koncim spojeni---");
      conn.close();
    }
  }
  
}
