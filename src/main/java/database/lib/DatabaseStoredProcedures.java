package database.lib;

import static database.DatabaseUtility.closeConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Stores method for processing attendance events. 
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class DatabaseStoredProcedures {

  public static void ccap_denni_zaznamy(java.sql.Date datum_m, String icp_m, Connection conn)
      throws SQLException {
    PreparedStatement stmt = null;

    String ccap_denni_zaznamy = "call ccap.ccap_denni_zaznamy(?, ?)";

    try {
      stmt = conn.prepareStatement(ccap_denni_zaznamy);
      stmt.setQueryTimeout(5);
      stmt.setDate(1, datum_m);
      stmt.setString(2, icp_m);
      stmt.executeUpdate();
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, null);
    }

  }

}
