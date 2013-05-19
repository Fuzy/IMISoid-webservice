package database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import exceptions.ClientErrorException;

import model.Event;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;
import static database.DatabaseUtility.*;

/**
 * DAO pro přístup do databáze.
 * 
 * @author KDA
 * 
 */
public class EventDao {
  private static Logger log = Logger.getLogger("imisoid");
  private static final String TABLE_EVENT = "karta";
  private static final String SQL_DELETE = "delete from " + TABLE_EVENT + " where rowid like ?";
  private static final String SQL_GET_EVENT = "select t.rowid, t.* from " + TABLE_EVENT
      + " t where t.rowid like ?";
  private static final String SQL_GET_EVENTS = "select rowid, t.* from " + TABLE_EVENT
      + " t where icp like ? " + "and datum >=  ? and datum <=  ? " + "and typ not like 'S'";
  private static final String SQL_INSERT = "insert into " + TABLE_EVENT
      + " (icp, datum, kod_po, druh, cas, ic_obs, typ, datum_zmeny, poznamka) "
      + "values (?, ?, ?, ?, ?, ?, ?, ?, ?) returning ROWID into ?";
  private static final String SQL_UPDATE = "update "
      + TABLE_EVENT
      + " set icp=?, datum=?, kod_po=?, druh=?, cas=?, ic_obs=?, typ=?, datum_zmeny=?, poznamka=?  where rowid=?";

  public static String createEvent(Event event, Connection conn) throws SQLException,
  ClientErrorException {
    // Connection conn = null;
    OraclePreparedStatement stmt = null;
    ResultSet rset = null;
    int affectedRows = 0;
    String rowid = null;

    Object[] values = event.eventAsArrayOfObjects();

    try {
      // conn = getConnection();
      stmt = (OraclePreparedStatement) conn.prepareStatement(SQL_INSERT);
      setValues(stmt, values);
      stmt.registerReturnParameter(10, OracleTypes.VARCHAR, 100);
      affectedRows = stmt.executeUpdate();

      if (affectedRows > 0) {
        rset = stmt.getReturnResultSet(); // rest is not null and not empty

        while (rset.next()) {
          rowid = rset.getString(1);
        }
      }

    }
    catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }

    System.out.println("affectedRows: " + affectedRows + " rowid: " + rowid);

    return rowid;
  }

  public static boolean deleteEvent(String rowid, Connection conn) throws SQLException {
    log.info("");
    // Connection conn = null;
    PreparedStatement stmt = null;

    try {
      // conn = getConnection();
      stmt = conn.prepareStatement(SQL_DELETE);
      stmt.setString(1, rowid);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 1) {
        return true;
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, null);
    }
    return false;
  }

  public static Event getEvent(String rowid, Connection conn) throws SQLException {
    // Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;

    try {
      // conn = getConnection();
      stmt = conn.prepareStatement(SQL_GET_EVENT);
      stmt.setString(1, rowid);
      rset = stmt.executeQuery();
      if (rset.next()) {
        return Event.resultSetToEvent(rset);
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }
    log.info("return null");
    return null;
  }

  public static List<Event> getEvents(String icp, String dateFrom, String dateTo, Connection conn)
      throws SQLException {
    log.info("");
    // Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Event> events = new ArrayList<Event>();

    try {
      // conn = getConnection();

      stmt = conn.prepareStatement(SQL_GET_EVENTS);
      stmt.setString(1, icp);
      stmt.setString(2, dateFrom);
      stmt.setString(3, dateTo);
      rset = stmt.executeQuery();

      while (rset.next()) {
        Event event = Event.resultSetToEvent(rset);
        log.info(""+event);
        events.add(event);
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
    return events;
  }

  public static boolean updateEvent(Event event, Connection conn) throws SQLException {
    log.info("");
    // Connection conn = null;
    PreparedStatement stmt = null;

    Object[] values = event.eventAsArrayOfObjects();
    try {
      // conn = getConnection();
      stmt = conn.prepareStatement(SQL_UPDATE);
      setValues(stmt, values);
      stmt.setString(10, event.getServer_id());
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 1) {
        return true;
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, null);
    }

    return false;
  }

  private static void setValues(PreparedStatement preparedStatement, Object... values)
      throws SQLException {
    for (int i = 0; i < values.length; i++) {
      preparedStatement.setObject(i + 1, values[i]);
    }
  }
}
