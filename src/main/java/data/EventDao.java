package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;
import static data.Credentials.*;

/**
 * DAO pro přístup do databáze.
 * 
 * @author KDA
 * 
 */
public class EventDao {
  private static final String TABLE_EVENT = "karta";
  private static final String SQL_DELETE = "delete from " + TABLE_EVENT + " where rowid like ?";
  private static final String SQL_GET = "select rowid, t.* from " + TABLE_EVENT
      + " t where icp like ? " + "and datum >=  ? and datum <=  ? ";
  private static final String SQL_INSERT = "insert into " + TABLE_EVENT
      + " (icp, datum, kod_po, druh, cas, ic_obs, typ, datum_zmeny, poznamka) "
      + "values (?, ?, ?, ?, ?, ?, ?, ?, ?) returning ROWID into ?";
  private static final String SQL_UPDATE = "update "
      + TABLE_EVENT
      + " set icp=?, datum=?, kod_po=?, druh=?, cas=?, ic_obs=?, typ=?, datum_zmeny=?, poznamka=?  where rowid=";

  static {
    try {
      Class.forName("oracle.jdbc.OracleDriver");
    }
    catch (ClassNotFoundException e) {
      // TODO sysexit
      e.printStackTrace();
    }

  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }

  public static String createEvent(Event event) {
    Connection conn = null;
    OraclePreparedStatement stmt = null;
    ResultSet rset = null;
    int affectedRows = 0;
    String rowid = "";

    Object[] values = { event.getIcp(), event.getDatum(), event.getKod_po(), event.getDruh(),
        event.getCas(), event.getIc_obs(), event.getTyp(), event.getDatum_zmeny(),
        event.getPoznamka() };

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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("affectedRows: " + affectedRows + " rowid: " + rowid);

    return rowid;
  }

  public static boolean deleteEvent(String rowid) {
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return false;
  }

  public static List<Event> getEvents(String icp, String dateFrom, String dateTo)
      throws SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Event> events = new ArrayList<Event>();

    try {
      conn = getConnection();

      stmt = conn.prepareStatement(SQL_GET);
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
    }
    finally {
      closeConnection(conn, stmt, rset);
    }

    for (Event event : events) {
      System.out.println(event);
    }
    return events;
  }
  
  public static boolean updateEvent(Event event) {
    Connection conn = null;
    PreparedStatement stmt = null;
    
    Object[] values = { event.getIcp(), event.getDatum(), event.getKod_po(), event.getDruh(),
        event.getCas(), event.getIc_obs(), event.getTyp(), event.getDatum_zmeny(),
        event.getPoznamka() };

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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return false;
  }

  private static void setValues(PreparedStatement preparedStatement, Object... values)
      throws SQLException {
    for (int i = 0; i < values.length; i++) {
      preparedStatement.setObject(i + 1, values[i]);
    }
  }

  private static void closeConnection(Connection conn, Statement stmt, ResultSet rset)
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
