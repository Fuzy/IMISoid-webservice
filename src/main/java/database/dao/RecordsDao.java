package database.dao;

import static database.DatabaseUtility.closeConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Record;

public class RecordsDao {
  private static Logger log = Logger.getLogger("imisoid");
  private static final String SQL_GET_RECORDS = "select t.id, t.datum, t.kodpra, t.stav_v, t.zc, " +
  		"t.cpolzak, t.cpozzak, t.mnozstvi_odved, t.pozn_hl, t.pozn_ukol, t.poznamka " +
  		"from den_vykaz t where kodpra like ? and datum >=  ? and datum <=  ?";
  
  public static List<Record> getRecords(String username, String dateFrom, String dateTo, Connection conn)
      throws SQLException {
    log.info("");
    // Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Record> records = new ArrayList<Record>();

    try {
      // conn = getConnection();

      stmt = conn.prepareStatement(SQL_GET_RECORDS);
      stmt.setString(1, username);
      stmt.setString(2, dateFrom);
      stmt.setString(3, dateTo);
      rset = stmt.executeQuery();
      log.info("executeQuery");
      while (rset.next()) {
        Record record = Record.resultSetToRecord(rset);
        records.add(record);
        System.out.println(record);
      }
    }
    catch (SQLException e) {
      log.warning(e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }
    /*for (Event event : events) {
      System.out.println(event);
    }*/  
    return records;
  }

}
