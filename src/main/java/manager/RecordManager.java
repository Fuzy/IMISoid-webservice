package manager;

import static database.DatabaseUtility.closeConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import model.Record;

import database.connection.ConnectionManager;
import database.dao.RecordsDao;

public class RecordManager {

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
  
  public static List<Record> processGetRecords(String username, String dateFrom, String dateTo) throws Exception {
    log.info("");
    conn = getConnection();
    List<Record> records = RecordsDao.getRecords(username, dateFrom, dateTo, conn);
    closeConnection(conn, null, null);
    return records;
  }
}
