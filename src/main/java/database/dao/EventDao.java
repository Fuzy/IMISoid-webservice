package database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.connection.ConnectionManager;
import database.lib.BArchivLibrary;
import database.lib.DatabaseStoredProcedures;
import exceptions.FormTriggerFailureException;

import model.Event;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;
import static database.DatabaseUtility.*;
import static utilities.Util.*;

/**
 * DAO pro přístup do databáze.
 * 
 * @author KDA
 * 
 */
public class EventDao {
  private static final String TABLE_EVENT = "karta";
  private static final String SQL_DELETE = "delete from " + TABLE_EVENT + " where rowid like ?";
  private static final String SQL_GET_EVENT = "select rowid, t.* from " + TABLE_EVENT
      + " t rowid like  ? ";
  private static final String SQL_GET_EVENTS = "select rowid, t.* from " + TABLE_EVENT
      + " t where icp like ? " + "and datum >=  ? and datum <=  ? ";
  private static final String SQL_INSERT = "insert into " + TABLE_EVENT
      + " (icp, datum, kod_po, druh, cas, ic_obs, typ, datum_zmeny, poznamka) "
      + "values (?, ?, ?, ?, ?, ?, ?, ?, ?) returning ROWID into ?";
  private static final String SQL_UPDATE = "update "
      + TABLE_EVENT
      + " set icp=?, datum=?, kod_po=?, druh=?, cas=?, ic_obs=?, typ=?, datum_zmeny=?, poznamka=?  where rowid=?";

  private static ConnectionManager connectionManager;
  private static Connection conn = null;

  static {
    connectionManager = new ConnectionManager();
  }

  public static Connection getConnection() throws SQLException {
    if (conn != null && conn.isClosed() != true) {
      System.out.println("EventDao spojeni existuje");
      return conn;
    }
    System.out.println("EventDao nove spojeni");
    return connectionManager.getConnection();
  }

  public static String processCreateEvent(Event event) throws Exception,
      FormTriggerFailureException {
    String rowid = null;
    try {

      conn = getConnection();
      conn.setAutoCommit(false);
      applyPreInsertBussinesLogic(event);
      rowid = createEvent(event);
      applyPostInsertBussinesLogic(event);
      conn.commit();
    }
    catch (Exception e) {
      conn.rollback();
      e.printStackTrace();
    }
    finally {
      closeConnection(conn, null, null);
    }

    return rowid;
  }

  public static String createEvent(Event event) throws SQLException, FormTriggerFailureException {
    Connection conn = null;
    OraclePreparedStatement stmt = null;
    ResultSet rset = null;
    int affectedRows = 0;
    String rowid = null;

    Object[] values = event.getEventAsArrayOfObjects();

    try {
      conn = getConnection();
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

  private static void applyPreInsertBussinesLogic(Event event) throws SQLException,
      FormTriggerFailureException {
    boolean lzeVlozit = BArchivLibrary.lzeVlozit(event.getIcp(), event.getDatum(), conn);
    if (lzeVlozit == false) {
      throw new FormTriggerFailureException(
          "Nelze vložit záznam s datem neodpovídajícím pracovnímu poměru.");
    }
    // nastavit typ 'O' nebo 'P' - asi klient

  }

  private static void applyPostInsertBussinesLogic(Event event) throws SQLException {
    long yesterday = getPreviousDay(event.getDatum());
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(yesterday), event.getIcp(), conn);
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(event.getDatum()), event.getIcp(), conn);
  }

  public static boolean processDeleteEvent(String rowid) throws Exception {
    boolean result;
    Event event = getEvent(rowid);
    if (event.getTyp().equals("O")) {
      event.setTyp("S");
      updateEvent(event);
      result = true;
    }
    else {
      result = deleteEvent(rowid);
    }
    applyPostDeleteBussinesLogic(event);
    return result;
  }

  private static void applyPostDeleteBussinesLogic(Event event) throws SQLException {
    long yesterday = getPreviousDay(event.getDatum());
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(yesterday), event.getIcp(), conn);
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(event.getDatum()), event.getIcp(), conn);
  }

  public static boolean deleteEvent(String rowid) throws SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getConnection();
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
      closeConnection(conn, stmt, null);
    }
    return false;
  }

  public static Event getEvent(String rowid) throws SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;

    try {
      conn = getConnection();

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
      closeConnection(conn, stmt, rset);
    }

    return null;
  }

  public static List<Event> getEvents(String icp, String dateFrom, String dateTo)
      throws SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Event> events = new ArrayList<Event>();

    try {
      conn = getConnection();

      stmt = conn.prepareStatement(SQL_GET_EVENTS);
      stmt.setString(1, icp);
      stmt.setString(2, dateFrom);
      stmt.setString(3, dateTo);
      rset = stmt.executeQuery();

      while (rset.next()) {
        Event event = Event.resultSetToEvent(rset);
        events.add(event);
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(conn, stmt, rset);
    }

    for (Event event : events) {
      System.out.println(event);
    }
    return events;
  }

  public static boolean processUpdateEvent(Event event) throws Exception {

    try {
      conn = getConnection();
      conn.setAutoCommit(false);
      if (event.getTyp().equals("O")) {
        Event copy = new Event(event);
        copy.setTyp("S");
        createEvent(copy);
      }
      else {
        updateEvent(event);
      }
      applyPostUpdateBussinesLogic(event);
      conn.commit();
    }
    catch (Exception e) {
      conn.rollback();
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(conn, null, null);
    }

    return true;
  }

  public static boolean updateEvent(Event event) throws SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;

    Object[] values = event.getEventAsArrayOfObjects();
    try {
      conn = getConnection();
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
      closeConnection(conn, stmt, null);
    }

    return false;
  }

  private static void applyPostUpdateBussinesLogic(Event event) throws SQLException {
    long yesterday = getPreviousDay(event.getDatum());
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(yesterday), event.getIcp(), conn);
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(event.getDatum()), event.getIcp(), conn);
  }

  private static void setValues(PreparedStatement preparedStatement, Object... values)
      throws SQLException {
    for (int i = 0; i < values.length; i++) {
      preparedStatement.setObject(i + 1, values[i]);
    }
  }
}
