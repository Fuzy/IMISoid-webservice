package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility methods for work with database.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class DatabaseUtility {
  // private static Logger log = Logger.getLogger("imisoid");

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

  public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columns = rsmd.getColumnCount();
    for (int x = 1; x <= columns; x++) {
      if (columnName.equals(rsmd.getColumnName(x))) {
        return true;
      }
    }
    return false;
  }

}
