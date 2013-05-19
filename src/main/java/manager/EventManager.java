package manager;

import static database.DatabaseUtility.closeConnection;
import static utilities.Util.getPreviousDay;
import static utilities.Util.longToDate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import model.Event;

import database.connection.ConnectionManager;
import database.dao.EventDao;
import database.lib.BArchivLibrary;
import database.lib.DatabaseStoredProcedures;
import exceptions.ClientErrorException;

public class EventManager {
  private static Logger log = Logger.getLogger("imisoid");

  private static ConnectionManager connectionManager;
  private static Connection conn = null;

  static {
    connectionManager = new ConnectionManager();
  }

  public static Connection getConnection() throws SQLException {
    if (conn != null && conn.isClosed() != true) {
      log.info("spojeni existuje");
      return conn;
    }
    log.info("---Zacinam spojeni---");
    return connectionManager.getConnection();
  }

  public static String processCreateEvent(Event event) throws Exception
       {
    log.info("");
    String rowid = null;
    try {

      conn = getConnection();
      conn.setAutoCommit(false);
      applyPreInsertBussinesLogic(event);
      rowid = EventDao.createEvent(event, conn);
      applyPostInsertBussinesLogic(event);
      conn.commit();
    }
    catch (Exception e) {
      conn.rollback();
      //e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(conn, null, null);
    }

    return rowid;
  }

  private static void applyPreInsertBussinesLogic(Event event) throws SQLException,
  ClientErrorException {
    log.info("");
    boolean lzeVlozit = BArchivLibrary.lzeVlozit(event.getIcp(), event.getDatum(), conn);
    if (lzeVlozit == false) {
      throw new ClientErrorException(
          "Nelze vložit záznam s datem neodpovídajícím pracovnímu poměru.");
    }
    // nastavit typ 'O' nebo 'P' - asi klient

  }

  private static void applyPostInsertBussinesLogic(Event event) throws SQLException {
    log.info("");
    long yesterday = getPreviousDay(event.getDatum());
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(yesterday), event.getIcp(), conn);
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(event.getDatum()), event.getIcp(), conn);
  }
  
  public static List<Event> processGetEvents(String icp, String dateFrom, String dateTo) throws Exception {
    log.info("");
    conn = getConnection();
    List<Event> events = EventDao.getEvents(icp, dateFrom, dateTo, conn);
    closeConnection(conn, null, null);
    return events;
  }
  
  public static boolean processDeleteEvent(String rowid) throws Exception {
    log.info("");
    boolean result;
    conn = getConnection();
    log.info("getEvent");
    Event event = EventDao.getEvent(rowid, conn);
    if (event == null) {
      closeConnection(conn, null, null);
      return false; //neexistuje
    }
    if (event.getTyp().equals("O")) {
      event.setTyp("S");
      log.info("updateEvent");
      EventDao.updateEvent(event, conn);
      result = true;
    }
    else {
      log.info("deleteEvent");
      result = EventDao.deleteEvent(rowid, conn);
    }
    applyPostDeleteBussinesLogic(event);
    closeConnection(conn, null, null);
    //TODO bolean divny
    return result;
  }

  private static void applyPostDeleteBussinesLogic(Event event) throws SQLException {
    log.info("");
    long yesterday = getPreviousDay(event.getDatum());
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(yesterday), event.getIcp(), conn);
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(event.getDatum()), event.getIcp(), conn);
  }
  
  public static boolean processUpdateEvent(Event event) throws Exception {
    log.info("");
    try {
      conn = getConnection();
      conn.setAutoCommit(false);
      if (event.getTyp().equals("O")) {
        Event copy = new Event(event);
        copy.setTyp("S");
        EventDao.createEvent(copy, conn);
      }
      else {
        EventDao.updateEvent(event, conn);
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
  
  private static void applyPostUpdateBussinesLogic(Event event) throws SQLException {
    log.info("");
    long yesterday = getPreviousDay(event.getDatum());
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(yesterday), event.getIcp(), conn);
    DatabaseStoredProcedures.ccap_denni_zaznamy(longToDate(event.getDatum()), event.getIcp(), conn);
  }
  
  
  
  
  
  
  

}
